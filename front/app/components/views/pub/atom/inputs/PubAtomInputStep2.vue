<script setup lang="ts">
  import { usePubGuideStore } from '~/stores/pubGuildStore'

  const pubGuideStore = usePubGuideStore()

  const numRefs = ref<number[]>([])
</script>

<template>
  <section class="border-grey-300 min-h-[420px] rounded-3xl border p-10">
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
          <NumberInput
            v-model="numRefs[6]"
            label="NUMBER prefix"
            prefix="회"
            placeholder=""
          />
        </div>
        <div class="flex-1">
          <NumberInput
            v-model="numRefs[7]"
            label="NUMBER prefix"
            suffix="신청기간"
            prefix="개월"
            placeholder=""
          />
        </div>
      </div>
    </div>
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
