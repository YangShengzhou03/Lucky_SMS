<template>
  <div class="app-container">
    <div class="top-nav">
      <div class="container">
        <div class="nav-content">
          <div class="nav-logo">
            <span class="logo-text">Lucky SMS</span>
            <span class="logo-badge">PRO</span>
          </div>
          <div class="nav-actions">
            <el-button type="text" size="large" class="main-login-btn" @click="handleLogin">
              <span class="btn-text">{{ isLoggedIn ? '我的主页' : '登录 / 注册' }}</span>
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <div v-if="isHomePage">
      <main>
        <section id="cta" class="cta-section">
          <div class="container">
            <div class="cta-content">
              <div class="cta-text">
                <h2 class="cta-title">开启智能教育新时代</h2>
                <p class="cta-subtitle">立即体验我们的智能学生管理系统，让教育管理变得更加高效便捷</p>
              </div>
              <div class="cta-actions">
                <el-button type="default" size="large" @click="handleLogin" class="cta-btn-secondary">
                  开始体验
                </el-button>
              </div>
            </div>
          </div>
        </section>
      </main>
    </div>

    <router-view v-else />

    <footer class="footer">
      <div class="container">
        <div class="footer-content">
          <div class="footer-section">
            <h3>Lucky SMS</h3>
            <p>高效、便捷的教育服务解决方案</p>
          </div>
          <div class="footer-section">
            <h3>快速链接</h3>
            <ul>
              <li @click="handleLogin">立即体验</li>
            </ul>
          </div>
          <div class="footer-section">
            <h3>联系信息</h3>
            <ul>
              <li>
                <el-icon>
                  <Location />
                </el-icon>
                <span>江西科技师范大学（红角洲校区）</span>
              </li>
              <li>
                <el-icon>
                  <Message />
                </el-icon>
                <span>3555844679@qq.com</span>
              </li>
            </ul>
          </div>
          <div class="footer-section">
            <h3>关注我们</h3>
            <div class="social-links">
              <el-icon :size="24">
                <ChatDotRound />
              </el-icon>
              <el-icon :size="24">
                <Opportunity />
              </el-icon>
              <el-icon :size="24">
                <Link />
              </el-icon>
              <el-icon :size="24">
                <User />
              </el-icon>
            </div>
          </div>
        </div>
        <div class="footer-bottom">
          <p>&copy; 2025 Yangshengzhou. All Rights Reserved.</p>
        </div>
      </div>
    </footer>

  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import {
  Location, Message, ChatDotRound,
  Opportunity, Link, User
} from '@element-plus/icons-vue'
import { useRoute, useRouter } from 'vue-router';

const router = useRouter()
const route = useRoute();
const isHomePage = computed(() => route.path === '/');
const isLoggedIn = ref(false);

const handleLogin = () => {
  if (isLoggedIn.value) {
    const userRole = localStorage.getItem('userRole');
    switch (userRole) {
      case 'ADMIN':
        router.push('/admin');
        break;
      case 'STUDENT':
        router.push('/student');
        break;
      case 'TEACHER':
        router.push('/teacher');
        break;
      default:
        router.push('/student');
    }
  } else {
    router.push('/login');
  }
}

onMounted(() => {
  const token = localStorage.getItem('token');
  isLoggedIn.value = !!token;
  
  if (window.particlesJS) {
    window.particlesJS('particles-js', {
      particles: {
        number: { value: 80, density: { enable: true, value_area: 800 } },
        color: { value: "#ffffff" },
        shape: { type: "circle" },
        opacity: { value: 0.5, random: true },
        size: { value: 3, random: true },
        line_linked: { enable: true, distance: 150, color: "#ffffff", opacity: 0.4, width: 1 },
        move: { enable: true, speed: 2, direction: "none", random: true, straight: false, out_mode: "out" }
      },
      interactivity: {
        detect_on: "canvas",
        events: {
          onhover: { enable: true, mode: "grab" },
          onclick: { enable: true, mode: "push" }
        }
      }
    })
  }
})

onUnmounted(() => {
  if (window.pJSDom && window.pJSDom.length > 0) {
    window.pJSDom[0].pJS.fn.vendors.destroypJS()
    window.pJSDom = []
  }
})
</script>

<style>
:root {
  --primary-color: #3b82f6;
  --primary-light: #93c5fd;
  --primary-dark: #2563eb;
  --secondary-color: #64748b;
  --success-color: #10b981;
  --success-light: #6ee7b7;
  --warning-color: #f59e0b;
  --danger-color: #ef4444;
  --gray-50: #f8fafc;
  --gray-100: #f1f5f9;
  --gray-200: #e2e8f0;
  --gray-300: #cbd5e1;
  --gray-400: #94a3b8;
  --gray-500: #64748b;
  --gray-600: #475569;
  --gray-700: #334155;
  --gray-800: #1e293b;
  --gray-900: #0f172a;
  --white: #ffffff;
  --black: #000000;
  --shadow-sm: 0 1px 2px 0 rgb(0 0 0 / 0.05);
  --shadow-md: 0 4px 6px -1px rgb(0 0 0 / 0.1), 0 2px 4px -2px rgb(0 0 0 / 0.1);
  --shadow-lg: 0 10px 15px -3px rgb(0 0 0 / 0.1), 0 4px 6px -4px rgb(0 0 0 / 0.1);
  --shadow-xl: 0 20px 25px -5px rgb(0 0 0 / 0.1), 0 8px 10px -6px rgb(0 0 0 / 0.1);
  --border-radius: 4px;
  --transition: all 0.15s ease;
}

.app-container {
  font-family: 'Inter', -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
  line-height: 1.6;
  color: var(--gray-800);
  background-color: var(--white);
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  background-image: radial-gradient(circle at 1px 1px, var(--gray-100) 1px, transparent 0);
  background-size: 20px 20px;
}

.container {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 20px;
}

.section-title {
  font-size: clamp(1.8rem, 4vw, 2.5rem);
  font-weight: 700;
  color: var(--gray-900);
  text-align: center;
  margin-bottom: 60px;
  position: relative;
  background: linear-gradient(90deg, var(--primary-color), var(--success-color));
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  display: inline-block;
  padding: 0 20px;
}

.section-title::after {
  content: '';
  position: absolute;
  bottom: -12px;
  left: 50%;
  transform: translateX(-50%);
  width: 60px;
  height: 3px;
  background: linear-gradient(90deg, var(--primary-color), var(--success-color));
  border-radius: 2px;
}

.top-nav {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(15px);
  -webkit-backdrop-filter: blur(15px);
  padding: 16px 0;
  box-shadow: 
    0 4px 20px rgba(0, 0, 0, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.5);
  border-bottom: 1px solid rgba(255, 255, 255, 0.3);
  position: sticky;
  top: 0;
  z-index: 9999;
  width: 100%;
  left: 0;
  transition: all 0.3s ease;
}

.nav-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
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
}

.logo-badge {
  position: absolute;
  top: -8px;
  right: -24px;
  font-size: 10px;
  background: var(--success-color);
  color: var(--white);
  padding: 2px 6px;
  border-radius: 10px;
  font-weight: bold;
}

.nav-actions {
  display: flex;
  gap: 16px;
}

.main-login-btn {
  color: var(--primary-color) !important;
  font-weight: 500 !important;
  transition: var(--transition) !important;
  border: 1px solid var(--primary-light) !important;
  padding: 6px 16px !important;
  border-radius: 8px !important;
}

.main-login-btn:hover {
  background-color: var(--primary-light) !important;
  color: var(--primary-dark) !important;
  transform: translateY(-1px);
}

.main-register-btn {
  color: var(--white) !important;
  font-weight: 500 !important;
  transition: var(--transition) !important;
  border: 1px solid var(--primary-light) !important;
  padding: 6px 16px !important;
  border-radius: 8px !important;
}

.main-register-btn:hover {
  transform: scale(1.05);
  opacity: 0.9;
}

.hero {
  height: 90vh;
  min-height: 600px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--primary-color);
  text-align: center;
  padding: 0 20px;
  position: relative;
  overflow: hidden;
  border-bottom: 1px solid var(--primary-light);
}

.particles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
}

.hero-content {
  max-width: 800px;
  position: relative;
  z-index: 1;
  padding: 40px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(12px);
  border-radius: var(--border-radius);
  border: 1px solid rgba(255, 255, 255, 0.3);
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  transition: all 0.4s cubic-bezier(0.16, 0.77, 0.21, 0.99);
}

.hero-content:hover {
  transform: translateY(-1px);
  background: rgba(255, 255, 255, 0.15);
  box-shadow: 0 6px 24px rgba(0, 0, 0, 0.15);
  border-color: rgba(255, 255, 255, 0.4);
}

.hero-title {
  font-size: clamp(2rem, 5vw, 3.5rem);
  font-weight: 800;
  color: var(--white);
  margin-bottom: 24px;
  line-height: 1.1;
}

.hero-subtitle-accent {
  color: var(--success-light);
  white-space: nowrap;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
}

.hero-subtitle {
  font-size: clamp(1rem, 2vw, 1.2rem);
  color: var(--white);
  margin-bottom: 48px;
  line-height: 1.6;
  opacity: 0.9;
}

.hero-actions {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.hero-actions .el-button {
  padding: 12px 32px;
  font-size: 18px;
  border-radius: 8px;
  transition: all 0.2s ease;
  box-shadow: var(--shadow-md);
  position: relative;
  overflow: hidden;
  z-index: 1;
  min-width: 160px;
}

.hero-actions .el-button::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(255, 255, 255, 0.1);
  z-index: -1;
  transition: var(--transition);
}

.hero-actions .el-button:hover::after {
  background: rgba(255, 255, 255, 0.2);
  transform: scale(1.05);
}

.hero-actions .el-button:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-lg);
}

.glass-effect {
  background: rgba(255, 255, 255, 0.95);
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: var(--shadow-md);
}

.features {
  padding: 120px 0;
  background: var(--white);
  position: relative;
  border-top: 1px solid var(--gray-100);
  border-bottom: 1px solid var(--gray-100);
}

.features::before {
  content: '';
  position: absolute;
  top: -60px;
  left: 0;
  width: 100%;
  height: 120px;
  background: var(--white);
  transform: skewY(-3deg);
}

.features-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 40px;
}

.feature-item {
  perspective: 1000px;
  text-align: center;
  padding: 40px 32px;
  border-radius: var(--border-radius);
  background: rgba(255, 255, 255, 0.8);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  border: 1px solid rgba(255, 255, 255, 0.5);
  box-shadow: 
    0 8px 32px rgba(0, 0, 0, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.5);
  transition: all 0.4s cubic-bezier(0.16, 0.77, 0.21, 0.99);
  position: relative;
  overflow: hidden;
}

.feature-item:nth-child(1) {
  border-top: 3px solid var(--primary-color);
}

.feature-item:nth-child(2) {
  border-top: 3px solid var(--success-color);
}

.feature-item:nth-child(3) {
  border-top: 3px solid var(--warning-color);
}

.feature-content {
  position: relative;
  width: 100%;
  height: 100%;
}

.feature-item:hover {
  box-shadow: 
    0 8px 32px rgba(0, 0, 0, 0.15),
    inset 0 1px 0 rgba(255, 255, 255, 0.6);
  border-color: rgba(255, 255, 255, 0.6);
  background: rgba(255, 255, 255, 0.9);
}

.feature-icon {
  font-size: 36px;
  color: var(--primary-color);
  margin-bottom: 24px;
  width: 80px;
  height: 80px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24px;
  background: rgba(255, 255, 255, 0.6);
  backdrop-filter: blur(5px);
  -webkit-backdrop-filter: blur(5px);
  border-radius: 50%;
  transition: all 0.4s cubic-bezier(0.68, -0.55, 0.27, 1.55);
  border: 1px solid rgba(255, 255, 255, 0.5);
  box-shadow: 
    0 4px 15px rgba(0, 0, 0, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.5);
}

.feature-item:hover .feature-icon {
  transform: scale(1.05) rotate(5deg);
  background: var(--primary-color);
  color: var(--white);
  border-color: rgba(255, 255, 255, 0.6);
  box-shadow: 
    0 4px 15px rgba(59, 130, 246, 0.2),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
}

.feature-title {
  font-size: 20px;
  font-weight: 600;
  color: var(--gray-900);
  margin-bottom: 16px;
}

.feature-desc {
  color: var(--gray-600);
  line-height: 1.7;
}

.pricing {
  padding: 120px 0;
  background: var(--gray-50);
  position: relative;
  border-bottom: 1px solid var(--gray-200);
}

.pricing::before {
  content: '';
  position: absolute;
  top: -60px;
  left: 0;
  width: 100%;
  height: 120px;
  background: var(--white);
  transform: skewY(-3deg);
}

.pricing-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 40px;
  position: relative;
  z-index: 1;
}

.pricing-card-wrapper {
  position: relative;
}

.premium-card-wrapper::before {
  content: '推荐';
  position: absolute;
  top: -10px;
  right: 20px;
  background: var(--success-color);
  color: var(--white);
  padding: 6px 16px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 600;
  z-index: 2;
  box-shadow: var(--shadow-md);
}

.pricing-card {
  background: var(--white);
  border-radius: var(--border-radius);
  box-shadow: var(--shadow-md);
  border: 2px solid var(--gray-200);
  overflow: hidden;
  position: relative;
  z-index: 1;
  height: 100%;
}

.pricing-card-wrapper:nth-child(1) .pricing-card {
  border-top: 4px solid var(--primary-color);
}

.pricing-card-wrapper:nth-child(2) .pricing-card {
  border-top: 4px solid var(--warning-color);
}

.premium-card {
  border-top: 4px solid var(--success-color) !important;
  transform: scale(1.05);
  z-index: 2;
  box-shadow: var(--shadow-xl);
}

.pricing-card:hover {
  box-shadow: var(--shadow-xl);
  border-color: var(--gray-300);
}

.premium-card:hover {
  box-shadow: var(--shadow-xl);
}

.pricing-header {
  padding: 32px;
  background: var(--primary-color);
  color: var(--white);
  text-align: center;
  position: relative;
  overflow: hidden;
}

.pricing-card-wrapper:nth-child(2) .pricing-header {
  background: var(--warning-color);
}

.premium-card .pricing-header {
  background: var(--success-color);
}

.pricing-header::before {
  content: '';
  position: absolute;
  bottom: -20px;
  left: 50%;
  width: 40px;
  height: 40px;
  background: var(--white);
  transform: translateX(-50%) rotate(45deg);
}

.plan-title {
  font-size: 24px;
  font-weight: 6 00;
  margin-bottom: 16px;
  position: relative;
  z-index: 1;
}

.plan-price {
  font-size: 36px;
  font-weight: 700;
  position: relative;
  z-index: 1;
}

.price-amount {
  display: block;
}

.price-period {
  font-size: 16px;
  font-weight: 400;
  opacity: 0.8;
}

.plan-features {
  padding: 32px;
  padding-top: 52px;
}

.plan-features ul {
  list-style: none;
}

.plan-features ul li {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
  color: var(--gray-600);
}

.plan-features ul li .el-icon {
  color: var(--success-color);
  margin-right: 12px;
}

.plan-cta {
  padding: 0 32px 32px;
  text-align: center;
}

.plan-cta .el-button {
  width: 100%;
  padding: 10px 0;
  font-size: 16px;
  border-radius: 8px;
  transition: var(--transition);
  box-shadow: var(--shadow-sm);
}

.plan-cta .el-button:hover {
  transform: translateY(-2px);
  box-shadow: var(--shadow-md);
}

/* CTA部分样式 */
.cta-section {
  padding: 120px 0;
  background: var(--primary-color);
  position: relative;
  overflow: hidden;
  border-top: 1px solid var(--primary-light);
  border-bottom: 1px solid var(--primary-light);
}

.cta-section::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 1000 1000"><polygon fill="rgba(255,255,255,0.05)" points="0,1000 1000,0 1000,1000"/></svg>');
  background-size: cover;
}

.cta-content {
  position: relative;
  z-index: 1;
  text-align: center;
  max-width: 800px;
  margin: 0 auto;
  padding: 60px 40px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  border-radius: var(--border-radius);
  border: 1px solid rgba(255, 255, 255, 0.2);
  box-shadow: 
    0 8px 32px rgba(0, 0, 0, 0.1),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
  transition: all 0.4s cubic-bezier(0.16, 0.77, 0.21, 0.99);
}

.cta-content:hover {
  background: rgba(255, 255, 255, 0.15);
  border-color: rgba(255, 255, 255, 0.25);
  box-shadow: 
    0 8px 32px rgba(0, 0, 0, 0.15),
    inset 0 1px 0 rgba(255, 255, 255, 0.2);
  transform: translateY(-2px);
}

.cta-title {
  font-size: clamp(2rem, 4vw, 2.8rem);
  font-weight: 700;
  color: var(--white);
  margin-bottom: 24px;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.cta-subtitle {
  font-size: clamp(1rem, 2vw, 1.2rem);
  color: rgba(255, 255, 255, 0.9);
  margin-bottom: 48px;
  line-height: 1.6;
  text-shadow: 0 1px 2px rgba(0, 0, 0, 0.2);
}

.cta-actions {
  display: flex;
  justify-content: center;
  gap: 24px;
  flex-wrap: wrap;
}

.cta-btn-primary {
  padding: 16px 40px !important;
  font-size: 18px !important;
  border-radius: 12px !important;
  background: linear-gradient(135deg, var(--success-color) 0%, #0d9488 100%) !important;
  border: none !important;
  box-shadow: 
    0 4px 15px rgba(16, 185, 129, 0.4),
    inset 0 1px 0 rgba(255, 255, 255, 0.2) !important;
  transition: all 0.15s ease !important;
  position: relative;
  overflow: hidden;
}

.cta-btn-primary::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
  transition: left 0.6s ease;
}

.cta-btn-primary:hover::before {
  left: 100%;
}

.cta-btn-primary:hover {
  transform: translateY(-1px) scale(1.02) !important;
  box-shadow: 
    0 4px 15px rgba(16, 185, 129, 0.4),
    inset 0 1px 0 rgba(255, 255, 255, 0.2) !important;
}

.cta-btn-secondary {
  padding: 16px 40px !important;
  font-size: 18px !important;
  border-radius: 4px !important;
  background: rgba(255, 255, 255, 0.2) !important;
  color: var(--white) !important;
  border: 1px solid rgba(255, 255, 255, 0.3) !important;
  box-shadow: var(--shadow-sm);
}

.footer {
  background: var(--gray-900);
  color: var(--white);
  padding: 120px 0 32px;
  position: relative;
  margin-top: auto;
  border-top: 1px solid var(--gray-800);
}

.footer::before {
  content: '';
  position: absolute;
  top: -60px;
  left: 0;
  width: 100%;
  height: 120px;
  background: var(--white);
  transform: skewY(-3deg);
}

.footer-content {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 48px;
  margin-bottom: 48px;
}

.footer-section h3 {
  font-size: 20px;
  font-weight: 600;
  margin-bottom: 24px;
  color: var(--white);
  position: relative;
}

.footer-section h3::after {
  content: '';
  position: absolute;
  bottom: -8px;
  left: 0;
  width: 40px;
  height: 2px;
  background: var(--primary-color);
}

.footer-section ul {
  list-style: none;
}

.footer-section ul li {
  margin-bottom: 12px;
  color: var(--gray-400);
  cursor: pointer;
  transition: var(--transition);
  font-size: 16px;
  display: flex;
  align-items: center;
}

.footer-section ul li:hover {
  color: var(--white);
  transform: translateX(5px);
}

.footer-section ul li .el-icon {
  margin-right: 10px;
  width: 20px;
  text-align: center;
}

.social-links {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
}

.social-links .el-icon {
  cursor: pointer;
  transition: var(--transition);
  color: var(--gray-400);
}

.social-links .el-icon:hover {
  color: var(--white);
}

.newsletter {
  margin-top: 24px;
}

.footer-bottom {
  text-align: center;
  padding-top: 32px;
  border-top: 1px solid var(--gray-800);
  color: var(--gray-500);
  font-size: 16px;
}

@media (max-width: 1024px) {
  .hero-title {
    font-size: clamp(2rem, 5vw, 3rem);
  }

  .features-grid,
  .pricing-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .hero-content {
    padding: 30px;
  }
}

@media (max-width: 768px) {
  .container {
    padding: 0 1rem;
  }

  .hero-title {
    font-size: clamp(1.8rem, 5vw, 2.5rem);
  }

  .section-title {
    font-size: clamp(1.5rem, 4vw, 2rem);
  }

  .features-grid,
  .pricing-grid,
  .footer-content {
    grid-template-columns: 1fr;
    gap: 1.5rem;
  }

  .footer-content {
    text-align: center;
  }

  .premium-card-wrapper::before {
    top: -10px;
    right: 50%;
    transform: translateX(50%);
  }

  .premium-card {
    transform: scale(1);
  }

  .pricing-card:hover,
  .premium-card:hover {
    transform: translateY(-5px) scale(1.02);
  }

  .hero-actions {
    flex-direction: column;
    gap: 12px;
  }

  .hero-actions .el-button {
    width: 100%;
    min-width: auto;
  }

  .nav-actions {
    gap: 8px;
  }

  .hero-content {
    padding: 20px;
  }

  .feature-item {
    padding: 24px 16px;
  }

  .nav-logo {
    font-size: 1.2rem;
  }

  .logo-text {
    font-size: 20px;
  }

  .social-links {
    justify-content: center;
  }

  .footer-section h3::after {
    left: 50%;
    transform: translateX(-50%);
  }
}

@media (max-width: 480px) {
  .hero {
    height: auto;
    min-height: 500px;
    padding: 60px 10px;
  }

  .hero-content {
    padding: 16px;
  }

  .hero-title {
    font-size: clamp(1.6rem, 5vw, 2.2rem);
  }

  .feature-item {
    padding: 16px;
  }

  .feature-icon {
    width: 60px;
    height: 60px;
    font-size: 28px;
  }

  .pricing-card {
    margin-bottom: 2rem;
  }

  .pricing-header {
    padding: 24px 16px;
  }

  .plan-title {
    font-size: 20px;
  }

  .plan-price {
    font-size: 28px;
  }

  .plan-features {
    padding: 24px 16px;
  }

  .footer {
    padding: 80px 0 24px;
  }

  .footer-content {
    gap: 32px;
  }
}
</style>
