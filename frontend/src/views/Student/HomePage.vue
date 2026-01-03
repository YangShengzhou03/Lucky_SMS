<template>
  <div class="student-home" @mousemove="handleMouseMove">
    <div>
      <div class="welcome-section modern-card">
        <h2>欢迎回来，<span class="username">{{ student?.username || '--' }}</span> </h2>
        <p class="subtitle">学号：{{ student?.student_no || '--' }} | 班级：{{ student?.class_name || '--' }}</p>
      </div>
      <div class="info-cards">
        <div class="modern-card score-card">
          <div class="card-header">
            <h3><el-icon>
                <Trophy />
              </el-icon> 学业成绩</h3>
            <div class="card-badge" v-if="student?.class_rank && student.class_rank <= 3">TOP {{ student.class_rank }}
            </div>
          </div>
          <div class="card-content">
            <div class="gpa">
              <span class="value">{{ student?.gpa || '--' }}</span>
              <span class="label">平均绩点</span>
            </div>
            <div class="progress-container">
              <el-progress :percentage="rankPercentage" :show-text="false" :stroke-width="8" color="#e6a23c" />
              <div class="progress-labels">
                <span>班级排名</span>
                <span>{{ student?.class_rank || '--' }} / {{ student?.classSize || '--' }}</span>
              </div>
            </div>
          </div>
        </div>
        <div class="modern-card course-card">
          <div class="card-header">
            <h3><el-icon>
                <Notebook />
              </el-icon> 我的课程</h3>
          </div>
          <div class="card-content">
            <div class="course-count">
              <span class="highlight">{{ student?.course_count || '--' }}</span> 门课程进行中
            </div>
            <div class="next-course">
              <div class="course-time">{{ student?.nextCourse ? formatCourseTime(student?.nextCourse?.time) : '--' }}
              </div>
              <div class="course-name">{{ student?.nextCourse?.name || '暂无课程' }}</div>
              <div class="course-location">{{ student?.nextCourse?.location || '待定教室' }}</div>
            </div>
          </div>
        </div>
        <div class="modern-card todo-card">
          <div class="card-header">
            <h3><el-icon>
                <List />
              </el-icon> 待办事项</h3>
            <el-tag size="small" type="danger" v-if="pendingCount > 0">
              {{ pendingCount }} 项待完成
            </el-tag>
          </div>
          <div class="card-content">
            <div class="todo-list">
              <template v-if="student?.todos && student.todos.length">
                <div class="todo-item" v-for="item in filteredTodos" :key="item.id"
                  :class="{ 'urgent': isUrgent(item.dueDate), 'overdue': isOverdue(item.dueDate), 'completed': item.completed }">
                  <el-checkbox v-model="item.completed" @change="updateTodo(item)" />
                  <div class="todo-content">
                    <span>{{ item.text }}</span>
                    <div class="todo-meta">
                      <el-tag size="small" :type="getDueTagType(item.dueDate)" effect="plain">
                        {{ formatDueDate(item.dueDate) }}
                      </el-tag>
                      <el-icon v-if="item.important" color="#F56C6C">
                        <StarFilled />
                      </el-icon>
                    </div>
                  </div>
                </div>
              </template>
              <div v-else class="empty-todos">
                <div class="empty-icon">
                  <el-icon size="48">
                    <Document />
                  </el-icon>
                </div>
                <el-button type="primary" @click="addFirstTodo" class="empty-button">
                  <el-icon>
                    <Plus />
                  </el-icon> 添加第一个待办事项
                </el-button>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div class="modern-card announcements">
        <div class="section-header">
          <h3><el-icon>
              <Bell />
            </el-icon> 校园公告</h3>
          <el-link type="primary" :underline="false">查看更多</el-link>
        </div>
        <el-scrollbar>
          <div class="announcement-list">
            <template v-if="announcements?.length">
              <div class="announcement-item" v-for="item in announcements" :key="item.id"
                @click="viewAnnouncement(item)">
                <el-tag class="announcement-tag" size="small" :type="item.announcement_type || 'INFO'">
                  {{ getAnnouncementType(item.announcement_type) }}
                </el-tag>
                <div class="announcement-content">
                  <div class="title">{{ item.title }}</div>
                  <div class="meta">
                    <span class="date">{{ formatDate(item.publish_date) }}</span>
                    <span class="department">{{ item.department_name }}</span>
                  </div>
                </div>
                <el-icon class="arrow">
                  <ArrowRight />
                </el-icon>
              </div>
            </template>
            <div v-else class="empty-announcements">
              <div class="empty-icon">
                <el-icon size="48">
                  <Bell />
                </el-icon>
              </div>
              <el-link type="primary" :underline="false" class="empty-button">
                查看历史公告
              </el-link>
            </div>
          </div>
        </el-scrollbar>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import service from '@/utils/request'
import {
  Trophy, Notebook, List, ArrowRight, StarFilled, Bell,
  Plus, Document
} from '@element-plus/icons-vue'
import { ElMessage, ElButton, ElLink } from 'element-plus'

const student = ref(null)
const announcements = ref(null)
const loading = ref(true)
const error = ref(null)

const pendingCount = computed(() => {
  return student.value?.todos ? student.value.todos.filter(todo => !todo.completed).length : 0
})

const filteredTodos = computed(() => {
  return student.value?.todos ? student.value.todos.filter(todo => !todo.completed).slice(0, 4) : []
})

const rankPercentage = computed(() => {
  if (student.value?.class_rank && student.value?.classSize) {
    return (1 - (student.value.class_rank - 1) / student.value.classSize) * 100
  }
  return 0
})

const updateTodo = async (item) => {
  const originalState = item.completed

  try {
    ElMessage.success('待办事项已更新')
  } catch (err) {
    item.completed = originalState
    ElMessage.error('更新失败，请重试')
    console.error('更新待办事项失败:', err)
  }
}

const viewAnnouncement = (item) => {
  console.log('查看公告:', item)
  ElMessage.info(`查看公告: ${item.title}`)
}

const formatDate = (dateString) => {
  return dateString?.replace(/-/g, '.') || '--'
}

const formatCourseTime = (timeString) => {
  return timeString?.replace('-', ' - ') || '--'
}

const getDaysDiff = (dueDate) => {
  if (!dueDate) return null

  const today = new Date()
  today.setHours(0, 0, 0, 0)

  const due = new Date(dueDate)
  due.setHours(0, 0, 0, 0)

  const diffTime = due - today
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))

  return diffDays
}

const formatDueDate = (dueDate) => {
  if (!dueDate) return '--'
  
  const daysDiff = getDaysDiff(dueDate)
  
  if (daysDiff === null) return dueDate
  
  if (daysDiff < 0) {
    return `已过期${Math.abs(daysDiff)}天`
  } else if (daysDiff === 0) {
    return '今天'
  } else if (daysDiff === 1) {
    return '明天'
  } else if (daysDiff <= 7) {
    return `${daysDiff}天后`
  } else {
    return formatDate(dueDate)
  }
}

const isUrgent = (dueDate) => {
  const daysDiff = getDaysDiff(dueDate)
  return daysDiff !== null && daysDiff <= 1
}

const isOverdue = (dueDate) => {
  const daysDiff = getDaysDiff(dueDate)
  return daysDiff !== null && daysDiff < 0
}

const getDueTagType = (dueDate) => {
  if (!dueDate) return 'info'

  const daysDiff = getDaysDiff(dueDate)

  if (daysDiff === null) return 'info'
  if (daysDiff < 0) return 'danger'
  if (daysDiff === 0) return 'danger'
  if (daysDiff === 1) return 'warning'
  if (daysDiff <= 3) return 'warning'
  return 'success'
}

const getAnnouncementType = (type) => {
  const types = {
    'IMPORTANT': '重要',
    'NOTICE': '通知',
    'INFO': '资讯',
    'ACTIVITY': '活动'
  }
  return types[type] || '公告'
}

const addFirstTodo = () => {
  ElMessage.info('功能正在建设中')
}

const handleMouseMove = (e) => {
  document.documentElement.style.setProperty('--mouse-x', `${e.clientX}px`)
  document.documentElement.style.setProperty('--mouse-y', `${e.clientY}px`)
}

const fetchData = async () => {
  loading.value = true
  error.value = null

  try {
    const res = await service.get('/student/home')

    if (res.code === 200) {
      student.value = res.data.student || {}
      announcements.value = res.data.announcements || []
    } else {
      throw new Error(res.message || '获取数据失败')
    }
  } catch (err) {
    error.value = '获取数据失败，请稍后重试'
    console.error('获取数据失败:', err)

    student.value = {}
    announcements.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  fetchData()
})

onUnmounted(() => {
  document.querySelector('.student-home')?.removeEventListener('mousemove', handleMouseMove)
})
</script>

<style scoped lang="scss">
:root {
  --text-primary: #303133;
  --text-secondary: #606266;
}

.dark {
  --text-primary: #ffffff;
  --text-secondary: rgba(255, 255, 255, 0.7);
}

.student-home {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  transition: background-color 0.3s ease;
  padding: 0 15px;
  --mouse-x: 0;
  --mouse-y: 0;
}

.modern-card {
  position: relative;
  border-radius: 4px;
  padding: 20px;
  overflow: hidden;
  z-index: 1;

  .card-content {
    position: relative;
    z-index: 2;
  }

  background: white;
  border: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

  .dark & {
    background: rgba(30, 41, 59, 0.9);
    border-color: rgba(74, 85, 104, 0.4);
  }
}

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

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

.info-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 30px;
  margin-bottom: 30px;
}

.welcome-section {
  margin-bottom: 30px;
  position: relative;
  background: white;
  border-radius: 8px;
  padding: 20px;
  overflow: hidden;

  .dark & {
    background: #1e293b;
  }

  h2 {
    position: relative;
    z-index: 2;
    font-size: 28px;
    font-weight: 600;
    margin: 0 0 10px;
    color: var(--text-primary);

    .username {
      color: var(--el-color-primary);
    }
  }

  .subtitle {
    position: relative;
    z-index: 2;
    font-size: 16px;
    color: var(--text-secondary);
    margin: 0;
  }
}

@keyframes shine {
  0% {
    left: -100%;
  }

  100% {
    left: 150%;
  }
}

/* 以下其他样式保持不变 */
.score-card {
  .gpa {
    .value {
      font-size: 40px;
      font-weight: 700;
      margin-right: 8px;
      color: #e6a23c;
      text-shadow: 0 2px 4px rgba(230, 162, 60, 0.1);
    }

    .label {
      font-size: 14px;
      color: var(--text-secondary);
      padding-bottom: 6px;
    }
  }

  .progress-labels {
    display: flex;
    justify-content: space-between;
    font-size: 12px;
    color: var(--text-secondary);
    margin-top: 8px;
  }
}

.course-card {
  .course-count {
    font-size: 14px;
    color: var(--text-secondary);
    margin-bottom: 20px;

    .highlight {
      font-size: 28px;
      font-weight: 700;
      color: #67c23a;
      margin-right: 6px;
      text-shadow: 0 2px 4px rgba(103, 194, 58, 0.1);
    }
  }

  .next-course {
    background: rgba(248, 248, 248, 0.6);
    border-radius: 12px;
    padding: 16px;
    transition: transform 0.3s ease;

    &:hover {
      transform: translateX(4px);
    }

    .dark & {
      background: rgba(40, 45, 55, 0.6);
    }

    .course-time {
      font-size: 14px;
      color: #67c23a;
      font-weight: 500;
      margin-bottom: 6px;
    }

    .course-name {
      font-size: 16px;
      font-weight: 600;
      margin-bottom: 6px;
      color: var(--text-primary);
    }

    .course-location {
      font-size: 13px;
      color: var(--text-secondary);
    }
  }
}

.todo-card {
  .todo-list {
    max-height: 240px;
    overflow-y: auto;
    padding-right: 12px;
    scrollbar-width: thin;
    scrollbar-color: rgba(0, 0, 0, 0.1) transparent;

    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-thumb {
      background: rgba(0, 0, 0, 0.1);
      border-radius: 3px;
    }

    .dark & {
      scrollbar-color: rgba(255, 255, 255, 0.1) transparent;

      &::-webkit-scrollbar-thumb {
        background: rgba(255, 255, 255, 0.1);
      }
    }
  }

  .todo-item {
    display: flex;
    align-items: center;
    padding: 12px 0;
    border-bottom: 1px dashed rgba(0, 0, 0, 0.08);
    transition: all 0.1s ease;

    &:hover {
      transform: translateX(4px);
    }

    &:last-child {
      border-bottom: none;
    }

    .el-checkbox {
      margin-right: 14px;
    }

    .todo-content {
      flex: 1;

      span {
        font-size: 14px;
        color: var(--text-primary);
        transition: all 0.2s ease;
      }

      .todo-meta {
        display: flex;
        align-items: center;
        margin-top: 6px;

        .el-tag {
          margin-right: 10px;
          box-shadow: 0 1px 4px rgba(0, 0, 0, 0.05);
        }
      }
    }

    &.urgent {
      .todo-content span {
        color: #898989;
      }
    }

    &.overdue {
      .todo-meta .el-tag {
        background-color: rgba(245, 108, 108, 0.1);
        border-color: #F56C6C;
        color: #F56C6C;
      }
    }

    .todo-meta {
      .el-tag--success {
        background-color: rgba(103, 194, 58, 0.1);
        border-color: #67c23a;
        color: #67c23a;
      }
    }

    &.completed {
      .todo-content span {
        text-decoration: line-through;
        color: #c0c4cc;
      }
    }
  }
}

.announcements {
  .section-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h3 {
      margin: 0;
      font-size: 20px;
      font-weight: 650;
      display: flex;
      align-items: center;
      color: var(--text-primary);
      letter-spacing: -0.3px;

      .el-icon {
        margin-right: 12px;
        font-size: 22px;
        color: #e6a23c;
        text-shadow: 0 2px 4px rgba(230, 162, 60, 0.1);
      }
    }
  }

  .announcement-item {
    display: flex;
    align-items: flex-start;
    padding: 16px 0;
    cursor: pointer;
    border-bottom: 1px dashed rgba(0, 0, 0, 0.08);

    &:last-child {
      border-bottom: none;
    }

    .announcement-tag {
      color: #409eff;
      margin-right: 16px;
      flex-shrink: 0;
      margin-top: 2px;
    }

    .announcement-content {
      flex: 1;

      .title {
        font-size: 16px;
        font-weight: 500;
        margin-bottom: 6px;
        color: var(--text-primary);
        line-height: 1.4;
      }

      .meta {
        display: flex;
        font-size: 13px;
        color: var(--text-secondary);

        .date {
          margin-right: 16px;
          position: relative;

          &::after {
            content: '•';
            position: absolute;
            right: -10px;
            opacity: 0.6;
          }
        }

        .department {
          display: flex;
          align-items: center;
        }
      }
    }
  }

  .el-scrollbar {
    max-height: 340px;
    padding-right: 8px;

    &::-webkit-scrollbar {
      width: 6px;
    }

    &::-webkit-scrollbar-thumb {
      background: rgba(0, 0, 0, 0.1);
      border-radius: 3px;
    }

    .dark & {
      &::-webkit-scrollbar-thumb {
        background: rgba(255, 255, 255, 0.1);
      }
    }
  }
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;

  h3 {
    font-size: 18px;
    margin: 0;
    display: flex;
    align-items: center;
    font-weight: 600;
    color: var(--text-primary);

    .el-icon {
      margin-right: 10px;
      font-size: 20px;
    }
  }

  .card-badge {
    background: #e6a23c;
    color: white;
    padding: 4px 10px;
    border-radius: 8px;
    font-size: 12px;
    font-weight: bold;
    box-shadow: 0 2px 6px rgba(230, 162, 60, 0.2);
  }
}

.empty-todos,
.empty-announcements {
  text-align: center;
  padding: 30px 20px;
  color: var(--text-secondary);

  .empty-icon {
    margin-bottom: 16px;
    opacity: 0.6;

    .el-icon {
      font-size: 48px;
      color: #909399;
    }
  }

  .empty-title {
    font-size: 16px;
    font-weight: 500;
    margin: 0 0 8px;
    color: var(--text-primary);
  }

  .empty-desc {
    font-size: 14px;
    margin: 0 0 20px;
    color: var(--text-secondary);
    line-height: 1.5;
  }

  .empty-button {
    padding: 10px 20px;
    border-radius: 4px;
    font-weight: 500;
    display: inline-flex;
    align-items: center;
    justify-content: center;
  }
}

.empty-todos {
  .empty-button {
    box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);

    &:hover {
      box-shadow: 0 6px 16px rgba(64, 158, 255, 0.3);
    }
  }
}

.empty-announcements {
  .empty-button {
    color: #409eff;
    border: 1px solid #409eff;
    background-color: rgba(64, 158, 255, 0.1);
  }
}

@media (max-width: 768px) {
  .welcome-section h2 {
    font-size: 24px;
  }

  .info-cards {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .card-header h3 {
    font-size: 16px;
  }

  .score-card .gpa .value {
    font-size: 32px;
  }

  .course-card .course-count .highlight {
    font-size: 24px;
  }

  .todo-card .todo-list {
    max-height: 200px;
  }

  .announcements .section-header h3 {
    font-size: 18px;
  }

  .announcements .el-scrollbar {
    max-height: 300px;
  }
}

@media (max-width: 480px) {
  .welcome-section h2 {
    font-size: 20px;
  }

  .welcome-section .subtitle {
    font-size: 14px;
  }

  .info-cards {
    padding: 0 10px;
  }

  .card-base {
    padding: 15px;
  }

  .todo-card .todo-item {
    padding: 10px 0;
  }

  .announcement-item {
    padding: 12px 0;
  }

  .announcement-item:hover {
    padding: 12px;
    margin: 0 -8px;
  }
}
</style>
