<script setup lang="ts">
  import type { CommonCodeGroupSummaryResponse } from '~/types/models/CommonCodeGroupSummaryResponse'
  import Button from '~/components/atoms/Button.vue'

  const commonCodeStore = useCommonCodeStore()

  const startAddRootGroup = () => {
    commonCodeStore.actions.startAddRootGroup()
  }

  const startEditRootGroup = (group: CommonCodeGroupSummaryResponse) => {
    commonCodeStore.actions.startEditRootGroup(group)
  }
</script>

<template>
  <div class="border-neutral-stroke-300 rounded-lg border bg-white p-6">
    <div class="mb-4 flex items-center justify-between">
      <h3 class="text-neutral-text-title text-base font-bold">
        최상위 그룹 목록
      </h3>
      <Button color="primary" size="sm" @click="startAddRootGroup">
        추가
      </Button>
    </div>
    <div class="space-y-2">
      <div
        v-for="group in commonCodeStore.rootGroups"
        :key="group.id"
        class="border-neutral-stroke-200 hover:bg-neutral-fill-50 cursor-pointer rounded border p-3"
        :class="{
          'bg-primary-50 border-primary-300':
            commonCodeStore.isRootGroupEditMode === 'edit' &&
            commonCodeStore.selectedRootGroupForEdit?.id === group.id,
        }"
        @click="startEditRootGroup(group)"
      >
        <div>
          <div class="text-neutral-text-title font-semibold">
            {{ group.groupName }}
          </div>
          <div class="text-neutral-text-caption text-xs">
            {{ group.groupCode }}
          </div>
        </div>
      </div>
      <div
        v-if="commonCodeStore.rootGroups.length === 0"
        class="text-neutral-text-caption py-8 text-center text-sm"
      >
        최상위 그룹 목록이 없습니다.
      </div>
    </div>
  </div>
</template>
