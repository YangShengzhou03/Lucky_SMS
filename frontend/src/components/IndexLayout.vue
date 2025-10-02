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
            <el-button type="text" size="small" class="main-login-btn" @click="handleLogin">
              <span class="btn-text">登录/注册</span>
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <div v-if="isHomePage">
      <main>
        <section id="home" class="hero">
          <div class="particles" id="particles-js"></div>
          <div class="hero-content">
            <div class="hero-text">
              <h1 class="hero-title">
                <span class="title-line">智能学生管理系统</span>
                <span class="hero-subtitle-accent">创新科技，引领未来</span>
              </h1>
              <p class="hero-subtitle">高效、便捷的教育服务解决方案</p>
              <div class="hero-actions">
                <el-button type="primary" size="large" @click="scrollToSection('features')">
                  了解功能
                </el-button>
                <el-button type="default" size="large" @click="scrollToSection('pricing')">
                  查看价格
                </el-button>
              </div>
            </div>
          </div>
        </section>

        <section id="features" class="features">
          <div class="container">
            <h2 class="section-title">核心功能</h2>
            <div class="features-grid">
              <div class="feature-item" v-for="feature in features" :key="feature.id">
                <div class="feature-content">
                  <div class="feature-icon">
                    <el-icon>
                      <component :is="feature.icon" />
                    </el-icon>
                  </div>
                  <h3 class="feature-title">{{ feature.title }}</h3>
                  <p class="feature-desc">{{ feature.description }}</p>
                </div>
              </div>
            </div>
          </div>
        </section>

        <section id="pricing" class="pricing">
          <div class="container">
            <h2 class="section-title">价格方案</h2>
            <div class="pricing-grid">
              <div class="pricing-card-wrapper" :class="{ 'premium-card-wrapper': plan.id === 3 }"
                v-for="plan in pricingPlans" :key="plan.id">
                <div class="pricing-card" :class="{ 'premium-card': plan.id === 3 }">
                  <div class="pricing-header">
                    <h3 class="plan-title">{{ plan.title }}</h3>
                    <div class="plan-price">
                      <span class="price-amount">{{ plan.price }}</span>
                      <span class="price-period">{{ plan.period }}</span>
                    </div>
                  </div>
                  <div class="plan-features">
                    <ul>
                      <li v-for="feature in plan.features" :key="feature">
                        <el-icon>
                          <Check />
                        </el-icon>
                        {{ feature }}
                      </li>
                    </ul>
                  </div>
                  <div class="plan-cta">
                    <el-button :type="plan.id === 3 ? 'success' : 'primary'" size="medium">
                      选择方案
                    </el-button>
                  </div>
                </div>
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
              <li @click="scrollToSection('home')">Lucky-SMS</li>
              <li @click="scrollToSection('features')">功能服务</li>
              <li @click="scrollToSection('pricing')">价格方案</li>
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
                  <Phone />
                </el-icon>
                <span>123-456-7890</span>
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
            <div class="newsletter">
              <el-input placeholder="您的邮箱地址" size="small">
                <template #append>
                  <el-button icon="ArrowRight" />
                </template>
              </el-input>
            </div>
          </div>
        </div>
        <div class="footer-bottom">
          <p>&copy; 2025 Yangshengzhou. All Rights Reserved.</p>
        </div>
      </div>
    </footer>

    <transition name="fade">
      <el-button v-show="showBackToTop" class="back-to-top" type="primary" :icon="ArrowUp" @click="scrollToTop" />
    </transition>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, watch } from 'vue'
import {
  Position, MostlyCloudy, Lock, ArrowUp, Check,
  Location, Phone, Message, ChatDotRound,
  Opportunity, Link, User
} from '@element-plus/icons-vue'
import { useRoute, useRouter } from 'vue-router';

const router = useRouter()
const route = useRoute();
const showBackToTop = ref(false)
const isHomePage = ref(true);

watch(
  () => route.path,
  (newPath) => {
    isHomePage.value = newPath === '/';
  }
);

const features = [
  {
    id: 1,
    icon: Position,
    title: '秒级响应',
    description: '百万级并发处理能力，信息数据秒级送达'
  },
  {
    id: 2,
    icon: MostlyCloudy,
    title: '云端服务',
    description: '分布式架构设计，99.99%系统可用性'
  },
  {
    id: 3,
    icon: Lock,
    title: '安全可靠',
    description: '金融级安全防护，数据加密传输'
  }
]

const pricingPlans = [
  {
    id: 1,
    title: '基础版',
    price: '¥99',
    period: '/月',
    features: [
      '十万级学生容量',
      '基础API接口',
      '邮件技术支持',
      '基础数据分析'
    ]
  },
  {
    id: 2,
    title: '企业版',
    price: '¥299',
    period: '/月',
    features: [
      '百万级学生容量',
      '高级API接口',
      '24/7技术支持',
      '高级数据分析',
      '自定义模板'
    ]
  },
  {
    id: 3,
    title: '旗舰版',
    price: '¥899',
    period: '/月',
    features: [
      '千万级学生容量',
      'VIP API接口',
      '专属客户经理',
      '高级安全保障',
      '定制化解决方案'
    ]
  }
]

const scrollToSection = (sectionId) => {
  const element = document.getElementById(sectionId)
  if (element) {
    window.scrollTo({
      top: element.offsetTop - 80,
      behavior: 'smooth'
    })
  }
}

const scrollToTop = () => {
  window.scrollTo({
    top: 0,
    behavior: 'smooth'
  })
}

const handleScroll = () => {
  const scrollTop = window.pageYOffset || document.documentElement.scrollTop
  showBackToTop.value = scrollTop > 300
}

const handleLogin = () => {
  router.push('/login')
}

onMounted(() => {
  window.addEventListener('scroll', handleScroll)
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
  window.removeEventListener('scroll', handleScroll)
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
  --border-radius: 12px;
  --transition: all 0.3s ease;
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
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  padding: 16px 0;
  box-shadow: var(--shadow-md);
  border-bottom: 1px solid var(--gray-100);
  position: sticky;
  top: 0;
  z-index: 9999;
  width: 100%;
  left: 0;
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
  text-shadow: 0 2px 4px rgba(59, 130, 246, 0.2);
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
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 100%);
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
  transform: translateY(-6px);
  background: rgba(255, 255, 255, 0.15);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.15),
    0 0 15px rgba(255, 255, 255, 0.2);
  border-color: rgba(255, 255, 255, 0.5);
}

.hero-title {
  font-size: clamp(2rem, 5vw, 3.5rem);
  font-weight: 800;
  color: var(--white);
  margin-bottom: 24px;
  line-height: 1.1;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
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
  box-shadow: var(--shadow-md);
  background: var(--white);
  border: 1px solid var(--gray-200);
  transition: var(--transition);
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
  transition: transform 0.6s;
  transform-style: preserve-3d;
}

.feature-item:hover .feature-content {
  transform: rotateY(10deg);
}

.feature-item:hover {
  box-shadow: var(--shadow-xl);
  transform: translateY(-5px);
  border-color: var(--gray-300);
}

.feature-icon {
  font-size: 36px;
  color: var(--primary-color);
  margin-bottom: 24px;
  width: 72px;
  height: 72px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 24px;
  background: var(--gray-100);
  border-radius: 50%;
  transition: all 0.4s cubic-bezier(0.68, -0.55, 0.27, 1.55);
  border: 1px solid var(--gray-200);
}

.feature-item:hover .feature-icon {
  transform: scale(1.1) rotate(5deg);
  background: var(--primary-color);
  color: var(--white);
  border-color: transparent;
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
  border-radius: 20px;
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
  transition: all 0.4s cubic-bezier(0.25, 0.8, 0.25, 1);
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
  transform: translateY(-5px) scale(1.02);
  border-color: var(--gray-300);
}

.premium-card:hover {
  transform: translateY(-5px) scale(1.07);
}

.pricing-header {
  padding: 32px;
  background: linear-gradient(135deg, var(--primary-color) 0%, var(--primary-dark) 100%);
  color: var(--white);
  text-align: center;
  position: relative;
  overflow: hidden;
}

.pricing-card-wrapper:nth-child(2) .pricing-header {
  background: linear-gradient(135deg, var(--warning-color) 0%, #d97706 100%);
}

.premium-card .pricing-header {
  background: linear-gradient(135deg, var(--success-color) 0%, #0d9488 100%);
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

.pricing-header::after {
  content: '';
  position: absolute;
  top: -50%;
  left: -50%;
  width: 200%;
  height: 200%;
  background: linear-gradient(to bottom right,
      rgba(255, 255, 255, 0) 0%,
      rgba(255, 255, 255, 0) 45%,
      rgba(255, 255, 255, 0.15) 48%,
      rgba(255, 255, 255, 0) 50%,
      rgba(255, 255, 255, 0) 100%);
  transform: rotate(30deg);
  animation: shine 3s infinite;
}

@keyframes shine {
  0% {
    transform: translateX(-100%) rotate(30deg);
  }

  100% {
    transform: translateX(100%) rotate(30deg);
  }
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
  transform: translateY(-3px);
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

.back-to-top {
  position: fixed;
  bottom: 40px;
  right: 40px;
  z-index: 1000;
  width: 56px;
  height: 56px;
  border-radius: 50%;
  box-shadow: var(--shadow-xl);
  opacity: 0.9;
  transition: var(--transition);
  background-color: var(--primary-color) !important;
}

.back-to-top:hover {
  opacity: 1;
  transform: translateY(-5px);
  background-color: var(--primary-light) !important;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@media (max-width: 768px) {
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
  }

  .nav-actions {
    gap: 8px;
  }

  .hero-content {
    padding: 20px;
  }
}
</style>
