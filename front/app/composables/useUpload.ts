export const useUpload = () => {
  const uploadFile = async (file: File, endpoint: string) => {
    const formData = new FormData()
    formData.append('file', file)

    const response = await $fetch(endpoint, {
      method: 'POST',
      body: formData,
    })

    return response
  }

  return {
    uploadFile,
  }
}
