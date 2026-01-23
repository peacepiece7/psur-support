<script setup lang="ts">
  definePageMeta({
    title: '게시판',
    description: '게시판 페이지',
  })

  const title = ref('')
  const content = ref('')

  const handleSubmit = () => {
    if (!title.value.trim()) {
      alert('제목을 입력해주세요')
      return
    }
    if (!content.value.trim()) {
      alert('내용을 입력해주세요')
      return
    }

    // TODO: 게시물 작성 API 호출
    alert('게시물이 작성되었습니다.')
  }
</script>

<template>
  <div class="relative min-h-screen w-full">
    <PageHeaderSection
      type="infoPage"
      title="게시판"
      image-src="/soccer-boy.png"
      image-alt="게시판 헤더 이미지"
    />
    <v-container class="mx-auto max-w-[1200px] px-4 py-12 max-md:py-8">
      <v-row>
        <v-col cols="12">
          <div
            class="rounded-lg bg-white p-8 shadow-sm max-md:px-4 max-md:py-6"
          >
            <h2 class="text-grey-900 mb-6 text-xl font-bold">
              게시물 작성
            </h2>
            <div class="w-full">
              <v-text-field
                v-model="title"
                label="제목"
                placeholder="제목을 입력하세요"
                variant="outlined"
                required
                class="pp-v-field mb-4"
              />
              <div class="mb-6 min-h-[400px]">
                <EditorRenderder
                  v-model:model-value="content"
                  name="board-content"
                  renderer="editor"
                />
              </div>
              <div class="mt-6 flex justify-end gap-3 max-md:flex-col-reverse">
                <Button color="secondary" @click="$router.back()">
                  취소
                </Button>
                <Button color="primary" @click="handleSubmit">
                  작성하기
                </Button>
              </div>
            </div>
          </div>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>
