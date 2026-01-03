<template>
  <div class="admin-home">
    <div class="welcome-section">
      <div class="welcome-card">
        <div class="avatar-container">
          <el-avatar :size="80" :src="adminInfo?.avatar">
            <span class="avatar-text">{{ adminInfo?.name?.charAt(0) || 'A' }}</span>
          </el-avatar>
          <div class="online-indicator" :class="{ 'online': adminInfo?.online }"></div>
        </div>
        <div class="welcome-content">
          <h2>欢迎回来，<span class="username">{{ adminInfo?.name || '系统管理员' }}</span></h2>
          <p class="subtitle">管理员编号：{{ adminInfo?.id || '--' }} | {{ adminInfo?.department || '系统管理' }}</p>
          <div class="welcome-stats">
            <div class="stat-item">
              <div class="stat-value">{{ adminInfo?.totalUsers || '--' }}</div>
              <div class="stat-label">总用户数</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ adminInfo?.totalStudents || '--' }}</div>
              <div class="stat-label">学生总数</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ adminInfo?.totalTeachers || '--' }}</div>
              <div class="stat-label">教师总数</div>
            </div>
            <div class="stat-item">
              <div class="stat-value">{{ adminInfo?.totalCourses || '--' }}</div>
              <div class="stat-label">课程总数</div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="dashboard-grid">
      <div class="dashboard-card">
        <div class="card-header">
          <h3><el-icon><User /></el-icon> 用户统计</h3>
        </div>
        <div class="card-content">
          <div class="chart-container">
            <div class="stat-item">
              <div class="stat-icon">
                <el-icon><User /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ adminInfo?.totalUsers || '--' }}</div>
                <div class="stat-label">总用户数</div>
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-icon">
                <el-icon><School /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ adminInfo?.totalStudents || '--' }}</div>
                <div class="stat-label">学生数量</div>
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-icon">
                <el-icon><Reading /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ adminInfo?.totalTeachers || '--' }}</div>
                <div class="stat-label">教师数量</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="dashboard-card">
        <div class="card-header">
          <h3><el-icon><Notebook /></el-icon> 课程统计</h3>
        </div>
        <div class="card-content">
          <div class="chart-container">
            <div class="stat-item">
              <div class="stat-icon">
                <el-icon><Notebook /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ adminInfo?.totalCourses || '--' }}</div>
                <div class="stat-label">课程总数</div>
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-icon">
                <el-icon><DataAnalysis /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ adminInfo?.avgCourseScore || '--' }}</div>
                <div class="stat-label">平均分</div>
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-icon">
                <el-icon><TrendCharts /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ adminInfo?.courseCompletionRate || '--' }}%</div>
                <div class="stat-label">完成率</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="dashboard-card">
        <div class="card-header">
          <h3><el-icon><OfficeBuilding /></el-icon> 部门统计</h3>
        </div>
        <div class="card-content">
          <div class="chart-container">
            <div class="stat-item">
              <div class="stat-icon">
                <el-icon><OfficeBuilding /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ adminInfo?.totalDepartments || '--' }}</div>
                <div class="stat-label">部门总数</div>
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-icon">
                <el-icon><UserFilled /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ adminInfo?.avgDepartmentUsers || '--' }}</div>
                <div class="stat-label">平均人数</div>
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-icon">
                <el-icon><Collection /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ adminInfo?.totalMajors || '--' }}</div>
                <div class="stat-label">专业数量</div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="dashboard-card">
        <div class="card-header">
          <h3><el-icon><DataAnalysis /></el-icon> 系统状态</h3>
        </div>
        <div class="card-content">
          <div class="chart-container">
            <div class="stat-item">
              <div class="stat-icon">
                <el-icon><Cpu /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ adminInfo?.systemStatus || '正常' }}</div>
                <div class="stat-label">系统状态</div>
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-icon">
                <el-icon><Timer /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ adminInfo?.uptime || '--' }}</div>
                <div class="stat-label">运行时间</div>
              </div>
            </div>
            <div class="stat-item">
              <div class="stat-icon">
                <el-icon><Monitor /></el-icon>
              </div>
              <div class="stat-content">
                <div class="stat-value">{{ adminInfo?.activeUsers || '--' }}</div>
                <div class="stat-label">在线用户</div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="quick-actions">
      <h3>快捷操作</h3>
      <div class="action-grid">
        <el-card class="action-card" @click="navigateToUsers">
          <div class="action-content">
            <el-icon class="action-icon"><User /></el-icon>
            <div class="action-text">用户管理</div>
          </div>
        </el-card>
        <el-card class="action-card" @click="navigateToCourses">
          <div class="action-content">
            <el-icon class="action-icon"><Notebook /></el-icon>
            <div class="action-text">课程管理</div>
          </div>
        </el-card>
        <el-card class="action-card" @click="navigateToGrades">
          <div class="action-content">
            <el-icon class="action-icon"><EditPen /></el-icon>
            <div class="action-text">成绩管理</div>
          </div>
        </el-card>
        <el-card class="action-card" @click="navigateToDepartments">
          <div class="action-content">
            <el-icon class="action-icon"><OfficeBuilding /></el-icon>
            <div class="action-text">部门管理</div>
          </div>
        </el-card>
        <el-card class="action-card" @click="navigateToSettings">
          <div class="action-content">
            <el-icon class="action-icon"><Setting /></el-icon>
            <div class="action-text">系统设置</div>
          </div>
        </el-card>
        <el-card class="action-card" @click="navigateToStatistics">
          <div class="action-content">
            <el-icon class="action-icon"><DataAnalysis /></el-icon>
            <div class="action-text">数据统计</div>
          </div>
        </el-card>
      </div>
    </div>

    <div class="recent-activities">
      <div class="card-header">
        <h3><el-icon><Clock /></el-icon> 最近活动</h3>
      </div>
      <div class="activity-list">
        <div v-for="activity in recentActivities" :key="activity.id" class="activity-item">
          <div class="activity-icon">
            <el-icon>
              <component :is="activity.icon" />
            </el-icon>
          </div>
          <div class="activity-content">
            <div class="activity-title">{{ activity.title }}</div>
            <div class="activity-time">{{ activity.time }}</div>
          </div>
          <div class="activity-status" :class="activity.status">{{ activity.statusText }}</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import {
  User, Notebook, EditPen, OfficeBuilding, Setting, DataAnalysis,
  School, Reading, Collection, Cpu, Timer, Monitor, Clock
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { getAdminHomeData } from '@/api/admin'

const router = useRouter()
const adminInfo = ref({})
const recentActivities = ref([])

const fetchAdminData = async () => {
  try {
    const res = await getAdminHomeData()
    if (res.code === 200) {
      adminInfo.value = res.data.admin || {}
      recentActivities.value = res.data.recentActivities || []
    } else {
      ElMessage.error(res.message || '获取数据失败')
    }
  } catch (error) {
    ElMessage.error('获取数据失败')
    console.error('获取管理员数据失败:', error)
  }
}

const navigateToUsers = () => {
  router.push('/admin/users')
}

const navigateToCourses = () => {
  router.push('/admin/courses')
}

const navigateToGrades = () => {
  router.push('/admin/grades')
}

const navigateToDepartments = () => {
  router.push('/admin/departments')
}

const navigateToSettings = () => {
  router.push('/admin/settings')
}

const navigateToStatistics = () => {
  router.push('/admin/statistics')
}

onMounted(() => {
  fetchAdminData()
})
</script>

<style scoped lang="scss">
.admin-home {
  padding: 0;
}

.welcome-section {
  margin-bottom: 30px;
}

.welcome-card {
  display: flex;
  align-items: center;
  background: white;
  color: #2c3e50;
  padding: 20px;
  border-radius: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.1);
}

.avatar-container {
  position: relative;
  margin-right: 24px;
}

.online-indicator {
  position: absolute;
  bottom: 2px;
  right: 2px;
  width: 12px;
  height: 12px;
  border-radius: 50%;
  background: #dc3545;
  border: 2px solid white;
}

.online-indicator.online {
  background: #28a745;
}

.avatar-text {
  font-size: 24px;
  font-weight: bold;
}

.welcome-content {
  flex: 1;
}

.welcome-content h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
  font-weight: 600;
}

.username {
  color: #409eff;
}

.subtitle {
  margin: 0 0 20px 0;
  opacity: 0.8;
  font-size: 14px;
  color: #606266;
}

.welcome-stats {
  display: flex;
  gap: 30px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 24px;
  font-weight: bold;
  margin-bottom:4px;
}

.stat-label {
  font-size: 12px;
  opacity: 0.8;
}

.dashboard-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
  gap: 20px;
  margin-bottom: 30px;
}

.dashboard-card {
  background: white;
  border-radius: 4px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.card-header h3 {
  margin: 0;
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
}

.card-header .el-icon {
  margin-right: 8px;
  color: #409eff;
}

.chart-container {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.stat-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-icon .el-icon {
  font-size: 20px;
  color: #409eff;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 2px;
}

.stat-label {
  font-size: 12px;
  color: #909399;
}

.quick-actions {
  margin-bottom: 30px;
}

.quick-actions h3 {
  margin-bottom: 20px;
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
}

.action-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
}

.action-card {
  cursor: pointer;
  transition: all 0.15s ease;
  border: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.action-card:hover {
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
}

.action-content {
  text-align: center;
  padding: 20px;
}

.action-icon {
  font-size: 32px;
  color: #409eff;
  margin-bottom: 12px;
}

.action-text {
  font-size: 14px;
  font-weight: 500;
  color: #2c3e50;
}

.recent-activities {
  background: white;
  border-radius: 4px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  border-radius: 8px;
  transition: background-color 0.3s ease;
}

.activity-item:hover {
  background: #f5f7fa;
}

.activity-icon {
  width: 32px;
  height: 32px;
  border-radius: 4px;
  background: #f5f7fa;
  display: flex;
  align-items: center;
  justify-content: center;
}

.activity-icon .el-icon {
  font-size: 16px;
  color: #409eff;
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-size: 14px;
  font-weight: 500;
  color: #2c3e50;
  margin-bottom: 2px;
}

.activity-time {
  font-size: 12px;
  color: #909399;
}

.activity-status {
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 4px;
  font-weight: 500;
}

.activity-status.success {
  background: #f0f9ff;
  color: #1890ff;
}

.activity-status.warning {
  background: #fff7e6;
  color: #fa8c16;
}

.activity-status.info {
  background: #f6ffed;
  color: #52c41a;
}

@media (max-width: 768px) {
  .welcome-card {
    flex-direction: column;
    text-align: center;
  }
  
  .avatar-container {
    margin-right: 0;
    margin-bottom: 16px;
  }
  
  .welcome-stats {
    justify-content: center;
  }
  
  .dashboard-grid {
    grid-template-columns: 1fr;
  }
  
  .action-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}
</style>
