import '@mdi/font/css/materialdesignicons.css'
import { createVuetify } from 'vuetify'
import { aliases, mdi } from 'vuetify/iconsets/mdi'
import { ko } from 'vuetify/locale'
import { h } from 'vue'
import { COLORS } from '~/constants/design'

export default defineNuxtPlugin((nuxtApp) => {
  const vuetify = createVuetify({
    ssr: true,
    /**
     * Vuetify 내부에서 VProgressLinear의 bgOpacity/bufferOpacity가 undefined로 흘러가면
     * inline style에 `opacity: NaN`이 찍히는 케이스가 있어 기본값을 숫자로 강제합니다.
     * (주로 v-field loader 영역에서 관찰됨)
     */
    defaults: {
      VProgressLinear: {
        bgOpacity: 0.2,
        bufferOpacity: 0.2,
      },
    },
    theme: {
      defaultTheme: 'light',
      themes: {
        light: {
          colors: {
            primary: COLORS.primary600, // Primary/P600(Base)
            secondary: COLORS.newBlue100, // e6ecff
            accent: COLORS.newBlue400, // 81a0ff
            error: COLORS.functionalNegative, // de1f37
            info: COLORS.blue600, // 0171d5
            success: COLORS.functionalSuccess, // 008a3e
            warning: COLORS.orange600, // ee6f00
            surface: COLORS.staticWhite, // f9fafb
            'on-surface': COLORS.neutralTextPlaceholder, // 28303a
            'on-primary': COLORS.primary600, // ffffff
            'on-secondary': COLORS.primary600, // 181e25
            'on-accent': COLORS.primary600, // ffffff
            'on-error': COLORS.primary600, // ffffff
            'on-info': COLORS.primary600, // ffffff
            'on-success': COLORS.primary600, // ffffff
            'on-warning': COLORS.primary600, // ffffff
            background: COLORS.staticWhite, // f9fafb
            'on-background': COLORS.primary600, // 28303a
          },
        },
        dark: {
          colors: {
            primary: COLORS.primary600, // Primary/P600(Base)
            secondary: COLORS.newBlue100, // e6ecff
            accent: COLORS.newBlue400, // 81a0ff
            error: COLORS.functionalNegative, // de1f37
            info: COLORS.blue600, // 0171d5
            success: COLORS.functionalSuccess, // 008a3e
            warning: COLORS.orange600, // ee6f00
            surface: COLORS.staticWhite, // f9fafb
            'on-surface': COLORS.neutralTextPlaceholder, // 28303a
            'on-primary': COLORS.primary600, // ffffff
            'on-secondary': COLORS.primary600, // 181e25
            'on-accent': COLORS.primary600, // ffffff
            'on-error': COLORS.primary600, // ffffff
            'on-info': COLORS.primary600, // ffffff
            'on-success': COLORS.primary600, // ffffff
            'on-warning': COLORS.primary600, // ffffff
            background: COLORS.staticWhite, // f9fafb
            'on-background': COLORS.primary600, // 28303a
          },
        },
      },
    },

    // @TODO: v-icon 제거하기
    icons: {
      defaultSet: 'mdi',
      aliases: aliases,
      sets: {
        mdi,
        local: {
          component: (props) =>
            h('svg', { 'aria-hidden': 'true', class: 'v-local-icon__svg' }, [
              h('use', { 'xlink:href': `/icons/${props.icon}.svg` }),
            ]),
        },
      },
    },
    locale: {
      locale: 'ko',
      fallback: 'en',
      messages: { ko },
    },
    date: {
      locale: ['ko'],
    },
  })

  nuxtApp.vueApp.use(vuetify)
})
