import vuetify from 'vite-plugin-vuetify'

export default defineNuxtConfig({
  /* --------------------------------------------------
   * Core / Versioning
   * 페이지 구조를 nuxt 4 기준으로 변경
   * -------------------------------------------------- */
  future: {
    compatibilityVersion: 4,
  },

  compatibilityDate: '2025-01-14',

  experimental: {
    componentIslands: true,
  },

  /* --------------------------------------------------
   * Runtime / Server
   * @TODO: VERCEL 테스트용 나중에 node-server만 쓸 예정
   * -------------------------------------------------- */
  nitro: {
    preset: process.env.VERCEL ? 'vercel' : 'node-server',
    routeRules: {
      '/': { redirect: '/home' },
    },
  },

  runtimeConfig: {
    public: {
      VUE_APP_KAKAO_APIKEY: process.env.VUE_APP_KAKAO_APIKEY,
    },
  },

  /* --------------------------------------------------
   * Build & Vite
   * -------------------------------------------------- */
  build: {
    transpile: ['@ckeditor/ckeditor5-vue', 'vuetify', '@vuepic/vue-datepicker'],
  },

  vite: {
    plugins: [
      vuetify({
        autoImport: {
          labs: true,
          ignore: [],
        },
      }),
    ],
  },

  /* --------------------------------------------------
   * App / Head
   * -------------------------------------------------- */
  app: {
    baseURL: '/',
    head: {
      htmlAttrs: {
        lang: 'ko',
      },
      meta: [{ charset: 'utf-8' }],
    },
  },

  /* --------------------------------------------------
   * Modules
   * -------------------------------------------------- */
  modules: [
    '@vueuse/nuxt',
    '@pinia/nuxt',
    '@vee-validate/nuxt',
    '@nuxt/image',
    '@nuxt/eslint',
    'vue3-carousel-nuxt',
    'dayjs-nuxt',
  ],

  /* --------------------------------------------------
   * Module Configs
   * -------------------------------------------------- */
  carousel: {
    prefix: 'Vue3',
  },

  veeValidate: {
    autoImports: true,
    componentNames: {
      Form: 'VeeForm',
      Field: 'VeeField',
      FieldArray: 'VeeFieldArray',
      ErrorMessage: 'VeeErrorMessage',
    },
  },

  pinia: {
    storesDirs: ['./app/stores/**'],
  },

  /* --------------------------------------------------
   * Auto Imports
   * -------------------------------------------------- */
  components: {
    dirs: [
      {
        path: '~/components',
        pathPrefix: false,
        ignore: ['molecules/MyCKEditor.vue'],
      },
    ],
  },

  imports: {
    presets: [
      {
        from: 'vuetify',
        imports: ['useDisplay', 'useTheme', 'useLocale'],
      },
    ],
  },

  /* --------------------------------------------------
   * Styles
   * plugin에 css 직접 추가하면 tree shaking 안됨 주의!
   * -------------------------------------------------- */
  css: [
    '@/assets/css/main.css',
    '@/assets/css/vuetify/main.css',
    '@vuepic/vue-datepicker/dist/main.css',
  ],

  postcss: {
    plugins: {
      // Tailwind CSS v4 (CSS-first) PostCSS plugin
      '@tailwindcss/postcss': {},
    },
  },

  /* --------------------------------------------------
   * Tooling
   * -------------------------------------------------- */
  sourcemap: {
    client: false,
    server: false,
  },

  eslint: {},
})
