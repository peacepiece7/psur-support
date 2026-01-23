const fs = require('fs/promises')
const path = require('path')
const { rimraf } = require('rimraf')
const OpenAPI = require('openapi-typescript-codegen')

/** 실행 기준 디렉터리 */
const ROOT_DIR = process.cwd()

/** 출력 루트 */
const OUTPUT_ROOT = path.resolve(ROOT_DIR, '__generated__')

/** Swagger base url */
const SWAGGER_BASE_URL = 'http://127.0.0.1:9090/v3/api-docs'

async function main() {
  console.log('🧹 clean generated directory')
  await rimraf(OUTPUT_ROOT)
  await fs.mkdir(OUTPUT_ROOT, { recursive: true })

  const groups = await fetchGroups()
  console.log(`📦 found groups: ${groups.join(', ')}`)

  for (const group of groups) {
    await generate(group)
  }

  console.log('✅ OpenAPI generation completed')
}

/** 1️⃣ /v3/api-docs 에서 group 목록 수집 */
async function fetchGroups() {
  const res = await fetch(SWAGGER_BASE_URL)
  if (!res.ok) {
    throw new Error('Failed to fetch api-docs root')
  }

  const json = await res.json()

  // 단일 spec fallback
  if (!Array.isArray(json.groups)) {
    console.warn('⚠️ no groups found, fallback to default spec')
    return ['default']
  }

  return json.groups
}

/** 2️⃣ group 별 spec 다운로드 + ts 생성 */
async function generate(group) {
  const outDir = path.join(OUTPUT_ROOT, group)
  const specFile = path.join(outDir, 'spec.json')
  const specUrl =
    group === 'default'
      ? SWAGGER_BASE_URL
      : `${SWAGGER_BASE_URL}/${encodeURIComponent(group)}`

  console.log(`\n📥 fetch spec: ${group}`)
  await fs.mkdir(outDir, { recursive: true })

  const res = await fetch(specUrl)
  if (!res.ok) {
    throw new Error(`Failed to fetch ${specUrl}`)
  }

  const json = await res.json()
  await fs.writeFile(specFile, JSON.stringify(json, null, 2), 'utf-8')

  console.log(`⚙️ generate types: ${group}`)
  await OpenAPI.generate({
    input: specFile,
    output: outDir,
    useOptions: true,
    useUnionTypes: true,
    exportSchemas: true,
    exportServices: true,
  })

  await postProcess(outDir)
}

/** 3️⃣ 후처리 */
async function postProcess(dir) {
  const files = await fs.readdir(dir)

  for (const file of files) {
    if (!file.endsWith('.ts')) continue

    const fullPath = path.join(dir, file)
    let code = await fs.readFile(fullPath, 'utf-8')

    // eslint-disable 주석 제거
    code = code.replace(/\/\*\s*eslint-disable.*?\*\//g, '')

    // 과도한 공백 정리
    code = code.replace(/\n{3,}/g, '\n\n')

    await fs.writeFile(fullPath, code, 'utf-8')
  }
}

main().catch((err) => {
  console.error(err)
  process.exit(1)
})
