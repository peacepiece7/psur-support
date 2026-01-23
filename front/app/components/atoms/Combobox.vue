<script setup lang="ts">
  import type { VCombobox } from 'vuetify/components'

  type VComboboxProps = VCombobox['$props']

  export type ComboboxProps = {
    /**
     * v-model 값
     * - multiple=false: string | number | object 등
     * - multiple=true: 배열
     */
    modelValue: VComboboxProps['modelValue']

    /**
     * items
     */
    items?: VComboboxProps['items']

    /**
     * item-title / item-value
     */
    itemTitle?: VComboboxProps['itemTitle']
    itemValue?: VComboboxProps['itemValue']

    /**
     * 옵션
     */
    multiple?: VComboboxProps['multiple']
    chips?: VComboboxProps['chips']
    closableChips?: VComboboxProps['closableChips']

    /**
     * 기본 입력 props
     */
    label?: VComboboxProps['label']
    placeholder?: VComboboxProps['placeholder']
    disabled?: VComboboxProps['disabled']
    clearable?: VComboboxProps['clearable']
    variant?: VComboboxProps['variant']
    density?: VComboboxProps['density']
  }

  const props = withDefaults(defineProps<ComboboxProps>(), {
    items: () => [],
    multiple: false,
    chips: false,
    closableChips: false,
    disabled: false,
    clearable: true,
    variant: 'outlined',
    density: 'compact',
  })

  const emit = defineEmits<{
    (e: 'update:modelValue', value: VComboboxProps['modelValue']): void
  }>()

  const value = computed({
    get: () => props.modelValue,
    set: (v: VComboboxProps['modelValue']) => emit('update:modelValue', v),
  })
</script>

<template>
  <v-combobox
    v-model="value"
    class="pp-v-field"
    :items="props.items"
    :item-title="props.itemTitle"
    :item-value="props.itemValue"
    :chips="props.chips"
    :closable-chips="props.closableChips"
    :multiple="props.multiple"
    :label="props.label"
    :placeholder="props.placeholder"
    :disabled="props.disabled"
    :clearable="props.clearable"
    :variant="props.variant"
    :density="props.density"
    v-bind="$attrs"
  >
    <template v-for="(_, name) in $slots" #[name]="slotProps">
      <slot :name="name" v-bind="slotProps" />
    </template>
  </v-combobox>
</template>
