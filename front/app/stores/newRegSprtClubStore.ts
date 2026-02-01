import { defineStore } from 'pinia'
import { computed, reactive, ref, toRefs } from 'vue'
import type {
  NewRegSprtClubFacility,
  NewRegSprtClubReq,
  NewRegSprtClubRes,
  NewRegSprtClubSport,
} from '~/types/apis/registered-sports-club/newRegSprtClub.types'
import type { RegSportsClubApplicationUpsertRequest } from '~/types/models/RegSportsClubApplicationUpsertRequest'
import type { ApiResponseRegSportsClubApplicationResponse } from '~/types/models/ApiResponseRegSportsClubApplicationResponse'
import type { RegSportsClubApplicationResponse } from '~/types/models/RegSportsClubApplicationResponse'
import { API_BASE_URL } from '~/constants/url'

export const useNewRegSprtClubStore = defineStore('newRegSprtClub', () => {
  /* =========================
   * state
   * ========================= */
  // state는 응답(res)만 유지한다.
  const newRegSportClubRes = reactive(_createInitialRes())

  // 신청 응답 데이터 (Step3에서 사용)
  const applicationResponse = ref<RegSportsClubApplicationResponse | null>(null)

  // 저장 시 response로 내려온 applyId (저장/신청 시 다시 전달)
  const applyId = ref<number | null>(null)

  // UI state (0-based step index)
  const currentIndex = ref(0)
  const maxIndex = computed(() => 6)

  /* =========================
   * getters (PURE)
   * ========================= */
  const getters = {
    dto: {
      req() {
        return _toReq(newRegSportClubRes)
      },
    },
    validate: {
      /**
       * 최소 필수값 검증(현재 Step3 기준)
       * - 실제 룰(yup/vee-validate)로 넘어가면 이 getter는 참고용으로만 사용
       */
      hasError() {
        return _getErrorList(_toReq(newRegSportClubRes)).length > 0
      },
      errorList() {
        return _getErrorList(_toReq(newRegSportClubRes))
      },
    },
  }

  /* =========================
   * actions (PUBLIC API)
   * ========================= */
  const actions = {
    step: {
      setIndex(next: number) {
        if (!Number.isFinite(next)) return
        currentIndex.value = Math.min(
          Math.max(0, Math.floor(next)),
          maxIndex.value,
        )
      },
      next() {
        actions.step.setIndex(currentIndex.value + 1)
      },
      prev() {
        actions.step.setIndex(currentIndex.value - 1)
      },
    },

    fetch: {
      async detail() {
        const data = await _fetchMockDetail()
        _applyDetail(data)
      },
    },

    /**
     * route.query.applyId 등 외부에서 applyId 설정 (onMounted 시 사용)
     */
    setApplyId(id: string | string[] | number | undefined) {
      if (id == null) {
        applyId.value = null
        return
      }
      const num =
        typeof id === 'number'
          ? id
          : parseInt(Array.isArray(id) ? id[0] : id, 10)
      applyId.value = Number.isFinite(num) ? num : null
    },

    /**
     * Step2 폼 데이터로 UpsertRequest 생성 (저장/신청 공통)
     */
    _formToUpsertRequest(formValues: {
      applicantName: string
      applicantTelno: string
      applicantEmail: string
      name: string
      location: string
      representativeName: string
      representativeTelno: string
      businessNo: string
      operatingSportParentCodeId: { id?: number } | null
      operatingSportChildCodeId: { id?: number } | null
    }): RegSportsClubApplicationUpsertRequest {
      const req: RegSportsClubApplicationUpsertRequest = {
        applicantName: formValues.applicantName || '',
        applicantTelno: formValues.applicantTelno || '',
        applicantEmail: formValues.applicantEmail || '',
        clubName: formValues.name || '',
        location: formValues.location || undefined,
        representativeName: formValues.representativeName || undefined,
        representativeTelno: formValues.representativeTelno || undefined,
        businessNo: formValues.businessNo || undefined,
        operatingSportParentCodeId:
          formValues.operatingSportParentCodeId?.id ?? undefined,
        operatingSportChildCodeId:
          formValues.operatingSportChildCodeId?.id ?? undefined,
      }
      if (applyId.value != null) req.applyId = applyId.value
      return req
    },

    /**
     * 저장 API (상태 전이 없이 신청 데이터 저장, response.applyId 저장)
     */
    async saveApplication(formValues: {
      applicantName: string
      applicantTelno: string
      applicantEmail: string
      name: string
      location: string
      representativeName: string
      representativeTelno: string
      businessNo: string
      operatingSportParentCodeId: { id?: number } | null
      operatingSportChildCodeId: { id?: number } | null
    }) {
      const requestBody = actions._formToUpsertRequest(formValues)
      const response =
        await $fetch<ApiResponseRegSportsClubApplicationResponse>(
          `${API_BASE_URL}/reg-sports-club-applications/save`,
          {
            method: 'POST',
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' },
            body: requestBody,
          },
        )
      if (response.resultCode !== 200) {
        throw new Error(response.resultMessage || '저장에 실패했습니다.')
      }
      if (response.data?.applyId != null) {
        applyId.value = response.data.applyId
      }
      return response.data
    },

    /**
     * 신청 API (BPM 상태 전이 수행)
     */
    async applyApplication(formValues: {
      applicantName: string
      applicantTelno: string
      applicantEmail: string
      name: string
      location: string
      representativeName: string
      representativeTelno: string
      businessNo: string
      operatingSportParentCodeId: { id?: number } | null
      operatingSportChildCodeId: { id?: number } | null
    }) {
      const requestBody = actions._formToUpsertRequest(formValues)
      const response =
        await $fetch<ApiResponseRegSportsClubApplicationResponse>(
          `${API_BASE_URL}/reg-sports-club-applications/apply`,
          {
            method: 'POST',
            credentials: 'include',
            headers: { 'Content-Type': 'application/json' },
            body: requestBody,
          },
        )
      if (response.resultCode !== 200) {
        throw new Error(response.resultMessage || '신청에 실패했습니다.')
      }
      applicationResponse.value = response.data ?? null
      if (response.data?.applyId != null) {
        applyId.value = response.data.applyId
      }
      return response.data
    },

    setRes(next: Partial<NewRegSprtClubRes>) {
      _applyResPatch(next)
    },

    setMembers(next: Partial<NewRegSprtClubReq['members']>) {
      _applyMembersPatch(next)
    },

    initialize() {
      _resetState()
      currentIndex.value = 0
      applicationResponse.value = null
      applyId.value = null
    },
  }

  async function _fetchMockDetail(): Promise<NewRegSprtClubRes> {
    // TODO: 실제 API 연동 시 교체
    return {
      id: 'mock-registered-sports-club-1',
      updatedAt: new Date().toISOString(),
      corpOrgName: '대한스포츠클럽(가칭)',
      corpEstablishedAt: '2020-01-01',
      representativeName: '홍길동',
      representativeBirthDate: '1990-05-03',
      clubName: '대한스포츠클럽',
      clubEstablishedAt: '2019-03-15',
      region: '서울특별시',
      mainPhone: '02-1234-5678',
      facilities: [
        {
          id: 'fac-1',
          name: '마포구민체육센터',
          address: '서울특별시 마포구 월드컵로 240',
          note: '실내체육관/다목적',
        },
      ],
      sports: [
        {
          id: 'sprt-1',
          name: '축구',
          category: '구기',
          note: '11인/풋살 포함',
        },
      ],
      members: {
        femaleMembers: 12,
        youthMembers: 8,
        severeDisabilityMembers: 1,
        physicalDisabilityMembers: 2,
        totalMembers: 30,
        visualDisabilityMembers: 0,
        autismDisabilityMembers: 0,
        developmentalDisabilityMembers: 0,
        otherDisabilityMembers: 0,
      },
    }
  }

  /* =========================
   * private (store-internal)
   * ========================= */
  const _resetState = () => {
    const next = _createInitialRes()
    ;(Object.keys(next) as Array<keyof NewRegSprtClubRes>).forEach((k) => {
      newRegSportClubRes[k] = next[k]
    })
  }

  /* =========================
   * private helpers
   * ========================= */
  const _toReq = (res: NewRegSprtClubRes): NewRegSprtClubReq => {
    return {
      corpOrgName: res.corpOrgName ?? '',
      corpEstablishedAt: res.corpEstablishedAt ? res.corpEstablishedAt : null,
      representativeName: res.representativeName ?? '',
      representativeBirthDate: res.representativeBirthDate
        ? res.representativeBirthDate
        : null,
      clubName: res.clubName ?? '',
      clubEstablishedAt: res.clubEstablishedAt ? res.clubEstablishedAt : null,
      region: res.region ?? null,
      mainPhone: res.mainPhone ?? '',
      facilities: res.facilities ?? [],
      sports: res.sports ?? [],
      members: {
        ..._createInitialMembers(),
        ...(res.members ?? {}),
      },
    }
  }

  // ! @TODO: 이거 mergeValidator 추가하고, 첨부파일이랑 step 이랑 연동 해야함
  const _getErrorList = (req: NewRegSprtClubReq) => {
    const errors: string[] = []
    if (!req.corpOrgName) errors.push('법인 단체명은 필수입니다.')
    if (!req.corpEstablishedAt) errors.push('법인 설립일은 필수입니다.')
    if (!req.representativeName) errors.push('대표자명은 필수입니다.')
    if (!req.representativeBirthDate)
      errors.push('대표자 생년월일은 필수입니다.')
    if (!req.clubName) errors.push('클럽명은 필수입니다.')
    if (!req.clubEstablishedAt) errors.push('클럽 설립일은 필수입니다.')
    if (!req.region) errors.push('등록지역은 필수입니다.')
    if (!req.mainPhone) errors.push('대표전화번호는 필수입니다.')
    if (!req.facilities?.length)
      errors.push('사용시설은 1개 이상 선택이 필요합니다.')
    if (!req.sports?.length)
      errors.push('운영종목은 1개 이상 선택이 필요합니다.')
    return errors
  }

  const _applyDetail = (data: NewRegSprtClubRes) => {
    ;(Object.keys(data) as Array<keyof NewRegSprtClubRes>).forEach((k) => {
      newRegSportClubRes[k] = data[k]
    })
  }

  const _applyResPatch = (next: Partial<NewRegSprtClubRes>) => {
    Object.assign(newRegSportClubRes, next)
  }

  const _applyMembersPatch = (next: Partial<NewRegSprtClubReq['members']>) => {
    newRegSportClubRes.members = {
      ...newRegSportClubRes.members,
      ...next,
    }
  }

  function _createInitialMembers(): NewRegSprtClubReq['members'] {
    return {
      femaleMembers: null,
      youthMembers: null,
      severeDisabilityMembers: null,
      physicalDisabilityMembers: null,
      totalMembers: null,
      visualDisabilityMembers: null,
      autismDisabilityMembers: null,
      developmentalDisabilityMembers: null,
      otherDisabilityMembers: null,
    }
  }

  function _createInitialRes(): NewRegSprtClubRes {
    return {
      id: null,
      updatedAt: null,
      corpOrgName: '',
      corpEstablishedAt: null,
      representativeName: '',
      representativeBirthDate: null,
      clubName: '',
      clubEstablishedAt: null,
      region: null,
      mainPhone: '',
      facilities: _createInitialFacilities(),
      sports: _createInitialSports(),
      members: _createInitialMembers(),
    }
  }

  function _createInitialFacilities(): NewRegSprtClubFacility[] {
    return []
  }

  function _createInitialSports(): NewRegSprtClubSport[] {
    return []
  }

  return {
    ...toRefs(newRegSportClubRes),
    newRegSportClubRes,
    applicationResponse,
    applyId,
    currentIndex,
    getters,
    actions,
  }
})
