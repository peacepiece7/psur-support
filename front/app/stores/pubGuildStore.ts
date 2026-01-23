import { defineStore } from 'pinia'
import { computed, reactive, ref, toRefs } from 'vue'
import type {
  PubGuideFacility,
  PubGuideReq,
  PubGuideRes,
  PubGuideSport,
  PubGuideStr,
  PubGuideNum,
  PubGuidePhone,
  PubGuideTel,
  PubGuideDate,
  PubGuideAny,
  PubGuideCode,
  PubGuideGp,
} from '~/types/apis/pub-guide/pubGuide.types'

export const usePubGuideStore = defineStore('pubGuideStore', () => {
  /* =========================
   * state
   * ========================= */
  // state는 응답(res)만 유지한다.
  const pubGuideRes = reactive(_createInitialRes())

  // UI state (0-based step index)
  const currentIndex = ref(0)
  const maxIndex = computed(() => 6)

  /* =========================
   * getters (PURE)
   * ========================= */
  const getters = {
    dto: {
      req() {
        return _toReq(pubGuideRes)
      },
    },
    validate: {
      /**
       * 최소 필수값 검증(현재 Step3 기준)
       * - 실제 룰(yup/vee-validate)로 넘어가면 이 getter는 참고용으로만 사용
       */
      hasError() {
        return _getErrorList(_toReq(pubGuideRes)).length > 0
      },
      errorList() {
        return _getErrorList(_toReq(pubGuideRes))
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

    setRes(next: Partial<PubGuideRes>) {
      _applyResPatch(next)
    },

    setMembers(next: Partial<PubGuideReq['members']>) {
      _applyMembersPatch(next)
    },

    initialize() {
      _resetState()
      // reset할 때 step index도 초기화
      currentIndex.value = 0
    },
  }

  /* =========================
   * 여기 아래로 볼 필요 없음
   * ========================= */
  async function _fetchMockDetail(): Promise<PubGuideRes> {
    // TODO: 실제 API 연동 시 교체
    return {
      id: 'mock-registered-sports-club-1',
      updatedAt: new Date().toISOString(),
      str: {
        name1: '대한스포츠클럽(가칭)',
        name2: '홍길동',
        name3: '대한스포츠클럽',
        name4: '',
        name5: '',
      },
      num: {
        name1: null,
        name2: null,
        name3: null,
        name4: null,
        name5: null,
      },
      phone: {
        name1: '02-1234-5678',
        name2: '',
        name3: '',
        name4: '',
        name5: '',
      },
      tel: {
        name1: '',
        name2: '',
        name3: '',
        name4: '',
        name5: '',
      },
      date: {
        name1: '2020-01-01',
        name2: '1990-05-03',
        name3: '2019-03-15',
        name4: null,
        name5: null,
      },
      any: {
        name1: null,
        name2: null,
        name3: null,
        name4: null,
        name5: null,
      },
      code: {
        name1: '서울특별시',
        name2: null,
        name3: null,
        name4: null,
        name5: null,
      },
      gp: {
        gp1: {
          name1: null,
          name2: null,
          name3: null,
          name4: null,
          name5: null,
        },
        gp2: {
          name1: null,
          name2: null,
          name3: null,
          name4: null,
          name5: null,
        },
        gp3: {
          name1: null,
          name2: null,
          name3: null,
          name4: null,
          name5: null,
        },
        gp4: {
          name1: null,
          name2: null,
          name3: null,
          name4: null,
          name5: null,
        },
        gp5: {
          name1: null,
          name2: null,
          name3: null,
          name4: null,
          name5: null,
        },
      },
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
    Object.assign(pubGuideRes, next)
  }

  /* =========================
   * private helpers
   * ========================= */
  const _toReq = (res: PubGuideRes): PubGuideReq => {
    return {
      str: {
        name1: res.str?.name1 ?? '',
        name2: res.str?.name2 ?? '',
        name3: res.str?.name3 ?? '',
        name4: res.str?.name4 ?? '',
        name5: res.str?.name5 ?? '',
      },
      num: {
        name1: res.num?.name1 ?? null,
        name2: res.num?.name2 ?? null,
        name3: res.num?.name3 ?? null,
        name4: res.num?.name4 ?? null,
        name5: res.num?.name5 ?? null,
      },
      phone: {
        name1: res.phone?.name1 ?? '',
        name2: res.phone?.name2 ?? '',
        name3: res.phone?.name3 ?? '',
        name4: res.phone?.name4 ?? '',
        name5: res.phone?.name5 ?? '',
      },
      tel: {
        name1: res.tel?.name1 ?? '',
        name2: res.tel?.name2 ?? '',
        name3: res.tel?.name3 ?? '',
        name4: res.tel?.name4 ?? '',
        name5: res.tel?.name5 ?? '',
      },
      date: {
        name1: res.date?.name1 ?? null,
        name2: res.date?.name2 ?? null,
        name3: res.date?.name3 ?? null,
        name4: res.date?.name4 ?? null,
        name5: res.date?.name5 ?? null,
      },
      any: {
        name1: res.any?.name1 ?? null,
        name2: res.any?.name2 ?? null,
        name3: res.any?.name3 ?? null,
        name4: res.any?.name4 ?? null,
        name5: res.any?.name5 ?? null,
      },
      code: {
        name1: res.code?.name1 ?? null,
        name2: res.code?.name2 ?? null,
        name3: res.code?.name3 ?? null,
        name4: res.code?.name4 ?? null,
        name5: res.code?.name5 ?? null,
      },
      gp: {
        gp1: {
          name1: res.gp?.gp1?.name1 ?? null,
          name2: res.gp?.gp1?.name2 ?? null,
          name3: res.gp?.gp1?.name3 ?? null,
          name4: res.gp?.gp1?.name4 ?? null,
          name5: res.gp?.gp1?.name5 ?? null,
        },
        gp2: {
          name1: res.gp?.gp2?.name1 ?? null,
          name2: res.gp?.gp2?.name2 ?? null,
          name3: res.gp?.gp2?.name3 ?? null,
          name4: res.gp?.gp2?.name4 ?? null,
          name5: res.gp?.gp2?.name5 ?? null,
        },
        gp3: {
          name1: res.gp?.gp3?.name1 ?? null,
          name2: res.gp?.gp3?.name2 ?? null,
          name3: res.gp?.gp3?.name3 ?? null,
          name4: res.gp?.gp3?.name4 ?? null,
          name5: res.gp?.gp3?.name5 ?? null,
        },
        gp4: {
          name1: res.gp?.gp4?.name1 ?? null,
          name2: res.gp?.gp4?.name2 ?? null,
          name3: res.gp?.gp4?.name3 ?? null,
          name4: res.gp?.gp4?.name4 ?? null,
          name5: res.gp?.gp4?.name5 ?? null,
        },
        gp5: {
          name1: res.gp?.gp5?.name1 ?? null,
          name2: res.gp?.gp5?.name2 ?? null,
          name3: res.gp?.gp5?.name3 ?? null,
          name4: res.gp?.gp5?.name4 ?? null,
          name5: res.gp?.gp5?.name5 ?? null,
        },
      },
      facilities: res.facilities ?? [],
      sports: res.sports ?? [],
      members: {
        ..._createInitialMembers(),
        ...(res.members ?? {}),
      },
    }
  }

  // ! @TODO: 이거 mergeValidator 추가하고, 첨부파일이랑 step 이랑 연동 해야함
  const _getErrorList = (req: PubGuideReq) => {
    const errors: string[] = []
    if (!req.str.name1) errors.push('str.name1은 필수입니다.')
    if (!req.date.name1) errors.push('date.name1은 필수입니다.')
    if (!req.str.name2) errors.push('str.name2은 필수입니다.')
    if (!req.date.name2) errors.push('date.name2은 필수입니다.')
    if (!req.str.name3) errors.push('str.name3은 필수입니다.')
    if (!req.date.name3) errors.push('date.name3은 필수입니다.')
    if (!req.code.name1) errors.push('code.name1은 필수입니다.')
    if (!req.phone.name1) errors.push('phone.name1은 필수입니다.')
    if (!req.facilities?.length)
      errors.push('사용시설은 1개 이상 선택이 필요합니다.')
    if (!req.sports?.length)
      errors.push('운영종목은 1개 이상 선택이 필요합니다.')
    return errors
  }

  const _applyDetail = (data: PubGuideRes) => {
    Object.assign(pubGuideRes, data)
  }

  const _applyResPatch = (next: Partial<PubGuideRes>) => {
    Object.assign(pubGuideRes, next)
  }

  const _applyMembersPatch = (next: Partial<PubGuideReq['members']>) => {
    pubGuideRes.members = {
      ...pubGuideRes.members,
      ...next,
    }
  }

  function _createInitialMembers(): PubGuideReq['members'] {
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

  function _createInitialStr(): PubGuideStr {
    return {
      name1: '',
      name2: '',
      name3: '',
      name4: '',
      name5: '',
    }
  }

  function _createInitialNum(): PubGuideNum {
    return {
      name1: null,
      name2: null,
      name3: null,
      name4: null,
      name5: null,
    }
  }

  function _createInitialPhone(): PubGuidePhone {
    return {
      name1: '',
      name2: '',
      name3: '',
      name4: '',
      name5: '',
    }
  }

  function _createInitialTel(): PubGuideTel {
    return {
      name1: '',
      name2: '',
      name3: '',
      name4: '',
      name5: '',
    }
  }

  function _createInitialDate(): PubGuideDate {
    return {
      name1: null,
      name2: null,
      name3: null,
      name4: null,
      name5: null,
    }
  }

  function _createInitialAny(): PubGuideAny {
    return {
      name1: null,
      name2: null,
      name3: null,
      name4: null,
      name5: null,
    }
  }

  function _createInitialCode(): PubGuideCode {
    return {
      name1: null,
      name2: null,
      name3: null,
      name4: null,
      name5: null,
    }
  }

  function _createInitialGp(): PubGuideGp {
    return {
      gp1: {
        name1: null,
        name2: null,
        name3: null,
        name4: null,
        name5: null,
      },
      gp2: {
        name1: null,
        name2: null,
        name3: null,
        name4: null,
        name5: null,
      },
      gp3: {
        name1: null,
        name2: null,
        name3: null,
        name4: null,
        name5: null,
      },
      gp4: {
        name1: null,
        name2: null,
        name3: null,
        name4: null,
        name5: null,
      },
      gp5: {
        name1: null,
        name2: null,
        name3: null,
        name4: null,
        name5: null,
      },
    }
  }

  function _createInitialRes(): PubGuideRes {
    return {
      id: null,
      updatedAt: null,
      str: _createInitialStr(),
      num: _createInitialNum(),
      phone: _createInitialPhone(),
      tel: _createInitialTel(),
      date: _createInitialDate(),
      any: _createInitialAny(),
      code: _createInitialCode(),
      gp: _createInitialGp(),
      facilities: _createInitialFacilities(),
      sports: _createInitialSports(),
      members: _createInitialMembers(),
    }
  }

  function _createInitialFacilities(): PubGuideFacility[] {
    return []
  }

  function _createInitialSports(): PubGuideSport[] {
    return []
  }

  return {
    ...toRefs(pubGuideRes),
    pubGuideRes,
    currentIndex,
    ...getters,
    ...actions,
  }
})
