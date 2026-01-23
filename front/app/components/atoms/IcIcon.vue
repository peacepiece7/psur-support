<script setup lang="ts">
  import type { IconFont } from '~/types/icon-font'

  defineProps<{ icon: IconFont }>()
</script>

<template>
  <i class="icon-font ft-icon" :class="`ic-${icon}`"></i>
</template>
