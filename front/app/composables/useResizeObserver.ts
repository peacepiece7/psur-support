export const useResizeObserver = (
  target: Ref<HTMLElement | null>,
  callback: (entries: ResizeObserverEntry[]) => void,
) => {
  let observer: ResizeObserver | null = null

  watch(
    target,
    (newTarget) => {
      if (observer && newTarget) {
        observer.unobserve(newTarget)
        observer.disconnect()
      }
      if (newTarget) {
        observer = new ResizeObserver(callback)
        observer.observe(newTarget)
      }
    },
    { immediate: true },
  )

  onBeforeUnmount(() => {
    if (observer && target.value) {
      observer.unobserve(target.value)
      observer.disconnect()
    }
  })
}
