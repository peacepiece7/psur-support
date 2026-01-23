export type LocationData = {
  latitude: number
  longitude: number
  city?: string
  town?: string
  county?: string
  state?: string
  address?: string
}

export const useGeolocation = () => {
  const locationData = ref<LocationData | null>(null)
  const isLoadingLocation = ref(false)
  const error = ref<Error | null>(null)

  // 위치 정보 가져오기
  const getCurrentLocation = () => {
    if (!import.meta.client || !navigator.geolocation) {
      return
    }

    isLoadingLocation.value = true
    error.value = null

    navigator.geolocation.getCurrentPosition(
      async (position) => {
        try {
          const { latitude, longitude } = position.coords

          // Kakao Maps API를 사용한 역지오코딩
          // 환경 변수에서 API 키를 가져옴 (선택사항)
          const KAKAO_REST_API_KEY =
            process.env.NUXT_PUBLIC_KAKAO_REST_API_KEY || ''

          if (KAKAO_REST_API_KEY) {
            // Kakao Maps API 사용
            const response = (await $fetch(
              `https://dapi.kakao.com/v2/local/geo/coord2regioncode.json?x=${longitude}&y=${latitude}`,
              {
                headers: {
                  Authorization: `KakaoAK ${KAKAO_REST_API_KEY}`,
                },
              },
            )) as { documents?: Array<{ address_name?: string }> }

            if (response.documents && response.documents.length > 0) {
              const region = response.documents[0]
              const address = region.address_name || ''
              // "서울특별시 마포구" 형식에서 시와 구 추출
              const parts = address.split(' ')
              const city =
                parts[0]?.replace('특별시', '').replace('광역시', '') || ''
              const district = parts[1] || ''

              locationData.value = {
                latitude,
                longitude,
                city,
                state: district,
                address,
              }
            } else {
              locationData.value = {
                latitude,
                longitude,
              }
            }
          } else {
            // Kakao API 키가 없을 경우 OpenStreetMap Nominatim 사용
            const response = (await $fetch(
              `https://nominatim.openstreetmap.org/reverse?format=json&lat=${latitude}&lon=${longitude}&accept-language=ko`,
            )) as {
              address?: {
                city?: string
                town?: string
                county?: string
                state?: string
              }
            }

            if (response.address) {
              const { city, town, county, state } = response.address
              locationData.value = {
                latitude,
                longitude,
                city: city || town || county,
                county,
                state,
              }
            } else {
              locationData.value = {
                latitude,
                longitude,
              }
            }
          }
        } catch (err) {
          console.error('위치 정보 가져오기 실패:', err)
          error.value =
            err instanceof Error
              ? err
              : new Error('위치 정보를 가져올 수 없습니다')
        } finally {
          isLoadingLocation.value = false
        }
      },
      (err) => {
        console.error('위치 권한 거부 또는 오류:', err)
        error.value = new Error(
          '위치 권한이 거부되었거나 위치를 가져올 수 없습니다',
        )
        isLoadingLocation.value = false
      },
      {
        enableHighAccuracy: true,
        timeout: 10000,
        maximumAge: 0,
      },
    )
  }

  return {
    locationData,
    isLoadingLocation,
    error,
    getCurrentLocation,
  }
}
