<template>
  <div class="app-container" :class="{ 'dark-mode': isDarkMode }">
    <header class="top-nav">
      <div class="container">
        <div class="nav-content">
          <div class="nav-left">
            <div class="nav-logo">
              <span class="logo-text">Lucky SMS</span>
              <span class="logo-badge">STU</span>
            </div>
          </div>

          <div class="nav-center">
            <div class="nav-search">
              <el-input v-model="searchQuery" placeholder="搜索课程/成绩/图书..." size="small" class="search-input"
                @keyup.enter="handleSearch" clearable :class="{ 'focus-visible': isSearchFocused }">
                <template #prefix>
                  <el-icon class="search-icon">
                    <Search />
                  </el-icon>
                </template>
              </el-input>
            </div>
          </div>

          <div class="nav-right">
            <button class="dark-mode-toggle action-btn" @click="toggleDarkMode" aria-label="切换暗色模式"
              :title="isDarkMode ? '切换至日间主题' : '切换至夜间主题'">
              <el-icon :size="20" :class="isDarkMode ? 'icon-moon' : 'icon-sun'">
                <Sunny v-if="!isDarkMode" />
                <Moon v-else />
              </el-icon>
            </button>

            <div class="notification-center">
              <el-badge :value="unreadCount" max="99" :hidden="unreadCount === 0">
                <button class="notification-btn action-btn" @click="toggleNotification" aria-label="通知中心" title="通知中心">
                  <el-icon :size="20">
                    <Bell />
                  </el-icon>
                </button>
              </el-badge>
            </div>

            <button class="mobile-menu-btn action-btn" v-if="isMobile" @click="toggleMobileSidebar"
              aria-label="移动端菜单" title="菜单">
              <el-icon>
                <Menu />
              </el-icon>
            </button>
          </div>
        </div>
      </div>
    </header>

    <div class="body-container">
      <aside v-if="showSidebar" class="sidebar" :class="{ 'sidebar-mobile-open': mobileSidebarOpen }"
        :aria-hidden="!mobileSidebarOpen && isMobile">
        <el-menu :default-active="activeMenuIndex" class="sidebar-menu" @select="handleMenuSelect"
          background-color="var(--sidebar-bg)" text-color="var(--sidebar-text)"
          active-text-color="var(--primary-color)">
          <el-menu-item index="1">
            <el-icon>
              <House />
            </el-icon>
            <template #title>首页</template>
          </el-menu-item>
          <el-menu-item index="2">
            <el-icon>
              <Notebook />
            </el-icon>
            <template #title>学籍信息</template>
          </el-menu-item>
          <el-menu-item index="3">
            <el-icon>
              <Histogram />
            </el-icon>
            <template #title>成绩查询</template>
          </el-menu-item>
          <el-menu-item index="4">
            <el-icon>
              <Calendar />
            </el-icon>
            <template #title>课表查询</template>
          </el-menu-item>
          <el-menu-item index="5">
            <el-icon>
              <Reading />
            </el-icon>
            <template #title>图书借阅</template>
          </el-menu-item>
          <el-menu-item index="6">
            <el-icon>
              <EditPen />
            </el-icon>
            <template #title>选课系统</template>
          </el-menu-item>
          <el-menu-item index="7">
            <el-icon>
              <Setting />
            </el-icon>
            <template #title>系统设置</template>
          </el-menu-item>
        </el-menu>
      </aside>

      <main class="main-content">
        <div class="breadcrumb-container">
          <el-breadcrumb separator="/" class="breadcrumb">
            <el-breadcrumb-item :to="{ path: '/' }">Lucky-SMS</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentPageName }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="router-view-container">
          <div class="content-loading" v-if="isLoading">
            <el-spinner size="40" type="ball-clip-rotate" />
            <div v-loading="isLoading"></div>
          </div>

          <div class="content-error" v-else-if="hasError">
            <el-icon size="60" class="error-icon">
              <Warning />
            </el-icon>
            <p class="error-message">{{ errorMessage }}</p>
            <el-button size="small" type="primary" @click="retry" round>
              点击重试
            </el-button>
          </div>

          <router-view v-else v-slot="{ Component }">
            <transition name="fade" mode="out-in">
              <component :is="Component" />
            </transition>
          </router-view>
        </div>
      </main>
    </div>

    <nav class="mobile-bottom-nav" v-if="isMobile && showSidebar">
      <button v-for="item in mobileNavItems" :key="item.index" @click="handleMobileNav(item)"
        :class="{ 'active': route.path === item.path }" :aria-current="route.path === item.path ? 'page' : undefined">
        <el-icon :size="20">
          <component :is="item.icon" />
        </el-icon>
        <span>{{ item.text }}</span>
      </button>
    </nav>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router';
import {
  Notebook, Histogram, Setting, Calendar, Reading, EditPen,
  Menu, Search, Bell, Moon, Sunny, Warning, House
} from '@element-plus/icons-vue'
import { provideDarkMode } from '@/composables/useDarkMode'

const router = useRouter()
const route = useRoute()

const { isDarkMode, toggleDarkMode } = provideDarkMode()

const mobileNavItems = [
  { index: '1', icon: House, text: '首页', path: '/student' },
  { index: '2', icon: Notebook, text: '学籍', path: '/student/status' },
  { index: '3', icon: Histogram, text: '成绩', path: '/student/grades' },
  { index: '4', icon: Calendar, text: '课表', path: '/student/schedule' },
  { index: '5', icon: Reading, text: '图书', path: '/student/library' },
  { index: '6', icon: EditPen, text: '选课', path: '/student/course' }
]

const currentPageName = ref('')
const searchQuery = ref('')
const isSearchFocused = ref(false)
const notificationVisible = ref(false)
const userMenuVisible = ref(false)
const mobileSidebarOpen = ref(false)
const isMobile = ref(false)
const isLoading = ref(false)
const hasError = ref(false)
const errorMessage = ref('')
const unreadCount = ref(3)

const setPageLoading = (loading, errorMsg = '') => {
  isLoading.value = loading
  hasError.value = !loading && !!errorMsg
  errorMessage.value = errorMsg || '加载失败，请重试'
}

const showSidebar = computed(() => {
  return !['/login'].includes(route.path)
})

const activeMenuIndex = computed(() => {
  const routeMap = {
    '/student': '1',
    '/student/status': '2',
    '/student/grades': '3',
    '/student/schedule': '4',
    '/student/library': '5',
    '/student/course': '6',
    '/student/settings': '7'
  }
  return routeMap[route.path] || '1'
})

watch(route, (newRoute) => {
  setPageLoading(true)
  const timer = setTimeout(() => {
    const pageTitles = {
      '/student': '首页',
      '/student/status': '学籍信息',
      '/student/grades': '成绩查询',
      '/student/schedule': '课表查询',
      '/student/library': '图书借阅',
      '/student/course': '选课系统',
      '/student/settings': '系统设置',
      '/login': '登录'
    }
    currentPageName.value = pageTitles[newRoute.path] || '首页'
    setPageLoading(false)
    clearTimeout(timer)
  }, 500)
}, { immediate: true })

const handleMobileNav = (item) => {
  router.push(item.path)
}

const toggleMobileSidebar = () => {
  mobileSidebarOpen.value = !mobileSidebarOpen.value
  document.body.style.overflow = mobileSidebarOpen.value ? 'hidden' : ''
}

const handleMenuSelect = (index) => {
  const routeMap = {
    '1': '/student',
    '2': '/student/status',
    '3': '/student/grades',
    '4': '/student/schedule',
    '5': '/student/library',
    '6': '/student/course',
    '7': '/student/settings'
  }
  if (routeMap[index]) {
    router.push(routeMap[index])
    if (isMobile.value) {
      mobileSidebarOpen.value = false
      document.body.style.overflow = ''
    }
  }
}

const closeDropdowns = (e) => {
  const userMenuEl = document.querySelector('.user-area .el-popover')
  const notificationEl = document.querySelector('.notification-center .el-popover')
  const sidebarEl = document.querySelector('.sidebar')
  const mobileMenuBtn = document.querySelector('.mobile-menu-btn')

  if (userMenuVisible.value && userMenuEl && !userMenuEl.contains(e.target)) {
    userMenuVisible.value = false
  }

  if (notificationVisible.value && notificationEl && !notificationEl.contains(e.target)) {
    notificationVisible.value = false
  }

  if (isMobile.value && mobileSidebarOpen.value && sidebarEl && mobileMenuBtn &&
    !sidebarEl.contains(e.target) && !mobileMenuBtn.contains(e.target)) {
    mobileSidebarOpen.value = false
    document.body.style.overflow = ''
  }
}

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    console.log('搜索:', searchQuery.value)
  }
}

const toggleNotification = () => {
  notificationVisible.value = !notificationVisible.value
}

const retry = () => {
  setPageLoading(true)
  setTimeout(() => {
    setPageLoading(false)
  }, 1000)
}

const checkScreenSize = () => {
  isMobile.value = window.innerWidth < 768
  if (isMobile.value) {
    mobileSidebarOpen.value = false
  }
}

onMounted(() => {
  document.addEventListener('click', closeDropdowns)
  window.addEventListener('resize', checkScreenSize)
  checkScreenSize()
  setPageLoading(true)
  setTimeout(() => {
    setPageLoading(false)
  }, 800)
})

onUnmounted(() => {
  document.removeEventListener('click', closeDropdowns)
  window.removeEventListener('resize', checkScreenSize)
})
</script>

<style lang="scss">
:root {
  --primary-color: #4361ee;
  --primary-light: #e0e7ff;
  --primary-dark: #3a56d5;
  --secondary-color: #6b7280;
  --success-color: #10b981;
  --warning-color: #f59e0b;
  --danger-color: #ef4444;
  --info-color: #3b82f6;
  --gray-50: #f9fafb;
  --gray-100: #f3f4f6;
  --gray-200: #e5e7eb;
  --gray-300: #d1d5db;
  --gray-400: #9ca3af;
  --gray-500: #6b7280;
  --gray-600: #4b5563;
  --gray-700: #374151;
  --gray-800: #1f2937;
  --gray-900: #111827;
  --student-color: #10b981;
  --white: #ffffff;
  --sidebar-bg: var(--white);
  --sidebar-text: var(--gray-600);
  --shadow-sm: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
  --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
  --shadow-xl: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
  --border-radius: 8px;
  --border-radius-lg: 16px;
  --transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  --transition-fast: all 0.15s cubic-bezier(0.4, 0, 0.2, 1);
}

.dark-mode {
  --primary-color: #6366f1;
  --primary-light: #4338ca;
  --primary-dark: #a5b4fc;
  --gray-50: #111827;
  --gray-100: #1f2937;
  --gray-200: #374151;
  --gray-300: #4b5563;
  --gray-400: #6b7280;
  --gray-500: #9ca3af;
  --gray-600: #d1d5db;
  --gray-700: #e5e7eb;
  --gray-800: #f3f4f6;
  --gray-900: #f9fafb;
  --white: #1f2937;
  --sidebar-bg: #111827;
  --sidebar-text: var(--gray-400);
}
</style>

<style scoped lang="scss">
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

.app-container {
  font-family: 'Inter', system-ui, -apple-system, sans-serif;
  line-height: 1.5;
  color: var(--gray-800);
  background-color: var(--gray-50);
  height: 100vh;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.container {
  max-width: 1440px;
  margin: 0 auto;
  padding: 0 24px;
}

.top-nav {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  padding: 8px 0;
  box-shadow: var(--shadow-sm);
  position: sticky;
  z-index: 1000;
  border-right: 1px solid var(--gray-200);
  transition: var(--transition);
}

.dark-mode .top-nav {
  background: rgba(17, 24, 39, 0.95);
  border-right: 1px solid var(--gray-800);
}

.nav-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 56px;
  gap: 16px;
}

.nav-left {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 0 0 auto;
}

.nav-logo {
  position: relative;
  display: flex;
  align-items: center;
}

.logo-text {
  font-size: 24px;
  font-weight: 700;
  color: var(--primary-color);
  letter-spacing: 0.5px;
  text-shadow: 0 2px 4px rgba(59, 130, 246, 0.2);
}

.logo-badge {
  position: absolute;
  top: -11px;
  right: -24px;
  font-size: 10px;
  background: var(--student-color);
  color: var(--white);
  padding: 2px 6px;
  border-radius: 10px;
  font-weight: bold;
}

.nav-center {
  flex: 1;
  display: flex;
  justify-content: center;
  max-width: 600px;
  margin: 0 20px;
}

.nav-search {
  width: 100%;
  max-width: 500px;
}

.search-input {
  border-radius: 20px;
  border: 1px solid var(--gray-300);
  transition: all 0.3s ease;
  background-color: var(--gray-50);
  box-shadow: none;

  :deep(.el-input__wrapper) {
    background-color: transparent;
    box-shadow: none;
    padding: 0 16px;
    height: 36px;
  }

  :deep(.el-input__prefix) {
    color: var(--gray-400);
    margin-right: 8px;
    transition: var(--transition);
  }
}

.search-input:hover,
.search-input:focus-within {
  border-color: var(--primary-color);
  box-shadow: 0 0 0 2px rgba(67, 97, 238, 0.1);

  :deep(.el-input__prefix) {
    color: var(--primary-color);
  }
}

.search-icon {
  transition: all 0.3s ease;
}

.search-input:focus-within .search-icon {
  transform: scale(1.1);
}

:deep(.el-input__clear) {
  color: var(--gray-400);
  transition: all 0.2s ease;

  &:hover {
    color: var(--primary-color);
    transform: scale(1.1);
  }
}

.dark-mode {
  .search-input {
    background-color: var(--gray-800);
    border-color: var(--gray-600);

    :deep(.el-input__inner) {
      color: var(--gray-200);
    }
  }

  .search-input:hover,
  .search-input:focus-within {
    border-color: var(--primary-color);
    box-shadow: 0 0 0 2px rgba(99, 102, 241, 0.2);
  }
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 0 0 auto;
}

.action-btn {
  background: none;
  border: none;
  color: var(--gray-600);
  cursor: pointer;
  width: 36px;
  height: 36px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  transition: var(--transition);
  position: relative;
  outline: none;
}

.action-btn:hover {
  background-color: var(--gray-100);
  color: var(--primary-color);
}

.action-btn:active {
  transform: scale(0.95);
}

.dark-mode .action-btn:hover {
  background-color: var(--gray-800);
}

.notification-center {
  position: relative;
}

.user-area {
  position: relative;
}

.user-btn {
  width: 40px;
  height: 40px;
}

.user-avatar {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid var(--primary-color);
}

.user-avatar-big {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid var(--primary-color);
}

.user-info {
  padding: 16px;
}

.user-detail {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.user-name {
  font-size: 16px;
  font-weight: 600;
  color: var(--gray-800);
}

.user-id {
  font-size: 12px;
  color: var(--gray-500);
}

.user-role {
  font-size: 12px;
  color: var(--gray-600);
}

.user-menu {
  border: none;
}

:deep(.user-menu .el-menu-item) {
  height: 40px;
  line-height: 40px;
  font-size: 14px;
  color: var(--gray-600);
  transition: var(--transition);

  &:hover {
    color: var(--primary-color);
    background-color: var(--gray-50);
  }
}

:deep(.user-menu .el-menu-item.is-active) {
  color: var(--primary-color);
  background-color: var(--primary-light);
}

.logout-item {
  color: var(--danger-color) !important;
}

.body-container {
  display: flex;
  flex: 1;
  overflow: hidden;
  height: calc(100vh - 72px);
}

.sidebar {
  width: 220px;
  background: var(--sidebar-bg);
  border-right: 1px solid var(--gray-200);
  display: flex;
  flex-direction: column;
  transition: all var(--transition);
  z-index: 100;
  height: 100%;
  position: relative;
}

.sidebar-menu {
  border-right: none;
  flex: 1;
  padding: 16px 8px;
  overflow-x: hidden;
  overflow-y: auto;
}

:deep(.sidebar-menu .el-menu-item) {
  height: 44px;
  line-height: 44px;
  padding: 0 12px !important;
  margin: 4px 0 !important;
  border-radius: var(--border-radius) !important;
  transition: var(--transition) !important;
  font-size: 14px;
  font-weight: 500;
  display: flex;
  align-items: center;
}

:deep(.sidebar-menu .el-menu-item:hover:not(.is-active)) {
  background-color: var(--gray-50) !important;
  color: var(--primary-color) !important;
}

:deep(.sidebar-menu .el-menu-item.is-active) {
  background-color: var(--primary-light) !important;
  color: var(--primary-color) !important;
  font-weight: 600 !important;
}

:deep(.sidebar-menu .el-menu-item .el-icon) {
  margin-right: 12px !important;
  font-size: 18px !important;
}

.dark-mode {
  :deep(.sidebar-menu .el-menu-item.is-active) {
    background-color: rgba(67, 56, 202, 0.2) !important;
  }

  :deep(.sidebar-menu .el-menu-item:hover:not(.is-active)) {
    background-color: var(--gray-800) !important;
  }
}

.main-content {
  flex: 1;
  padding: 24px;
  background-color: var(--gray-50);
  transition: var(--transition);
  position: relative;
  overflow-y: auto;
  height: 100%;
}

.main-content::after {
  content: '';
  position: fixed;
  top: 72px;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 99;
  opacity: 0;
  pointer-events: none;
  transition: opacity var(--transition);
}

.router-view-container {
  min-height: calc(100% - 40px);
  background: var(--white);
  border-radius: var(--border-radius-lg);
  box-shadow: var(--shadow-sm);
  padding: 24px;
  transition: var(--transition);
}

.router-view-container:hover {
  box-shadow: var(--shadow-md);
}

.breadcrumb-container {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
  flex-wrap: wrap;
  gap: 12px;
}

.breadcrumb {
  font-size: 0.875rem;
  color: var(--gray-500);
}

:deep(.el-breadcrumb__item:last-child .el-breadcrumb__inner) {
  color: var(--primary-color) !important;
  font-weight: 500;
}

.breadcrumb :deep(.el-breadcrumb__inner) {
  color: var(--gray-600);
  transition: var(--transition);
}

.breadcrumb :deep(.el-breadcrumb__inner:hover) {
  color: var(--primary-color);
}

.dark-mode .breadcrumb :deep(.el-breadcrumb__inner) {
  color: var(--gray-400);
}

.dark-mode .breadcrumb :deep(.el-breadcrumb__inner:hover) {
  color: var(--primary-light);
}

.page-actions {
  display: flex;
  gap: 8px;
}

.content-loading {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(255, 255, 255, 0.9);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;
  z-index: 10;
  border-radius: var(--border-radius-lg);
}

.dark-mode .content-loading {
  background-color: rgba(17, 24, 39, 0.9);
}

.content-error {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: var(--white);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  gap: 16px;
  z-index: 10;
  border-radius: var(--border-radius-lg);
  text-align: center;
  padding: 24px;
}

.error-icon {
  color: var(--warning-color);
}

.error-message {
  color: var(--gray-600);
  max-width: 400px;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

.mobile-bottom-nav {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  background: var(--white);
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.1);
  z-index: 1000;
  padding: 8px 0;

  button {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    background: none;
    border: none;
    color: var(--gray-500);
    cursor: pointer;
    transition: var(--transition-fast);
    padding: 4px 0;

    .el-icon {
      margin-bottom: 4px;
      font-size: 1.25rem;
    }

    span {
      font-size: 0.75rem;
    }

    &.active {
      color: var(--primary-color);

      .el-icon {
        color: var(--primary-color);
      }
    }

    &:hover:not(.active) {
      color: var(--gray-700);
    }
  }
}

.dark-mode .mobile-bottom-nav {
  background: var(--gray-800);
  box-shadow: 0 -2px 8px rgba(0, 0, 0, 0.3);

  button {
    color: var(--gray-400);

    &:hover:not(.active) {
      color: var(--gray-300);
    }
  }
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@media (max-width: 1024px) {
  .nav-search {
    max-width: 400px;
  }

  .sidebar {
    width: 260px;
  }
  
  .main-content {
    padding: 20px;
  }

  .router-view-container {
    padding: 16px;
  }
}

@media (max-width: 768px) {
  .main-content {
    padding: 15px;
    margin-left: 0 !important;
    width: 100% !important;
    padding-bottom: 72px;
  }

  .sidebar {
    width: 280px;
    position: fixed;
    left: 0;
    top: 72px;
    height: calc(100vh - 72px);
    transform: translateX(-100%);
    z-index: 1001;
  }

  .sidebar.sidebar-mobile-open {
    transform: translateX(0);
    box-shadow: var(--shadow-xl);
  }

  .main-content::after {
    display: block;
  }

  .sidebar-mobile-open+.main-content::after {
    opacity: 1;
    pointer-events: auto;
  }

  .nav-center {
    display: none;
  }

  .breadcrumb-container {
    flex-direction: column;
    align-items: flex-start;
  }

  .router-view-container {
    height: auto;
    min-height: calc(100vh - 180px);
    padding: 15px;
  }

  .top-nav .container {
    padding: 0 15px;
  }

  .mobile-menu-btn {
    display: flex;
  }
}

@media (max-width: 480px) {
  .container {
    padding: 0 10px;
  }

  .logo-text {
    font-size: 18px;
  }

  .main-content {
    padding: 10px;
    padding-bottom: 72px;
  }

  .router-view-container {
    padding: 10px;
    min-height: calc(100vh - 160px);
  }

  .sidebar {
    width: 260px;
  }

  .action-btn {
    width: 32px;
    height: 32px;
  }

  .nav-content {
    gap: 8px;
  }
}
</style>
