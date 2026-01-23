<script setup lang="ts">
  export interface BreadcrumbProps {
    menuNmPath: string
  }

  const props = defineProps<BreadcrumbProps>()

  const paths = computed(() => {
    if (!props.menuNmPath) return []
    return props.menuNmPath
      .split('>')
      .map((path) => path.trim())
      .filter(Boolean)
  })

  const shouldTruncate = computed(() => paths.value.length >= 4)

  const displayPaths = computed(() => {
    if (!shouldTruncate.value) {
      return paths.value
    }
    // home > ... > lastDepth 형태
    return [paths.value[0], paths.value[paths.value.length - 1]]
  })
</script>

<template>
  <nav class="flex items-center gap-2" aria-label="Breadcrumb">
    <!-- Home Icon -->
    <IcIcon icon="home" class="text-grey-800" />

    <template v-if="paths.length === 0">
      <!-- 빈 상태 -->
    </template>

    <template v-else-if="!shouldTruncate">
      <!-- 4개 미만일 때: 모든 항목 표시 -->
      <template v-for="(path, index) in paths" :key="index">
        <IcIcon icon="chevron-right" class="text-grey-800" />
        <span
          :class="{
            'text-neutral-text-caption': index < paths.length - 1,
            'text-neutral-text-text font-medium': index === paths.length - 1,
          }"
        >
          {{ path }}
        </span>
      </template>
    </template>

    <template v-else>
      <!-- 4개 이상일 때: home > ... > lastDepth -->
      <IcIcon icon="chevron-right" class="text-grey-800" />
      <span class="text-neutral-text-caption font-medium">
        {{ displayPaths[0] }}
      </span>
      <IcIcon icon="chevron-right" class="text-grey-800" />
      <span class="text-neutral-text-caption font-medium">...</span>
      <IcIcon icon="chevron-right" class="text-grey-800" />
      <span class="text-neutral-text-text font-medium">
        {{ displayPaths[1] }}
      </span>
    </template>
  </nav>
</template>

<style scoped></style>
