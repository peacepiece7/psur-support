<script setup lang="ts">
  import { GNB_TRIGGER_HEIGHT } from '~/constants/ux'

  // 메뉴 아이템 타입 정의
  export type MenuItem = {
    title: string
    to: string
    children?: Array<{ title: string; to: string }>
  }

  // 로그인 상태 확인 (로그인 시 메뉴 추가)
  const authStore = useAuthStore()

  // 디바이스 감지
  const { isDesktop } = useDevice()

  // 스크롤 추적
  const { scrollY, scrollDirection } = useTracker()

  const { isMounted } = useMounted()

  const isLoggedIn = computed(() => {
    return authStore.isLoggedIn
  })

  // GNB 기본 메뉴 아이템 정의
  const baseMenuItems: MenuItem[] = [
    {
      title: 'Pub Playground',
      to: '/pub',
      children: [
        { title: 'atom(inputs)', to: '/pub/atom/inputs' },
        { title: 'atom(buttons)', to: '/pub/atom/buttons' },
        { title: 'molecules', to: '/pub/molecules' },
        { title: 'organisms', to: '/pub/organisms' },
      ],
    },
    // {
    //   title: '스포츠클럽 정보',
    //   to: '/info',
    //   children: [
    //     { title: '등록 스포츠클럽', to: '/info/registered-sports-club' },
    //   ],
    // },

    // {
    //   title: '스포츠클럽 커뮤니티',
    //   to: '/community',
    //   children: [{ title: '게시판', to: '/community/board' }],
    // },
  ]

  // 로그인 시 마이페이지 메뉴 추가
  const myPageMenuItem: MenuItem = {
    title: '마이페이지',
    to: '/me',
  }

  const menuItems = computed<MenuItem[]>(() => {
    const items: MenuItem[] = [...baseMenuItems]

    // 로그인 시 마이페이지 메뉴 추가
    if (isLoggedIn.value) {
      items.push(myPageMenuItem)
    }

    if (isLoggedIn.value) {
      // "스포츠클럽 정보" 뒤에 "신청" 메뉴 삽입
      const insertIndex = items.findIndex((i) => i.to === '/info')
      const applyMenu: MenuItem = {
        title: '스포츠클럽 신청',
        to: 'registered-sports-club/apply',
        children: [
          {
            title: '등록 스포츠클럽 신청',
            to: '/registered-sports-club/apply',
          },
          {
            title: '등록스포츠클럽 신청 현황',
            to: '/registered-sports-club/list',
          },
        ],
      }

      if (insertIndex >= 0) {
        items.splice(insertIndex + 1, 0, applyMenu)
      } else {
        items.push(applyMenu)
      }

      // 관리 메뉴 추가
      const managementMenu: MenuItem = {
        title: '관리',
        to: '/management',
        children: [
          {
            title: '공통코드 관리',
            to: '/management/common-code',
          },
          {
            title: '공통코드리스트 관리',
            to: '/management/common-code-list',
          },
          {
            title: '권한 관리',
            to: '/management/user-role',
          },
        ],
      }
      items.push(managementMenu)
    }

    return items
  })

  // GNB 표시 여부 계산
  const isVisible = computed(() => {
    if (!isMounted.value) return true
    if (scrollY.value < GNB_TRIGGER_HEIGHT) return true
    if (scrollDirection.value === null) return true
    return scrollDirection.value === 'up'
  })

  // 스크롤이 최상단인지 확인
  const isAtTop = computed(() => {
    if (!isMounted.value) return true
    return scrollY.value === 0
  })
</script>

<template>
  <div
    class="fixed top-0 left-0 z-1000 w-full translate-y-0 transition-transform duration-200 ease-in-out"
    :class="{ '-translate-y-full': !isVisible }"
  >
    <GNBForDesktop
      v-if="isDesktop"
      :menu-items="menuItems"
      :visible="isVisible"
      :is-at-top="isAtTop"
    />
    <GNBForMobile
      v-else
      :menu-items="menuItems"
      :visible="isVisible"
      :is-at-top="isAtTop"
    />
  </div>
</template>
