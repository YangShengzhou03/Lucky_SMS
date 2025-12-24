<template>
  <div class="login-page">
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

            <el-form v-if="loginMode === 'phone'" ref="phoneFormRef" :model="phoneForm" :rules="phoneRules"
              class="login-form" @keyup.enter="isResetMode ? handleReset() : handlePhoneLogin()">
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

              <el-form-item prop="password" class="form-item" v-if="isResetMode">
                <el-input v-model="phoneForm.newPassword" placeholder="设置新密码" size="large" :prefix-icon="Lock" clearable
                  show-password class="custom-input" />
              </el-form-item>

              <el-form-item class="form-item" v-if="!isResetMode">
                <el-link type="primary" @click="loginMode = 'account'" :underline="false" class="alternative-link">
                  使用账号密码登录
                </el-link>
              </el-form-item>

              <el-button type="primary" size="large" class="login-btn"
                @click="isResetMode ? handleReset() : handlePhoneLogin()" :loading="loginLoading">
                <span v-if="!loginLoading">{{ isResetMode ? '重置密码' : '登录/注册' }}</span>
                <span v-else>{{ '请稍等' }}</span>
              </el-button>

              <div class="alternative-login" v-if="!isResetMode">
                <el-divider>其他登录方式</el-divider>
                <div class="social-icons">
                  <el-button type="default" circle class="social-icon">
                    <el-icon>
                      <ChatDotRound />
                    </el-icon>
                  </el-button>
                  <el-button type="default" circle class="social-icon">
                    <el-icon>
                      <Iphone />
                    </el-icon>
                  </el-button>
                </div>
              </div>
            </el-form>

            <el-form v-if="loginMode === 'account'" ref="loginFormRef" :model="loginForm" :rules="loginRules"
              class="login-form" @keyup.enter="handleLogin">
              <el-form-item prop="phone" class="form-item">
                <el-input v-model="loginForm.phone" placeholder="输入手机号" size="large" :prefix-icon="User" clearable
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
                <el-link type="primary" :underline="false" class="forgot-password"
                  @click="isResetMode = true; loginMode = 'phone'">
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
  phone: '',
  password: '',
});

const loginRules = reactive({
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
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
  newPassword: ''
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

const toggleQRCode = () => {
  showQRCode.value = !showQRCode.value;
};

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

const sendCaptcha = async () => {
  if (!validatePhone()) return;

  if (captchaCooldown.value > 0) {
    ElMessage.warning(`请等待${captchaCooldown.value}秒后重试`);
    return;
  }

  try {
      captchaCooldown.value = 60;

    phoneForm.captcha = '123456';

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

const handleLoginSuccess = (role, token) => {
  if (token) {
    localStorage.setItem('token', token);
  }

  if (role) {
    localStorage.setItem('userRole', role);
  }

  ElMessage.success('登录成功！');
  switch (role) {
    case 'ADMIN':
      router.push('/student'); // 管理员暂时重定向到学生页面，后续可添加管理员页面
      break;
    case 'STUDENT':
      router.push('/student');
      break;
    case 'TEACHER':
      router.push('/teacher');
      break;
    case 'COUNSELOR': // 辅导员
      router.push('/teacher'); // 辅导员使用教师页面
      break;
    case 'DEAN': // 院长/系主任
      router.push('/teacher'); // 院系领导使用教师页面
      break;
    default:
      ElMessage.error('登录失败: 角色未定义');
      break;
  }
};

const handleLogin = async () => {
  if (!loginFormRef.value) return;

  try {
    await loginFormRef.value.validate();
    loginLoading.value = true;

    const loginData = new URLSearchParams();
    loginData.append('phone', loginForm.phone);
    loginData.append('password', loginForm.password);

    const res = await service.post('/login/password', loginData, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    });

    if (res.code === 200) {
      handleLoginSuccess(res.data?.role, res.data?.token);
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
    // 使用 validate 方法的回调形式来处理验证结果
    const isValid = await new Promise((resolve) => {
      phoneFormRef.value.validate((valid) => {
        resolve(valid);
      });
    });

    if (!isValid) {
      ElMessage.error('请填写完整的表单信息');
      return;
    }

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
      handleLoginSuccess(res.data?.role, res.data?.token);
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
    const isValid = await new Promise((resolve) => {
      phoneFormRef.value.validate((valid) => {
        resolve(valid);
      });
    });

    if (!isValid) {
      ElMessage.error('请填写完整的表单信息');
      return;
    }

    loginLoading.value = true;

    const resetData = new URLSearchParams();
    resetData.append('phone', phoneForm.phone);
    resetData.append('captcha', phoneForm.captcha);
    resetData.append('newPassword', phoneForm.newPassword);

    const res = await service.post('/resetPassword', resetData, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded'
      }
    });

    if (res.code === 200) {
      // 成功后延迟一下切换到登录模式
      setTimeout(() => {
        isResetMode.value = false;
        phoneForm.captcha = '';
      }, 1500);

      ElMessage.success('密码重置成功！');
    } else {
      ElMessage.error('密码重置失败: ' + (res.message || '请稍后重试'));
    }
  } catch (error) {
    ElMessage.error('密码重置失败: ' + (error.message || '网络异常，请稍后重试'));
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
  background: #f8fafc;
  padding: 20px;
}

.login-container {
  width: 100%;
  max-width: 420px;
}

.login-card {
  background: white;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  padding: 40px;
  position: relative;
  min-height: 400px;
}

.qr-corner {
  position: absolute;
  top: 0;
  right: 0;
  width: 36px;
  height: 36px;
  cursor: pointer;
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
  background: #3b82f6;
  border: none;
  margin-top: 10px;
  color: white;
  transition: all 0.3s ease;
  cursor: pointer;
}

.login-btn:hover {
  background: #2563eb;
  transform: translateY(-1px);
  box-shadow: 0 4px 8px rgba(37, 99, 235, 0.3);
}

.login-btn:active {
  background: #1d4ed8;
  transform: translateY(0);
  box-shadow: 0 2px 4px rgba(37, 99, 235, 0.3);
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
  border-radius: 8px;
  padding: 10px;
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

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}

/* 忘记密码对话框样式 */
:deep(.el-dialog) {
  border-radius: 8px;
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
