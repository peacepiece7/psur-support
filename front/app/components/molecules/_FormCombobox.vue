<script setup lang="ts">
  import Combobox from '~/components/atoms/Combobox.vue'
  import { useField } from 'vee-validate'
  import { computed, useAttrs } from 'vue'

  const props = withDefaults(
    defineProps<{
      modelValue?: unknown
      name: string
      items?: any[]
      itemTitle?: any
      itemValue?: any
      multiple?: boolean
      chips?: boolean
      closableChips?: boolean
      label?: string
      placeholder?: string
      disabled?: boolean
    }>(),
    {
      items: () => [],
      multiple: false,
      chips: false,
      closableChips: false,
      disabled: false,
    },
  )

  const emit = defineEmits<{
    (e: 'update:modelValue', value: unknown): void
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

  const initialValue = computed(() => {
    if (props.modelValue !== undefined) return props.modelValue
    return props.multiple ? [] : null
  })

  const { value, errorMessage, handleChange, handleBlur, meta } =
    useField<unknown>(() => props.name ?? '', undefined, {
      validateOnValueUpdate: false,
      syncVModel: true,
      initialValue: initialValue.value,
    })
</script>

<template>
  <div class="VCombobox">
    <Combobox
      :model-value="value"
      v-bind="inputAttrs"
      :class="{
        valid: meta.touched && meta.valid,
        invalid: meta.touched && !meta.valid,
      }"
      :items="props.items"
      :item-title="props.itemTitle"
      :item-value="props.itemValue"
      :multiple="props.multiple"
      :chips="props.chips"
      :closable-chips="props.closableChips"
      :label="props.label"
      :placeholder="props.placeholder"
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
