<script setup lang="ts">
  import '~/assets/icon-font/icon-font.css'

  const route = useRoute()

  const pageMeta = computed(() => {
    return {
      title: route.meta.title,
      description: route.meta.description,
    }
  })

  onMounted(() => {
    console.log('default layout mounted')
  })

  useHead(pageMeta)
</script>

<template>
  <v-app>
    <GNB />
    <v-main>
      <slot />
    </v-main>
    <Footer />
  </v-app>
</template>

<style></style>
