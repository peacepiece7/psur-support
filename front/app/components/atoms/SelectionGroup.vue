<script setup lang="ts">
  type Direction = 'vertical' | 'horizontal'
  type ColorSet = 'primary' | 'success' | 'warning'

  export type GroupOption<TValue extends string | number = string> = {
    label: string
    value: TValue
    disabled?: boolean
  }

  type RadioProps<TValue extends string | number = string> = {
    type?: 'radio'
    modelValue: TValue | null
  }

  type CheckboxProps<TValue extends string | number = string> = {
    type: 'checkbox'
    modelValue: TValue[]
  }

  type BaseProps<TValue extends string | number = string> = {
    items: GroupOption<TValue>[]
    direction?: Direction
    colorSet?: ColorSet
    label?: string
    disabled?: boolean
    readonly?: boolean
    /** 에러 상태 */
    error?: ComputedRef<boolean> | boolean
    /** 에러 메시지 ID (aria-describedby용) */
    errorMessageId?: ComputedRef<string> | string
  }

  type Props<TValue extends string | number = string> =
    | (BaseProps<TValue> & RadioProps<TValue>)
    | (BaseProps<TValue> & CheckboxProps<TValue>)

  const props = withDefaults(defineProps<Props>(), {
    type: 'radio',
    direction: 'vertical',
    colorSet: 'primary',
    disabled: false,
    readonly: false,
    label: undefined,
    errorMessageId: undefined,
    error: undefined,
  })

  const error = computed(() => unref(props.error))
  const errorMessageId = computed(() => unref(props.errorMessageId))

  const emit = defineEmits<{
    (
      e: 'update:modelValue',
      value: string | number | null | (string | number)[],
    ): void
  }>()

  const value = computed({
    get: () => props.modelValue,
    set: (v) => {
      emit('update:modelValue', v)
    },
  })

  /**
   * warning은 "빨강 계열" 요청이라 Vuetify의 error 컬러를 사용
   * error prop이 true일 때도 error 컬러 사용
   */
  const mappedColor = computed(() => {
    if (props.colorSet === 'warning' || error.value) return 'error'
    return props.colorSet
  })

  const fieldsetClass = computed(() => {
    return [
      'selection-group__fieldset',
      {
        'selection-group__fieldset--horizontal':
          props.direction === 'horizontal',
        'selection-group__fieldset--readonly': props.readonly,
      },
    ]
  })

  // 클릭 시 ripple 애니메이션 트리거
  const handleClick = (event: MouseEvent) => {
    // readonly일 때는 애니메이션 실행하지 않음
    if (props.readonly) return

    const target = event.currentTarget as HTMLElement
    const input = target.querySelector(
      '.v-selection-control__input',
    ) as HTMLElement
    if (input) {
      // 기존 애니메이션 제거 후 재시작
      input.classList.remove('selection-ripple-active')
      void input.offsetWidth // 리플로우 강제
      input.classList.add('selection-ripple-active')
      // 애니메이션 종료 후 클래스 제거
      setTimeout(() => {
        input.classList.remove('selection-ripple-active')
      }, 1000)
    }
  }
</script>

<template>
  <div v-if="props.type === 'radio'" class="selection-group" v-bind="$attrs">
    <div v-if="props.label" class="text-grey-900 mb-2 text-sm font-semibold">
      {{ props.label }}
    </div>
    <div class="selection-group__wrapper">
      <fieldset
        :class="fieldsetClass"
        :aria-invalid="error ? 'true' : undefined"
        :aria-describedby="errorMessageId ?? undefined"
        :disabled="props.disabled"
      >
        <v-radio-group
          v-model="value"
          :disabled="props.disabled"
          :readonly="props.readonly"
          :inline="props.direction === 'horizontal'"
          hide-details
        >
          <v-radio
            v-for="opt in props.items"
            :key="String(opt.value)"
            :label="opt.label"
            :value="opt.value"
            :disabled="opt.disabled"
            :color="mappedColor"
            :tabindex="props.readonly ? -1 : 0"
            @click="handleClick"
          />
        </v-radio-group>
      </fieldset>
      <div v-if="error" class="selection-group__error-box" aria-hidden="true" />
    </div>
  </div>

  <div v-else class="selection-group" v-bind="$attrs">
    <div v-if="props.label" class="text-grey-900 mb-2 text-sm font-semibold">
      {{ props.label }}
    </div>
    <div class="selection-group__wrapper">
      <fieldset
        :class="fieldsetClass"
        :aria-invalid="error ? 'true' : undefined"
        :aria-describedby="errorMessageId ?? undefined"
        :disabled="props.disabled"
      >
        <div class="selection-group__items">
          <v-checkbox
            v-for="opt in props.items"
            :key="String(opt.value)"
            v-model="value"
            :label="opt.label"
            :value="opt.value"
            :disabled="props.disabled || opt.disabled"
            :readonly="props.readonly"
            :color="mappedColor"
            :tabindex="props.readonly ? -1 : undefined"
            hide-details
            @click="handleClick"
          />
        </div>
      </fieldset>
      <div v-if="error" class="selection-group__error-box" aria-hidden="true" />
    </div>
  </div>
</template>
