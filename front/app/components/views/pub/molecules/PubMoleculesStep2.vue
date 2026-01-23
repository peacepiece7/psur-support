<script setup lang="ts">
  import TextField from '~/components/atoms/TextField.vue'
  import Dialog from '~/components/molecules/Dialog.vue'

  const searchValue = ref('')
  const isDialogOpen = ref(false)

  const handleClickSearchIcon = () => {
    isDialogOpen.value = true
  }
</script>

<template>
  <div class="bg-grey-50 min-h-screen p-8">
    <div class="mx-auto max-w-2xl">
      <h1 class="text-grey-900 mb-8 text-3xl font-bold">Dialog 예제</h1>

      <section
        class="border-grey-300 min-h-[420px] rounded-3xl border bg-white p-10"
      >
        <h2 class="m-0 mb-6 text-2xl font-bold">TextField + Dialog</h2>

        <div class="grid max-w-[720px] gap-4">
          <TextField
            v-model="searchValue"
            label="검색"
            placeholder="검색어를 입력하세요"
            append-inner-icon="local:search"
            readonly
            @click:append-inner="handleClickSearchIcon"
          />
        </div>
      </section>

      <!-- Dialog -->
      <Dialog v-model="isDialogOpen">
        <template #title>
          <p class="text-neutral-text-text w-full text-start">유의사항 안내</p>
        </template>

        <template #content>
          <p class="text-neutral-text-text">
            지정서의 법인·단체명, 대표자 성명, 소재지, 사용 시설, 운영 종목을
            변경하기 위해서는 먼저 등록스포츠클럽 변경 신청을 완료해야 합니다.
          </p>

          <p class="text-neutral-text-text">
            등록스포츠클럽 변경 신청 후 등록증이 발급되어야 해당 정보를 기준으로
            검토 후 지정서 변경이 가능합니다.
          </p>

          <p class="text-neutral-text-caption">
            * 변경된 등록증 첨부는 필수이므로, 신청 전 반드시 준비해주세요.
          </p>
        </template>
        <template #footer>
          <div class="flex w-full items-center justify-center">
            <Button variant="fill" class="px-16!" @click="isDialogOpen = false">
              확인
            </Button>
          </div>
        </template>

        <!-- <template #footer> </template> -->
      </Dialog>

      <!-- Tooltip 예제 -->
      <section
        class="border-grey-300 mt-8 min-h-[420px] rounded-3xl border bg-white p-10"
      >
        <h2 class="m-0 mb-6 text-2xl font-bold">Tooltip</h2>

        <div class="grid max-w-[720px] gap-8">
          <!-- 기본 예제 (black variant, top location) -->
          <div class="flex items-center gap-4">
            <span class="text-grey-900 text-lg font-medium">기본 예제:</span>
            <Tooltip text="이것은 툴팁입니다." />
          </div>

          <!-- white variant 예제 -->
          <div class="flex items-center gap-4">
            <span class="text-grey-900 text-lg font-medium"
              >White variant:</span
            >
            <Tooltip text="흰색 배경의 툴팁입니다." variant="white" />
          </div>

          <!-- 위치별 예제 -->
          <div class="grid gap-6">
            <div class="flex items-center gap-4">
              <span class="text-grey-900 text-lg font-medium"
                >위치별 예제:</span
              >
            </div>

            <div class="flex flex-wrap items-center gap-8">
              <div class="flex items-center gap-2">
                <span class="text-grey-600 text-sm">top:</span>
                <Tooltip text="위쪽에 표시되는 툴팁입니다." location="top" />
              </div>

              <div class="flex items-center gap-2">
                <span class="text-grey-600 text-sm">bottom:</span>
                <Tooltip
                  text="아래쪽에 표시되는 툴팁입니다."
                  location="bottom"
                />
              </div>

              <div class="flex items-center gap-2">
                <span class="text-grey-600 text-sm">left:</span>
                <Tooltip text="왼쪽에 표시되는 툴팁입니다." location="left" />
              </div>

              <div class="flex items-center gap-2">
                <span class="text-grey-600 text-sm">right:</span>
                <Tooltip
                  text="오른쪽에 표시되는 툴팁입니다."
                  location="right"
                />
              </div>
            </div>
          </div>

          <!-- 커스텀 activator 예제 -->
          <div class="flex items-center gap-4">
            <span class="text-grey-900 text-lg font-medium"
              >커스텀 activator:</span
            >
            <Tooltip text="커스텀 아이콘을 사용한 툴팁입니다.">
              <template #activator>
                <span class="text-primary-700 cursor-pointer text-xl">?</span>
              </template>
            </Tooltip>
          </div>

          <!-- hover 예제 -->
          <div class="grid gap-6">
            <div class="flex items-center gap-4">
              <span class="text-grey-900 text-lg font-medium"
                >Hover 시 표시:</span
              >
            </div>

            <div class="flex flex-wrap items-center gap-8">
              <div class="flex items-center gap-2">
                <span class="text-grey-600 text-sm">hover 활성화:</span>
                <Tooltip
                  text="마우스를 올리면 표시되는 툴팁입니다."
                  :open-on-hover="true"
                />
              </div>

              <div class="flex items-center gap-2">
                <span class="text-grey-600 text-sm"
                  >hover 비활성화 (기본):</span
                >
                <Tooltip
                  text="클릭해야 표시되는 툴팁입니다. (기본 동작)"
                  :open-on-hover="false"
                />
              </div>
            </div>
          </div>

          <!-- initialOpen 예제 -->
          <div class="grid gap-6">
            <div class="flex items-center gap-4">
              <span class="text-grey-900 text-lg font-medium"
                >초기 표시 옵션:</span
              >
            </div>

            <div class="flex flex-wrap items-center gap-8">
              <div class="flex items-center gap-2">
                <span class="text-grey-600 text-sm"
                  >initialOpen=true (렌더 시 바로 표시):</span
                >
                <Tooltip
                  text="페이지 로드 시 바로 표시되는 툴팁입니다."
                  :initial-open="true"
                />
              </div>

              <div class="flex items-center gap-2">
                <span class="text-grey-600 text-sm"
                  >initialOpen=false (기본값, hover 시만 표시):</span
                >
                <Tooltip
                  text="마우스를 올려야 표시되는 툴팁입니다. (기본 동작)"
                  :initial-open="false"
                />
              </div>
            </div>
          </div>

          <!-- persistent 예제 -->
          <div class="grid gap-6">
            <div class="flex items-center gap-4">
              <span class="text-grey-900 text-lg font-medium"
                >Persistent 옵션:</span
              >
            </div>

            <div class="flex flex-wrap items-center gap-8">
              <div class="flex items-center gap-2">
                <span class="text-grey-600 text-sm"
                  >persistent=true (닫히지 않음):</span
                >
                <Tooltip
                  text="외부 클릭이나 hover 해제로 닫히지 않는 툴팁입니다."
                  :persistent="true"
                  :initial-open="true"
                />
              </div>

              <div class="flex items-center gap-2">
                <span class="text-grey-600 text-sm"
                  >persistent=false (기본값):</span
                >
                <Tooltip
                  text="외부 클릭이나 hover 해제로 닫힐 수 있는 툴팁입니다."
                  :persistent="false"
                  :initial-open="true"
                />
              </div>
            </div>
          </div>

          <!-- 조합 예제 -->
          <div class="grid gap-6">
            <div class="flex items-center gap-4">
              <span class="text-grey-900 text-lg font-medium"
                >옵션 조합 예제:</span
              >
            </div>

            <div class="grid gap-4">
              <div class="flex items-center gap-2">
                <span class="text-grey-600 text-sm"
                  >initialOpen + persistent 조합:</span
                >
                <Tooltip
                  text="페이지 로드 시 바로 표시되고, 닫히지 않는 툴팁입니다."
                  :initial-open="true"
                  :persistent="true"
                />
              </div>

              <div class="flex items-center gap-2">
                <span class="text-grey-600 text-sm"
                  >openOnHover + initialOpen 조합:</span
                >
                <Tooltip
                  text="hover로도 표시되고, 페이지 로드 시에도 바로 표시되는 툴팁입니다."
                  :open-on-hover="true"
                  :initial-open="true"
                />
              </div>

              <div class="flex items-center gap-2">
                <span class="text-grey-600 text-sm"
                  >모든 옵션 조합 (white variant):</span
                >
                <Tooltip
                  text="흰색 배경, hover 활성화, 초기 표시, persistent 활성화된 툴팁입니다."
                  variant="white"
                  :open-on-hover="true"
                  :initial-open="true"
                  :persistent="true"
                />
              </div>
            </div>
          </div>
        </div>
      </section>
    </div>
  </div>
</template>
