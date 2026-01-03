<template>
  <div class="personal-center-page">
    <div class="personal-center-content">
      <div class="personal-info-card modern-card">
        <div class="card-header">
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
    </div>

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
  Plus, User, Edit,
  Calendar, Phone, School, Collection
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import service from '@/utils/request'

const editDialogVisible = ref(false)
const submitLoading = ref(false)
const editFormRef = ref()

const editForm = reactive({
  name: '',
  gender: 'male',
  birthdate: '',
  email: '',
  phone: ''
})

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

const openEditDialog = () => {
  Object.assign(editForm, {
    gender: userProfile.gender,
    birthdate: userProfile.birthdate,
    email: userProfile.email,
    phone: userProfile.phone
  })
  editDialogVisible.value = true
}

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

const formatDate = (dateString) => {
  return new Date(dateString).toLocaleDateString('zh-CN')
}

const getGenderText = (gender) => {
  const genderMap = {
    'male': '男',
    'female': '女',
    'other': '其他'
  }
  return genderMap[gender] || '未知'
}

const convertGenderToChinese = (gender) => {
  const genderMap = {
    'male': '男',
    'female': '女',
    'other': '其他'
  }
  return genderMap[gender] || gender
}

const handleDialogClose = () => {
  editDialogVisible.value = false
  editFormRef.value?.resetFields()
}

const submitEditForm = async () => {
  if (!editFormRef.value) return

  try {
    const valid = await editFormRef.value.validate()
    if (!valid) return

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
    const result = await service.get('/student/settings')

    if (result.code === 200 && result.data) {
      Object.assign(userProfile, {
        studentId: result.data.studentNo || userProfile.studentId,
        name: result.data.username || userProfile.name,
        gender: result.data.gender === '男' ? 'male' :
          result.data.gender === '女' ? 'female' :
            result.data.gender || userProfile.gender,
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

    if (error.message && !error.message.includes('未登录')) {
      ElMessage.warning({
        message: `加载个人信息失败: ${error.message}`,
        duration: 3000,
        showClose: true
      })
    }
  }
})

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
  --text-primary: #1e293b;
  --text-secondary: #64748b;
  --text-tertiary: #94a3b8;
  --accent-color: #6366f1;
  --item-bg: rgba(248, 250, 252, 0.8);
  --avatar-border: 3px solid #fff;
  --uploader-bg: #f8fafc;
  --uploader-border: 2px dashed #d9d9d9;
  --info-title-color: var(--accent-color);
}

@media (prefers-color-scheme: dark) {
  :root {
    --bg-color: #1a1c23;
    --card-bg: rgba(30, 35, 45, 0.98);
    --text-primary: #f1f5f9;
    --text-secondary: #94a3b8;
    --text-tertiary: #94a3b8;
    --accent-color: #8b5cf6;
    --item-bg: rgba(37, 41, 50, 0.8);
    --avatar-border: 3px solid #252932;
    --uploader-bg: #252932;
    --uploader-border: 2px dashed #3a3f4b;
    --info-title-color: #a78bfa;
  }
}

.personal-center-page {
  background: var(--bg-color);
  padding: 20px 15px;
}

.personal-center-content {
  max-width: 1200px;
  margin: 0 auto;
}

.personal-info-card {
  background: var(--card-bg);
  border-radius: 8px;
  padding: 20px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  gap: 20px;
}

.avatar-uploader {
  flex-shrink: 0;
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  object-fit: cover;
  border: var(--avatar-border);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
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
}

.user-info {
  flex: 1;
}

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

.edit-button {
  padding: 8px 16px;
  font-weight: 500;
}

.card-body {
  margin-top: 24px;
}

.info-group {
  margin-bottom: 24px;
}

.info-group:last-child {
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
}

.info-item {
  display: flex;
  align-items: center;
  padding: 12px 14px;
  border-radius: 12px;
  background: var(--item-bg);
}

.item-icon {
  font-size: 20px;
  color: var(--accent-color);
  margin-right: 16px;
  flex-shrink: 0;
}

.item-content {
  flex: 1;
}

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

@media (max-width: 768px) {
  .personal-center-page {
    padding: 15px;
  }

  .personal-info-card {
    padding: 20px;
  }

  .card-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .info-grid {
    grid-template-columns: 1fr;
    gap: 10px;
  }

  .avatar,
  .avatar-uploader-icon {
    width: 80px;
    height: 80px;
  }

  .username {
    font-size: 1.25rem;
  }

  .info-item {
    padding: 10px 12px;
  }
}

@media (max-width: 480px) {
  .personal-center-page {
    padding: 10px;
  }

  .personal-info-card {
    padding: 16px;
  }

  .avatar,
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
}
</style>