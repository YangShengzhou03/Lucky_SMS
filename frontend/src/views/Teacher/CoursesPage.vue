<template>
  <div class="course-management-container" @mousemove="handleMouseMove">
    <div class="main-content">
      <div class="course-list-panel">
        <div class="course-list modern-card">
          <div class="panel-header">
            <h3>
                <el-icon>
                  <Collection />
                </el-icon>
                我的课程
              </h3>
              <div class="header-actions">
                <div v-if="selectedCourses.length > 0" class="batch-actions">
                  <span>{{ selectedCourses.length }} </span>
                  <el-button @click="batchDeleteCourses" type="danger" size="small" style="margin-left: 12px">
                    批量删除
                  </el-button>
                </div>
                <el-button @click="openCreateCourseDialog" type="primary" size="small" class="mr-2">
                  <el-icon>
                    <Plus />
                  </el-icon>
                  创建课程
                </el-button>
                <el-select v-model="currentSemester" placeholder="选择学期" size="small" class="modern-select">
                  <el-option v-for="item in semesters" :key="item.value" :label="item.label" :value="item.value" />
                </el-select>
              </div>
          </div>

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

          <div class="courses-container">
            <div v-for="course in filteredCourses" :key="course.id" class="course-card-wrapper">
              <el-checkbox v-model="selectedRowKeys" :label="course.id" @change="handleSelectionChange" class="selection-checkbox"></el-checkbox>
              <el-card class="course-card" shadow="hover">
              <div class="course-header">
                <h4>{{ course.name }}</h4>
                <span class="course-code">{{ course.code }}</span>
              </div>
              <div class="course-info">
                <div class="info-item">
                  <el-icon>
                    <User />
                  </el-icon>
                  <span>{{ course.studentCount }}名学生</span>
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
                <div class="info-item status">
                  <el-tag :type="courseStatusType(course.status)">{{ courseStatusText(course.status) }}</el-tag>
                </div>
              </div>
              <div class="course-footer">
                <el-button-group>
                  <el-button @click="viewCourseDetails(course)" type="primary" size="small">
                    <el-icon>
                      <View />
                    </el-icon>
                    查看
                  </el-button>
                  <el-button @click="editCourse(course)" type="warning" size="small">
                    <el-icon>
                      <Edit />
                    </el-icon>
                    编辑
                  </el-button>
                  <el-button @click="deleteCourse(course)" type="danger" size="small">
                    <el-icon>
                      <Delete />
                    </el-icon>
                    删除
                  </el-button>
                </el-button-group>
              </div>
              </el-card>
            </div>

            <div v-if="!filteredCourses.length && !searchQuery" class="no-courses">
              <el-empty description="暂无课程" />
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
    </div>

    <!-- 创建课程对话框 -->
    <el-dialog :model-value="createCourseDialogVisible" title="创建新课程" @close="createCourseDialogVisible = false">
      <template #content>
        <el-form ref="createCourseForm" :model="newCourse" label-width="120px">
          <el-form-item label="课程名称" prop="name">
            <el-input v-model="newCourse.name" placeholder="请输入课程名称"></el-input>
          </el-form-item>
          <el-form-item label="课程代码" prop="code">
            <el-input v-model="newCourse.code" placeholder="请输入课程代码"></el-input>
          </el-form-item>
          <el-form-item label="课程分类" prop="category">
            <el-select v-model="newCourse.category" placeholder="请选择课程分类">
              <el-option v-for="category in categories" :key="category.value" :label="category.label"
                :value="category.value" />
            </el-select>
          </el-form-item>
          <el-form-item label="学分" prop="credits">
            <el-input-number v-model="newCourse.credits" :min="1" :max="6" :step="1"></el-input-number>
          </el-form-item>
          <el-form-item label="上课时间" prop="time">
            <el-input v-model="newCourse.time" placeholder="请输入上课时间"></el-input>
          </el-form-item>
          <el-form-item label="上课地点" prop="location">
            <el-input v-model="newCourse.location" placeholder="请输入上课地点"></el-input>
          </el-form-item>
          <el-form-item label="课程状态" prop="status">
            <el-radio-group v-model="newCourse.status">
              <el-radio :label="1">未开始</el-radio>
              <el-radio :label="2">进行中</el-radio>
              <el-radio :label="3">已结束</el-radio>
            </el-radio-group>
          </el-form-item>
        </el-form>
      </template>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="createCourseDialogVisible = false">取消</el-button>
          <el-button type="primary" @click="handleCreateCourse">创建课程</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, reactive, onMounted } from 'vue'
import {
  Collection,
  Plus,
  Search,
  User,
  Clock,
  Location,
  CreditCard,
  Edit,
  Delete,
  View
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox, ElCheckbox } from 'element-plus'
import teacherApi from '@/api/teacher'

const currentSemester = ref('2023-2024-2')
const semesters = ref([
  { value: '2023-2024-2', label: '2023-2024学年第二学期' },
  { value: '2023-2024-1', label: '2023-2024学年第一学期' }
])

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
const selectedRowKeys = ref([])
const selectedCourses = ref([])

const allCourses = ref([])

// 计算属性
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

// 课程状态相关函数
const courseStatusText = (status) => {
  const statusMap = {
    1: '未开始',
    2: '进行中',
    3: '已结束'
  }
  return statusMap[status] || '未知'
}

const courseStatusType = (status) => {
  const typeMap = {
    1: 'info',
    2: 'success',
    3: 'primary'
  }
  return typeMap[status] || 'default'
}

// 创建课程相关
const createCourseDialogVisible = ref(false)
const newCourse = reactive({
  name: '',
  code: '',
  category: 'compulsory',
  credits: 3,
  time: '',
  location: '',
  status: 1
})

const handleSizeChange = (newSize) => {
  pageSize.value = newSize
}

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
}

const handleSelectionChange = () => {
  selectedCourses.value = allCourses.value.filter(course => 
    selectedRowKeys.value.includes(course.id)
  )
}

const batchDeleteCourses = () => {
  if (selectedCourses.value.length === 0) {
    ElMessage.warning('请先选择要删除的课程')
    return
  }
  
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedCourses.value.length} 门课程吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
  .then(() => {
    // 删除选中的课程
    const idsToDelete = selectedCourses.value.map(c => c.id)
    allCourses.value = allCourses.value.filter(c => !idsToDelete.includes(c.id))
    ElMessage.success('批量删除成功')
    selectedRowKeys.value = []
    selectedCourses.value = []
  })
  .catch(() => {
    // 取消删除
  })
}

// 创建课程相关方法
const openCreateCourseDialog = () => {
  // 重置表单
  newCourse.name = ''
  newCourse.code = ''
  newCourse.category = 'compulsory'
  newCourse.credits = 3
  newCourse.time = ''
  newCourse.location = ''
  newCourse.status = 1

  createCourseDialogVisible.value = true
}

const handleCreateCourse = () => {
  // 简单验证
  if (!newCourse.name || !newCourse.code) {
    ElMessage.warning('请填写课程名称和课程代码')
    return
  }

  // 创建新课程对象
  const newCourseObj = {
    id: Date.now(), // 使用时间戳作为临时ID
    name: newCourse.name,
    code: newCourse.code,
    teacher: '李教授', // 假设当前教师是李教授
    studentCount: 0,
    time: newCourse.time,
    location: newCourse.location,
    credits: newCourse.credits,
    category: newCourse.category,
    status: newCourse.status,
    schedule: []
  }

  // 添加到课程列表
  allCourses.value.push(newCourseObj)

  // 关闭对话框
  createCourseDialogVisible.value = false

  // 显示成功消息
  ElMessage.success('课程创建成功')
}

// 课程操作方法
const viewCourseDetails = (course) => {
  ElMessage.info(`查看课程详情: ${course.name}`)
  // 这里可以跳转到课程详情页
}

const editCourse = (course) => {
  ElMessage.info(`编辑课程: ${course.name}`)
  // 这里可以打开编辑课程对话框
}

const deleteCourse = (course) => {
  ElMessageBox.confirm(
    `确定要删除课程 "${course.name}" 吗？`,
    '警告',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    ElMessage.success('课程删除成功')
  }).catch(() => {
  })
}

const loadCourses = async () => {
  try {
    const res = await teacherApi.getCoursesList({
      semester: currentSemester.value,
      page: currentPage.value,
      size: pageSize.value
    })
    if (res.code === 200) {
      allCourses.value = res.data.courses || []
    }
  } catch (error) {
    console.error('获取课程列表失败:', error)
    ElMessage.error('获取课程列表失败')
    allCourses.value = []
  }
}

const handleMouseMove = (e) => {
  document.documentElement.style.setProperty('--mouse-x', `${e.clientX}px`)
  document.documentElement.style.setProperty('--mouse-y', `${e.clientY}px`)
}

onMounted(() => {
  loadCourses()
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

.course-management-container {
  display: flex;
  flex-direction: column;
  gap: 30px;
  margin: 0 auto;
  padding: 0px 15px;
}

.main-content {
  display: grid;
  grid-template-columns: 1fr;
  gap: 30px;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 20px;
}

.batch-actions {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 8px 16px;
  background-color: #f5f7fa;
  border-radius: 8px;
  font-size: 14px;
}

.dark .batch-actions {
  background-color: rgba(51, 65, 85, 0.8);
}

.course-card-wrapper {
  position: relative;
  margin-bottom: 16px;
}

.selection-checkbox {
  position: absolute;
  top: 12px;
  left: 12px;
  z-index: 10;
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
    border: 1px solid rgba(255, 255, 255, 0.1);
    box-shadow: 0 6px 20px rgba(0, 0, 0, 0.2);
  }

  &:hover {
    box-shadow: 0 12px 32px rgba(0, 0, 0, 0.15);
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
  margin-bottom: 24px;

  .modern-input {
    width: 200px;

    .el-input__inner {
      background-color: rgba(255, 255, 255, 0.7);
      border: 1px solid rgba(226, 232, 240, 0.8);
      border-radius: 12px;
      padding: 0 12px;
      height: 36px;
      box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
      transition: all 0.1s ease;

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

.courses-container {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 16px;
  margin-bottom: 24px;
}

.course-card {
  display: flex;
  flex-direction: column;
  border-radius: 8px;
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
    box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1);
    border-color: rgba(199, 210, 254, 0.8);
  }

  .dark &:hover {
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
    }
  }

  .course-info {
    display: grid;
    grid-template-columns: 1fr 1fr;
    gap: 10px;
    margin-bottom: 16px;

    .info-item {
      display: flex;
      align-items: center;
      font-size: 14px;
      color: #64748b;

      .dark & {
        color: var(--dark-text-secondary);
      }

      .el-icon {
        margin-right: 8px;
        color: #409eff;
        width: 18px;
        height: 18px;
      }

      .status {
        .el-tag {
          padding: 2px 8px;
          font-size: 12px;
        }
      }
    }
  }

  .course-footer {
    display: flex;
    justify-content: flex-end;
    margin-top: auto;
  }
}

.no-courses {
  padding: 30px 0;
  display: flex;
  justify-content: center;
  width: 100%;
}

.pagination {
  display: flex;
  justify-content: center;
  padding-top: 16px;
}
</style>
