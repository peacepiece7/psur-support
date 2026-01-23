<script setup lang="ts">
  const sectionRef = ref<HTMLElement | null>(null)
  const isVisible = ref(false)

  useIntersectionObserver(sectionRef, (entry) => {
    if (entry.isIntersecting) isVisible.value = true
  })
</script>

<template>
  <section
    ref="sectionRef"
    class="bg-grey-100 px-4 py-12"
    style="
      position: relative;
      opacity: 0;
      transform: translateY(50px);
      transition:
        opacity 0.8s ease-out,
        transform 0.8s ease-out;
    "
    :class="{ 'info-reg-benefits-sec--visible': isVisible }"
  >
    <div class="" style="max-width: 1400px; margin: 0 auto">
      <div class="mb-8" style="text-align: center">
        <h2 class="text-grey-900 m-0 text-3xl font-bold">
          등록스포츠클럽 혜택
        </h2>
      </div>
      <div class="" style="max-width: 900px; margin: 0 auto">
        <div
          class="rounded-3xl p-8 shadow-lg"
          style="
            background: linear-gradient(
              135deg,
              color-mix(in srgb, var(--color-primary-700) 5%, transparent) 0%,
              color-mix(in srgb, var(--color-primary-500) 8%, transparent) 100%
            );
            border: 2px solid
              color-mix(in srgb, var(--color-primary-700) 20%, transparent);
            transition: box-shadow 0.3s ease-out;
          "
        >
          <div
            class="mb-6 gap-4"
            style="display: flex; align-items: center; flex-wrap: wrap"
          >
            <v-icon size="48" color="primary" class="" style="flex-shrink: 0">
              mdi-ticket-percent
            </v-icon>
            <h3 class="text-grey-900 m-0 text-2xl font-bold" style="flex: 1">
              공공체육시설 사용료 감면
            </h3>
          </div>
          <div class="gap-4" style="display: flex; flex-direction: column">
            <p class="text-grey-600 m-0 text-base" style="line-height: 1.8">
              「스포츠클럽법」제15조에 따라, 등록스포츠클럽이 지자체의
              공공체육시설을 사용하는 경우 사용료의 전부 또는 일부 감면 가능함
            </p>
            <p class="text-grey-600 m-0 text-base" style="line-height: 1.8">
              감면 범위는 지자체별 조례로 정하는 바에 따르며, 등록스포츠클럽에
              대해서는 사용료의 100분의 80 이하의 범위에서 감면할 수 있음
            </p>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
