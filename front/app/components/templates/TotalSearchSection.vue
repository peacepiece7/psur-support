<script setup lang="ts">
  const searchQuery = ref('')

  const recommendedKeywords = ['스포츠', '마포', '수영']

  const handleSearch = () => {
    // 검색 로직
    console.log('Search:', searchQuery.value)
  }

  const handleKeywordClick = (keyword: string) => {
    searchQuery.value = keyword
    handleSearch()
  }
</script>

<template>
  <v-card
    class="border-grey-200 w-full min-w-[340px] border-2 bg-white shadow-[var(--shadow-base)] transition-shadow hover:shadow-[var(--shadow-md)]"
    elevation="2"
  >
    <v-card-title
      class="border-grey-300 border-b-2 bg-white px-6 py-4 text-lg font-bold"
    >
      통합검색
    </v-card-title>

    <v-card-text class="flex flex-col gap-4 bg-white p-6">
      <v-text-field
        v-model="searchQuery"
        label="검색어를 입력하세요"
        variant="outlined"
        class="pp-v-field text-field-size-lg"
        @keyup.enter="handleSearch"
      >
        <template #append-inner>
          <v-icon
            class="hover:text-new-blue-700 cursor-pointer text-[1.5rem] transition-colors"
            @click="handleSearch"
            >local:search</v-icon
          >
        </template>
      </v-text-field>

      <div class="flex flex-wrap items-center gap-2 text-sm">
        <span class="text-grey-600 whitespace-nowrap">추천검색어:</span>
        <div class="flex flex-wrap gap-2">
          <button
            v-for="keyword in recommendedKeywords"
            :key="keyword"
            class="border-grey-300 bg-grey-100 text-grey-900 hover:border-new-blue-700 hover:bg-new-blue-700 rounded-sm border-2 px-2 py-1 text-sm font-medium shadow-[var(--shadow-xs)] transition-[background-color,border-color,color,box-shadow,transform] duration-200 ease-in-out hover:-translate-y-px hover:text-white hover:shadow-[var(--shadow-sm)] active:translate-y-0 active:shadow-[var(--shadow-xs)]"
            @click="handleKeywordClick(keyword)"
          >
            #{{ keyword }}
          </button>
        </div>
      </div>
    </v-card-text>
  </v-card>
</template>
