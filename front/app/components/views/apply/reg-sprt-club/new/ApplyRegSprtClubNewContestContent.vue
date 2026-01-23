<script setup lang="ts">
  import dayjs from 'dayjs'

  export type ApplyRegSprtClubNewContestValues = {
    pbancTtl: string
    hostOgzNm?: string
    spvsOgzNm?: string

    potdBegnDttm?: string | Date
    potdEndDttm?: string | Date

    sbmsnBegnDttm?: string | Date
    sbmsnEndDttm?: string | Date

    /** 공모 개요/내용 (HTML string) */
    pbctOutlCtet?: string

    /** 작성 관련 문의 */
    chpsGdCtet?: string
    /** 담당자 이메일 */
    emailAddrGdCtet?: string
    /** 시스템 문의 */
    telnoGdCtet?: string
  }

  type Props = {
    values: ApplyRegSprtClubNewContestValues
  }

  const props = defineProps<Props>()

  const fmtDate = (d?: string | Date, format = 'YYYY년 MM월 DD일') => {
    if (!d) return ''
    const v = dayjs(d)
    return v.isValid() ? v.format(format) : ''
  }
</script>

<template>
  <section class="contest">
    <h3 class="text-grey-900 text-xl font-bold" style="margin: 0 0 1.5rem 0">
      공모 내용
    </h3>

    <div class="mb-6">
      <h4 class="text-grey-900 m-0 text-2xl leading-tight font-extrabold">
        {{ props.values.pbancTtl }}
      </h4>
    </div>

    <div class="mb-6">
      <ul class="m-0 gap-3 p-0" style="list-style: none; display: grid">
        <li v-if="props.values.hostOgzNm" class="contest__meta-item">
          <strong class="font-bold" style="color: var(--color-primary-700)"
            >주최</strong
          >
          <span
            class="text-grey-900 font-semibold"
            style="word-break: keep-all"
            >{{ props.values.hostOgzNm }}</span
          >
        </li>

        <li v-if="props.values.spvsOgzNm" class="contest__meta-item">
          <strong class="font-bold" style="color: var(--color-primary-700)"
            >주관</strong
          >
          <span
            class="text-grey-900 font-semibold"
            style="word-break: keep-all"
            >{{ props.values.spvsOgzNm }}</span
          >
        </li>

        <li
          v-if="props.values.potdBegnDttm || props.values.potdEndDttm"
          class="contest__meta-item"
        >
          <strong class="font-bold" style="color: var(--color-primary-700)"
            >평가기간</strong
          >
          <span
            class="text-grey-900 font-semibold"
            style="word-break: keep-all"
          >
            {{ fmtDate(props.values.potdBegnDttm) }}
            <template v-if="props.values.potdEndDttm">
              ~ {{ fmtDate(props.values.potdEndDttm) }}
            </template>
          </span>
        </li>

        <li
          v-if="props.values.sbmsnBegnDttm || props.values.sbmsnEndDttm"
          class="contest__meta-item"
        >
          <strong class="contest__meta-label">자료 제출기한</strong>
          <span class="contest__meta-value">
            {{
              fmtDate(props.values.sbmsnBegnDttm, 'YYYY년 MM월 DD일 HH시 mm분')
            }}
            <template v-if="props.values.sbmsnEndDttm">
              ~
              {{
                fmtDate(props.values.sbmsnEndDttm, 'YYYY년 MM월 DD일 HH시 mm분')
              }}
            </template>
          </span>
        </li>

        <li v-if="props.values.chpsGdCtet" class="contest__meta-item">
          <strong class="contest__meta-label">작성 관련 문의</strong>
          <span class="contest__meta-value">{{ props.values.chpsGdCtet }}</span>
        </li>

        <li v-if="props.values.emailAddrGdCtet" class="contest__meta-item">
          <strong class="contest__meta-label">담당자 이메일</strong>
          <span class="contest__meta-value">{{
            props.values.emailAddrGdCtet
          }}</span>
        </li>

        <li v-if="props.values.telnoGdCtet" class="contest__meta-item">
          <strong class="contest__meta-label">시스템 관련 문의</strong>
          <span class="contest__meta-value">{{
            props.values.telnoGdCtet
          }}</span>
        </li>
      </ul>
    </div>

    <div
      v-if="props.values.pbctOutlCtet"
      class="border-grey-200 text-grey-900 rounded-3xl border-t border-dashed p-6 pt-6 max-md:p-5"
      style="
        background: color-mix(
          in srgb,
          var(--color-primary-300) 8%,
          transparent
        );
      "
      v-html="props.values.pbctOutlCtet"
    />
  </section>
</template>

<style scoped>
  .contest {
    background: #ffffff;
    border: 1px solid
      color-mix(in srgb, var(--color-primary-300) 35%, transparent);
    border-radius: 1.5rem;
    padding: 2rem;
    box-shadow:
      0 12px 36px rgba(15, 23, 42, 0.12),
      0 2px 10px rgba(15, 23, 42, 0.06);
    position: relative;
    overflow: hidden;
  }

  /* 카드 상단 강조 바 */
  .contest::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 6px;
    background: linear-gradient(
      90deg,
      var(--color-primary-700) 0%,
      var(--color-primary-300) 55%,
      color-mix(in srgb, var(--color-primary-300) 25%, transparent) 100%
    );
  }

  .contest__meta-item {
    display: grid;
    grid-template-columns: 140px 1fr;
    gap: 0.75rem;
    align-items: start;
    padding: 0.5rem 0;
    border-bottom: 1px solid var(--color-grey-100);
  }

  .contest__meta-item:last-child {
    border-bottom: none;
  }

  @media (max-width: 768px) {
    .contest {
      padding: 1.5rem;
    }

    .contest__meta-item {
      grid-template-columns: 1fr;
    }
  }
</style>
