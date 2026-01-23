import fs from 'node:fs'
import path from 'node:path'

const MAIN_CSS = '/workspace/app/assets/css/main.css'

const SPACING_KEYS = new Set([
  '0',
  '1',
  '2',
  '3',
  '4',
  '5',
  '6',
  '8',
  '10',
  '12',
  '16',
  '20',
  '24',
])

function tokenToKey(token) {
  if (token === '0') return '0'
  const m = token.match(/^var\(--spacing-(\d+)\)$/)
  if (!m) return null
  const key = m[1]
  return SPACING_KEYS.has(key) ? key : null
}

function replaceLine(line) {
  // Preserve indentation
  const indent = line.match(/^\s*/)?.[0] ?? ''
  const trimmed = line.trim()

  // Single-value shorthands
  const single = [
    { re: /^padding:\s*(var\(--spacing-\d+\)|0);$/, cls: (k) => `p-${k}` },
    { re: /^margin:\s*(var\(--spacing-\d+\)|0);$/, cls: (k) => `m-${k}` },
    { re: /^gap:\s*(var\(--spacing-\d+\)|0);$/, cls: (k) => `gap-${k}` },
    { re: /^padding-top:\s*(var\(--spacing-\d+\)|0);$/, cls: (k) => `pt-${k}` },
    { re: /^padding-right:\s*(var\(--spacing-\d+\)|0);$/, cls: (k) => `pr-${k}` },
    { re: /^padding-bottom:\s*(var\(--spacing-\d+\)|0);$/, cls: (k) => `pb-${k}` },
    { re: /^padding-left:\s*(var\(--spacing-\d+\)|0);$/, cls: (k) => `pl-${k}` },
    { re: /^margin-top:\s*(var\(--spacing-\d+\)|0);$/, cls: (k) => `mt-${k}` },
    { re: /^margin-right:\s*(var\(--spacing-\d+\)|0);$/, cls: (k) => `mr-${k}` },
    { re: /^margin-bottom:\s*(var\(--spacing-\d+\)|0);$/, cls: (k) => `mb-${k}` },
    { re: /^margin-left:\s*(var\(--spacing-\d+\)|0);$/, cls: (k) => `ml-${k}` },
    { re: /^row-gap:\s*(var\(--spacing-\d+\)|0);$/, cls: (k) => `gap-y-${k}` },
    { re: /^column-gap:\s*(var\(--spacing-\d+\)|0);$/, cls: (k) => `gap-x-${k}` },
  ]
  for (const s of single) {
    const m = trimmed.match(s.re)
    if (m) {
      const key = tokenToKey(m[1])
      if (!key) return null
      return [`${indent}@apply ${s.cls(key)};`]
    }
  }

  // Two-value shorthands: (vertical, horizontal)
  // Only handle 0 or var(--spacing-x)
  const twoValue = [
    {
      re: /^padding:\s*(var\(--spacing-\d+\)|0)\s+(var\(--spacing-\d+\)|0);$/,
      cls: (y, x) => `py-${y} px-${x}`,
    },
    {
      re: /^margin:\s*(var\(--spacing-\d+\)|0)\s+(var\(--spacing-\d+\)|0);$/,
      cls: (y, x) => `my-${y} mx-${x}`,
    },
  ]
  for (const t of twoValue) {
    const m = trimmed.match(t.re)
    if (m) {
      const y = tokenToKey(m[1])
      const x = tokenToKey(m[2])
      if (!y || !x) return null
      return [`${indent}@apply ${t.cls(y, x)};`]
    }
  }

  return null
}

function main() {
  const css = fs.readFileSync(MAIN_CSS, 'utf8')
  const start = css.indexOf('@layer components')
  if (start === -1) throw new Error('Could not find @layer components')

  // We only transform inside @layer components {...} (which ends at EOF here)
  const before = css.slice(0, start)
  const inside = css.slice(start)

  const lines = inside.split('\n')
  let changed = 0
  const out = []
  for (const line of lines) {
    const repl = replaceLine(line)
    if (repl) {
      out.push(...repl)
      changed += 1
    } else {
      out.push(line)
    }
  }

  const updated = before + out.join('\n')
  fs.writeFileSync(MAIN_CSS, updated, 'utf8')
  console.log(`Converted ${changed} spacing declarations to @apply in ${path.relative('/workspace', MAIN_CSS)}.`)
}

main()
