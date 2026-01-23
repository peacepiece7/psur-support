// ============================================
// Design System Constants (Vuetify)
// ============================================

// ============================================
// Colors
// ============================================

export const COLORS = {
  // New Blue (from main.css)
  newBlue50: '#f7f9ff',
  newBlue100: '#e6ecff',
  newBlue200: '#d0dbff',
  newBlue300: '#a4bbff',
  newBlue400: '#81a0ff',
  newBlue500: '#5b81f3',
  newBlue600: '#3661e5',
  newBlue700: '#1a48d5',
  newBlue800: '#0a38c2',
  newBlue900: '#0630ae',

  // Blue (from main.css)
  blue50: '#f5f8ff',
  blue100: '#eaf0ff',
  blue200: '#d2e2ff',
  blue300: '#a2cbff',
  blue400: '#66aaff',
  blue500: '#2485ff',
  blue600: '#0171d5',
  blue700: '#0156a8',
  blue800: '#00407f',
  blue900: '#003064',

  // Sky Blue (from main.css)
  skyBlue50: '#f0faff',
  skyBlue100: '#e2f6ff',
  skyBlue200: '#d0f0ff',
  skyBlue300: '#9fdfff',
  skyBlue400: '#54c6ff',
  skyBlue500: '#21b5ff',
  skyBlue600: '#0291d8',
  skyBlue700: '#0075af',
  skyBlue800: '#005c8a',
  skyBlue900: '#004568',

  // Cyan (from main.css)
  cyan50: '#f0fdff',
  cyan100: '#e1fafe',
  cyan200: '#d1f1f6',
  cyan300: '#92e7f3',
  cyan400: '#2ed3e9',
  cyan500: '#04b8cf',
  cyan600: '#0298ab',
  cyan700: '#028798',
  cyan800: '#007482',
  cyan900: '#01636f',

  // Green (from main.css)
  green50: '#f1fff4',
  green100: '#e5ffe9',
  green200: '#d2fadb',
  green300: '#9af0b0',
  green400: '#48c16d',
  green500: '#25a555',
  green600: '#008a3e',
  green700: '#006d2f',
  green800: '#005322',
  green900: '#003915',

  // Purple Blue (from main.css)
  purpleBlue50: '#f8f5ff',
  purpleBlue100: '#efe9ff',
  purpleBlue200: '#ded3ff',
  purpleBlue300: '#b0a3f4',
  purpleBlue400: '#927cf1',
  purpleBlue500: '#7e50f4',
  purpleBlue600: '#5729cb',
  purpleBlue700: '#461dad',
  purpleBlue800: '#310d8b',
  purpleBlue900: '#240967',

  // Purple (from main.css)
  purple50: '#fbf7ff',
  purple100: '#f5ecff',
  purple200: '#e7d1ff',
  purple300: '#d3abfd',
  purple400: '#b882f2',
  purple500: '#9b59e0',
  purple600: '#7729cb',
  purple700: '#6315b6',
  purple800: '#511295',
  purple900: '#3f0978',

  // Red (from main.css)
  red50: '#fff6f7',
  red100: '#ffdde1',
  red200: '#ffafb9',
  red300: '#f77b8a',
  red400: '#f25569',
  red500: '#ec354c',
  red600: '#de1f37',
  red700: '#bf0018',
  red800: '#980517',
  red900: '#760210',

  // Orange (from main.css)
  orange50: '#fff2e5',
  orange100: '#ffe3c9',
  orange200: '#ffcd9f',
  orange300: '#ffb570',
  orange400: '#fb9c43',
  orange500: '#f88010',
  orange600: '#ee6f00',
  orange700: '#bd4d02',
  orange800: '#8c3800',
  orange900: '#6f2e02',

  // Yellow (from main.css)
  yellow50: '#fff8dc',
  yellow100: '#fff0b2',
  yellow200: '#ffe57c',
  yellow300: '#ffd735',
  yellow400: '#ffcc13',
  yellow500: '#f3bb01',
  yellow600: '#dda600',
  yellow700: '#c08300',
  yellow800: '#926401',
  yellow900: '#744f00',

  // Grey (from main.css)
  grey50: '#f9fafb',
  grey100: '#f3f5f8',
  grey200: '#e1e7ee',
  grey300: '#d0d4d9',
  grey400: '#a2a9b3',
  grey500: '#858f9d',
  grey600: '#6a7583',
  grey700: '#505b69',
  grey800: '#28303a',
  grey900: '#181e25',

  // Static Colors (from main.css)
  staticWhite: '#ffffff',
  staticBlack: '#000000',

  // Primary Colors (from main.css)
  primary600: '#3661e5', // Primary/P600(Base)

  // Neutral Colors (from main.css)
  // Neutral/Text
  neutralTextPlaceholder: '#6a7583', // Neutral/Text/Placeholder
  neutralTextDisabled: '#858f9d', // Neutral/Text/Disabled
  neutralTextBody: '#28303a', // Neutral/Text/Body
  neutralTextTitle: '#181e25', // Neutral/Text/Text,Title
  neutralTextText: '#181e25', // Neutral/Text/Text,Title
  neutralTextCaption: '#505b69', // Neutral/Text/Caption

  // Neutral/Fill
  neutralFill50: '#f9fafb', // Neutral/Fill/Fill-50
  neutralFill50Opacity50: 'rgba(249, 250, 251, 0.5)', // Neutral/Fill/Fill-50 with opacity 0.5
  neutralFill100: '#f3f5f8', // Neutral/Fill/Fill-100
  neutralFill100Opacity50: 'rgba(243, 245, 248, 0.5)', // Neutral/Fill/Fill-100 with opacity 0.5
  neutralFill200: '#e1e7ee', // Neutral/Fill/Fill-200
  neutralFill200Opacity50: 'rgba(225, 231, 238, 0.5)', // Neutral/Fill/Fill-200 with opacity 0.5

  // Neutral/Stroke
  neutralStroke200: '#e1e7ee', // Neutral/Stroke/Stroke-200
  neutralStroke300: '#d0d4d9', // Neutral/Stroke/Stroke-300

  // Functional Colors (from main.css)
  functionalPositive: '#008a3e', // Functional/Positive
  functionalNegative: '#de1f37', // Functional/Negative
  functionalSuccess: '#008a3e', // Functional/Success
} as const

// ============================================
// Spacing (in rem)
// ============================================

export const SPACING = {
  0: '0',
  1: '0.25rem', // 4px
  2: '0.5rem', // 8px
  3: '0.75rem', // 12px
  4: '1rem', // 16px
  5: '1.25rem', // 20px
  6: '1.5rem', // 24px
  8: '2rem', // 32px
  10: '2.5rem', // 40px
  12: '3rem', // 48px
  16: '4rem', // 64px
  20: '5rem', // 80px
  24: '6rem', // 96px
} as const

// ============================================
// Component Sizes
// ============================================

export const INPUT = {
  height: {
    xs: '2rem', // 32px
    sm: '2.5rem', // 40px
    md: '3rem', // 48px
    lg: '3.5rem', // 56px
    max: '100%',
  },
  width: {
    xs: '120px',
    sm: '200px',
    md: '280px',
    lg: '360px',
    xl: '480px',
    max: '100%',
  },
  paddingX: {
    sm: SPACING[3], // 12px
    md: SPACING[4], // 16px
    lg: SPACING[5], // 20px
  },
  paddingY: {
    sm: SPACING[2], // 8px
    md: SPACING[2], // 8px
    lg: SPACING[3], // 12px
  },
  fontSize: {
    sm: '0.875rem', // 14px
    md: '1rem', // 16px
    lg: '1.25rem', // 20px
  },
  borderRadius: '0.5rem', // 8px
} as const
