const fs = require('fs')
const path = require('path')
const Fontagon = require('fontagon')

// public/icons 폴더의 파일 목록 읽기
const iconFiles = fs.readdirSync('public/icons')

// 확장자 제거한 파일명 추출
const iconNames = iconFiles
  .filter((file) => file.endsWith('.svg'))
  .map((file) => file.replace(/\.svg$/, ''))

// 타입 파일 생성
const typeDir = path.join('app', 'types', 'icon-font')
const typeFilePath = path.join(typeDir, 'index.ts')

// 디렉토리가 없으면 생성
if (!fs.existsSync(typeDir)) {
  fs.mkdirSync(typeDir, { recursive: true })
}

// 타입 정의 생성 (가독성을 위해 여러 줄로 포맷팅)
const typeDefinition = `export type IconFont =\n  | ${iconNames.map((name) => `'${name}'`).join('\n  | ')}\n`

// 타입 파일 작성
fs.writeFileSync(typeFilePath, typeDefinition, 'utf-8')

// Fontagon({
//   files: ['public/icons/**/*.svg'],
//   dist: 'public/icon-font',
//   fontName: 'icon-font',
//   style: 'css',
//   classOptions: {
//     baseClass: 'icon-font',
//     classPrefix: 'ic',
//   },
// })
//   .then((opts) => {
//     console.log('done! ', opts)
//   })
//   .catch((err) => {
//     console.log('fail! ', err)
//   })

Fontagon({
  files: ['public/icons/**/*.svg'],
  dist: 'app/assets/icon-font',
  fontName: 'icon-font',
  style: 'css',
  classOptions: {
    baseClass: 'icon-font',
    classPrefix: 'ic',
  },
})
  .then((opts) => {
    console.log('done! ', opts)
  })
  .catch((err) => {
    console.log('fail! ', err)
  })

// {
/* <i class="icon-font ft-icon ic-apple"></i> */
// }
