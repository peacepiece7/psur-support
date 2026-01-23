<script setup lang="ts">
  import type { VCarousel } from 'vuetify/components'
  import type { ComponentPublicInstance } from 'vue'

  // VCarousel의 props 타입 추출
  type CarouselProps = ComponentPublicInstance<typeof VCarousel>['$props']

  /**
   * Carousel 아이템 기본 타입
   */
  export type CarouselItem = {
    /**
     * 이미지/비디오 소스 URL
     */
    src?: string
    /**
     * 대체 텍스트
     */
    alt?: string
    /**
     * Parallax 효과 사용 여부
     */
    parallax?: boolean
    /**
     * 최대 높이
     */
    maxHeight?: string
    /**
     * 최대 너비
     */
    maxWidth?: string
    /**
     * 비디오 소스 URL
     */
    videoSrc?: string
    /**
     * 이미지 소스 URL (비디오 로드 전 표시)
     */
    img?: string
    /**
     * 추가 속성 (인덱스 시그니처로 확장 가능)
     */
    [key: string]: unknown
  }

  type Props = {
    /**
     * Carousel에 표시할 아이템 배열
     */
    items: CarouselItem[]
    /**
     * Carousel 높이 (string | number)
     */
    height?: CarouselProps['height']
    /**
     * Carousel 비활성화 여부
     */
    disabled?: CarouselProps['disabled']
    /**
     * 인라인 스타일
     */
    style?: any
    /**
     * 슬라이드 순서 반전
     */
    reverse?: CarouselProps['reverse']
    /**
     * 연속 슬라이드 모드
     */
    continuous?: CarouselProps['continuous']
    /**
     * 자동 순환 모드
     */
    cycle?: CarouselProps['cycle']
    /**
     * 구분선 아이콘
     */
    delimiterIcon?: CarouselProps['delimiterIcon']
    /**
     * 화살표 버튼 표시 여부
     */
    showArrows?: boolean
    /**
     * 자동 순환 간격 (ms)
     */
    interval?: number
    /**
     * 구분선 숨김 여부
     */
    hideDelimiters?: boolean
  }

  const props = withDefaults(defineProps<Props>(), {
    items: () => [],
    style: {},
    showArrows: false,
    continuous: false,
    cycle: false,
    height: '100%',
    interval: 5000,
    hideDelimiters: false,
  })

  const currentIndex = ref(0) as Ref<number>
  const videoRefs = ref<Map<number, HTMLVideoElement>>(new Map())

  const registerVideo = (index: number, video: HTMLVideoElement | null) => {
    if (video) {
      videoRefs.value.set(index, video)
    } else {
      videoRefs.value.delete(index)
    }
  }

  const playCurrentVideo = () => {
    const video = videoRefs.value.get(currentIndex.value)
    if (video && video.paused) {
      video.currentTime = 0
      video.play().catch(() => {
        // 재생 실패 시 무시 (autoplay 정책 등)
      })
    }
  }

  const next = () => {
    if (currentIndex.value < props.items.length - 1) {
      currentIndex.value++
    }
  }

  const prev = () => {
    if (currentIndex.value > 0) {
      currentIndex.value--
    }
  }

  const goToIndex = (index: number) => {
    currentIndex.value = index
  }

  // currentIndex가 변경될 때 현재 비디오 재생
  watch(currentIndex, () => {
    nextTick(() => {
      playCurrentVideo()
    })
  })

  // 외부에서 사용할 수 있도록 expose
  defineExpose({
    next,
    prev,
    goToIndex,
    currentIndex: readonly(currentIndex),
  })
</script>

<template>
  <div class="relative w-full overflow-hidden">
    <v-carousel
      v-bind="$attrs"
      v-model="currentIndex"
      :height="props.height"
      :disabled="props.disabled"
      :reverse="props.reverse"
      :continuous="props.continuous"
      :cycle="props.cycle"
      :interval="props.interval"
      hide-delimiter-background
      :show-arrows="props.showArrows"
      class="w-full"
      :style="{
        ...props.style,
        maxHeight:
          props.height === 'auto' || props.height === '100%' ? 'none' : '700px',
      }"
    >
      <v-carousel-item v-for="(item, i) in props.items" :key="i" class="h-full">
        <div class="flex h-full w-full items-center justify-center">
          <slot name="item" :item="item" :index="i">
            <video
              v-if="item.videoSrc"
              :ref="(el) => registerVideo(i, el as HTMLVideoElement | null)"
              :muted="true"
              :autoplay="currentIndex === i"
              playsinline
              :src="item.videoSrc"
              class="h-full w-full object-cover object-center"
              :style="{ maxHeight: item.maxHeight, maxWidth: item.maxWidth }"
              @ended="next"
            />
            <img
              v-else
              :src="item.img"
              class="home-carousel-image h-full w-full object-cover object-center"
              :style="{ maxHeight: item.maxHeight, maxWidth: item.maxWidth }"
            />
          </slot>
        </div>
      </v-carousel-item>
    </v-carousel>

    <!-- 하단 네비게이션 컨테이너 (Arrows + Pagination) -->
    <div
      class="absolute bottom-4 left-1/2 z-10 flex -translate-x-1/2 items-center gap-5"
    >
      <!-- 하단 인덱스 버튼 (Pagination) -->
      <div class="flex gap-2">
        <button
          v-for="(item, index) in props.items"
          :key="index"
          class="h-3 w-3 cursor-pointer rounded-full border-2 border-white bg-white/50 p-0 transition-[background-color,transform] duration-200 ease-in-out hover:bg-white/80 hover:scale-[1.2]"
          :class="{ 'bg-white scale-[1.3]': currentIndex === index }"
          @click="goToIndex(index)"
        >
          <span
            class="p-0"
            style="
              position: absolute;
              width: 1px;
              height: 1px;
              margin: -1px;
              overflow: hidden;
              clip: rect(0, 0, 0, 0);
              white-space: nowrap;
              border-width: 0;
            "
            >{{ index + 1 }}번째 슬라이드</span
          >
        </button>
      </div>
    </div>
  </div>
</template>
