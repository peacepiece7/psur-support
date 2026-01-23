import fs from 'node:fs'
import path from 'node:path'

const WORKSPACE = '/workspace'
const APP_DIR = path.join(WORKSPACE, 'app')
const TARGET_DIRS = [path.join(APP_DIR, 'components'), path.join(APP_DIR, 'pages')]
const MAIN_CSS = path.join(APP_DIR, 'assets/css/main.css')

function walkVueFiles(dir) {
  /** @type {string[]} */
  const out = []
  /** @type {string[]} */
  const stack = [dir]
  while (stack.length) {
    const cur = stack.pop()
    if (!cur) continue
    const entries = fs.readdirSync(cur, { withFileTypes: true })
    for (const e of entries) {
      const abs = path.join(cur, e.name)
      if (e.isDirectory()) stack.push(abs)
      else if (e.isFile() && abs.endsWith('.vue')) out.push(abs)
    }
  }
  return out
}

function rel(p) {
  return p.startsWith(WORKSPACE + path.sep) ? p.slice(WORKSPACE.length + 1) : p
}

function ensureMainCssHasComponentsLayer(css) {
  const idx = css.indexOf('@layer components')
  if (idx === -1) throw new Error(`Could not find @layer components in ${MAIN_CSS}`)
  return idx
}

function insertBeforeFinalClosingBrace(css, insertion) {
  // Assumption: the final `}` in the file closes @layer components.
  const m = css.match(/\n\}\s*$/)
  if (!m || m.index == null) throw new Error(`Could not find final closing brace in ${MAIN_CSS}`)
  const at = m.index
  return css.slice(0, at) + insertion + css.slice(at)
}

function stripStyleScopedBlocks(vue) {
  // Removes all <style scoped>...</style> blocks and returns { updatedVue, extractedCssBlocks }
  /** @type {string[]} */
  const blocks = []

  const re = /<style\s+scoped(?:\s[^>]*)?>\s*([\s\S]*?)\s*<\/style>\s*/g
  let updated = vue
  updated = updated.replace(re, (_full, css) => {
    const trimmed = String(css ?? '').trim()
    if (trimmed) blocks.push(trimmed)
    return ''
  })

  return { updatedVue: updated, extractedCssBlocks: blocks }
}

function main() {
  let mainCss = fs.readFileSync(MAIN_CSS, 'utf8')
  ensureMainCssHasComponentsLayer(mainCss)

  const vueFiles = TARGET_DIRS.flatMap(walkVueFiles).sort()

  /** @type {{ file: string, blocks: string[] }[]} */
  const migrated = []

  for (const file of vueFiles) {
    const src = fs.readFileSync(file, 'utf8')
    if (!src.includes('<style scoped')) continue

    const { updatedVue, extractedCssBlocks } = stripStyleScopedBlocks(src)
    if (updatedVue !== src) {
      // Normalize excessive blank lines after removal
      const normalized = updatedVue
        .replace(/\n{4,}/g, '\n\n\n')
        .replace(/\n{3,}<\/template>/g, '\n\n</template>')
      fs.writeFileSync(file, normalized, 'utf8')
    }

    if (extractedCssBlocks.length) migrated.push({ file, blocks: extractedCssBlocks })
  }

  if (!migrated.length) {
    console.log('No <style scoped> blocks found to migrate.')
    return
  }

  let insertion = '\n\n'
  for (const m of migrated) {
    insertion += `  /* ===== ${rel(m.file)} (migrated from <style scoped>) ===== */\n`
    for (const block of m.blocks) {
      // Indent by two spaces to align within @layer components { ... }
      const indented = block
        .split('\n')
        .map((line) => `  ${line}`.replace(/\s+$/g, ''))
        .join('\n')
      insertion += `${indented}\n\n`
    }
  }

  // Prevent triple-empty at end
  insertion = insertion.replace(/\n{3,}$/g, '\n\n')

  mainCss = insertBeforeFinalClosingBrace(mainCss, insertion)
  fs.writeFileSync(MAIN_CSS, mainCss, 'utf8')

  console.log(`Migrated ${migrated.length} file(s) into ${rel(MAIN_CSS)}.`)
}

main()
