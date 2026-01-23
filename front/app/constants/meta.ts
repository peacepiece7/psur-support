export const HOME_META = {
  TITLE: '스포츠클럽포털',
  DESCRIPTION:
    '대한민국 스포츠클럽 통합 포털 - 스포츠클럽 검색, 커뮤니티, 정보 제공',
  KEYWORDS: '스포츠클럽, 스포츠, 체육, 운동, 클럽, 포털, 체육회',
  OG_TITLE: '스포츠클럽포털',
  OG_DESCRIPTION:
    '대한민국 스포츠클럽 통합 포털 - 스포츠클럽 검색, 커뮤니티, 정보 제공',
  OG_TYPE: 'website',
  OG_IMAGE: '/og-image.jpg',
} as const

export const HOME_META_HEADER = {
  title: HOME_META.TITLE,
  htmlAttrs: {
    lang: 'ko',
  },
  meta: [
    { charset: 'utf-8' },
    { name: 'viewport', content: 'width=device-width, initial-scale=1' },
    { name: 'description', content: HOME_META.DESCRIPTION },
    { name: 'keywords', content: HOME_META.KEYWORDS },
    { name: 'format-detection', content: 'telephone=no' },
    // Open Graph
    { property: 'og:title', content: HOME_META.OG_TITLE },
    { property: 'og:description', content: HOME_META.OG_DESCRIPTION },
    { property: 'og:type', content: HOME_META.OG_TYPE },
    { property: 'og:image', content: HOME_META.OG_IMAGE },
    { property: 'og:locale', content: 'ko_KR' },
    // Twitter Card
    { name: 'twitter:card', content: 'summary_large_image' },
    { name: 'twitter:title', content: HOME_META.OG_TITLE },
    { name: 'twitter:description', content: HOME_META.OG_DESCRIPTION },
    { name: 'twitter:image', content: HOME_META.OG_IMAGE },
  ],
  link: [
    { rel: 'icon', type: 'image/x-icon', href: '/favicon.ico' },
    { rel: 'apple-touch-icon', href: '/apple-touch-icon.png' },
    { rel: 'manifest', href: '/site.webmanifest' },
    // {
    //   rel: 'stylesheet',
    //   href: 'https://cdn.ckeditor.com/ckeditor5/45.1.0/ckeditor5-content.css',
    // },
  ],
  script: [
    // {
    //   src: '/home/js/pgAsistant.js',
    //   defer: true,
    // },
    {
      src: 'https://static.nid.naver.com/js/naveridlogin_js_sdk_2.0.2.js',
      defer: true,
    },
  ],
}

export const HOME_SEO_META = {
  title: HOME_META.TITLE,
  description: HOME_META.DESCRIPTION,
  ogTitle: HOME_META.OG_TITLE,
  ogDescription: HOME_META.OG_DESCRIPTION,
  ogImage: HOME_META.OG_IMAGE,
  ogType: HOME_META.OG_TYPE,
  twitterCard: 'summary_large_image',
  twitterTitle: HOME_META.OG_TITLE,
  twitterDescription: HOME_META.OG_DESCRIPTION,
  twitterImage: HOME_META.OG_IMAGE,
} as const

// ------------------------------------------------------------------------------------------
