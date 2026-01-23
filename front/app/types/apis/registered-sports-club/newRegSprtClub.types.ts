export type NewRegSprtClubMembers = {
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

export type NewRegSprtClubFacility = {
  id: string
  name: string
  address: string
  note?: string
}

export type NewRegSprtClubSport = {
  id: string
  name: string
  category?: string
  note?: string
}

/**
 * 등록 스포츠클럽 신규 신청(임시저장/제출 공용 DTO)
 * - Step3(클럽 정보 입력) 기준으로 구성
 */
export type NewRegSprtClubReq = {
  /** 법인 단체명 */
  corpOrgName: string
  /** 법인 설립일 (YYYY-MM-DD) */
  corpEstablishedAt: string | null
  /** 대표자명 */
  representativeName: string
  /** 대표자 생년월일 (YYYY-MM-DD) */
  representativeBirthDate: any
  /** 클럽명 */
  clubName: string
  /** 클럽 설립일 (YYYY-MM-DD) */
  clubEstablishedAt: string | null
  /** 등록지역 */
  region: string | null
  /** 대표전화번호 */
  mainPhone: string
  /** 사용시설 */
  facilities: NewRegSprtClubFacility[]
  /** 운영종목 */
  sports: NewRegSprtClubSport[]

  /** 회원 구성 */
  members: NewRegSprtClubMembers
}

/**
 * 신규 신청 조회/불러오기 응답 DTO (mock 포함)
 * - 현재는 Req와 동일한 형태로 내려온다고 가정
 */
export type NewRegSprtClubRes = NewRegSprtClubReq & {
  /** 서버 식별자(없으면 null) */
  id?: string | null
  /** 마지막 저장 시각(없으면 null) */
  updatedAt?: string | null
}
