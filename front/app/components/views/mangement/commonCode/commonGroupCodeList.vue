<script setup lang="ts">
  import type { CommonCodeResponse } from '~/types/models/CommonCodeResponse'
  import SelectV2 from '~/components/atoms/SelectV2.vue'
  import Button from '~/components/atoms/Button.vue'

  const commonCodeStore = useCommonCodeStore()

  const startAddCode = () => {
    commonCodeStore.actions.startAddCode()
  }

  const handleCodeSelect = (code: CommonCodeResponse) => {
    commonCodeStore.actions.setSelectedCode(code.code || null)
  }

  const startEditCode = (code: CommonCodeResponse) => {
    commonCodeStore.actions.startEditCode(code)
  }

  const startAddChildCode = () => {
    commonCodeStore.actions.startAddChildCode()
  }

  const handleChildCodeSelect = (code: CommonCodeResponse) => {
    commonCodeStore.actions.setSelectedChildCode(code.code || null)
  }

  const startEditChildCode = (code: CommonCodeResponse) => {
    commonCodeStore.actions.startEditChildCode(code)
  }

  const handleDeleteCode = async (code: CommonCodeResponse) => {
    const groupCode = commonCodeStore.getters.selectedGroupCodeValue()
    if (!groupCode || !code.code) {
      alert('삭제할 수 없습니다.')
      return
    }
    const success = await commonCodeStore.actions.deleteCommonCode(
      groupCode,
      code.code,
    )
    if (success) {
      await commonCodeStore.actions.fetchCodeList(groupCode)
      if (commonCodeStore.editingCode?.code === code.code) {
        commonCodeStore.actions.cancelCodeEdit()
      }
      alert('공통코드가 삭제되었습니다.')
    }
  }

  const handleDeleteChildCode = async (code: CommonCodeResponse) => {
    const item = commonCodeStore.getters.selectedCodeItem()
    if (!commonCodeStore.selectedCode || !code.code || !item?.groupCode) {
      alert('삭제할 수 없습니다.')
      return
    }
    const success = await commonCodeStore.actions.deleteCommonCode(
      item.groupCode,
      code.code,
    )
    if (success) {
      await commonCodeStore.actions.fetchChildCodeList(item.groupCode)
      if (commonCodeStore.editingChildCode?.code === code.code) {
        commonCodeStore.actions.cancelCodeEdit()
      }
      alert('하위 코드가 삭제되었습니다.')
    }
  }
</script>

<template>
  <div
    class="border-neutral-stroke-300 rounded-lg border bg-white p-6 lg:col-span-2"
  >
    <!-- 그룹 선택 -->
    <div class="mb-4">
      <label class="text-neutral-text-title mb-2 block text-sm font-semibold">
        그룹 선택
      </label>
      <SelectV2
        :model-value="commonCodeStore.selectedGroupCode"
        @update:model-value="
          commonCodeStore.actions.setSelectedGroupCode($event)
        "
        :items="commonCodeStore.rootGroups"
        item-text="groupName"
        item-value="groupCode"
        placeholder="그룹을 선택하세요"
        size="md"
        clearable
      />
    </div>

    <!-- 코드 선택 -->
    <div v-if="commonCodeStore.selectedGroupCode" class="mb-4">
      <div class="mb-2 flex items-center justify-between">
        <label class="text-neutral-text-title block text-sm font-semibold">
          코드 선택
        </label>
        <Button color="primary" size="xs" @click="startAddCode"> 추가 </Button>
      </div>
      <div
        v-if="commonCodeStore.isLoadingCodes"
        class="text-neutral-text-caption py-4 text-center text-sm"
      >
        로딩 중...
      </div>
      <div v-else-if="commonCodeStore.codeList.length > 0" class="space-y-1">
        <div
          v-for="item in commonCodeStore.codeList"
          :key="item.code"
          class="border-neutral-stroke-200 hover:bg-neutral-fill-50 flex cursor-pointer items-center justify-between rounded border p-2"
          :class="{
            'bg-primary-50 border-primary-300':
              commonCodeStore.selectedCode === item.code ||
              (commonCodeStore.isCodeEditMode === 'edit' &&
                commonCodeStore.editingCode?.code === item.code),
          }"
          @click="handleCodeSelect(item)"
        >
          <div>
            <div class="text-neutral-text-title text-sm font-semibold">
              {{ item.codeName }}
            </div>
            <div class="text-neutral-text-caption text-xs">
              {{ item.code }}
            </div>
          </div>
          <div class="flex gap-2">
            <Button
              color="primary"
              size="xs"
              variant="outlined"
              @click.stop="startEditCode(item)"
            >
              수정
            </Button>
            <Button
              color="warning"
              size="xs"
              variant="outlined"
              @click.stop="handleDeleteCode(item)"
            >
              삭제
            </Button>
          </div>
        </div>
      </div>
      <div v-else class="text-neutral-text-caption py-4 text-center text-sm">
        코드 목록이 없습니다.
      </div>
    </div>

    <!-- 하위 코드 선택 -->
    <div v-if="commonCodeStore.selectedCode" class="mb-4">
      <div class="mb-2 flex items-center justify-between">
        <label class="text-neutral-text-title block text-sm font-semibold">
          하위 코드 선택
        </label>
        <Button color="primary" size="xs" @click="startAddChildCode">
          추가
        </Button>
      </div>
      <div
        v-if="commonCodeStore.isLoadingChildCodes"
        class="text-neutral-text-caption py-4 text-center text-sm"
      >
        로딩 중...
      </div>
      <div
        v-else-if="commonCodeStore.childCodeList.length > 0"
        class="space-y-1"
      >
        <div
          v-for="item in commonCodeStore.childCodeList"
          :key="item.code"
          class="border-neutral-stroke-200 hover:bg-neutral-fill-50 flex cursor-pointer items-center justify-between rounded border p-2"
          :class="{
            'bg-primary-50 border-primary-300':
              commonCodeStore.selectedChildCode === item.code ||
              (commonCodeStore.isChildCodeEditMode === 'edit' &&
                commonCodeStore.editingChildCode?.code === item.code),
          }"
          @click="handleChildCodeSelect(item)"
        >
          <div>
            <div class="text-neutral-text-title text-sm font-semibold">
              {{ item.codeName }}
            </div>
            <div class="text-neutral-text-caption text-xs">
              {{ item.code }}
            </div>
          </div>
          <div class="flex gap-2">
            <Button
              color="primary"
              size="xs"
              variant="outlined"
              @click.stop="startEditChildCode(item)"
            >
              수정
            </Button>
            <Button
              color="warning"
              size="xs"
              variant="outlined"
              @click.stop="handleDeleteChildCode(item)"
            >
              삭제
            </Button>
          </div>
        </div>
      </div>
      <div v-else class="text-neutral-text-caption py-4 text-center text-sm">
        하위 코드 목록이 없습니다.
      </div>
    </div>
  </div>
</template>
