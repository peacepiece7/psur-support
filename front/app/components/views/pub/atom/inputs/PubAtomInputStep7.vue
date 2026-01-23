<script setup lang="ts">
  // ===== SelectionGroup 값 =====
  const radioValue = ref<string | null>('option2')
  const radioHorizontalValue = ref<string | null>('option1')
  const checkboxValue = ref<string[]>([])
  const checkboxHorizontalValue = ref<string[]>([])
  const checkboxColorSetValue = ref<string[]>([])
  const checkboxDisabledValue = ref<string[]>(['option2'])
  const checkboxPartialDisabledValue = ref<string[]>(['option1'])
  const radioReadonlyValue = ref<string | null>('CODE2')
  const checkboxReadonlyValue = ref<string[]>(['CODE1', 'CODE3'])

  // ===== Switch 값 =====
  const switchValue = ref<boolean>(false)
  const switchValue2 = ref<boolean>(true)
  const switchDisabledValue = ref<boolean>(true)
  const switchReadonlyValue = ref<boolean>(true)

  // SelectionGroup 옵션
  const checkboxOptions = [
    { label: '등록스포츠클럽', value: 'CODE1' },
    { label: '지정스포츠클럽', value: 'CODE2' },
    { label: '예비지정스포츠클럽', value: 'CODE3' },
    { label: '일반회원', value: 'CODE4' },
    { label: '비회원', value: 'CODE5' },
  ]

  // 개별 disabled 옵션이 있는 체크박스 그룹
  const checkboxPartialDisabledOptions = [
    { label: '옵션 1', value: 'option1' },
    { label: '옵션 2 (비활성)', value: 'option2', disabled: true },
    { label: '옵션 3', value: 'option3' },
    { label: '옵션 4 (비활성)', value: 'option4', disabled: true },
    { label: '옵션 5', value: 'option5' },
  ]
</script>

<template>
  <section class="apply-reg-step">
    <h3 class="apply-reg-step__title">4단계. 클럽 정보 입력</h3>
    <p class="apply-reg-step__desc">(컨텐츠는 추후 단계별로 채웁니다)</p>

    <p>checkbox, radio focus-within 스타일 확인해주세요</p>
    <p>checkbox 는 탭으로 이동, space로 선택</p>
    <p>radio는 focus시 방향키로 이동입니다</p>

    <div class="mt-10" />
    <h3 class="m-0 mb-4 text-2xl font-bold">SelectionGroup</h3>

    <div class="grid max-w-[720px] gap-6">
      <!-- Radio 기본 (Vertical) -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Radio (Vertical)</h4>
        <SelectionGroup
          v-model="radioValue"
          :items="checkboxOptions"
          type="radio"
          direction="vertical"
          label="라디오 버튼 (세로)"
        />
        <p class="text-grey-600 mt-2 text-sm">
          선택된 값: {{ radioValue || '없음' }}
        </p>
      </div>

      <!-- Radio Horizontal -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Radio (Horizontal)</h4>
        <SelectionGroup
          v-model="radioHorizontalValue"
          :items="checkboxOptions"
          type="radio"
          direction="horizontal"
          label="라디오 버튼 (가로)"
        />
        <p class="text-grey-600 mt-2 text-sm">
          선택된 값: {{ radioHorizontalValue || '없음' }}
        </p>
      </div>

      <!-- Checkbox 기본 (Vertical) -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Checkbox (Vertical)</h4>
        <SelectionGroup
          v-model="checkboxValue"
          :items="checkboxOptions"
          type="checkbox"
          direction="vertical"
          label="체크박스 (세로)"
        />
        <p class="text-grey-600 mt-2 text-sm">
          선택된 값: {{ checkboxValue.join(', ') || '없음' }}
        </p>
      </div>

      <!-- Checkbox Horizontal -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Checkbox (Horizontal)</h4>
        <SelectionGroup
          v-model="checkboxHorizontalValue"
          :items="checkboxOptions"
          type="checkbox"
          direction="horizontal"
          label="체크박스 (가로)"
        />
        <p class="text-grey-600 mt-2 text-sm">
          선택된 값: {{ checkboxHorizontalValue.join(', ') || '없음' }}
        </p>
      </div>

      <!-- ColorSet -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">ColorSet</h4>
        <div class="grid gap-4">
          <SelectionGroup
            v-model="checkboxColorSetValue"
            :items="checkboxOptions.slice(0, 3)"
            type="checkbox"
            direction="vertical"
            color-set="primary"
            label="Primary (기본)"
          />
          <SelectionGroup
            v-model="checkboxColorSetValue"
            :items="checkboxOptions.slice(0, 3)"
            type="checkbox"
            direction="vertical"
            color-set="success"
            label="Success (초록)"
          />
          <SelectionGroup
            v-model="checkboxColorSetValue"
            :items="checkboxOptions.slice(0, 3)"
            type="checkbox"
            direction="vertical"
            color-set="warning"
            label="Warning (빨강 계열)"
          />
        </div>
        <p class="text-grey-600 mt-2 text-sm">
          선택된 값: {{ checkboxColorSetValue.join(', ') || '없음' }}
        </p>
      </div>

      <!-- Disabled -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Disabled</h4>
        <SelectionGroup
          v-model="checkboxDisabledValue"
          :items="checkboxOptions"
          type="checkbox"
          direction="vertical"
          label="전체 비활성화"
          :disabled="true"
        />
        <p class="text-grey-600 mt-2 text-sm">
          선택된 값: {{ checkboxDisabledValue.join(', ') || '없음' }}
        </p>
      </div>

      <!-- 개별 옵션 Disabled -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">개별 옵션 Disabled</h4>
        <SelectionGroup
          v-model="checkboxPartialDisabledValue"
          :items="checkboxPartialDisabledOptions"
          type="checkbox"
          direction="vertical"
          label="일부 옵션만 비활성화"
        />
        <p class="text-grey-600 mt-2 text-sm">
          선택된 값: {{ checkboxPartialDisabledValue.join(', ') || '없음' }}
        </p>
      </div>

      <!-- Readonly -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Readonly</h4>
        <div class="grid gap-4">
          <div>
            <SelectionGroup
              v-model="radioReadonlyValue"
              :items="checkboxOptions"
              type="radio"
              direction="vertical"
              label="Radio Readonly (값은 보이지만 수정 불가)"
              :readonly="true"
            />
            <p class="text-grey-600 mt-2 text-sm">
              선택된 값: {{ radioReadonlyValue || '없음' }}
            </p>
          </div>
          <div>
            <SelectionGroup
              v-model="checkboxReadonlyValue"
              :items="checkboxOptions"
              type="checkbox"
              direction="vertical"
              label="Checkbox Readonly (값은 보이지만 수정 불가)"
              :readonly="true"
            />
            <p class="text-grey-600 mt-2 text-sm">
              선택된 값: {{ checkboxReadonlyValue.join(', ') || '없음' }}
            </p>
          </div>
        </div>
      </div>
    </div>

    <div class="mt-10" />
    <h3 class="m-0 mb-4 text-2xl font-bold">Switch</h3>

    <div class="grid max-w-[720px] gap-6">
      <!-- Switch 기본 -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Switch 기본</h4>
        <Switch v-model="switchValue" label="스위치 기본" />
        <p class="text-grey-600 mt-2 text-sm">
          선택된 값: {{ switchValue ? '켜짐' : '꺼짐' }}
        </p>
      </div>

      <!-- Switch 켜진 상태 -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Switch 켜진 상태</h4>
        <Switch v-model="switchValue2" label="스위치 켜짐" />
        <p class="text-grey-600 mt-2 text-sm">
          선택된 값: {{ switchValue2 ? '켜짐' : '꺼짐' }}
        </p>
      </div>

      <!-- Switch Disabled -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Switch Disabled</h4>
        <Switch
          v-model="switchDisabledValue"
          label="스위치 비활성화"
          :disabled="true"
        />
        <p class="text-grey-600 mt-2 text-sm">
          선택된 값: {{ switchDisabledValue ? '켜짐' : '꺼짐' }}
        </p>
      </div>

      <!-- Switch Readonly -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Switch Readonly</h4>
        <Switch
          v-model="switchReadonlyValue"
          label="스위치 읽기전용 (값은 보이지만 수정 불가)"
          :readonly="true"
        />
        <p class="text-grey-600 mt-2 text-sm">
          선택된 값: {{ switchReadonlyValue ? '켜짐' : '꺼짐' }}
        </p>
      </div>

      <!-- Switch 라벨 없음 -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Switch 라벨 없음</h4>
        <Switch v-model="switchValue" />
        <p class="text-grey-600 mt-2 text-sm">
          선택된 값: {{ switchValue ? '켜짐' : '꺼짐' }}
        </p>
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
