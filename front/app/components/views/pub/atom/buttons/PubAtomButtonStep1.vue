<script setup lang="ts">
  import { usePubGuideStore } from '~/stores/pubGuildStore'

  const pubGuideStore = usePubGuideStore()

  const handleClick = () => {
    alert('Button clicked!')
  }
</script>

<template>
  <section
    class="border-grey-300 bg-grey-50 min-h-[420px] rounded-3xl border p-10"
  >
    <h3 class="m-0 mb-4 text-2xl font-bold">Button</h3>

    <div class="grid max-w-[720px] gap-4">
      <!-- Size Examples -->
      <div>
        <p class="text-grey-700 mb-2 text-sm font-semibold">Size</p>
        <div class="flex flex-wrap items-center gap-2">
          <Button size="icon">
            <v-icon>mdi-heart</v-icon>
          </Button>
          <Button size="xs">XS</Button>
          <Button size="sm">SM</Button>
          <Button size="md">MD</Button>
          <Button size="lg">Large(LG)</Button>
          <Button size="xl">XLarge(XL)</Button>
        </div>
      </div>

      <!-- Color Examples (Fill Variant) -->
      <div>
        <p class="text-grey-700 mb-2 text-sm font-semibold">Color (Fill)</p>
        <div class="flex flex-wrap items-center gap-2">
          <Button color="primary">Primary</Button>
          <Button color="secondary">Secondary</Button>
          <Button color="tertiary">Tertiary</Button>
          <Button color="warning">Warning</Button>
          <Button color="surface-strong">Surface Strong</Button>
          <Button color="surface">Surface</Button>
          <Button color="success">Success</Button>
          <Button color="muted">Muted</Button>
          <Button color="inverse">Inverse</Button>
        </div>
      </div>

      <!-- Variant Examples (Outlined) -->
      <div>
        <p class="text-grey-700 mb-2 text-sm font-semibold">
          Variant (Outlined)
        </p>
        <div class="flex flex-wrap items-center gap-2">
          <Button variant="outlined" color="primary">Primary</Button>
          <Button variant="outlined" color="secondary">Secondary</Button>
          <Button variant="outlined" color="tertiary">Tertiary</Button>
          <Button variant="outlined" color="warning">Warning</Button>
          <Button variant="outlined" color="surface-strong">
            Surface Strong
          </Button>
          <Button variant="outlined" color="surface">Surface</Button>
          <Button variant="outlined" color="success">Success</Button>
          <Button variant="outlined" color="muted">Muted</Button>
          <Button variant="outlined" color="inverse">Inverse</Button>
        </div>
      </div>

      <!-- Variant Examples (Secondary & Tertiary) -->
      <!-- <div>
        <p class="text-grey-700 mb-2 text-sm font-semibold">
          Variant (Secondary & Tertiary)
        </p>
        <div class="flex flex-wrap items-center gap-2">
          <Button variant="fill" color="secondary">Secondary Fill</Button>
          <Button variant="outlined" color="secondary"
            >Secondary Outlined</Button
          >
          <Button variant="fill" color="tertiary">Tertiary Fill</Button>
          <Button variant="outlined" color="tertiary">Tertiary Outlined</Button>
        </div>
      </div> -->

      <!-- Shape Examples -->
      <div>
        <p class="text-grey-700 mb-2 text-sm font-semibold">
          Shape (Primary Fill)
        </p>
        <div class="flex flex-wrap items-center gap-2">
          <Button shape="square">Square</Button>
          <Button shape="soft">Soft</Button>
          <Button shape="rounded">Rounded</Button>
          <Button shape="circle">
            <v-icon>mdi-heart</v-icon>
          </Button>
        </div>
      </div>

      <!-- Disabled State -->
      <div>
        <p class="text-grey-700 mb-2 text-sm font-semibold">Disabled</p>
        <div class="flex flex-wrap items-center gap-2">
          <Button disabled>Disabled Fill</Button>
          <Button variant="outlined" disabled>Disabled Outlined</Button>
        </div>
      </div>

      <!-- With Icons -->
      <div>
        <p class="text-grey-700 mb-2 text-sm font-semibold">With Icons</p>
        <div class="flex flex-wrap items-center gap-2">
          <Button>
            <v-icon>mdi-plus</v-icon>
            Add
          </Button>
          <Button variant="outlined">
            <v-icon>mdi-delete</v-icon>
            Delete
          </Button>
        </div>
      </div>

      <!-- Click Handler -->
      <div>
        <p class="text-grey-700 mb-2 text-sm font-semibold">Click Handler</p>
        <div class="flex flex-wrap items-center gap-2">
          <Button @click="handleClick">Click Me</Button>
          <Button variant="outlined" @click="handleClick">Click Me</Button>
        </div>
      </div>

      <!-- Color Combinations -->
      <p>Inverse는 내부 컨텐츠 색상을 정의해서 사용하세요</p>
      <div>
        <p class="text-grey-700 mb-2 text-sm font-semibold">
          Color Combinations (Outlined)
        </p>
        <div class="flex flex-wrap items-center gap-2">
          <Button variant="outlined" color="primary">Primary</Button>
          <Button variant="outlined" color="secondary">Secondary</Button>
          <Button variant="outlined" color="tertiary">Tertiary</Button>
          <Button variant="outlined" color="warning">Warning</Button>
          <Button variant="outlined" color="success">Success</Button>
          <Button variant="outlined" color="muted">Muted</Button>
          <Button variant="outlined" size="icon" color="inverse" shape="circle">
            <v-icon icon="mdi-heart" class="text-red-500" />
          </Button>
        </div>
      </div>
    </div>
  </section>
</template>
