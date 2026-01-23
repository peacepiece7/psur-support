<script setup lang="ts">
  /**
   * HorizontalSlider 아이템 타입
   */
  export type HorizontalSliderItem = {
    /**
     * 아이템 타입: 'video' | 'image'
     */
    type: 'video' | 'image'
    /**
     * 비디오 소스 URL (type이 'video'일 때 사용)
     */
    videoSrc?: string
    /**
     * 이미지 소스 URL (type이 'image'일 때 사용)
     */
    imageSrc?: string
    /**
     * 썸네일 이미지 (비디오용, 선택사항)
     */
    thumbnail?: string
    /**
     * 아이템 제목 (선택사항)
     */
    title?: string
    /**
     * 아이템 설명 (선택사항)
     */
    description?: string
    /**
     * 클릭 시 이동할 링크 (선택사항)
     */
    link?: string
  }

  type Props = {
    /**
     * 슬라이더에 표시할 아이템 배열
     */
    items: HorizontalSliderItem[]
    /**
     * 아이템 너비 (기본값: 500px)
     */
    itemWidth?: string
  }

  const props = withDefaults(defineProps<Props>(), {
    items: () => [],
    itemWidth: '500px',
  })

  const scrollContainerRef = ref<HTMLElement | null>(null)
  const isDragging = ref(false)
  const startX = ref(0)
  const scrollLeft = ref(0)
  const dragStartX = ref(0)
  const hasDragged = ref(false)

  // 마우스 드래그 시작
  const handleMouseDown = (e: MouseEvent) => {
    if (!scrollContainerRef.value) return
    isDragging.value = true
    hasDragged.value = false
    startX.value = e.pageX - scrollContainerRef.value.offsetLeft
    dragStartX.value = e.pageX
    scrollLeft.value = scrollContainerRef.value.scrollLeft
    scrollContainerRef.value.style.cursor = 'grabbing'
    scrollContainerRef.value.style.userSelect = 'none'
  }

  // 마우스 드래그 중
  const handleMouseMove = (e: MouseEvent) => {
    if (!isDragging.value || !scrollContainerRef.value) return
    e.preventDefault()
    const x = e.pageX - scrollContainerRef.value.offsetLeft
    const walk = (x - startX.value) * 2 // 스크롤 속도 조절
    scrollContainerRef.value.scrollLeft = scrollLeft.value - walk

    // 드래그 여부 확인 (5px 이상 이동하면 드래그로 간주)
    if (Math.abs(e.pageX - dragStartX.value) > 5) {
      hasDragged.value = true
    }
  }

  // 마우스 드래그 종료
  const handleMouseUp = () => {
    if (!scrollContainerRef.value) return
    isDragging.value = false
    scrollContainerRef.value.style.cursor = 'grab'
    scrollContainerRef.value.style.userSelect = 'auto'
    // 드래그가 아니었다면 클릭으로 간주하지 않음 (아이템 클릭 핸들러에서 처리)
    setTimeout(() => {
      hasDragged.value = false
    }, 100)
  }

  // 마우스가 컨테이너를 벗어났을 때
  const handleMouseLeave = () => {
    if (!scrollContainerRef.value) return
    isDragging.value = false
    hasDragged.value = false
    scrollContainerRef.value.style.cursor = 'grab'
    scrollContainerRef.value.style.userSelect = 'auto'
  }

  // 마우스 휠 스크롤 처리
  const handleWheel = (e: WheelEvent) => {
    if (!scrollContainerRef.value) return
    // Shift 키를 누르지 않았을 때만 가로 스크롤
    if (!e.shiftKey && e.deltaY !== 0) {
      e.preventDefault()
      scrollContainerRef.value.scrollLeft += e.deltaY
    }
  }

  // 터치 이벤트 처리
  const touchStartX = ref(0)
  const touchScrollLeft = ref(0)
  const touchDragStartX = ref(0)
  const touchHasDragged = ref(false)

  const handleTouchStart = (e: TouchEvent) => {
    if (!scrollContainerRef.value) return
    touchStartX.value = e.touches[0].pageX - scrollContainerRef.value.offsetLeft
    touchDragStartX.value = e.touches[0].pageX
    touchScrollLeft.value = scrollContainerRef.value.scrollLeft
    touchHasDragged.value = false
  }

  const handleTouchMove = (e: TouchEvent) => {
    if (!scrollContainerRef.value) return
    const x = e.touches[0].pageX - scrollContainerRef.value.offsetLeft
    const walk = (x - touchStartX.value) * 2
    scrollContainerRef.value.scrollLeft = touchScrollLeft.value - walk

    // 드래그 여부 확인 (5px 이상 이동하면 드래그로 간주)
    if (Math.abs(e.touches[0].pageX - touchDragStartX.value) > 5) {
      touchHasDragged.value = true
    }
  }

  const handleTouchEnd = () => {
    setTimeout(() => {
      touchHasDragged.value = false
    }, 100)
  }

  // 비디오 재생 관리
  const videoRefs = ref<Map<number, HTMLVideoElement>>(new Map())
  const hoveredVideos = ref<Set<number>>(new Set())
  const touchedVideos = ref<Set<number>>(new Set())

  const registerVideo = (index: number, video: HTMLVideoElement | null) => {
    if (video) {
      videoRefs.value.set(index, video)
    } else {
      videoRefs.value.delete(index)
      hoveredVideos.value.delete(index)
      touchedVideos.value.delete(index)
    }
  }

  // 비디오 hover 시작
  const handleVideoMouseEnter = (index: number) => {
    hoveredVideos.value.add(index)
    playVideoIfNeeded(index)
  }

  // 비디오 hover 종료
  const handleVideoMouseLeave = (index: number) => {
    hoveredVideos.value.delete(index)
    pauseVideoIfNeeded(index)
  }

  // 비디오 터치 시작
  const handleVideoTouchStart = (index: number) => {
    touchedVideos.value.add(index)
    playVideoIfNeeded(index)
  }

  // 비디오 터치 종료
  const handleVideoTouchEnd = (index: number) => {
    touchedVideos.value.delete(index)
    pauseVideoIfNeeded(index)
  }

  // 비디오 재생 (hover 또는 touch 상태일 때만)
  const playVideoIfNeeded = (index: number) => {
    const video = videoRefs.value.get(index)
    if (!video) return

    const shouldPlay =
      hoveredVideos.value.has(index) || touchedVideos.value.has(index)

    if (shouldPlay && video.paused) {
      video.play().catch(() => {
        // 재생 실패 시 무시
      })
    }
  }

  // 비디오 일시정지 (hover와 touch 모두 없을 때)
  const pauseVideoIfNeeded = (index: number) => {
    const video = videoRefs.value.get(index)
    if (!video) return

    const shouldPause =
      !hoveredVideos.value.has(index) && !touchedVideos.value.has(index)

    if (shouldPause && !video.paused) {
      video.pause()
      video.currentTime = 0 // 처음으로 되돌림
    }
  }

  // 아이템 클릭 시 링크 이동 (데모용)
  const handleItemClick = (
    item: HorizontalSliderItem,
    index: number,
    e: MouseEvent | TouchEvent,
  ) => {
    // 드래그 중이면 클릭으로 간주하지 않음
    if (hasDragged.value || touchHasDragged.value || isDragging.value) {
      return
    }

    if (item.link) {
      // 데모용: 실제 이동 로직은 구현하지 않음
      console.log(`링크 이동: ${item.link}`, { item, index })
      // 실제 구현 시: navigateTo(item.link) 또는 window.location.href = item.link
    }
  }
</script>

<template>
  <div class="" style="position: relative; width: 100%; min-width: 0">
    <div
      ref="scrollContainerRef"
      class=""
      style="
        width: 100%;
        min-width: 0;
        overflow-x: auto;
        overflow-y: hidden;
        cursor: grab;
        scroll-behavior: smooth;
        -webkit-overflow-scrolling: touch;
        scrollbar-width: thin;
        scrollbar-color: var(--color-grey-300) var(--color-grey-100);
      "
      @mousedown="handleMouseDown"
      @mousemove="handleMouseMove"
      @mouseup="handleMouseUp"
      @mouseleave="handleMouseLeave"
      @touchstart="handleTouchStart"
      @touchmove="handleTouchMove"
      @touchend="handleTouchEnd"
      @wheel="handleWheel"
    >
      <div
        class="gap-4"
        style="display: flex; padding: 0.5rem 1rem 0.5rem 0; width: max-content"
      >
        <div
          v-for="(item, index) in props.items"
          :key="index"
          class=""
          style="flex-shrink: 0"
        >
          <div
            class="rounded-2xl"
            style="
              display: flex;
              flex-direction: column;
              max-width: 400px;
              background-color: #ffffff;
              overflow: hidden;
              box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
              transition:
                transform 200ms ease-in-out,
                box-shadow 200ms ease-in-out;
            "
            :class="{
              'horizontal-slider__card--clickable': item.link,
            }"
            @click="(e) => handleItemClick(item, index, e)"
          >
            <!-- 비디오/이미지 영역 -->
            <div
              class=""
              style="
                position: relative;
                width: 350px;
                height: 300px;
                display: flex;
                align-items: center;
                justify-content: center;
                background-color: #000000;
                overflow: hidden;
              "
              @mouseenter="
                item.type === 'video' ? handleVideoMouseEnter(index) : null
              "
              @mouseleave="
                item.type === 'video' ? handleVideoMouseLeave(index) : null
              "
              @touchstart="
                item.type === 'video' ? handleVideoTouchStart(index) : null
              "
              @touchend="
                item.type === 'video' ? handleVideoTouchEnd(index) : null
              "
            >
              <!-- 비디오 -->
              <template v-if="item.type === 'video' && item.videoSrc">
                <video
                  :ref="
                    (el) => registerVideo(index, el as HTMLVideoElement | null)
                  "
                  :src="item.videoSrc"
                  :poster="item.thumbnail"
                  class="horizontal-slider__media horizontal-slider__video"
                  muted
                  playsinline
                  loop
                />
              </template>

              <!-- 이미지 -->
              <template v-else-if="item.type === 'image' && item.imageSrc">
                <img
                  :src="item.imageSrc"
                  :alt="item.title || `이미지 ${index + 1}`"
                  class="horizontal-slider__media horizontal-slider__image"
                />
              </template>
            </div>

            <!-- 타이틀 영역 -->
            <div
              v-if="item.title || item.description"
              class="p-4 gap-2"
              style="display: flex; flex-direction: column"
            >
              <h3
                v-if="item.title"
                class="text-xl font-semibold text-grey-900 m-0"
                style="line-height: 1.4"
              >
                {{ item.title }}
              </h3>
              <p
                v-if="item.description"
                class="text-base text-grey-900 m-0"
                style="line-height: 1.5; opacity: 0.8"
              >
                {{ item.description }}
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
