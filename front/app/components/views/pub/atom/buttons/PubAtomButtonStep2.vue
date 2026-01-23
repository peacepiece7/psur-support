<script setup lang="ts">
  import { usePubGuideStore } from '~/stores/pubGuildStore'

  const pubGuideStore = usePubGuideStore()
</script>

<template>
  <section
    class="border-grey-300 bg-grey-50 min-h-[420px] rounded-3xl border p-10"
  >
    <h3 class="m-0 mb-4 text-2xl font-bold">Badge</h3>

    <div class="grid max-w-[720px] gap-4">
      <!-- Type: Tint -->
      <div>
        <p class="text-grey-700 mb-2 text-sm font-semibold">Type: Tint</p>
        <div class="flex flex-wrap items-center gap-2">
          <Badge type="tint" color="blue" label="Blue">
            <IcIcon icon="check" /> <span class="pl-2">Blue</span>
          </Badge>
          <Badge type="tint" color="sky-blue" label="Sky Blue" />
          <Badge type="tint" color="green" label="Green" />
          <Badge type="tint" color="purple" label="Purple" />
          <Badge type="tint" color="red" label="Red" />
          <Badge type="tint" color="orange" label="Orange" />
          <Badge type="tint" color="yellow" label="Yellow" />
          <Badge type="tint" color="grey" label="Grey" />
          <Badge type="tint" color="black" label="Black" />
        </div>
      </div>

      <!-- Type: Solid -->
      <div>
        <p class="text-grey-700 mb-2 text-sm font-semibold">Type: Solid</p>
        <div class="flex flex-wrap items-center gap-2">
          <Badge type="solid" color="blue" label="Blue">
            <IcIcon icon="check" />
            <span class="pl-2">Blue</span>
          </Badge>
          <Badge type="solid" color="blue" label="Blue">
            <IcIcon icon="alram" />
            <span class="pl-2">Blue</span>
          </Badge>
          <Badge type="solid" color="sky-blue" label="Sky Blue" />
          <Badge type="solid" color="green" label="Green" />
          <Badge type="solid" color="purple" label="Purple" />
          <Badge type="solid" color="red" label="Red" />
          <Badge type="solid" color="orange" label="Orange" />
          <Badge type="solid" color="yellow" label="Yellow" />
          <Badge type="solid" color="grey" label="Grey" />
          <Badge type="solid" color="black" label="Black" />
        </div>
      </div>

      <!-- Type: Line -->
      <div>
        <p class="text-grey-700 mb-2 text-sm font-semibold">Type: Line</p>
        <div class="flex flex-wrap items-center gap-2">
          <Badge type="line" color="blue" label="Blue">
            <IcIcon icon="eye-on" />
            <span class="pl-2">Blue</span>
          </Badge>
          <Badge type="line" color="blue" label="Blue">
            <IcIcon icon="check" />
            <span class="pl-2">Blue</span>
          </Badge>
          <Badge type="line" color="sky-blue" label="Sky Blue" />
          <Badge type="line" color="green" label="Green" />
          <Badge type="line" color="purple" label="Purple" />
          <Badge type="line" color="red" label="Red" />
          <Badge type="line" color="orange" label="Orange" />
          <Badge type="line" color="yellow" label="Yellow" />
          <Badge type="line" color="grey" label="Grey" />
          <Badge type="line" color="black" label="Black" />
        </div>
      </div>

      <!-- Size Variations -->
      <div>
        <p class="text-grey-700 mb-2 text-sm font-semibold">Size Variations</p>
        <div class="flex flex-wrap items-center gap-2">
          <Badge size="sm" type="tint" color="blue" label="Small Tint">
            <IcIcon icon="check" />
            <span class="pl-2">Small Tint</span>
          </Badge>
          <Badge size="md" type="tint" color="blue" label="Medium Tint" />
          <Badge size="sm" type="solid" color="green" label="Small Solid" />
          <Badge size="md" type="solid" color="green" label="Medium Solid" />
          <Badge size="sm" type="line" color="red" label="Small Line" />
          <Badge size="md" type="line" color="red" label="Medium Line" />
        </div>
      </div>
    </div>
  </section>
</template>
