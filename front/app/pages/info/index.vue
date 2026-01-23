<script setup lang="ts">
  definePageMeta({
    title: '스포츠클럽 정보',
    description: '스포츠클럽 정보 페이지',
  })
</script>

<template>
  <div class="relative min-h-screen w-full">
    <PageHeaderSection
      title="스포츠클럽이란"
      image-src="/swimming-bg.png"
      image-alt="스포츠클럽 헤더 이미지"
      type="infoPage"
    />
    <InfoAboutUsSection />
    <InfoNoteButton />
    <InfoRegistrationDesignationSection />
    <InfoComparisonSection />
    <InfoWhyNeededSection />
  </div>
</template>
