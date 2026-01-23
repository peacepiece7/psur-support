import { ref, toValue, watch } from 'vue'

// 파일 업로드 타입
type FileUploadType = {
  file?: File | null
  sortSeqNo: number
  fileLnkTpCd?: string
  thumbUsagYn?: string
  thumbWdthSize?: string
  thumbVelSize?: string
}

type FileValue = {
  name: string
  size: number
  sortSeqNo: number
  file: File | null
  fileUuid: string
  fileLnkTpCd?: string
  thumbUsagYn?: string
  thumbWdthSize?: string
  thumbVelSize?: string
}

/**
 * 저장api 호출시 파일을 저장하는 함수
 * @param uuid 파일 uuid
 * @param onStart 파일 저장 시작시 호출되는 함수
 * @param onDone 파일 저장 완료시 호출되는 함수
 * @returns fileValue: 파일값, fileSave: 파일 저장 함수
 *
 */
export default function useFileSave(
  uuid?: (() => string | undefined) | string,
  onStart?: () => void,
  onDone?: (val: FileValue[]) => void,
) {
  const fileValue = ref<FileValue[]>([])
  const { fileList, fileMultiUpload, fileDelete } = useFileUpload()

  // 파일 저장
  const fileSave: (val?: FileValue[]) => Promise<string> = async (val) => {
    // 파일 UUID
    const fileUUID = toValue(uuid) || ''
    const fileVal = val || fileValue.value

    // 가지고있는 시퀀스 번호
    const fileSeq = fileVal.map((ele) => ele.sortSeqNo)

    const file = fileValueToFileUploadFormat(fileVal)

    if (fileUUID) {
      const list = await fileList({
        fileUuid: fileUUID,
      })

      const removeFile = list.filter(
        (ele) => !fileSeq.includes(Number(ele.sortSeqNo)),
      )

      // 제거된 파일이 있는지 확인 후 제거
      if (removeFile.length) {
        // 파일 삭제
        for (const ele of removeFile) {
          await fileDelete({
            fileUuid: fileUUID,
            sortSeqNo: ele.sortSeqNo,
          })
        }
      }

      if (file.length) {
        // 파일 저장
        await fileMultiUpload({
          files: file,
          fileUuid: fileUUID,
        })
      }

      return fileUUID
    }

    if (!fileVal.length) return

    if (file.length) {
      // 파일 신규 저장
      const uuid = await fileMultiUpload({
        files: file,
      })

      return uuid
    }

    return ''
  }

  // 파일 값 -> 파일 업로드 타입으로 변환
  function fileValueToFileUploadFormat(val: FileValue[]) {
    return val.map((ele) => {
      const fileUploadType: FileUploadType = {
        sortSeqNo: ele.sortSeqNo,
        file: ele.file || null,
      }

      if (ele.fileLnkTpCd) fileUploadType.fileLnkTpCd = ele.fileLnkTpCd
      if (ele.thumbUsagYn) fileUploadType.thumbUsagYn = ele.thumbUsagYn
      if (ele.thumbWdthSize) fileUploadType.thumbWdthSize = ele.thumbWdthSize
      if (ele.thumbVelSize) fileUploadType.thumbVelSize = ele.thumbVelSize

      return fileUploadType
    })
  }

  watch(
    () => toValue(uuid),
    async (value) => {
      if (value) {
        if (typeof onStart === 'function') {
          onStart()
        }

        const list = await fileList({
          fileUuid: value,
        })

        fileValue.value = list.map((ele) => {
          return {
            name: ele.fileNm,
            size: ele.fileSize,
            sortSeqNo: Number(ele.sortSeqNo),
            file: null,
            fileUuid: value ?? '',
            fileLnkTpCd: ele.fileLnkTpCd,
            thumbUsagYn: (ele.thumbUsagYn?.toString() as string) || '',
            thumbWdthSize: (ele.thumbWdthSize?.toString() as string) || '',
            thumbVelSize: (ele.thumbVelSize?.toString() as string) || '',
          }
        })
      }
      if (typeof onDone === 'function') {
        onDone(fileValue.value)
      }
    },
    { immediate: true },
  )

  return { fileValue, fileSave }
}
