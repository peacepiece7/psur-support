<script setup lang="ts">
  import type { MenuItem } from '~/components/templates/GNB.vue'

  interface Props {
    menuItems: MenuItem[]
    visible: boolean
    isAtTop: boolean
  }

  const props = defineProps<Props>()

  // Mobile용 drawer 상태
  const isDrawerOpen = ref(false)

  const toggleDrawer = () => {
    isDrawerOpen.value = !isDrawerOpen.value
  }

  const closeDrawer = () => {
    isDrawerOpen.value = false
    expandedMenuIndex.value = null
  }

  // 라우트 변경 감지
  const route = useRoute()
  watch(
    () => route.path,
    () => {
      isDrawerOpen.value = false
    },
  )

  // Mobile에서 확장된 메뉴 인덱스
  const expandedMenuIndex = ref<number | null>(null)

  const toggleMenuExpansion = (index: number) => {
    console.log('toggleMenuExpansion called', {
      current: expandedMenuIndex.value,
      clicked: index,
    })
    if (expandedMenuIndex.value === index) {
      expandedMenuIndex.value = null
    } else {
      expandedMenuIndex.value = index
    }
    console.log('expandedMenuIndex after:', expandedMenuIndex.value)
  }

  // GNB 배경색 결정 (우선순위: drawer 열림 > 스크롤 위치)
  const shouldBeTransparent = computed(() => {
    // drawer가 열려있으면 항상 초록색 (투명하지 않음)
    if (isDrawerOpen.value) {
      return false
    }
    // drawer가 닫혀있고 스크롤이 최상단이면 투명
    return props.isAtTop
  })

  // 로그인 상태 확인
  const authStore = useAuthStore()
  const isLoggedIn = computed(() => authStore.isLoggedIn)
  const userName = computed(() => authStore.user?.name || '')

  const goToMyPage = () => {
    navigateTo('/mypage')
    closeDrawer()
  }

  // 로그인 다이얼로그 상태
  const isLoginDialogOpen = ref(false)

  const handleLoginLogout = () => {
    if (isLoggedIn.value) {
      authStore.logout()
      closeDrawer()
    } else {
      isLoginDialogOpen.value = true
      closeDrawer()
    }
  }

  const handleLoginSuccess = () => {
    isLoginDialogOpen.value = false
  }

  // app-bar 표시 여부 결정 (drawer가 열려있으면 항상 표시)
  const shouldShowAppBar = computed(() => {
    return props.visible || isDrawerOpen.value
  })
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
      :style="{
        transform: shouldShowAppBar ? 'translateY(0)' : 'translateY(-100%)',
      }"
    >
      <v-container
        class="flex w-full max-w-full items-center justify-between px-4 py-0"
        :class="{
          '!bg-transparent': shouldBeTransparent,
        }"
      >
        <!-- 왼쪽 : 로고 -->
        <div class="flex items-center">
          <NuxtLink
            :prefetch="false"
            to="/"
            class="flex cursor-pointer items-center no-underline transition-opacity duration-200 ease-in hover:opacity-80"
            @click="closeDrawer"
          >
            <NuxtImg
              src="/icon.svg"
              alt="체육회 로고"
              width="40"
              height="40"
              class="shrink-0 object-contain"
            />
          </NuxtLink>
        </div>

        <!-- 오른쪽 : 사용자 아이콘(로그인 시), 로그인/로그아웃, 햄버거 메뉴 버튼 -->
        <div class="flex items-center gap-1">
          <template v-if="isLoggedIn">
            <Button variant="outlined" color="inverse" @click="goToMyPage">
              <v-icon>mdi-account-circle</v-icon>
            </Button>
          </template>
          <Button variant="outlined" color="inverse" @click="handleLoginLogout">
            {{ isLoggedIn ? '로그아웃' : '로그인' }}
          </Button>
          <Button variant="outlined" color="inverse" @click.stop="toggleDrawer">
            <v-icon>mdi-menu</v-icon>
          </Button>
        </div>
      </v-container>
    </v-app-bar>

    <!-- 로그인 다이얼로그 -->
    <Dialog v-model="isLoginDialogOpen" :max-width="500">
      <template #content>
        <LoginSection @login-success="handleLoginSuccess" />
      </template>
    </Dialog>

    <!-- Mobile Drawer -->
    <v-navigation-drawer
      v-model="isDrawerOpen"
      location="right"
      temporary
      teleport="body"
      class="gnb-mobile-drawer z-drawer"
    >
      <div class="flex h-full flex-col">
        <!-- Drawer 헤더 -->
        <div
          class="border-grey-200 flex items-center justify-between border-b p-4"
        >
          <h2 class="text-grey-900 m-0 text-2xl font-bold">메뉴</h2>
          <Button
            variant="outlined"
            color="primary"
            class="!text-grey-900"
            shape="square"
            @click="closeDrawer"
          >
            <v-icon>mdi-close</v-icon>
          </Button>
        </div>

        <!-- 메뉴 리스트 -->
        <div class="flex-1 overflow-y-auto px-0 py-2">
          <div
            v-for="(item, index) in menuItems"
            :key="item.to"
            class="border-grey-100 border-b"
          >
            <!-- 메인 메뉴 -->
            <div
              class="w-full"
              :class="{
                'gnb-drawer-menu-expanded': expandedMenuIndex === index,
              }"
            >
              <NuxtLink
                v-if="!item.children || item.children.length === 0"
                :prefetch="false"
                :to="item.to"
                class="text-grey-900 block p-4 text-base font-medium no-underline transition-[background-color,color] duration-200 ease-in-out"
                @click="closeDrawer"
              >
                {{ item.title }}
              </NuxtLink>
              <Button
                v-else
                color="primary"
                shape="square"
                class="w-full"
                @click="toggleMenuExpansion(index)"
              >
                <span>{{ item.title }}</span>
                <v-icon
                  :class="{
                    'gnb-drawer-menu-icon-rotated': expandedMenuIndex === index,
                  }"
                  class="transition-transform duration-200 ease-in-out"
                  :style="{
                    transform:
                      expandedMenuIndex === index
                        ? 'rotate(180deg)'
                        : 'rotate(0deg)',
                  }"
                >
                  mdi-chevron-down
                </v-icon>
              </Button>
            </div>

            <!-- 하위 메뉴 -->
            <div
              v-if="
                item.children &&
                item.children.length > 0 &&
                expandedMenuIndex === index
              "
              class="bg-grey-50 animate-[slideDown_200ms_ease-in-out] px-0 py-2"
            >
              <NuxtLink
                v-for="child in item.children"
                :key="child.to"
                :prefetch="false"
                :to="child.to"
                class="text-grey-600 block px-4 py-3 pl-8 text-sm no-underline transition-[background-color,color] duration-200 ease-in-out"
                @click="closeDrawer"
              >
                {{ child.title }}
              </NuxtLink>
            </div>
          </div>
        </div>
      </div>
    </v-navigation-drawer>
  </div>
</template>

<style scoped>
  /* v-app-bar와 v-app-bar__content의 transition 처리 */
  :deep(.v-app-bar) {
    transition:
      transform 0.3s ease-in-out,
      background-color 0.3s ease-in-out,
      box-shadow 0.3s ease-in-out !important;
  }

  :deep(.v-app-bar__content) {
    transition:
      transform 0.3s ease-in-out,
      background-color 0.3s ease-in-out,
      box-shadow 0.3s ease-in-out !important;
  }
</style>
