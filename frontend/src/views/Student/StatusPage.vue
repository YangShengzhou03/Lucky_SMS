<template>
  <div class="status-dashboard" :class="{ 'dark': isDarkMode }" @mousemove="handleMouseMove">
    <!-- 加载状态 -->
    <div v-if="loading" class="loading-container">
      <el-icon class="is-loading"><loading /></el-icon>
      <p>正在加载学籍状态数据...</p>
    </div>
    
    <!-- 主要内容区域 -->
    <main v-else class="dashboard-content">
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

// DOM 引用
const statusCard = ref(null)
const creditChartCard = ref(null)
const trendChartCard = ref(null)
const creditDoughnutCanvas = ref(null)
const performanceTrendCanvas = ref(null)

// 从父组件或全局状态中注入暗色模式状态
const isDarkMode = inject('isDarkMode', ref(false))

// 学籍状态配置映射 - 定义不同学籍状态的显示样式
const statusConfig = {
  normal: { label: '正常', type: 'success', color: '#10b981' },
  suspended: { label: '休学', type: 'warning', color: '#f59e0b' },
  probation: { label: '试读', type: 'info', color: '#3b82f6' },
  graduated: { label: '已毕业', type: 'primary', color: '#6366f1' },
  dropped: { label: '退学', type: 'danger', color: '#ef4444' }
}

// 响应式数据
const status = ref('normal') // 学籍状态
const effectiveDate = ref('2022-09-01') // 入学日期
const graduationDate = ref('2026-06-30') // 预计毕业日期
const credits = ref(68) // 已修学分
const totalCredits = ref(140) // 总学分要求
const attendanceRate = ref(96) // 出勤率
const performanceLevel = ref('良好') // 学业等级
const loading = ref(false) // 加载状态
const error = ref(null) // 错误信息
const academicHistory = ref([]) // 历史学业表现数据
let creditChart = null // 学分进度图表实例
let trendChart = null // 学业表现趋势图表实例

// 获取学生状态数据
const fetchStudentStatus = async () => {
  loading.value = true
  error.value = null
  
  try {
    const response = await service.get('/student/status')
    
    if (response.code === 200) {
      const data = response.data
      
      // 更新基本信息
      status.value = data.academicStatus.status || 'normal'
      credits.value = data.academicStatus.completedCredits || 0
      totalCredits.value = data.academicStatus.totalCredits || 140
      effectiveDate.value = data.basicInfo.enrollmentDate || '2022-09-01'
      
      // 计算预计毕业时间（假设学制为4年）
      if (data.basicInfo.enrollmentDate) {
        const enrollDate = new Date(data.basicInfo.enrollmentDate)
        const gradDate = new Date(enrollDate)
        gradDate.setFullYear(gradDate.getFullYear() + 4)
        graduationDate.value = gradDate.toISOString().split('T')[0]
      }
      
      // 根据GPA计算学业等级
      const gpa = parseFloat(data.academicStatus.gpa || 0)
      if (gpa >= 3.7) performanceLevel.value = '优秀'
      else if (gpa >= 3.0) performanceLevel.value = '良好'
      else if (gpa >= 2.0) performanceLevel.value = '中等'
      else if (gpa >= 1.0) performanceLevel.value = '及格'
      else performanceLevel.value = '不及格'
      
      // 模拟出勤率（实际应用中应该从后端获取）
      attendanceRate.value = Math.min(95, Math.max(60, Math.round(gpa * 25 + Math.random() * 10)))
      
      // 保存历史数据用于趋势图（如果后端提供了历史数据）
      if (data.academicHistory && data.academicHistory.length > 0) {
        academicHistory.value = data.academicHistory
        // 使用最新的历史数据中的出勤率
        attendanceRate.value = data.academicHistory[data.academicHistory.length - 1].attendanceRate || attendanceRate.value
      }
      
      // 重新初始化图表以更新数据
      if (creditChart && creditChart.data && creditChart.data.datasets) {
        creditChart.data.datasets[0].data = [credits.value, totalCredits.value - credits.value]
        creditChart.update()
      }
      
      if (trendChart && trendChart.data && trendChart.data.datasets) {
        // 更新趋势图数据
        const trendLabels = academicHistory.value.map(item => item.semester)
        const trendData = academicHistory.value.map(item => item.attendanceRate)
        
        // 添加当前学期数据点
        trendLabels.push('当前')
        trendData.push(attendanceRate.value)
        
        trendChart.data.labels = trendLabels
        trendChart.data.datasets[0].data = trendData
        trendChart.update()
      }
    } else {
      throw new Error(response.message || '获取数据失败')
    }
  } catch (err) {
    error.value = '获取数据失败，使用模拟数据'
    console.error('获取学生状态失败:', err)
    ElMessage.warning('获取学籍状态失败，显示模拟数据')
    
    // 使用模拟数据作为后备 - 与后端API响应格式一致
    const mockResponse = {
      code: 200,
      message: "获取成功",
      data: {
        basicInfo: {
          id: "20220001",
          name: "张三",
          gender: "男",
          birthDate: "2003-05-15",
          idCard: "110101200305156789",
          nation: "汉族",
          politicalStatus: "共青团员",
          enrollmentDate: "2022-09-01",
          educationLevel: "本科",
          major: "计算机科学与技术",
          class: "计科2201班",
          department: "信息工程学院",
          advisor: "李教授",
          phone: "13800138000",
          email: "zhangsan@example.com",
          address: "北京市海淀区XX路XX号"
        },
        academicStatus: {
          status: "normal",
          gpa: "3.65",
          totalCredits: 140,
          completedCredits: 68,
          failedCourses: 2
        },
        // 添加历史学业表现数据，用于趋势图
        academicHistory: [
          { semester: "大一上", gpa: "3.2", attendanceRate: 92 },
          { semester: "大一下", gpa: "3.35", attendanceRate: 94 }
        ]
      }
    }
    
    // 处理模拟数据
    const data = mockResponse.data
    
    // 更新基本信息
    status.value = data.academicStatus.status || 'normal'
    credits.value = data.academicStatus.completedCredits || 0
    totalCredits.value = data.academicStatus.totalCredits || 140
    effectiveDate.value = data.basicInfo.enrollmentDate || '2022-09-01'
    
    // 计算预计毕业时间（假设学制为4年）
    if (data.basicInfo.enrollmentDate) {
      const enrollDate = new Date(data.basicInfo.enrollmentDate)
      const gradDate = new Date(enrollDate)
      gradDate.setFullYear(gradDate.getFullYear() + 4)
      graduationDate.value = gradDate.toISOString().split('T')[0]
    }
    
    // 根据GPA计算学业等级
    const gpa = parseFloat(data.academicStatus.gpa || 0)
    if (gpa >= 3.7) performanceLevel.value = '优秀'
    else if (gpa >= 3.0) performanceLevel.value = '良好'
    else if (gpa >= 2.0) performanceLevel.value = '中等'
    else if (gpa >= 1.0) performanceLevel.value = '及格'
    else performanceLevel.value = '不及格'
    
    // 使用历史数据中的出勤率
    attendanceRate.value = data.academicHistory && data.academicHistory.length > 0 
      ? data.academicHistory[data.academicHistory.length - 1].attendanceRate 
      : 96
    
    // 保存历史数据用于趋势图
    academicHistory.value = data.academicHistory || []
    
    // 重新初始化图表以更新数据
    if (creditChart && creditChart.data && creditChart.data.datasets) {
      creditChart.data.datasets[0].data = [credits.value, totalCredits.value - credits.value]
      creditChart.update()
    }
    
    if (trendChart && trendChart.data && trendChart.data.datasets) {
        // 更新趋势图数据
        const trendLabels = academicHistory.value.map(item => item.semester)
        const trendData = academicHistory.value.map(item => item.attendanceRate)
        
        // 添加当前学期数据点
        trendLabels.push('当前')
        trendData.push(attendanceRate.value)
        
        trendChart.data.labels = trendLabels
        trendChart.data.datasets[0].data = trendData
        trendChart.update()
      }
  } finally {
    loading.value = false
  }
}

// 鼠标移动事件处理 - 用于创建卡片的光影效果
const handleMouseMove = (e) => {
  // 直接更新CSS变量，避免响应式变量更新导致的重渲染
  document.documentElement.style.setProperty('--mouse-x', `${e.clientX}px`)
  document.documentElement.style.setProperty('--mouse-y', `${e.clientY}px`)
}

// 计算属性 - 学分完成百分比
const creditProgress = computed(() => {
  return Math.round((credits.value / totalCredits.value) * 100)
})

// 计算属性 - 根据学分进度确定进度条颜色
const progressColor = computed(() => {
  const progress = creditProgress.value
  if (progress < 30) return '#ef4444' // 红色：进度较低
  if (progress < 60) return '#f59e0b' // 橙色：进度中等
  return '#10b981' // 绿色：进度良好
})

// 计算属性 - 根据出勤率确定进度条颜色
const attendanceColor = computed(() => {
  const rate = attendanceRate.value
  if (rate < 70) return '#ef4444' // 红色：出勤率低
  if (rate < 90) return '#f59e0b' // 橙色：出勤率中等
  return '#10b981' // 绿色：出勤率高
})

// 将学业等级转换为评分（用于星级显示）
const levelToRating = (level) => {
  const levelMap = {
    '优秀': 5,
    '良好': 4,
    '中等': 3,
    '及格': 2,
    '不及格': 1
  }
  return levelMap[level] || 3 // 默认返回中等评分
}

// 计算属性：根据暗色模式动态调整评分颜色
const ratingColors = computed(() => {
  return isDarkMode.value
    ? ['rgba(255, 255, 255, 0.3)', '#f7ba1e', '#f7ba1e', '#f7ba1e', '#10b981']
    : ['rgba(0, 0, 0, 0.2)', '#3b82f6', '#3b82f6', '#10b981', '#10b981']
})

// 日期格式化方法 - 将YYYY-MM-DD格式转换为本地日期格式
const formatDate = (dateStr) => {
  if (!dateStr) return '--'
  return new Date(dateStr).toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  }).replace(/\//g, '-')
}

// 初始化图表
const initCharts = () => {
  nextTick(() => {
    // 检查canvas元素是否存在
    if (!creditDoughnutCanvas.value || !performanceTrendCanvas.value) {
      console.error('Canvas elements not found')
      return
    }
    
    // 学分环形图
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
            '#409eff', // 已修部分颜色
            isDarkMode.value ? 'rgba(255, 255, 255, 0.1)' : 'rgba(0, 0, 0, 0.05)' // 剩余部分颜色，根据暗色模式调整
          ],
          borderWidth: 0,
          cutout: '70%' // 环形图中心空洞大小
        }]
      },
      options: {
        responsive: true,
        maintainAspectRatio: false,
        plugins: {
          legend: {
            display: false // 不显示图例
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

    // 学业表现趋势图
    const trendCtx = performanceTrendCanvas.value.getContext('2d')
    if (!trendCtx) {
      console.error('Failed to get trend chart context')
      return
    }
    
    // 准备趋势图数据
    const trendLabels = academicHistory.value.map(item => item.semester)
    const trendData = academicHistory.value.map(item => item.attendanceRate)
    
    // 添加当前学期数据点
    trendLabels.push('当前')
    trendData.push(attendanceRate.value)
    
    trendChart = new Chart(trendCtx, {
      type: 'line',
      data: {
        labels: trendLabels,
        datasets: [{
          label: '学业表现',
          data: trendData,
          borderColor: '#409eff', // 线条颜色
          backgroundColor: isDarkMode.value
            ? 'rgba(64, 158, 255, 0.1)'
            : 'rgba(64, 158, 255, 0.2)', // 填充颜色，根据暗色模式调整
          tension: 0.4, // 线条弯曲度
          fill: true, // 填充区域
          pointBackgroundColor: '#fff', // 数据点背景色
          pointBorderColor: '#409eff', // 数据点边框色
          pointBorderWidth: 2, // 数据点边框宽度
          pointRadius: 4, // 数据点半径
          pointHoverRadius: 6 // 鼠标悬停时数据点半径
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
                : 'rgba(0, 0, 0, 0.05)' // 网格线颜色，根据暗色模式调整
            }
          },
          x: {
            grid: {
              display: false // 不显示x轴网格线
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

// 监听暗色模式变化，更新图表样式
watch(isDarkMode, (newVal) => {
  // 更新学分环形图样式
  if (creditChart) {
    creditChart.data.datasets[0].backgroundColor[1] = newVal
      ? 'rgba(255, 255, 255, 0.1)'
      : 'rgba(0, 0, 0, 0.05)'
    creditChart.update()
  }

  // 更新学业表现趋势图样式
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

// 组件挂载时初始化数据和图表
onMounted(async () => {
  // 先获取学生状态数据
  await fetchStudentStatus()
  
  // 等待DOM更新完成后初始化图表
  await nextTick()
  initCharts()

  // 数字计数器动画 - 用于学分和出勤率的数字增长动画
  const counters = document.querySelectorAll('.counter')
  counters.forEach(counter => {
    const target = parseInt(counter.innerText)
    counter.innerText = '0'

    const updateCounter = () => {
      const current = parseInt(counter.innerText)
      const increment = Math.ceil(target / 20)

      if (current < target) {
        counter.innerText = current + increment
        setTimeout(updateCounter, 50)
      } else {
        counter.innerText = target
      }
    }

    updateCounter()
  })
})

// 组件卸载时清理资源
onUnmounted(() => {
  if (creditChart) creditChart.destroy() // 销毁学分图表
  if (trendChart) trendChart.destroy() // 销毁趋势图表
})
</script>

<style scoped lang="scss">
// 仪表盘整体样式
.status-dashboard {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  transition: background-color 0.3s ease;
  padding: 0 15px;
  --mouse-x: 0;
  --mouse-y: 0;
}

// 主内容区域
.dashboard-content {
  flex: 1;
  width: 100%;
  max-width: 1400px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: 1fr;
  gap: 30px;
}

// 现代化卡片样式 - 与主页保持一致的卡片设计语言
.modern-card {
  position: relative;
  border-radius: 16px;
  padding: 30px;
  transition: all 0.3s ease;
  overflow: hidden;
  z-index: 1;

  // 卡片内部相对定位
  .card-content {
    position: relative;
    z-index: 2;
  }

  // 浅色模式
  background: white;
  border: 1px solid rgba(0, 0, 0, 0.1);
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.05);

  // 深色模式样式
  .dark & {
    background: rgba(30, 41, 59, 0.8);
    backdrop-filter: blur(12px);
    border: 1px solid rgba(255, 255, 255, 0.1);
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
  }

  // 卡片光影效果 - 随鼠标位置变化
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

  // 卡片悬停效果
  &:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);

    &::before {
      opacity: 1;
    }
  }

  // 卡片头部
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

// 学籍状态卡片
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
        border-radius: 999px;
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

      // 深色模式
      .dark & {
        background-color: rgba(255, 255, 255, 0.05);
      }

      // 卡片悬停效果
      &:hover {
        transform: translateY(-4px);
        box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
      }

      // 统计值样式
      .stat-value {
        font-size: 32px;
        font-weight: 700;
        color: var(--text-primary);
        margin-bottom: 8px;
      }

      // 统计标签样式
      .stat-label {
        font-size: 15px;
        color: var(--text-secondary);
        margin-bottom: 12px;
      }

      // 进度条样式
      .stat-progress {
        margin-top: 12px;
      }

      // 评分样式
      .stat-rating {
        margin-top: 12px;
      }
    }
  }

  // 增强评分组件的视觉效果
  .stat-rating {
    margin-top: 12px;

    .el-rate__item {
      // 添加过渡效果
      transition: transform 0.2s ease;

      // 鼠标悬停时的微动画
      &:hover {
        transform: scale(1.1);
      }
    }
  }
}

// 图表卡片
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

// 图表容器样式
.charts-container {
  display: grid;
  grid-template-columns: 1fr;
  gap: 30px;

  @media (min-width: 1024px) {
    grid-template-columns: 1fr 1fr;
  }
}

// 颜色变量 - 与主页保持一致的配色方案
:root {
  --text-primary: #303133;
  --text-secondary: #606266;
}

.dark {
  --text-primary: #ffffff;
  --text-secondary: rgba(255, 255, 255, 0.7);
}

// 加载状态样式
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

// 错误提示样式
.error-container {
  margin: 20px 0;
}

// 旋转动画
@keyframes rotating {
  from {
    transform: rotate(0deg);
  }
  to {
    transform: rotate(360deg);
  }
}
</style>