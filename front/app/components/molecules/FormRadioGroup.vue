<script setup lang="ts">
  import CheckBoxGroup, {
    type GroupOption,
  } from '~/components/atoms/SelectionGroup.vue'
  import { useField } from 'vee-validate'
  import { computed, useAttrs } from 'vue'

  type Direction = 'vertical' | 'horizontal'
  type ColorSet = 'primary' | 'success' | 'warning'

  const props = withDefaults(
    defineProps<{
      modelValue?: string | number | null
      name: string
      items: GroupOption[]
      direction?: Direction
      colorSet?: ColorSet
      label?: string
      disabled?: boolean
    }>(),
    {
      direction: 'vertical',
      colorSet: 'primary',
      disabled: false,
    },
  )

  const emit = defineEmits<{
    (e: 'update:modelValue', value: string | number | null): void
    (e: 'blur'): void
  }>()

  defineOptions({
    inheritAttrs: false,
  })

  const attrs: any = useAttrs()
  const inputAttrs = computed(() => {
    const { class: _ignored, ...rest } = attrs
    return rest
  })

  const { value, errorMessage, handleChange, handleBlur, meta } = useField<
    string | number | null
  >(() => props.name ?? '', undefined, {
    validateOnValueUpdate: false,
    syncVModel: true,
    initialValue: props.modelValue ?? null,
  })
</script>

<template>
  <div class="VRadioGroup">
    <CheckBoxGroup
      :model-value="value"
      v-bind="inputAttrs"
      :class="{
        valid: meta.touched && meta.valid,
        invalid: meta.touched && !meta.valid,
      }"
      :items="props.items"
      type="radio"
      :direction="props.direction"
      :color-set="props.colorSet"
      :label="props.label"
      :disabled="props.disabled"
      @update:model-value="(v) => handleChange(v, false)"
      @blur="
        (e) => {
          handleBlur(e, true)
          emit('blur')
        }
      "
    />
    <span class="mt-2" style="color: var(--color-red-500); display: block">{{
      errorMessage
    }}</span>
  </div>
</template>
