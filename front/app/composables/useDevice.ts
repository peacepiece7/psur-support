import { computed, ref, onBeforeMount } from 'vue'
import { useDisplay } from 'vuetify'

type DeviceType = 'mobile' | 'tablet' | 'desktop'

/** UserAgent 기반 기기 판별 */
function _detectFromUA(ua: string): DeviceType {
  const uaLower = ua.toLowerCase()

  const isTablet = /ipad|tablet|playbook|silk|android(?!.*mobile)/i.test(
    uaLower,
  )
  if (isTablet) return 'tablet'

  const isMobile =
    /iphone|ipod|android|blackberry|iemobile|opera mini|webos/i.test(uaLower)
  if (isMobile) return 'mobile'

  return 'desktop'
}

/**
 * @description
 * - DeviceType은 UA 기반으로 **스크린 사이즈에 영향을 받지 않습니다**
 * - is[Mobile|Tablet|Desktop]은 **스크린 사이즈 기반으로 판별**됩니다.
 */
export const useDevice = () => {
  const display = useDisplay()

  /** UA 기반 기기 타입 (절대값, resize 영향 없음) */
  const deviceType = ref<DeviceType>('desktop')

  onBeforeMount(() => {
    if (import.meta.client) {
      deviceType.value = _detectFromUA(navigator.userAgent)
    }
  })

  /** 화면 크기 기반 UI 상태 */
  const isMobile = computed(() => display.xs.value) // < 600
  const isTablet = computed(() => display.sm.value || display.md.value) // 600 ~ 1279
  const isDesktop = computed(() => display.lgAndUp.value) // ≥ 1280
  /** breakpoint UI 타입 */
  const breakpointType = computed(() => {
    if (isMobile.value) return 'mobile'
    if (isTablet.value) return 'tablet'
    return 'desktop'
  })

  return {
    /** 기기 자체 (UA 기반) */
    deviceType: computed(() => deviceType.value),

    /** UI 상태 (반응형) */
    isMobile,
    isTablet,
    isDesktop,

    breakpointType,
  }
}
