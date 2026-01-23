import { usePubGuideStore } from '../stores/pubGuildStore'

export default defineNuxtRouteMiddleware(async (to, from) => {
  if (!import.meta.client) return
  const pubGuideStore = usePubGuideStore()
  pubGuideStore.initialize()
})
