/**
 * 전화번호 형식 변환
 * @param phone 전화번호
 * @returns 형식 변환된 전화번호
 */
export const formatPhone = (phone: string): string => {
  const cleaned = phone.replace(/\D/g, '')

  if (cleaned.length === 11) {
    return cleaned.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3')
  } else if (cleaned.length === 10) {
    return cleaned.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3')
  }

  return phone
}

/**
 * 날짜 형식 변환
 * @param date 날짜
 * @param format 형식
 * @returns 형식 변환된 날짜
 */
export const formatDate = (
  date: Date | string,
  format: string = 'YYYY-MM-DD',
): string => {
  const d = typeof date === 'string' ? new Date(date) : date

  const year = d.getFullYear()
  const month = String(d.getMonth() + 1).padStart(2, '0')
  const day = String(d.getDate()).padStart(2, '0')
  const hours = String(d.getHours()).padStart(2, '0')
  const minutes = String(d.getMinutes()).padStart(2, '0')
  const seconds = String(d.getSeconds()).padStart(2, '0')

  return format
    .replace('YYYY', String(year))
    .replace('MM', month)
    .replace('DD', day)
    .replace('HH', hours)
    .replace('mm', minutes)
    .replace('ss', seconds)
}
