<template>
  <div class="personal-center-page" @mousemove="handleMouseMove">
    <div class="personal-center-content">
      <!-- 个人信息卡片 -->
      <div class="personal-info-card modern-card" :class="{ 'panel-hover': isHoveringPanel }">
        <div class="card-header">
          <div class="avatar-section">
            <el-upload class="avatar-uploader" action="#" :show-file-list="false" :on-success="handleAvatarSuccess"
              :before-upload="beforeAvatarUpload">
              <img v-if="userProfile.avatarUrl" :src="userProfile.avatarUrl" class="avatar" />
              <el-icon v-else class="avatar-uploader-icon">
                <Plus />
              </el-icon>
            </el-upload>
            <div class="user-info">
              <h2 class="username">{{ userProfile.name }}</h2>
              <p class="student-id">学号: {{ userProfile.studentId }}</p>
            </div>
          </div>
          <el-button type="primary" size="small" @click="openEditDialog" class="edit-button" round>
            <el-icon>
              <Edit />
            </el-icon> 编辑资料
          </el-button>
        </div>

        <div class="card-body">
          <div class="info-group">
            <h3 class="info-title">基本信息</h3>
            <div class="info-grid">
              <div class="info-item">
                <el-icon class="item-icon">
                  <User />
                </el-icon>
                <div class="item-content">
                  <p class="item-label">性别</p>
                  <p class="item-value">{{ getGenderText(userProfile.gender) }}</p>
                </div>
              </div>

              <div class="info-item">
                <el-icon class="item-icon">
                  <Calendar />
                </el-icon>
                <div class="item-content">
                  <p class="item-label">出生日期</p>
                  <p class="item-value">{{ formatDate(userProfile.birthdate) }}</p>
                </div>
              </div>

              <div class="info-item">
                <el-icon class="item-icon">
                  <Notification />
                </el-icon>
                <div class="item-content">
                  <p class="item-label">邮箱</p>
                  <p class="item-value">{{ userProfile.email }}</p>
                </div>
              </div>

              <div class="info-item">
                <el-icon class="item-icon">
                  <Phone />
                </el-icon>
                <div class="item-content">
                  <p class="item-label">手机号码</p>
                  <p class="item-value">{{ userProfile.phone }}</p>
                </div>
              </div>
            </div>
          </div>

          <div class="info-group">
            <h3 class="info-title">教育信息</h3>
            <div class="info-grid">
              <div class="info-item">
                <el-icon class="item-icon">
                  <School />
                </el-icon>
                <div class="item-content">
                  <p class="item-label">学院</p>
                  <p class="item-value">{{ userProfile.college }}</p>
                </div>
              </div>

              <div class="info-item">
                <el-icon class="item-icon">
                  <Collection />
                </el-icon>
                <div class="item-content">
                  <p class="item-label">班级</p>
                  <p class="item-value">{{ userProfile.class }}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 快捷操作卡片 -->
      <div class="quick-actions-card modern-card">
        <div class="card-title">
          <el-icon class="title-icon">
            <Setting />
          </el-icon>
          <span>账户设置</span>
        </div>

        <div class="actions-grid">
          <div v-for="action in quickActions" :key="action.name" class="action-item" @click="action.handler">
            <div class="action-content">
              <el-icon class="action-icon">
                <component :is="action.icon" />
              </el-icon>
              <span>{{ action.name }}</span>
            </div>
            <el-icon class="arrow-icon">
              <ArrowRight />
            </el-icon>
          </div>
        </div>
      </div>
    </div>

    <!-- 编辑个人信息对话框 -->
    <el-dialog v-model="editDialogVisible" title="编辑个人信息" width="600px" :before-close="handleDialogClose">
      <el-form ref="editFormRef" :model="editForm" :rules="editFormRules" label-width="100px">
        <el-form-item label="姓名" prop="name">
          <el-input v-model="editForm.name" placeholder="请输入姓名" />
        </el-form-item>

        <el-form-item label="性别" prop="gender">
          <el-radio-group v-model="editForm.gender">
            <el-radio label="male">男</el-radio>
            <el-radio label="female">女</el-radio>
            <el-radio label="other">其他</el-radio>
          </el-radio-group>
        </el-form-item>

        <el-form-item label="出生日期" prop="birthdate">
          <el-date-picker v-model="editForm.birthdate" type="date" placeholder="选择出生日期" value-format="YYYY-MM-DD"
            style="width: 100%" />
        </el-form-item>

        <el-form-item label="邮箱" prop="email">
          <el-input v-model="editForm.email" placeholder="请输入邮箱地址" />
        </el-form-item>

        <el-form-item label="手机号码" prop="phone">
          <el-input v-model="editForm.phone" placeholder="请输入手机号码" />
        </el-form-item>

        <!-- 移除不允许修改的字段：学院和班级 -->
      </el-form>

      <template #footer>
        <span class="dialog-footer">
          <el-button @click="handleDialogClose">取消</el-button>
          <el-button type="primary" @click="submitEditForm" :loading="submitLoading">
            保存
          </el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import {
  Plus, User, Edit, ArrowRight, Lock,
  Calendar, Notification, Phone, Setting, Bell,
  FirstAidKit, QuestionFilled, School, Collection
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import service from '@/utils/request'

// 编辑对话框相关状态
const editDialogVisible = ref(false)
const submitLoading = ref(false)
const editFormRef = ref()

// 编辑表单数据
const editForm = reactive({
  name: '',
  gender: 'male',
  birthdate: '',
  email: '',
  phone: ''
})

// 表单验证规则
const editFormRules = {
  gender: [
    { required: true, message: '请选择性别', trigger: 'change' }
  ],
  birthdate: [
    { required: true, message: '请选择出生日期', trigger: 'change' }
  ],
  email: [
    { required: true, message: '请输入邮箱地址', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号码', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号码', trigger: 'blur' }
  ]
}

// 首先定义所有函数
const openEditDialog = () => {
  // 将当前用户信息填充到编辑表单
  Object.assign(editForm, {
    gender: userProfile.gender,
    birthdate: userProfile.birthdate,
    email: userProfile.email,
    phone: userProfile.phone
  })
  editDialogVisible.value = true
}

const changePassword = () => ElMessage.info('修改密码功能将在未来版本中实现')
const notificationSettings = () => ElMessage.info('通知设置功能将在未来版本中实现')
const securitySettings = () => ElMessage.info('安全设置功能将在未来版本中实现')
const helpCenter = () => ElMessage.info('帮助中心功能将在未来版本中实现')

// 然后定义 quickActions 数组
const quickActions = [
  { name: '修改密码', icon: Lock, handler: changePassword },
  { name: '通知设置', icon: Bell, handler: notificationSettings },
  { name: '安全设置', icon: FirstAidKit, handler: securitySettings },
  { name: '帮助中心', icon: QuestionFilled, handler: helpCenter }
]

// 个人信息
const userProfile = reactive({
  studentId: '--',
  name: '--',
  gender: '--',
  birthdate: '--',
  email: '--',
  phone: '--',
  college: '--',
  class: '--',
  avatarUrl: 'https://picsum.photos/id/1005/200/200'
})

// 卡片悬停状态
const isHoveringPanel = ref(false)

// 鼠标位置跟踪
const handleMouseMove = (e) => {
  document.documentElement.style.setProperty('--mouse-x', `${e.clientX}px`)
  document.documentElement.style.setProperty('--mouse-y', `${e.clientY}px`)
}

// 格式化日期显示
const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('zh-CN')
}

// 获取性别的文本表示
const getGenderText = (gender) => {
  const genderMap = {
    'male': '男',
    'female': '女',
    'other': '其他'
  }
  return genderMap[gender] || '未知'
}

// 将英文性别转换为中文
const convertGenderToChinese = (gender) => {
  const genderMap = {
    'male': '男',
    'female': '女',
    'other': '其他'
  }
  return genderMap[gender] || gender
}

// 编辑对话框关闭处理
const handleDialogClose = () => {
  editDialogVisible.value = false
  editFormRef.value?.resetFields()
}

// 提交编辑表单
const submitEditForm = async () => {
  if (!editFormRef.value) return

  try {
    // 验证表单
    const valid = await editFormRef.value.validate()
    if (!valid) return

    // 确认对话框
    try {
      await ElMessageBox.confirm(
        '确定要保存个人信息修改吗？',
        '确认修改',
        {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning',
        }
      )
    } catch (cancel) {
      // 用户取消操作
      return
    }

    submitLoading.value = true

    const birthDate = editForm.birthdate || ''

    await service.post('/student/setting/info', {
      username: editForm.name,
      email: editForm.email,
      phone: editForm.phone,
      gender: convertGenderToChinese(editForm.gender),
      birthDate: birthDate,
      emergencyContact: '',
      emergencyPhone: ''
    })

    // 更新本地用户信息 - 只更新允许修改的字段
    Object.assign(userProfile, {
      gender: editForm.gender,
      birthdate: editForm.birthdate,
      email: editForm.email,
      phone: editForm.phone
    })

    ElMessage.success({
      message: '个人信息更新成功',
      duration: 2000,
      showClose: true
    })
    handleDialogClose()
  } catch (error) {
    console.error('更新个人信息失败:', error)

    // 错误处理由封装的service组件自动处理，这里只需要处理业务逻辑错误
    if (error.message) {
      ElMessage.error({
        message: error.message,
        duration: 3000,
        showClose: true
      })
    }
  } finally {
    submitLoading.value = false
  }
}

onMounted(async () => {
  try {
    // 使用封装的service组件获取个人信息
    const result = await service.get('/student/settings')

    if (result.code === 200 && result.data) {
      // 更新本地用户信息，正确映射后端字段名
      Object.assign(userProfile, {
        studentId: result.data.studentNo || userProfile.studentId,
        name: result.data.username || userProfile.name,
        gender: result.data.gender === '男' ? 'male' :
          result.data.gender === '女' ? 'female' :
            result.data.gender || userProfile.gender, // 将中文性别转换为英文格式
        birthdate: result.data.birthDate || userProfile.birthdate,
        email: result.data.email || userProfile.email,
        phone: result.data.phone || userProfile.phone,
        college: result.data.departmentName || userProfile.college,
        class: result.data.className || userProfile.class,
        avatarUrl: result.data.avatarUrl || userProfile.avatarUrl
      })

    } else {
      throw new Error(result.message || '获取个人信息失败')
    }
  } catch (error) {
    console.error('获取个人信息失败:', error)

    // 错误处理由封装的service组件自动处理，这里只需要处理业务逻辑错误
    if (error.message && !error.message.includes('未登录')) {
      ElMessage.warning({
        message: `加载个人信息失败: ${error.message}`,
        duration: 3000,
        showClose: true
      })
    }
  }
})

// 头像上传相关方法
const handleAvatarSuccess = (res, file) => {
  userProfile.avatarUrl = URL.createObjectURL(file.raw)
  ElMessage.success('头像更新成功')
}

const beforeAvatarUpload = (file) => {
  const isJPG = file.type === 'image/jpeg' || file.type === 'image/png'
  const isLt2M = file.size / 1024 / 1024 < 2

  if (!isJPG) ElMessage.error('上传头像图片只能是 JPG 或 PNG 格式!')
  if (!isLt2M) ElMessage.error('上传头像图片大小不能超过 2MB!')
  return isJPG && isLt2M
}
</script>

<style scoped lang="scss">
:root {
  --bg-color: #ffffff;
  --card-bg: rgba(255, 255, 255, 0.98);
  --card-border: 1px solid rgba(226, 232, 240, 0.8);
  --card-border-hover: 1px solid rgba(199, 210, 254, 0.9);
  --text-primary: #1e293b;
  --text-secondary: #64748b;
  --text-tertiary: #94a3b8;
  --accent-color: #6366f1;
  --hover-bg: rgba(241, 245, 249, 0.7);
  --item-bg: rgba(248, 250, 252, 0.8);
  --shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  --shadow-hover: 0 12px 40px rgba(0, 0, 0, 0.15);
  --avatar-border: 3px solid #fff;
  --uploader-bg: #f8fafc;
  --uploader-border: 2px dashed #d9d9d9;
  --info-title-color: var(--accent-color);
}

@media (prefers-color-scheme: dark) {
  :root {
    --bg-color: #1a1c23;
    --card-bg: rgba(30, 35, 45, 0.98);
    --card-border: 1px solid rgba(74, 85, 104, 0.6);
    --card-border-hover: 1px solid rgba(199, 210, 254, 0.9);
    --text-primary: #f1f5f9;
    --text-secondary: #94a3b8;
    --text-tertiary: #94a3b8;
    --accent-color: #8b5cf6;
    --hover-bg: rgba(45, 49, 58, 0.7);
    --item-bg: rgba(37, 41, 50, 0.8);
    --shadow: 0 4px 20px rgba(0, 0, 0, 0.15);
    --shadow-hover: 0 12px 40px rgba(0, 0, 0, 0.3);
    --avatar-border: 3px solid #252932;
    --uploader-bg: #252932;
    --uploader-border: 2px dashed #3a3f4b;
    --info-title-color: #a78bfa;
  }
}

.personal-center-page {
  background: var(--bg-color);
  --mouse-x: 0;
  --mouse-y: 0;
}

.personal-center-content {
  width: 100%;
  margin: 0 auto;
  padding: 0px 15px;
  display: grid;
  gap: 24px;

  @media (min-width: 992px) {
    grid-template-columns: 1fr 1fr;
    align-items: start;
  }

  .modern-card {
    position: relative;
    border-radius: 16px;
    padding: 30px;
    transition: all 0.3s ease;
    overflow: hidden;
    z-index: 1;

    .card-content {
      position: relative;
      z-index: 2;
    }

    background: white;
    border: 1px solid rgba(0, 0, 0, 0.1);
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.05);

    .dark & {
      background: rgba(30, 41, 59, 0.8);
      backdrop-filter: blur(12px);
      border: 1px solid rgba(255, 255, 255, 0.1);
      box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
    }

    &::before {
      content: '';
      position: absolute;
      top: 0;
      left: 0;
      right: 0;
      bottom: 0;
      background: radial-gradient(600px circle at var(--mouse-x) var(--mouse-y),
          rgba(99, 102, 241, 0.08) 0%,
          transparent 70%);
      opacity: 0;
      transition: opacity 0.3s ease;
      z-index: -1;
      pointer-events: none;
    }

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);

      &::before {
        opacity: 1;
      }
    }

    .card-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20px;

      h2,
      h3 {
        margin: 0;
        font-size: 20px;
        font-weight: 600;
        color: var(--text-primary);
      }

      .progress-indicator {
        background-color: rgba(64, 158, 255, 0.1);
        color: #409eff;
        padding: 6px 12px;
        border-radius: 999px;
        font-size: 14px;
      }
    }
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 24px;

    .avatar-section {
      display: flex;
      align-items: center;
      gap: 20px;

      .user-info {
        .username {
          margin: 0;
          font-size: 1.5rem;
          font-weight: 600;
          color: var(--text-primary);
          line-height: 1.3;
        }

        .student-id {
          margin: 6px 0 0;
          font-size: 0.9rem;
          color: var(--text-secondary);
        }
      }
    }

    .edit-button {
      padding: 8px 16px;
      font-weight: 500;
      transition: all 0.2s ease;

      &:hover {
        transform: translateY(-1px);
      }

      .el-icon {
        margin-right: 6px;
      }
    }
  }

  .avatar-uploader {
    .avatar {
      width: 100px;
      height: 100px;
      border-radius: 50%;
      object-fit: cover;
      border: var(--avatar-border);
      box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      transition: all 0.3s ease;
      cursor: pointer;

      &:hover {
        transform: scale(1.05);
        box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
      }
    }

    .avatar-uploader-icon {
      width: 100px;
      height: 100px;
      display: flex;
      align-items: center;
      justify-content: center;
      border: var(--uploader-border);
      border-radius: 50%;
      background-color: var(--uploader-bg);
      color: var(--text-tertiary);
      font-size: 24px;
      cursor: pointer;
      transition: all 0.3s ease;

      &:hover {
        border-color: var(--accent-color);
        background-color: rgba(99, 102, 241, 0.1);
        color: var(--accent-color);
      }
    }
  }

  .card-body {
    .info-group {
      margin-bottom: 24px;

      &:last-child {
        margin-bottom: 0;
      }

      .info-title {
        font-size: 1rem;
        font-weight: 600;
        color: var(--info-title-color);
        margin: 0 0 16px 0;
        padding-bottom: 8px;
        border-bottom: 1px solid rgba(99, 102, 241, 0.2);
      }

      .info-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 12px;

        @media (max-width: 768px) {
          grid-template-columns: 1fr;
          gap: 10px;
        }

        /* 响应式布局调整 */
        @media (max-width: 1024px) {
          .personal-center-content {
            gap: 20px;
          }
        }

        @media (max-width: 768px) {
          .personal-center-content {
            padding: 15px;
            gap: 15px;
          }

          .personal-info-card {
            padding: 20px;
          }

          .card-header {
            flex-direction: column;
            align-items: flex-start;
            gap: 15px;
          }

          .avatar-section {
            flex-direction: column;
            align-items: flex-start;
            gap: 12px;
          }

          .avatar {
            width: 80px;
            height: 80px;
          }

          .avatar-uploader-icon {
            width: 80px;
            height: 80px;
          }

          .username {
            font-size: 1.25rem;
          }

          .info-title {
            font-size: 0.95rem;
          }

          .info-item {
            padding: 12px 14px;
          }

          .quick-actions-card {
            padding: 20px;
          }

          .card-title {
            font-size: 1.1rem;
          }

          .action-item {
            padding: 14px;
          }

          .edit-dialog {
            width: 90% !important;
            max-width: 500px;
          }
        }

        @media (max-width: 480px) {
          .personal-center-content {
            padding: 10px;
            gap: 12px;
          }

          .personal-info-card,
          .quick-actions-card {
            padding: 16px;
          }

          .avatar {
            width: 70px;
            height: 70px;
          }

          .avatar-uploader-icon {
            width: 70px;
            height: 70px;
          }

          .username {
            font-size: 1.15rem;
          }

          .student-id {
            font-size: 0.85rem;
          }

          .info-item {
            padding: 10px 12px;
          }

          .item-icon {
            font-size: 18px;
            margin-right: 12px;
          }

          .item-value {
            font-size: 0.95rem;
          }

          .edit-button {
            padding: 6px 14px;
            font-size: 0.9rem;
            width: 100%;
          }

          .el-dialog {
            width: 95% !important;
          }

          .el-form {
            padding: 0 8px;
          }

          .info-item {
            display: flex;
            align-items: center;
            padding: 14px 16px;
            border-radius: 12px;
            background: var(--item-bg);
            transition: all 0.2s ease;
            cursor: default;

            &:hover {
              background: var(--hover-bg);
              transform: translateY(-2px);
            }

            .item-icon {
              font-size: 20px;
              color: var(--accent-color);
              margin-right: 16px;
              flex-shrink: 0;
            }

            .item-content {
              .item-label {
                font-size: 0.85rem;
                color: var(--text-secondary);
                margin-bottom: 4px;
              }

              .item-value {
                font-size: 1rem;
                font-weight: 500;
                color: var(--text-primary);
              }
            }
          }
        }
      }
    }

    .quick-actions-card {
      .card-title {
        display: flex;
        align-items: center;
        margin-bottom: 20px;
        font-size: 1.25rem;
        font-weight: 600;
        color: var(--text-primary);

        .title-icon {
          margin-right: 12px;
          color: var(--accent-color);
          font-size: 1.2em;
        }
      }

      .actions-grid {
        display: grid;
        grid-template-columns: 1fr;
        gap: 8px;
      }

      .action-item {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 16px;
        border-radius: 12px;
        transition: all 0.2s ease;
        cursor: pointer;
        background: var(--item-bg);

        &:hover {
          background: var(--hover-bg);
          transform: translateY(-2px);

          .action-content {
            color: var(--accent-color);
          }

          .arrow-icon {
            color: var(--accent-color);
            transform: translateX(3px);
          }
        }

        .action-content {
          display: flex;
          align-items: center;
          color: var(--text-primary);
          transition: color 0.2s ease;

          .action-icon {
            margin-right: 12px;
            color: var(--accent-color);
            font-size: 1.1em;
          }

          span {
            font-weight: 500;
          }
        }

        .arrow-icon {
          color: var(--text-tertiary);
          transition: all 0.2s ease;
        }
      }
    }
  }
}
</style>
