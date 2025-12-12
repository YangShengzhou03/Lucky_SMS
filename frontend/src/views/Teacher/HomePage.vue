<template>
  <div class="teacher-home">
    <div>
      <div class="welcome-section">
        <div class="welcome-card">
          <div class="avatar-container">
            <el-avatar :size="80" :src="teacher?.avatar">
              <span class="avatar-text">{{ teacher?.name?.charAt(0) || 'T' }}</span>
            </el-avatar>
            <div class="online-indicator" :class="{ 'online': teacher?.online }"></div>
          </div>
          <div class="welcome-content">
            <h2>欢迎回来，<span class="username">{{ teacher?.name || '--' }}</span> 老师</h2>
            <p class="subtitle">教师编号：{{ teacher?.id || '--' }} | {{ teacher?.department || '--' }}</p>
          </div>
          <div class="welcome-stats">
            <div class="stat-item">
              <div class="stat-value">{{ teacher?.teachingYears || '--' }}</div>
              <div class="stat-label">教龄(年)</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ teacher?.title || '--' }}</div>
              <div class="stat-label">职称</div>
            </div>
          </div>
        </div>
      </div>

      <div class="info-cards">
        <div class="info-card teaching-card">
          <div class="card-header">
            <h3><el-icon>
                <Notebook />
              </el-icon> 教学情况</h3>
            <el-link type="primary" :underline="false" @click="navigateToTeaching">详情 <el-icon>
                <ArrowRight />
              </el-icon></el-link>
          </div>
          <div class="card-content">
            <div class="stats-grid">
              <div class="stat-item">
                <div class="stat-icon">
                  <el-icon>
                    <Collection />
                  </el-icon>
                </div>
                <div class="stat-content">
                  <div class="stat-value">{{ teacher?.courseCount || '--' }}</div>
                  <div class="stat-label">本学期课程</div>
                </div>
              </div>
              <div class="stat-item">
                <div class="stat-icon">
                  <el-icon>
                    <User />
                  </el-icon>
                </div>
                <div class="stat-content">
                  <div class="stat-value">{{ teacher?.studentCount || '--' }}</div>
                  <div class="stat-label">学生总数</div>
                </div>
              </div>
              <div class="stat-item">
                <div class="stat-icon">
                  <el-icon>
                    <DataLine />
                  </el-icon>
                </div>
                <div class="stat-content">
                  <div class="stat-value">{{ teacher?.avgScore || '--' }}</div>
                  <div class="stat-label">平均评分</div>
                </div>
              </div>
              <div class="stat-item">
                <div class="stat-icon">
                  <el-icon>
                    <TrendCharts />
                  </el-icon>
                </div>
                <div class="stat-content">
                  <div class="stat-value">{{ attendanceRate }}%</div>
                  <div class="stat-label">到课率</div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div class="info-card schedule-card">
          <div class="card-header">
            <h3><el-icon>
                <Calendar />
              </el-icon> 今日课程</h3>
            <el-tag v-if="hasClassToday" :type="nextClassSoon ? 'danger' : 'success'" size="small">
              {{ nextClassStatus }}
            </el-tag>
          </div>
          <div class="card-content">
            <template v-if="hasClassToday">
              <div class="class-info">
                <div class="class-time">
                  <el-icon>
                    <Clock />
                  </el-icon>
                  {{ formatCourseTime(teacher?.nextClass?.time || '--') }}
                  <el-tag v-if="nextClassSoon" type="danger" size="mini" effect="dark">
                    即将开始
                  </el-tag>
                </div>
                <div class="class-name">{{ teacher?.nextClass?.name || '--' }}</div>
                <div class="class-meta">
                  <span class="meta-item">
                    <el-icon>
                      <Location />
                    </el-icon>
                    {{ teacher?.nextClass?.location || '待定教室' }}
                  </span>
                  <span class="meta-item">
                    <el-icon>
                      <User />
                    </el-icon>
                    {{ teacher?.nextClass?.studentCount || '--' }}名学生
                  </span>
                </div>
                <div class="class-actions">
                  <el-button type="primary" size="small" @click="navigateToClass(teacher.nextClass.id)">
                    <el-icon>
                      <Connection />
                    </el-icon> 进入课堂
                  </el-button>
                  <el-button size="small" @click="viewClassMaterials(teacher.nextClass.id)">
                    <el-icon>
                      <Folder />
                    </el-icon> 教学资料
                  </el-button>
                </div>
              </div>
            </template>
            <div v-else class="no-class">
              <el-empty description="今日无课程安排" :image-size="80">
                <el-button type="primary" size="small" @click="navigateToSchedule">查看完整课表</el-button>
              </el-empty>
            </div>
          </div>
        </div>

        <div class="info-card task-card">
          <div class="card-header">
            <h3><el-icon>
                <List />
              </el-icon> 待办事项</h3>
            <div class="task-header-actions">
              <el-tag v-if="pendingTasksCount > 0" type="danger" size="small" class="task-count-tag">
                {{ pendingTasksCount }} 项待完成
              </el-tag>
              <el-button size="small" circle @click="showAddTaskDialog" class="add-task-btn">
                <el-icon>
                  <Plus />
                </el-icon>
              </el-button>
            </div>
          </div>
          <div class="card-content">
            <div class="task-list">
              <template v-if="teacher?.tasks?.length">
                <div class="task-item" v-for="item in filteredTasks" :key="item.id"
                  :class="{ 'urgent': isUrgent(item.dueDate), 'completed': item.completed }">
                  <el-checkbox v-model="item.completed" @change="updateTask(item)" />
                  <div class="task-content">
                    <div class="task-main">
                      <span class="task-text">{{ item.text }}</span>
                      <el-tag v-if="item.important" size="mini" type="danger" effect="plain">
                        重要
                      </el-tag>
                    </div>
                    <div class="task-meta">
                      <el-tag size="mini" :type="getDueTagType(item.dueDate)" effect="plain">
                        <el-icon>
                          <Clock />
                        </el-icon>
                        {{ item.dueDate }}
                      </el-tag>
                      <span class="task-course" v-if="item.course">
                        <el-icon>
                          <Collection />
                        </el-icon>
                        {{ item.course }}
                      </span>
                    </div>
                  </div>
                  <el-dropdown trigger="click" @command="handleTaskCommand($event, item)">
                    <el-icon class="task-more">
                      <MoreFilled />
                    </el-icon>
                    <template #dropdown>
                      <el-dropdown-menu>
                        <el-dropdown-item command="edit">编辑</el-dropdown-item>
                        <el-dropdown-item command="delete" divided>删除</el-dropdown-item>
                      </el-dropdown-menu>
                    </template>
                  </el-dropdown>
                </div>
              </template>
              <div v-else class="empty-tasks">
                <el-empty description="暂无待办事项" :image-size="80">
                  <el-button type="primary" size="small" @click="showAddTaskDialog">
                    <el-icon>
                      <Plus />
                    </el-icon> 添加新任务
                  </el-button>
                </el-empty>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="announcements-card">
        <div class="card-header">
          <h3><el-icon>
              <Bell />
            </el-icon> 最新公告</h3>
          <el-link type="primary" :underline="false" @click="navigateToAnnouncements">
            查看全部 <el-icon>
              <ArrowRight />
            </el-icon>
          </el-link>
        </div>
        <div class="card-content">
          <el-scrollbar>
            <div class="announcement-list">
              <template v-if="announcements?.length">
                <div class="announcement-item" v-for="item in announcements" :key="item.id"
                  @click="viewAnnouncement(item)">
                  <div class="announcement-badge" :class="item.type">
                    <el-icon v-if="item.type === 'important'">
                      <WarningFilled />
                    </el-icon>
                    <el-icon v-else-if="item.type === 'notice'">
                      <BellFilled />
                    </el-icon>
                    <el-icon v-else>
                      <InfoFilled />
                    </el-icon>
                  </div>
                  <div class="announcement-content">
                    <div class="announcement-title">{{ item.title }}</div>
                    <div class="announcement-meta">
                      <span class="meta-item">
                        <el-icon>
                          <Clock />
                        </el-icon>
                        {{ formatDate(item.date) }}
                      </span>
                      <span class="meta-item">
                        <el-icon>
                          <OfficeBuilding />
                        </el-icon>
                        {{ item.department }}
                      </span>
                    </div>
                  </div>
                  <el-icon class="announcement-arrow">
                    <ArrowRight />
                  </el-icon>
                </div>
              </template>
              <div v-else class="empty-announcements">
                <el-empty description="暂无公告" :image-size="80">
                  <el-link type="primary" :underline="false" @click="navigateToAnnouncements">
                    查看历史公告
                  </el-link>
                </el-empty>
              </div>
            </div>
          </el-scrollbar>
        </div>
      </div>
    </div>

    <el-dialog v-model="taskDialogVisible" title="添加新任务" width="500px">
      <el-form :model="newTaskForm" label-width="80px">
        <el-form-item label="任务内容" required>
          <el-input v-model="newTaskForm.text" placeholder="请输入任务内容" />
        </el-form-item>
        <el-form-item label="关联课程">
          <el-select v-model="newTaskForm.course" placeholder="请选择关联课程" clearable>
            <el-option v-for="course in teacher?.courses" :key="course.id" :label="course.name" :value="course.name" />
          </el-select>
        </el-form-item>
        <el-form-item label="截止时间">
          <el-date-picker v-model="newTaskForm.dueDate" type="datetime" placeholder="选择截止时间"
            :disabled-date="disabledDate" :shortcuts="dateShortcuts" />
        </el-form-item>
        <el-form-item label="重要程度">
          <el-switch v-model="newTaskForm.important" active-text="重要" inactive-text="普通" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="taskDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addNewTask">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import service from '@/utils/request'
import {
  Notebook, Calendar, List, Bell, ArrowRight, Plus,
  WarningFilled, Collection, User, DataLine,
  TrendCharts, Clock, Location, Connection, Folder,
  MoreFilled, BellFilled, InfoFilled, OfficeBuilding
} from '@element-plus/icons-vue'
import {
  ElMessage, ElButton, ElLink, ElTag,
  ElDialog, ElForm, ElFormItem, ElInput, ElSelect,
  ElOption, ElDatePicker, ElSwitch, ElEmpty, ElAvatar,
  ElDropdown, ElDropdownMenu, ElDropdownItem, ElScrollbar
} from 'element-plus'

const teacher = ref(null)
const announcements = ref(null)
const loading = ref(true)
const error = ref(null)
const taskDialogVisible = ref(false)
const newTaskForm = ref({
  text: '',
  course: '',
  dueDate: '',
  important: false
})

const pendingTasksCount = computed(() => {
  return teacher.value?.tasks?.filter(task => !task.completed).length || 0
})

const filteredTasks = computed(() => {
  return teacher.value?.tasks?.slice(0, 5) || []
})

const attendanceRate = computed(() => {
  return teacher.value?.attendanceRate || 0
})

const hasClassToday = computed(() => {
  return teacher.value?.nextClass?.name && teacher.value.nextClass.name !== '暂无课程'
})

const nextClassSoon = computed(() => {
  if (!teacher.value?.nextClass?.time) return false
  const [startTime] = teacher.value.nextClass.time.split('-')
  const now = new Date()
  const [hours, minutes] = startTime.split(':').map(Number)
  const classTime = new Date(now.getFullYear(), now.getMonth(), now.getDate(), hours, minutes)
  return classTime - now < 3600000
})

const nextClassStatus = computed(() => {
  return nextClassSoon.value ? '即将上课' : '今日有课'
})

const dateShortcuts = [
  {
    text: '今天',
    value: new Date(),
  },
  {
    text: '明天',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() + 3600 * 1000 * 24)
      return date
    },
  },
  {
    text: '一周后',
    value: () => {
      const date = new Date()
      date.setTime(date.getTime() + 3600 * 1000 * 24 * 7)
      return date
    },
  },
]

const fetchData = async () => {
  loading.value = true
  error.value = null

  try {
    const res = await service.get('/teacher/home')
    
    if (res.code === 200) {
      teacher.value = res.data.teacher || {}
      announcements.value = res.data.announcements || []
    } else {
      throw new Error(res.message || '获取数据失败')
    }
  } catch (err) {
    error.value = '获取数据失败，请稍后重试'
    console.error('获取数据失败:', err)
    
    teacher.value = {
      name: '张明华',
      id: 'T2023001',
      department: '计算机科学与技术学院',
      title: '副教授',
      teachingYears: 8,
      online: true,
      avatar: '',
      courseCount: 4,
      studentCount: 156,
      avgScore: 4.7,
      attendanceRate: 92.5,
      nextClass: {
        id: 'C001',
        name: '数据结构与算法',
        time: '14:00-15:30',
        location: '逸夫楼305',
        studentCount: 42
      },
      courses: [
        { id: 'C001', name: '数据结构与算法' },
        { id: 'C002', name: '操作系统原理' },
        { id: 'C003', name: '计算机网络' },
        { id: 'C004', name: '数据库系统' }
      ],
      tasks: [
        {
          id: 1,
          text: '批改数据结构作业第3章',
          dueDate: '明天',
          completed: false,
          important: true,
          course: '数据结构与算法'
        },
        {
          id: 2,
          text: '准备下周操作系统课程PPT',
          dueDate: '3天后',
          completed: false,
          important: false,
          course: '操作系统原理'
        },
        {
          id: 3,
          text: '提交期中考试成绩',
          dueDate: '2023-11-15',
          completed: true,
          important: false
        },
        {
          id: 4,
          text: '参加教学研讨会',
          dueDate: '2023-11-20',
          completed: false,
          important: true
        },
        {
          id: 5,
          text: '编写数据库实验指导书',
          dueDate: '2023-12-01',
          completed: false,
          important: true,
          course: '数据库系统'
        }
      ]
    }

    announcements.value = [
      {
        id: 1,
        title: '关于2023-2024学年第二学期教学安排的通知',
        date: '2023-11-01',
        department: '教务处',
        type: 'important',
        content: '下学期课程安排已发布，请各位老师及时查看。'
      },
      {
        id: 2,
        title: '2023年度教学成果奖申报通知',
        date: '2023-10-25',
        department: '科研处',
        type: 'notice',
        content: '2023年度教学成果奖申报工作已开始，请有意向的老师按时提交材料。'
      },
      {
        id: 3,
        title: '关于举办教师信息化教学能力提升培训的通知',
        date: '2023-10-20',
        department: '教师发展中心',
        type: 'info',
        content: '本次培训将介绍最新的信息化教学工具和方法，欢迎各位老师报名参加。'
      },
      {
        id: 4,
        title: '关于开展期中教学检查工作的通知',
        date: '2023-10-15',
        department: '教学质量监控中心',
        type: 'notice',
        content: '根据学校教学工作安排，将于第10-11周开展期中教学检查工作。'
      }
    ]
  } finally {
    loading.value = false
  }
}

const updateTask = async (item) => {
  const originalState = item.completed
  try {
    ElMessage.success(`任务"${item.text}"已${item.completed ? '完成' : '待办'}`)
  } catch (err) {
    item.completed = originalState
    ElMessage.error('更新失败，请重试')
    console.error('更新任务失败:', err)
  }
}

const viewAnnouncement = (item) => {
  ElMessage.info(`查看公告: ${item.title}`)
}

const formatDate = (dateString) => {
  if (!dateString) return '--'
  const date = new Date(dateString)
  return `${date.getFullYear()}.${date.getMonth() + 1}.${date.getDate()}`
}

const formatCourseTime = (timeString) => {
  return timeString?.replace('-', ' - ') || '--'
}

const isUrgent = (dueDate) => {
  return dueDate?.includes('天') || dueDate?.includes('明天')
}

const getDueTagType = (dueDate) => {
  if (!dueDate) return 'info'
  if (dueDate.includes('明天') || dueDate.includes('今天')) return 'danger'
  if (dueDate.includes('天') && parseInt(dueDate) <= 3) return 'warning'
  return 'info'
}

const showAddTaskDialog = () => {
  taskDialogVisible.value = true
}

const addNewTask = () => {
  if (!newTaskForm.value.text) {
    ElMessage.warning('请填写任务内容')
    return
  }

  const newTask = {
    id: Date.now(),
    text: newTaskForm.value.text,
    course: newTaskForm.value.course,
    dueDate: formatDueDate(newTaskForm.value.dueDate),
    important: newTaskForm.value.important,
    completed: false
  }

  teacher.value.tasks.unshift(newTask)
  ElMessage.success('任务添加成功')
  resetTaskForm()
  taskDialogVisible.value = false
}

const formatDueDate = (date) => {
  if (!date) return ''
  if (typeof date === 'string') return date

  const now = new Date()
  const diffTime = date - now
  const diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24))

  if (diffDays === 0) return '今天'
  if (diffDays === 1) return '明天'
  if (diffDays <= 7) return `${diffDays}天后`

  return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`
}

const resetTaskForm = () => {
  newTaskForm.value = {
    text: '',
    course: '',
    dueDate: '',
    important: false
  }
}

const disabledDate = (date) => {
  return date < new Date(new Date().setHours(0, 0, 0, 0))
}

const handleTaskCommand = (command, task) => {
  if (command === 'edit') {
    ElMessage.info(`编辑任务: ${task.text}`)
  } else if (command === 'delete') {
    teacher.value.tasks = teacher.value.tasks.filter(t => t.id !== task.id)
    ElMessage.success('任务已删除')
  }
}

const navigateToSchedule = () => ElMessage.info('跳转到课表页面')
const navigateToTeaching = () => ElMessage.info('跳转到教学情况页面')
const navigateToClass = (classId) => ElMessage.info(`进入课堂: ${classId}`)
const viewClassMaterials = (classId) => ElMessage.info(`查看教学资料: ${classId}`)
const navigateToAnnouncements = () => ElMessage.info('跳转到公告列表')

onMounted(() => {
  fetchData()
})
</script>

<style scoped lang="scss">
.teacher-home {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  padding: 0 15px;
  max-width: 100%;
  margin: 0 auto;
  --mouse-x: 0;
  --mouse-y: 0;
}

.card-base {
  position: relative;
  border-radius: 16px;
  padding: 25px;
  transition: all 0.3s ease;
  overflow: hidden;
  z-index: 1;
  background: white;
  border: 1px solid rgba(0, 0, 0, 0.08);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);

  .dark & {
    background: rgba(30, 41, 59, 0.9);
    border-color: rgba(74, 85, 104, 0.4);
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
    border-color: rgba(199, 210, 254, 0.8);

    .dark & {
      border-color: rgba(99, 102, 241, 0.5);
    }

    &::before {
      opacity: 1;
    }
  }

  .card-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 20px;

    h3 {
      margin: 0;
      font-size: 18px;
      font-weight: 6px;
      color: var(--text-primary);

      .el-icon {
        margin-right: 8px;
        color: var(--el-color-primary);
      }
    }
  }
}

.welcome-section {
  margin-bottom: 30px;

  .welcome-card {
    @extend .card-base;
    display: flex;
    align-items: center;
    background: linear-gradient(135deg, #f6f7f9 0%, #f0f4ff 50%, #ffffff 100%);
    padding: 30px;
    position: relative;
    overflow: hidden;

    .dark & {
      background: linear-gradient(135deg, #1e293b 0%, #0f172a 100%);
    }

    &::after {
      content: '';
      position: absolute;
      top: 0;
      left: -100%;
      width: 50%;
      height: 100%;
      background: linear-gradient(to right,
          transparent 0%,
          rgba(255, 255, 255, 0.3) 50%,
          transparent 100%);
      transform: skewX(-25deg);
      animation: shine 6s infinite;
      z-index: 1;
    }

    .dark &::after {
      background: linear-gradient(to right,
          transparent 0%,
          rgba(99, 102, 241, 0.3) 50%,
          transparent 100%);
    }

    @keyframes shine {
      0% {
        left: -100%;
      }

      100% {
        left: 150%;
      }
    }

    .avatar-container {
      position: relative;
      margin-right: 25px;
      flex-shrink: 0;
      z-index: 2;

      .avatar-text {
        font-size: 32px;
        font-weight: bold;
      }

      .online-indicator {
        position: absolute;
        bottom: 5px;
        right: 5px;
        width: 14px;
        height: 14px;
        border-radius: 50%;
        background-color: #e0e0e0;
        border: 2px solid white;

        &.online {
          background-color: #67c23a;
        }
      }
    }

    .welcome-content {
      flex: 1;
      z-index: 2;

      h2 {
        font-size: 24px;
        font-weight: 6px;
        margin: 0 0 8px;
        color: var(--text-primary);

        .username {
          color: var(--el-color-primary);
        }
      }

      .subtitle {
        font-size: 14px;
        color: var(--text-secondary);
        margin: 0 0 15px;
      }
    }

    .welcome-stats {
      display: flex;
      gap: 30px;
      margin-left: 30px;
      padding-left: 30px;
      border-left: 1px solid rgba(0, 0, 0, 0.08);
      z-index: 2;

      .dark & {
        border-left-color: rgba(255, 255, 255, 0.1);
      }

      .stat-item {
        text-align: center;

        .stat-value {
          font-size: 24px;
          font-weight: 700;
          color: var(--el-color-primary);
          margin-bottom: 5px;
        }

        .stat-label {
          font-size: 12px;
          color: var(--text-secondary);
          text-transform: uppercase;
          letter-spacing: 1px;
        }
      }
    }
  }
}

.info-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 30px;
  margin-bottom: 30px;
}

.teaching-card {
  @extend .card-base;

  .stats-grid {
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    gap: 15px;

    .stat-item {
      display: flex;
      align-items: center;
      padding: 15px;
      background-color: #f8fafc;
      border-radius: 8px;
      transition: all 0.2s ease;

      .dark & {
        background-color: rgba(30, 41, 59, 0.5);
      }

      &:hover {
        background-color: #f1f5f9;
        transform: translateY(-2px);

        .dark & {
          background-color: rgba(30, 41, 59, 0.8);
        }
      }

      .stat-icon {
        width: 40px;
        height: 40px;
        border-radius: 8px;
        background-color: rgba(64, 158, 255, 0.1);
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 12px;
        flex-shrink: 0;

        .el-icon {
          font-size: 20px;
          color: var(--el-color-primary);
        }
      }

      .stat-content {
        .stat-value {
          font-size: 18px;
          font-weight: 6px;
          color: var(--text-primary);
          margin-bottom: 2px;
        }

        .stat-label {
          font-size: 12px;
          color: var(--text-secondary);
        }
      }
    }
  }
}

.schedule-card {
  @extend .card-base;

  .class-info {
    .class-time {
      display: flex;
      align-items: center;
      font-size: 14px;
      color: var(--el-color-primary);
      font-weight: 5px;
      margin-bottom: 10px;

      .el-icon {
        margin-right: 6px;
      }

      .el-tag {
        margin-left: 8px;
        height: 20px;
        line-height: 18px;
      }
    }

    .class-name {
      font-size: 18px;
      font-weight: 6px;
      color: var(--text-primary);
      margin-bottom: 12px;
    }

    .class-meta {
      display: flex;
      flex-wrap: wrap;
      gap: 15px;
      margin-bottom: 20px;

      .meta-item {
        display: flex;
        align-items: center;
        font-size: 13px;
        color: var(--text-secondary);

        .el-icon {
          margin-right: 5px;
          font-size: 14px;
        }
      }
    }

    .class-actions {
      display: flex;
      gap: 10px;
    }
  }

  .no-class {
    padding: 30px 0;
  }
}

.task-card {
  @extend .card-base;

  .card-header {
    padding-bottom: 15px;

    .task-header-actions {
      display: flex;
      align-items: center;
      gap: 10px;

      .task-count-tag {
        margin-right: 5px;
      }

      .add-task-btn {
        margin-left: 0;
      }
    }
  }

  .task-list {
    .task-item {
      display: flex;
      align-items: center;
      padding: 12px 15px;
      border-bottom: 1px dashed rgba(0, 0, 0, 0.08);
      transition: all 0.2s ease;

      .dark & {
        border-bottom-color: rgba(255, 255, 255, 0.1);
      }

      &:hover {
        transform: translateX(3px);
      }

      &:last-child {
        border-bottom: none;
      }

      .el-checkbox {
        margin-right: 12px;
      }

      .task-content {
        flex: 1;

        .task-main {
          display: flex;
          align-items: center;
          margin-bottom: 4px;

          .task-text {
            font-size: 14px;
            color: var(--text-primary);
            margin-right: 8px;
          }
        }

        .task-meta {
          display: flex;
          align-items: center;
          gap: 8px;

          .el-tag {
            height: 22px;
            padding: 0 6px;
            line-height: 20px;
          }

          .task-course {
            font-size: 12px;
            color: var(--text-secondary);
            display: flex;
            align-items: center;

            .el-icon {
              margin-right: 3px;
              font-size: 12px;
            }
          }
        }
      }

      .task-more {
        color: var(--text-secondary);
        cursor: pointer;
        padding: 5px;
        border-radius: 4px;
        transition: all 0.2s;

        &:hover {
          color: var(--el-color-primary);
          background-color: rgba(64, 158, 255, 0.1);
        }
      }

      &.completed {
        .task-text {
          text-decoration: line-through;
          color: var(--text-secondary);
        }
      }

      &.urgent {
        .task-text {
          color: var(--el-color-danger);
        }
      }
    }
  }

  .empty-tasks {
    padding: 30px 0;
  }
}

.announcements-card {
  @extend .card-base;
  padding: 0;

  .card-header {
    padding: 20px 25px 15px;
    margin-bottom: 0;

    h3 {
      margin: 0;
      padding: 0;
    }
  }

  .card-content {
    padding: 0;

    .el-scrollbar {
      height: 280px;
    }
  }

  .announcement-list {
    padding: 0 20px;

    .announcement-item {
      display: flex;
      align-items: center;
      padding: 16px 0;
      cursor: pointer;
      border-bottom: 1px dashed rgba(0, 0, 0, 0.08);
      transition: all 0.2s ease;

      .dark & {
        border-bottom-color: rgba(255, 255, 255, 0.1);
      }

      &:hover {
        transform: translateX(5px);
      }

      &:last-child {
        border-bottom: none;
      }

      .announcement-badge {
        width: 32px;
        height: 32px;
        border-radius: 8px;
        display: flex;
        align-items: center;
        justify-content: center;
        margin-right: 15px;
        flex-shrink: 0;

        &.important {
          background-color: rgba(245, 108, 108, 0.1);
          color: var(--el-color-danger);
        }

        &.notice {
          background-color: rgba(255, 159, 28, 0.1);
          color: var(--el-color-warning);
        }

        &.info {
          background-color: rgba(64, 158, 255, 0.1);
          color: var(--el-color-primary);
        }

        .el-icon {
          font-size: 16px;
        }
      }

      .announcement-content {
        flex: 1;

        .announcement-title {
          font-size: 14px;
          font-weight: 5px;
          color: var(--text-primary);
          margin-bottom: 6px;
          line-height: 1.4;
        }

        .announcement-meta {
          display: flex;
          gap: 15px;
          font-size: 12px;
          color: var(--text-secondary);

          .meta-item {
            display: flex;
            align-items: center;

            .el-icon {
              margin-right: 4px;
              font-size: 12px;
            }
          }
        }
      }

      .announcement-arrow {
        color: var(--text-secondary);
        margin-left: 10px;
        flex-shrink: 0;
      }
    }
  }

  .empty-announcements {
    padding: 30px 0;
  }
}

@media (max-width: 768px) {
  .welcome-card {
    flex-direction: column;
    text-align: center;
    padding: 20px;

    .avatar-container {
      margin-right: 0;
      margin-bottom: 15px;
    }

    .welcome-content {
      margin-bottom: 15px;
    }

    .welcome-content h2 {
      font-size: 20px;
    }

    .welcome-stats {
      margin-left: 0;
      padding-left: 0;
      border-left: none;
      width: 100%;
      justify-content: center;
      gap: 20px;
    }

    .stat-value {
      font-size: 20px;
    }
  }

  .info-cards {
    grid-template-columns: 1fr;
    gap: 20px;
  }

  .card-header h3 {
    font-size: 16px;
  }

  .teaching-card .stats-grid {
    grid-template-columns: 1fr;
    gap: 12px;
  }

  .class-info .class-name {
    font-size: 16px;
  }

  .class-info .class-meta {
    flex-direction: column;
    gap: 8px;
    align-items: flex-start;
  }

  .announcements-card .card-header {
    padding: 15px 20px 12px;
  }

  .announcement-list {
    padding: 0 15px;
  }

  .announcement-item {
    padding: 12px 0;
  }

  .announcement-item .announcement-content .announcement-title {
    font-size: 13px;
  }
}

@media (max-width: 480px) {
  .teacher-home {
    padding: 0 10px;
  }

  .welcome-card {
    padding: 15px;
  }

  .card-base {
    padding: 15px;
  }

  .task-card .task-item {
    padding: 10px 12px;
  }

  .announcements-card .el-scrollbar {
    height: 250px;
  }

  .announcement-item:hover {
    transform: translateX(3px);
  }

  .class-actions {
    flex-direction: column;
  }

  .class-actions .el-button {
    width: 100%;
    margin-bottom: 8px;
  }
  
  .class-actions .el-button:last-child {
    margin-bottom: 0;
  }
}
</style>
