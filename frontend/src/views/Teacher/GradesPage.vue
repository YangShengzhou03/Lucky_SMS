<template>
  <div class="grades-dashboard" :class="{ 'dark': isDarkMode }" @mousemove="handleMouseMove">
    <!-- 第一张卡片：成绩统计概览 -->
    <div class="grades-stats modern-card">
      <div class="card-header">
        <h2>成绩统计概览</h2>
        <div v-if="selectedCourse && selectedClass" class="header-actions">
          <el-button @click="refreshStats" type="text" size="small" class="refresh-btn">
            <el-icon :size="16">
              <Refresh />
            </el-icon> 刷新数据
          </el-button>
        </div>
      </div>
      <div class="stats-container">
        <div class="stats-card" :class="{ 'high-light': selectedCourse && selectedClass }">
          <div class="stat-header">
            <h3>当前课程平均分</h3>
            <div class="stat-value">{{ courseAverageScore || '0' }}</div>
          </div>
          <div class="stat-trend">
            <span v-if="courseAverageScoreTrend > 0" class="trend-up">
              <el-icon :size="16">
                <ArrowUp />
              </el-icon>
              {{ courseAverageScoreTrend.toFixed(1) }}%
            </span>
            <span v-else-if="courseAverageScoreTrend < 0" class="trend-down">
              <el-icon :size="16">
                <ArrowDown />
              </el-icon>
              {{ Math.abs(courseAverageScoreTrend).toFixed(1) }}%
            </span>
            <span v-else>持平</span>
            <span class="text-sm ml-2">vs 上学期</span>
          </div>
        </div>

        <div class="stats-card" :class="{ 'high-light': selectedCourse && selectedClass }">
          <div class="stat-header">
            <h3>及格率</h3>
            <div class="stat-value">{{ coursePassRate || '0%' }}</div>
          </div>
          <div class="stat-progress">
            <el-progress :percentage="coursePassRateValue" :stroke-width="6" :color="coursePassRateColor" />
          </div>
        </div>

        <div class="stats-card" :class="{ 'high-light': selectedCourse && selectedClass }">
          <div class="stat-header">
            <h3>最高分/最低分</h3>
            <div class="stat-value">{{ courseMaxScore || '0' }}/{{ courseMinScore || '0' }}</div>
          </div>
          <div class="stat-meta">
            <div class="text-sm text-secondary">
              {{ courseMaxStudent || '暂无数据' }} / {{ courseMinStudent || '暂无数据' }}
            </div>
          </div>
        </div>

        <div class="stats-card" :class="{ 'high-light': selectedCourse && selectedClass }">
          <div class="stat-header">
            <h3>待提交成绩</h3>
            <div class="stat-value">{{ pendingGradesCount || '0' }}</div>
          </div>
          <div class="stat-meta">
            <el-button @click="checkPendingGrades" type="text" size="small" class="view-details-btn">
              查看详情
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 第二张卡片：成绩管理 -->
    <div class="grades-management modern-card">
      <!-- 卡片头部 -->
      <div class="card-header">
        <h2>成绩管理</h2>
        <div v-if="selectedCourse && selectedClass" class="header-info">
          <span class="selected-info">
            当前：{{ getCourseName(selectedCourse) }} - {{ getClassName(selectedClass) }}
          </span>
        </div>
      </div>

      <!-- 筛选与操作区 -->
      <div class="management-controls">
        <!-- 顶部筛选区 -->
        <div class="filters-section">
          <div class="filter-group">
            <el-select v-model="selectedCourse" placeholder="选择课程" size="small" @change="resetClassSelection">
              <el-option v-for="course in courses" :key="course.id" :label="course.name" :value="course.id" />
            </el-select>
            <el-select v-model="selectedClass" placeholder="选择班级" size="small">
              <el-option v-for="classItem in classes" :key="classItem.id" :label="classItem.name"
                :value="classItem.id" />
            </el-select>
            <el-select v-model="currentSemester" placeholder="选择学期" size="small">
              <el-option v-for="item in semesters" :key="item.value" :label="item.label" :value="item.value" />
            </el-select>
          </div>

          <div class="action-buttons">
            <el-button @click="importGrades" type="primary" size="small" class="btn-import" :disabled="!selectedCourse">
              <el-icon :size="16">
                <Upload />
              </el-icon> 导入成绩
            </el-button>
            <el-button @click="exportGrades" type="success" size="small" class="btn-export"
              :disabled="!selectedCourse || !selectedClass">
              <el-icon :size="16">
                <Download />
              </el-icon> 导出成绩
            </el-button>
            <el-button @click="submitGrades" type="danger" size="small" class="btn-submit" :loading="isSubmitting"
              :disabled="!selectedCourse || !selectedClass">
              提交成绩
            </el-button>
          </div>
        </div>

        <!-- 搜索与筛选区 -->
        <div class="search-section">
          <el-input v-model="searchKeyword" placeholder="搜索学生姓名" prefix-icon="Search" size="small" class="search-input"
            clearable />
          <el-select v-model="filterType" placeholder="筛选类型" size="small" class="filter-select">
            <el-option label="全部学生" value="all" />
            <el-option label="及格" value="pass" />
            <el-option label="不及格" value="fail" />
            <el-option label="待提交" value="pending" />
          </el-select>
        </div>
      </div>

      <!-- 表格内容区 -->
      <div class="table-container">
        <el-table :data="filteredGrades" stripe class="grades-data-table" style="width: 100%"
          :empty-text="getEmptyText()" border>
          <el-table-column prop="studentId" label="学号" width="120" />
          <el-table-column prop="studentName" label="学生姓名" width="100" />
          <el-table-column prop="courseName" label="课程名称" min-width="150" />
          <el-table-column prop="courseType" label="课程类型" width="100" />
          <el-table-column prop="credits" label="学分" width="80" />
          <el-table-column label="成绩" width="150">
            <template #default="scope">
              <el-input-number v-model="scope.row.score" :min="0" :max="100" size="small"
                @change="handleScoreChange(scope.row)" :disabled="scope.row.submitted"
                :placeholder="scope.row.submitted ? '已提交' : '请输入'" />
            </template>
          </el-table-column>
          <el-table-column label="绩点" width="80" :formatter="formatGPA" />
          <el-table-column label="状态" width="100">
            <template #default="scope">
              <el-tag :type="getStatusTagType(scope.row)" size="small">
                {{ formatStatus(scope.row) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="100">
            <template #default="scope">
              <el-button type="text" size="small" @click="viewDetails(scope.row)" class="operation-btn">
                详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页控件 -->
      <div class="pagination" v-if="filteredGrades.length > 0">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"
          :page-sizes="[5, 10, 20]" :page-size="pageSize" layout="total, sizes, prev, pager, next"
          :total="filteredGrades.length" small>
        </el-pagination>
      </div>
    </div>

    <!-- 批量提交确认对话框 -->
    <el-dialog v-model="confirmSubmitVisible" title="提交成绩确认" @close="confirmSubmitVisible = false">
      <p>确定要提交以下成绩数据吗？</p>
      <ul class="submit-details">
        <li>课程：{{ getCourseName(selectedCourse) }}</li>
        <li>班级：{{ getClassName(selectedClass) }}</li>
        <li>学期：{{ getSemesterName(currentSemester) }}</li>
        <li>学生总数：{{ courseGrades.value.length }}人</li>
        <li v-if="pendingGradesCount > 0" class="warning-item">
          <el-icon :size="16" class="warning-icon">
            <WarningFilled />
          </el-icon>
          待提交成绩：{{ pendingGradesCount }}人
        </li>
      </ul>
      <template #footer>
        <el-button @click="confirmSubmitVisible = false">取消</el-button>
        <el-button type="primary" @click="proceedSubmit" :loading="isSubmitting">确认提交</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, inject, watch, onMounted } from 'vue'
import { Upload, Download, ArrowUp, ArrowDown, WarningFilled, Refresh } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

// 深色模式状态
const isDarkMode = inject('isDarkMode', ref(false))

// 课程与班级数据
const selectedCourse = ref(null)
const selectedClass = ref(null)
const currentSemester = ref('2023-2024-2')

const semesters = ref([
  { value: '2023-2024-2', label: '2023-2024学年第二学期' },
  { value: '2023-2024-1', label: '2023-2024学年第一学期' },
  { value: '2022-2023-2', label: '2022-2023学年第二学期' },
  { value: '2022-2023-1', label: '2022-2023学年第一学期' }
])

const courses = ref([
  { id: 1, name: '数据结构与算法' },
  { id: 2, name: '计算机网络' },
  { id: 3, name: '操作系统' },
  { id: 4, name: '人工智能导论' },
  { id: 5, name: '高等数学（下）' },
  { id: 6, name: '线性代数' }
])

const classes = ref([
  { id: 1, name: '计算机科学与技术1班' },
  { id: 2, name: '计算机科学与技术2班' },
  { id: 3, name: '软件工程1班' },
  { id: 4, name: '软件工程2班' }
])

// 成绩数据
const allGrades = ref([
  {
    id: 1,
    studentId: '2023001',
    studentName: '张三',
    courseId: 1,
    courseName: '数据结构与算法',
    courseType: '必修课',
    credits: 4,
    score: 85,
    semester: '2023-2024-2',
    classId: 1,
    submitted: true
  },
  {
    id: 2,
    studentId: '2023002',
    studentName: '李四',
    courseId: 1,
    courseName: '数据结构与算法',
    courseType: '必修课',
    credits: 4,
    score: 78,
    semester: '2023-2024-2',
    classId: 1,
    submitted: true
  },
  {
    id: 3,
    studentId: '2023003',
    studentName: '王五',
    courseId: 1,
    courseName: '数据结构与算法',
    courseType: '必修课',
    credits: 4,
    score: null,
    semester: '2023-2024-2',
    classId: 1,
    submitted: false
  },
  {
    id: 4,
    studentId: '2023004',
    studentName: '赵六',
    courseId: 1,
    courseName: '数据结构与算法',
    courseType: '必修课',
    credits: 4,
    score: 92,
    semester: '2023-2024-2',
    classId: 2,
    submitted: true
  },
  {
    id: 5,
    studentId: '2023005',
    studentName: '钱七',
    courseId: 1,
    courseName: '数据结构与算法',
    courseType: '必修课',
    credits: 4,
    score: 88,
    semester: '2023-2024-2',
    classId: 2,
    submitted: false
  },
  {
    id: 6,
    studentId: '2023006',
    studentName: '孙八',
    courseId: 1,
    courseName: '数据结构与算法',
    courseType: '必修课',
    credits: 4,
    score: 59,
    semester: '2023-2024-2',
    classId: 2,
    submitted: false
  }
])

// 搜索与筛选
const searchKeyword = ref('')
const filterType = ref('all')
const currentPage = ref(1)
const pageSize = ref(10)
const isSubmitting = ref(false)
const confirmSubmitVisible = ref(false)

// 筛选成绩数据
const filteredGrades = computed(() => {
  let grades = allGrades.value

  // 筛选课程
  if (selectedCourse.value) {
    grades = grades.filter(grade => grade.courseId === selectedCourse.value)
  }

  // 筛选班级
  if (selectedClass.value) {
    grades = grades.filter(grade => grade.classId === selectedClass.value)
  }

  // 筛选学期
  if (currentSemester.value) {
    grades = grades.filter(grade => grade.semester === currentSemester.value)
  }

  // 搜索学生姓名
  if (searchKeyword.value) {
    grades = grades.filter(grade =>
      grade.studentName.toLowerCase().includes(searchKeyword.value.toLowerCase())
    )
  }

  // 筛选及格/不及格/待提交
  if (filterType.value === 'pass') {
    grades = grades.filter(grade => grade.score >= 60)
  } else if (filterType.value === 'fail') {
    grades = grades.filter(grade => grade.score < 60 && grade.score !== null)
  } else if (filterType.value === 'pending') {
    grades = grades.filter(grade => grade.score === null || !grade.submitted)
  }

  return grades
})

// 当前课程统计
const courseGrades = computed(() => {
  if (!selectedCourse.value || !selectedClass.value) return []

  return allGrades.value.filter(grade =>
    grade.courseId === selectedCourse.value &&
    grade.classId === selectedClass.value &&
    grade.semester === currentSemester.value
  )
})

// 统计数据计算
const courseAverageScore = computed(() => {
  if (courseGrades.value.length === 0) return 0
  const validScores = courseGrades.value.filter(g => g.score !== null)
  if (validScores.length === 0) return 0

  const sum = validScores.reduce((total, grade) => total + grade.score, 0)
  return (sum / validScores.length).toFixed(1)
})

const courseAverageScoreTrend = computed(() => {
  if (courseGrades.value.length === 0) return 0
  // 模拟与上学期对比数据
  return (Math.random() * 10 - 5).toFixed(1) * 1
})

const coursePassRate = computed(() => {
  if (courseGrades.value.length === 0) return '0%'
  const validScores = courseGrades.value.filter(g => g.score !== null)
  if (validScores.length === 0) return '0%'

  const passCount = validScores.filter(grade => grade.score >= 60).length
  return ((passCount / validScores.length) * 100).toFixed(1) + '%'
})

const coursePassRateValue = computed(() => {
  if (courseGrades.value.length === 0) return 0
  const validScores = courseGrades.value.filter(g => g.score !== null)
  if (validScores.length === 0) return 0

  const passCount = validScores.filter(grade => grade.score >= 60).length
  return Math.round((passCount / validScores.length) * 100)
})

const coursePassRateColor = computed(() => {
  const rate = coursePassRateValue.value
  if (rate < 60) return '#ef4444'
  if (rate < 85) return '#f59e0b'
  return '#10b981'
})

const courseMaxScore = computed(() => {
  const validScores = courseGrades.value.filter(g => g.score !== null)
  if (validScores.length === 0) return 0
  return Math.max(...validScores.map(grade => grade.score))
})

const courseMinScore = computed(() => {
  const validScores = courseGrades.value.filter(g => g.score !== null)
  if (validScores.length === 0) return 0
  return Math.min(...validScores.map(grade => grade.score))
})

const courseMaxStudent = computed(() => {
  const validScores = courseGrades.value.filter(g => g.score !== null)
  if (validScores.length === 0) return ''

  const maxScore = Math.max(...validScores.map(grade => grade.score))
  const maxStudent = validScores.find(grade => grade.score === maxScore)
  return maxStudent ? maxStudent.studentName : ''
})

const courseMinStudent = computed(() => {
  const validScores = courseGrades.value.filter(g => g.score !== null)
  if (validScores.length === 0) return ''

  const minScore = Math.min(...validScores.map(grade => grade.score))
  const minStudent = validScores.find(grade => grade.score === minScore)
  return minStudent ? minStudent.studentName : ''
})

const pendingGradesCount = computed(() => {
  if (courseGrades.value.length === 0) return 0
  return courseGrades.value.filter(grade => grade.score === null || !grade.submitted).length
})

// 辅助函数
const getCourseName = (courseId) => {
  const course = courses.value.find(c => c.id === courseId)
  return course ? course.name : '未知课程'
}

const getClassName = (classId) => {
  const classItem = classes.value.find(c => c.id === classId)
  return classItem ? classItem.name : '未知班级'
}

const getSemesterName = (semesterValue) => {
  const semester = semesters.value.find(s => s.value === semesterValue)
  return semester ? semester.label : '未知学期'
}

const formatGPA = (row) => {
  if (row.score === null || row.score === undefined) return '-'
  if (row.score >= 90) return 4.0
  if (row.score >= 85) return 3.7
  if (row.score >= 80) return 3.3
  if (row.score >= 75) return 3.0
  if (row.score >= 70) return 2.7
  if (row.score >= 65) return 2.3
  if (row.score >= 60) return 1.0
  return 0
}

const formatStatus = (row) => {
  if (row.score === null || row.score === undefined) return '未录入'
  if (row.score >= 60) return '及格'
  return '不及格'
}

const getStatusTagType = (row) => {
  if (row.score === null || row.score === undefined) return 'info'
  if (row.score >= 60) return 'success'
  return 'danger'
}

const getEmptyText = () => {
  if (!selectedCourse.value || !selectedClass.value) {
    return '请选择课程和班级'
  }
  if (searchKeyword.value) {
    return '未找到匹配的学生'
  }
  if (filterType.value !== 'all') {
    return `没有符合条件的${filterType.value === 'pass' ? '及格' : filterType.value === 'fail' ? '不及格' : '待提交'}学生`
  }
  return '暂无成绩数据'
}

// 事件处理函数
const handleScoreChange = (row) => {
  // 更新成绩后可以做一些验证或其他操作
  console.log(`成绩更新: ${row.studentName} - ${row.courseName}: ${row.score}`)
  row.submitted = false // 成绩修改后重置提交状态

  // 添加简单的验证
  if (row.score !== null && (row.score < 0 || row.score > 100)) {
    ElMessage.warning('成绩必须在0-100之间')
    row.score = null
  }
}

const viewDetails = (row) => {
  ElMessage.info(`查看 ${row.studentName} 的 ${row.courseName} 成绩详情`)
  // 这里可以添加查看详情的逻辑
}

const importGrades = () => {
  ElMessage.info('导入成绩功能')
  // 这里可以添加导入成绩的逻辑
}

const exportGrades = () => {
  if (!selectedCourse.value || !selectedClass.value) {
    ElMessage.warning('请先选择课程和班级')
    return
  }

  ElMessage.success(`导出 ${getSemesterName(currentSemester.value)} ${getCourseName(selectedCourse.value)} ${getClassName(selectedClass.value)} 的成绩数据`)
  // 这里可以添加导出成绩的逻辑
}

const submitGrades = () => {
  if (!selectedCourse.value || !selectedClass.value) {
    ElMessage.warning('请先选择课程和班级')
    return
  }

  confirmSubmitVisible.value = true
}

const proceedSubmit = () => {
  isSubmitting.value = true

  // 模拟提交成绩到服务器
  setTimeout(() => {
    isSubmitting.value = false
    confirmSubmitVisible.value = false

    // 更新提交状态
    courseGrades.value.forEach(grade => {
      if (grade.score !== null) {
        grade.submitted = true
      }
    })

    ElMessage.success('成绩提交成功！')
  }, 1500)
}

const checkPendingGrades = () => {
  if (!selectedCourse.value || !selectedClass.value) {
    ElMessage.warning('请先选择课程和班级')
    return
  }

  filterType.value = 'pending'
}

const resetClassSelection = () => {
  // 当选择课程变化时，重置班级选择
  selectedClass.value = null
  // 重置页码
  currentPage.value = 1
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1 // 重置页码
}

const handleCurrentChange = (page) => {
  currentPage.value = page
}

const handleMouseMove = (e) => {
  document.documentElement.style.setProperty('--mouse-x', `${e.clientX}px`)
  document.documentElement.style.setProperty('--mouse-y', `${e.clientY}px`)
}

const refreshStats = () => {
  ElMessage.info('数据已刷新')
  // 实际应用中这里会调用API重新获取数据
}

onMounted(() => {
  // 初始化时默认选择第一个课程
  if (courses.value.length > 0) {
    selectedCourse.value = courses.value[0].id
  }
})

watch([selectedCourse, selectedClass, currentSemester], () => {
  // 重置页码
  currentPage.value = 1
})

watch(isDarkMode, (newVal) => {
  // 深色模式切换时的处理
  console.log('深色模式切换:', newVal)
})
</script>

<style scoped lang="scss">
:root {
  --text-primary: #303133;
  --text-secondary: #606266;
  --text-tertiary: #909399;
  --text-highlight: #409eff;
  --border-color: #ebeef5;
  --card-bg: white;
  --card-hover-bg: #f5f7fa;
  --trend-up: #10b981;
  --trend-down: #ef4444;
  --shadow-light: 0 4px 12px rgba(0, 0, 0, 0.05);
  --shadow-medium: 0 6px 16px rgba(0, 0, 0, 0.08);
}

.dark {
  --text-primary: #ffffff;
  --text-secondary: rgba(255, 255, 255, 0.7);
  --text-tertiary: rgba(255, 255, 255, 0.5);
  --text-highlight: #64748b;
  --border-color: rgba(255, 255, 255, 0.1);
  --card-bg: rgba(30, 41, 59, 0.8);
  --card-hover-bg: rgba(51, 65, 85, 0.8);
  --trend-up: #22c55e;
  --trend-down: #ef4444;
  --shadow-light: 0 4px 12px rgba(0, 0, 0, 0.15);
  --shadow-medium: 0 6px 16px rgba(0, 0, 0, 0.2);
}

.base-card {
  position: relative;
  border-radius: 16px;
  padding: 24px;
  transition: all 0.3s ease;
  overflow: hidden;
  z-index: 1;

  &::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: radial-gradient(600px circle at var(--mouse-x) var(--mouse-y),
        rgba(99, 102, 241, 0.05) 0%,
        transparent 80%);
    opacity: 0;
    transition: opacity 0.3s ease;
    z-index: -1;
    pointer-events: none;
  }

  &:hover {
    transform: translateY(-4px);
    box-shadow: var(--shadow-medium);
  }

  &:hover::before {
    opacity: 1;
  }

  .dark & {
    &::before {
      background: radial-gradient(600px circle at var(--mouse-x) var(--mouse-y),
          rgba(99, 102, 241, 0.08) 0%,
          transparent 80%);
    }
  }
}

.grades-dashboard {
  display: flex;
  flex-direction: column;
  transition: background-color 0.3s ease;
  gap: 30px;
  padding: 0 15px;
  --mouse-x: 0;
  --mouse-y: 0;

  .dark & {
    background-color: #1e293b;
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

    h2 {
      margin: 0;
      font-size: 22px;
      font-weight: 600;
      color: var(--text-primary);
    }

    .header-actions {
      display: flex;
      gap: 10px;
    }

    .header-info {
      color: var(--text-secondary);
      font-size: 14px;
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


.grades-stats {
  .stats-container {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(240px, 1fr));
    gap: 24px;

    .stats-card {
      @extend .base-card;
      background: rgba(255, 255, 255, 0.9);
      backdrop-filter: blur(10px);
      border: 1px solid rgba(226, 232, 240, 0.7);
      box-shadow: 0 6px 20px rgba(0, 0, 0, 0.06);

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

      .stat-header {
        display: flex;
        flex-direction: column;
        margin-bottom: 16px;

        h3 {
          margin: 0;
          font-size: 16px;
          font-weight: 500;
          color: var(--text-secondary);
        }

        .stat-value {
          font-size: 32px;
          font-weight: 700;
          color: var(--text-primary);
          margin-top: 8px;
        }
      }

      .stat-trend {
        display: flex;
        align-items: center;
        font-size: 14px;

        .trend-up {
          color: var(--trend-up);
          display: flex;
          align-items: center;
        }

        .trend-down {
          color: var(--trend-down);
          display: flex;
          align-items: center;
        }
      }

      .stat-progress {
        margin-top: 16px;
      }

      .stat-meta {
        margin-top: 16px;
        color: var(--text-tertiary);
      }
    }
  }
}


.grades-management {
  .management-controls {
    display: flex;
    flex-direction: column;
    gap: 20px;
    margin-bottom: 24px;

    .filters-section {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .filter-group {
        display: flex;
        gap: 15px;
      }

      .action-buttons {
        display: flex;
        gap: 10px;
      }
    }

    .search-section {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .search-input {
        width: 240px;
      }

      .filter-select {
        width: 160px;
      }
    }
  }

  .grades-data-table {
    .el-table__header th {
      background-color: rgba(245, 247, 250, 0.7);

      .dark & {
        background-color: rgba(51, 65, 85, 0.7);
      }
    }

    .el-input-number {
      width: 100%;
    }
  }

  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}


.submit-details {
  list-style: none;
  padding: 0;
  margin: 0;

  li {
    margin-bottom: 10px;
    display: flex;
    align-items: center;

    .warning-icon {
      margin-right: 8px;
      color: #f59e0b;
    }

    .warning-item {
      color: #f59e0b;
    }
  }
}


.btn-import {
  background-color: #409eff;
  border-color: #409eff;
  color: white;

  &:hover {
    background-color: #66b1ff;
    border-color: #66b1ff;
  }
}

.btn-export {
  background-color: #52c41a;
  border-color: #52c41a;
  color: white;

  &:hover {
    background-color: #73d13d;
    border-color: #73d13d;
  }
}

.btn-submit {
  background-color: #ff4d4f;
  border-color: #ff4d4f;
  color: white;

  &:hover {
    background-color: #ff7875;
    border-color: #ff7875;
  }
}


.operation-btn {
  color: #409eff;

  &:hover {
    color: #66b1ff;
  }
}

.view-details-btn {
  color: #409eff;

  &:hover {
    color: #66b1ff;
  }
}

.refresh-btn {
  color: #909399;

  &:hover {
    color: #606266;
  }
}
</style>