<script setup lang="ts">
  import { Rive } from '@rive-app/canvas'
  import type { Rive as RiveType, StateMachineInput } from '@rive-app/canvas'
  import { useForm } from 'vee-validate'
  import { toTypedSchema } from '@vee-validate/yup'
  import * as yup from 'yup'
  import FormField from '~/components/molecules/FormField.vue'
  import TextField from '~/components/atoms/TextField.vue'
  import SelectionGroup, {
    type GroupOption,
  } from '~/components/atoms/SelectionGroup.vue'

  /* =========================
   * validation schema
   * ========================= */
  const loginSchema = yup.object({
    userId: yup.string().required('아이디를 입력해주세요'),
    password: yup.string().required('비밀번호를 입력해주세요'),
    rememberId: yup.array().of(yup.string()).default([]),
  })

  type LoginFormValues = yup.InferType<typeof loginSchema>

  // useForm 설정
  const { handleSubmit, values } = useForm<LoginFormValues>({
    validationSchema: toTypedSchema(loginSchema),
    initialValues: {
      userId: '',
      password: '',
      rememberId: [],
    },
  })

  // SelectionGroup 옵션 정의
  const rememberIdOptions: GroupOption<string>[] = [
    { label: '아이디저장', value: 'remember' },
  ]

  const emit = defineEmits<{
    'login-success': []
  }>()

  const canvas = ref<HTMLCanvasElement | null>(null)
  let rive: RiveType | null = null

  // State Machine inputs
  const inputs: Record<string, StateMachineInput> = {}

  // eye_track smoothing용
  let eyeTrackRAF: number | null = null
  let currentEyeTrack = 0.5 // 중앙
  let targetEyeTrack = 0.5

  /* =========================
   * auth
   * ========================= */
  const { loginNative, loginKakao, loginNaver, loginNice } = useAuth()

  /* =========================
   * eye_track helper
   * ========================= */
  const startEyeTrack = () => {
    stopEyeTrack()

    const animate = () => {
      currentEyeTrack += (targetEyeTrack - currentEyeTrack) * 0.15
      if (inputs.eye_track) {
        inputs.eye_track.value = currentEyeTrack
      }
      eyeTrackRAF = requestAnimationFrame(animate)
    }

    animate()
  }

  const stopEyeTrack = () => {
    if (eyeTrackRAF) {
      cancelAnimationFrame(eyeTrackRAF)
      eyeTrackRAF = null
    }
  }

  /* =========================
   * rive handlers
   * ========================= */
  const handleIdFocus = () => {
    if (inputs.isFocus) {
      inputs.isFocus.value = true
    }
  }

  const handleIdBlur = () => {
    if (inputs.isFocus) {
      inputs.isFocus.value = false
    }
  }

  const handlePasswordFocus = () => {
    if (inputs.IsPassword) {
      // inputs.IsPassword.value = false
      inputs.IsPassword.value = true
    }

    // password 입력 시 시선 고정 (정면 약간 아래)
    // targetEyeTrack = 0.5
    // startEyeTrack()
  }

  const handlePasswordBlur = () => {
    if (inputs.IsPassword) {
      inputs.IsPassword.value = false
    }

    // 다시 idle 시선
    targetEyeTrack = 0.5
    startEyeTrack()
  }

  /* =========================
   * login handlers
   * ========================= */
  const onSubmit = handleSubmit(async (formValues) => {
    try {
      await loginNative(formValues.userId, formValues.password, true)
      inputs.login_success?.fire()
      emit('login-success')
    } catch {
      inputs.login_fail?.fire()
    }
  })

  const handleLogin = async () => {
    // validation 실패 시 login_fail 이벤트 발생
    const isValid = await new Promise<boolean>((resolve) => {
      handleSubmit(
        () => {
          resolve(true)
        },
        () => {
          inputs.login_fail?.fire()
          resolve(false)
        },
      )()
    })

    if (isValid) {
      await onSubmit()
    }
  }

  const handleKakaoLogin = async () => {
    await loginKakao(true)
    inputs.login_success?.fire()
    emit('login-success')
  }

  const handleNaverLogin = async () => {
    await loginNaver(true)
    inputs.login_success?.fire()
    emit('login-success')
  }

  const handleNiceLogin = async () => {
    await loginNice(true)
    inputs.login_success?.fire()
    emit('login-success')
  }

  const handleSignup = () => {
    emit('login-success')
    navigateTo('/sign-up')
  }

  /* =========================
   * lifecycle
   * ========================= */
  onMounted(async () => {
    await nextTick()
    if (!canvas.value) return

    rive = new Rive({
      src: '/animated-login-bunny-character.riv',
      canvas: canvas.value,
      stateMachines: 'State Machine 1',
      autoplay: true,
      onLoad: () => {
        const smInputs = rive!.stateMachineInputs('State Machine 1')
        smInputs.forEach((input) => {
          inputs[input.name] = input
        })

        // 초기 시선 중앙
        if (inputs.eye_track) {
          inputs.eye_track.value = currentEyeTrack
        }
      },
    })
  })

  onUnmounted(() => {
    stopEyeTrack()
    rive?.cleanup()
    rive = null
  })
</script>
<template>
  <div
    class="login-section"
    style="
      display: flex;
      flex-direction: column;
      align-items: center;
      gap: 1rem;
    "
  >
    <!-- Rive 캐릭터 -->
    <div class="border-grey-200 rounded-4xl border-2 border-solid">
      <canvas
        ref="canvas"
        class="login-section__canvas"
        style="width: 100%; height: 100%"
      />
    </div>

    <!-- 로그인 카드 -->
    <v-card style="width: 100%; min-width: 340px">
      <v-card-title
        class="border-grey-200 border-b px-6 py-4 text-xl font-bold"
      >
        로그인
      </v-card-title>

      <v-card-text
        class="gap-2 p-6"
        style="display: flex; flex-direction: column"
      >
        <FormField name="userId">
          <template #default="{ bind, field }">
            <TextField
              :model-value="field.value"
              v-bind="bind"
              label="아이디"
              placeholder="아이디를 입력하세요"
              size="max"
              @focus="handleIdFocus"
            />
          </template>
        </FormField>

        <FormField name="password">
          <template #default="{ bind, field }">
            <TextField
              :model-value="field.value"
              v-bind="bind"
              label="비밀번호"
              type="password"
              placeholder="비밀번호를 입력하세요"
              size="max"
              @keyup.enter="handleLogin"
              @focus="handlePasswordFocus"
              @blur="handlePasswordBlur"
            />
          </template>
        </FormField>

        <Button color="primary" class="mt-2" @click="handleLogin">
          로그인
        </Button>

        <div
          class="gap-2"
          style="
            display: flex;
            align-items: center;
            justify-content: space-between;
          "
        >
          <FormField name="rememberId">
            <template #default="{ bind, field }">
              <SelectionGroup
                type="checkbox"
                :model-value="field.value"
                v-bind="bind"
                :items="rememberIdOptions"
                direction="horizontal"
                color-set="primary"
              />
            </template>
          </FormField>

          <div class="gap-2 text-sm" style="display: flex; align-items: center">
            <a href="#" class="text-grey-600">아이디찾기</a>
            <span style="color: var(--color-grey-300)">|</span>
            <a href="#" class="text-grey-600">비밀번호 초기화</a>
          </div>
        </div>

        <div class="gap-2" style="display: flex; justify-content: center">
          <button
            style="width: 40px; height: 40px; border-radius: 50%"
            @click="handleKakaoLogin"
          >
            <v-icon>mdi-chat</v-icon>
          </button>

          <button
            style="
              width: 40px;
              height: 40px;
              border-radius: 50%;
              background-color: #03c75a;
            "
            @click="handleNaverLogin"
          >
            <v-icon>mdi-account</v-icon>
          </button>
        </div>

        <Button color="primary" class="mt-2" @click="handleNiceLogin">
          나이스 본인인증
        </Button>

        <Button color="primary" class="mt-2" @click="handleSignup">
          회원가입
        </Button>
      </v-card-text>
    </v-card>
  </div>
</template>
