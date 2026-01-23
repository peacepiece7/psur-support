<script setup lang="ts">
  export type WindowStep = {
    /** 1~7 */
    step: number
    /** 스텝 제목 */
    title: string
    /** (선택) 스텝 보조 설명 */
    subtitle?: string
  }

  type Props = {
    /** 0-based 현재 인덱스 */
    modelValue: number
    steps: WindowStep[]
  }

  const props = defineProps<Props>()

  const emit = defineEmits<{
    (e: 'update:modelValue', value: number): void
  }>()

  const active = computed({
    get: () => props.modelValue,
    set: (v: number) => emit('update:modelValue', v),
  })
</script>

<template>
  <div class="window" role="region" aria-label="신청 단계 컨텐츠">
    <!-- Vuetify v-window: 버튼으로만 이동(터치/스와이프/탭 점프 금지) -->
    <v-window v-model="active" class="" style="width: 100%" :touch="false">
      <v-window-item v-for="(s, i) in props.steps" :key="s.step" :value="i">
        <!-- 접근성: 현재 스텝 외 컨텐츠는 읽히지/포커스되지 않게 -->
        <div
          class=""
          style="width: 100%"
          :aria-hidden="i !== active"
          :inert="i !== active"
        >
          <slot :step="s" :index="i" />
        </div>
      </v-window-item>
    </v-window>
  </div>
</template>

<style scoped>
  .window :deep(.v-window) {
    overflow: visible;
  }
</style>
