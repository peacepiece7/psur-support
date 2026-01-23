import type { MaybeRefOrGetter, Ref } from 'vue'
import { ref, toValue, watch } from 'vue'
import { tryOnScopeDispose } from '@vueuse/core'

export interface UseIntersectionObserverOptions {
  /**
   * The Element or Document whose bounds are used as the bounding box when testing for intersection.
   */
  root?: MaybeRefOrGetter<Element | Document | null | undefined>

  /**
   * A string which specifies a set of offsets to add to the root's bounding_box when calculating intersections.
   * @default '0px'
   */
  rootMargin?: string

  /**
   * Either a single number or an array of numbers between 0.0 and 1.
   * @default 0
   */
  threshold?: number | number[]

  /**
   * Start the IntersectionObserver immediately on creation
   * @default true
   */
  immediate?: boolean
}

export interface UseIntersectionObserverReturn {
  /**
   * Whether the target element is intersecting
   */
  isIntersecting: Ref<boolean>

  /**
   * The intersection ratio
   */
  intersectionRatio: Ref<number>

  /**
   * Stop observing
   */
  stop: () => void

  /**
   * Start observing
   */
  start: () => void
}

/**
 * Detects that a target element's visibility.
 *
 * @param target - The target element to observe
 * @param callback - Callback function when intersection changes
 * @param options - IntersectionObserver options
 */
export function useIntersectionObserver(
  target: MaybeRefOrGetter<Element | null | undefined>,
  callback?: (entry: IntersectionObserverEntry) => void,
  options: UseIntersectionObserverOptions = {
    threshold: 0.2,
    rootMargin: '0px',
  },
): UseIntersectionObserverReturn {
  const { root, rootMargin = '0px', threshold = 0, immediate = true } = options

  const isIntersecting = ref(false)
  const intersectionRatio = ref(0)

  let observer: IntersectionObserver | null = null
  let isActive = immediate

  const cleanup = () => {
    if (observer) {
      observer.disconnect()
      observer = null
    }
  }

  const stop = () => {
    cleanup()
    isActive = false
  }

  const createObserver = () => {
    const element = toValue(target)
    const rootElement = toValue(root)

    if (!element || !isActive) {
      return
    }

    cleanup()

    observer = new IntersectionObserver(
      (entries) => {
        entries.forEach((entry) => {
          isIntersecting.value = entry.isIntersecting
          intersectionRatio.value = entry.intersectionRatio

          if (callback) {
            callback(entry)
          }
        })
      },
      {
        root: rootElement || null,
        rootMargin,
        threshold,
      },
    )

    observer.observe(element)
  }

  const start = () => {
    isActive = true
    createObserver()
  }

  // Watch for target changes
  watch(
    () => toValue(target),
    (element) => {
      if (!isActive) return

      if (element) {
        createObserver()
      } else {
        cleanup()
      }
    },
    { immediate },
  )

  tryOnScopeDispose(() => {
    cleanup()
  })

  return {
    isIntersecting,
    intersectionRatio,
    stop,
    start,
  }
}
