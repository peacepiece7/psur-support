<script setup lang="ts">
  const readonly = ref(false)
  const MAX_FILE_SIZE = 20

  const attachDocDto: any = {
    atchDocMthCtet:
      '<p><span style="font-size:14px;">입력된 내용 확인 후 인쇄 후 직인 날인 하여 스캔하여 PDF 첨부하세요.</span></p>',
    accept: ['*'],
    fileFormat: [],
    formTpCd: '20',
  }

  const apmtFileObj = ref(useFileSave(() => ''))

  const fileUpload = useFileUpload()

  const downloadFile = async (file: any) => {
    if (file.fileUuid) {
      await fileUpload.fileDownload({
        fileUuid: file.fileUuid,
        sortSeqNo: file.sortSeqNo,
      })
    }
  }
</script>

<template>
  <div class="bg-grey-50 min-h-screen p-8">
    <div class="mx-auto max-w-[1400px]">
      <h1 class="text-grey-900 mb-8 text-3xl font-bold">FileUpload 예제</h1>

      <section class="border-grey-300 mt-8 rounded-3xl border bg-white p-10">
        <h2 class="text-grey-900 mb-6 text-2xl font-bold">File Upload Multi</h2>

        <FileUploadContent
          v-if="!readonly"
          :attach-doc-list-dto="attachDocDto"
        />

        <FormFileUpload
          v-model="apmtFileObj.fileValue"
          size="max"
          name="apmtFileObj"
          :total-size="MAX_FILE_SIZE"
          :attach-doc-list-dto="attachDocDto"
          :readonly-and-download="readonly"
          :multiple="true"
          @download="downloadFile"
        />
      </section>

      <section class="border-grey-300 mt-8 rounded-3xl border bg-white p-10">
        <h2 class="text-grey-900 mb-6 text-2xl font-bold">
          File Upload Single
        </h2>

        <FileUploadContent
          v-if="!readonly"
          :attach-doc-list-dto="attachDocDto"
        />

        <FormFileUpload
          v-model="apmtFileObj.fileValue"
          size="max"
          name="apmtFileObj"
          :total-size="MAX_FILE_SIZE"
          :attach-doc-list-dto="attachDocDto"
          :readonly-and-download="readonly"
          @download="downloadFile"
        />
      </section>

      <!-- <v-tooltip
        text="Tooltip content"
        content-class="bg-white text-red-500"
        persistent
        :model-value="true"
        @update:model-value="() => true"
      >
        <template #activator="{ props }">
          <v-btn v-bind="props">Hover Over Me</v-btn>
        </template>
      </v-tooltip> -->
    </div>
  </div>
</template>

<!-- .v-tooltip > .v-overlay__content -->

<style scoped lang="scss"></style>
