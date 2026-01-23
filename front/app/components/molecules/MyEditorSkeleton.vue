<script setup lang="ts">
  // Skeleton UI for CKEditor loading state using Vuetify
</script>

<template>
  <div
    class="border border-grey-300"
    style="
      border-radius: 4px;
      background: #ffffff;
      min-height: 450px;
      position: relative;
      overflow: hidden;
    "
  >
    <!-- Toolbar Skeleton -->
    <div
      class="border-b border-grey-300"
      style="
        display: flex;
        align-items: center;
        background: #ffffff;
        flex-wrap: wrap;
      "
    >
      <template v-for="i in 7" :key="i">
        <v-skeleton-loader
          type="button"
          width="5rem"
          height="5rem"
          class=""
          style="flex-shrink: 0"
        />
      </template>
    </div>

    <!-- Content Skeleton -->
    <div
      class="p-4"
      style="
        display: flex;
        flex-direction: column;
        gap: 12px;
        background: #ffffff;
      "
    >
      <v-skeleton-loader type="text" width="100%" height="2rem" />
      <v-skeleton-loader type="text" width="100%" height="2rem" />
      <v-skeleton-loader type="text" width="60%" height="2rem" />
      <v-skeleton-loader type="text" width="100%" height="2rem" />
      <v-skeleton-loader type="text" width="100%" height="2rem" />
      <v-skeleton-loader type="text" width="60%" height="2rem" />
    </div>

    <!-- Loading Message -->
    <div
      class="text-grey-900 font-medium"
      style="
        position: absolute;
        bottom: 16px;
        left: 50%;
        transform: translateX(-50%);
        display: flex;
        align-items: center;
        gap: 8px;
      "
    >
      <v-progress-circular
        indeterminate
        size="16"
        width="2"
        color="grey"
        class="mr-2"
      />
      <span>에디터를 불러오는 중입니다...</span>
    </div>
  </div>
</template>
