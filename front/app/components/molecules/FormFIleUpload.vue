<script setup lang="ts">
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
    maxLength: 100,
    totalSize: 20,
    placeholder: '',
  })

  const emit = defineEmits<{
    (e: 'update:modelValue', v: FileValue[]): void
    (e: 'change', files: File[]): void
    (e: 'remove' | 'download', v: FileValue): void
    (e: 'allRemove'): void
  }>()

  defineOptions({ inheritAttrs: false })
</script>

<template>
  <FormSingleFileUpload
    v-if="props.attachDocListDto.multiFileAtchYn !== 'Y'"
    v-bind="{ ...props, ...$attrs }"
    @update:model-value="$emit('update:modelValue', $event)"
    @change="$emit('change', $event)"
    @remove="$emit('remove', $event)"
    @all-remove="$emit('allRemove')"
    @download="$emit('download', $event)"
  />
  <!-- atchFileCnt가 0일 경우 행정 최대 첨부파일개수(100개) 사용 -->
  <FormMultiFileUpload
    v-else
    v-bind="{ ...props, ...$attrs }"
    @update:model-value="$emit('update:modelValue', $event)"
    @change="$emit('change', $event)"
    @remove="$emit('remove', $event)"
    @all-remove="$emit('allRemove')"
    @download="$emit('download', $event)"
  />
</template>

<style scoped lang="scss"></style>
