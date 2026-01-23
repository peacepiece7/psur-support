<script setup lang="ts">
  const textareaRefs = ref<any[]>([
    'foo bar barz',
    '',
    'loem ipsum dolor sit amet consectetur adipisicing elit.\nQuisquam, quos.',
    'the quick brown fox jumps over the lazy dog',
    `Lorem ipsum dolor sit amet consectetur adipisicing elit.
    Quisquam, quos.
    
    Amet consectetur adipisicing elit.
    For example, this is a long text that should be wrapped.
    This is a long text that should be wrapped.
    `,
    `Lorem ipsum dolor sit amet consectetur adipisicing elit.
    Quisquam, quos.

    Amet consectetur adipisicing elit.
    For example, this is a long text that should be wrapped.
    This is a long text that should be wrapped.
    `,

    `Lorem ipsum dolor sit amet consectetur adipisicing elit.
    Quisquam, quos.

    Amet consectetur adipisicing elit.
    For example, this is a long text that should be wrapped.
    This is a long text that should be wrapped.
    `,

    `Lorem ipsum dolor sit amet consectetur adipisicing elit.
    Quisquam, quos.
    Amet consectetur adipisicing elit.
    For example, this is a long text that should be wrapped.
    This is a long text that should be wrapped.
    `,
  ])
</script>

<template>
  <section class="apply-reg-step">
    <h3 class="apply-reg-step__title">4단계. 클럽 정보 입력</h3>
    <p class="apply-reg-step__desc">(컨텐츠는 추후 단계별로 채웁니다)</p>

    <div class="mt-10" />
    <h3 class="m-0 mb-4 text-2xl font-bold">Textarea</h3>

    <div class="grid max-w-[720px] gap-4">
      <Textarea v-model="textareaRefs[1]" label="TEXTAREA SM" size="sm" />
      <Textarea v-model="textareaRefs[2]" label="TEXTAREA MD" size="md" />
      <Textarea v-model="textareaRefs[3]" label="TEXTAREA LG" size="lg" />
      <Textarea v-model="textareaRefs[4]" label="TEXTAREA XL" size="xl" />
      <Textarea v-model="textareaRefs[5]" label="TEXTAREA MAX" size="max" />

      <div class="flex">
        <div class="flex-1">
          <Textarea v-model="textareaRefs[6]" label="Readonly" readonly />
        </div>
        <div class="flex-1">
          <Textarea v-model="textareaRefs[7]" label="Disabled" disabled />
        </div>
      </div>

      <div class="flex">
        <div class="flex-1">
          <Textarea v-model="textareaRefs[8]" label="Rows 2" :rows="2" />
        </div>
        <div class="flex-1">
          <Textarea v-model="textareaRefs[9]" label="Rows 6" :rows="6" />
        </div>
      </div>

      <div class="flex">
        <div class="flex-1">
          <Textarea
            v-model="textareaRefs[10]"
            label="Auto Grow"
            :auto-grow="true"
            :rows="2"
          />
        </div>
        <div class="flex-1">
          <Textarea
            v-model="textareaRefs[11]"
            label="Max Length 100"
            max-length="100"
          />
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
  .apply-reg-step {
    background: var(--color-grey-50);
    border: 1px solid var(--color-grey-200);
    border-radius: 1.5rem;
    padding: 2.5rem;
    min-height: 420px;
  }

  .apply-reg-step__title {
    margin: 0 0 1rem 0;
    font-size: 1.5rem;
    font-weight: 700;
  }

  .apply-reg-step__desc {
    margin: 0;
    color: var(--color-grey-600);
  }
</style>
