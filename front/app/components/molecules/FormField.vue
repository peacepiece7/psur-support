<script setup lang="ts">
  import { useField } from 'vee-validate'
  import type { GenericObject } from 'vee-validate'
  import { computed, watch } from 'vue'
  import KeepTransition from './KeepTransition.vue'

  type Props = {
    name: string
    /** v-model 값 (외부에서 전달받는 값) */
    modelValue?: unknown
    initialValue?: unknown
    rules?:
      | string
      | ((
          value: unknown,
          ctx?: GenericObject,
        ) => boolean | string | Promise<boolean | string>)
    validateOnValueUpdate?: boolean
    syncVModel?: boolean
    showErrorMessage?: boolean
  }

  const props = withDefaults(defineProps<Props>(), {
    modelValue: undefined,
    initialValue: undefined,
    rules: undefined,
    validateOnValueUpdate: false,
    syncVModel: true,
    showErrorMessage: true,
  })

  const emit = defineEmits<{
    (e: 'update:modelValue', v: unknown): void
  }>()

  const {
    value,
    errorMessage,
    handleBlur,
    meta,
    setValue: setFieldValue,
  } = useField(() => props.name, props.rules, {
    validateOnValueUpdate: props.validateOnValueUpdate,
    syncVModel: props.syncVModel,
    initialValue: props.initialValue,
  })

  // 외부 modelValue → vee-validate 동기화
  watch(
    () => props.modelValue,
    (v) => {
      if (v !== undefined && value.value !== v) {
        value.value = v
      }
    },
    { immediate: true },
  )

  // vee-validate → 외부 modelValue emit
  const setValue = (v: unknown) => {
    setFieldValue(v)
    emit('update:modelValue', v)
  }

  /**
   * ✅ v-model 전용 프록시
   */
  const field = computed<any>({
    get: () => value.value,
    set: (val) => {
      setValue(val)
    },
  })

  /**
   * 에러 상태 계산 (computed로 반응형 유지)
   */
  const hasError = computed(() => !!errorMessage.value)
  const isValid = computed(() => meta.valid)
  const isTouched = computed(() => meta.touched)

  /**
   * 에러 메시지 ID 생성 (접근성용)
   */
  const errorMessageId = computed(() => {
    if (!errorMessage.value) return undefined
    return `${props.name}-error`
  })

  /**
   * slot에 전달할 props
   */
  const slotProps = {
    bind: {
      'onUpdate:modelValue': setValue,
      onBlur: handleBlur,
      errorMessageId,
      error: hasError,
    },
    field,
    value,
    errorMessage,
    errorMessageId,
    hasError,
    meta,
    isValid,
    isTouched,
  }
</script>

<template>
  <div class="FormField">
    <!-- 디버깅용 코드-->
    <!-- <p>field: {{ field }}</p>
    <p>errorMessage: {{ errorMessage }}</p>
    <p>hasError: {{ hasError }}</p> -->
    <div class="FormField__control" :class="{ 'has-error': !!errorMessage }">
      <slot v-bind="slotProps" />
    </div>

    <div class="FormField__empty_message_area">
      <KeepTransition direction="down">
        <p
          v-if="showErrorMessage && !!errorMessage"
          :id="errorMessageId"
          class="FormField__error"
        >
          {{ errorMessage }}
        </p>
      </KeepTransition>
    </div>
  </div>
</template>

<style scoped>
  .FormField__error {
    color: rgb(var(--v-theme-error));
  }

  .FormField__empty_message_area {
    max-height: 1rem;
    min-height: 1rem;
  }
</style>
