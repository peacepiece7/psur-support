<script setup lang="ts">
  import type { ApiResponseListUserListResponse } from '~/types/models/ApiResponseListUserListResponse'
  import type { ApiResponseListUserRoleResponse } from '~/types/models/ApiResponseListUserRoleResponse'
  import type { ApiResponseListRoleResponse } from '~/types/models/ApiResponseListRoleResponse'
  import type { ApiResponseVoid } from '~/types/models/ApiResponseVoid'
  import type { ApiResponseUserRoleResponse } from '~/types/models/ApiResponseUserRoleResponse'
  import type { UserListResponse } from '~/types/models/UserListResponse'
  import type { UserRoleResponse } from '~/types/models/UserRoleResponse'
  import type { RoleResponse } from '~/types/models/RoleResponse'
  import type { UserRoleAssignRequest } from '~/types/models/UserRoleAssignRequest'
  import { API_BASE_URL } from '~/constants/url'

  // 로그인 상태 확인
  const authStore = useAuthStore()
  const isLoggedIn = computed(() => authStore.isLoggedIn)

  if (!isLoggedIn.value) {
    await navigateTo('/')
  }

  /** 테이블 행: userId 기준 그룹 + 권한 목록 */
  type UserRoleRow = {
    id: number
    userId: number
    roles: UserRoleResponse[]
  }

  const tableHeaders = [
    { title: '사용자 ID', key: 'userId', width: 120 },
    { title: '권한요약', key: 'roleSummary', sortable: false },
  ]

  const loading = ref(false)
  const userRoleRows = ref<UserRoleRow[]>([])
  const dialogOpen = ref(false)
  const selectedUserId = ref<number | null>(null)
  const dialogUserRoles = ref<UserRoleResponse[]>([])
  const dialogLoading = ref(false)
  const allRoles = ref<RoleResponse[]>([])
  const rolesLoading = ref(false)
  const selectedRoleId = ref<string | null>(null)
  const assignLoading = ref(false)
  const deleteLoadingMap = ref<Record<string, boolean>>({})

  // 신규 사용자 추가 다이얼로그
  const addUserDialogOpen = ref(false)
  const addUserList = ref<UserListResponse[]>([])
  const addUserSelectedUserId = ref<string | null>(null)
  const addUserRolesForSelect = ref<RoleResponse[]>([])
  const addUserDialogLoading = ref(false)
  const addUserSubmitLoading = ref(false)

  /** 권한 목록 조회 (userId 없으면 전체) */
  const fetchUserRoles = async (userId?: number) => {
    const res = await $fetch<ApiResponseListUserRoleResponse>(
      `${API_BASE_URL}/user-roles`,
      {
        method: 'GET',
        credentials: 'include',
        query: {
          ...(userId != null ? { userId } : {}),
          includeInactive: true,
        },
      },
    )
    return res.data ?? []
  }

  /** 역할 목록 조회 */
  const fetchRoles = async () => {
    const res = await $fetch<ApiResponseListRoleResponse>(
      `${API_BASE_URL}/roles`,
      {
        method: 'GET',
        credentials: 'include',
        query: { includeInactive: true },
      },
    )
    return res.data ?? []
  }

  /** 테이블용 전체 사용자·권한 로드 (userId로 그룹화) */
  const loadTableData = async () => {
    loading.value = true
    try {
      const list = await fetchUserRoles()
      const byUser = new Map<number, UserRoleResponse[]>()
      for (const ur of list) {
        if (ur.userId == null) continue
        const active = ur.isActive !== false
        if (!active) continue
        if (!byUser.has(ur.userId)) byUser.set(ur.userId, [])
        byUser.get(ur.userId)!.push(ur)
      }
      userRoleRows.value = Array.from(byUser.entries()).map(
        ([userId, roles]) => ({
          id: userId,
          userId,
          roles,
        }),
      )
    } finally {
      loading.value = false
    }
  }

  /** 행 클릭 → 다이얼로그 열기 */
  const openDialog = async (item: UserRoleRow) => {
    selectedUserId.value = item.userId
    dialogOpen.value = true
    dialogUserRoles.value = []
    selectedRoleId.value = null
    dialogLoading.value = true
    try {
      dialogUserRoles.value = await fetchUserRoles(item.userId)
      if (allRoles.value.length === 0) {
        rolesLoading.value = true
        try {
          allRoles.value = await fetchRoles()
        } finally {
          rolesLoading.value = false
        }
      }
    } finally {
      dialogLoading.value = false
    }
  }

  const closeDialog = () => {
    dialogOpen.value = false
    selectedUserId.value = null
    dialogUserRoles.value = []
    selectedRoleId.value = null
  }

  /** 다이얼로그에서 권한 추가 (roleIdStr: Select에서 선택한 값) */
  const assignRole = async (roleIdStr?: string | null) => {
    const uid = selectedUserId.value
    const idStr = roleIdStr ?? selectedRoleId.value
    if (uid == null || idStr == null || idStr === '') return
    const roleId = Number(idStr)
    if (Number.isNaN(roleId)) return
    const already = dialogUserRoles.value.some(
      (r) => r.roleId === roleId && r.isActive !== false,
    )
    if (already) {
      selectedRoleId.value = null
      return
    }
    assignLoading.value = true
    selectedRoleId.value = null
    try {
      const body: UserRoleAssignRequest = { userId: uid, roleId }
      const res = await $fetch<ApiResponseUserRoleResponse>(
        `${API_BASE_URL}/user-roles`,
        {
          method: 'POST',
          credentials: 'include',
          body,
        },
      )
      if (res.data) {
        dialogUserRoles.value = [...dialogUserRoles.value, res.data]
      }
      await loadTableData()
    } finally {
      assignLoading.value = false
    }
  }

  /** 다이얼로그에서 권한 삭제 */
  const removeRole = async (userRole: UserRoleResponse) => {
    const uid = selectedUserId.value
    const roleId = userRole.roleId
    if (uid == null || roleId == null) return
    const loadingKey = `${uid}-${roleId}`
    deleteLoadingMap.value = { ...deleteLoadingMap.value, [loadingKey]: true }
    try {
      await $fetch<ApiResponseVoid>(`${API_BASE_URL}/user-roles`, {
        method: 'DELETE',
        credentials: 'include',
        query: { userId: uid, roleId },
      })
      dialogUserRoles.value = dialogUserRoles.value.filter(
        (r) => !(r.roleId === roleId && r.userId === uid),
      )
      await loadTableData()
    } finally {
      deleteLoadingMap.value = {
        ...deleteLoadingMap.value,
        [loadingKey]: false,
      }
    }
  }

  /** 테이블 행의 권한 요약 텍스트 */
  const roleSummaryText = (row: UserRoleRow) =>
    row.roles.map((r) => r.roleName ?? r.roleCode ?? '-').join(', ') || '-'

  /** 사용자 목록 조회 (GET /users) */
  const fetchUserList = async () => {
    const res = await $fetch<ApiResponseListUserListResponse>(
      `${API_BASE_URL}/users`,
      {
        method: 'GET',
        credentials: 'include',
        query: { includeInactive: true },
      },
    )
    return res.data ?? []
  }

  /** 신규 사용자 추가 다이얼로그 열기: 사용자 리스트·역할 리스트 로드 */
  const openAddUserDialog = async () => {
    addUserDialogOpen.value = true
    addUserSelectedUserId.value = null
    addUserDialogLoading.value = true
    try {
      const [users, roles] = await Promise.all([
        fetchUserList(),
        fetchRoles(),
      ])
      addUserList.value = users
      addUserRolesForSelect.value = roles
    } finally {
      addUserDialogLoading.value = false
    }
  }

  const closeAddUserDialog = () => {
    addUserDialogOpen.value = false
    addUserSelectedUserId.value = null
    addUserList.value = []
    addUserRolesForSelect.value = []
  }

  /** role 리스폰스의 첫 번째 역할 (GET /roles 의 첫 번째 값) */
  const firstRole = computed(() => addUserRolesForSelect.value[0] ?? null)

  /** 신규 사용자 추가: 선택한 사용자에게 첫 번째 권한 부여 */
  const submitAddUser = async () => {
    const userIdStr = addUserSelectedUserId.value
    const role = firstRole.value
    if (userIdStr == null || userIdStr === '' || role?.id == null) return
    const userId = Number(userIdStr)
    if (Number.isNaN(userId)) return
    addUserSubmitLoading.value = true
    try {
      const body: UserRoleAssignRequest = { userId, roleId: role.id }
      await $fetch<ApiResponseUserRoleResponse>(`${API_BASE_URL}/user-roles`, {
        method: 'POST',
        credentials: 'include',
        body,
      })
      await loadTableData()
      closeAddUserDialog()
    } finally {
      addUserSubmitLoading.value = false
    }
  }

  /** 신규 사용자 추가 다이얼로그: 사용자 Select 옵션 (GET /users 결과) */
  const addUserSelectOptions = computed(() =>
    addUserList.value
      .filter((u) => u.id != null)
      .map((u) => ({
        title: `${u.username ?? u.loginId ?? '-'} (ID: ${u.id})`,
        value: String(u.id!),
      })),
  )

  /** Select 옵션: 아직 부여되지 않은 역할만 */
  const assignableRoleOptions = computed(() => {
    const currentIds = new Set(
      dialogUserRoles.value
        .filter((r) => r.isActive !== false)
        .map((r) => r.roleId),
    )
    return allRoles.value
      .filter((r) => r.id != null && !currentIds.has(r.id))
      .map((r) => ({
        title: r.roleName ?? r.roleCode ?? String(r.id),
        value: String(r.id!),
      }))
  })

  onMounted(() => {
    loadTableData()
  })
</script>

<template>
  <div class="relative min-h-screen w-full">
    <PageHeaderSection
      title="관리"
      image-src="/main_banner.png"
      image-alt="관리 페이지 헤더 이미지"
      type="infoPage"
    />

    <div class="bg-neutral-fill-50 min-h-screen p-8">
      <div class="mx-auto max-w-[1400px]">
        <div
          class="border-neutral-stroke-300 overflow-hidden rounded-lg border bg-white"
        >
          <div
            class="border-neutral-stroke-300 bg-neutral-fill-50 border-b px-4 py-3"
          >
            <h2 class="text-neutral-text-title text-lg font-bold">
              사용자 권한 관리
            </h2>
            <p class="text-neutral-text-caption mt-1 text-sm">
              행을 클릭하면 상세 정보 및 권한 수정이 가능합니다.
            </p>
            <div class="mt-3">
              <Button
                color="primary"
                variant="fill"
                size="sm"
                @click="openAddUserDialog"
              >
                신규 사용자 추가
              </Button>
            </div>
          </div>

          <div class="p-4">
            <div v-if="loading" class="flex justify-center py-12">
              <span class="text-neutral-text-caption text-sm"
                >데이터를 불러오는 중...</span
              >
            </div>

            <template v-else>
              <div
                class="border-neutral-stroke-300 overflow-hidden rounded-lg border"
              >
                <table class="w-full text-left text-sm">
                  <thead class="border-neutral-stroke-300 bg-neutral-fill-100">
                    <tr>
                      <th
                        v-for="h in tableHeaders"
                        :key="h.key"
                        class="border-neutral-stroke-300 border-r border-b px-4 py-3 last:border-r-0"
                        :style="h.width ? { width: `${h.width}px` } : {}"
                      >
                        {{ h.title }}
                      </th>
                    </tr>
                  </thead>
                  <tbody>
                    <tr
                      v-for="row in userRoleRows"
                      :key="row.id"
                      class="border-neutral-stroke-200 hover:bg-neutral-fill-100 cursor-pointer border-b transition-colors last:border-b-0"
                      @click="openDialog(row)"
                    >
                      <td
                        class="border-neutral-stroke-200 border-r px-4 py-3 last:border-r-0"
                      >
                        사용자 ID: {{ row.userId }}
                      </td>
                      <td
                        class="border-neutral-stroke-200 border-r px-4 py-3 last:border-r-0"
                      >
                        {{ roleSummaryText(row) }}
                      </td>
                    </tr>
                    <tr
                      v-if="userRoleRows.length === 0"
                      class="border-neutral-stroke-200 border-b"
                    >
                      <td
                        colspan="2"
                        class="text-neutral-text-caption px-4 py-8 text-center"
                      >
                        조회된 사용자 권한이 없습니다.
                      </td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </template>
          </div>
        </div>
      </div>
    </div>

    <!-- 신규 사용자 추가 다이얼로그 -->
    <Dialog
      v-model="addUserDialogOpen"
      max-width="480"
      @update:model-value="(v) => !v && closeAddUserDialog()"
    >
      <template #title>
        <span class="text-neutral-text-title text-lg font-bold">
          신규 사용자 추가
        </span>
      </template>
      <template #close-icon="{ close }">
        <Button color="inverse" size="xs" variant="fill" @click="close">
          <IcIcon icon="close" />
        </Button>
      </template>
      <template #content>
        <template v-if="addUserDialogLoading">
          <div class="flex justify-center py-8">
            <span class="text-neutral-text-caption text-sm">
              사용자 리스트를 불러오는 중...
            </span>
          </div>
        </template>
        <template v-else>
          <div class="dialog__section">
            <Select
              v-model="addUserSelectedUserId"
              label="사용자 선택"
              placeholder="사용자 선택"
              :options="addUserSelectOptions"
              size="md"
              class="max-w-xs"
            />
            <p
              v-if="firstRole"
              class="text-neutral-text-caption mt-2 text-xs"
            >
              추가 시 부여할 권한: {{ firstRole.roleName ?? firstRole.roleCode ?? firstRole.id }}
              (권한 리스트 첫 번째 값)
            </p>
            <p
              v-else-if="addUserRolesForSelect.length === 0"
              class="text-neutral-text-caption mt-2 text-xs"
            >
              부여할 권한이 없습니다. 역할을 먼저 등록해 주세요.
            </p>
          </div>
        </template>
      </template>
      <template #footer>
        <Button
          color="secondary"
          variant="outlined"
          class="mr-2"
          @click="closeAddUserDialog"
        >
          닫기
        </Button>
        <Button
          color="primary"
          variant="fill"
          :disabled="
            !addUserSelectedUserId ||
            !firstRole ||
            addUserSubmitLoading
          "
          @click="submitAddUser"
        >
          {{ addUserSubmitLoading ? '추가 중...' : '추가' }}
        </Button>
      </template>
    </Dialog>

    <!-- 상세/권한 수정 다이얼로그 -->
    <Dialog
      v-model="dialogOpen"
      max-width="560"
      @update:model-value="(v) => !v && closeDialog()"
    >
      <template #title>
        <span class="text-neutral-text-title text-lg font-bold"
          >사용자 권한 상세</span
        >
      </template>
      <template #close-icon="{ close }">
        <Button color="inverse" size="xs" variant="fill" @click="close">
          <IcIcon icon="close" />
        </Button>
      </template>
      <template #content>
        <template v-if="dialogLoading">
          <div class="flex justify-center py-8">
            <span class="text-neutral-text-caption text-sm">로딩 중...</span>
          </div>
        </template>
        <template v-else>
          <div class="dialog__section">
            <div class="text-neutral-text-title mb-1 font-semibold">사용자</div>
            <div class="text-neutral-text-body">
              사용자 ID: {{ selectedUserId ?? '-' }}
            </div>
          </div>

          <div class="dialog__section">
            <div class="text-neutral-text-title mb-2 font-semibold">
              권한 리스트
            </div>
            <div class="mb-3">
              <Select
                v-model="selectedRoleId"
                label="권한 추가"
                placeholder="권한 선택"
                :options="assignableRoleOptions"
                size="md"
                class="max-w-xs"
                @update:model-value="(v) => v && assignRole(v)"
              />
              <div
                v-if="assignLoading"
                class="text-neutral-text-caption mt-1 text-xs"
              >
                추가 중...
              </div>
            </div>
            <div class="flex flex-wrap gap-2">
              <template v-for="ur in dialogUserRoles" :key="ur.id">
                <template v-if="ur.isActive !== false">
                  <div
                    class="border-neutral-stroke-300 bg-neutral-fill-50 text-neutral-text-body inline-flex items-center gap-1 rounded-md border px-3 py-1.5 text-sm"
                  >
                    <span>{{
                      ur.roleName ?? ur.roleCode ?? ur.roleId ?? '-'
                    }}</span>
                    <button
                      type="button"
                      class="text-neutral-text-caption hover:text-neutral-text-body hover:bg-neutral-fill-200 ml-1 rounded p-0.5 transition-colors"
                      :disabled="deleteLoadingMap[`${ur.userId}-${ur.roleId}`]"
                      aria-label="권한 삭제"
                      @click.stop="removeRole(ur)"
                    >
                      <IcIcon icon="close" />
                    </button>
                  </div>
                </template>
              </template>
              <span
                v-if="
                  dialogUserRoles.filter((r) => r.isActive !== false).length ===
                  0
                "
                class="text-neutral-text-caption text-sm"
              >
                부여된 권한이 없습니다.
              </span>
            </div>
          </div>
        </template>
      </template>
      <template #footer>
        <Button color="secondary" variant="outlined" @click="closeDialog">
          닫기
        </Button>
      </template>
    </Dialog>
  </div>
</template>

<style scoped></style>
