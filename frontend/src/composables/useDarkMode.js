import { ref } from 'vue'

const isDarkMode = ref(false)

const toggleDarkMode = () => {
  isDarkMode.value = !isDarkMode.value
  localStorage.setItem('isDarkMode', isDarkMode.value)
  document.documentElement.classList.toggle('dark-mode', isDarkMode.value)
}

const provideDarkMode = () => {
  const savedDarkMode = localStorage.getItem('isDarkMode')
  if (savedDarkMode !== null) {
    isDarkMode.value = savedDarkMode === 'true'
    document.documentElement.classList.toggle('dark-mode', isDarkMode.value)
  }

  return {
    isDarkMode,
    toggleDarkMode
  }
}

export { provideDarkMode }
