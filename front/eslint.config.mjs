import withNuxt from './.nuxt/eslint.config.mjs'

export default withNuxt(
  {
    files: [
      '**/*.vue',
      '**/*.js',
      '**/*.jsx',
      '**/*.cjs',
      '**/*.mjs',
      '**/*.ts',
      '**/*.tsx',
      '**/*.cts',
      '**/*.mts',
    ],
    rules: {
      'no-console': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
      'no-debugger': process.env.NODE_ENV === 'production' ? 'warn' : 'off',
      '@typescript-eslint/ban-ts-comment': 'off',
      '@typescript-eslint/no-explicit-any': 'warn',
      '@typescript-eslint/no-unused-vars': 'off',
      'vue/html-self-closing': 'off',
      'vue/no-unused-vars': 'warn',
    },
    linterOptions: {
      reportUnusedDisableDirectives: 'off',
    },
  },
  {
    ignores: ['**/public/'],
  },
)
