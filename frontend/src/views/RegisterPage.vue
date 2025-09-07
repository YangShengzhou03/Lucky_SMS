<template>
  <div class="register-page">
    <div class="particles" id="register-particles"></div>

    <div class="register-container">
      <div class="register-card">
        <div class="card-header">
          <h2 class="welcome-text">创建账户</h2>
          <p class="register-subtitle">加入Lucky SMS学生管理系统</p>
        </div>

        <el-form ref="registerFormRef" :model="registerForm" :rules="registerRules" class="register-form"
          @keyup.enter="handleRegister">
          <el-form-item prop="username" class="form-item">
            <el-input v-model="registerForm.username" placeholder="设置用户名" size="large" :prefix-icon="User" clearable
              class="custom-input" />
          </el-form-item>

          <el-form-item prop="phone" class="form-item">
            <el-input v-model="registerForm.phone" placeholder="输入手机号" size="large" :prefix-icon="Phone" clearable
              class="custom-input" maxlength="11" />
          </el-form-item>

          <el-form-item prop="captcha" class="form-item">
            <div class="captcha-container">
              <el-input v-model="registerForm.captcha" placeholder="输入验证码" size="large" :prefix-icon="Key"
                class="custom-input captcha-input" maxlength="6" />
              <el-button type="primary" size="large" class="captcha-btn" :disabled="captchaCooldown > 0"
                @click="sendCaptcha">
                {{ captchaBtnText }}
              </el-button>
            </div>
          </el-form-item>

          <el-form-item prop="agreement" class="form-item agreement-item">
            <el-checkbox v-model="registerForm.agreement">
              我已阅读并同意
              <el-link type="primary" :underline="false">《用户协议》</el-link>
              和
              <el-link type="primary" :underline="false">《隐私政策》</el-link>
            </el-checkbox>
          </el-form-item>

          <el-button type="primary" size="large" class="register-btn" @click="handleRegister"
            :loading="registerLoading">
            <span v-if="!registerLoading">立即注册</span>
            <span v-else>注册中...</span>
          </el-button>

          <div class="login-link">
            已有账号?
            <el-link type="primary" @click="$router.push('/login')" :underline="false" class="login-text">
              立即登录
            </el-link>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted, onUnmounted } from 'vue';
import { ElMessage } from 'element-plus';
import { User, Key, Phone } from '@element-plus/icons-vue';
import router from '@/router';
import service from '@/utils/request.js';

const registerForm = reactive({
  username: '',
  phone: '',
  captcha: '',
  agreement: false
});

const USERNAME_RULES = [
  { required: true, message: '请输入用户名', trigger: 'blur' },
  { min: 3, max: 20, message: '用户名长度在3-20个字符之间', trigger: 'blur' },
  { pattern: /^[a-zA-Z0-9_]+$/, message: '用户名只能包含字母、数字和下划线', trigger: 'blur' }
];
const PHONE_RULES = [
  { required: true, message: '请输入手机号', trigger: 'blur' },
  { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号格式', trigger: 'blur' }
];
const CAPTCHA_RULES = [
  { required: true, message: '请输入验证码', trigger: 'blur' },
  { len: 6, message: '验证码长度为6位', trigger: 'blur' }
];
const AGREEMENT_RULES = [
  {
    validator: (rule, value, callback) => {
      if (!value) callback(new Error('请同意用户协议和隐私政策'));
      else callback();
    },
    trigger: 'change'
  }
];

const registerRules = reactive({
  username: USERNAME_RULES,
  phone: PHONE_RULES,
  captcha: CAPTCHA_RULES,
  agreement: AGREEMENT_RULES
});

const registerFormRef = ref(null);
const registerLoading = ref(false);
const captchaCooldown = ref(0);
let captchaTimer = null;

const captchaBtnText = computed(() =>
  captchaCooldown.value > 0 ? `${captchaCooldown.value}秒后重试` : '获取验证码'
);

const sendCaptcha = () => {
  const phoneValid = PHONE_RULES.every(rule => {
    if (rule.required && !registerForm.phone) {
      ElMessage.warning(rule.message);
      return false;
    }
    if (rule.pattern && !rule.pattern.test(registerForm.phone)) {
      ElMessage.warning(rule.message);
      return false;
    }
    return true;
  });
  if (!phoneValid) return;

  captchaCooldown.value = 60;
  captchaTimer = setInterval(() => {
    captchaCooldown.value--;
    if (captchaCooldown.value <= 0) clearInterval(captchaTimer);
  }, 1000);

  ElMessage.success(`验证码已发送至 ${registerForm.phone}`);
};

// 提交注册表单
const handleRegister = async () => {
  registerLoading.value = true;

  const registerDate = new URLSearchParams();
  registerDate.append('username', registerForm.username);
  registerDate.append('phone', registerForm.phone);
  registerDate.append('captcha', registerForm.captcha);

  try {
    const res = await service.post('/register', registerForm, {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded' // 表单传参必备
      }
    });
    console.log(res);

    if (res.code==200) {
      ElMessage.success('注册成功！');
      router.push('/teacher')
    } else {
      ElMessage.error("注册失败，请稍后重试")
    }

  } catch (error) {
    ElMessage.error('注册失败: ' + (error.message || '请稍后再试'));
  } finally {
    registerLoading.value = false;
  }
};

onMounted(() => {
  if (window.particlesJS) {
    window.particlesJS('register-particles', {
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
.register-page {
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

.register-container {
  width: 100%;
  max-width: 420px;
  position: relative;
  z-index: 1;
}

.register-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 16px;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
  padding: 40px;
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.3);
}

.register-card:hover {
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.15);
  transform: translateY(-2px);
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
  background: linear-gradient(90deg, #10b981, #3b82f6);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
}

.register-subtitle {
  font-size: 14px;
  color: #64748b;
}

.register-form {
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
  box-shadow: 0 4px 8px rgba(16, 185, 129, 0.2);
}

.custom-input :deep(.el-input__wrapper.is-focus) {
  box-shadow: 0 0 0 1px #10b981 inset, 0 4px 8px rgba(16, 185, 129, 0.2);
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

.register-btn {
  width: 100%;
  padding: 14px 0;
  font-size: 16px;
  font-weight: 500;
  letter-spacing: 1px;
  background: linear-gradient(90deg, #10b981, #3b82f6);
  border: none;
  transition: all 0.3s ease;
  margin-top: 10px;
  box-shadow: 0 4px 12px rgba(16, 185, 129, 0.3);
}

.register-btn:hover {
  background: linear-gradient(90deg, #0d9488, #2563eb);
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(16, 185, 129, 0.4);
}

.register-btn:active {
  transform: translateY(0);
}

.login-link {
  text-align: center;
  margin-top: 24px;
  font-size: 14px;
  color: #64748b;
}

.login-text {
  font-weight: 500;
  margin-left: 4px;
  transition: all 0.2s ease;
}

.login-text:hover {
  transform: translateX(2px);
}

@media (max-width: 480px) {
  .register-card {
    padding: 30px 20px;
    border-radius: 12px;
  }

  .register-container {
    padding: 0 15px;
  }

  .welcome-text {
    font-size: 22px;
  }

  .captcha-container {
    flex-direction: column;
    gap: 12px;
  }

  .captcha-btn {
    width: 100%;
    padding: 12px 0;
  }

  .form-item {
    margin-bottom: 18px;
  }

  .register-btn {
    padding: 12px 0;
    font-size: 15px;
  }
}
</style>
