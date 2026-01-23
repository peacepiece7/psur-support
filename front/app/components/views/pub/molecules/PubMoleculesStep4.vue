<script setup lang="ts">
  // ===== Accordion 이용약관 예제 =====
  const allChecked = ref(false)
  const agreements = ref([
    { key: 'terms', label: '서비스 이용약관 동의', checked: false },
    { key: 'privacy', label: '개인정보 처리방침 동의', checked: false },
    { key: 'marketing', label: '마케팅 정보 수신 동의', checked: false },
  ])

  // 전체 선택/해제
  const toggleAll = (value: boolean) => {
    allChecked.value = value
    agreements.value.forEach((item) => {
      item.checked = value
    })
  }

  // 개별 체크박스 변경 시 전체 선택 상태 동기화
  const syncAll = () => {
    allChecked.value = agreements.value.every((item) => item.checked)
  }
</script>

<template>
  <div class="bg-grey-50 min-h-screen p-8">
    <div class="mx-auto max-w-[1400px]">
      <h1 class="text-grey-900 mb-8 text-3xl font-bold">Accordion 예제</h1>

      <!-- Accordion 이용약관 예제 -->
      <section class="border-grey-300 mt-8 rounded-3xl border bg-white p-10">
        <h2 class="text-grey-900 mb-6 text-2xl font-bold">이용약관</h2>

        <Accordion>
          <AccordionItem>
            <template #title>
              <p>이용약관 전체보기</p>
              <!-- <v-checkbox
                v-model="allChecked"
                label="전체 동의하기"
                hide-details
                @click.stop
                @update:model-value="toggleAll"
              /> -->
            </template>

            <template #text>
              <div class="accordion-item__content">
                <v-checkbox
                  v-for="item in agreements"
                  :key="item.key + 'c'"
                  v-model="item.checked"
                  :label="item.label"
                  hide-details
                  class="mb-2"
                  @update:model-value="syncAll"
                />
              </div>
            </template>
          </AccordionItem>
        </Accordion>
      </section>
    </div>
  </div>
</template>
