export const useApi = () => {
  const fetchData = async <T>(
    url: string,
    options?: Parameters<typeof $fetch>[1],
  ): Promise<T> => {
    try {
      const response = await $fetch<T>(url, {
        ...options,
        headers: {
          'Content-Type': 'application/json',
          ...(options as any)?.headers,
        },
      })
      return response
    } catch (error) {
      console.error('API Error:', error)
      throw error
    }
  }

  const postData = async <T>(
    url: string,
    data: any,
    options?: Parameters<typeof $fetch>[1],
  ): Promise<T> => {
    return fetchData<T>(url, {
      method: 'POST',
      body: data,
      ...options,
    })
  }

  const putData = async <T>(
    url: string,
    data: any,
    options?: Parameters<typeof $fetch>[1],
  ): Promise<T> => {
    return fetchData<T>(url, {
      method: 'PUT',
      body: data,
      ...options,
    })
  }

  const deleteData = async <T>(
    url: string,
    options?: Parameters<typeof $fetch>[1],
  ): Promise<T> => {
    return fetchData<T>(url, {
      method: 'DELETE',
      ...options,
    })
  }

  return {
    fetchData,
    postData,
    putData,
    deleteData,
  }
}
