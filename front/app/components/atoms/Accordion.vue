<script setup lang="ts">
  import { useAttrs } from 'vue'

  defineOptions({ inheritAttrs: false })

  const attrs = useAttrs()
</script>

<template>
  <v-expansion-panels v-bind="attrs" class="accordion">
    <slot />
  </v-expansion-panels>
</template>

<style scoped>
  /* .accordion :deep(.v-expansion-panels) {
    box-shadow: none;
  }

  .accordion :deep(.v-expansion-panel) {
    box-shadow: none;
    border-bottom: 1px solid var(--color-grey-200);
  }

  .accordion :deep(.v-expansion-panel:last-child) {
    border-bottom: 0;
  }

  .accordion :deep(.v-expansion-panel-title) {
    min-height: 0;
    padding-top: 1rem;
    padding-bottom: 1rem;
  }

  .accordion :deep(.v-expansion-panel-text__wrapper) {
    padding-top: 0;
    padding-bottom: 1rem;
  } */
</style>
