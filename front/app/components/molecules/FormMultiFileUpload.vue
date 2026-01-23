<script setup lang="ts">
  import { useField } from 'vee-validate'
  import { computed, unref, useAttrs, watch } from 'vue'

  interface FileValue {
    name: string
    size: number
    sortSeqNo: number
    file?: File | null
    fileLnkTpCd?: string | null
    fileUuid?: string | null
  }

  interface Props {
    name: string
    modelValue?: FileValue[]
    totalSize?: number
    attachDocListDto: any
    readonlyAndDownload?: boolean
    disabled?: boolean
    title?: string
    placeholder?: string
    maxLength?: number
  }

  const props = withDefaults(defineProps<Props>(), {
    modelValue: () => [],
    readonlyAndDownload: false,
    disabled: false,
    title: '첨부파일',
    maxLength: 100,
    totalSize: 20,
    placeholder: '',
  })

  defineOptions({ inheritAttrs: false })

  const attrs = useAttrs()
  const rootClass = computed(() => attrs.class)

  const { value, errorMessage, meta, setValue } = useField<FileValue[]>(
    () => props.name,
    undefined,
    {
      validateOnValueUpdate: false,
      syncVModel: true,
      initialValue: props.modelValue || [],
    },
  )

  watch(
    () => props.modelValue,
    (newVal) => {
      if (Array.isArray(newVal)) setValue(newVal)
    },
  )

  const emit = defineEmits<{
    (e: 'update:modelValue', v: FileValue[]): void
    (e: 'change', files: File[]): void
    (e: 'remove' | 'download', v: FileValue): void
    (e: 'allRemove'): void
  }>()

  function handleUpdateModelValue(next: FileValue[]) {
    setValue(next)
    emit('update:modelValue', next)
  }

  const isInvalid = computed(
    () => !!(props.name && meta.touched && unref(errorMessage)),
  )

  // attachDocListDto에서 atchDocKindList를 파싱하여 fileFormat과 accept 생성
  const fileFormat = computed(() => {
    if (!props.attachDocListDto.atchDocKindList) return []
    return props.attachDocListDto.atchDocKindList
      .split(';')
      .map((item) => item?.split('.')[1])
      .filter(Boolean)
  })

  const accept = computed(() => {
    if (!props.attachDocListDto.atchDocKindList) return ''
    return props.attachDocListDto.atchDocKindList
      .split(';')
      .map((item) => item?.split('*')[1])
      .join(',')
  })
</script>

<template>
  <div class="VFormSpptDnDFileUpload" :class="rootClass">
    <FileUpload
      :id="props.name"
      :model-value="value || []"
      :total-size="totalSize"
      :file-format="fileFormat"
      :readonly-and-download="readonlyAndDownload"
      :accept="accept"
      :disabled="disabled"
      :title="title"
      :placeholder="placeholder"
      :invalid="isInvalid"
      :multiple="props.attachDocListDto.multiFileAtchYn === 'Y'"
      :max-length="Number(props.maxLength) == 0 ? 100 : maxLength"
      @update:model-value="handleUpdateModelValue"
      @change="(files) => emit('change', files)"
      @remove="(file) => emit('remove', file)"
      @all-remove="() => emit('allRemove')"
      @download="(file) => emit('download', file)"
    >
      <template #description>
        <p v-if="readonlyAndDownload" class="leading-4">
          업로드된 파일이 없습니다.
        </p>
        <template v-else>
          <p class="leading-4">
            {{ attachDocListDto.atchDocNm }} 파일을 여기에 끌어다 놓거나,
          </p>

          <p class="leading-4">파일 선택 버튼을 직접 선택해 주세요</p>
          <p class="leading-4">
            <span v-if="fileFormat.length > 0">
              {{ fileFormat.join(', ') }} 파일만 업로드 가능</span
            >
            (최대 {{ totalSize }}MB)
          </p>
        </template>
      </template>
    </FileUpload>
    <KeepTransition>
      <span
        v-if="name && meta.touched && errorMessage"
        class="VFormSpptDnDFileUpload-alert"
        >{{ errorMessage }}</span
      >
    </KeepTransition>
  </div>
</template>

<style scoped lang="scss">
  .VFormSpptDnDFileUpload {
    display: block;

    &-alert {
      color: var(--color-red-500);
      display: block;
      margin-top: 8px;
    }
  }
</style>
