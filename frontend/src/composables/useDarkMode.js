// src/composables/useDarkMode.js
import { ref, watchEffect, provide, inject } from 'vue'

const DARK_MODE_KEY = 'isDarkMode'

// 提供深色模式状态
export function provideDarkMode() {
  const isDarkMode = ref(localStorage.getItem(DARK_MODE_KEY) === 'true')

  const toggleDarkMode = () => {
    isDarkMode.value = !isDarkMode.value
    localStorage.setItem(DARK_MODE_KEY, isDarkMode.value)
  }

  // 应用到HTML根元素
  watchEffect(() => {
    document.documentElement.classList.toggle('dark', isDarkMode.value)
    document.documentElement.style.setProperty('color-scheme', isDarkMode.value ? 'dark' : 'light')
  })

  provide('darkMode', {
    isDarkMode,
    toggleDarkMode
  })

  return { isDarkMode, toggleDarkMode }
}

// 在子组件中使用深色模式
export function useDarkMode() {
  const darkMode = inject('darkMode')
  if (!darkMode) {
    throw new Error('useDarkMode() is called without provider.')
  }
  return darkMode
}