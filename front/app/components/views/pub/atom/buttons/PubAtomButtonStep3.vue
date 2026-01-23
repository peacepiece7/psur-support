<script setup lang="ts">
  // 아이콘 목록
  const icons = [
    'alram',
    'announcement',
    'at',
    'attachment',
    'blank',
    'calendar',
    'call',
    'caution',
    'caution-lined',
    'cautrion-filled',
    'check',
    'check-bold',
    'chevron-down',
    'chevron-down-bold',
    'chevron-left',
    'chevron-left-bold',
    'chevron-left-double',
    'chevron-left-double-bold',
    'chevron-right',
    'chevron-right-bold',
    'chevron-right-double',
    'chevron-right-double-bold',
    'chevron-up',
    'chevron-up-bold',
    'close',
    'close-bold',
    'close-circle-filled',
    'close-circle-lined',
    'download',
    'edit',
    'eye-off',
    'eye-on',
    'file',
    'graph',
    'home',
    'info-filled',
    'info-lined',
    'keyboard',
    'keyboard-bold',
    'link',
    'location',
    'login-v1',
    'login-v2',
    'mark',
    'mark-bold',
    'message-default',
    'message-error',
    'message-success',
    'minus',
    'minus-bold',
    'new',
    'parking',
    'pause',
    'pause-bold',
    'pin',
    'pin-bold',
    'play',
    'play-bold',
    'plus',
    'plus-bold',
    'question-filled',
    'question-lined',
    'refresh',
    'search',
    'setting',
    'setting-bold',
    'star-filled',
    'star-lined',
    'tilde',
    'timeout',
    'updown-down',
    'updown-up',
    'upload',
    'wifi',
  ] as const
</script>

<template>
  <section
    class="border-grey-300 bg-grey-50 min-h-[420px] rounded-3xl border p-10"
  >
    <h3 class="m-0 mb-4 text-2xl font-bold">Badge with Icons</h3>

    <div class="grid max-w-[720px] gap-6">
      <!-- Type: Tint -->
      <div>
        <p class="text-grey-700 mb-3 text-sm font-semibold">
          Type: Tint (Black)
        </p>
        <div class="flex flex-wrap items-center gap-2">
          <Badge
            v-for="icon in icons"
            :key="`tint-${icon}`"
            type="tint"
            color="black"
          >
            <IcIcon :icon="icon" />
            <span class="ml-1">{{ icon }}</span>
          </Badge>
        </div>
      </div>

      <!-- Type: Solid -->
      <div>
        <p class="text-grey-700 mb-3 text-sm font-semibold">
          Type: Solid (Black)
        </p>
        <div class="flex flex-wrap items-center gap-2">
          <Badge
            v-for="icon in icons"
            :key="`solid-${icon}`"
            type="solid"
            color="black"
          >
            <IcIcon :icon="icon" />
            <span class="ml-1">{{ icon }}</span>
          </Badge>
        </div>
      </div>
    </div>
  </section>
</template>
