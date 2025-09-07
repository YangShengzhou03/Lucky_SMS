<template>
  <div class="login-page">
    <div class="particles" id="login-particles"></div>

    <div class="login-container">
      <div class="login-card">
        <div v-if="!showQRCode" class="qr-corner" @click="toggleQRCode" :class="{ active: showQRCode }">
          <img src="@/assets/images/login-qr-code.png" alt="扫码登录" class="qr-icon">
        </div>

        <transition name="fade">
          <div class="form-content" v-if="!showQRCode">
            <div class="card-header">
              <h2 class="welcome-text">欢迎回来</h2>
              <p class="login-subtitle">登录Lucky SMS学生管理系统</p>
            </div>

            <el-form ref="loginFormRef" :model="loginForm" :rules="loginRules" class="login-form"
              @keyup.enter="handleLogin">
              <el-form-item prop="username" class="form-item">
                <el-input v-model="loginForm.username" placeholder="输入用户名" size="large" :prefix-icon="User" clearable
                  class="custom-input" />
              </el-form-item>

              <el-form-item prop="password" class="form-item">
                <el-input v-model="loginForm.password" placeholder="输入密码" size="large" :prefix-icon="Lock" clearable
                  show-password class="custom-input" />
              </el-form-item>

              <div class="login-options">
                <el-checkbox v-model="loginForm.remember">记住我</el-checkbox>
                <el-link type="primary" :underline="false" class="forgot-password">
                  忘记密码?
                </el-link>
              </div>

              <el-button type="primary" size="large" class="login-btn" @click="handleLogin" :loading="loginLoading">
                <span v-if="!loginLoading">登录</span>
                <span v-else>登录中...</span>
              </el-button>

              <div class="register-link">
                没有账号?
                <el-link type="primary" @click="$router.push('/register')" :underline="false" class="register-text">
                  立即注册
                </el-link>
              </div>
            </el-form>
          </div>
        </transition>

        <transition name="fade">
          <div class="qr-content" v-if="showQRCode">
            <div class="qr-card">
              <div class="qr-code-container">
                <img src="@/assets/images/login-qr-code.svg" alt="扫码登录" class="qr-image">
              </div>

              <div class="qr-text-container">
                <h3>扫码登录</h3>
                <p>使用Lucky SMS APP扫描二维码</p>
              </div>

              <el-button type="text" class="back-btn" @click="toggleQRCode">
                <el-icon>
                  <ArrowLeft />
                </el-icon>
                返回账号登录
              </el-button>
            </div>
          </div>
        </transition>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessage } from 'element-plus';
import service from '@/utils/request';
import {
  User, Lock, ArrowLeft
} from '@element-plus/icons-vue';

const router = useRouter();
const showQRCode = ref(false);
const loginLoading = ref(false);

const loginForm = reactive({
  username: '',
  password: '',
  remember: false
});

const loginRules = reactive({
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度在6-20个字符之间', trigger: 'blur' }
  ]
});

const toggleQRCode = () => {
  showQRCode.value = !showQRCode.value;
};

const handleLogin = async () => {
  loginLoading.value = true;
  try {
    // 1. 转换字段名（保持原逻辑）
    const loginData = new URLSearchParams();
    loginData.append('username', loginForm.username); // 前端username对应后端username
    loginData.append('keyhash', loginForm.password); // 前端password对应后端keyhash

    // 2. 去掉.then()，用await直接接收响应（符合async/await语法）
    // 注意：service.post成功后返回的是后端的 { code, msg, data }（来自request.js封装）
    const res = await service.post('/login', loginData, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded' // 表单传参必备
      }
    });

    // 3. 直接判断res.code（无需多取一层data）
    if (res.code === 200) {
      ElMessage.success('登录成功！');
      // 4. 读取角色（res.data才是后端返回的业务数据）
      const role = res.data?.role; 
      if (role === 'admin') {
        router.push('/admin');
      } else if (role === 'student') {
        router.push('/student');
      } else if (role === 'teacher') {
        router.push('/teacher');
      } else {
        ElMessage.error('登录失败: 角色未定义');
      }
    } else {
      // 5. 错误信息读取res.msg（而非Response.message）
      ElMessage.error('登录失败: ' + (res.message || '用户名或密码错误'));
    }

  } catch (error) {
    // 6. catch捕获网络错误或后端500等异常（error.message来自request.js封装）
    ElMessage.error('登录失败: ' + (error.message || '网络异常，请稍后重试'));
  } finally {
    // 7. 无论成功/失败，都关闭loading
    loginLoading.value = false;
  }
};

onMounted(() => {
  if (window.particlesJS) {
    window.particlesJS('login-particles', {
      particles: {
        number: { value: 60, density: { enable: true, value_area: 800 } },
        color: { value: "#3b82f6" },
        shape: { type: "circle" },
        opacity: { value: 0.5, random: true },
        size: { value: 3, random: true },
        line_linked: {
          enable: true,
          distance: 150,
          color: "#3b82f6",
          opacity: 0.3,
          width: 1
        },
        move: {
          enable: true,
          speed: 2,
          direction: "none",
          random: true,
          straight: false,
          out_mode: "out"
        }
      },
      interactivity: {
        detect_on: "canvas",
        events: {
          onhover: { enable: true, mode: "grab" },
          onclick: { enable: true, mode: "push" }
        }
      }
    });
  }
});
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  position: relative;
  overflow: hidden;
  padding: 20px;
}

.particles {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 0;
}

.login-container {
  width: 100%;
  max-width: 420px;
  position: relative;
  z-index: 1;
}

.login-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  padding: 40px;
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.3);
  position: relative;
  overflow: hidden;
  min-height: 400px;
}

.login-card:hover {
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
}

.qr-corner {
  position: absolute;
  top: 0;
  right: 0;
  width: 36px;
  height: 36px;
  cursor: pointer;
  transition: all 0.3s ease;
  z-index: 20;
  background-color: white;
  border-radius: 0 0 0 8px;
  padding: 4px;
  box-shadow: -2px 2px 4px rgba(0, 0, 0, 0.1);
}

.qr-icon {
  width: 100%;
  height: 100%;
  object-fit: contain;
  transition: transform 0.3s ease;
}

.qr-corner:hover .qr-icon {
  transform: scale(1.1);
}

.qr-corner.active .qr-icon {
  filter: hue-rotate(90deg);
}

.card-header {
  text-align: center;
  margin-bottom: 28px;
}

.welcome-text {
  font-size: 24px;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 8px;
  background: linear-gradient(90deg, #3b82f6, #10b981);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.login-subtitle {
  font-size: 14px;
  color: #64748b;
}

.login-form {
  margin-top: 10px;
}

.form-item {
  margin-bottom: 20px;
}

.custom-input {
  border-radius: 10px;
}

.custom-input :deep(.el-input__wrapper) {
  padding: 0 15px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  border-radius: 10px;
  transition: all 0.3s ease;
}

.custom-input :deep(.el-input__wrapper:hover) {
  box-shadow: 0 4px 8px rgba(59, 130, 246, 0.2);
}

.custom-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #3b82f6 inset, 0 4px 8px rgba(59, 130, 246, 0.2);
}

.login-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.forgot-password {
  font-size: 14px;
}

.login-btn {
  width: 100%;
  padding: 14px 0;
  font-size: 16px;
  font-weight: 500;
  letter-spacing: 1px;
  background: linear-gradient(90deg, #3b82f6, #10b981);
  border: none;
  transition: all 0.3s ease;
  margin-top: 10px;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.3);
  color: white;
}

.login-btn:hover {
  background: linear-gradient(90deg, #2563eb, #0d9488);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(59, 130, 246, 0.4);
}

.login-btn:active {
  transform: translateY(0);
}

.register-link {
  text-align: center;
  margin-top: 24px;
  font-size: 14px;
  color: #64748b;
}

.register-text {
  font-weight: 500;
  margin-left: 4px;
  transition: all 0.2s ease;
}

.register-text:hover {
  transform: translateX(2px);
}

.qr-content {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: white;
  padding: 40px;
  box-sizing: border-box;
}

.qr-card {
  text-align: center;
  width: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 100%;
  justify-content: center;
}

.qr-code-container {
  width: 200px;
  height: 200px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f8fafc;
  border-radius: 12px;
  padding: 10px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
  margin-bottom: 20px;
}

.qr-image {
  width: 100%;
  height: 100%;
  object-fit: contain;
}

.qr-text-container {
  width: 100%;
  margin-bottom: 25px;
}

.qr-card h3 {
  font-size: 18px;
  margin-bottom: 8px;
  color: #1e293b;
  font-weight: 600;
}

.qr-card p {
  color: #64748b;
  font-size: 14px;
}

.back-btn {
  color: #64748b;
}

.back-btn:hover {
  color: #3b82f6;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

@media (max-width: 480px) {
  .login-card {
    padding: 30px 20px;
    border-radius: 12px;
  }

  .qr-code-container {
    width: 180px;
    height: 180px;
  }

  .qr-corner {
    width: 32px;
    height: 32px;
  }
}
</style>
