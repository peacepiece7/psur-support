<script setup lang="ts">
  import type { PhoneNumberValue } from '~/components/molecules/PhoneNumberInput.vue'
  import type { IDNumberValue } from '~/components/molecules/IDNumberInput.vue'
  import PhoneNumberInput from '~/components/molecules/PhoneNumberInput.vue'
  import IDNumberInput from '~/components/molecules/IDNumberInput.vue'
  import FormField from '~/components/molecules/FormField.vue'
  import { useForm } from 'vee-validate'
  import { toTypedSchema } from '@vee-validate/yup'
  import * as yup from 'yup'

  // PhoneNumberInput 상태 (객체 형태)
  const phoneNumber1 = ref<PhoneNumberValue | null>(null)
  const phoneNumber2 = ref<PhoneNumberValue | null>({
    part1: '010',
    part2: '1234',
    part3: '5678',
  })
  const phoneNumber3 = ref<PhoneNumberValue | null>(null)
  const phoneNumber4 = ref<PhoneNumberValue | null>({
    part1: '010',
    part2: '1234',
    part3: '5678',
  })
  const phoneNumberError = ref<PhoneNumberValue | null>(null)
  const phoneError = ref(false)

  // 다양한 전화번호 형식 예제
  const phoneMobile = ref<PhoneNumberValue | null>(null) // 모바일 3-4-4
  const phoneLocal = ref<PhoneNumberValue | null>(null) // 지역전화 2-3-4
  const phoneInternet = ref<PhoneNumberValue | null>(null) // 인터넷전화 4-4-4

  // 서버에서 내려온 데이터를 객체로 변환하는 예제
  const serverPhoneData = '01012345678'
  const phoneFromServer = computed(() => {
    if (!serverPhoneData) return null
    return {
      part1: serverPhoneData.slice(0, 3),
      part2: serverPhoneData.slice(3, 7),
      part3: serverPhoneData.slice(7, 11),
    }
  })

  // FormField와 함께 사용하는 예제
  const formSchema = yup.object({
    phone: yup
      .object({
        part1: yup.string().required('⚠️ 전화번호 앞자리를 입력해주세요'),
        part2: yup.string().required('⚠️ 전화번호 중간자리를 입력해주세요'),
        part3: yup.string().required('⚠️ 전화번호 뒷자리를 입력해주세요'),
      })
      .nullable()
      .required('⚠️ 전화번호를 입력해주세요')
      .test('phone-complete', '⚠️ 전화번호를 모두 입력해주세요', (value) => {
        if (!value) return false
        return (
          value.part1?.length === 3 &&
          value.part2?.length === 4 &&
          value.part3?.length === 4
        )
      }),
  })

  type FormValues = yup.InferType<typeof formSchema>

  const { handleSubmit, values } = useForm<FormValues>({
    validationSchema: toTypedSchema(formSchema),
    initialValues: {
      phone: null,
    },
  })

  const onSubmit = handleSubmit((formValues) => {})
  const idNumber1 = ref<IDNumberValue | null>(null)
  const idNumber2 = ref<IDNumberValue | null>({
    part1: '123456',
    part2: '1234567',
  })
  const idNumber3 = ref<IDNumberValue | null>(null)
  const idNumber4 = ref<IDNumberValue | null>(null)
  const idNumberError = ref<IDNumberValue | null>(null)
  const idError = ref(false)

  // 서버에서 내려온 데이터를 객체로 변환하는 예제
  const serverIdData = '1234561234567'
  const idFromServer = computed(() => {
    if (!serverIdData) return null
    return {
      part1: serverIdData.slice(0, 6),
      part2: serverIdData.slice(6, 13),
    }
  })

  // PhoneNumberInput blur 핸들러
  const handlePhoneBlur = () => {
    console.log('PhoneNumberInput blur')
  }

  // IDNumberInput blur 핸들러
  const handleIdBlur = () => {
    console.log('IDNumberInput blur')
  }

  // 에러 시뮬레이션
  const simulatePhoneError = () => {
    phoneError.value = !phoneError.value
  }

  const simulateIdError = () => {
    idError.value = !idError.value
  }
</script>

<template>
  <section class="bg-grey-50 min-h-screen p-8">
    <div class="mx-auto max-w-[1400px]">
      <h1 class="text-grey-900 mb-8 text-3xl font-bold">
        PhoneNumberInput & IDNumberInput 예제
      </h1>

      <div class="grid gap-8">
        <!-- PhoneNumberInput 섹션 -->
        <section
          class="border-grey-300 min-h-[420px] rounded-3xl border bg-white p-10"
        >
          <h2 class="text-grey-900 mb-6 text-2xl font-bold">
            PhoneNumberInput
          </h2>

          <div class="grid max-w-[720px] gap-6">
            <!-- 기본 사용 -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                기본 사용
              </h3>
              <PhoneNumberInput v-model="phoneNumber1" />
              <p class="text-grey-600 mt-2 text-sm">
                값: {{ phoneNumber1 ? JSON.stringify(phoneNumber1) : '없음' }}
              </p>
            </div>

            <!-- 초기값 설정 -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                초기값 설정
              </h3>
              <PhoneNumberInput v-model="phoneNumber2" />
              <p class="text-grey-600 mt-2 text-sm">
                값: {{ phoneNumber2 ? JSON.stringify(phoneNumber2) : '없음' }}
              </p>
            </div>

            <!-- 서버 데이터 변환 예제 -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                서버 데이터 변환 예제
              </h3>
              <p class="text-grey-600 mb-2 text-sm">
                서버 데이터: "{{ serverPhoneData }}"
              </p>
              <PhoneNumberInput v-model="phoneFromServer" />
              <p class="text-grey-600 mt-2 text-sm">
                변환된 값:
                {{ phoneFromServer ? JSON.stringify(phoneFromServer) : '없음' }}
              </p>
            </div>

            <!-- Placeholder 커스터마이징 -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                Placeholder 커스터마이징
              </h3>
              <PhoneNumberInput
                v-model="phoneNumber3"
                placeholder-part1="010"
                placeholder-part2="1234"
                placeholder-part3="5678"
              />
              <p class="text-grey-600 mt-2 text-sm">
                값: {{ phoneNumber3 ? JSON.stringify(phoneNumber3) : '없음' }}
              </p>
            </div>

            <!-- 다양한 전화번호 형식 -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                다양한 전화번호 형식
              </h3>
              <div class="grid gap-4">
                <div>
                  <p class="text-grey-600 mb-2 text-sm">지역전화(3-3-4)</p>
                  <PhoneNumberInput
                    v-model="phoneMobile"
                    :max-length-part1="3"
                    :max-length-part2="3"
                    :max-length-part3="4"
                    placeholder-part1="051"
                    placeholder-part2="123"
                    placeholder-part3="4567"
                  />
                  <p class="text-grey-600 mt-2 text-sm">
                    값:
                    {{ phoneMobile ? JSON.stringify(phoneMobile) : '없음' }}
                  </p>
                </div>
                <div>
                  <p class="text-grey-600 mb-2 text-sm">
                    지역전화 + 모바일(3-4-4)
                  </p>
                  <PhoneNumberInput
                    v-model="phoneLocal"
                    :max-length-part1="3"
                    :max-length-part2="4"
                    :max-length-part3="4"
                    placeholder-part1="010"
                    placeholder-part2="1234"
                    placeholder-part3="5678"
                  />
                  <p class="text-grey-600 mt-2 text-sm">
                    값:
                    {{ phoneLocal ? JSON.stringify(phoneLocal) : '없음' }}
                  </p>
                </div>
                <div>
                  <p class="text-grey-600 mb-2 text-sm">
                    지역전화 + 모바일 + 인터넷전화(4-4-4)
                  </p>
                  <PhoneNumberInput
                    v-model="phoneInternet"
                    :max-length-part1="4"
                    :max-length-part2="4"
                    :max-length-part3="4"
                    placeholder-part1="0704"
                    placeholder-part2="1234"
                    placeholder-part3="5678"
                  />
                  <p class="text-grey-600 mt-2 text-sm">
                    값:
                    {{ phoneInternet ? JSON.stringify(phoneInternet) : '없음' }}
                  </p>
                </div>
              </div>
            </div>

            <!-- Size 커스터마이징 -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                Size 커스터마이징
              </h3>
              <div class="grid gap-4">
                <div>
                  <p class="text-grey-600 mb-2 text-sm">
                    Size: md (기본값, max-width: 280px)
                  </p>
                  <PhoneNumberInput v-model="phoneNumber2" size="md" />
                </div>
                <div>
                  <p class="text-grey-600 mb-2 text-sm">
                    Size: lg (max-width: 360px)
                  </p>
                  <PhoneNumberInput v-model="phoneNumber2" size="lg" />
                </div>
                <div>
                  <p class="text-grey-600 mb-2 text-sm">
                    Size: xl (max-width: 480px)
                  </p>
                  <PhoneNumberInput v-model="phoneNumber2" size="xl" />
                </div>
                <div>
                  <p class="text-grey-600 mb-2 text-sm">
                    Size: max (max-width: 100%)
                  </p>
                  <PhoneNumberInput v-model="phoneNumber2" size="max" />
                </div>
              </div>
            </div>

            <!-- Disabled & Readonly -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                Disabled & Readonly
              </h3>
              <div class="grid gap-4">
                <div>
                  <p class="text-grey-600 mb-2 text-sm">Disabled</p>
                  <PhoneNumberInput v-model="phoneNumber4" :disabled="true" />
                </div>
                <div>
                  <p class="text-grey-600 mb-2 text-sm">Readonly</p>
                  <PhoneNumberInput v-model="phoneNumber2" :readonly="true" />
                </div>
              </div>
            </div>

            <!-- 에러 상태 -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                에러 상태
              </h3>
              <div class="mb-2">
                <button
                  type="button"
                  class="border-grey-300 text-grey-700 hover:bg-grey-50 rounded-lg border bg-white px-4 py-2 text-sm font-semibold transition-colors"
                  @click="simulatePhoneError"
                >
                  에러 토글
                </button>
              </div>
              <PhoneNumberInput
                v-model="phoneNumberError"
                :error="phoneError"
              />
              <p class="text-grey-600 mt-2 text-sm">
                값:
                {{
                  phoneNumberError ? JSON.stringify(phoneNumberError) : '없음'
                }}
              </p>
            </div>

            <!-- Blur 이벤트 -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                Blur 이벤트
              </h3>
              <PhoneNumberInput
                v-model="phoneNumber1"
                @blur="handlePhoneBlur"
              />
              <p class="text-grey-600 mt-2 text-sm">
                콘솔에서 blur 이벤트 확인 가능
              </p>
            </div>

            <!-- FormField와 함께 사용 -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                FormField와 함께 사용 (Validation)
              </h3>
              <form @submit.prevent="onSubmit">
                <FormField name="phone" error-message-id="phone-error">
                  <template #default="{ bind, field }">
                    <PhoneNumberInput
                      :model-value="field.value"
                      v-bind="bind"
                    />
                  </template>
                </FormField>
                <div class="mt-2">
                  <button
                    type="submit"
                    class="border-grey-300 text-grey-700 hover:bg-grey-50 rounded-lg border bg-white px-4 py-2 text-sm font-semibold transition-colors"
                  >
                    제출
                  </button>
                </div>
                <p class="text-grey-600 mt-2 text-sm">
                  현재 값:
                  {{ values.phone ? JSON.stringify(values.phone) : '없음' }}
                </p>
              </form>
            </div>
          </div>
        </section>

        <!-- IDNumberInput 섹션 -->
        <section
          class="border-grey-300 min-h-[420px] rounded-3xl border bg-white p-10"
        >
          <h2 class="text-grey-900 mb-6 text-2xl font-bold">IDNumberInput</h2>

          <div class="grid max-w-[720px] gap-6">
            <!-- 기본 사용 (주민등록번호) -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                기본 사용 (주민등록번호, 6-7자리)
              </h3>
              <IDNumberInput v-model="idNumber1" />
              <p class="text-grey-600 mt-2 text-sm">
                값: {{ idNumber1 ? JSON.stringify(idNumber1) : '없음' }}
              </p>
            </div>

            <!-- 초기값 설정 -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                초기값 설정
              </h3>
              <IDNumberInput v-model="idNumber2" />
              <p class="text-grey-600 mt-2 text-sm">
                값: {{ idNumber2 ? JSON.stringify(idNumber2) : '없음' }}
              </p>
            </div>

            <!-- 서버 데이터 변환 예제 -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                서버 데이터 변환 예제
              </h3>
              <p class="text-grey-600 mb-2 text-sm">
                서버 데이터: "{{ serverIdData }}"
              </p>
              <IDNumberInput v-model="idFromServer" />
              <p class="text-grey-600 mt-2 text-sm">
                변환된 값:
                {{ idFromServer ? JSON.stringify(idFromServer) : '없음' }}
              </p>
            </div>

            <!-- Placeholder 커스터마이징 -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                Placeholder 커스터마이징
              </h3>
              <IDNumberInput
                v-model="idNumber3"
                placeholder-part1="앞자리"
                placeholder-part2="뒷자리"
              />
              <p class="text-grey-600 mt-2 text-sm">
                값: {{ idNumber3 ? JSON.stringify(idNumber3) : '없음' }}
              </p>
            </div>

            <!-- MaxLength 커스터마이징 -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                MaxLength 커스터마이징 (예: 5-8자리)
              </h3>
              <IDNumberInput
                v-model="idNumber4"
                :part1-max-length="5"
                :part2-max-length="8"
              />
              <p class="text-grey-600 mt-2 text-sm">
                값: {{ idNumber4 ? JSON.stringify(idNumber4) : '없음' }}
              </p>
            </div>

            <!-- Size 커스터마이징 -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                Size 커스터마이징
              </h3>
              <div class="grid gap-4">
                <div>
                  <p class="text-grey-600 mb-2 text-sm">
                    Size: md (기본값, max-width: 280px)
                  </p>
                  <IDNumberInput v-model="idNumber2" size="md" />
                </div>
                <div>
                  <p class="text-grey-600 mb-2 text-sm">
                    Size: lg (max-width: 360px)
                  </p>
                  <IDNumberInput v-model="idNumber2" size="lg" />
                </div>
                <div>
                  <p class="text-grey-600 mb-2 text-sm">
                    Size: xl (max-width: 480px)
                  </p>
                  <IDNumberInput v-model="idNumber2" size="xl" />
                </div>
                <div>
                  <p class="text-grey-600 mb-2 text-sm">
                    Size: max (max-width: 100%)
                  </p>
                  <IDNumberInput v-model="idNumber2" size="max" />
                </div>
              </div>
            </div>

            <!-- Disabled & Readonly -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                Disabled & Readonly
              </h3>
              <div class="grid gap-4">
                <div>
                  <p class="text-grey-600 mb-2 text-sm">Disabled</p>
                  <IDNumberInput v-model="idNumber4" :disabled="true" />
                </div>
                <div>
                  <p class="text-grey-600 mb-2 text-sm">Readonly</p>
                  <IDNumberInput v-model="idNumber2" :readonly="true" />
                </div>
              </div>
            </div>

            <!-- 에러 상태 -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                에러 상태
              </h3>
              <div class="mb-2">
                <button
                  type="button"
                  class="border-grey-300 text-grey-700 hover:bg-grey-50 rounded-lg border bg-white px-4 py-2 text-sm font-semibold transition-colors"
                  @click="simulateIdError"
                >
                  에러 토글
                </button>
              </div>
              <IDNumberInput v-model="idNumberError" :error="idError" />
              <p class="text-grey-600 mt-2 text-sm">
                값:
                {{ idNumberError ? JSON.stringify(idNumberError) : '없음' }}
              </p>
            </div>

            <!-- Blur 이벤트 -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                Blur 이벤트
              </h3>
              <IDNumberInput v-model="idNumber1" @blur="handleIdBlur" />
              <p class="text-grey-600 mt-2 text-sm">
                콘솔에서 blur 이벤트 확인 가능
              </p>
            </div>

            <!-- Password 마스킹 기능 -->
            <div>
              <h3 class="text-grey-700 mb-3 text-lg font-semibold">
                Password 마스킹 기능 (뒷자리)
              </h3>
              <p class="text-grey-600 mb-2 text-sm">
                뒷자리는 기본적으로 password 타입으로 마스킹되며, 눈 아이콘을
                클릭하여 표시/숨김을 토글할 수 있습니다.
              </p>
              <IDNumberInput v-model="idNumber2" />
              <p class="text-grey-600 mt-2 text-sm">
                실제 값:
                {{ idNumber2 ? JSON.stringify(idNumber2) : '없음' }}
              </p>
            </div>

            <!-- 중요  -->
            <div
              class="mb-6 rounded-lg border border-red-200 bg-red-50 p-4 text-red-800"
            >
              <p class="mb-1 font-semibold">⚠️ 중요</p>
              <p class="text-sm">
                <code class="rounded bg-red-100 px-1">IDNumberInput</code>은
                <code class="rounded bg-red-100 px-1">ariaLabelPart1</code>과
                <code class="rounded bg-red-100 px-1">ariaLabelPart2</code>를
                <strong>반드시 입력</strong>해주세요
              </p>
            </div>
          </div>
        </section>
      </div>
    </div>
  </section>
</template>

<style scoped></style>
