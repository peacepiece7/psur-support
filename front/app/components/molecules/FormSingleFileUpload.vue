<script setup lang="ts">
  import { useField } from 'vee-validate'
  import { computed, unref, useAttrs, watch } from 'vue'
  //   import type { AttachDocListDto } from '@/store/useRegRqsStore'

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
    multiple?: boolean
    maxLength?: number
  }

  const props = withDefaults(defineProps<Props>(), {
    modelValue: () => [],
    readonlyAndDownload: false,
    disabled: false,
    title: '첨부파일',
    multiple: false,
    maxLength: undefined,
    placeholder: '',
    // totalSize 확인 필요
    totalSize: 20,
  })

  const emit = defineEmits<{
    (e: 'update:modelValue', v: FileValue[]): void
    (e: 'change', files: File[]): void
    (e: 'remove' | 'download', v: FileValue): void
    (e: 'allRemove'): void
  }>()

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
      :multiple="multiple"
      :max-length="maxLength"
      @update:model-value="handleUpdateModelValue"
      @change="(files) => emit('change', files)"
      @remove="(file) => emit('remove', file)"
      @all-remove="() => emit('allRemove')"
      @download="(file) => emit('download', file)"
    >
      <template #description>
        <div class="file-sheet-icon">
          <div class="file-sheet-back"></div>
          <div class="file-sheet-front">
            <IcIcon icon="plus-bold" class="px-[12px] text-xs" />
          </div>
        </div>
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
      >
        {{ errorMessage }}
      </span>
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

  .file-sheet-icon {
    position: relative;
    width: 24px;
    height: 32px;
    margin: 0 auto 12px;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .file-sheet-back {
    position: absolute;
    width: 24px;
    height: 32px;
    background-color: var(--color-grey-300);
    border-radius: 6px;
    z-index: 1;
    left: -4px;
    top: 4px;
  }

  .file-sheet-front {
    position: relative;
    width: 24px;
    height: 32px;
    font-size: 16px;
    background-color: var(--color-grey-200);
    border-radius: 6px;
    z-index: 2;
    display: flex;
    align-items: center;
    justify-content: center;
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  }
</style>
