<script setup lang="ts">
  definePageMeta({
    title: '등록 스포츠클럽',
    description: '등록 스포츠클럽 안내 페이지',
  })
</script>

<template>
  <div class="relative w-full min-h-screen">
    <PageHeaderSection
      title="등록 스포츠클럽"
      image-src="/baskitbal-girl-2.png"
      image-alt="등록 스포츠클럽 헤더 이미지"
      type="infoPage"
    >
      <template #subtitle>
        <p>정기적으로 활동하는 체육단체라면 등록 스포츠클럽을 신청해보세요</p>
        <p>스포츠클럽 등록시 다양한 혜택을 받을 수 있습니다</p>
      </template>
    </PageHeaderSection>
    <RegSprtClubAboutUsSection />
    <RegSprtClubProcessSection />
    <RegSprtClubRequirementsSection />
    <RegSprtClubBenefitsSection />
  </div>
</template>
