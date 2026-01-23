import { defineStore } from 'pinia'
import { computed, reactive, ref, toRefs } from 'vue'
import type {
  NewRegSprtClubFacility,
  NewRegSprtClubReq,
  NewRegSprtClubRes,
  NewRegSprtClubSport,
} from '~/types/apis/registered-sports-club/newRegSprtClub.types'

export const useNewRegSprtClubStore = defineStore('newRegSprtClub', () => {
  /* =========================
   * state
   * ========================= */
  // state는 응답(res)만 유지한다.
  const newRegSportClubRes = reactive(_createInitialRes())

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

    setRes(next: Partial<NewRegSprtClubRes>) {
      _applyResPatch(next)
    },

    setMembers(next: Partial<NewRegSprtClubReq['members']>) {
      _applyMembersPatch(next)
    },

    initialize() {
      _resetState()
      // reset할 때 step index도 초기화
      currentIndex.value = 0
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
    currentIndex,
    getters,
    actions,
  }
})
