<script setup lang="ts">
  import type { CommonCodeGroupResponse } from '~/types/models/CommonCodeGroupResponse'
  import type { CommonCodeResponse } from '~/types/models/CommonCodeResponse'

  const props = defineProps<{
    tree: CommonCodeGroupResponse
    level?: number
  }>()

  const currentLevel = computed(() => props.level ?? 0)
  const indentStyle = computed(() => {
    if (currentLevel.value === 0) return {}
    return {
      marginLeft: `${currentLevel.value * 1}rem`,
    }
  })
</script>

<template>
  <div class="space-y-2">
    <!-- 현재 그룹의 코드들 -->
    <div v-if="tree.codes && tree.codes.length > 0" class="space-y-1">
      <div
        v-for="code in tree.codes"
        :key="code.code"
        class="text-neutral-text-body rounded bg-neutral-fill-50 p-2 text-sm"
        :style="indentStyle"
      >
        <div class="flex items-center justify-between">
          <div>
            <span class="font-semibold">{{ code.codeName || code.code }}</span>
            <span v-if="code.code" class="text-neutral-text-caption ml-2 text-xs"
              >({{ code.code }})</span
            >
          </div>
          <span
            v-if="code.sortOrder !== undefined"
            class="text-neutral-text-caption text-xs"
            >순서: {{ code.sortOrder }}</span
          >
        </div>
        <div
          v-if="code.description"
          class="text-neutral-text-caption mt-1 text-xs"
        >
          {{ code.description }}
        </div>
      </div>
    </div>

    <!-- 하위 그룹들 (재귀적으로 표시) -->
    <div v-if="tree.children && tree.children.length > 0" class="space-y-2">
      <div
        v-for="child in tree.children"
        :key="child.groupCode"
        class="border-neutral-stroke-200 rounded border-l-2 pl-3"
      >
        <div class="mb-2">
          <div class="text-neutral-text-title font-semibold">
            {{ child.groupName || child.groupCode }}
          </div>
          <div
            v-if="child.description"
            class="text-neutral-text-caption text-xs"
          >
            {{ child.description }}
          </div>
        </div>
        <TreeDisplay :tree="child" :level="currentLevel + 1" />
      </div>
    </div>

    <!-- 코드나 하위 그룹이 없을 때 -->
    <div
      v-if="(!tree.codes || tree.codes.length === 0) && (!tree.children || tree.children.length === 0)"
      class="text-neutral-text-caption py-2 text-center text-sm"
      :style="indentStyle"
    >
      데이터가 없습니다.
    </div>
  </div>
</template>

<style scoped></style>
