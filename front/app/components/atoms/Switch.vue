<script setup lang="ts">
  import type { VSwitch } from 'vuetify/components'

  type VSwitchProps = VSwitch['$props']

  defineOptions({
    inheritAttrs: false,
  })

  export type SwitchProps = {
    /**
     * v-model 값
     */
    modelValue: VSwitchProps['modelValue']

    /** 라벨 */
    label?: VSwitchProps['label']
    /** 비활성 */
    disabled?: VSwitchProps['disabled']
    /** 읽기전용 */
    readonly?: VSwitchProps['readonly']
    /** 색상 */
    color?: VSwitchProps['color']
    /** hide-details */
    hideDetails?: VSwitchProps['hideDetails']
  }

  const props = withDefaults(defineProps<SwitchProps>(), {
    label: undefined,
    disabled: false,
    readonly: false,
    color: 'primary',
    hideDetails: true,
  })

  const emit = defineEmits<{
    (e: 'update:modelValue', value: VSwitchProps['modelValue']): void
  }>()

  const value = computed({
    get: () => props.modelValue,
    set: (v: VSwitchProps['modelValue']) => emit('update:modelValue', v),
  })

  const attrs = useAttrs()
</script>

<template>
  <div class="grid gap-1">
    <div class="switch-container">
      <v-switch
        v-model="value"
        v-bind="attrs"
        class="pp-v-switch"
        :label="props.label"
        :disabled="props.disabled"
        :readonly="props.readonly"
        :color="props.color"
        :hide-details="props.hideDetails"
      >
        <!-- <template v-for="(_, name) in $slots" #[name]="slotProps">
        <slot :name="name" v-bind="slotProps" />
      </template> -->
      </v-switch>
      <p v-if="value" class="switch-label switch-label-on">on</p>
      <p v-else class="switch-label switch-label-off">off</p>
    </div>
  </div>
</template>

<style scoped>
  .switch-container {
    position: relative;
  }

  .pp-v-switch :deep(.v-switch__track) {
    min-width: 50px;
    height: 28px;
    opacity: 1 !important;
  }

  .pp-v-switch :deep(.v-switch__track:not(.bg-primary)) {
    background-color: var(--color-neutral-stroke-300) !important;
  }

  .pp-v-switch :deep(.v-switch__thumb) {
    background-color: var(--color-white) !important;
  }

  .pp-v-switch :deep(.v-label) {
    color: var(--color-neutral-text-text);
  }

  .switch-label {
    position: absolute;
    top: 50%;
    transform: translateY(-50%);
    margin: 0;
    font-size: 12px;
    font-weight: 500;
    pointer-events: none;
    z-index: 1;
  }

  .switch-label-on {
    left: 4px;
    color: var(--color-white);
    font-weight: 700;
  }

  .switch-label-off {
    left: 24px;
    color: var(--color-neutral-text-placeholder);
    font-weight: 500;
  }
</style>
