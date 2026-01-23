export type PubGuideMembers = {
  /** 여성 회원 */
  femaleMembers: number | null
  /** 청소년 회원 */
  youthMembers: number | null
  /** 중증 장애 */
  severeDisabilityMembers: number | null
  /** 지체 장애 */
  physicalDisabilityMembers: number | null
  /** 전체 회원 */
  totalMembers: number | null
  /** 시각 장애 */
  visualDisabilityMembers: number | null
  /** 자폐 장애 */
  autismDisabilityMembers: number | null
  /** 발달 장애 */
  developmentalDisabilityMembers: number | null
  /** 기타 장애 */
  otherDisabilityMembers: number | null
}

export type PubGuideFacility = {
  id: string
  name: string
  address: string
  note?: string
}

export type PubGuideSport = {
  id: string
  name: string
  category?: string
  note?: string
}

export type PubGuideStr = {
  name1: string
  name2: string
  name3: string
  name4: string
  name5: string
}

export type PubGuideNum = {
  name1: number | null
  name2: number | null
  name3: number | null
  name4: number | null
  name5: number | null
}

export type PubGuidePhone = {
  name1: string
  name2: string
  name3: string
  name4: string
  name5: string
}

export type PubGuideTel = {
  name1: string
  name2: string
  name3: string
  name4: string
  name5: string
}

export type PubGuideDate = {
  name1: string | null
  name2: string | null
  name3: string | null
  name4: string | null
  name5: string | null
}

export type PubGuideAny = {
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  name1: any
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  name2: any
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  name3: any
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  name4: any
  // eslint-disable-next-line @typescript-eslint/no-explicit-any
  name5: any
}

export type PubGuideCode = {
  name1: string | null
  name2: string | null
  name3: string | null
  name4: string | null
  name5: string | null
}

export type PubGuideGp1 = {
  name1: string | null
  name2: string | null
  name3: string | null
  name4: string | null
  name5: string | null
}

export type PubGuideGp2 = {
  name1: string | null
  name2: string | null
  name3: string | null
  name4: string | null
  name5: string | null
}

export type PubGuideGp3 = {
  name1: string | null
  name2: string | null
  name3: string | null
  name4: string | null
  name5: string | null
}

export type PubGuideGp4 = {
  name1: string | null
  name2: string | null
  name3: string | null
  name4: string | null
  name5: string | null
}

export type PubGuideGp5 = {
  name1: string | null
  name2: string | null
  name3: string | null
  name4: string | null
  name5: string | null
}

export type PubGuideGp = {
  gp1: PubGuideGp1
  gp2: PubGuideGp2
  gp3: PubGuideGp3
  gp4: PubGuideGp4
  gp5: PubGuideGp5
}

/**
 * 등록 스포츠클럽 신규 신청(임시저장/제출 공용 DTO)
 * - Step3(클럽 정보 입력) 기준으로 구성
 */
export type PubGuideReq = {
  /** 문자열 필드 */
  str: PubGuideStr
  /** 숫자 필드 */
  num: PubGuideNum
  /** 전화번호 필드 */
  phone: PubGuidePhone
  /** 전화번호 필드 (tel) */
  tel: PubGuideTel
  /** 날짜 필드 */
  date: PubGuideDate
  /** any 타입 필드 */
  any: PubGuideAny
  /** 코드 필드 */
  code: PubGuideCode
  /** 그룹 필드 */
  gp: PubGuideGp
  /** 사용시설 */
  facilities: PubGuideFacility[]
  /** 운영종목 */
  sports: PubGuideSport[]
  /** 회원 구성 */
  members: PubGuideMembers
}

/**
 * 신규 신청 조회/불러오기 응답 DTO (mock 포함)
 * - 현재는 Req와 동일한 형태로 내려온다고 가정
 */
export type PubGuideRes = PubGuideReq & {
  /** 서버 식별자(없으면 null) */
  id?: string | null
  /** 마지막 저장 시각(없으면 null) */
  updatedAt?: string | null
}
