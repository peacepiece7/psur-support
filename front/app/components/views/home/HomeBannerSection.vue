<script setup lang="ts">
  import type { CarouselItem } from '~/components/organisms/Carousel.vue'

  // 캐러셀 이미지 데이터
  const carouselItems: CarouselItem[] = [
    {
      videoSrc: '/boy_drible.mp4',
      alt: '배너 비디오 1',
      parallax: true,
      maxHeight: '100vh',
      maxWidth: '100%',
    },
    // {
    //   videoSrc: '/banner_video.mp4',
    //   alt: '배너 비디오 2',
    //   parallax: true,
    //   maxHeight: '100vh',
    //   maxWidth: '100vw',
    // },
  ]
</script>

<template>
  <div
    class="relative overflow-hidden -mt-[65px] mb-[-65px]"
    style="
      width: 100%;
      height: calc(100vh + 65px);
      min-height: 100vh;
      max-width: 100vw;
    "
  >
    <Carousel
      :items="carouselItems"
      class="home-carousel"
      cycle
      height="100%"
    />

    <div
      class="py-8 px-4"
      style="
        position: absolute;
        inset: 0;
        display: grid;
        place-items: center;
        z-index: 2;
        pointer-events: none;
      "
      aria-hidden="true"
    >
      <h1 class="home-banner-section__headline">
        <span class="" style="display: block">SPORTS CLUB</span>
        <span class="" style="display: block">PORTAL</span>
      </h1>
    </div>
  </div>
</template>

<style scoped>
  .home-banner-section__headline {
    margin: 0;
    color: #ffffff;
    font-weight: 800;
    letter-spacing: -0.02em;
    line-height: 1.02;
    text-align: center;
    text-shadow: 0 8px 24px rgba(0, 0, 0, 0.45);
    animation: banner-rise-up 900ms ease-in-out both;
    font-size: 3rem; /* ~48px */
  }

  /* Responsive headline sizing */
  @media (min-width: 768px) {
    .home-banner-section__headline {
      font-size: 4.25rem; /* ~68px */
    }
  }

  @media (min-width: 1024px) {
    .home-banner-section__headline {
      font-size: 5rem; /* ~80px */
    }
  }
</style>
