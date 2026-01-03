<template>
  <div class="course-dashboard" :class="{ 'dark': isDarkMode }" @mousemove="handleMouseMove">
    <div class="course-overview-card modern-card" ref="overviewRef">
      <div class="card-header">
        <h2>
          <el-icon>
            <DataAnalysis />
          </el-icon>
          课程概览
        </h2>
        <div class="semester-selector">
          <el-select v-model="currentSemester" placeholder="选择学期" size="small" class="modern-select">
            <el-option v-for="item in semesters" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </div>
      </div>

      <div class="stats-grid">
        <div class="stat-card" v-for="(stat, index) in stats" :key="index" :style="{ '--card-color': stat.color }">
          <div class="stat-icon">
            <div class="icon-bg"></div>
            <el-icon :size="24">
              <component :is="stat.icon" />
            </el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">
              <animated-number :value="stat.value" :formatValue="formatStatValue" :duration="800" />
            </div>
            <div class="stat-label">{{ stat.label }}</div>
          </div>
          <div class="stat-trend" v-if="stat.trend">
            <el-icon :color="stat.trend > 0 ? '#f56c6c' : '#67c23a'">
              <CaretTop v-if="stat.trend > 0" />
              <CaretBottom v-else />
            </el-icon>
            <span :style="{ color: stat.trend > 0 ? '#f56c6c' : '#67c23a' }">
              {{ Math.abs(stat.trend) }}%
            </span>
          </div>
        </div>
      </div>
    </div>

    <div class="course-content">
      <div class="course-schedule modern-card" ref="scheduleRef">
        <div class="card-header">
          <h3>
            <el-icon>
              <Calendar />
            </el-icon> 本周课程表
          </h3>
          <div class="view-toggle">
            <el-radio-group v-model="scheduleView">
              <el-radio-button label="week">周视图</el-radio-button>
              <el-radio-button label="day">日视图</el-radio-button>
            </el-radio-group>
          </div>
        </div>

        <div class="schedule-grid">
          <div class="grid-header">
            <div class="time-column"></div>
            <div class="day-column" v-for="day in weekdays" :key="day">{{ day }}</div>
          </div>

          <div class="grid-body">
            <div class="time-slot" v-for="time in timeSlots" :key="time">
              <div class="time-label">{{ time }}</div>
              <div class="day-slot" v-for="day in weekdays" :key="day">
                <div class="course-block" v-for="course in getCoursesByDayAndTime(day, time)" :key="course.id"
                  :style="{ backgroundColor: course.color }" @mouseenter="handleCourseHover(course)"
                  @mouseleave="handleCourseLeave">
                  <div class="course-name">{{ course.name }}</div>
                  <div class="course-teacher">{{ course.teacher }}</div>
                  <div class="course-location">{{ course.location }}</div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="course-progress modern-card" ref="progressRef">
        <div class="card-header">
          <h3>
            <el-icon>
              <Notebook />
            </el-icon> 课程进度
          </h3>
          <div class="progress-filter">
            <el-select v-model="progressFilter" placeholder="筛选课程" size="small">
              <el-option label="全部课程" value="all" />
              <el-option label="进行中" value="ongoing" />
              <el-option label="已完成" value="completed" />
            </el-select>
          </div>
        </div>

        <div class="progress-list">
          <el-card class="progress-card" v-for="course in filteredCourses" :key="course.id" shadow="hover"
            :style="{ transform: 'translateZ(0)' }">
            <div class="card-content">
              <div class="course-info">
                <div class="course-name">{{ course.name }}</div>
                <div class="course-code">{{ course.code }}</div>
              </div>

              <div class="progress-bar">
                <el-progress :percentage="course.progress" :color="courseProgressColor(course.progress)"
                  :stroke-width="10" />
              </div>

              <div class="course-details">
                <div class="detail-item">
                  <span class="label">授课教师:</span>
                  <span class="value">{{ course.teacher }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">学分:</span>
                  <span class="value">{{ course.credits }}</span>
                </div>
                <div class="detail-item">
                  <span class="label">进度:</span>
                  <span class="value">{{ course.progress }}%</span>
                </div>
              </div>
            </div>

            <div class="card-actions">
              <el-button type="text" size="small" @click="viewCourseDetails(course)">
                查看详情 <el-icon>
                  <ArrowRight />
                </el-icon>
              </el-button>
            </div>
          </el-card>
        </div>
      </div>
    </div>

    <div class="history-courses modern-card" ref="historyRef">
      <div class="card-header">
        <h3>
          <el-icon>
            <History />
          </el-icon> 历史课程记录
        </h3>
        <div class="history-filter">
          <el-input v-model="historySearch" placeholder="搜索课程" size="small" clearable>
            <template #suffix>
              <el-icon>
                <Search />
              </el-icon>
            </template>
          </el-input>
        </div>
      </div>

      <el-table :data="filteredHistoryCourses" border stripe class="history-table">
        <el-table-column prop="semester" label="学期" width="120" />
        <el-table-column prop="name" label="课程名称" min-width="180" />
        <el-table-column prop="code" label="课程代码" width="120" />
        <el-table-column prop="teacher" label="授课教师" width="120" />
        <el-table-column prop="credits" label="学分" width="80" />
        <el-table-column prop="score" label="成绩" width="80" />
        <el-table-column prop="status" label="状态" width="100" :formatter="formatCourseStatus" />
      </el-table>

      <div class="pagination" v-if="filteredHistoryCourses.length > 0">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"
          :page-sizes="[5, 10, 20]" :page-size="pageSize" :total="filteredHistoryCourses.length"
          layout="total, sizes, prev, pager, next" small />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import {
  Calendar,
  Notebook,
  ArrowRight,
  Search,
  DataAnalysis,
  CaretTop,
  CaretBottom,
  Collection,
  Check,
  Close,
  Star
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getStudentSchedule, getGradesWithPagination } from '@/api/student'

const isDarkMode = ref(false)

const courseStatusConfig = {
  passed: { label: '通过', type: 'success' },
  failed: { label: '未通过', type: 'danger' },
  retaking: { label: '重修中', type: 'warning' },
  auditing: { label: '旁听', type: 'info' }
}

const currentSemester = ref('2023-2024-2')
const semesters = ref([
  { value: '2023-2024-2', label: '2023-2024学年第二学期' },
  { value: '2023-2024-1', label: '2023-2024学年第一学期' },
  { value: '2022-2023-2', label: '2022-2023学年第二学期' },
  { value: '2022-2023-1', label: '2022-2023学年第一学期' }
])

const weekdays = ref(['周一', '周二', '周三', '周四', '周五', '周六', '周日'])
const timeSlots = ref(['08:00', '10:00', '14:00', '16:00', '19:00'])

const currentCourses = ref([])
const historyCourses = ref([])

const currentPage = ref(1)
const pageSize = ref(10)
const hoveredCourse = ref(null)
const scheduleView = ref('week')
const progressFilter = ref('all')
const historySearch = ref('')

const stats = computed(() => [
  {
    value: currentCourses.value.length,
    label: '当前课程',
    icon: Collection,
    color: '#6366f1',
    trend: 5.2
  },
  {
    value: completedCoursesCount.value,
    label: '已完成课程',
    icon: Check,
    color: '#10b981',
    trend: 12.8
  },
  {
    value: failedCoursesCount.value,
    label: '未通过课程',
    icon: Close,
    color: '#ef4444',
    trend: -3.5
  },
  {
    value: avgScore.value,
    label: '平均成绩',
    icon: Star,
    color: '#f59e0b',
    trend: 2.1
  }
])

const completedCoursesCount = computed(() => {
  return historyCourses.value.filter(course => course.status === 'passed').length
})

const failedCoursesCount = computed(() => {
  return historyCourses.value.filter(course => course.status === 'failed').length
})

const avgScore = computed(() => {
  const passedCourses = historyCourses.value.filter(course => course.status === 'passed')
  if (passedCourses.length === 0) return 0
  const totalScore = passedCourses.reduce((sum, course) => sum + course.score, 0)
  return Math.round(totalScore / passedCourses.length)
})

const filteredCourses = computed(() => {
  if (progressFilter.value === 'all') return currentCourses.value
  if (progressFilter.value === 'ongoing') return currentCourses.value.filter(c => c.progress < 100)
  return currentCourses.value.filter(c => c.progress === 100)
})

const filteredHistoryCourses = computed(() => {
  if (!historySearch.value.trim()) return historyCourses.value

  const searchTerm = historySearch.value.toLowerCase().trim()
  return historyCourses.value.filter(course =>
    course.name.toLowerCase().includes(searchTerm) ||
    course.code.toLowerCase().includes(searchTerm) ||
    course.teacher.toLowerCase().includes(searchTerm)
  )
})

const getCoursesByDayAndTime = (day, time) => {
  return currentCourses.value.filter(course =>
    course.schedule.some(s => s.day === day && s.time === time)
  )
}

const courseProgressColor = (progress) => {
  if (progress >= 80) return '#67c23a'
  if (progress >= 60) return '#e6a23c'
  return '#f56c6c'
}

const formatCourseStatus = (row) => {
  return courseStatusConfig[row.status]?.label || '未知'
}

const viewCourseDetails = (course) => {
  ElMessage.info(`查看课程详情: ${course.name}`)
}

const handleSizeChange = (size) => {
  pageSize.value = size
}

const handleCurrentChange = (page) => {
  currentPage.value = page
}

const handleCourseHover = (course) => {
  hoveredCourse.value = course
}

const handleCourseLeave = () => {
  hoveredCourse.value = null
}

const handleMouseMove = (e) => {
  const x = e.clientX / window.innerWidth * 100
  const y = e.clientY / window.innerHeight * 100
  document.documentElement.style.setProperty('--mouse-x', `${x}px`)
  document.documentElement.style.setProperty('--mouse-y', `${y}px`)
}

const formatStatValue = (value) => {
  return typeof value === 'number' && !Number.isInteger(value) ? value.toFixed(1) : value
}

const loadScheduleData = async () => {
  try {
    const res = await getStudentSchedule({
      semester: currentSemester.value
    })
    if (res.code === 200) {
      const courses = res.data.courses || []
      currentCourses.value = courses.map(course => ({
        id: course.id,
        name: course.name,
        code: course.code,
        teacher: course.teacher,
        credits: course.credits,
        progress: course.progress || 0,
        schedule: course.schedule || [],
        color: ['#409eff', '#67c23a', '#e6a23c', '#f56c6c', '#909399'][Math.floor(Math.random() * 5)]
      }))
    }
  } catch (error) {
    console.error('获取课程表失败:', error)
    ElMessage.error('获取课程表失败')
    currentCourses.value = []
  }
}

const loadHistoryData = async () => {
  try {
    const res = await getGradesWithPagination({
      page: 1,
      size: 100
    })
    if (res.code === 200) {
      const grades = res.data.courseGrades || []
      historyCourses.value = grades.map(grade => ({
        semester: grade.semester || '--',
        name: grade.courseName || '--',
        code: grade.courseCode || '--',
        teacher: grade.teacher || '--',
        credits: grade.credits || 0,
        score: grade.score || 0,
        status: grade.score >= 60 ? 'passed' : 'failed'
      }))
    }
  } catch (error) {
    console.error('获取历史课程失败:', error)
    ElMessage.error('获取历史课程失败')
    historyCourses.value = []
  }
}

onMounted(async () => {
  await loadScheduleData()
  await loadHistoryData()
})
</script>

<style scoped lang="scss">
.course-dashboard {
  display: flex;
  flex-direction: column;
  transition: background-color 0.3s ease;
  gap: 30px;
  padding: 0 15px;
  --mouse-x: 0;
  --mouse-y: 0;
}

.modern-card {
  position: relative;
  border-radius: 8px;
  padding: 20px;
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

  &:hover {
    box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
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
  margin-bottom: 1.5rem;
  padding-bottom: 0.75rem;
  border-bottom: 1px solid rgba(226, 232, 240, 0.5);

  .dark & {
    border-color: rgba(74, 85, 104, 0.3);
  }

  h2 {
    font-size: 1.25rem;
    margin: 0;
    display: flex;
    align-items: center;
    font-weight: 600;
    color: #1e293b;
    letter-spacing: -0.025em;

    .dark & {
      color: rgba(248, 250, 252, 0.9);
    }

    .el-icon {
      margin-right: 0.75rem;
      font-size: 1.5rem;
      color: #6366f1;

      .dark & {
        color: #818cf8;
      }
    }
  }

  h3 {
    font-size: 1.125rem;
    margin: 0;
    display: flex;
    align-items: center;
    font-weight: 600;
    color: #333;
    letter-spacing: -0.025em;

    .dark & {
      color: rgba(247, 250, 252, 0.9);
    }

    .el-icon {
      margin-right: 0.75rem;
      font-size: 1.25rem;
      color: #409eff;

      .dark & {
        color: #63b3ed;
      }
    }
  }
}

.modern-select {
  .el-input__inner {
    background-color: rgba(255, 255, 255, 0.7);
    border: 1px solid rgba(226, 232, 240, 0.8);
    border-radius: 12px;
    padding: 0 12px;
    height: 36px;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
    transition: all 0.2s ease;

    &:hover {
      border-color: #c7d2fe;
    }

    &:focus {
      border-color: #6366f1;
      box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.2);
    }

    .dark & {
      background-color: rgba(30, 41, 59, 0.7);
      border-color: rgba(74, 85, 104, 0.5);
      color: rgba(248, 250, 252, 0.9);

      &:hover {
        border-color: #4f46e5;
      }

      &:focus {
        border-color: #6366f1;
        box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.3);
      }
    }
  }
}

.course-content {
  display: flex;
  flex-direction: column;
  gap: 30px
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
  gap: 1.5rem;

  @media (max-width: 768px) {
    grid-template-columns: 1fr 1fr;
    gap: 1rem;
  }

  @media (max-width: 480px) {
    grid-template-columns: 1fr;
  }

  .stat-card {
    display: flex;
    align-items: center;
    padding: 1.5rem;
    border-radius: 14px;
    background: rgba(255, 255, 255, 0.9);
    backdrop-filter: blur(8px);
    transition: all 0.15s ease;
    position: relative;
    overflow: hidden;
    border: 1px solid rgba(226, 232, 240, 0.6);

    .dark & {
      background: rgba(30, 41, 59, 0.9);
      border-color: rgba(74, 85, 104, 0.4);
    }

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1);
      border-color: rgba(199, 210, 254, 0.8);
    }

    .dark &:hover {
      transform: translateY(-4px);
      box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.3);
      border-color: rgba(99, 102, 241, 0.5);
    }

    .stat-icon {
      position: relative;
      margin-right: 1.25rem;
      width: 48px;
      height: 48px;
      border-radius: 12px;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-shrink: 0;
      z-index: 1;

      .icon-bg {
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        border-radius: 12px;
        background-color: var(--card-color);
        opacity: 0.1;
        transition: all 0.3s ease;
      }

      .el-icon {
        color: var(--card-color);
        z-index: 1;
      }
    }

    .stat-content {
      flex: 1;
      min-width: 0;
      z-index: 1;

      .stat-value {
        font-size: 1.75rem;
        font-weight: 700;
        color: #1e293b;
        line-height: 1.2;
        margin-bottom: 0.25rem;
        font-feature-settings: "tnum";

        .dark & {
          color: rgba(248, 250, 252, 0.9);
        }
      }

      .stat-label {
        font-size: 0.875rem;
        font-weight: 500;
        color: #64748b;
        line-height: 1.4;

        .dark & {
          color: rgba(203, 213, 225, 0.8);
        }
      }
    }

    .stat-trend {
      display: flex;
      align-items: center;
      margin-left: 0.75rem;
      font-size: 0.75rem;
      font-weight: 600;

      .el-icon {
        margin-right: 0.25rem;
        font-size: 0.875rem;
      }
    }
  }
}

.schedule-grid {
  overflow-x: auto;

  .grid-header {
    display: grid;
    grid-template-columns: 80px repeat(7, 1fr);
    border-bottom: 1px solid #ebeef5;

    .dark & {
      border-color: rgba(255, 255, 255, 0.1);
    }

    .time-column,
    .day-column {
      padding: 12px;
      font-weight: 500;
      text-align: center;
      color: #606266;

      .dark & {
        color: rgba(255, 255, 255, 0.7);
      }
    }
  }

  .grid-body {
    .time-slot {
      display: grid;
      grid-template-columns: 80px repeat(7, 1fr);
      border-bottom: 1px solid #f2f3f5;

      .dark & {
        border-color: rgba(255, 255, 255, 0.05);
      }

      .time-label {
        padding: 12px;
        text-align: center;
        color: #909399;
        font-size: 14px;
        border-right: 1px solid #f2f3f5;

        .dark & {
          color: rgba(255, 255, 255, 0.5);
          border-color: rgba(255, 255, 255, 0.05);
        }
      }

      .day-slot {
        padding: 4px;

        .course-block {
          padding: 8px;
          border-radius: 8px;
          margin-bottom: 4px;
          color: white;
          font-size: 13px;
          box-shadow: 0 2px 6px rgba(0, 0, 0, 0.1);
          transition: all 0.2s ease;

          &:hover {
            transform: translateY(-2px);
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
          }

          .course-name {
            font-weight: 500;
            margin-bottom: 2px;
          }

          .course-teacher,
          .course-location {
            font-size: 12px;
            opacity: 0.8;
          }
        }
      }
    }
  }
}

.progress-list {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 16px;

  .progress-card {
    transition: all 0.3s ease;
    border: 1px solid rgba(226, 232, 240, 0.6);
    border-radius: 14px;
    overflow: hidden;

    .dark & {
      background: #1e293b;
      border-color: rgba(74, 85, 104, 0.4);
    }

    &:hover {
      transform: translateY(-4px);
      box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1);
      border-color: rgba(199, 210, 254, 0.8);
    }

    .dark &:hover {
      transform: translateY(-4px);
      box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.3);
      border-color: rgba(99, 102, 241, 0.5);
    }

    .course-info {
      display: flex;
      justify-content: space-between;
      margin-bottom: 12px;

      .course-name {
        font-size: 16px;
        font-weight: 500;

        .dark & {
          color: rgba(255, 255, 255, 0.9);
        }
      }

      .course-code {
        font-size: 13px;
        color: #909399;

        .dark & {
          color: rgba(255, 255, 255, 0.6);
        }
      }
    }

    .progress-bar {
      margin-bottom: 16px;
    }

    .course-details {
      display: grid;
      grid-template-columns: 1fr 1fr;
      gap: 8px;

      .detail-item {
        display: flex;
        align-items: center;

        .label {
          font-size: 13px;
          color: #909399;
          margin-right: 8px;

          .dark & {
            color: rgba(255, 255, 255, 0.6);
          }
        }

        .value {
          font-size: 13px;
          color: #333;

          .dark & {
            color: rgba(255, 255, 255, 0.9);
          }
        }
      }
    }

    .card-actions {
      margin-top: 16px;
      text-align: right;
    }
  }
}

.history-table {
  .el-table__header th {
    background-color: rgba(245, 247, 250, 0.7);

    .dark & {
      background-color: rgba(40, 45, 55, 0.7);
    }
  }

  .el-table__body td {
    transition: all 0.2s ease;
  }

  .el-table__row:hover {
    td {
      background-color: rgba(64, 158, 255, 0.05) !important;

      .dark & {
        background-color: rgba(64, 158, 255, 0.1) !important;
      }
    }
  }
}

.pagination {
  margin-top: 20px;
  text-align: center;
}

.view-toggle,
.progress-filter,
.history-filter {

  .el-select,
  .el-input {
    width: 180px;
  }
}

@media (max-width: 768px) {
  .schedule-grid {
    min-width: 700px;
  }

  .progress-list {
    grid-template-columns: 1fr;
  }

  .card-header {
    flex-direction: column;
    align-items: flex-start;

    h3 {
      margin-bottom: 12px;
    }

    .view-toggle,
    .progress-filter,
    .history-filter {
      width: 100%;

      .el-select,
      .el-input {
        width: 100%;
      }
    }
  }
}
</style>