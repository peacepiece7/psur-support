<script setup lang="ts">
  import type { VDialog } from 'vuetify/components'

  type VDialogProps = VDialog['$props']

  export interface DialogProps {
    modelValue: boolean
    maxWidth?: VDialogProps['maxWidth']
    scrollable?: VDialogProps['scrollable']
    persistent?: VDialogProps['persistent']
    fullscreen?: VDialogProps['fullscreen']
  }

  const props = withDefaults(defineProps<DialogProps>(), {
    maxWidth: '800',
    scrollable: true,
    persistent: false,
    fullscreen: false,
  })

  const emit = defineEmits<{
    'update:modelValue': [boolean]
  }>()

  const isOpen = computed({
    get: () => props.modelValue,
    set: (value) => emit('update:modelValue', value),
  })

  const closeDialog = () => (isOpen.value = false)

  // body scroll lock
  watch(
    () => isOpen.value,
    (open) => {
      if (import.meta.client) {
        document.body.classList.toggle('dialog-open', open)
      }
    },
  )

  onBeforeUnmount(() => {
    if (import.meta.client) {
      document.body.classList.remove('dialog-open')
    }
  })

  const dialogProps = computed(() => {
    const {
      modelValue,
      maxWidth,
      scrollable,
      persistent,
      fullscreen,
      ...rest
    } = props
    return {
      maxWidth,
      scrollable,
      persistent,
      fullscreen,
      ...rest,
    }
  })
</script>

<template>
  <v-dialog v-model="isOpen" v-bind="dialogProps">
    <v-card class="dialog">
      <!-- Title -->
      <v-card-title
        v-if="$slots.title || $slots['close-icon']"
        class="border-grey-300 flex items-center justify-between border-b"
        style="
          display: flex;
          justify-content: space-between;
          align-items: center;
        "
      >
        <div class="dialog__title-content flex flex-1 items-center gap-2">
          <slot name="title" />
        </div>

        <div>
          <slot name="close-icon" :close="closeDialog">
            <!-- default close button -->
            <Button
              color="inverse"
              size="xs"
              variant="fill"
              @click="closeDialog"
            >
              <IcIcon icon="close" />
            </Button>
          </slot>
        </div>
      </v-card-title>

      <!-- Content -->
      <v-card-text
        class="gap-6 p-6"
        style="display: flex; flex-direction: column"
      >
        <slot name="content" />
      </v-card-text>

      <!-- Footer -->
      <v-card-actions
        v-if="$slots.footer"
        class="dialog__footer border-grey-200 border-t"
      >
        <slot name="footer" :close="closeDialog" />
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<style scoped>
  .dialog {
    .dialog__title-content {
      display: flex;
      flex-direction: column;
      gap: 0.5rem;
    }

    .dialog__info-item {
      display: flex;
      align-items: center;
      gap: 0.75rem;
    }

    .dialog__info-icon {
      font-size: 1.5rem;
      color: var(--color-grey-600);
    }

    .dialog__info-text {
      color: var(--color-grey-900);
      font-size: 1rem;
      padding: 0.25rem;
    }

    .dialog__section {
      display: flex;
      flex-direction: column;
      padding-top: 0.5rem;
      padding-bottom: 0.5rem;
      padding-left: 0;
      padding-right: 0;
    }

    .dialog__section-title {
      font-size: 1.5rem;
      font-weight: 700;
      color: var(--color-grey-900);
      margin: 0;
    }

    .dialog__text {
      color: var(--color-grey-600);
      font-size: 1rem;
      line-height: 1.8;
      margin: 0;
    }
  }

  .dialog-item {
    cursor: pointer;
  }
</style>
