<script setup lang="ts">
  import { v4 as uuidv4 } from 'uuid'
  import { ref, computed } from 'vue'
  // import etx from "@/assets/ic_file_etc.svg";
  // import excel from "@/assets/ic_file_excel.svg";
  // import ppt from "@/assets/ic_file_ppt.svg";
  // import { useCustomAlert } from "@/composables/useCustomAlert";
  // import { DEFAULT_BLOCKED_FORMATS } from "@/constants";

  interface FileValue {
    name: string
    size: number
    sortSeqNo: number
    file?: File | null
    fileLnkTpCd?: string | null
    fileUuid?: string | null
  }

  type FileUpload = {
    totalSize?: number
    fileFormat?: string[]
    readonlyAndDownload?: boolean
    accept?: string
    title?: string
    invalid?: boolean
    multiple?: boolean
    maxLength?: number
    modelValue: FileValue[]
    disabled?: boolean
  }

  /**
   * @note 기본 차단 파일 포맷
   */
  // prettier-ignore
  const DEFAULT_BLOCKED_FORMATS: string[] = [ 'exe', 'bat', 'cmd', 'sh', 'js', 'jar', 'msi', 'vbs', 'ps1', 'scr', 'dll', 'com', 'apk', 'ipa', 'hta', 'pif', 'gadget', 'wsf', 'rar', '7z', 'tar', 'gz', 'bz2', 'xz', 'lz', 'lzma', 'z', 'iso', 'cab', 'arj', 'ace', 'zst' ]

  const props = withDefaults(defineProps<FileUpload>(), {
    modelValue: () => [],
    fileFormat: () => [],
    disabled: false,
    title: '첨부파일',
    invalid: false,
    multiple: false,
    maxLength: 100,
    totalSize: 20,
    readonlyAndDownload: false,
    accept: '',
  })

  const emit = defineEmits<{
    'update:modelValue': [p: FileValue[]]
    change: [p: File[]]
    remove: [p: FileValue]
    allRemove: []
    download: [p: FileValue]
  }>()

  const uuid = uuidv4()
  // const alertStore = useCustomAlert();

  const isDraggingOver = ref(false)
  const dropEffect = ref<'copy' | 'none'>('copy')

  const filteredAccept = computed(() => {
    if (!props.accept) return undefined as unknown as string | undefined
    return (
      props.accept
        .split(',')
        .map((ext) => ext.trim())
        .filter((ext) => {
          const cleanExt = ext.replace(/^\./, '').toLowerCase()
          return cleanExt && !DEFAULT_BLOCKED_FORMATS.includes(cleanExt)
        })
        .join(',') || undefined
    )
  })

  // 토탈 파일 사이즈
  const mbFileSize = computed(() => {
    const bytes =
      props.modelValue?.reduce((prev, file) => {
        return prev + file.size
      }, 0) ?? 0

    return byteToMB(bytes)
  })

  const handleDragEnter = (e: DragEvent) => {
    e.preventDefault()

    // readonlyAndDownload가 true이면 드래그 이벤트 무시
    if (props.readonlyAndDownload) {
      return
    }

    // 파일 형식 검증
    const files = Array.from(e.dataTransfer?.files || [])

    // files가 비어있으면 items를 통해 파일 정보 확인
    let hasInvalidFile = false
    if (files.length === 0 && e.dataTransfer?.items) {
      for (let i = 0; i < e.dataTransfer.items.length; i++) {
        const item = e.dataTransfer.items[i]

        if (item.kind === 'file') {
          // MIME 타입을 통해 파일 확장자 추정
          const mimeType = item.type

          // MIME 타입을 확장자로 변환
          const mimeToExt: Record<string, string> = {
            'image/svg+xml': 'svg',
            'image/png': 'png',
            'image/jpeg': 'jpg',
            'image/jpg': 'jpg',
            'image/gif': 'gif',
            'application/pdf': 'pdf',
            'application/msword': 'doc',
            'application/vnd.openxmlformats-officedocument.wordprocessingml.document':
              'docx',
            'application/vnd.ms-excel': 'xls',
            'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet':
              'xlsx',
            'application/vnd.ms-powerpoint': 'ppt',
            'application/vnd.openxmlformats-officedocument.presentationml.presentation':
              'pptx',
            'text/plain': 'txt',
            'text/csv': 'csv',
          }

          const estimatedExt = mimeToExt[mimeType]

          if (estimatedExt) {
            const fileExp = new RegExp(
              '(' + props.fileFormat.join('|') + ')',
              'i',
            )
            hasInvalidFile =
              props.fileFormat.length > 0 && !fileExp.test(estimatedExt)
            break
          }
        }
      }
    } else {
      hasInvalidFile = files.some((file) => {
        const format = (file.name.split('.').pop() as string).toLowerCase()
        const fileExp = new RegExp('(' + props.fileFormat.join('|') + ')', 'i')
        return props.fileFormat.length > 0 && !fileExp.test(format)
      })
    }

    dropEffect.value = hasInvalidFile ? 'none' : 'copy'

    if (e.dataTransfer) {
      e.dataTransfer.dropEffect = dropEffect.value
    }
  }

  const handleDragOver = (e: DragEvent) => {
    e.preventDefault()

    // readonlyAndDownload가 true이면 드래그 이벤트 무시
    if (props.readonlyAndDownload) {
      return
    }

    isDraggingOver.value = true

    // dropEffect는 dragEnter에서 이미 설정되었으므로 여기서는 유지
    if (e.dataTransfer) {
      e.dataTransfer.dropEffect = dropEffect.value
    }
  }

  const handleDragDrop = (e: DragEvent) => {
    e.preventDefault()
    if (props.disabled || props.readonlyAndDownload) {
      isDraggingOver.value = false
      return
    }
    const droppedFiles = Array.from(e.dataTransfer?.files || [])
    processFiles(droppedFiles)
    isDraggingOver.value = false
  }

  const handleDragLeave = (e: DragEvent) => {
    e.preventDefault()

    // readonlyAndDownload가 true이면 드래그 이벤트 무시
    if (props.readonlyAndDownload) {
      return
    }

    isDraggingOver.value = false
  }

  const handleDragEnd = (e: DragEvent) => {
    e.preventDefault()

    // readonlyAndDownload가 true이면 드래그 이벤트 무시
    if (props.readonlyAndDownload) {
      return
    }

    isDraggingOver.value = false
    dropEffect.value = 'copy'
  }

  const singleFilename = computed(() =>
    props.modelValue[0] ? props.modelValue[0].name : '',
  )

  function processFiles(fileArr: File[]) {
    if (
      props.maxLength &&
      props.modelValue &&
      props.maxLength === props.modelValue.length
    ) {
      alert('파일 최대 업로드 개수는 ' + props.maxLength + '개입니다.')
      return
    }

    // 파일포맷 정규식
    const fileExp = new RegExp('(' + props.fileFormat.join('|') + ')', 'i')
    const initFileName = props.modelValue?.map((file) => file.name) ?? []
    let initFileSize =
      props.modelValue?.reduce((prev, file) => {
        return prev + file.size
      }, 0) ?? 0

    // 바이트로 변환
    const maxFilesize = (props.totalSize ?? 0) * 1024 * 1024

    for (const file of fileArr) {
      const format = (file.name.split('.').pop() as string).toLowerCase()

      if (file.name.length > 100) {
        alert('파일명은 100자 이하로 입력해주세요.')
        return
      }

      if (file.size === 0) {
        alert('파일이 비어있습니다.')
        return
      }

      if (DEFAULT_BLOCKED_FORMATS.includes(format)) {
        alert('차단된 파일 형식입니다.')
        return
      }

      if (props.fileFormat.length && !fileExp.test(format)) {
        alert(
          '파일 확장자를 확인해 주세요.' +
            ' (' +
            props.fileFormat.join(', ') +
            ')',
        )
        return
      }

      if (initFileName.some((name) => name === file.name)) {
        alert('이미 같은 파일명이 존재합니다.')
        return
      }

      if (maxFilesize && !props.multiple && file.size > maxFilesize) {
        alert('사이즈가 너무 큽니다.' + ` (최대 ${props.totalSize}MB)`)
        return
      } else if (maxFilesize && initFileSize + file.size > maxFilesize) {
        alert('사이즈가 너무 큽니다.' + ` (최대 ${props.totalSize}MB)`)
        return
      } else {
        initFileSize += file.size
      }
    }

    if (maxFilesize && fileArr[0].size > maxFilesize) {
      alert('사이즈가 너무 큽니다.' + `(최대 ${props.totalSize}MB)`)
      return
    }

    // 기존에 있던 파일과 업로드한 파일 합치기
    const totalFileArr = [...(props.modelValue ?? []), ...fileArr]

    if (
      props.maxLength &&
      props.modelValue &&
      props.maxLength < totalFileArr.length
    ) {
      alert('파일 최대 업로드 개수는 ' + props.maxLength + '개입니다.')
      return
    }

    // 기존에 있던 파일과 업로드한 파일 합치기
    const prevData = props.modelValue.map((ele) => ({
      name: ele.name,
      size: ele.size,
      file: ele.file,
      sortSeqNo: ele.sortSeqNo,
    }))

    const newData = fileArr.map((ele) => ({
      name: ele.name,
      size: ele.size,
      file: ele,
      sortSeqNo: -1,
    }))

    if (props.multiple) {
      const value = [...prevData, ...newData]
      emit('update:modelValue', value)
    } else {
      emit('update:modelValue', newData)
    }
    emit('change', fileArr)
  }

  const uploadHandler = (e: Event) => {
    const fileInput = e.target as HTMLInputElement
    const fileList = fileInput.files as any
    const fileArr: File[] = [...fileList]
    processFiles(fileArr)
    fileInput.value = ''
  }

  function triggerSelect() {
    if (props.disabled || props.readonlyAndDownload) return
    const inputEl = document.getElementById(uuid) as HTMLInputElement | null
    inputEl?.click()
  }

  function getFileFormat(filename: string): string {
    const ext = filename.split('.').pop()?.toLowerCase() || ''

    // 확장자별 아이콘 매핑
    const iconMap: Record<string, string> = {
      // 문서 파일
      doc: 'file-doc',
      docx: 'file-doc',
      // 이미지 파일
      gif: 'file-gif',
      bmp: 'file-bmp',
      jpg: 'file-jpg',
      jpeg: 'file-jpg',
      png: 'file-png',
      // 기타
      pdf: 'file-pdf',
      zip: 'file-zip',
      xls: 'file-xlsx',
      xlsx: 'file-xlsx',
      ppt: 'file-ppt',
      pptx: 'file-ppt',
      hwp: 'file-hwp',
    }

    return iconMap[ext] || 'file-attachment'
  }

  async function removeFile(file: FileValue) {
    const ok = await confirm('파일을 삭제하시겠습니까?')
    if (!ok) return
    emit('remove', file)
    emit(
      'update:modelValue',
      props.modelValue.filter((ele) => ele.name !== file.name),
    )
  }

  function removeAllFile() {
    if (!window.confirm('파일을 모두 삭제하시겠습니까?')) return
    emit('update:modelValue', [])
    emit('allRemove')
  }

  function byteToMB(bytes: number) {
    const mb = bytes / (1024 * 1024)
    return Math.round(mb * 10) / 10
  }
</script>

<template>
  <div class="VUpload">
    <template v-if="!multiple">
      <div
        class="text-grey-600 mb-3.75 flex h-auto min-h-[80px] w-full flex-col items-center justify-center rounded-lg border-2 border-dashed px-8 py-10 text-center text-sm transition-[background-color,border-color,box-shadow,transform] duration-[120ms] ease-in-out"
        :class="{
          'border-primary-700 bg-primary-700/10 shadow-[0_3px_12px_rgba(0,0,0,0.08)]':
            isDraggingOver && dropEffect === 'copy',
          'cursor-not-allowed border-red-500 bg-red-500/10':
            isDraggingOver && dropEffect === 'none',
          'border-red-500 bg-red-500/6 hover:bg-red-500/8': invalid,
          'border-grey-200 hover:border-grey-200 focus:border-grey-200 active:border-grey-200 cursor-default bg-white hover:transform-none hover:bg-white hover:shadow-none focus:bg-white focus:shadow-none focus:outline-none active:transform-none active:bg-white active:shadow-none':
            readonlyAndDownload,
          'border-grey-200 bg-white':
            !isDraggingOver && !invalid && !readonlyAndDownload,
        }"
        tabindex="0"
        :data-drop-effect="dropEffect"
        @dragenter="handleDragEnter"
        @dragleave="handleDragLeave"
        @dragover="handleDragOver"
        @drop="handleDragDrop"
        @dragend="handleDragEnd"
        @keydown.enter="triggerSelect"
      >
        <div class="flex w-full justify-center">
          <template v-if="!(modelValue && modelValue.length)">
            <div class="flex items-center gap-3">
              <div>
                <slot name="icon">
                  <VIconFont
                    icon="ic_upload"
                    class="mb-2.5 text-[36px] text-[var(--color-primary-700)]"
                  />
                </slot>
              </div>
              <div>
                <slot name="description">
                  <div
                    class="flex flex-col justify-center text-sm text-[#767676]"
                  >
                    <p v-if="readonlyAndDownload">업로드된 파일이 없습니다.</p>
                    <p v-else>파일을 선택하거나 여기로 드래그하세요</p>
                    <p v-if="totalSize && !readonlyAndDownload">
                      (최대 {{ totalSize }}MB)
                    </p>
                  </div>
                </slot>
                <Button
                  variant="outlined"
                  color="surface"
                  size="xs"
                  class="mt-4 px-[12px]!"
                  @click="triggerSelect"
                >
                  파일 선택
                </Button>
              </div>
            </div>
          </template>
          <template v-else>
            <div class="flex w-full justify-between">
              <div class="flex min-w-0 flex-1 items-start">
                <img
                  :src="`/icons/${getFileFormat(modelValue[0].name)}.svg`"
                  :alt="modelValue[0].name"
                  class="mr-2.5 h-5 w-[18px] shrink-0"
                />
                <strong
                  class="min-w-0 flex-1 cursor-pointer overflow-hidden text-ellipsis whitespace-nowrap"
                  tabindex="0"
                  @click.stop="emit('download', modelValue[0])"
                  @keydown.enter.stop="emit('download', modelValue[0])"
                  @keydown.stop
                >
                  {{ singleFilename }}
                </strong>
                <span
                  class="ml-2.5 shrink-0 text-[13px] font-normal text-[#767676]"
                  >{{ (modelValue[0].size / (1024 * 1024)).toFixed(1) }}MB</span
                >
              </div>
              <div v-if="!readonlyAndDownload">
                <button
                  class="text-grey-600 bg-grey-600 flex h-[20px] w-[20px] cursor-pointer items-center justify-center rounded-full"
                  @click.stop="removeFile(modelValue[0])"
                  @keydown.stop
                >
                  <IcIcon icon="close-bold" class="text-white" />
                </button>
              </div>
            </div>
          </template>
        </div>
      </div>
    </template>
    <template v-else>
      <div
        v-if="!readonlyAndDownload"
        class="mb-2.5 flex items-end justify-between"
      >
        <strong class="text-[13px] font-normal break-all text-[#767676]"
          >Total {{ modelValue?.length }} / {{ mbFileSize }}MB</strong
        >

        <div>
          <Button
            color="secondary"
            size="xs"
            :disabled="!modelValue?.length"
            @click="removeAllFile"
          >
            전체 삭제
          </Button>
        </div>
      </div>
      <div
        class="text-grey-600 mb-3.75 flex h-auto min-h-[80px] w-full flex-col items-center justify-center rounded-lg border-2 border-dashed px-8 py-10 text-center text-sm transition-[background-color,border-color,box-shadow,transform] duration-[120ms] ease-in-out"
        :class="{
          'border-primary-700 bg-primary-700/10 shadow-[0_3px_12px_rgba(0,0,0,0.08)]':
            isDraggingOver && dropEffect === 'copy',
          'cursor-not-allowed border-red-500 bg-red-500/10':
            isDraggingOver && dropEffect === 'none',
          'border-red-500 bg-red-500/6 hover:bg-red-500/8': invalid,
          'border-grey-200 hover:border-grey-200 focus:border-grey-200 active:border-grey-200 cursor-default bg-white hover:transform-none hover:bg-white hover:shadow-none focus:bg-white focus:shadow-none focus:outline-none active:transform-none active:bg-white active:shadow-none':
            readonlyAndDownload,
          'border-grey-200 bg-white':
            !isDraggingOver && !invalid && !readonlyAndDownload,
        }"
        :data-drop-effect="dropEffect"
        @dragenter="handleDragEnter"
        @dragleave="handleDragLeave"
        @dragover="handleDragOver"
        @drop="handleDragDrop"
        @dragend="handleDragEnd"
      >
        <div class="flex w-full justify-center">
          <div class="flex items-center gap-3">
            <div>
              <slot name="icon">
                <VIconFont
                  icon="ic_upload"
                  class="text-primary-700 mb-2.5 text-[36px]"
                />
              </slot>
            </div>
            <div>
              <slot name="description">
                <div class="text-grey-600 flex flex-col justify-center text-sm">
                  <p v-if="readonlyAndDownload">업로드된 파일이 없습니다.</p>
                  <p v-else>파일을 선택하거나 여기로 드래그하세요</p>
                  <p v-if="totalSize && !readonlyAndDownload">
                    (최대 {{ totalSize }}MB)
                  </p>
                </div>
              </slot>
              <Button
                variant="outlined"
                color="surface"
                size="xs"
                class="my-4 px-[12px]!"
                @click="triggerSelect"
              >
                파일 선택
              </Button>
            </div>
          </div>
        </div>

        <div class="max-h-[200px] w-full overflow-y-auto">
          <ul class="m-0 list-none p-0">
            <li
              v-for="file of modelValue"
              :key="file.name"
              class="mb-3.75 flex h-12 items-center justify-between rounded-lg border border-[var(--color-neutral-stroke-300)] px-4 last:mb-0"
            >
              <div class="flex min-w-0 flex-1 items-center">
                <img
                  :src="`/icons/${getFileFormat(file.name)}.svg`"
                  :alt="file.name"
                  class="mr-2.5 h-5 w-[18px] shrink-0"
                />
                <p
                  class="text-neutral-text-text min-w-0 overflow-hidden text-ellipsis whitespace-nowrap"
                  :class="{
                    'cursor-pointer': readonlyAndDownload,
                    'cursor-default': !readonlyAndDownload,
                  }"
                  tabindex="0"
                  @click.stop="emit('download', file)"
                  @keydown.enter.stop="emit('download', file)"
                  @keydown.stop
                >
                  {{ file.name }}
                </p>
                <span class="mx-2.5 text-[13px] font-normal text-[#767676]">
                  {{ byteToMB(file.size) }}MB
                </span>
              </div>
              <div v-if="!readonlyAndDownload">
                <button
                  class="text-grey-600 bg-grey-600 flex h-[20px] w-[20px] cursor-pointer items-center justify-center rounded-full"
                  :disabled="!modelValue?.length"
                  @click.stop="removeFile(file)"
                >
                  <IcIcon icon="close-bold" class="text-white" />
                </button>
              </div>
            </li>
          </ul>
        </div>
      </div>
    </template>

    <input
      v-show="false"
      :id="uuid"
      type="file"
      :accept="filteredAccept"
      :title="title"
      :multiple="multiple"
      @change="uploadHandler"
    />
  </div>
</template>
