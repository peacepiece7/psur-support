import { defineStore } from 'pinia'
import { ref } from 'vue'
import type { CommonCodeResponse } from '~/types/models/CommonCodeResponse'
import type { CommonCodeGroupSummaryResponse } from '~/types/models/CommonCodeGroupSummaryResponse'
import type { CommonCodeCreateRequest } from '~/types/models/CommonCodeCreateRequest'
import type { ApiResponseCommonCodeGroupResponse } from '~/types/models/ApiResponseCommonCodeGroupResponse'
import type { ApiResponseListCommonCodeGroupSummaryResponse } from '~/types/models/ApiResponseListCommonCodeGroupSummaryResponse'
import { API_BASE_URL } from '~/constants/url'

export const useCommonCodeStore = defineStore('commonCode', () => {
  /* =========================
   * state (response / UI)
   * ========================= */

  // 최상위 그룹 목록 섹션
  const rootGroups = ref<CommonCodeGroupSummaryResponse[]>([])
  const selectedRootGroupForEdit = ref<CommonCodeGroupSummaryResponse | null>(
    null,
  )
  const isRootGroupEditMode = ref<'add' | 'edit' | null>(null)
  const isSubmittingRootGroup = ref(false)

  // 그룹별 코드 관리 섹션
  const selectedGroupCode = ref<CommonCodeGroupSummaryResponse | null>(null)
  const selectedCode = ref<CommonCodeResponse['code'] | null>(null)
  const selectedChildCode = ref<CommonCodeResponse['code'] | null>(null)
  const codeList = ref<CommonCodeResponse[]>([])
  const childCodeList = ref<CommonCodeResponse[]>([])
  const isLoadingCodes = ref(false)
  const isLoadingChildCodes = ref(false)
  const isCodeEditMode = ref<'add' | 'edit' | null>(null)
  const isChildCodeEditMode = ref<'add' | 'edit' | null>(null)
  const editingCode = ref<CommonCodeResponse | null>(null)
  const editingChildCode = ref<CommonCodeResponse | null>(null)
  const isSubmittingCode = ref(false)

  /* =========================
   * getters (PURE)
   * ========================= */
  const getters = {
    /** 선택된 그룹의 groupCode 문자열 (null 가능) */
    selectedGroupCodeValue(): string | null {
      return selectedGroupCode.value?.groupCode ?? null
    },
    /** codeList에서 선택된 코드 아이템 */
    selectedCodeItem(): CommonCodeResponse | undefined {
      if (!selectedCode.value) return undefined
      return codeList.value.find((item) => item.code === selectedCode.value)
    },
  }

  /* =========================
   * actions (PUBLIC API)
   * ========================= */

  const _getErrorMessage = (error: unknown): string => {
    return (
      (error as { data?: { resultMessage?: string } })?.data?.resultMessage ||
      (error as { message?: string })?.message ||
      '오류가 발생했습니다.'
    )
  }

  const actions = {
    // ---------- 최상위 그룹 ----------
    async fetchRootGroups() {
      try {
        const response =
          await $fetch<ApiResponseListCommonCodeGroupSummaryResponse>(
            `${API_BASE_URL}/common-codes/groups/root`,
            {
              method: 'GET',
              credentials: 'include',
            },
          )
        if (response.resultCode === 200 && response.data) {
          rootGroups.value = response.data
        } else {
          alert(
            response.resultMessage ||
              '루트 그룹 목록을 불러오는데 실패했습니다.',
          )
        }
      } catch (error: unknown) {
        console.error('Fetch root groups error:', error)
        alert(_getErrorMessage(error))
      }
    },

    startAddRootGroup() {
      isRootGroupEditMode.value = 'add'
      selectedRootGroupForEdit.value = null
    },

    startEditRootGroup(group: CommonCodeGroupSummaryResponse) {
      isRootGroupEditMode.value = 'edit'
      selectedRootGroupForEdit.value = group
    },

    cancelRootGroupEdit() {
      isRootGroupEditMode.value = null
      selectedRootGroupForEdit.value = null
    },

    async createRootGroup(data: {
      groupCode: string
      groupName: string
      description?: string
      sortOrder: number
    }): Promise<boolean> {
      try {
        const response = await $fetch<{
          resultCode?: number
          resultMessage?: string
        }>(`${API_BASE_URL}/common-codes/groups/root`, {
          method: 'POST',
          credentials: 'include',
          body: data,
        })
        if (response.resultCode === 200) {
          return true
        }
        alert(response.resultMessage || '루트 그룹 생성에 실패했습니다.')
        return false
      } catch (error: unknown) {
        console.error('Create root group error:', error)
        alert(_getErrorMessage(error))
        return false
      }
    },

    setSubmittingRootGroup(value: boolean) {
      isSubmittingRootGroup.value = value
    },

    // ---------- 그룹 선택 / 코드 목록 ----------
    setSelectedGroupCode(group: CommonCodeGroupSummaryResponse | null) {
      selectedGroupCode.value = group
    },

    async fetchCodeList(groupCode: string) {
      if (!groupCode) return
      try {
        isLoadingCodes.value = true
        const response = await $fetch<ApiResponseCommonCodeGroupResponse>(
          `${API_BASE_URL}/common-codes/${groupCode}/tree`,
          {
            method: 'GET',
            credentials: 'include',
            query: { depth: 3, includeCodes: true },
          },
        )
        if (response.resultCode === 200 && response.data?.codes) {
          codeList.value = response.data.codes
        } else {
          alert(
            response.resultMessage || '코드 목록을 불러오는데 실패했습니다.',
          )
        }
      } catch (error: unknown) {
        console.error('Fetch code list error:', error)
        alert(_getErrorMessage(error))
      } finally {
        isLoadingCodes.value = false
      }
    },

    async fetchChildCodeList(code: string) {
      if (!code) return
      try {
        isLoadingChildCodes.value = true
        const response = await $fetch<ApiResponseCommonCodeGroupResponse>(
          `${API_BASE_URL}/common-codes/${code}/tree`,
          {
            method: 'GET',
            credentials: 'include',
            query: { depth: 3, includeCodes: true },
          },
        )
        if (response.resultCode === 200 && response.data?.codes) {
          childCodeList.value = response.data.codes
        } else {
          alert(
            response.resultMessage ||
              '하위 코드 목록을 불러오는데 실패했습니다.',
          )
        }
      } catch (error: unknown) {
        console.error('Fetch child code list error:', error)
        alert(_getErrorMessage(error))
      } finally {
        isLoadingChildCodes.value = false
      }
    },

    /** 그룹 선택 시 코드/하위코드 초기화 후 코드 목록 조회 (watch에서 호출) */
    async onSelectedGroupCodeChange(
      newGroup: CommonCodeGroupSummaryResponse | null,
    ) {
      const groupCode = newGroup?.groupCode
      if (groupCode) {
        selectedCode.value = null
        selectedChildCode.value = null
        childCodeList.value = []
        await actions.fetchCodeList(groupCode)
      } else {
        codeList.value = []
        selectedCode.value = null
        selectedChildCode.value = null
        childCodeList.value = []
      }
    },

    setSelectedCode(code: CommonCodeResponse['code'] | null) {
      selectedCode.value = code
    },

    /** 코드 선택 시 하위 코드 목록 조회 (watch에서 호출) */
    async onSelectedCodeChange(newCode: CommonCodeResponse['code'] | null) {
      if (newCode) {
        selectedChildCode.value = null
        const selectedCodeItem = codeList.value.find(
          (item) => item.code === newCode,
        )
        if (selectedCodeItem?.groupCode) {
          await actions.fetchChildCodeList(selectedCodeItem.groupCode)
        } else {
          childCodeList.value = []
        }
      } else {
        childCodeList.value = []
        selectedChildCode.value = null
      }
    },

    setSelectedChildCode(code: CommonCodeResponse['code'] | null) {
      selectedChildCode.value = code
    },

    // ---------- 코드 편집 모드 ----------
    startAddCode() {
      if (!selectedGroupCode.value) return
      isCodeEditMode.value = 'add'
      isChildCodeEditMode.value = null
      editingCode.value = null
      editingChildCode.value = null
    },

    startEditCode(code: CommonCodeResponse) {
      selectedCode.value = code.code || null
      isCodeEditMode.value = 'edit'
      isChildCodeEditMode.value = null
      editingCode.value = code
      editingChildCode.value = null
    },

    startAddChildCode() {
      if (!selectedCode.value) return
      const selectedCodeItem = codeList.value.find(
        (item) => item.code === selectedCode.value,
      )
      if (!selectedCodeItem?.groupCode) {
        alert('하위 코드를 추가할 수 없습니다. (groupCode가 없습니다)')
        return
      }
      isChildCodeEditMode.value = 'add'
      isCodeEditMode.value = null
      editingCode.value = null
      editingChildCode.value = null
    },

    startEditChildCode(code: CommonCodeResponse) {
      if (!selectedCode.value) return
      selectedChildCode.value = code.code || null
      const selectedCodeItem = codeList.value.find(
        (item) => item.code === selectedCode.value,
      )
      if (!selectedCodeItem?.groupCode) {
        alert('하위 코드를 수정할 수 없습니다. (groupCode가 없습니다)')
        return
      }
      isChildCodeEditMode.value = 'edit'
      isCodeEditMode.value = null
      editingCode.value = null
      editingChildCode.value = code
    },

    cancelCodeEdit() {
      isCodeEditMode.value = null
      isChildCodeEditMode.value = null
      editingCode.value = null
      editingChildCode.value = null
    },

    setSubmittingCode(value: boolean) {
      isSubmittingCode.value = value
    },

    // ---------- API (공통코드 생성/삭제) ----------
    async createCommonCode(data: CommonCodeCreateRequest): Promise<boolean> {
      try {
        const response = await $fetch<{
          resultCode?: number
          resultMessage?: string
        }>(`${API_BASE_URL}/common-codes`, {
          method: 'POST',
          credentials: 'include',
          body: data,
        })
        if (response.resultCode === 200) return true
        alert(response.resultMessage || '공통코드 생성에 실패했습니다.')
        return false
      } catch (error: unknown) {
        console.error('Create common code error:', error)
        alert(_getErrorMessage(error))
        return false
      }
    },

    async deleteCommonCode(groupCode: string, code: string): Promise<boolean> {
      if (!confirm('정말 삭제하시겠습니까?')) return false
      try {
        const response = await $fetch<{
          resultCode?: number
          resultMessage?: string
        }>(`${API_BASE_URL}/common-codes/${groupCode}/codes/${code}`, {
          method: 'DELETE',
          credentials: 'include',
        })
        if (response.resultCode === 200) return true
        alert(response.resultMessage || '공통코드 삭제에 실패했습니다.')
        return false
      } catch (error: unknown) {
        console.error('Delete common code error:', error)
        alert(_getErrorMessage(error))
        return false
      }
    },

    initialize() {
      rootGroups.value = []
      selectedRootGroupForEdit.value = null
      isRootGroupEditMode.value = null
      isSubmittingRootGroup.value = false
      selectedGroupCode.value = null
      selectedCode.value = null
      selectedChildCode.value = null
      codeList.value = []
      childCodeList.value = []
      isLoadingCodes.value = false
      isLoadingChildCodes.value = false
      isCodeEditMode.value = null
      isChildCodeEditMode.value = null
      editingCode.value = null
      editingChildCode.value = null
      isSubmittingCode.value = false
    },
  }

  return {
    // state
    rootGroups,
    selectedRootGroupForEdit,
    isRootGroupEditMode,
    isSubmittingRootGroup,
    selectedGroupCode,
    selectedCode,
    selectedChildCode,
    codeList,
    childCodeList,
    isLoadingCodes,
    isLoadingChildCodes,
    isCodeEditMode,
    isChildCodeEditMode,
    editingCode,
    editingChildCode,
    isSubmittingCode,
    getters,
    actions,
  }
})
