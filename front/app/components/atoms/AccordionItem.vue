<script setup lang="ts">
  import { useAttrs } from 'vue'

  defineOptions({ inheritAttrs: false })

  const attrs = useAttrs()
</script>

<template>
  <v-expansion-panel v-bind="attrs" class="accordion-item">
    <template #title>
      <slot name="title" />
    </template>

    <template #text>
      <slot name="text" />
    </template>
  </v-expansion-panel>
</template>

<style scoped>
  .accordion-item :deep(.v-expansion-panel-title) {
    display: flex;
    align-items: center;
  }

  .accordion-item :deep(.v-expansion-panel-title:hover) {
    background-color: var(--color-neutral-fill-50);
    color: var(--color-primary-700);
  }

  .accordion-item :deep(.v-expansion-panel-title__overlay) {
    display: none;
  }

  .accordion-item :deep(.v-expansion-panel-text) {
    display: block !important;
    padding-left: 0;
    padding-right: 0;
  }

  .accordion-item :deep(.accordion-item__content) {
    width: 100%;
  }
</style>
