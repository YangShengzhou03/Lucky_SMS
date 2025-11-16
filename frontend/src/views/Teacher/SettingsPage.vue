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
              <p class="teacher-id">教师编号: {{ userProfile.teacherId }}</p>
            </div>
          </div>
          <el-button type="primary" size="small" @click="editProfile" class="edit-button" round>
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
            <h3 class="info-title">教学信息</h3>
            <div class="info-grid">
              <div class="info-item">
                <el-icon class="item-icon">
                  <School />
                </el-icon>
                <div class="item-content">
                  <p class="item-label">学院</p>
                  <p class="item-value">{{ userProfile.department }}</p>
                </div>
              </div>

              <div class="info-item">
                <el-icon class="item-icon">
                  <Collection />
                </el-icon>
                <div class="item-content">
                  <p class="item-label">职称</p>
                  <p class="item-value">{{ userProfile.title }}</p>
                </div>
              </div>

              <div class="info-item">
                <el-icon class="item-icon">
                  <Document />
                </el-icon>
                <div class="item-content">
                  <p class="item-label">教授课程</p>
                  <p class="item-value">{{ userProfile.courses.join(', ') }}</p>
                </div>
              </div>

              <div class="info-item">
                <el-icon class="item-icon">
                  <Medal />
                </el-icon>
                <div class="item-content">
                  <p class="item-label">办公室</p>
                  <p class="item-value">{{ userProfile.office }}</p>
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
          <span>教师功能</span>
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
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import {
  Plus, User, Edit, ArrowRight,
  Calendar, Notification, Phone, Setting, School, Collection,
  Document, Medal
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

// 首先定义所有函数
const editProfile = () => ElMessage.info('编辑个人信息功能将在未来版本中实现')
const courseManagement = () => ElMessage.info('课程管理功能将在未来版本中实现')
const gradeManagement = () => ElMessage.info('成绩管理功能将在未来版本中实现')
const studentManagement = () => ElMessage.info('学生管理功能将在未来版本中实现')
const teachingPlan = () => ElMessage.info('教学计划功能将在未来版本中实现')

// 然后定义 quickActions 数组
const quickActions = [
  { name: '课程管理', icon: Document, handler: courseManagement },
  { name: '成绩管理', icon: Medal, handler: gradeManagement },
  { name: '学生管理', icon: User, handler: studentManagement },
  { name: '教学计划', icon: Calendar, handler: teachingPlan }
]

// 个人信息
const userProfile = reactive({
  teacherId: 'T2023001',
  name: '王教授',
  gender: 'male',
  birthdate: '1980-05-20',
  email: 'wang@example.com',
  phone: '13900139000',
  department: '计算机科学与技术学院',
  title: '副教授',
  courses: ['数据结构', '算法分析', '人工智能导论'],
  office: '科技楼B501',
  avatarUrl: 'https://picsum.photos/id/1012/200/200'
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
/* 样式部分保持不变 */
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
  min-height: 100vh;
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

  .teacher-id {
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

  .action-content {
    .action-icon {
      margin-right: 10px;
      font-size: 1em;
    }
  }
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
</style>