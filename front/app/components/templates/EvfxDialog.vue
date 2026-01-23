<script setup lang="ts">
  import type { NewRegSprtClubSport } from '~/types/apis/registered-sports-club'

  type Props = {
    isOpen: boolean
    options: NewRegSprtClubSport[]
    /** 선택된 id 목록(부모 상태는 submit 전까지 변경되지 않음) */
    selected: string[]
  }

  const props = defineProps<Props>()

  const emit = defineEmits<{
    (e: 'update:isOpen', value: boolean): void
    (e: 'close'): void
    (e: 'submit', value: NewRegSprtClubSport[]): void
  }>()

  const isSubmitting = ref(false)
  const localSelected = ref<string[]>([])

  watch(
    () => props.isOpen,
    (open) => {
      if (open) localSelected.value = [...(props.selected ?? [])]
    },
    { immediate: true },
  )

  const dialog = computed({
    get: () => props.isOpen,
    set: (v: boolean) => {
      emit('update:isOpen', v)
      if (!v && !isSubmitting.value) emit('close')
      if (!v) isSubmitting.value = false
    },
  })

  const toggle = (id: string) => {
    const idx = localSelected.value.indexOf(id)
    if (idx >= 0) localSelected.value.splice(idx, 1)
    else localSelected.value.push(id)
  }

  const onCancel = () => {
    dialog.value = false
  }

  const onSubmit = () => {
    isSubmitting.value = true
    const selectedOptions = props.options.filter((o) =>
      localSelected.value.includes(o.id),
    )
    emit('submit', selectedOptions)
    dialog.value = false
  }
</script>

<template>
  <Dialog v-model="dialog" max-width="560">
    <template #title>운영종목 검색</template>

    <template #content>
      <v-list lines="two" density="compact">
        <v-list-item
          v-for="opt in props.options"
          :key="opt.id"
          class="dialog-item"
          @click="toggle(opt.id)"
        >
          <template #prepend>
            <v-checkbox-btn :model-value="localSelected.includes(opt.id)" />
          </template>

          <v-list-item-title>{{ opt.name }}</v-list-item-title>
          <v-list-item-subtitle>
            <template v-if="opt.category">{{ opt.category }}</template>
            <template v-if="opt.note">
              <template v-if="opt.category"> · </template>{{ opt.note }}
            </template>
          </v-list-item-subtitle>
        </v-list-item>
      </v-list>
    </template>

    <template #footer>
      <v-spacer />
      <Button color="surface" @click="onCancel">취소</Button>
      <Button color="primary" @click="onSubmit">선택 완료</Button>
    </template>
  </Dialog>
</template>
