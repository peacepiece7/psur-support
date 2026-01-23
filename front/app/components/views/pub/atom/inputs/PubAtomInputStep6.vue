<script setup lang="ts">
  // Divider 예제는 별도의 state가 필요 없음
</script>

<template>
  <section class="border-grey-300 min-h-[420px] rounded-3xl border p-10">
    <h3 class="m-0 mb-4 text-2xl font-bold">Divider</h3>

    <div class="grid max-w-[720px] gap-6">
      <!-- Variant 예제 -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Variant</h4>
        <div class="grid gap-4">
          <div>
            <p class="text-grey-600 mb-2 text-sm">
              dotted (기본값, stroke-300, 2px)
            </p>
            <Divier variant="dotted" />
          </div>
          <div>
            <p class="text-grey-600 mb-2 text-sm">
              solidGrey (stroke-200, 1px)
            </p>
            <Divier variant="solidGrey" />
          </div>
          <div>
            <p class="text-grey-600 mb-2 text-sm">solidBlack (grey-900, 2px)</p>
            <Divier variant="solidBlack" />
          </div>
          <div>
            <p class="text-grey-600 mb-2 text-sm">
              solidFat (stroke-300, 10px)
            </p>
            <Divier variant="solidFat" />
          </div>
        </div>
      </div>

      <!-- Orientation 예제 -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Orientation</h4>
        <div class="grid gap-4">
          <div>
            <p class="text-grey-600 mb-2 text-sm">Horizontal (기본값)</p>
            <div class="w-full">
              <Divier variant="dotted" />
            </div>
          </div>
          <div>
            <p class="text-grey-600 mb-2 text-sm">Vertical</p>
            <div class="flex h-32 items-center">
              <Divier variant="dotted" orientation="vertical" />
            </div>
          </div>
        </div>
      </div>

      <!-- Color 예제 -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">Color (4가지 색상)</h4>
        <div class="grid gap-4">
          <div>
            <p class="text-grey-600 mb-2 text-sm">stroke-200</p>
            <Divier variant="solidGrey" color="stroke-200" />
          </div>
          <div>
            <p class="text-grey-600 mb-2 text-sm">stroke-300</p>
            <Divier variant="dotted" color="stroke-300" />
          </div>
          <div>
            <p class="text-grey-600 mb-2 text-sm">grey-50</p>
            <Divier variant="solidGrey" color="grey-50" />
          </div>
          <div>
            <p class="text-grey-600 mb-2 text-sm">grey-900</p>
            <Divier variant="solidBlack" color="grey-900" />
          </div>
        </div>
      </div>

      <!-- 커스터마이징 예제 -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">
          커스터마이징 (Color Override)
        </h4>
        <div class="grid gap-4">
          <div>
            <p class="text-grey-600 mb-2 text-sm">
              dotted variant의 기본 색상(stroke-300)을 grey-900로 변경
            </p>
            <Divier variant="dotted" color="grey-900" />
          </div>
          <div>
            <p class="text-grey-600 mb-2 text-sm">
              solidGrey variant의 기본 색상(stroke-200)을 grey-900로 변경
            </p>
            <Divier variant="solidGrey" color="grey-900" />
          </div>
          <div>
            <p class="text-grey-600 mb-2 text-sm">
              solidBlack variant의 기본 색상(grey-900)을 stroke-200로 변경
            </p>
            <Divier variant="solidBlack" color="stroke-200" />
          </div>
          <div>
            <p class="text-grey-600 mb-2 text-sm">
              solidFat variant의 기본 색상(stroke-300)을 grey-50로 변경
            </p>
            <Divier variant="solidFat" color="grey-50" />
          </div>
        </div>
      </div>

      <!-- Height 커스터마이징 예제 -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">
          커스터마이징 (Height Override)
        </h4>
        <div class="grid gap-4">
          <div>
            <p class="text-grey-600 mb-2 text-sm">
              dotted variant의 기본 height(2px)를 4px로 변경
            </p>
            <Divier variant="dotted" :height="4" />
          </div>
          <div>
            <p class="text-grey-600 mb-2 text-sm">
              solidGrey variant의 기본 height(1px)를 3px로 변경
            </p>
            <Divier variant="solidGrey" :height="3" />
          </div>
          <div>
            <p class="text-grey-600 mb-2 text-sm">
              solidBlack variant의 기본 height(2px)를 1px로 변경
            </p>
            <Divier variant="solidBlack" :height="1" />
          </div>
          <div>
            <p class="text-grey-600 mb-2 text-sm">
              solidFat variant의 기본 height(10px)를 5px로 변경
            </p>
            <Divier variant="solidFat" :height="5" />
          </div>
        </div>
      </div>

      <!-- Color + Height 동시 커스터마이징 예제 -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">
          커스터마이징 (Color + Height 동시 Override)
        </h4>
        <div class="grid gap-4">
          <div>
            <p class="text-grey-600 mb-2 text-sm">
              dotted, color: grey-900, height: 5px
            </p>
            <Divier variant="dotted" color="grey-900" :height="5" />
          </div>
          <div>
            <p class="text-grey-600 mb-2 text-sm">
              solidGrey, color: stroke-300, height: 2px
            </p>
            <Divier variant="solidGrey" color="stroke-300" :height="2" />
          </div>
          <div>
            <p class="text-grey-600 mb-2 text-sm">
              solidBlack, color: grey-50, height: 4px
            </p>
            <Divier variant="solidBlack" color="grey-50" :height="4" />
          </div>
          <div>
            <p class="text-grey-600 mb-2 text-sm">
              solidFat, color: grey-900, height: 3px
            </p>
            <Divier variant="solidFat" color="grey-900" :height="3" />
          </div>
        </div>
      </div>

      <!-- 조합 예제 -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">조합 예제</h4>
        <div class="grid gap-4">
          <div>
            <p class="text-grey-600 mb-2 text-sm">
              Horizontal Dotted, Color: grey-900, Height: 3px
            </p>
            <Divier
              variant="dotted"
              orientation="horizontal"
              color="grey-900"
              :height="3"
            />
          </div>
          <div>
            <p class="text-grey-600 mb-2 text-sm">
              Vertical SolidGrey, Color: stroke-300, Height: 2px
            </p>
            <div class="flex h-32 items-center">
              <Divier
                variant="solidGrey"
                orientation="vertical"
                color="stroke-300"
                :height="2"
              />
            </div>
          </div>
        </div>
      </div>

      <!-- 실제 사용 예제 -->
      <div>
        <h4 class="mb-3 text-lg font-semibold">사용 예제</h4>
        <div class="grid gap-4">
          <div>
            <p class="text-grey-900 mb-2 text-sm">섹션 구분 (dotted)</p>
            <div class="space-y-4">
              <div>
                <p>섹션 1 내용</p>
              </div>
              <Divier variant="dotted" />
              <div>
                <p>섹션 2 내용</p>
              </div>
            </div>
          </div>
          <div>
            <p class="text-grey-900 mb-2 text-sm">
              섹션 구분 (solidBlack, 굵게)
            </p>
            <div class="space-y-4">
              <div>
                <p>섹션 1 내용</p>
              </div>
              <Divier variant="solidBlack" />
              <div>
                <p>섹션 2 내용</p>
              </div>
            </div>
          </div>
          <div>
            <p class="text-grey-900 mb-2 text-sm">리스트 아이템 구분</p>
            <div class="flex items-center gap-4">
              <span>아이템 1</span>
              <Divier variant="dotted" orientation="vertical" :height="2" />
              <span>아이템 2</span>
              <Divier variant="dotted" orientation="vertical" :height="2" />
              <span>아이템 3</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>

<style scoped>
  /* Divider 예제 전용 스타일 */
</style>
