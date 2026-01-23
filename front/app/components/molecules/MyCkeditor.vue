<script setup lang="ts">
  import { Ckeditor } from '@ckeditor/ckeditor5-vue'
  import translations from 'ckeditor5/translations/ko.js'
  import 'ckeditor5/ckeditor5.css'
  import MyEditorSkeleton from './MyEditorSkeleton.vue'

  interface Props {
    name: string
    modelValue?: string | null
    disabled?: boolean
  }

  const props = withDefaults(defineProps<Props>(), {
    modelValue: '',
  })

  const emits = defineEmits<{
    'update:modelValue': [p: string]
    blur: []
    input: [p: string]
  }>()

  const { value, errorMessage, handleChange, handleBlur, meta } =
    useField<string>(() => props.name ?? '', undefined, {
      validateOnValueUpdate: false,
    })

  defineOptions({
    inheritAttrs: false,
  })

  const attrs = useAttrs()

  const touched = ref<boolean>(meta.touched)

  watch(
    () => meta.touched,
    () => {
      touched.value = false
      setTimeout(() => (touched.value = meta.touched), 10)
    },
    { immediate: true },
  )

  // props.modelValue가 변경되면 useField의 value 동기화
  watch(
    () => props.modelValue,
    (newValue) => {
      if (newValue !== value.value) {
        handleChange(newValue || '', false)
      }
    },
    { immediate: true },
  )

  const isLayoutReady = ref(false)
  const editor = ref<any>(null)
  const config = ref<any>(null)

  onMounted(async () => {
    const {
      ClassicEditor,
      AccessibilityHelp,
      Autoformat,
      AutoImage,
      Autosave,
      BlockQuote,
      Bold,
      Essentials,
      FontBackgroundColor,
      FontColor,
      FontFamily,
      FontSize,
      GeneralHtmlSupport,
      Heading,
      ImageBlock,
      ImageInline,
      ImageInsert,
      ImageInsertViaUrl,
      ImageResize,
      ImageStyle,
      ImageToolbar,
      ImageUpload,
      Indent,
      IndentBlock,
      Italic,
      Link,
      LinkImage,
      List,
      ListProperties,
      MediaEmbed,
      Paragraph,
      PasteFromOffice,
      SelectAll,
      SimpleUploadAdapter,
      SourceEditing,
      Table,
      TableCaption,
      TableCellProperties,
      TableColumnResize,
      TableProperties,
      TableToolbar,
      TextTransformation,
      TodoList,
      Underline,
      Undo,
    } = await import('ckeditor5')
    editor.value = ClassicEditor

    config.value = {
      toolbar: {
        items: [
          'undo',
          'redo',
          '|',
          'sourceEditing',
          '|',
          'heading',
          '|',
          'fontSize',
          'fontFamily',
          'fontColor',
          'fontBackgroundColor',
          '|',
          'bold',
          'italic',
          'underline',
          '|',
          'link',
          'insertImage',
          'mediaEmbed',
          'insertTable',
          'blockQuote',
          '|',
          'bulletedList',
          'numberedList',
          'todoList',
          'outdent',
          'indent',
        ],
        shouldNotGroupWhenFull: false,
      },
      plugins: [
        AccessibilityHelp,
        Autoformat,
        AutoImage,
        Autosave,
        BlockQuote,
        Bold,
        Essentials,
        FontBackgroundColor,
        FontColor,
        FontFamily,
        FontSize,
        GeneralHtmlSupport,
        Heading,
        ImageBlock,
        ImageInline,
        ImageInsert,
        ImageInsertViaUrl,
        ImageResize,
        ImageStyle,
        ImageToolbar,
        ImageUpload,
        Indent,
        IndentBlock,
        Italic,
        Link,
        LinkImage,
        List,
        ListProperties,
        MediaEmbed,
        Paragraph,
        PasteFromOffice,
        SelectAll,
        SimpleUploadAdapter,
        SourceEditing,
        Table,
        TableCaption,
        TableCellProperties,
        TableColumnResize,
        TableProperties,
        TableToolbar,
        TextTransformation,
        TodoList,
        Underline,
        Undo,
        MyCustomUploadAdapterPlugin,
      ],
      fontFamily: {
        supportAllValues: true,
      },
      fontSize: {
        options: [10, 12, 14, 'default', 18, 20, 22],
        supportAllValues: true,
      },
      heading: {
        options: [
          {
            model: 'paragraph',
            title: 'Paragraph',
            class: 'ck-heading_paragraph',
          },
          {
            model: 'heading1',
            view: 'h1',
            title: 'Heading 1',
            class: 'ck-heading_heading1',
          },
          {
            model: 'heading2',
            view: 'h2',
            title: 'Heading 2',
            class: 'ck-heading_heading2',
          },
          {
            model: 'heading3',
            view: 'h3',
            title: 'Heading 3',
            class: 'ck-heading_heading3',
          },
          {
            model: 'heading4',
            view: 'h4',
            title: 'Heading 4',
            class: 'ck-heading_heading4',
          },
          {
            model: 'heading5',
            view: 'h5',
            title: 'Heading 5',
            class: 'ck-heading_heading5',
          },
          {
            model: 'heading6',
            view: 'h6',
            title: 'Heading 6',
            class: 'ck-heading_heading6',
          },
        ],
      },
      htmlSupport: {
        allow: [
          {
            name: /^.*$/,
            styles: true,
            attributes: true,
            classes: true,
          },
        ],
      },
      image: {
        toolbar: [
          'imageTextAlternative',
          '|',
          'imageStyle:inline',
          'imageStyle:wrapText',
          'imageStyle:breakText',
          '|',
          'resizeImage',
        ],
      },
      initialData: props.modelValue || value.value || '',
      language: 'ko',
      link: {
        addTargetToExternalLinks: true,
        defaultProtocol: 'https://',
        decorators: {
          toggleDownloadable: {
            mode: 'manual',
            label: 'Downloadable',
            attributes: {
              download: 'file',
            },
          },
        },
      },
      list: {
        properties: {
          styles: true,
          startIndex: true,
          reversed: true,
        },
      },
      placeholder: '내용을 입력하세요.',
      table: {
        contentToolbar: [
          'tableColumn',
          'tableRow',
          'mergeTableCells',
          'tableProperties',
          'tableCellProperties',
        ],
      },
      translations: [translations],
    }

    isLayoutReady.value = true
  })

  class UploadAdapter {
    loader: any
    xhr: any

    constructor(loader: any) {
      this.loader = loader
    }

    upload() {
      return this.loader.file.then(
        (file: any) =>
          new Promise((resolve, reject) => {
            this._initRequest()
            this._initListeners(resolve, reject)
            this._sendRequest(file)
          }),
      )
    }

    _initRequest() {
      const xhr = (this.xhr = new XMLHttpRequest())
      xhr.open('POST', '/api/common/ckeditor/upload', true)
      xhr.responseType = 'json'
    }

    _initListeners(resolve: any, reject: any) {
      const genericErrorText = '파일을 업로드 할 수 없습니다.'

      this.xhr.addEventListener('error', () => reject(genericErrorText))
      this.xhr.addEventListener('abort', () => reject())
      this.xhr.addEventListener('load', () => {
        const response = this.xhr.response

        if (!response || response.error) {
          return reject(response?.error?.message || genericErrorText)
        }

        resolve({ default: response.url })
      })
    }

    _sendRequest(file: any) {
      const data = new FormData()
      // key값은 서버에서 받을 때 사용할 key값
      data.append('upload', file)
      this.xhr.send(data)
    }
  }

  function MyCustomUploadAdapterPlugin(editor: any) {
    editor.plugins.get('FileRepository').createUploadAdapter = (
      loader: any,
    ) => {
      return new UploadAdapter(loader)
    }
  }
</script>

<template>
  <div class="main-container" :class="attrs.class">
    <div class="editor-container editor-container_classic-editor">
      <div class="editor-container__editor">
        <!-- Skeleton UI -->
        <MyEditorSkeleton v-if="!isLayoutReady || !editor" />
        <!-- Actual Editor -->
        <Ckeditor
          v-if="isLayoutReady && editor"
          :model-value="props.modelValue || value || ''"
          :editor="editor"
          :config="config"
          :disabled="props.disabled"
          :class="{
            valid: touched && meta.valid,
            invalid: touched && !meta.valid,
          }"
          @update:model-value="
            ($event) => {
              handleChange($event, false)
              emits('update:modelValue', $event)
            }
          "
          @blur="
            () => {
              // handleBlur(new Event('blur'), true)
              handleBlur(null, true)
              emits('blur')
            }
          "
          @input="emits('input', $event)"
        />
      </div>
    </div>
    <span
      v-if="props.name && errorMessage && touched"
      class="mt-2 block text-sm text-red-500"
    >
      {{ errorMessage }}
    </span>
  </div>
</template>
