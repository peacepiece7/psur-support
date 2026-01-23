<script setup lang="ts">
  import { computed, ref, useAttrs } from 'vue'
  import { useFormContext } from 'vee-validate'
  import KeepTransition from './KeepTransition.vue'
  //   import VMap from './VMap.vue'
  //   import Popover from 'peacepiece-primevue/popover'
  //   import MapIcon from '@/assets/imgs/ic_map.png'
  //   import { MAX_INPUT_LENGTH } from '@/constants'

  type InputSize = 'xs' | 'sm' | 'md' | 'lg' | 'xl' | 'max'

  interface Props {
    width?: string
    size?: InputSize

    // 주소
    zpcdName: string
    addrName: string
    dtlAddrName: string
    // 위도
    latName: string
    // 경도
    lngName: string

    // 그외
    readonly?: boolean
  }

  defineOptions({
    inheritAttrs: false,
  })

  const emit = defineEmits<{
    (e: 'submit', payload: { [key: string]: string }): void
    (e: 'change', event: Event): void
    (e: 'blur', payload?: unknown): void
  }>()

  const props = defineProps<Props>()
  const attrs = useAttrs()

  const isMax = computed(() => props.size === 'max')

  // useFormContext를 사용하여 폼 컨텍스트에서 에러와 값을 가져오기 (FormField와 동일한 상태 공유)
  const { errors, values, setFieldValue } = useFormContext()

  // setValue 메서드 (반응성 보장)
  const setZipCodeValue = (value: string) =>
    setFieldValue(props.zpcdName, value)
  const setAddressValue = (value: string) =>
    setFieldValue(props.addrName, value)
  const setAddressDetailValue = (value: string) =>
    setFieldValue(props.dtlAddrName, value)
  const setLatitudeValue = (value: string) =>
    setFieldValue(props.latName, value)
  const setLongitudeValue = (value: string) =>
    setFieldValue(props.lngName, value)

  // 값 접근 (computed로 반응성 보장)
  const zipCodeValue = computed(() => values[props.zpcdName])
  const addressValue = computed(() => values[props.addrName])
  const addressDetailValue = computed(() => values[props.dtlAddrName])
  const latitudeValue = computed(() => values[props.latName])
  const longitudeValue = computed(() => values[props.lngName])

  // 에러 메시지 접근 (computed로 반응성 보장)
  const zipCodeErrorMessage = computed(() => errors.value[props.zpcdName])
  const addressErrorMessage = computed(() => errors.value[props.addrName])
  const addressDetailErrorMessage = computed(
    () => errors.value[props.dtlAddrName],
  )

  // 주소 검색 콜백
  const { openPopup } = useSearchAddress((type, data) => {
    if (type === 'error' || type === 'close') return
    else if (type === 'success' && data !== null) {
      // useField의 setValue를 사용하여 부모 useForm과 동기화
      setZipCodeValue(data.zonecode)
      setAddressValue(data.address)
      setLatitudeValue(data.lat)
      setLongitudeValue(data.lng)

      // submit 이벤트 emit
      emit('submit', {
        [`${props.zpcdName}`]: data.zonecode,
        [`${props.addrName}`]: data.address,
        [`${props.dtlAddrName}`]: addressDetailValue.value || '',
        [`${props.latName}`]: data.lat,
        [`${props.lngName}`]: data.lng,
      })
    }
  })

  const isOpenMapView = ref(false)
  const mapContainerRef = ref<HTMLElement | null>(null)
  const mapPopoverRef = ref<{
    show: (event: Event) => void
    hide: (event: Event) => void
  } | null>(null)

  const handleMouseEnter = (event: Event) => {
    if (zipCodeValue.value && addressValue.value) {
      mapPopoverRef.value?.show(event)
    }
  }

  const handleMouseLeave = (event: Event) => {
    mapPopoverRef.value?.hide(event)
  }

  // 통합 에러 메시지 (우선순위: 우편번호 > 우편번호주소 > 상세주소)
  const unifiedErrorMessage = computed(() => {
    if (zipCodeErrorMessage.value) {
      return zipCodeErrorMessage.value
    }
    if (addressErrorMessage.value) {
      return addressErrorMessage.value
    }
    if (addressDetailErrorMessage.value) {
      return addressDetailErrorMessage.value
    }
    return undefined
  })

  // 통합 에러 상태: 세 필드 중 하나라도 에러가 있으면 모든 필드에 에러 표시
  const hasAnyError = computed(() => {
    return !!(
      zipCodeErrorMessage.value ||
      addressErrorMessage.value ||
      addressDetailErrorMessage.value
    )
  })
</script>

<template>
  <div class="FormAddressInputs" :class="attrs.class">
    <!-- 첫 번째 줄: 우편번호 + 주소 + 주소검색 버튼 -->
    <div class="address-row">
      <div class="address-row__fields">
        <!-- 우편번호 -->
        <FormField :name="props.zpcdName" class="address-row__zipcode">
          <template #default="{ bind, field }">
            <TextField
              :model-value="field.value"
              v-bind="bind"
              :error="hasAnyError"
              :label="undefined"
              placeholder="우편번호"
              size="xs"
              readonly
            />
          </template>
        </FormField>

        <!-- 주소 -->
        <FormField :name="props.addrName" class="address-row__address">
          <template #default="{ bind, field }">
            <TextField
              :model-value="field.value"
              v-bind="bind"
              :error="hasAnyError"
              :label="undefined"
              placeholder="우편번호 주소"
              :size="isMax ? 'max' : 'md'"
              readonly
            />
          </template>
        </FormField>
      </div>

      <!-- 주소검색 버튼 -->
      <div class="address-row__button">
        <Button
          color="tertiary"
          variant="fill"
          size="md"
          :readonly="props.readonly"
          @click="openPopup"
        >
          주소검색
        </Button>
      </div>

      <!-- 지도 버튼 (선택적) -->
      <!-- <div
        ref="mapContainerRef"
        class="map-container"
        @mouseenter="handleMouseEnter"
        @mouseleave="handleMouseLeave"
      >
        <button
          type="button"
          class="map-button"
          :disabled="!(zipCodeValue.value && addressValue.value)"
        >
          <img :src="MapIcon" alt="map" class="map-icon" />
        </button>
        <Popover
          ref="mapPopoverRef"
          class="map-preview-popover"
          :auto-hide="false"
        >
          <div class="preview-container">
            <VMap
              :club-post-addr="addressValue"
              :club-nm="addressDetailValue"
              :index="0"
            />
          </div>
        </Popover>
      </div> -->
    </div>

    <!-- 두 번째 줄: 상세주소 -->
    <div class="address-detail-row">
      <FormField :name="props.dtlAddrName">
        <template #default="{ bind, field }">
          <TextField
            :model-value="field.value"
            v-bind="bind"
            :error="hasAnyError"
            :label="undefined"
            placeholder="상세주소"
            :size="isMax ? 'max' : 'xl'"
            :disabled="props.readonly"
          />
        </template>
      </FormField>
    </div>

    <!-- 통합 에러 메시지 -->
    <div class="FormAddressInputs__error-message">
      <KeepTransition direction="down">
        <p v-if="unifiedErrorMessage" class="FormAddressInputs__error-text">
          {{ unifiedErrorMessage }}
        </p>
      </KeepTransition>
    </div>

    <!-- 숨겨진 필드 (위도, 경도) -->
    <div style="display: none">
      <FormField :name="props.latName">
        <template #default="{ bind, field }">
          <TextField
            :model-value="field.value"
            v-bind="bind"
            :label="undefined"
            type="text"
          />
        </template>
      </FormField>
      <FormField :name="props.lngName">
        <template #default="{ bind, field }">
          <TextField
            :model-value="field.value"
            v-bind="bind"
            :label="undefined"
            type="text"
          />
        </template>
      </FormField>
    </div>
  </div>
</template>

<style scoped lang="scss">
  .map-button {
    width: 34px;
    height: 34px;
    background-color: white;
    border: 1px solid #d6dce5;
    border-radius: 4px;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 0;

    &:hover:not(:disabled) {
      background-color: #eaeaea !important;
      border: 1px solid #d6dce5 !important;
    }

    &:disabled {
      opacity: 0.5;
      cursor: not-allowed;
    }

    img {
      min-width: 20px;
      min-height: 20px;
      width: 100%;
      height: 100%;
      object-fit: contain;
    }
  }

  .FormAddressInputs {
    display: flex;
    flex-direction: column;
    gap: 0;
  }

  /* 첫 번째 줄: 우편번호 + 주소 + 주소검색 버튼 */
  .address-row {
    display: flex;
    align-items: flex-start;
    gap: 8px;
    width: 100%;
  }

  .address-row__fields {
    display: flex;
    align-items: flex-start;
    gap: 8px;
    flex: 1;
    min-width: 0;
  }

  .address-row__zipcode {
    flex-shrink: 0;
    width: auto;
  }

  .address-row__zipcode :deep(.FormField) {
    width: auto;
  }

  .address-row__zipcode :deep(.FormField__control) {
    width: auto;
  }

  .address-row__zipcode :deep(.FormField__empty_message_area) {
    display: none;
  }

  .address-row__address {
    flex: 1;
    min-width: 0;
  }

  .address-row__address :deep(.FormField) {
    width: 100%;
  }

  .address-row__address :deep(.FormField__control) {
    width: 100%;
  }

  .address-row__address :deep(.FormField__empty_message_area) {
    display: none;
  }

  .address-row__button {
    flex-shrink: 0;
    display: flex;
    align-items: flex-start;
    padding-top: 0;
    height: 48px; /* TextField 높이와 맞춤 */
  }

  /* 두 번째 줄: 상세주소 */
  .address-detail-row {
    margin-top: 8px;
    width: 100%;
  }

  .address-detail-row :deep(.FormField) {
    width: 100%;
  }

  .address-detail-row :deep(.FormField__empty_message_area) {
    display: none;
  }

  .map-container {
    display: inline-block;
    position: relative;
    flex-shrink: 0;
  }

  .preview-container {
    padding: 5px;
    width: 350px;
    height: 350px;
    overflow: hidden;
    display: flex;
    align-items: center;
    justify-content: center;
    background-color: #f8f9fa;
  }

  :deep(.map-preview-popover) {
    .p-popover-content {
      padding: 0;
      width: 350px;
      height: 350px;
      overflow: hidden;
    }
  }

  /* 통합 에러 메시지 */
  .FormAddressInputs__error-message {
    margin-top: 8px;
    max-height: 1rem;
    min-height: 1rem;
  }

  .FormAddressInputs__error-text {
    color: rgb(var(--v-theme-error));
    font-size: 0.875rem;
    margin: 0;
    line-height: 1.2;
  }
</style>
