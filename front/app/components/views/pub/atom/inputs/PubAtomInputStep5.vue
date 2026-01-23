<script setup lang="ts">
  const selectOptions = [
    { title: '옵션 1', value: 'option1' },
    { title: '옵션 2', value: 'option2' },
    { title: '옵션 3', value: 'option3' },
    { title: '옵션 4', value: 'option4' },
    { title: '옵션 5', value: 'option5' },
  ]

  const selectValues = ref<(string | null)[]>([
    null,
    'option2',
    null,
    'option1',
    'option3',
    'option4',
    'option5',
    'option2',
  ])

  // 기본 옵션 (title, value 사용) - SelectV2용
  const defaultOptions = [
    { title: '옵션 1', value: 'option1' },
    { title: '옵션2 text overflow ellipsis 처리', value: 'option2' },
    { title: '옵션 3', value: 'option3' },
    { title: '옵션 4', value: 'option4' },
    { title: '옵션 5', value: 'option5' },
  ]

  // 커스텀 옵션 (code, codeName 사용) - SelectV2용
  const customOptions = [
    { code: 'A0101', codeName: 'moew' },
    { code: 'A0102', codeName: 'bark' },
    { code: 'A0103', codeName: 'chaw' },
    { code: 'A0104', codeName: 'meow' },
    { code: 'A0105', codeName: 'woof' },
  ]

  // 사이즈별 값 - SelectV2용
  const sizeValues = ref<(string | null)[]>([
    null, // xs
    'option2', // sm
    null, // md
    'option1', // lg
    'option3', // xl
    'option4', // max
    null, // placeholder 1
    null, // placeholder 2
  ])

  // 상태별 값 - SelectV2용
  const stateValues = ref<(string | null)[]>([
    'option2', // readonly
    'option3', // disabled
  ])

  // Clearable 값 - SelectV2용
  const clearableValue = ref<string | null>('option2')

  // Multiple 값 - SelectV2용
  const multipleValue = ref<string[]>(['option1', 'option3'])

  // 커스텀 옵션 값 - SelectV2용
  const customValue = ref<string | null>('A0102')
</script>

<template>
  <!-- Select (System) 섹션 -->
  <section class="apply-reg-step">
    <h3 class="m-0 mb-4 text-2xl font-bold">Select</h3>

    <div class="grid max-w-[720px] gap-4">
      <Select
        v-model="selectValues[0]"
        :options="selectOptions"
        label="SELECT XS"
        size="xs"
      />
      <Select
        v-model="selectValues[1]"
        :options="selectOptions"
        label="SELECT SM"
        size="sm"
      />
      <Select
        v-model="selectValues[2]"
        :options="selectOptions"
        label="SELECT MD"
        size="md"
      />
      <Select
        v-model="selectValues[3]"
        :options="selectOptions"
        label="SELECT LG"
        size="lg"
      />
      <Select
        v-model="selectValues[4]"
        :options="selectOptions"
        label="SELECT XL"
        size="xl"
      />
      <Select
        v-model="selectValues[5]"
        :options="selectOptions"
        label="SELECT MAX"
        size="max"
      />

      <div class="flex">
        <div class="flex-1">
          <Select
            v-model="selectValues[6]"
            :options="selectOptions"
            label="Readonly"
            readonly
          />
        </div>
        <div class="flex-1">
          <Select
            v-model="selectValues[7]"
            :options="selectOptions"
            label="Disabled"
            disabled
          />
        </div>
      </div>
    </div>
  </section>

  <!-- SelectV2 (Vuetify) 섹션 -->
  <section class="apply-reg-step mt-6">
    <h3 class="m-0 mb-4 text-2xl font-bold">SelectV2 (Vuetify)</h3>

    <div class="grid max-w-[720px] gap-6">
      <!-- Size 예제 -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Size</h4>
        <div class="grid gap-4">
          <SelectV2
            v-model="sizeValues[0]"
            :items="defaultOptions"
            label="SELECT XS"
            size="xs"
          />
          <SelectV2
            v-model="sizeValues[1]"
            :items="defaultOptions"
            label="SELECT SM"
            size="sm"
          />
          <SelectV2
            v-model="sizeValues[2]"
            :items="defaultOptions"
            label="SELECT MD"
            size="md"
          />
          <SelectV2
            v-model="sizeValues[3]"
            :items="defaultOptions"
            label="SELECT LG"
            size="lg"
          />
          <SelectV2
            v-model="sizeValues[4]"
            :items="defaultOptions"
            label="SELECT XL"
            size="xl"
          />
          <SelectV2
            v-model="sizeValues[5]"
            :items="defaultOptions"
            label="SELECT MAX"
            size="max"
          />
        </div>
      </div>

      <!-- Disabled & Readonly -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Disabled & Readonly</h4>
        <div class="flex gap-4">
          <div class="flex-1">
            <SelectV2
              v-model="stateValues[0]"
              :items="defaultOptions"
              label="Readonly"
              readonly
            />
          </div>
          <div class="flex-1">
            <SelectV2
              v-model="stateValues[1]"
              :items="defaultOptions"
              label="Disabled"
              disabled
            />
          </div>
        </div>
      </div>

      <!-- Clearable -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Clearable</h4>
        <SelectV2
          v-model="clearableValue"
          :items="defaultOptions"
          label="Clearable (선택 해제 가능)"
          :clearable="true"
        />
        <p class="text-grey-600 mt-2 text-sm">
          선택된 값: {{ clearableValue || '없음' }}
        </p>
      </div>

      <!-- Multiple -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Multiple</h4>
        <p>아.. 이거 text overflow ellipsis 처리 안되는 구조임..</p>
        <del>어차피 안쓰기로 함</del>
        <SelectV2
          v-model="multipleValue"
          :items="defaultOptions"
          label="Multiple (다중 선택)"
          :multiple="true"
        />
        <p class="text-grey-600 mt-2 text-sm">
          선택된 값: {{ multipleValue.join(', ') || '없음' }}
        </p>
      </div>

      <!-- Custom itemText & itemValue -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Custom itemText & itemValue</h4>
        <SelectV2
          v-model="customValue"
          :items="customOptions"
          item-text="codeName"
          item-value="code"
          label="커스텀 필드명 (codeName / code)"
          placeholder="코드를 선택하세요"
        />
        <p class="text-grey-600 mt-2 text-sm">
          선택된 코드: {{ customValue || '없음' }}
        </p>
      </div>

      <!-- Placeholder -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Placeholder</h4>
        <div class="grid gap-4">
          <SelectV2
            v-model="sizeValues[6]"
            :items="defaultOptions"
            label="기본 Placeholder"
            placeholder="선택"
          />
          <SelectV2
            v-model="sizeValues[7]"
            :items="defaultOptions"
            label="커스텀 Placeholder"
            placeholder="옵션을 선택해주세요"
          />
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
  .apply-reg-step {
    background: var(--color-grey-50);
    border: 1px solid var(--color-grey-200);
    border-radius: 1.5rem;
    padding: 2.5rem;
    min-height: 420px;
  }

  .apply-reg-step__title {
    margin: 0 0 1rem 0;
    font-size: 1.5rem;
    font-weight: 700;
  }

  .apply-reg-step__desc {
    margin: 0;
    color: var(--color-grey-600);
  }
</style>
