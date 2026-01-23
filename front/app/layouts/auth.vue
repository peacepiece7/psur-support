<script setup lang="ts">
  const route = useRoute()

  const pageMeta = computed(() => {
    return {
      title: route.meta.title,
      description: route.meta.description,
    }
  })

  useHead(pageMeta)
</script>

<template>
  <v-app>
    <v-main>
      <v-container class="fill-height" fluid>
        <v-row align="center" justify="center">
          <v-col cols="12" sm="8" md="4">
            <slot />
          </v-col>
        </v-row>
      </v-container>
    </v-main>
  </v-app>
</template>
