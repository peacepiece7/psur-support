export const useMounted = () => {
  const isMounted = ref(false)

  onMounted(() => {
    requestAnimationFrame(() => {
      isMounted.value = true
    })
  })

  return {
    isMounted,
  }
}
