export const useTracker = () => {
  const scrollY = ref(0)
  const scrollX = ref(0)
  const scrollDirection = ref<'up' | 'down' | null>(null)
  const lastScrollY = ref(0)

  const updateScroll = () => {
    if (import.meta.client) {
      const currentScrollY = window.scrollY
      const currentScrollX = window.scrollX

      // 스크롤 방향 계산
      if (currentScrollY > lastScrollY.value) {
        scrollDirection.value = 'down'
      } else if (currentScrollY < lastScrollY.value) {
        scrollDirection.value = 'up'
      }

      scrollY.value = currentScrollY
      scrollX.value = currentScrollX
      lastScrollY.value = currentScrollY
    }
  }

  onMounted(() => {
    if (import.meta.client) {
      lastScrollY.value = window.scrollY
      window.addEventListener('scroll', updateScroll, { passive: true })
      updateScroll()
    }
  })

  onBeforeUnmount(() => {
    if (import.meta.client) {
      window.removeEventListener('scroll', updateScroll)
    }
  })

  return {
    scrollY,
    scrollX,
    scrollDirection,
  }
}
