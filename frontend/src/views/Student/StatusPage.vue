<template>
  <div class="status-dashboard" :class="{ 'dark': isDarkMode }" @mousemove="handleMouseMove">
    <!-- 主要内容区域 -->
    <main class="dashboard-content">
      <!-- 学籍状态卡片 -->
      <div class="status-card modern-card" ref="statusCard">
        <div class="card-header">
          <h2>学籍状态概览</h2>
        </div>

        <div class="status-main">
          <div class="status-indicator">
            <div class="status-badge">
              <el-tag :type="statusConfig[status].type" size="large" effect="dark" class="status-tag">
                {{ statusConfig[status].label }}
              </el-tag>
            </div>
            <div class="status-meta">
              <p class="meta-item">
                <el-icon class="meta-icon">
                  <Calendar />
                </el-icon>
                <span>入学时间: {{ formatDate(effectiveDate) }}</span>
              </p>
              <p class="meta-item">
                <el-icon class="meta-icon">
                  <School />
                </el-icon>
                <span>预计毕业: {{ formatDate(graduationDate) }}</span>
              </p>
            </div>
          </div>

          <div class="status-stats">
            <div class="stat-card">
              <div class="stat-value counter">{{ credits }}</div>
              <div class="stat-label">已修学分</div>
              <div class="stat-progress">
                <el-progress :percentage="creditProgress" :stroke-width="6" :color="progressColor" />
              </div>
            </div>
            <div class="stat-card">
              <div class="stat-value counter">{{ attendanceRate }}%</div>
              <div class="stat-label">出勤率</div>
              <div class="stat-progress">
                <el-progress :percentage="attendanceRate" :stroke-width="6" :color="attendanceColor" />
              </div>
            </div>
            <div class="stat-card">
              <div class="stat-value">{{ performanceLevel }}</div>
              <div class="stat-label">学业等级</div>
              <div class="stat-rating">
                <el-rate :value="levelToRating(performanceLevel)" disabled :show-text="false" :allow-half="true"
                  :colors="ratingColors" />
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 图表与数据区域 -->
      <div class="charts-container">
        <!-- 学分进度图表 -->
        <div class="chart-card modern-card" ref="creditChartCard">
          <div class="card-header">
            <h3>学分完成进度</h3>
            <div class="progress-indicator">
              <span class="progress-text">{{ creditProgress }}%</span>
            </div>
          </div>
          <div class="chart-wrapper">
            <div class="credit-chart">
              <div class="doughnut-chart">
                <canvas ref="creditDoughnutCanvas"></canvas>
              </div>
              <div class="credit-info">
                <div class="credit-detail">
                  <span class="detail-label">已修:</span>
                  <span class="detail-value">{{ credits }}/{{ totalCredits }} 学分</span>
                </div>
                <div class="credit-detail">
                  <span class="detail-label">剩余:</span>
                  <span class="detail-value">{{ totalCredits - credits }} 学分</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 学籍状态趋势图 -->
        <div class="chart-card modern-card" ref="trendChartCard">
          <div class="card-header">
            <h3>学业表现趋势</h3>
          </div>
          <div class="chart-wrapper">
            <div class="trend-chart">
              <canvas ref="performanceTrendCanvas"></canvas>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted, nextTick, watch, inject } from 'vue'
import { Calendar, School } from '@element-plus/icons-vue'
import Chart from 'chart.js/auto'
import service from '@/utils/request'
import { ElMessage } from 'element-plus'


const statusCard = ref(null)
const creditChartCard = ref(null)
const trendChartCard = ref(null)
const creditDoughnutCanvas = ref(null)
const performanceTrendCanvas = ref(null)


const isDarkMode = inject('isDarkMode', ref(false))


const statusConfig = {
  ACTIVE: { label: '在读', type: 'success', color: '#10b981' },
  GRADUATED: { label: '已毕业', type: 'primary', color: '#6366f1' },
  SUSPENDED: { label: '休学', type: 'warning', color: '#f59e0b' },
  TRANSFERRED: { label: '转学', type: 'info', color: '#3b82f6' },
  LEAVE_SICK: { label: '病假', type: 'warning', color: '#f59e0b' },
  LEAVE_PERSONAL: { label: '事假', type: 'warning', color: '#f59e0b' },
  DROP_OUT: { label: '退学', type: 'danger', color: '#ef4444' }
}

const status = ref('ACTIVE')
const effectiveDate = ref('2022-09-01')
const graduationDate = ref('2026-06-30')
const credits = ref(70)
const totalCredits = ref(140)
const attendanceRate = ref(99)
const performanceLevel = ref('良好')
const loading = ref(false)
const error = ref(null)
const academicHistory = ref([])
let creditChart = null
let trendChart = null


const fetchStudentStatus = async () => {
  loading.value = true
  error.value = null
  
  try {
    const response = await service.get('/student/status')
    
    if (response.code === 200) {
      const data = response.data

      status.value = data.basicInfo.status || 'ACTIVE'
      credits.value = data.basicInfo.completedCredits || 0
      totalCredits.value = data.basicInfo.totalCredits || 140
      effectiveDate.value = data.basicInfo.enrollmentDate || '--'
      graduationDate.value = data.basicInfo.expectedGraduationDate || '--'

      const gpa = parseFloat(data.basicInfo.gpa || 0)
      if (gpa >= 3.7) performanceLevel.value = '优秀'
      else if (gpa >= 3.0) performanceLevel.value = '良好'
      else if (gpa >= 2.0) performanceLevel.value = '中等'
      else if (gpa >= 1.0) performanceLevel.value = '及格'
      else performanceLevel.value = '不及格'

      attendanceRate.value = data.basicInfo.attendanceRate || 99

      if (data.academicHistory && data.academicHistory.length > 0) {
        academicHistory.value = data.academicHistory
        attendanceRate.value = data.academicHistory[data.academicHistory.length - 1].attendanceRate || attendanceRate.value
      }

      updateChartsData()
    } else {
      throw new Error(response.message || '获取数据失败')
    }
  } catch (err) {
    error.value = '获取数据失败，请稍后重试'
    console.error('获取学生状态失败:', err)
    ElMessage.error('获取学籍状态失败')

    status.value = 'ACTIVE'
    credits.value = 0
    totalCredits.value = 140
    effectiveDate.value = '--'
    graduationDate.value = '--'
    performanceLevel.value = '不及格'
    attendanceRate.value = 0
    academicHistory.value = []

    updateChartsData()
  } finally {
    loading.value = false
  }
}

const updateChartsData = () => {
  if (creditChart && creditChart.data && creditChart.data.datasets) {
    creditChart.data.datasets[0].data = [credits.value, totalCredits.value - credits.value]
    creditChart.update()
  }

  if (trendChart && trendChart.data && trendChart.data.datasets) {
    const trendLabels = academicHistory.value.map(item => item.semester)
    const trendData = academicHistory.value.map(item => item.attendanceRate)

    trendLabels.push('当前')
    trendData.push(attendanceRate.value)

    trendChart.data.labels = trendLabels
    trendChart.data.datasets[0].data = trendData
    trendChart.update()
  }
}


const handleMouseMove = (e) => {
  document.documentElement.style.setProperty('--mouse-x', `${e.clientX}px`)
  document.documentElement.style.setProperty('--mouse-y', `${e.clientY}px`)
}


const creditProgress = computed(() => {
  return Math.round((credits.value / totalCredits.value) * 100)
})


const progressColor = computed(() => {
  const progress = creditProgress.value
  if (progress < 30) return '#ef4444'
  if (progress < 60) return '#f59e0b'
  return '#10b981'
})

const attendanceColor = computed(() => {
  const rate = attendanceRate.value
  if (rate < 70) return '#ef4444'
  if (rate < 90) return '#f59e0b'
  return '#10b981'
})

const levelToRating = (level) => {
  const levelMap = {
    '优秀': 5,
    '良好': 4,
    '中等': 3,
    '及格': 2,
    '不及格': 1
  }
  return levelMap[level] || 3
}


const ratingColors = computed(() => {
  return isDarkMode.value
    ? ['rgba(255, 255, 255, 0.3)', '#f7ba1e', '#f7ba1e', '#f7ba1e', '#10b981']
    : ['rgba(0, 0, 0, 0.2)', '#3b82f6', '#3b82f6', '#10b981', '#10b981']
})


const formatDate = (dateStr) => {
  if (!dateStr) return '--'
  return new Date(dateStr).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  }).replace(/\//g, '-')
}


const initCharts = () => {
  nextTick(() => {
    if (!creditDoughnutCanvas.value || !performanceTrendCanvas.value) {
      console.error('Canvas elements not found')
      return
    }

    const creditCtx = creditDoughnutCanvas.value.getContext('2d')
    if (!creditCtx) {
      console.error('Failed to get credit chart context')
      return
    }

    creditChart = new Chart(creditCtx, {
      type: 'doughnut',
      data: {
        labels: ['已修', '剩余'],
        datasets: [{
          data: [credits.value, totalCredits.value - credits.value],
          backgroundColor: [
            '#409eff', 
            isDarkMode.value ? 'rgba(255, 255, 255, 0.1)' : 'rgba(0, 0, 0, 0.05)' 
          ],
          borderWidth: 0,
          cutout: '70%' 
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false 
          },
          tooltip: {
            callbacks: {
              label: function (context) {
                const label = context.label || ''
                const value = context.raw || 0
                const total = context.dataset.data.reduce((a, b) => a + b, 0)
                const percentage = Math.round((value / total) * 100)
                return `${label}: ${value} 学分 (${percentage}%)`
              }
            }
          }
        },
        animation: {
          animateScale: true,
          animateRotate: true
        }
      }
    })

    const trendCtx = performanceTrendCanvas.value.getContext('2d')
    if (!trendCtx) {
      console.error('Failed to get trend chart context')
      return
    }

    const trendLabels = academicHistory.value.map(item => item.semester)
    const trendData = academicHistory.value.map(item => item.attendanceRate)

    trendLabels.push('当前')
    trendData.push(attendanceRate.value)
    
    trendChart = new Chart(trendCtx, {
      type: 'line',
      data: {
        labels: trendLabels,
        datasets: [{
          label: '学业表现',
          data: trendData,
          borderColor: '#409eff', 
          backgroundColor: isDarkMode.value
            ? 'rgba(64, 158, 255, 0.1)'
            : 'rgba(64, 158, 255, 0.2)', 
          tension: 0.4, 
          fill: true, 
          pointBackgroundColor: '#fff', 
          pointBorderColor: '#409eff', 
          pointBorderWidth: 2, 
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
            min: 50,
            max: 100,
            ticks: {
              stepSize: 10
            },
            grid: {
              color: isDarkMode.value
                ? 'rgba(255, 255, 255, 0.1)'
                : 'rgba(0, 0, 0, 0.05)' 
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
            display: false // 不显示图例
          },
          tooltip: {
            mode: 'index',
            intersect: false
          }
        },
        interaction: {
          mode: 'nearest',
          axis: 'x',
          intersect: false
        }
      }
    })
  })
}


watch(isDarkMode, (newVal) => {
  
  if (creditChart) {
    creditChart.data.datasets[0].backgroundColor[1] = newVal
      ? 'rgba(255, 255, 255, 0.1)'
      : 'rgba(0, 0, 0, 0.05)'
    creditChart.update()
  }

  
  if (trendChart) {
    trendChart.data.datasets[0].backgroundColor = newVal
      ? 'rgba(64, 158, 255, 0.1)'
      : 'rgba(64, 158, 255, 0.2)'

    trendChart.options.scales.y.grid.color = newVal
      ? 'rgba(255, 255, 255, 0.1)'
      : 'rgba(0, 0, 0, 0.05)'

    trendChart.update()
  }
})


onMounted(async () => {
  await fetchStudentStatus()

  await nextTick()
  initCharts()
})


onUnmounted(() => {
  if (creditChart) creditChart.destroy() 
  if (trendChart) trendChart.destroy() 
})
</script>

<style scoped lang="scss">

.status-dashboard {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  transition: background-color 0.3s ease;
  padding: 0 15px;
  --mouse-x: 0;
  --mouse-y: 0;
}


.dashboard-content {
  flex: 1;
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr;
  gap: 30px;
}


.modern-card {
  position: relative;
  border-radius: 8px;
  padding: 20px;
  transition: all 0.15s ease;
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
      border-radius: 8px;
      font-size: 14px;
    }
  }
}


.status-card {
  .status-main {
    display: flex;
    justify-content: space-between;
    align-items: center;
    gap: 30px;

    @media (max-width: 768px) {
      flex-direction: column;
      align-items: flex-start;
    }
  }

  .status-indicator {
    .status-badge {
      margin-bottom: 16px;

      .status-tag {
        font-size: 18px;
        padding: 8px 20px;
        border-radius: 8px;
        box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
      }
    }

    .status-meta {
      display: flex;
      flex-wrap: wrap;
      gap: 16px;

      .meta-item {
        display: flex;
        align-items: center;
        gap: 10px;
        margin-bottom: 8px;
        color: var(--text-secondary);
        font-size: 16px;

        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;

        .meta-icon {
          color: #409eff;
          font-size: 18px;
        }
      }
    }
  }

  .status-stats {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    gap: 20px;
    width: 100%;

    @media (max-width: 768px) {
      grid-template-columns: 1fr;
    }

    .stat-card {
      background-color: rgba(0, 0, 0, 0.03);
      border-radius: 12px;
      padding: 20px;
      text-align: center;
      transition: all 0.3s ease;

      
      .dark & {
        background-color: rgba(255, 255, 255, 0.05);
      }

      
      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
      }

      
      .stat-value {
        font-size: 32px;
        font-weight: 700;
        color: var(--text-primary);
        margin-bottom: 8px;
      }

      
      .stat-label {
        font-size: 15px;
        color: var(--text-secondary);
        margin-bottom: 12px;
      }

      
      .stat-progress {
        margin-top: 12px;
      }

      
      .stat-rating {
        margin-top: 12px;

        .el-rate__item {
          
          transition: transform 0.2s ease;

          
          &:hover {
            transform: scale(1.1);
          }
        }
      }
    }
  }

  
  .stat-rating {
    margin-top: 12px;

    .el-rate__item {
      transition: transform 0.2s ease;

      &:hover {
        transform: scale(1.1);
      }
    }
  }
}


.chart-card {
  .chart-wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 300px;
    position: relative;
  }

  .credit-chart {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 30px;
    width: 100%;

    .doughnut-chart {
      width: 200px;
      height: 200px;
    }

    .credit-info {
      display: flex;
      flex-direction: column;
      gap: 12px;

      .credit-detail {
        display: flex;
        align-items: center;
        gap: 12px;

        .detail-label {
          color: var(--text-secondary);
          font-size: 15px;
        }

        .detail-value {
          color: var(--text-primary);
          font-size: 18px;
          font-weight: 500;
        }
      }
    }
  }

  .trend-chart {
    width: 100%;
    height: 100%;
  }
}


.charts-container {
  display: grid;
  grid-template-columns: 1fr;
  gap: 30px;

  @media (min-width: 1024px) {
    grid-template-columns: 1fr 1fr;
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


.loading-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 50vh;
  
  .el-icon {
    font-size: 48px;
    color: var(--el-color-primary);
    animation: rotating 2s linear infinite;
  }
  
  p {
    margin-top: 20px;
    color: var(--text-secondary);
    font-size: 16px;
  }
}

.error-container {
  margin: 20px 0;
}

@keyframes rotating {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>