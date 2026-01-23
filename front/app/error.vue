<script setup lang="ts">
  const props = defineProps<{
    error: {
      statusCode?: number
      statusMessage?: string
      message?: string
    }
  }>()

  const message = computed(() => String(props.error?.message || ''))
  const is404 = computed(
    () => props.error?.statusCode === 404 || message.value?.includes('404'),
  )

  function handleError() {
    return clearError({ redirect: '/' })
  }
</script>

<template>
  <NuxtLayout>
    <v-container class="fill-height">
      <v-row align="center" justify="center">
        <v-col cols="12" class="text-center">
          <h1 class="text-h3 mb-4">
            {{ is404 ? '페이지를 찾을 수 없습니다' : '오류가 발생했습니다' }}
          </h1>
          <p class="text-body-1 mb-4">요청하신 페이지를 찾을 수 없습니다.</p>
          <v-btn color="primary" @click="handleError">홈으로 돌아가기</v-btn>
        </v-col>
      </v-row>
    </v-container>
  </NuxtLayout>
</template>
