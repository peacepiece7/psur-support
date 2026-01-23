<script setup lang="ts">
  const noteDialogOpen = ref(false)
</script>

<template>
  <div class="fixed top-1/2 right-4 z-1000 -translate-y-1/2">
    <v-btn
      class="font-bold transition-all duration-300 ease-in-out"
      style="
        box-shadow: 0 4px 12px
          color-mix(in srgb, var(--color-primary-700) 30%, transparent);
      "
      color="primary"
      variant="elevated"
      @click="noteDialogOpen = true"
    >
      NOTE
    </v-btn>

    <!-- NOTE 모달 -->
    <v-dialog v-model="noteDialogOpen" max-width="900" scrollable>
      <v-card class="">
        <v-card-title
          class="border-grey-300 text-grey-900 flex items-center justify-between border-b p-6 text-2xl font-bold"
        >
          <span>안 좋은 예제 참고</span>
          <v-btn
            variant="text"
            class="text-grey-600"
            @click="noteDialogOpen = false"
          >
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </v-card-title>

        <v-card-text class="flex flex-col gap-4 p-6">
          <div class="info-note-button__image-item">
            <p class="m-2.5">안 좋은 예제 1</p>
            <img
              src="/bad_practice_1.png"
              alt="안 좋은 예제 1"
              class="info-note-button__image"
            />
          </div>
          <div class="info-note-button__image-item">
            <p class="m-2.5">안 좋은 예제 2</p>
            <img
              src="/bad_practice_2.png"
              alt="안 좋은 예제 2"
              class="info-note-button__image"
            />
          </div>
          <p class="text-grey-600 my-2 mt-0 text-center text-sm italic">
            좌우 형태로하면 height 조절이 제한적
          </p>
        </v-card-text>

        <v-card-actions class="border-grey-300 border-t px-6 py-4">
          <v-spacer />
          <v-btn variant="text" @click="noteDialogOpen = false">닫기</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>
