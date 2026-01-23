<script setup lang="ts">
  import { usePubGuideStore } from '~/stores/pubGuildStore'

  const pubGuideStore = usePubGuideStore()

  const strRefs = ref<any[]>([])
  const numRefs = ref<number[]>([])

  const handleClickAppendInner = () => {
    alert('click append inner')
  }
</script>

<template>
  <!-- TextField 섹션 -->
  <section class="border-grey-300 min-h-[420px] rounded-3xl border p-10">
    <h3 class="m-0 mb-4 text-2xl font-bold">TextField</h3>

    <div class="grid max-w-[720px] gap-4">
      <p class="text-grey-400">placeholder ellipsis 처리..?</p>
      <TextField v-model="strRefs[0]" label="TEXT FIELD XS" size="xs" />

      <TextField v-model="strRefs[1]" label="TEXT FIELD SM" size="sm" />

      <TextField v-model="strRefs[2]" label="TEXT FIELD MD" size="md" />
      <TextField
        v-model="strRefs[2]"
        label="TEXT FIELD MD"
        size="md"
        suffix="명"
      />

      <TextField
        v-model="strRefs[2]"
        label="TEXT FIELD MD"
        size="md"
        prefix="https://"
        suffix="명"
      />

      <TextField v-model="strRefs[3]" label="TEXT FIELD LG" size="lg" />
      <TextField
        v-model="strRefs[3]"
        label="TEXT FIELD LG"
        size="lg"
        suffix="명"
      />
      <TextField
        v-model="strRefs[3]"
        label="TEXT FIELD LG"
        size="lg"
        prefix="https://"
        suffix="명"
      />

      <TextField v-model="strRefs[4]" label="TEXT FIELD XL" size="xl" />
      <TextField
        v-model="strRefs[4]"
        label="TEXT FIELD XL"
        size="xl"
        suffix="명"
      />
      <TextField
        v-model="strRefs[4]"
        label="TEXT FIELD XL"
        size="xl"
        prefix="https://"
        suffix="명"
      />

      <TextField v-model="strRefs[5]" label="TEXT FIELD MAX" size="max" />
      <TextField
        v-model="strRefs[5]"
        label="TEXT FIELD MAX"
        size="max"
        suffix="명"
      />
      <TextField
        v-model="strRefs[5]"
        label="TEXT FIELD MAX"
        size="max"
        prefix="https://"
        suffix="명"
      />
      <!--  -->

      <div class="flex">
        <div class="flex-1">
          <TextField model-value="foo bar" label="Readonly" readonly />
        </div>
        <div class="flex-1">
          <TextField model-value="foo bar" label="Disabled" disabled />
        </div>
      </div>

      <div class="flex">
        <div class="flex-1">
          <TextField
            v-model="strRefs[7]"
            label="TEXT prefix (X)"
            prefix="https://"
            suffix=""
            placeholder=""
            max-length="4"
          />
        </div>
        <div class="flex-1">
          <TextField
            v-model="strRefs[7]"
            label="TEXT surfix"
            prefix=""
            suffix="회"
          />
        </div>
      </div>

      <div class="flex">
        <div class="flex-1">
          <TextField
            v-model="strRefs[7]"
            label="TEXT prefix (X)"
            prefix="+82"
            suffix=""
            placeholder=""
            max-length="4"
          />
        </div>
        <div class="flex-1">
          <TextField
            v-model="strRefs[7]"
            label="TEXT surfix"
            prefix=""
            suffix="개월"
            max-length="4"
            size="md"
          />
        </div>
      </div>

      <div class="flex">
        <div class="flex-1">
          <TextField
            v-model="strRefs[8]"
            label="TEXT max-length 4"
            max-length="4"
          />
        </div>
      </div>
    </div>

    <div class="flex">
      <div class="flex-1">
        <TextField
          v-model="strRefs[6]"
          label="Append Inner Icon"
          append-inner-icon="local:search"
          @click:append-inner="handleClickAppendInner"
        />
      </div>
      <div class="flex-1">
        <TextField
          v-model="strRefs[7]"
          label="Append Inner Icon readonly"
          readonly
          append-inner-icon="local:search"
          @click:append-inner="handleClickAppendInner"
        />
      </div>
    </div>

    <div class="flex">
      <div class="flex-1">
        <TextField
          v-model="strRefs[7]"
          label="Append Inner Icon disabled"
          disabled
          append-inner-icon="local:search"
          @click:append-inner="handleClickAppendInner"
        />
      </div>
      <div class="flex-1">
        <!-- <TextField
          v-model="strRefs[7]"
          label="Append Inner Icon readonly"
          readonly
          append-inner-icon="local:search"
          @click:append-inner="handleClickAppendInner"
        /> -->
      </div>
    </div>
  </section>

  <!-- NumberInput 섹션 -->
  <section class="border-grey-300 mt-6 min-h-[420px] rounded-3xl border p-10">
    <h3 class="m-0 mb-4 text-2xl font-bold">NumberInput</h3>

    <div class="grid max-w-[720px] gap-4">
      <NumberInput v-model="numRefs[0]" label="NUMBER INPUT XS" size="xs" />
      <NumberInput v-model="numRefs[1]" label="NUMBER INPUT SM" size="sm" />
      <NumberInput v-model="numRefs[2]" label="NUMBER INPUT MD" size="md" />
      <NumberInput v-model="numRefs[3]" label="NUMBER INPUT LG" size="lg" />
      <NumberInput v-model="numRefs[4]" label="NUMBER INPUT XL" size="xl" />
      <NumberInput v-model="numRefs[5]" label="NUMBER INPUT MAX" size="max" />

      <div class="flex">
        <div class="flex-1">
          <NumberInput :model-value="100" label="Readonly" readonly />
        </div>
        <div class="flex-1">
          <NumberInput :model-value="200" label="Disabled" disabled />
        </div>
      </div>

      <p>
        Number Input은 오른쪽 부터 채워지기 떄문에(reverse = true)
        <span class="text-red-500">Prefix와 Suffix가 반대입니다</span>
      </p>
      <!-- suffix 예제 -->
      <div class="flex">
        <div class="flex-1">
          <NumberInput v-model="numRefs[6]" label="NUMBER prefix" prefix="회" />
        </div>
        <div class="flex-1">
          <NumberInput
            v-model="numRefs[7]"
            label="NUMBER prefix"
            suffix="신청기간"
            prefix="개월"
          />
        </div>
      </div>
    </div>
  </section>

  <!-- DONT USE IT LIKE BELLOW EXAMPLE 섹션 -->
  <!-- <section class="border-grey-300 mt-6 min-h-[420px] rounded-3xl border p-10">
    <h3 class="m-0 mb-4 text-2xl font-bold">DONT USE IT LIKE BELLOW EXAMPLE</h3>
    <div class="grid max-w-[720px] gap-4">
      <div class="flex">
        <div class="flex-1">
          <TextField
            v-model="strRefs[10]"
            label="Date -> DatePicker"
            type="date"
            size="sm"
          />
        </div>
        <div class="flex-1">
          <TextField
            v-model="strRefs[11]"
            label="number -> NumberInput"
            type="number"
            size="sm"
          />
        </div>
      </div>

      <div class="flex">
        <div class="flex-1">
          <TextField
            v-model="strRefs[12]"
            label="radio -> RadioGroup"
            type="radio"
            size="sm"
          />
        </div>
        <div class="flex-1">
          <TextField
            v-model="strRefs[13]"
            label="checkbox -> CheckboxGroup"
            type="checkbox"
            size="sm"
          />
        </div>
      </div>
    </div>
  </section> -->
</template>
