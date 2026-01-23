import type { RouterOptions } from 'vue-router'

export default <RouterOptions>{
  scrollBehavior(to, from, savedPosition) {
    return { left: 0, top: 0 }
  },
}
