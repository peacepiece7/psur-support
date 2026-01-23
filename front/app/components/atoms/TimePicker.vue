<script setup lang="ts">
  import { INPUT } from '~/constants/design'
  import dayjs from 'dayjs'

  defineOptions({
    inheritAttrs: false,
  })

  export type TimePickerSize = 'sm' | 'md' | 'lg' | 'xl' | 'max'

  export type TimePickerProps = {
    /**
     * v-model 값 (string only: 'HH:mm' | '')
     */
    modelValue: string

    // ===== UI 설정 =====
    /** 입력 사이즈 */
    size?: TimePickerSize
    /** 라벨 */
    label?: string
    /** 플레이스홀더 */
    placeholder?: string

    // ===== 동작 설정 =====
    /** 비활성화 */
    disabled?: boolean
    /** 읽기 전용 */
    readonly?: boolean
    /** 스크롤 가능 */
    scrollable?: boolean

    // ===== 시간 제한 =====
    /** 최소 시간 (string: 'HH:mm') */
    min?: string | undefined
    /** 최대 시간 (string: 'HH:mm') */
    max?: string | undefined

    // ===== 시간 선택 제한 =====
    /** 허용된 시간 (숫자 배열 또는 함수) */
    allowedHours?: number[] | ((val: number) => boolean)
    /** 허용된 분 (숫자 배열 또는 함수) */
    allowedMinutes?: number[] | ((val: number) => boolean)
    /** 허용된 초 (숫자 배열 또는 함수) */
    allowedSeconds?: number[] | ((val: number) => boolean)

    // ===== 포맷 =====
    /** 시간 포맷 ('ampm' | '24hr') */
    format?: 'ampm' | '24hr'
    /** 초 사용 여부 */
    useSeconds?: boolean
  }

  const props = withDefaults(defineProps<TimePickerProps>(), {
    modelValue: '',
    size: 'md',
    placeholder: '시:분',
    label: '',
    disabled: false,
    readonly: false,
    scrollable: false,
    min: undefined,
    max: undefined,
    allowedHours: undefined,
    allowedMinutes: undefined,
    allowedSeconds: undefined,
    format: '24hr',
    useSeconds: false,
  })

  const emit = defineEmits<{
    (e: 'update:modelValue', value: string): void
  }>()

  // v-time-picker에 전달되는 값 (string only: 'HH:mm')
  const pickerValue = ref<string>('')

  // 사용자가 입력한 텍스트 값 (TextField에 표시, pickerValue 업데이트와 독립적)
  const userInputValue = ref<string>('')

  // 메뉴 열림/닫기 상태
  const showMenu = ref(false)

  // activator ref
  const activatorRef = ref<HTMLElement | null>(null)

  // 시간을 표시 형식으로 포맷팅
  const formatForDisplay = (value: string): string => {
    if (!value) return ''
    return value
  }

  // modelValue → 내부 동기화 (렌더 안정)
  watch(
    () => props.modelValue,
    (value) => {
      pickerValue.value = value || ''
      userInputValue.value = formatForDisplay(value)
    },
    { immediate: true },
  )

  // v-time-picker에서 시간 선택 시 userInputValue 동기화
  watch(pickerValue, (value) => {
    // pickerValue가 변경되면 (시간 선택 등) userInputValue도 업데이트
    if (value) {
      userInputValue.value = formatForDisplay(value)
      commit()
    } else {
      userInputValue.value = ''
    }
  })

  // emit은 여기서만
  const commit = () => {
    emit('update:modelValue', pickerValue.value || '')
  }

  // 4자리 숫자만 HH:mm 형식으로 변환
  const formatNumericInput = (value: string): string => {
    // 숫자만 추출
    const numbers = value.replace(/\D/g, '')

    if (numbers.length === 0) return ''

    // 4자리 숫자만 변환: "2214" -> "22:14"
    if (numbers.length === 4) {
      const hours = numbers.slice(0, 2)
      const minutes = numbers.slice(2, 4)
      return `${hours}:${minutes}`
    }

    // 4자리가 아니면 그대로 반환
    return value
  }

  // min/max 검증
  const validateTimeRange = (time: string): boolean => {
    if (!time) return true

    // min 검증
    if (props.min) {
      const currentTime = dayjs(`2000-01-01 ${time}`)
      const minTime = dayjs(`2000-01-01 ${props.min}`)
      if (currentTime.isBefore(minTime)) {
        return false
      }
    }

    // max 검증
    if (props.max) {
      const currentTime = dayjs(`2000-01-01 ${time}`)
      const maxTime = dayjs(`2000-01-01 ${props.max}`)
      if (currentTime.isAfter(maxTime)) {
        return false
      }
    }

    return true
  }

  // 시간 형식 및 범위 검증 (HH:mm 형식)
  const validateTimeFormat = (time: string): boolean => {
    if (!time) return false

    // HH:mm 형식 검증
    const timeRegex = /^([0-1]?[0-9]|2[0-3]):([0-5]?[0-9])$/
    const match = time.match(timeRegex)

    if (!match || time.length !== 5) {
      return false
    }

    const hours = parseInt(match[1])
    const minutes = parseInt(match[2])

    // 시간 범위 검증 (0-23, 0-59)
    if (hours < 0 || hours > 23 || minutes < 0 || minutes > 59) {
      return false
    }

    // min/max 범위 검증
    const normalized = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`
    if (!validateTimeRange(normalized)) {
      return false
    }

    return true
  }

  // TextField 입력 처리 - 유효성 검사 포함
  const handleInput = (value: string) => {
    // 빈 값이면 초기화
    if (!value) {
      userInputValue.value = ''
      pickerValue.value = ''
      commit()
      return
    }

    // 콜론이 포함된 경우는 자동 변환하지 않고 그대로 유지
    if (value.includes(':')) {
      userInputValue.value = value

      // 완전히 입력된 경우(5자: HH:mm)에만 검증 및 정규화
      const timeRegex = /^([0-1]?[0-9]|2[0-3]):([0-5]?[0-9])$/
      const match = value.match(timeRegex)

      if (match && value.length === 5) {
        const hours = parseInt(match[1]).toString().padStart(2, '0')
        const minutes = parseInt(match[2]).toString().padStart(2, '0')
        const normalized = `${hours}:${minutes}`

        const hour = parseInt(hours)
        const minute = parseInt(minutes)

        if (hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59) {
          // min/max 검증
          if (validateTimeRange(normalized)) {
            pickerValue.value = normalized
            userInputValue.value = normalized
            commit()
          } else {
            // min/max 범위를 벗어나면 이전 값으로 복원
            pickerValue.value = props.modelValue || ''
            userInputValue.value = formatForDisplay(props.modelValue || '')
            commit()
          }
        } else {
          // 유효하지 않은 시간 범위면 이전 값으로 복원
          pickerValue.value = props.modelValue || ''
          userInputValue.value = formatForDisplay(props.modelValue || '')
          commit()
        }
      } else {
        // 입력 중이면 userInputValue만 업데이트 (pickerValue는 변경하지 않음)
        // pickerValue는 유지
      }
      return
    }

    // 4자리 숫자만 입력된 경우 자동 포맷팅
    const numericOnly = /^\d+$/.test(value)
    if (numericOnly && value.length === 4) {
      const formatted = formatNumericInput(value)
      const timeRegex = /^([0-1]?[0-9]|2[0-3]):([0-5][0-9])$/
      const match = formatted.match(timeRegex)

      if (match) {
        const hours = parseInt(match[1]).toString().padStart(2, '0')
        const minutes = parseInt(match[2]).toString().padStart(2, '0')
        const normalized = `${hours}:${minutes}`

        const hour = parseInt(hours)
        const minute = parseInt(minutes)

        if (hour >= 0 && hour <= 23 && minute >= 0 && minute <= 59) {
          // min/max 검증
          if (validateTimeRange(normalized)) {
            pickerValue.value = normalized
            userInputValue.value = normalized
            commit()
          } else {
            // min/max 범위를 벗어나면 이전 값으로 복원
            pickerValue.value = props.modelValue || ''
            userInputValue.value = formatForDisplay(props.modelValue || '')
            commit()
          }
          return
        }
      }
    }

    // 그 외의 경우 (숫자가 아닌 문자 포함, 4자리가 아닌 숫자 등)
    userInputValue.value = value
  }

  const timePickerWidth = computed(() => {
    return INPUT.width[props.size]
  })

  // textInput 모드인지 확인
  const isTextInputMode = computed(() => {
    return !props.readonly
  })

  // 시간 선택기 열기
  const openTimePicker = () => {
    if (props.disabled) return
    showMenu.value = true
  }

  // blur 이벤트 핸들러 - validation 실패 시 초기값으로 복원
  const handleBlur = () => {
    // 빈 값이면 초기값으로 복원
    if (!userInputValue.value) {
      pickerValue.value = props.modelValue || ''
      userInputValue.value = formatForDisplay(props.modelValue || '')
      commit()
      return
    }

    // validation 검증
    if (!validateTimeFormat(userInputValue.value)) {
      // validation 실패 시 초기값으로 복원
      pickerValue.value = props.modelValue || ''
      userInputValue.value = formatForDisplay(props.modelValue || '')
      commit()
    } else {
      // validation 성공 시 정규화하여 저장
      const timeRegex = /^([0-1]?[0-9]|2[0-3]):([0-5]?[0-9])$/
      const match = userInputValue.value.match(timeRegex)
      if (match) {
        const hours = parseInt(match[1]).toString().padStart(2, '0')
        const minutes = parseInt(match[2]).toString().padStart(2, '0')
        const normalized = `${hours}:${minutes}`
        pickerValue.value = normalized
        userInputValue.value = normalized
        commit()
      }
    }
  }
</script>

<template>
  <div :style="{ width: timePickerWidth }" class="timepicker-wrapper">
    <v-menu
      v-model="showMenu"
      min-width="0"
      :close-on-content-click="false"
      location="bottom right"
    >
      <template #activator="{ props: menuProps }">
        <div
          class="relative"
          :class="{ 'timepicker-readonly-field': !isTextInputMode }"
          :style="{ width: timePickerWidth }"
        >
          <TextField
            :model-value="userInputValue"
            :size="props.size"
            :label="props.label"
            :placeholder="props.placeholder"
            :readonly="!isTextInputMode"
            :disabled="props.disabled"
            max-length="5"
            autocomplete="off"
            @update:model-value="handleInput"
            @blur="handleBlur"
          >
            <template #append-inner>
              <button
                type="button"
                v-bind="menuProps"
                class="h-full cursor-pointer"
              >
                <IcIcon icon="timeout" class="text-2xl" />
              </button>
            </template>
          </TextField>
        </div>
      </template>

      <div class="timepicker-menu-wrapper">
        <v-time-picker
          v-model="pickerValue"
          :format="props.format"
          :min="props.min"
          :max="props.max"
          :disabled="props.disabled"
          :readonly="props.readonly"
          :scrollable="props.scrollable"
          :allowed-hours="props.allowedHours"
          :allowed-minutes="props.allowedMinutes"
          :allowed-seconds="props.allowedSeconds"
          :use-seconds="props.useSeconds"
        />
      </div>
    </v-menu>
  </div>
</template>

<style scoped>
  /* readonly 모드일 때 TextField 전체가 cursor: pointer */
  .timepicker-readonly-field :deep(.pp-v-field) {
    cursor: pointer;
  }

  .timepicker-readonly-field :deep(.pp-v-field .v-field__input) {
    cursor: pointer;
  }

  /* v-time-picker의 dial (시계 UI) 숨기기 */
  .timepicker-menu-wrapper :deep(.v-time-picker-clock) {
    display: none;
  }

  .timepicker-wrapper :deep(.v-field__append-inner) {
    background-color: var(--color-static-white) !important;
  }
</style>
