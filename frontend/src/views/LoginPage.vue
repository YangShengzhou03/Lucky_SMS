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
              <h2 class="welcome-text">{{ isResetMode ? '重置密码' : '欢迎回来' }}</h2>
              <p class="login-subtitle">{{ isResetMode ? '重置您的密码' : 'Lucky SMS学生管理系统' }}</p>
            </div>

            <!-- 手机号登录/注册表单 -->
            <el-form 
              v-if="loginMode === 'phone'" 
              ref="phoneFormRef" 
              :model="phoneForm" 
              :rules="phoneRules" 
              class="login-form"
              @keyup.enter="isResetMode ? handleReset() : handlePhoneLogin()"
            >
              <el-form-item prop="phone" class="form-item">
                <el-input v-model="phoneForm.phone" placeholder="输入手机号" size="large" :prefix-icon="Phone" clearable
                  class="custom-input" maxlength="11" @blur="validatePhone" />
              </el-form-item>

              <el-form-item prop="captcha" class="form-item">
                <div class="captcha-container">
                  <el-input v-model="phoneForm.captcha" placeholder="输入验证码" size="large" :prefix-icon="Key"
                    class="custom-input captcha-input" maxlength="6" />
                  <el-button type="primary" size="large" class="captcha-btn" :disabled="captchaCooldown > 0"
                    @click="sendCaptcha">
                    {{ captchaBtnText }}
                  </el-button>
                </div>
              </el-form-item>

              <!-- 重置密码显示密码输入框 -->
              <el-form-item prop="password" class="form-item" v-if="isResetMode">
                <el-input v-model="phoneForm.password" placeholder="设置新密码" size="large" :prefix-icon="Lock" clearable
                  show-password class="custom-input" />
              </el-form-item>

              <!-- 登录时账密登录选项 -->
              <el-form-item class="form-item" v-if="!isResetMode">
                <el-link type="primary" @click="loginMode = 'account'" :underline="false" class="alternative-link">
                  使用账号密码登录
                </el-link>
              </el-form-item>

              <el-button type="primary" size="large" class="login-btn" @click="isResetMode ? handleReset() : handlePhoneLogin()" 
                :loading="loginLoading">
                <span v-if="!loginLoading">{{ isResetMode ? '重置密码' : '登录/注册' }}</span>
                <span v-else>{{ '请稍等' }}</span>
              </el-button>

              <!-- 手机号登录模式下显示账号密码登录入口 -->
              <div class="alternative-login" v-if="!isResetMode">
                <el-divider>其他登录方式</el-divider>
                <div class="social-icons">
                  <el-button type="default" circle class="social-icon">
                    <el-icon><ChatDotRound /></el-icon>
                  </el-button>
                  <el-button type="default" circle class="social-icon">
                    <el-icon><Iphone /></el-icon>
                  </el-button>
                </div>
              </div>
            </el-form>

            <!-- 账号密码登录表单 -->
            <el-form 
              v-if="loginMode === 'account'" 
              ref="loginFormRef" 
              :model="loginForm" 
              :rules="loginRules" 
              class="login-form"
              @keyup.enter="handleLogin"
            >
              <el-form-item prop="username" class="form-item">
                <el-input v-model="loginForm.username" placeholder="输入用户名" size="large" :prefix-icon="User" clearable
                  class="custom-input" />
              </el-form-item>

              <el-form-item prop="password" class="form-item">
                <el-input v-model="loginForm.password" placeholder="输入密码" size="large" :prefix-icon="Lock" clearable
                  show-password class="custom-input" />
              </el-form-item>

              <div class="login-options">
                <el-link type="primary" @click="loginMode = 'phone'" :underline="false" class="register-text">
                  立即注册
                </el-link>
                <el-link type="primary" :underline="false" class="forgot-password" @click="isResetMode = true; loginMode = 'phone'">
                  忘记密码?
                </el-link>
              </div>

              <el-button type="primary" size="large" class="login-btn" @click="handleLogin" :loading="loginLoading">
                <span v-if="!loginLoading">登录</span>
                <span v-else>登录中...</span>
              </el-button>
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
import { ref, reactive, computed, onMounted, onUnmounted } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { ElMessage } from 'element-plus';
import service from '@/utils/request';
import {
  User, Lock, ArrowLeft, Phone, Key
} from '@element-plus/icons-vue';

const router = useRouter();
const route = useRoute();
const showQRCode = ref(false);
const loginLoading = ref(false);
const loginMode = ref('phone'); // 'phone' 或 'account'
const isResetMode = ref(false); // 是否处于注册模式

// 验证码倒计时
const captchaCooldown = ref(0);
let captchaTimer = null;

const captchaBtnText = computed(() =>
  captchaCooldown.value > 0 ? `${captchaCooldown.value}秒后重试` : '获取验证码'
);

// 账号密码登录表单
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

// 手机号登录/注册表单
const phoneForm = reactive({
  phone: '',
  captcha: '',
  username: '', // 注册时使用
  agreement: false // 注册时使用
});

const phoneRules = reactive({
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
  ],
  captcha: [
    { required: true, message: '请输入验证码', trigger: 'blur' },
    { len: 6, message: '验证码长度为6位', trigger: 'blur' }
  ],
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在3-20个字符之间', trigger: 'blur' },
    { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线', trigger: 'blur' }
  ],
  agreement: [
    {
      validator: (rule, value, callback) => {
        if (!value) callback(new Error('请同意用户协议和隐私政策'));
        else callback();
      },
      trigger: 'change'
    }
  ]
});

// 表单引用
const loginFormRef = ref(null);
const phoneFormRef = ref(null);

// 切换二维码登录
const toggleQRCode = () => {
  showQRCode.value = !showQRCode.value;
};

// 验证手机号格式
const validatePhone = () => {
  if (!phoneForm.phone) {
    ElMessage.warning('请输入手机号');
    return false;
  }
  if (!/^1[3-9]\d{9}$/.test(phoneForm.phone)) {
    ElMessage.warning('请输入正确的手机号格式');
    return false;
  }
  return true;
};

// 发送验证码（手机号登录/注册）
const sendCaptcha = async () => {
  if (!validatePhone()) return;
  
  if (captchaCooldown.value > 0) {
    ElMessage.warning(`请等待${captchaCooldown.value}秒后重试`);
    return;
  }

  try {
    // 这里应该调用发送验证码的API
    // const res = await service.post('/captcha/send', { phone: phoneForm.phone });
    
    // 模拟发送验证码
    captchaCooldown.value = 60;
    captchaTimer = setInterval(() => {
      captchaCooldown.value--;
      if (captchaCooldown.value <= 0) {
        clearInterval(captchaTimer);
        captchaTimer = null;
      }
    }, 1000);

    ElMessage.success(`验证码已发送至 ${phoneForm.phone}`);
  } catch (error) {
    ElMessage.error('验证码发送失败，请稍后重试');
  }
};

// 处理登录成功后的路由跳转
const handleLoginSuccess = (role) => {
  ElMessage.success('登录成功！');
  switch (role) {
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
      ElMessage.error('登录失败: 角色未定义');
      break;
  }
};

// 账号密码登录
const handleLogin = async () => {
  if (!loginFormRef.value) return;
  
  try {
    await loginFormRef.value.validate();
    loginLoading.value = true;
    
    const loginData = new URLSearchParams();
    loginData.append('username', loginForm.username);
    loginData.append('keyhash', loginForm.password);

    const res = await service.post('/login', loginData, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    });

    if (res.code === 200) {
      handleLoginSuccess(res.data?.role);
    } else {
      ElMessage.error('登录失败: ' + (res.message || '用户名或密码错误'));
    }
  } catch (error) {
    ElMessage.error('登录失败: ' + (error.message || '网络异常，请稍后重试'));
  } finally {
    loginLoading.value = false;
  }
};

// 手机号登录
const handlePhoneLogin = async () => {
  if (!phoneFormRef.value) return;
  
  try {
    await phoneFormRef.value.validate();
    loginLoading.value = true;
    
    const loginData = new URLSearchParams();
    loginData.append('phone', phoneForm.phone);
    loginData.append('captcha', phoneForm.captcha);

    const res = await service.post('/login/phone', loginData, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    });

    if (res.code === 200) {
      handleLoginSuccess(res.data?.role);
    } else {
      ElMessage.error('登录失败: ' + (res.message || '手机号或验证码错误'));
    }
  } catch (error) {
    ElMessage.error('登录失败: ' + (error.message || '网络异常，请稍后重试'));
  } finally {
    loginLoading.value = false;
  }
};

// 重置密码
const handleReset = async () => {
  if (!phoneFormRef.value) return;
  
  try {
    await phoneFormRef.value.validate();
    loginLoading.value = true;
    
    const registerData = new URLSearchParams();
    registerData.append('username', phoneForm.username);
    registerData.append('phone', phoneForm.phone);
    registerData.append('captcha', phoneForm.captcha);

    const res = await service.post('/register', registerData, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    });

    if (res.code === 200) {
      ElMessage.success('注册成功！');
      // 注册成功后切换到登录模式
      isResetMode.value = false;
      // 清空表单
      phoneForm.captcha = '';
      phoneForm.username = '';
      phoneForm.agreement = false;
    } else {
      ElMessage.error('注册失败: ' + (res.message || '请稍后重试'));
    }
  } catch (error) {
    ElMessage.error('注册失败: ' + (error.message || '网络异常，请稍后重试'));
  } finally {
    loginLoading.value = false;
  }
};

onMounted(() => {
  // 确保在页面刷新时，如果当前路径是/login，则显示登录页面
  if (route.path === '/login') {
    // 这里可以添加任何需要在登录页面加载时执行的逻辑
    // 例如，检查用户是否已经登录，如果已登录则重定向到相应页面
  }
  
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

onUnmounted(() => {
  if (captchaTimer) clearInterval(captchaTimer);
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

.login-mode-switch {
  margin-bottom: 24px;
}

.mode-tabs {
  display: flex;
  border-radius: 10px;
  background: #f1f5f9;
  padding: 4px;
}

.mode-tab {
  flex: 1;
  text-align: center;
  padding: 10px 0;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 14px;
  font-weight: 500;
  color: #64748b;
}

.mode-tab.active {
  background: white;
  color: #3b82f6;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
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

.captcha-container {
  display: flex;
  gap: 10px;
}

.captcha-input {
  flex: 1;
}

.captcha-btn {
  flex-shrink: 0;
  width: 120px;
  padding: 0;
}

.captcha-btn:disabled {
  background: #e2e8f0 !important;
  color: #94a3b8 !important;
  cursor: not-allowed;
}

.agreement-item :deep(.el-checkbox__label) {
  font-size: 14px;
  color: #64748b;
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

.alternative-login {
  margin-top: 24px;
  text-align: center;
}

.alternative-link {
  font-size: 14px;
  font-weight: 500;
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

/* 忘记密码对话框样式 */
:deep(.el-dialog) {
  border-radius: 12px;
}

:deep(.el-dialog__header) {
  padding-top: 20px;
}

:deep(.el-dialog__body) {
  padding: 20px 30px 30px;
}

:deep(.el-form-item__label) {
  font-weight: 500;
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

  .captcha-container {
    flex-direction: column;
    gap: 12px;
  }

  .captcha-btn {
    width: 100%;
    padding: 12px 0;
  }
}
</style>
