<script setup lang="ts">
  import { VueDatePicker } from '@vuepic/vue-datepicker'
  import { ko } from 'date-fns/locale'
  import type { Locale } from 'date-fns/locale'
  import dayjs from 'dayjs'
  import { INPUT } from '~/constants/design'

  defineOptions({
    inheritAttrs: false,
  })

  export type DatePickerSize = 'sm' | 'md' | 'lg' | 'xl' | 'max'

  export type DatePickerProps = {
    /**
     * v-model 값 (string only: 'YYYY-MM-DD' | '')
     */
    modelValue: string

    // ===== 선택 모드 =====
    /** 월 선택 모드 */
    monthPicker?: boolean
    /** 년 선택 모드 */
    yearPicker?: boolean
    /** 분기 선택 모드 */
    quarterPicker?: boolean

    // ===== 에러 상태 =====
    /** 에러 상태 */
    error?: ComputedRef<boolean> | boolean
    /** 에러 메시지 ID (aria-describedby용) */
    errorMessageId?: ComputedRef<string> | string

    // ===== UI 설정 =====
    /** 입력 사이즈 */
    size?: DatePickerSize
    /** 라벨 */
    label?: string
    /** 플레이스홀더 */
    placeholder?: string
    /** 텔레포트 위치 */
    teleport?: string

    // ===== 동작 설정 =====
    /** 비활성화 */
    disabled?: boolean
    /** 읽기 전용 */
    readonly?: boolean
    /** 자동 적용 (확인 버튼 없이 즉시 적용) */
    autoApply?: boolean

    // ===== 날짜 제한 =====
    /** 최소 날짜 (string: 'YYYY-MM-DD') */
    minDate?: string | undefined
    /** 최대 날짜 (string: 'YYYY-MM-DD') */
    maxDate?: string | undefined

    // ===== 포맷 및 로케일 =====
    /** 로케일 (기본값: ko) */
    locale?: Locale
  }

  const props = withDefaults(defineProps<DatePickerProps>(), {
    modelValue: '',
    size: 'md',
    monthPicker: false,
    yearPicker: false,
    quarterPicker: false,
    placeholder: '연도-월-일',
    label: '',
    disabled: false,
    readonly: false,
    autoApply: true,
    minDate: undefined,
    maxDate: undefined,
    locale: () => ko,
    teleport: undefined,
    error: undefined,
    errorMessageId: undefined,
  })

  const emit = defineEmits<{
    (e: 'update:modelValue', value: string): void
  }>()

  const error = computed(() => unref(props.error))
  const errorMessageId = computed(() => unref(props.errorMessageId))

  // VueDatePicker에 전달되는 값 (string only)
  const pickerValue = ref<string>('')

  // 사용자가 입력한 텍스트 값 (TextField에 표시, pickerValue 업데이트와 독립적)
  const userInputValue = ref<string>('')

  // ref는 null 허용
  const datePickerRef = ref<InstanceType<typeof VueDatePicker> | null>(null)

  // 날짜를 표시 형식으로 포맷팅
  const formatForDisplay = (value: string): string => {
    if (!value) return ''

    const d = dayjs(value)
    if (!d.isValid()) return value

    if (props.monthPicker) return d.format('YYYY년 MM월')
    if (props.quarterPicker) {
      const q = Math.ceil((d.month() + 1) / 3)
      return `${d.format('YYYY')}년 ${q}분기`
    }
    if (props.yearPicker) {
      const date = dayjs(`${value.toString().slice(0, 4)}-01-01`)
      return date.format('YYYY년')
    }

    return d.format('YYYY-MM-DD')
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

  // VueDatePicker에서 캘린더로 날짜 선택 시 userInputValue 동기화
  watch(pickerValue, (value) => {
    // pickerValue가 변경되면 (캘린더 선택 등) userInputValue도 업데이트
    if (value) {
      userInputValue.value = formatForDisplay(value)
    } else {
      userInputValue.value = ''
    }
  })

  // emit은 여기서만
  const commit = () => {
    // 사용자가 입력을 하지 않거나 모두 지웠고, 캘린더도 선택된 값이 없을 경우 modelValue를 업데이트 하지 않음
    // 유효성 검사 컨벤션을 다른 input과 동일하게 맞추기 위함
    if (!userInputValue.value && !pickerValue.value) return

    emit('update:modelValue', pickerValue.value || '')
  }

  // TextField 입력 처리 - 사용자 입력값만 업데이트, pickerValue는 업데이트하지 않음
  const handleInput = (value: string) => {
    // 사용자가 입력하는 값은 그대로 유지
    userInputValue.value = value
  }

  // userInputValue를 정규화하여 pickerValue와 동기화 (blur 시 호출)
  const syncUserInputToPickerValue = () => {
    if (!userInputValue.value) {
      pickerValue.value = ''
      userInputValue.value = ''
      return
    }

    // 입력값을 파싱하여 정규화
    let parsed: dayjs.Dayjs | null = null

    if (props.monthPicker) {
      // "2024년 01월" 형식 파싱
      const match = userInputValue.value.match(/(\d{4})년\s*(\d{1,2})월/)
      if (match) {
        const year = parseInt(match[1])
        const month = parseInt(match[2])
        parsed = dayjs(`${year}-${month.toString().padStart(2, '0')}-01`)
      } else {
        parsed = dayjs(userInputValue.value)
      }
    } else if (props.quarterPicker) {
      // "2024년 1분기" 형식 파싱
      const match = userInputValue.value.match(/(\d{4})년\s*(\d)분기/)
      if (match) {
        const year = parseInt(match[1])
        const quarter = parseInt(match[2])
        const quarterStartMonth = (quarter - 1) * 3 + 1
        parsed = dayjs(
          `${year}-${quarterStartMonth.toString().padStart(2, '0')}-01`,
        )
      } else {
        parsed = dayjs(userInputValue.value)
      }
    } else if (props.yearPicker) {
      // "2024년" 형식 파싱
      const match = userInputValue.value.match(/(\d{4})년/)
      if (match) {
        const year = parseInt(match[1])
        parsed = dayjs(`${year}-01-01`)
      } else {
        parsed = dayjs(userInputValue.value)
      }
    } else {
      // 기본 날짜 형식 (YYYY-MM-DD)
      parsed = dayjs(userInputValue.value)
    }

    if (parsed && parsed.isValid()) {
      // 정규화된 형식으로 변환
      if (props.monthPicker) {
        pickerValue.value = parsed.format('YYYY-MM')
      } else if (props.quarterPicker) {
        const month = parsed.month() + 1
        const quarter = Math.ceil(month / 3)
        const quarterStartMonth = (quarter - 1) * 3 + 1
        pickerValue.value = parsed.format(
          `YYYY-${quarterStartMonth.toString().padStart(2, '0')}`,
        )
      } else if (props.yearPicker) {
        pickerValue.value = parsed.format('YYYY')
      } else {
        pickerValue.value = parsed.format('YYYY-MM-DD')
      }
      // 정규화된 값으로 userInputValue도 업데이트
      userInputValue.value = formatForDisplay(pickerValue.value)
    } else {
      // 유효하지 않은 값이면 pickerValue와 userInputValue 모두 초기화
      pickerValue.value = ''
      // 이전 유효한 값으로 복원 (modelValue가 있으면)
      if (props.modelValue) {
        userInputValue.value = formatForDisplay(props.modelValue)
      } else {
        userInputValue.value = ''
      }
    }
  }

  const datePickerClass = computed(() => {
    return [
      'vuepick-datepicker',
      `datepicker-size-${props.size}`,
      {
        'has-error': error.value,
        error: error.value,
      },
    ]
  })

  const datePickerWidth = computed(() => {
    return INPUT.width[props.size]
  })

  const iconButtonPositionClass = computed(() => {
    const sizeMap: Record<DatePickerSize, string> = {
      sm: 'right-2 bottom-[7px]',
      md: 'right-2 bottom-[7px]',
      lg: 'right-2.5 bottom-[7px]',
      xl: 'right-3 bottom-[7px]',
      max: 'right-2.5 bottom-[7px]',
    }
    return sizeMap[props.size] || sizeMap.md
  })

  // textInput 모드인지 확인
  const isTextInputMode = computed(() => {
    return (
      !props.readonly &&
      !props.monthPicker &&
      !props.yearPicker &&
      !props.quarterPicker
    )
  })

  // 캘린더 열기/닫기 제어
  const openCalendar = () => {
    if (!datePickerRef.value) return

    // 이미 캘린더가 열려있는지 확인
    const calendarMenu = document.querySelector(
      '.dp__menu[role="dialog"]',
    ) as HTMLElement
    if (calendarMenu) {
      // 이미 열려있으면 아무것도 하지 않음
      return
    }

    const picker = datePickerRef.value as InstanceType<typeof VueDatePicker> & {
      openMenu?: () => void
      togglePicker?: () => void
    }

    if (picker.openMenu) {
      picker.openMenu()
    } else if (picker.togglePicker) {
      picker.togglePicker()
    }

    // 캘린더가 열린 후 포커스를 캘린더로 이동
    nextTick(() => {
      const calendarMenu = document.querySelector(
        '.dp__menu[role="dialog"]',
      ) as HTMLElement
      if (!calendarMenu) return

      const firstFocusableElement = calendarMenu.querySelector(
        '[tabindex="0"]',
      ) as HTMLElement
      ;(firstFocusableElement || calendarMenu).focus()
    })
  }

  // 캘린더 닫기
  const closeCalendar = () => {
    if (!datePickerRef.value) return

    const picker = datePickerRef.value as InstanceType<typeof VueDatePicker> & {
      closeMenu?: () => void
      togglePicker?: () => void
    }
    if (picker.closeMenu) {
      picker.closeMenu()
    } else if (picker.togglePicker) {
      // 캘린더가 열려있는지 확인
      const calendarMenu = document.querySelector(
        '.dp__menu[role="dialog"]',
      ) as HTMLElement
      if (calendarMenu) {
        picker.togglePicker()
      }
    }
  }

  // TextField focus 이벤트 핸들러
  const handleTextFieldFocus = (event: FocusEvent) => {
    // dayPicker + readonly가 아닌 경우 (textInput 모드): 캘린더 열지 않음
    if (isTextInputMode.value) {
      event.stopPropagation()
      return
    }
    // 그 외 경우: 캘린더 열기
    event.stopPropagation()
    openCalendar()
  }

  // TextField click 이벤트 핸들러
  const handleTextFieldClick = (event: MouseEvent) => {
    // dayPicker + readonly가 아닌 경우 (textInput 모드): 캘린더 열지 않음
    if (isTextInputMode.value) {
      event.stopPropagation()
      return
    }
    // 그 외 경우: 캘린더 열기
    event.stopPropagation()
    event.preventDefault()
    openCalendar()
  }

  // TextField Enter 키 핸들러
  const handleTextFieldEnter = (event: KeyboardEvent) => {
    // textInput 모드일 때만 처리
    if (isTextInputMode.value) {
      event.preventDefault()
      event.stopPropagation()
      // 입력값 동기화 및 캘린더 닫기
      syncUserInputToPickerValue()
      commit()
      closeCalendar()
    }
  }

  // 캘린더 닫힘 처리
  const handleCalendarClosed = () => {
    console.log('handleCalendarClosed')
    commit()
  }

  // TextField blur 시 emit 및 캘린더 닫기
  const handleTextFieldBlur = () => {
    syncUserInputToPickerValue()
    commit()
    // textInput 모드일 때 캘린더 닫기
    if (isTextInputMode.value) {
      // closeCalendar()
    }
  }
</script>

<template>
  <div :style="{ width: datePickerWidth }">
    <VueDatePicker
      ref="datePickerRef"
      v-model="pickerValue"
      :teleport="props.teleport"
      :month-picker="props.monthPicker"
      :year-picker="props.yearPicker"
      :quarter-picker="props.quarterPicker"
      :auto-apply="props.autoApply"
      :min-date="props.minDate"
      :max-date="props.maxDate"
      :disabled="props.disabled"
      :readonly="props.readonly"
      :locale="props.locale"
      :time-config="{ enableTimePicker: false }"
      model-type="yyyy-MM-dd"
      :class="datePickerClass"
      :aria-invalid="error ? 'true' : undefined"
      :aria-describedby="errorMessageId ?? undefined"
      @closed="handleCalendarClosed"
    >
      <template #dp-input>
        <div
          class="relative"
          :class="{ 'datepicker-readonly-field': !isTextInputMode }"
          :style="{ width: datePickerWidth }"
        >
          <TextField
            :model-value="userInputValue"
            :size="props.size"
            :label="props.label"
            :placeholder="props.placeholder"
            :readonly="props.readonly"
            :disabled="props.disabled"
            max-length="10"
            append-inner-icon="local:calendar"
            autocomplete="off"
            @update:model-value="handleInput"
            @keydown.enter="handleTextFieldEnter"
            @blur="handleTextFieldBlur"
            @click="handleTextFieldClick"
            @focus="handleTextFieldFocus"
            @click:append-inner="openCalendar"
          />
          <!-- <div
            class="pointer-events-auto absolute z-10"
            :class="iconButtonPositionClass"
          >
            <Button
              type="button"
              variant="fill"
              color="inverse"
              size="xs"
              shape="circle"
              @click.stop="openCalendar"
            >
              <v-icon icon="local:calendar" class="text-black" />
            </Button>
          </div> -->
        </div>
      </template>
    </VueDatePicker>
  </div>
</template>

<style scoped>
  /* 아이콘 스타일 조정 */
  .vuepick-datepicker :deep(.dp__input_icon) {
    display: none;
  }

  /* Clear 버튼 숨기기 */
  .vuepick-datepicker :deep(.dp--clear-btn) {
    display: none;
  }

  .vuepick-datepicker :deep(.dp__input_wrap .relative) {
    pointer-events: auto;
  }

  /* 버튼 클릭 이벤트 보장 */
  .vuepick-datepicker :deep(.dp__input_wrap .pointer-events-auto) {
    pointer-events: auto !important;
  }

  /* readonly 모드일 때 TextField 전체가 cursor: pointer */
  .datepicker-readonly-field :deep(.pp-v-field) {
    cursor: pointer;
  }

  .datepicker-readonly-field :deep(.pp-v-field .v-field__input) {
    cursor: pointer;
  }

  .vuepick-datepicker :deep(.v-input__details) {
    display: none;
  }

  /* 에러 상태일 때 border를 빨간색으로 override */
  .vuepick-datepicker.has-error :deep(.v-field__outline),
  .vuepick-datepicker.error :deep(.v-field__outline) {
    color: rgb(var(--v-theme-error));
  }

  .vuepick-datepicker :deep(.v-field__append-inner) {
    background-color: var(--color-static-white) !important;
  }
  .vuepick-datepicker :deep(.v-field__append-inner svg) {
    opacity: 1 !important;
  }

  .vuepick-datepicker :deep(.dp__instance_calendar) {
    color: var(--color-neutral-text-body);
  }
</style>
