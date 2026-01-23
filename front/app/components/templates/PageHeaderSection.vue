<script setup lang="ts">
  export type PageHeaderType = 'infoPage' | 'applyPage'

  interface Props {
    type: PageHeaderType
    title: string
    imageSrc: string
    imageAlt?: string
  }

  // default props 제거 (사용처에서 명시적으로 전달)
  const props = defineProps<Props>()

  const { scrollY } = useTracker()

  // 헤더 영역 ref
  const headerRef = ref<HTMLElement | null>(null)
  const headerTop = ref(0)

  onMounted(() => {
    if (headerRef.value) {
      headerTop.value = headerRef.value.offsetTop
    }
  })

  // 헤더 이미지 zoom 효과 계산
  const headerImageStyle = computed(() => {
    if (!headerRef.value) return {}
    const scrollProgress = Math.max(
      0,
      Math.min(1, (scrollY.value - headerTop.value) / 500),
    )
    const scale = 1 + scrollProgress * 0.2 // 1.0 ~ 1.2
    return {
      transform: `scale(${scale})`,
    }
  })

  // 헤더 parallax 효과
  const headerParallaxStyle = computed(() => {
    if (!headerRef.value) return {}
    const parallaxOffset = (scrollY.value - headerTop.value) * 0.5
    return {
      transform: `translateY(${Math.max(0, parallaxOffset)}px)`,
    }
  })

  const headerHeightClass = computed(() => {
    // offset(130px)을 포함한 전체 높이
    return props.type === 'applyPage'
      ? 'h-[330px] md:h-[430px]'
      : 'h-[530px] md:h-[730px]'
  })
</script>

<template>
  <section
    ref="headerRef"
    class="relative flex w-full items-center justify-center overflow-hidden top-[-130px] mb-[-130px]"
    :class="headerHeightClass"
  >
    <div class="relative z-20 text-center">
      <h1
        class="m-0 font-bold tracking-wide text-white text-2xl md:text-3xl drop-shadow-[0_2px_8px_rgba(0,0,0,0.3)]"
      >
        {{ title }}
      </h1>
      <div
        v-if="$slots.subtitle"
        class="mx-auto mt-3 max-w-[800px] px-4 text-white text-base leading-[1.6] drop-shadow-[0_2px_8px_rgba(0,0,0,0.3)] md:mt-4 md:px-0 md:text-lg [&>p]:m-0"
      >
        <slot name="subtitle" />
      </div>
    </div>
    <div class="absolute inset-0 z-10" :style="headerParallaxStyle">
      <img
        :src="imageSrc"
        :alt="imageAlt"
        class="h-full w-full origin-center object-cover object-center transition-transform duration-100 ease-out"
        :style="headerImageStyle"
      />
    </div>
  </section>
</template>
