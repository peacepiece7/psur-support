export const useMap = () => {
  const mapInstance = ref<any>(null)
  const markers = ref<any[]>([])

  const initMap = (container: HTMLElement, options?: any) => {
    // 지도 초기화 로직
    // 예: Google Maps, Naver Maps 등
  }

  const addMarker = (position: { lat: number; lng: number }, options?: any) => {
    // 마커 추가 로직
  }

  const removeMarker = (marker: any) => {
    // 마커 제거 로직
  }

  return {
    mapInstance,
    markers,
    initMap,
    addMarker,
    removeMarker,
  }
}
