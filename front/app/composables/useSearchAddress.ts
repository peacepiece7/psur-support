import { onMounted } from 'vue'

declare global {
  interface Window {
    kakao: any
  }
}

// daum-api-type.d.ts에 PostcodeData interface 참고
function useSearchAddress(
  predicate: (type: 'success' | 'close' | 'error', data: any | null) => void,
) {
  const config = useRuntimeConfig()

  // * 스크립트 추가
  onMounted(() => {
    if (typeof window === 'undefined') return
    if (!document.querySelector('#daum-postcode-script')) {
      const DaumMapscript = document.createElement('script')
      DaumMapscript.src =
        '//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js'
      DaumMapscript.id = 'daum-postcode-script'
      document.body.appendChild(DaumMapscript)
    }
    if (!window.kakao || !window.kakao.maps) {
      const kakaoScripts = document.querySelector('#kakao-map-script')
      if (kakaoScripts) return

      const KakaoMapScript = document.createElement('script')
      KakaoMapScript.src = `//dapi.kakao.com/v2/maps/sdk.js?appkey=${config.public.VUE_APP_KAKAO_APIKEY}&libraries=services&autoload=false`
      KakaoMapScript.id = 'kakao-map-script'
      KakaoMapScript.onload = () => window.kakao.maps.load(() => {})
      document.body.appendChild(KakaoMapScript)
    }
  })

  const openPopup = () => {
    try {
      console.log('open popup')
      if (!window.kakao?.maps) {
        console.log('Kakao Maps not loaded')
        throw new Error('Kakao Maps not loaded')
      }

      new daum.Postcode({
        oncomplete: async function (data: any) {
          try {
            let mainAddress = ''
            let zipCode = ''
            let lng = 0
            let lat = 0

            mainAddress = data.roadAddress || data.jibunAddress
            zipCode = data.zonecode

            const coords = await getAddressCoords(mainAddress)
            lng = coords.getLng()
            lat = coords.getLat()

            predicate('success', { ...data, lng, lat })
          } catch (err) {
            predicate('success', data)
          }
        },
        onclose: function () {
          predicate('close', null)
        },
      }).open()
    } catch (err) {
      predicate('error', null)
    }
  }

  const getAddressCoords = (address: string): Promise<any> => {
    return new Promise((resolve, reject) => {
      if (!window.kakao?.maps?.services) {
        reject(new Error('Kakao Maps services not loaded'))
        return
      }
      const geoCoder = new window.kakao.maps.services.Geocoder()
      geoCoder.addressSearch(address, (result: any, status: any) => {
        if (status === window.kakao.maps.services.Status.OK) {
          const coords = new window.kakao.maps.LatLng(result[0].y, result[0].x)
          resolve(coords)
        } else {
          reject(status)
        }
      })
    })
  }

  return { openPopup }
}

export { useSearchAddress }
