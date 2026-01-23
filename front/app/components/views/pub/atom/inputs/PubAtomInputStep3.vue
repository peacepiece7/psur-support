<script setup lang="ts">
  import dayjs from 'dayjs'
  import { usePubGuideStore } from '~/stores/pubGuildStore'

  const pubGuideStore = usePubGuideStore()

  const dateRefs = ref<any[]>([
    '',
    '',
    '',
    '',
    '',
    '',
    '20250101',
    '2025-03-01',
    '',
    new Date(),
    '2023-03',
    '2024',
    '2024-04-01',
    '2023',
  ])

  const timeRefs = ref<string[]>([
    '',
    '',
    '',
    '',
    '11:15',
    '10:10',
    '',
    '',
    '09:30',
    '22:15',
    '',
  ])

  const allowedHours = (v: number) => v % 2 === 0
  const allowedMinutes = (v: number) => v >= 10 && v <= 50
  const allowedStep = (m: number) => m % 10 === 0
</script>

<template>
  <section
    class="border-grey-300 bg-grey-50 min-h-[420px] rounded-3xl border p-10"
  >
    <h3 class="m-0 mb-4 text-2xl font-bold">DatePicker</h3>
    <p>
      v-model의 arg로
      <span class="text-red-500">
        Date Instance(ISO 8601)를 지원하지 않습니다.
      </span>
    </p>
    <p>
      string type의 YYYY,YYYY-MM, YYYYMMDD, YYYY-MM-DD, YYYY-MM 포멧만
      지원합니다. <del>작업하기 귀찮</del>
    </p>

    <div class="mt-10 grid max-w-[720px] gap-4">
      <DatePicker v-model="dateRefs[1]" label="DATE PICKER SM" size="sm" />
      <DatePicker v-model="dateRefs[2]" label="DATE PICKER MD" size="md" />
      <DatePicker v-model="dateRefs[3]" label="DATE PICKER LG" size="lg" />
      <DatePicker v-model="dateRefs[4]" label="DATE PICKER XL" size="xl" />
      <DatePicker v-model="dateRefs[5]" label="DATE PICKER MAX" size="max" />

      <div class="flex">
        <div class="flex-1">
          <DatePicker v-model="dateRefs[6]" label="Readonly" readonly />
        </div>
        <div class="flex-1">
          <DatePicker v-model="dateRefs[7]" label="Disabled" disabled />
        </div>
      </div>

      <div class="flex">
        <div class="flex-1">
          <DatePicker
            v-model="dateRefs[9]"
            label="Month Picker"
            month-picker
            placeholder="연도-월"
          />
        </div>
        <div class="flex-1">
          <DatePicker
            v-model="dateRefs[10]"
            label="Month Picker"
            month-picker
            placeholder="연도-월"
          />
        </div>
      </div>
      <div class="flex">
        <div class="flex-1">
          <DatePicker
            v-model="dateRefs[11]"
            label="Year Picker"
            year-picker
            placeholder="연도"
          />
        </div>
        <div class="flex-1">
          <DatePicker
            v-model="dateRefs[12]"
            label="Year Picker"
            year-picker
            placeholder="연도"
          />
        </div>
      </div>

      <div class="flex">
        <div class="flex-1">
          <DatePicker
            v-model="dateRefs[13]"
            label="Quarter Picker, 2023 -> 2023년 1분기"
            quarter-picker
            placeholder="연도-분기"
          />
        </div>
        <div class="flex-1">
          <DatePicker
            v-model="dateRefs[14]"
            label="Quarter Picker"
            quarter-picker
            placeholder="연도-분기"
          />
        </div>
      </div>
    </div>

    <h3 class="m-0 mt-16 mb-4 text-2xl font-bold">TimePicker</h3>
    <del>min, max가 적용이 안되네 썅</del>

    <div class="mt-10 grid max-w-[720px] gap-4">
      <TimePicker v-model="timeRefs[10]" label="TIME PICKER SM" size="sm" />
      <TimePicker v-model="timeRefs[1]" label="TIME PICKER MD" size="md" />
      <TimePicker v-model="timeRefs[2]" label="TIME PICKER LG" size="lg" />
      <TimePicker v-model="timeRefs[3]" label="TIME PICKER XL" size="xl" />
      <TimePicker v-model="timeRefs[4]" label="TIME PICKER MAX" size="max" />

      <div class="flex">
        <div class="flex-1">
          <TimePicker v-model="timeRefs[5]" label="Readonly" readonly />
        </div>
        <div class="flex-1">
          <TimePicker v-model="timeRefs[6]" label="Disabled" disabled />
        </div>
      </div>

      <div class="flex">
        <div class="flex-1">
          <TimePicker
            v-model="timeRefs[7]"
            label="With Restrictions"
            :allowed-hours="allowedHours"
            :allowed-minutes="allowedMinutes"
            min="09:30"
            max="22:15"
            scrollable
          />
        </div>
        <div class="flex-1">
          <TimePicker
            v-model="timeRefs[8]"
            label="Step Minutes (10분 단위)"
            :allowed-minutes="allowedStep"
          />
        </div>
      </div>

      <div class="flex">
        <div class="flex-1">
          <TimePicker
            v-model="timeRefs[9]"
            label="Min/Max Time"
            min="09:30"
            max="22:15"
          />
        </div>
        <div class="flex-1">
          <TimePicker v-model="timeRefs[0]" label="Scrollable" scrollable />
        </div>
      </div>
    </div>
  </section>
</template>
