<template>
  <div class="grades-dashboard" :class="{ 'dark': isDarkMode }" @mousemove="handleMouseMove">
    <div class="grades-overview modern-card" ref="overviewCard">
      <div class="card-header">
        <h2>成绩概览</h2>
        <div class="semester-selector">
          <el-select v-model="currentSemester" placeholder="选择学期" size="small" class="semester-select" @change="handleSemesterChange">
              <el-option
                v-for="item in semesters"
                :key="item.value"
                :label="item.label"
                :value="item.value"
              />
            </el-select>
        </div>
      </div>

      <div class="stats-container">
        <div class="stats-card">
          <div class="stat-header">
            <h3>平均绩点</h3>
            <div class="stat-value">{{ gpa || '0.00' }}</div>
          </div>
          <div class="stat-meta">
            <div class="meta-item">
              <el-icon>
                <User />
              </el-icon>
              <span>班级排名: {{ rank || '--' }}/{{ classSize || '--' }}</span>
            </div>
            <div class="stat-progress">
              <el-progress :percentage="gpaPercentage" :stroke-width="6" :color="progressColor" />
              <div class="progress-text">超过班级 {{ gpaPercentage }}% 的同学</div>
            </div>
          </div>
        </div>

        <div class="stats-card">
          <div class="stat-header">
            <h3>平均分数</h3>
            <div class="stat-value">{{ avgScore || '0' }}</div>
          </div>
          <div class="stat-meta">
            <div class="score-distribution">
              <div class="distribution-item" v-for="(item, index) in scoreDistribution" :key="index">
                <div class="distribution-bar" :style="{ height: `${item.percentage}%`, backgroundColor: item.color }">
                </div>
                <div class="distribution-label">{{ item.label }}</div>
              </div>
            </div>
          </div>
        </div>

        <div class="stats-card">
          <div class="stat-header">
            <h3>学分完成情况</h3>
            <div class="stat-value">{{ completedCredits || '0' }}/{{ totalCredits || '0' }}</div>
          </div>
          <div class="stat-meta">
            <el-progress :percentage="creditPercentage" :stroke-width="6" :color="creditColor" />
            <div class="progress-text">已完成 {{ creditPercentage }}% 的毕业学分要求</div>
          </div>
        </div>

      </div>
    </div>

    <div class="grades-table modern-card">
      <div class="card-header">
        <h2>详细成绩</h2>
        <div class="table-actions">
          <el-input v-model="searchKeyword" placeholder="搜索课程名称" prefix-icon="Search" size="small"
            class="search-input" @clear="handleSearch" @keyup.enter="handleSearch" />
          <el-select v-model="filterType" placeholder="筛选类型" size="small" class="filter-select" @change="handleFilterChange">
              <el-option label="全部课程" value="all" />
              <el-option label="必修课" value="required" />
              <el-option label="选修课" value="elective" />
              <el-option label="实践课" value="practical" />
            </el-select>
        </div>
      </div>

      <el-table :data="allGrades" border stripe class="grades-data-table" style="width: 100%" v-loading="loading" element-loading-text="加载中...">
        <el-table-column prop="courseName" label="课程名称" min-width="200" />
        <el-table-column prop="courseCode" label="课程代码" width="120" />
        <el-table-column prop="courseType" label="课程类型" width="100" />
        <el-table-column prop="credits" label="学分" width="80" />
        <el-table-column prop="score" label="成绩" width="100" :formatter="formatScore" />
        <el-table-column prop="gpa" label="绩点" width="80" />
        <el-table-column prop="semester" label="学期" width="120" />
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button type="text" size="small" @click="viewDetails(scope.row)">
              详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination" v-if="filteredGrades.length > 0">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"
          :page-sizes="[5, 10, 20]" :page-size="pageSize" :total="totalGrades"
          layout="total, sizes, prev, pager, next" small />
      </div>
    </div>

    <div class="grades-analysis modern-card">
      <div class="card-header">
        <h2>成绩趋势分析</h2>
        <div class="chart-type-selector">
          <el-radio-group v-model="chartType">
            <el-radio-button label="gpa">绩点趋势</el-radio-button>
            <el-radio-button label="score">分数趋势</el-radio-button>
            <el-radio-button label="credit">学分趋势</el-radio-button>
          </el-radio-group>
        </div>
      </div>

      <div class="chart-container">
        <div class="trend-chart">
          <canvas ref="trendChartCanvas"></canvas>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, inject, watch } from 'vue'
import { User } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import Chart from 'chart.js/auto'
import service from '@/utils/request'

const isDarkMode = inject('isDarkMode', ref(false))

const allGrades = ref([])
const gradeStats = ref(null)
const semesterGPAList = ref([])
const loading = ref(false)
const chartType = ref('gpa')
const totalGrades = ref(0)
const searchKeyword = ref('')
const filterType = ref('all')
const currentSemester = ref('全部')
const semesters = ref([
  { value: '全部', label: '全部学期' },
  { value: '2023-2024-1', label: '2023-2024学年第一学期' },
  { value: '2023-2024-2', label: '2023-2024学年第二学期' },
  { value: '2022-2023-1', label: '2022-2023学年第一学期' },
  { value: '2022-2023-2', label: '2022-2023学年第二学期' }
])


const fetchGradesData = async () => {
  loading.value = true
  try {
    const res = await service.get('/student/grades/pagination', {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })
    if (res.code === 200) {
      const data = res.data
      allGrades.value = data.courseGrades || []
      gradeStats.value = data.gradeStats || null
      semesterGPAList.value = data.semesterGPAList || []
      totalGrades.value = data.total || 0
      
  
      if (trendChartCanvas.value) {
        initTrendChart()
      }
    } else {
      throw new Error(res.message || '获取成绩数据失败')
    }
  } catch (err) {
    console.error('获取成绩数据失败:', err)
    ElMessage.error('获取成绩数据失败，请稍后重试')
    

    allGrades.value = []
    gradeStats.value = null
    semesterGPAList.value = []
    totalGrades.value = 0
  } finally {
    loading.value = false
  }
}


const fetchGradesDataBySemester = async (semester) => {
  loading.value = true
  try {
    const res = await service.get(`/student/grades/${semester}/pagination`, {
      params: {
        page: currentPage.value,
        size: pageSize.value
      }
    })
    if (res.code === 200) {
      const data = res.data
      allGrades.value = data.courseGrades || []
      gradeStats.value = data.gradeStats || null
      semesterGPAList.value = data.semesterGPAList || []
      totalGrades.value = data.total || 0
      // 学期GPA列表保持不变，因为我们需要所有学期的数据来绘制趋势图
      
      // 初始化图表
      if (trendChartCanvas.value) {
        initTrendChart()
      }
    } else {
      throw new Error(res.message || '获取成绩数据失败')
    }
  } catch (err) {
    console.error('获取成绩数据失败:', err)
    ElMessage.error('获取成绩数据失败，请稍后重试')
    
    // 如果API请求失败，使用默认空数据
    allGrades.value = []
    gradeStats.value = null
    totalGrades.value = 0
  } finally {
    loading.value = false
  }
}

const trendChartCanvas = ref(null)
let trendChart = null

const currentPage = ref(1)
const pageSize = ref(10)

// 过滤后的成绩数据（根据当前选择的学期）
const filteredGrades = computed(() => {
  return allGrades.value
})

// 处理学期选择变化
const handleSemesterChange = (semester) => {
  currentSemester.value = semester
  currentPage.value = 1 // 重置到第一页
  if (semester === '全部') {
    fetchGradesData()
  } else {
    fetchGradesDataBySemester(semester)
  }
}

const gpa = computed(() => {
  if (!gradeStats.value || allGrades.value.length === 0) return '0.00'
  return gradeStats.value.gpa?.toFixed(2) || '0.00'
})

const avgScore = computed(() => {
  if (!gradeStats.value || allGrades.value.length === 0) return 0
  return gradeStats.value.avgScore || 0
})

const rank = computed(() => {
  return gradeStats.value?.rank || '--'
})

const classSize = computed(() => {
  return gradeStats.value?.classSize || '--'
})

const completedCredits = computed(() => {
  return gradeStats.value?.completedCredits || 0
})

const totalCredits = computed(() => {
  return gradeStats.value?.totalCredits || 0
})

const gpaPercentage = computed(() => {
  if (!gradeStats.value || !gradeStats.value.rank || !gradeStats.value.classSize) return 0
  return Math.round((1 - (gradeStats.value.rank / gradeStats.value.classSize)) * 100)
})

const creditPercentage = computed(() => {
  if (!gradeStats.value || !gradeStats.value.completedCredits || !gradeStats.value.totalCredits) return 0
  return Math.round((gradeStats.value.completedCredits / gradeStats.value.totalCredits) * 100)
})

const progressColor = computed(() => {
  const progress = gpaPercentage.value
  if (progress < 30) return '#ef4444'
  if (progress < 60) return '#f59e0b'
  return '#10b981'
})

const creditColor = computed(() => {
  const progress = creditPercentage.value
  if (progress < 30) return '#ef4444'
  if (progress < 60) return '#f59e0b'
  return '#10b981'
})

const scoreDistribution = ref([
  { label: '90-100', percentage: 25, color: '#67c23a' },
  { label: '80-89', percentage: 35, color: '#409eff' },
  { label: '70-79', percentage: 25, color: '#e6a23c' },
  { label: '60-69', percentage: 10, color: '#f56c6c' },
  { label: '60以下', percentage: 5, color: '#909399' }
])

const initTrendChart = () => {
  if (!trendChartCanvas.value || !semesterGPAList.value || semesterGPAList.value.length === 0) return
  
  const ctx = trendChartCanvas.value.getContext('2d')

  // 销毁旧图表
  if (trendChart && trendChart.value) {
    trendChart.value.destroy()
  }

  // 根据图表类型设置数据
  let chartLabel, chartData, chartColor, chartMin, chartMax, chartStep
  
  if (chartType.value === 'gpa') {
    chartLabel = '绩点趋势'
    chartData = semesterGPAList.value.map(item => item.gpa)
    chartColor = '#409eff'
    chartMin = 2.5
    chartMax = 4.0
    chartStep = 0.5
  } else if (chartType.value === 'score') {
    chartLabel = '分数趋势'
    chartData = semesterGPAList.value.map(item => item.avgScore)
    chartColor = '#67c23a'
    chartMin = 60
    chartMax = 100
    chartStep = 10
  } else if (chartType.value === 'credit') {
    chartLabel = '学分趋势'
    chartData = semesterGPAList.value.map(item => item.credits)
    chartColor = '#e6a23c'
    chartMin = 0
    chartMax = 40
    chartStep = 10
  }

  // 创建新图表
  if (!trendChart) {
    trendChart = {}
  }
  
  trendChart.value = new Chart(ctx, {
    type: 'line',
    data: {
      labels: semesterGPAList.value.map(item => item.semester),
      datasets: [{
        label: chartLabel,
        data: chartData,
        borderColor: chartColor,
        backgroundColor: isDarkMode.value ? `${chartColor}20` : `${chartColor}40`,
        tension: 0.4,
        borderWidth: 2,
        pointBackgroundColor: '#fff',
        pointBorderColor: chartColor,
        pointRadius: 4,
        pointHoverRadius: 6
      }]
    },
    options: {
      responsive: true,
      maintainAspectRatio: false,
      scales: {
        y: {
          beginAtZero: false,
          min: chartMin,
          max: chartMax,
          ticks: {
            stepSize: chartStep
          },
          grid: {
            color: isDarkMode.value ? 'rgba(255, 255, 255, 0.1)' : 'rgba(0, 0, 0, 0.05)'
          }
        },
        x: {
          grid: {
            display: false
          }
        }
      },
      plugins: {
        legend: {
          display: false
        }
      }
    }
  })
}

watch(isDarkMode, (newVal) => {
  if (trendChart && trendChart.value) {
    trendChart.value.options.scales.y.grid.color = newVal
      ? 'rgba(255, 255, 255, 0.1)'
      : 'rgba(0, 0, 0, 0.05)'
    trendChart.value.data.datasets[0].backgroundColor = newVal
      ? `${trendChart.value.data.datasets[0].borderColor}20`
      : `${trendChart.value.data.datasets[0].borderColor}40`
    trendChart.value.update()
  }
})

watch(chartType, () => {
  if (trendChartCanvas.value && semesterGPAList.value.length > 0) {
    initTrendChart()
  }
})

const formatScore = (row) => {
  if (row.score >= 90) return `${row.score} (优秀)`
  if (row.score >= 80) return `${row.score} (良好)`
  if (row.score >= 70) return `${row.score} (中等)`
  if (row.score >= 60) return `${row.score} (及格)`
  return `${row.score} (不及格)`
}

const viewDetails = (row) => {
  ElMessage.info(`查看课程详情: ${row.courseName}`)
}

// 处理每页条数变化
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1 // 重置到第一页
  // 重新获取数据
  if (currentSemester.value !== '全部') {
    fetchGradesDataBySemester(currentSemester.value)
  } else {
    fetchGradesData()
  }
}

// 处理页码变化
const handleCurrentChange = (page) => {
  currentPage.value = page
  // 重新获取数据
  if (currentSemester.value !== '全部') {
    fetchGradesDataBySemester(currentSemester.value)
  } else {
    fetchGradesData()
  }
}

// 处理搜索
const handleSearch = () => {
  currentPage.value = 1 // 重置到第一页
  // 重新获取数据
  if (currentSemester.value !== '全部') {
    fetchGradesDataBySemester(currentSemester.value)
  } else {
    fetchGradesData()
  }
}

// 处理过滤条件变化
const handleFilterChange = () => {
  currentPage.value = 1 // 重置到第一页
  // 重新获取数据
  if (currentSemester.value !== '全部') {
    fetchGradesDataBySemester(currentSemester.value)
  } else {
    fetchGradesData()
  }
}

const handleMouseMove = (e) => {
  document.documentElement.style.setProperty('--mouse-x', `${e.clientX}px`)
  document.documentElement.style.setProperty('--mouse-y', `${e.clientY}px`)
}

// 组件挂载时获取成绩数据
onMounted(() => {
  fetchGradesData().then(() => {
    // 初始化图表
    if (trendChartCanvas.value) {
      initTrendChart()
    }
  })
})

onUnmounted(() => {
  if (trendChart && trendChart.value) {
    trendChart.value.destroy()
  }
})
</script>

<style scoped lang="scss">
.grades-dashboard {
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
  border-radius: 16px;
  padding: 30px;
  transition: all 0.3s ease;
  overflow: hidden;
  z-index: 1;
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
  }
}

.grades-overview {
  .stats-container {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
    gap: 24px;

    @media (max-width: 768px) {
      grid-template-columns: 1fr;
    }
  }

  .stats-card {
    position: relative;
    border-radius: 16px;
    padding: 24px;
    transition: all 0.3s ease;
    overflow: hidden;
    z-index: 1;
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
      justify-content: space-between;
      align-items: center;
      margin-bottom: 16px;

      h3 {
        margin: 0;
        font-size: 16px;
        font-weight: 500;
        color: var(--text-secondary);
      }

      .stat-value {
        font-size: 28px;
        font-weight: 700;
        color: var(--text-primary);
      }
    }

    .stat-meta {
      .meta-item {
        display: flex;
        align-items: center;
        gap: 8px;
        margin-bottom: 12px;
        color: var(--text-secondary);
        font-size: 14px;

        .el-icon {
          color: #409eff;
        }
      }

      .stat-progress {
        margin-top: 16px;

        .progress-text {
          margin-top: 8px;
          font-size: 13px;
          color: var(--text-secondary);
          text-align: center;
        }
      }

      .score-distribution {
        display: flex;
        justify-content: space-around;
        align-items: flex-end;
        height: 120px;
        margin-top: 16px;

        .distribution-item {
          display: flex;
          flex-direction: column;
          align-items: center;
          width: 20px;

          .distribution-bar {
            width: 100%;
            border-radius: 4px 4px 0 0;
            transition: height 0.5s ease;
          }

          .distribution-label {
            margin-top: 8px;
            font-size: 12px;
            color: var(--text-secondary);
          }
        }
      }
    }
  }
}

.grades-table {
  .table-actions {
    display: flex;
    gap: 12px;

    .search-input {
      width: 240px;
    }

    .filter-select {
      width: 150px;
    }
  }

  .grades-data-table {
    margin-top: 20px;
  }

  .pagination {
    margin-top: 20px;
    display: flex;
    justify-content: flex-end;
  }
}

.grades-analysis {
  .chart-type-selector {
    margin-bottom: 20px;
  }

  .chart-container {
    height: 350px;

    .trend-chart {
      width: 100%;
      height: 100%;
    }
  }
}

:root {
  --text-primary: #303133;
  --text-secondary: #606266;
}

.dark {
  --text-primary: #ffffff;
  --text-secondary: rgba(255, 255, 255, 0.7);
}
</style>
