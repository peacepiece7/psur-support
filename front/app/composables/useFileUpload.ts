const baseURL = `/common/file/`

/**
 * 파일 리스트 파라미터
 */
type FileList = {
  fileUuid: string //UUID
  sortSeqNo?: string // 정렬번호
  pageNo?: number // 페이지번호
  imgYn?: 'Y' | 'N' // 이지미여부
  thumbUsagYn?: 'Y' | 'N' // 썸네일여부
}

type FileInit = {
  fileUuid: string //UUID
  sortSeqNo?: string // 정렬번호
}

/**
 * 파일 정보
 */
export type FileInfo = {
  fileUuid: string // 파일UUID
  sortSeqNo: string // 정렬순서번호
  fileTpVl: string // 파일유형값
  fileLnkTpCd: string // 파일연결유형코드
  actlFileNm: string // 실제파일명
  fileNm: string // 파일명
  fileSize: number // 파일사이즈
  filePth: string // 파일경로
  fileExtsNm: string // 파일확장자명
  usagYn: string // 사용여부
  imgYn: string // 이미지여부
  thumbWdthSize: number // 썸네일가로사이즈
  thumbVelSize: number // 썸네일세로사이즈
  thumbUsagYn: string // 썸네일사용여부
  delYn: string // 삭제여부
  frstRegDttm: string // 최초등록일시
  frstRegpMemNo: number // 최초등록자회원번호
  lstUpdDttm: string // 최종수정일시
  lstAmdrMemNo: number // 최종수정자회원번호
}

declare global {
  interface Window {
    cachedIp: string
  }
}

/**
 * 파일 업로드 컴포저블
 * @description
 * fileDelete : 파일 삭제 함수
 *
 * fileList : 파일 리스트형태로 가져오는 함수
 *
 * fileImage : 이미지 URL로 가져오는 함수
 */
function useFileUpload() {
  const { $api } = useNuxtApp()

  /**
   * 파일 삭제 API
   * @property fileUuid UUID*
   * @property sortSeqNo UUID에 해당 파일 순서
   */
  async function fileDelete(data: FileInit): Promise<boolean> {
    const res = await $api<number>(baseURL + data.fileUuid, {
      method: 'DELETE',
      query: { sortSeqNo: data.sortSeqNo },
    })

    return res === 1
  }

  /**
   * 파일 리스트 파일 정보를 배열로 반환한다.
   * @property fileUuid UUID*
   * @property sortSeqNo UUID에 해당 파일 순서
   * @property pageNo  페이지번호
   * @property imgYn 이지미여부
   * @property thumbUsagYn 썸네일여부
   */
  async function fileList(data: FileList): Promise<FileInfo[]> {
    const { fileUuid, ...param } = data
    // 서버사이드에서도 사용되어야하기에 $api로 호출
    const res = await $api<{ total: number; list: FileInfo[] }>(
      '/common/file/' + fileUuid,
      {
        query: param,
      },
    )

    return res.list
  }

  /**
   * 이미지파일 호출
   * @property fileUuid string UUID*
   * @property sortSeqNo  UUID에 해당 파일 순서
   * @property thumbUsagYn 썸네일여부
   * @property fileLnkTpCd 파일구분자
   */
  function fileImage(data: {
    fileUuid: string
    sortSeqNo?: number | string
    thumbUsagYn?: 'Y' | 'N'
    fileLnkTpCd?: string
  }) {
    const { fileUuid, ...rest } = data

    let query = ''
    let dataCount = 0
    for (const key in rest) {
      const uniqueKey = key as keyof typeof rest
      if (rest[uniqueKey]) {
        if (dataCount === 0) {
          query += `?${uniqueKey}=${rest[uniqueKey]}`
          dataCount++
        } else {
          query += `&${uniqueKey}=${rest[uniqueKey]}`
        }
      }
    }

    return baseURL + 'image/' + data.fileUuid + query
  }

  /**
   * 파일 다운로드 함수이다. 다중파일일때는 zip로 다운된다.
   * @property fileUuid UUID*
   * @property sortSeqNo UUID에 해당 파일 순서
   */
  async function fileDownload(data: FileInit) {
    try {
      // ios 한글 깨짐 현상 해결을 위해 axios로 직접 blob을 받아옴
      const res = await $api.raw<Blob>(
        '/api/common/file/download/' + data.fileUuid,
        { params: { sortSeqNo: data.sortSeqNo ?? '' } },
      )

      // blob에서 기존 파일명 추출
      const contentDisposition = res.headers.get('content-disposition')
      const fileName = contentDisposition
        .split('filename=')[1]
        .replace(/"/g, '')

      // blob 다운로드
      const url = window.URL.createObjectURL(new Blob([res._data]))

      const a = document.createElement('a')

      document.body.appendChild(a)
      a.download = decodeURIComponent(fileName) // a tag에 download 속성을 줘서 클릭할 때 다운로드가 일어날 수 있도록 하기
      a.href = url // href에 url 달아주기

      a.click() // 코드 상으로 클릭을 해줘서 다운로드를 트리거

      document.body.removeChild(a) // cleanup - 쓰임을 다한 a 태그 삭제
      window.URL.revokeObjectURL(url) // cleanup - 쓰임을 다한 url 객체 삭제
    } catch (err) {
      alert('파일이 없습니다.' + `(${data.fileUuid})`)
    }
  }

  /**
   * 파일을 복사한다.
   * @property fileUuid UUID*
   * @property sortSeqNo UUID에 해당 파일 순서
   * @returns UUID
   */
  async function fileCopy(fileInit: FileInit): Promise<string> {
    const res = await $api<string>(baseURL + 'copy/' + fileInit.fileUuid, {
      params: {
        sortSeqNo: fileInit.sortSeqNo ?? '',
      },
    })

    return res
  }

  /**
   * 다중 파일 업로드
   * @description
   * UUID 있을때 : 기존 UUID에 파일 추가
   *
   * UUID 없을때 : 신규업로드 UUID 생성
   * @property  fileUuid 파일 UUID(필수)
   * @property  fileLnkTpCd 사용자 정의 구분자 (디폴드 : public)
   * @property  thumbUsagYn 썸네일 여부 (디폴드 : N)
   * @property  thumbWdthSize 썸네일 가로 사이즈 (디폴드 : 200)
   * @property  thumbVelSize 썸네일 세로 사이즈 (디폴드 : 100)
   */
  async function fileUpload(fileInfo: {
    files: File[]
    fileUuid?: string
    fileLnkTpCd?: string
    thumbUsagYn?: string
    thumbWdthSize?: string
    thumbVelSize?: string
  }): Promise<string> {
    const formdata = new FormData()

    for (const key in fileInfo) {
      if (key !== 'files') {
        const ele = fileInfo as any
        if (ele[key]) {
          formdata.append(key, ele[key])
        }
      }
    }

    fileInfo.files.forEach((file) => {
      formdata.append('files', file)
    })

    const data = await $api<string>(baseURL + 'upload/', {
      method: 'POST',
      body: formdata,
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    })

    return data
  }

  /**
   * 단일 파일 업로드
   * @description
   * UUID 있을때 : 기존 UUID 삭제 후 업로드
   *
   * UUID 없을때 : 신규 업로드
   * @property  fileUuid 파일 UUID(필수)
   * @property  fileLnkTpCd 사용자 정의 구분자 (디폴드 : public)
   * @property  thumbUsagYn 썸네일 여부 (디폴드 : N)
   * @property  thumbWdthSize 썸네일 가로 사이즈 (디폴드 : 200)
   * @property  thumbVelSize 썸네일 세로 사이즈 (디폴드 : 100)
   */
  async function fileSingleUpload(data: {
    files: File
    fileUuid?: string
    fileLnkTpCd?: string
    thumbUsagYn?: string
    thumbWdthSize?: string
    thumbVelSize?: string
  }): Promise<string> {
    const formdata = new FormData()
    for (const key in data) {
      if (key !== 'files') {
        const ele = data as any
        if (ele[key]) {
          formdata.append(key, ele[key])
        }
      }
    }
    formdata.append('files', data.files)

    const res = await $api<string>(baseURL + 'single/upload/', {
      method: 'POST',
      body: formdata,
      headers: { 'Content-Type': 'multipart/form-data' },
    })

    return res
  }

  async function fileMultiUpload(params: {
    files: {
      file?: File | null
      sortSeqNo: number
      fileLnkTpCd?: string
      thumbUsagYn?: string
      thumbWdthSize?: string
      thumbVelSize?: string
    }[]
    fileUuid?: string
  }) {
    const formdata = new FormData()

    params.files.forEach((ele) => {
      for (const key in ele) {
        if (key === 'file') {
          const data = ele as any
          // if (data) {
          formdata.append('files', data[key] || '')
          // }
        } else {
          const data = ele as any
          if (data[key]) {
            formdata.append(key, data[key])
          }
        }
      }
      if (params.fileUuid) {
        formdata.append('fileUuid', params.fileUuid)
      }
    })

    const res = await $api<string>(baseURL + 'upload/', {
      method: 'POST',
      body: formdata,
      headers: { 'Content-Type': 'multipart/form-data' },
    })

    return res
  }

  return {
    fileList,
    fileDelete,
    fileDownload,
    fileCopy,
    fileImage,
    fileSingleUpload,
    fileUpload,
    fileMultiUpload,
  }
}

export default useFileUpload
