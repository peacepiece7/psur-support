<script setup lang="ts">
  import dayjs from 'dayjs'
  import RegionSelect from '~/components/molecules/RegionSelect.vue'
  import TextField from '~/components/atoms/TextField.vue'
  import DatePicker from '~/components/atoms/DatePicker.vue'
  import { useNewRegSprtClubStore } from '~/stores/newRegSprtClubStore'
  import FacilitiesDialog from '~/components/templates/FacilitiesDialog.vue'
  import EvfxDialog from '~/components/templates/EvfxDialog.vue'
  import type {
    NewRegSprtClubFacility,
    NewRegSprtClubSport,
  } from '~/types/apis/registered-sports-club'

  const newRegSprtClubStore = useNewRegSprtClubStore()
  const isOpenClubEstablishedAtPicker = ref(false)

  const clubEstablishedAtPickerValue = computed({
    get: () => newRegSprtClubStore.clubEstablishedAt,
    set: (val: unknown) => {
      if (!val) {
        newRegSprtClubStore.clubEstablishedAt = null
        return
      }

      const d = dayjs(val as any)
      newRegSprtClubStore.clubEstablishedAt = d.isValid()
        ? d.format('YYYY-MM-DD')
        : null
    },
  })

  const clubEstablishedAtText = computed(() => {
    return newRegSprtClubStore.clubEstablishedAt ?? ''
  })

  // TODO: API 연동 시 교체
  const facilityOptions = ref<NewRegSprtClubFacility[]>([
    {
      id: 'fac-1',
      name: '마포구민체육센터',
      address: '서울특별시 마포구 월드컵로 240',
      note: '실내체육관/다목적',
    },
    {
      id: 'fac-2',
      name: '잠실종합운동장',
      address: '서울특별시 송파구 올림픽로 25',
      note: '주경기장/보조경기장',
    },
    {
      id: 'fac-3',
      name: '올림픽공원 체조경기장',
      address: '서울특별시 송파구 올림픽로 424',
      note: '대관 가능(일정 협의)',
    },
  ])

  // TODO: API 연동 시 교체
  const sportOptions = ref<NewRegSprtClubSport[]>([
    { id: 'sprt-1', name: '축구', category: '구기', note: '11인/풋살 포함' },
    { id: 'sprt-2', name: '농구', category: '구기' },
    { id: 'sprt-3', name: '배드민턴', category: '라켓' },
    { id: 'sprt-4', name: '수영', category: '수상' },
  ])

  // Dialog에는 id 목록만 전달(중복 state를 별도로 들지 않고 store에서 파생)
  const selectedFacilityIds = computed(() =>
    (newRegSprtClubStore.facilities ?? []).map((v) => v.id),
  )
  const selectedSportIds = computed(() =>
    (newRegSprtClubStore.sports ?? []).map((v) => v.id),
  )

  const isOpenFacilityDialog = ref(false)
  const isOpenEvfxDialog = ref(false)

  const openFacilityDialog = () => {
    isOpenFacilityDialog.value = true
  }

  const openSportDialog = () => {
    isOpenEvfxDialog.value = true
  }

  // dialog submit 시 store(res)로 반영
  const onFacilitiesSubmit = (selected: NewRegSprtClubFacility[]) => {
    newRegSprtClubStore.actions.setRes({ facilities: selected })
  }

  const onSportsSubmit = (selected: NewRegSprtClubSport[]) => {
    newRegSprtClubStore.actions.setRes({ sports: selected })
  }
</script>

<template>
  <section
    class="border-grey-300 bg-grey-50 min-h-[420px] rounded-3xl border p-10"
  >
    <h3 class="m-0 mb-4 text-2xl font-bold">3단계. 클럽 정보 입력</h3>

    <div class="grid max-w-[720px] gap-4">
      <TextField
        v-model="newRegSprtClubStore.corpOrgName"
        label="법인 단체명"
        placeholder="법인(단체)명을 입력하세요"
        variant="outlined"
        density="compact"
        size="md"
      />

      <DatePicker
        v-model="newRegSprtClubStore.representativeBirthDate"
        label="대표자 생년월일"
        quarter-picker
      />

      <TextField
        v-model="newRegSprtClubStore.representativeName"
        label="대표자명"
        placeholder="대표자명을 입력하세요"
      />

      <RegionSelect v-model="newRegSprtClubStore.region" />

      <TextField
        v-model="newRegSprtClubStore.mainPhone"
        label="대표전화번호"
        placeholder="예) 02-1234-5678"
        type="tel"
      />

      <NumberInput
        v-model="newRegSprtClubStore.members.femaleMembers"
        label="여성 회원 수"
      />

      <TextField
        v-model="newRegSprtClubStore.clubName"
        label="클럽명"
        placeholder="클럽명을 입력하세요"
        aria-label="클럽명"
      />

      <!-- <v-menu
        v-model="isOpenClubEstablishedAtPicker"
        :close-on-content-click="false"
        location="bottom"
      >
        <template #activator="{ props }">
          <TextField
            v-model="clubEstablishedAtText"
            v-bind="props"
            label="클럽 설립일"
            aria-label="클럽 설립일"
            readonly
            clearable
            variant="outlined"
            density="compact"
            @click:clear="newRegSprtClubStore.clubEstablishedAt = null"
          />
        </template>

        <v-date-picker
          v-model="clubEstablishedAtPickerValue"
          @update:model-value="isOpenClubEstablishedAtPicker = false"
        />
      </v-menu> -->

      <!-- 사용시설명: 직접 입력 불가(검색 다이얼로그 오픈) -->
      <!-- <v-select
        v-model="newRegSprtClubStore.facilities"
        :items="facilityOptions"
        item-title="name"
        item-value="id"
        :return-object="true"
        class="apply-reg-step__search-input"
        label="사용시설명"
        placeholder="검색해서 선택하세요"
        aria-label="사용시설명"
        :multiple="true"
        :chips="true"
        :clearable="false"
        readonly
        :menu="false"
        append-inner-icon="local:search"
        @click="openFacilityDialog"
        @click:append-inner="openFacilityDialog"
      /> -->

      <!-- 운영종목: 직접 입력 불가(검색 다이얼로그 오픈) -->
      <!-- <v-select
        v-model="newRegSprtClubStore.sports"
        :items="sportOptions"
        item-title="name"
        item-value="id"
        :return-object="true"
        class="apply-reg-step__search-input"
        label="운영종목"
        placeholder="검색해서 선택하세요"
        aria-label="운영종목"
        :multiple="true"
        :chips="true"
        :clearable="false"
        readonly
        :menu="false"
        append-inner-icon="local:search"
        @click="openSportDialog"
        @click:append-inner="openSportDialog"
      /> -->
    </div>

    <FacilitiesDialog
      v-model:is-open="isOpenFacilityDialog"
      :options="facilityOptions"
      :selected="selectedFacilityIds"
      @close="isOpenFacilityDialog = false"
      @submit="onFacilitiesSubmit"
    />

    <EvfxDialog
      v-model:is-open="isOpenEvfxDialog"
      :options="sportOptions"
      :selected="selectedSportIds"
      @close="isOpenEvfxDialog = false"
      @submit="onSportsSubmit"
    />
  </section>
</template>

<style scoped>
  /* Step3 전용 스타일 */
  .apply-reg-step__search-input {
    :deep(.v-field__input) {
      cursor: pointer;
    }
  }
</style>
