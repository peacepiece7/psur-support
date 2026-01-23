<script setup lang="ts">
  import type { MenuItem } from '~/components/templates/GNB.vue'

  interface Props {
    menuItems: MenuItem[]
    visible: boolean
    isAtTop: boolean
  }

  const props = defineProps<Props>()

  // Desktop용 상태
  const hoveredMenuIndex = ref<number | null>(null)

  const selectedMenu = computed(() => {
    if (hoveredMenuIndex.value === null) return null
    return props.menuItems[hoveredMenuIndex.value]
  })

  // 하위 메뉴가 열려있는지 확인
  const isSubmenuOpen = computed(() => {
    return (
      selectedMenu.value !== null &&
      selectedMenu.value.children !== undefined &&
      selectedMenu.value.children.length > 0 &&
      hoveredMenuIndex.value !== null &&
      props.visible
    )
  })

  // GNB 배경색 결정 (우선순위: 하위 메뉴 열림 > 스크롤 위치)
  const shouldBeTransparent = computed(() => {
    // 하위 메뉴가 열려있으면 항상 초록색 (투명하지 않음)
    if (isSubmenuOpen.value) {
      return false
    }
    // 하위 메뉴가 닫혀있고 스크롤이 최상단이면 투명
    return props.isAtTop
  })

  const handleMouseEnter = (index: number) => {
    hoveredMenuIndex.value = index
  }

  const handleMouseLeave = () => {
    hoveredMenuIndex.value = null
  }

  const handleSubmenuClick = () => {
    hoveredMenuIndex.value = null
  }

  // 라우트 변경 감지
  const route = useRoute()

  watch(
    () => route.path,
    () => {
      hoveredMenuIndex.value = null
    },
  )

  // 로그인 상태 확인
  const authStore = useAuthStore()
  const isLoggedIn = computed(() => authStore.isLoggedIn)
  const userName = computed(() => authStore.user?.name || '')

  const goToMyPage = () => {
    navigateTo('/me')
  }

  // 로그인 다이얼로그 상태
  const isLoginDialogOpen = ref(false)

  const handleLoginLogout = () => {
    if (isLoggedIn.value) {
      authStore.logout()
    } else {
      isLoginDialogOpen.value = true
    }
  }

  const handleLoginSuccess = () => {
    isLoginDialogOpen.value = false
  }
</script>

<template>
  <div class="w-full">
    <v-app-bar
      :color="shouldBeTransparent ? 'transparent' : 'primary'"
      :elevation="shouldBeTransparent ? 0 : 2"
      class="relative w-full transition-[transform,background-color,box-shadow] duration-300 ease-in-out will-change-transform"
      :class="{
        'border-b border-white/30 !bg-transparent': shouldBeTransparent,
      }"
      app
      :style="{
        transform: visible ? 'translateY(0)' : 'translateY(-100%)',
      }"
    >
      <div
        class="mx-auto flex w-full max-w-[1400px] items-center overflow-visible p-0"
        :class="{
          '!bg-transparent': shouldBeTransparent,
        }"
      >
        <!-- 왼쪽: 로고 + 텍스트 + 메뉴 버튼들 -->
        <div class="flex items-center gap-4 pl-4">
          <!-- 로고 + 타이틀 (클릭 시 홈으로 이동) -->
          <NuxtLink
            :prefetch="false"
            to="/"
            class="flex cursor-pointer items-center no-underline transition-opacity duration-200 ease-in hover:opacity-80"
          >
            <v-icon class="shrink-0 text-white" size="40">mdi-trophy</v-icon>
          </NuxtLink>

          <!-- 메뉴 버튼들 -->
          <div class="relative ml-4 flex items-center gap-2">
            <div
              v-for="(item, index) in menuItems"
              :key="item.to"
              class="relative"
              @mouseenter="handleMouseEnter(index)"
            >
              <NuxtLink
                :prefetch="false"
                class="relative inline-block px-3 py-2 font-medium text-white! no-underline transition-colors duration-200 ease-in"
                :class="{ 'gnb-menu-link-active': hoveredMenuIndex === index }"
                :to="item.to"
              >
                {{ item.title }}
              </NuxtLink>
            </div>
          </div>
        </div>

        <v-spacer />

        <!-- 우측: 알람 아이콘, 사용자 정보(로그인 시), 로그인/로그아웃 -->
        <ClientOnly>
          <div class="flex items-center gap-2">
            <Button variant="outlined" color="inverse">
              <v-icon>mdi-bell-outline</v-icon>
            </Button>
            <template v-if="isLoggedIn">
              <span
                class="px-2 py-0 text-sm font-medium whitespace-nowrap text-white"
                >{{ userName }}</span
              >
              <Button variant="outlined" color="inverse" @click="goToMyPage">
                <v-icon>mdi-account-circle</v-icon>
              </Button>
            </template>
            <Button
              variant="outlined"
              color="inverse"
              @click="handleLoginLogout"
            >
              {{ isLoggedIn ? '로그아웃' : '로그인' }}
            </Button>
          </div>
        </ClientOnly>
      </div>
    </v-app-bar>

    <!-- 로그인 다이얼로그 -->
    <Dialog v-model="isLoginDialogOpen" :max-width="500">
      <template #content>
        <LoginSection @login-success="handleLoginSuccess" />
      </template>
    </Dialog>

    <!-- 하위 메뉴 영역 -->
    <div
      v-if="
        selectedMenu &&
        selectedMenu.children &&
        selectedMenu.children.length > 0 &&
        hoveredMenuIndex !== null &&
        visible
      "
      class="border-grey-200 z-app-bar fixed top-16 left-0 w-full translate-y-0 animate-[slideDown_200ms_ease-in-out] border-t bg-white pt-0 shadow-lg"
      @mouseenter="handleMouseEnter(hoveredMenuIndex)"
      @mouseleave="handleMouseLeave"
    >
      <div class="mx-auto max-w-[1400px] px-4 py-6">
        <div class="flex items-start gap-6">
          <div class="flex h-[250px] w-[250px] items-center justify-center">
            <NuxtImg
              src="/dali_hi.png"
              alt="달리 캐릭터"
              class="h-full w-full object-contain"
            />
          </div>
          <div class="flex-1">
            <div class="mb-4">
              <h3 class="heading-3">{{ selectedMenu.title }}</h3>
            </div>
            <div
              class="grid gap-2"
              style="
                grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
              "
            >
              <NuxtLink
                v-for="child in selectedMenu.children"
                :key="child.to"
                :prefetch="false"
                :to="child.to"
                class="text-grey-900 hover:bg-grey-100 block rounded-xl px-4 py-3 text-base no-underline transition-all duration-200 ease-in-out"
                :scroll="true"
                @click="handleSubmenuClick"
              >
                {{ child.title }}
              </NuxtLink>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
  /* v-app-bar와 v-app-bar__content의 transition 및 overflow 처리 */
  :deep(.v-app-bar) {
    transition:
      transform 0.3s ease-in-out,
      background-color 0.3s ease-in-out,
      box-shadow 0.3s ease-in-out !important;
  }

  :deep(.v-app-bar__content) {
    overflow: visible !important;
    transition:
      transform 0.3s ease-in-out,
      background-color 0.3s ease-in-out,
      box-shadow 0.3s ease-in-out !important;
  }
</style>
