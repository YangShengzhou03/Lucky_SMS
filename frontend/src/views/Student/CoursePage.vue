<template>
  <div class="course-selection-container" @mousemove="handleMouseMove">
    <div class="modern-card">
      <div class="panel-header">
        <h3>
          <el-icon>
            <DataAnalysis />
          </el-icon>
          课程概览
        </h3>
        <div class="semester-selector">
          <el-select v-model="currentSemester" placeholder="选择学期" size="small" class="modern-select">
            <el-option v-for="item in semesters" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </div>
      </div>

      <div class="stats-grid">
        <div class="stat-card" v-for="(stat, index) in stats" :key="index" :style="{ '--card-color': stat.color }"
          @mouseenter="hoverStat(index)" @mouseleave="unhoverStat(index)">
          <div class="stat-icon">
            <div class="icon-bg"></div>
            <el-icon :size="24">
              <component :is="stat.icon" />
            </el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stat.value }}</div>
            <div class="stat-label">{{ stat.label }}</div>
          </div>
        </div>
      </div>
    </div>

    <div class="main-content">
      <div class="course-list-panel">
        <div class="course-list modern-card">
          <div class="panel-header">
            <h3>
              <el-icon>
                <Collection />
              </el-icon>
              可选课程
            </h3>
            <div class="search-filter">
              <el-input v-model="searchQuery" placeholder="搜索课程" size="small" clearable class="modern-input">
                <template #suffix>
                  <el-icon>
                    <Search />
                  </el-icon>
                </template>
              </el-input>
              <el-select v-model="filterCategory" placeholder="筛选分类" size="small" class="modern-select">
                <el-option v-for="category in categories" :key="category.value" :label="category.label"
                  :value="category.value" />
              </el-select>
            </div>
          </div>

          <div class="courses-container">
            <el-card v-for="course in filteredCourses" :key="course.id" class="course-card" shadow="hover">
              <div class="course-header">
                <h4>{{ course.name }}</h4>
                <span class="course-code">{{ course.code }}</span>
              </div>
              <div class="course-info">
                <div class="info-item">
                  <el-icon>
                    <User />
                  </el-icon>
                  <span>{{ course.teacher }}</span>
                </div>
                <div class="info-item">
                  <el-icon>
                    <Clock />
                  </el-icon>
                  <span>{{ course.time }}</span>
                </div>
                <div class="info-item">
                  <el-icon>
                    <Location />
                  </el-icon>
                  <span>{{ course.location }}</span>
                </div>
                <div class="info-item">
                  <el-icon>
                    <CreditCard />
                  </el-icon>
                  <span>{{ course.credits }}学分</span>
                </div>
                <div class="info-item capacity">
                  <el-progress :percentage="course.capacityUsed" :color="capacityColor(course.capacityUsed)"
                    :stroke-width="10" text-inside status="success" />
                  <span>{{ course.capacityUsed }}%</span>
                </div>
              </div>
              <div class="course-footer">
                <el-button @click="selectCourse(course)" :disabled="isCourseSelected(course.id)" type="primary"
                  size="small">
                  {{ isCourseSelected(course.id) ? '已选' : '选课' }}
                </el-button>
                <el-button v-if="isCourseSelected(course.id)" @click="dropSelectedCourse(course)" type="danger"
                  size="small">
                  退课
                </el-button>
              </div>
            </el-card>

            <div v-if="!filteredCourses.length && !searchQuery" class="no-courses">
              <el-empty description="暂无可选课程" />
            </div>

            <div v-if="!filteredCourses.length && searchQuery" class="no-courses">
              <el-empty image="search" description="没有找到匹配的课程" />
            </div>
          </div>

          <div class="pagination">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
              :current-page="currentPage" :page-sizes="[5, 10, 15]" :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper" :total="allCourses.length">
            </el-pagination>
          </div>
        </div>
      </div>

      <div class="selected-courses-panel">
        <div class="timetable-preview modern-card">
          <div class="panel-header">
            <h3>
              <el-icon>
                <Calendar />
              </el-icon>
              课程表预览
            </h3>
          </div>

          <div class="timetable-container">
            <div class="timetable-header">
              <div class="time-slot-header"></div>
              <div class="day-header" v-for="day in daysOfWeek" :key="day">{{ day }}</div>
            </div>

            <div class="timetable-grid">
              <div class="time-row" v-for="(timeSlot, timeIndex) in timeSlots" :key="timeIndex">
                <div class="time-label">{{ timeSlot }}</div>
                <div class="day-column" v-for="(day, dayIndex) in daysOfWeek" :key="dayIndex">
                  <!-- 这里是关键修改：只渲染属于当前天的课程 -->
                  <div class="course-block"
                    v-for="course in selectedCourses.filter(c => c.schedule && c.schedule.some(s => s.day === dayIndex + 1 && s.timeSlot === timeIndex))"
                    :key="course.id" :style="{
                      left: '0',
                      top: '0',
                      height: '72px',
                      width: '100%'
                    }">
                    <div class="course-block-inner"
                      :style="{ backgroundColor: courseColors[course.id % courseColors.length] }">
                      <div class="course-name">{{ course.name }}</div>
                      <div class="course-location">{{ course.location }}</div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import {
  DataAnalysis,
  Collection,
  Check,
  Calendar,
  CreditCard,
  User,
  Clock,
  Location,
  Search
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getAvailableCourses, getSelectedCourses, selectCourse as apiSelectCourse, dropCourse } from '@/api/student'

const currentSemester = ref('2023-2024-2')
const semesters = ref([
  { value: '2023-2024-2', label: '2023-2024学年第二学期' },
  { value: '2023-2024-1', label: '2023-2024学年第一学期' }
])

const hoveredStat = ref(null)

const maxCredits = ref(25) // 初始值，将从后端获取

const searchQuery = ref('')
const filterCategory = ref('all')
const categories = ref([
  { value: 'all', label: '全部' },
  { value: 'compulsory', label: '必修课' },
  { value: 'elective', label: '选修课' },
  { value: 'professional', label: '专业课' },
  { value: 'general', label: '通识课' }
])

const currentPage = ref(1)
const pageSize = ref(5)

// 从后端获取的课程数据
const allCourses = ref([])
const selectedCourses = ref([])

// 加载可选课程数据
const loadAvailableCourses = async () => {
  try {
    const response = await getAvailableCourses()
    if (response.code === 200) {
      // 转换后端数据为前端需要的格式
      allCourses.value = response.data.map(course => ({
        id: course.courseId,
        name: course.courseName,
        code: course.courseCode,
        teacher: course.teacherName,
        time: course.schedule.map(s => `${getDayName(s.day)} ${getTimeSlotName(s.timeSlot)}`).join(', '),
        location: course.location,
        credits: course.credits,
        capacityUsed: Math.round((course.selectedCount / course.capacity) * 100),
        category: course.courseType.toLowerCase(),
        schedule: course.schedule
      }))
    }
  } catch (error) {
    ElMessage.error('获取可选课程失败')
    console.error('获取可选课程失败:', error)
  }
}

// 加载已选课程数据
const loadSelectedCourses = async () => {
  try {
    const response = await getSelectedCourses()
    if (response.code === 200) {
      // 转换后端数据为前端需要的格式
      selectedCourses.value = response.data.map(course => ({
        id: course.courseId,
        name: course.courseName,
        code: course.courseCode,
        teacher: course.teacherName,
        time: course.schedule.map(s => `${getDayName(s.day)} ${getTimeSlotName(s.timeSlot)}`).join(', '),
        location: course.location,
        credits: course.credits,
        capacityUsed: Math.round((course.selectedCount / course.capacity) * 100),
        category: course.courseType.toLowerCase(),
        schedule: course.schedule
      }))
    }
  } catch (error) {
    ElMessage.error('获取已选课程失败')
    console.error('获取已选课程失败:', error)
  }
}

// 将星期数字转换为名称
const getDayName = (day) => {
  const days = ['周一', '周二', '周三', '周四', '周五', '周六', '周日']
  return days[day - 1] || ''
}

// 将时间段转换为名称
const getTimeSlotName = (timeSlot) => {
  const slots = ['1-2节', '3-4节', '5-6节', '7-8节', '9-10节']
  return slots[timeSlot] || ''
}

const stats = computed(() => [
  {
    value: allCourses.value.length,
    label: '可选课程',
    icon: Collection,
    color: '#6366f1'
  },
  {
    value: selectedCourses.value.length,
    label: '已选课程',
    icon: Check,
    color: '#10b981'
  },
  {
    value: `${selectedCredits.value}/${maxCredits.value}`,
    label: '已选学分',
    icon: CreditCard,
    color: '#f59e0b'
  },
  {
    value: '7天',
    label: '选课截止',
    icon: Calendar,
    color: '#ef4444'
  }
])

const daysOfWeek = ref(['周一', '周二', '周三', '周四', '周五'])
const timeSlots = ref(['08:00-09:40', '10:00-11:40', '14:00-15:40'])
const courseColors = ref([
  'rgba(99, 102, 241, 0.8)',
  'rgba(16, 185, 129, 0.8)',
  'rgba(245, 158, 11, 0.8)',
  'rgba(239, 68, 68, 0.8)',
  'rgba(139, 92, 246, 0.8)',
  'rgba(236, 72, 153, 0.8)'
])

const filteredCourses = computed(() => {
  let filtered = allCourses.value

  if (filterCategory.value !== 'all') {
    filtered = filtered.filter(course => course.category === filterCategory.value)
  }

  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(course =>
      course.name.toLowerCase().includes(query) ||
      course.code.toLowerCase().includes(query) ||
      course.teacher.toLowerCase().includes(query)
    )
  }

  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filtered.slice(start, end)
})

const selectedCredits = computed(() => {
  return selectedCourses.value.reduce((total, course) => total + course.credits, 0)
})

const handleMouseMove = (e) => {
  document.documentElement.style.setProperty('--mouse-x', `${e.clientX}px`)
  document.documentElement.style.setProperty('--mouse-y', `${e.clientY}px`)
}

const hoverStat = (index) => {
  hoveredStat.value = index
}

const unhoverStat = (index) => {
  if (hoveredStat.value === index) {
    hoveredStat.value = null
  }
}

const selectCourse = async (course) => {
  if (selectedCredits.value + course.credits > maxCredits.value) {
    ElMessage.warning(`已达到学分上限(${maxCredits.value}学分)，无法选择更多课程`)
    return
  }

  const hasConflict = selectedCourses.value.some(selectedCourse => {
    return selectedCourse.schedule.some(s =>
      course.schedule.some(cs => s.day === cs.day && s.timeSlot === cs.timeSlot)
    )
  })

  if (hasConflict) {
    ElMessage.warning('所选课程与已选课程时间冲突')
    return
  }

  try {
    const response = await apiSelectCourse(course.id)
    if (response.code === 200) {
      // 选课成功，重新加载课程数据
      await loadAvailableCourses()
      await loadSelectedCourses()
      ElMessage.success(`已成功选择课程：${course.name}`)
    } else {
      ElMessage.error(response.message || '选课失败')
    }
  } catch (error) {
    ElMessage.error('选课请求失败')
    console.error('选课失败:', error)
  }
}

// 添加退课功能
const dropSelectedCourse = async (course) => {
  try {
    const response = await dropCourse(course.id)
    if (response.code === 200) {
      // 退课成功，重新加载课程数据
      await loadAvailableCourses()
      await loadSelectedCourses()
      ElMessage.success(`已成功退选课程：${course.name}`)
    } else {
      ElMessage.error(response.message || '退课失败')
    }
  } catch (error) {
    ElMessage.error('退课请求失败')
    console.error('退课失败:', error)
  }
}

const isCourseSelected = (courseId) => {
  return selectedCourses.value.some(course => course.id === courseId)
}

const capacityColor = (capacity) => {
  if (capacity >= 90) return '#f56c6c'
  if (capacity >= 70) return '#f59e0b'
  return '#67c23a'
}

const handleSizeChange = (newSize) => {
  pageSize.value = newSize
}

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
}

onMounted(async () => {
  // 加载课程数据
  await loadAvailableCourses()
  await loadSelectedCourses()
})
</script>

<style scoped lang="scss">
:root {
  --dark-bg: #1e293b;
  --dark-card: rgba(51, 65, 85, 0.8);
  --dark-card-hover: rgba(66, 84, 111, 0.8);
  --dark-border: rgba(100, 116, 139, 0.3);
  --dark-text-primary: #f8fafc;
  --dark-text-secondary: #cbd5e1;
  --dark-text-tertiary: #94a3b8;
  --dark-input-bg: rgba(30, 41, 59, 0.7);
  --dark-input-border: rgba(100, 116, 139, 0.5);
  --dark-progress-bg: rgba(148, 163, 184, 0.2);
}

.course-selection-container {
  display: flex;
  flex-direction: column;
  gap: 30px;
  margin: 0 auto;
  padding: 0px 15px;
  --mouse-x: 0;
  --mouse-y: 0;
}

.main-content {
  display: flex;
  flex-direction: column;
  gap: 30px;
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

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;

  h3 {
    margin: 0;
    font-size: 20px;
    font-weight: 600;
    color: #2c3e50;
    display: flex;
    align-items: center;

    .el-icon {
      margin-right: 12px;
      font-size: 22px;
      color: #409eff;
    }

    .dark & {
      color: var(--dark-text-primary);
    }
  }
}

.search-filter {
  display: flex;
  gap: 12px;

  .modern-input {
    width: 200px;

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
    }

    .dark & .el-input__inner {
      background-color: var(--dark-input-bg);
      border: 1px solid var(--dark-input-border);
      color: var(--dark-text-primary);

      &::placeholder {
        color: var(--dark-text-tertiary);
      }

      &:hover {
        border-color: rgba(199, 210, 254, 0.5);
      }

      &:focus {
        border-color: #6366f1;
        box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.3);
      }
    }
  }

  .modern-select {
    width: 150px;

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
    }

    .dark & .el-input__inner {
      background-color: var(--dark-input-bg);
      border: 1px solid var(--dark-input-border);
      color: var(--dark-text-primary);

      &::placeholder {
        color: var(--dark-text-tertiary);
      }

      &:hover {
        border-color: rgba(199, 210, 254, 0.5);
      }

      &:focus {
        border-color: #6366f1;
        box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.3);
      }
    }
  }
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
    transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
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
          color: var(--dark-text-primary);
        }
      }

      .stat-label {
        font-size: 0.875rem;
        font-weight: 500;
        color: #64748b;
        line-height: 1.4;

        .dark & {
          color: var(--dark-text-secondary);
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

.courses-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.course-card {
  display: flex;
  flex-direction: column;
  border-radius: 16px;
  padding: 20px;
  transition: all 0.3s ease;
  background: rgba(255, 255, 255, 0.9);
  border: 1px solid rgba(226, 232, 240, 0.6);
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);

  .dark & {
    color: #fff;
    background: rgba(51, 65, 85, 0.9);
    border: 1px solid rgba(100, 116, 139, 0.3);
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

  .course-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 16px;

    h4 {
      margin: 0;
      font-size: 18px;
      font-weight: 600;
      color: #2c3e50;

      .dark & {
        color: var(--dark-text-primary);
      }
    }

    .course-code {
      font-size: 12px;
      color: #64748b;
      background-color: rgba(226, 232, 240, 0.5);
      padding: 2px 8px;
      border-radius: 4px;

      .dark & {
        color: var(--dark-text-primary);
        background-color: rgba(100, 116, 139, 0.2);
      }
    }
  }

  .course-info {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 12px;
    margin-bottom: 16px;

    .info-item {
      display: flex;
      align-items: center;
      font-size: 14px;

      .el-icon {
        margin-right: 8px;
        color: #64748b;
        width: 18px;
        text-align: center;
      }

      span {
        color: #4b5563;

        .dark & {
          color: var(--dark-text-primary);
        }
      }
    }

    .capacity {
      grid-column: 1 / span 2;
      margin-top: 4px;

      .el-progress__text {
        font-size: 12px !important;

        .dark & {
          color: var(--dark-text-primary) !important;
        }
      }
    }
  }

  .course-footer {
    display: flex;
    justify-content: flex-end;
  }
}

.no-courses {
  padding: 40px 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}

.timetable-container {
  overflow-x: auto;
}

.timetable-header {
  display: flex;
  margin-bottom: 8px;

  .time-slot-header {
    width: 100px;
    font-weight: 500;
    color: #64748b;
  }

  .day-header {
    flex: 1;
    text-align: center;
    font-weight: 500;
    color: #64748b;
    padding: 8px 0;
  }
}

.timetable-grid {
  position: relative;
  min-height: 400px;

  .time-row {
    display: flex;
    height: 80px;
    border-bottom: 1px solid rgba(226, 232, 240, 0.5);

    .dark & {
      border-bottom: 1px solid rgba(100, 116, 139, 0.2);
    }

    .time-label {
      width: 100px;
      display: flex;
      align-items: center;
      padding-left: 12px;
      font-size: 14px;
      color: #64748b;

      .dark & {
        color: var(--dark-text-primary);
      }
    }

    .day-column {
      flex: 1;
      position: relative;
      border-right: 1px solid rgba(226, 232, 240, 0.5);

      .dark & {
        border-right: 1px solid rgba(100, 116, 139, 0.2);
      }

      &:last-child {
        border-right: none;
      }
    }
  }

  .course-block {
    position: absolute;
    width: calc(100% - 16px);
    margin: 4px 0px;
    border-radius: 8px;
    overflow: hidden;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
    transition: all 0.2s ease;
    opacity: 0.95;

    &:hover {
      opacity: 1;
      transform: scale(1.02);
      z-index: 10;
    }

    .course-block-inner {
      display: flex;
      flex-direction: column;
      justify-content: center;
      height: 100%;
      padding: 8px 12px;

      .course-name {
        color: white;
        font-weight: 500;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
        margin-bottom: 2px;
      }

      .course-location {
        color: rgba(255, 255, 255, 0.8);
        font-size: 12px;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
      }
    }
  }
}

.dark-mode-toggle {
  position: fixed;
  bottom: 24px;
  right: 24px;
  z-index: 100;

  .el-switch__core {
    background-color: rgba(100, 116, 139, 0.5);
  }

  .el-switch__core::after {
    background-color: white;
  }
}
</style>