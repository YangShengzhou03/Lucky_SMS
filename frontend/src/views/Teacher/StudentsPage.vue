<template>
  <div class="student-management-container" @mousemove="handleMouseMove">
    <div class="modern-card">
      <div class="panel-header">
        <h3>
          <el-icon>
            <DataAnalysis />
          </el-icon>
          学生总览
        </h3>
        <div class="department-selector">
          <el-select v-model="currentDepartment" placeholder="选择院系" size="small" class="modern-select">
            <el-option v-for="item in departments" :key="item.value" :label="item.label" :value="item.value" />
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
      <div class="student-list-panel">
        <div class="student-list modern-card">
          <div class="panel-header">
            <h3>
              <el-icon>
                <User />
              </el-icon>
              学生信息列表
            </h3>
            <div class="header-actions">
              <div v-if="selectedStudents.length > 0" class="batch-actions">
                <span>已选择 {{ selectedStudents.length }} 名学生</span>
                <el-button @click="batchDeleteStudents" type="danger" size="small" style="margin-left: 12px">
                  批量删除
                </el-button>
              </div>
              <div class="search-filter">
            </div>
              <el-input v-model="searchQuery" placeholder="搜索学生姓名/学号" size="small" clearable class="modern-input">
                <template #suffix>
                  <el-icon>
                    <Search />
                  </el-icon>
                </template>
              </el-input>
              <el-select v-model="filterGrade" placeholder="筛选年级" size="small" class="modern-select">
                <el-option v-for="grade in grades" :key="grade.value" :label="grade.label" :value="grade.value" />
              </el-select>
              <el-select v-model="filterClass" placeholder="筛选班级" size="small" class="modern-select">
                <el-option v-for="classItem in classes" :key="classItem.value" :label="classItem.label"
                  :value="classItem.value" />
              </el-select>
            </div>
          </div>

          <el-table :data="filteredStudents" stripe highlight-current-row class="student-table"
            @row-click="viewStudentDetail" :row-selection="{ selectedRowKeys: selectedRowKeys, onChange: handleSelectionChange }">
            <el-table-column type="selection" width="55"></el-table-column>
            <el-table-column prop="studentId" label="学号" width="120"></el-table-column>
            <el-table-column prop="name" label="姓名" width="100"></el-table-column>
            <el-table-column label="性别" width="80">
              <template #default="scope">
                {{ scope.row.gender === 'male' ? '男' : '女' }}
              </template>
            </el-table-column>
            <el-table-column prop="major" label="专业" width="120"></el-table-column>
            <el-table-column prop="grade" label="年级" width="100"></el-table-column>
            <el-table-column label="班级" width="100">
              <template #default="scope">
                {{ scope.row.class || '-' }}
              </template>
            </el-table-column>
            <el-table-column label="寝室号" width="100">
              <template #default="scope">
                <div class="ellipsis-cell">{{ scope.row.dormitory || '-' }}</div>
              </template>
            </el-table-column>
            <el-table-column prop="phone" label="电话" width="130">
              <template #default="scope">
                <div class="ellipsis-cell">{{ scope.row.phone }}</div>
              </template>
            </el-table-column>
            <el-table-column label="状态" width="100">
              <template #default="scope">
                <el-tag :type="getTagType(scope.row.status)">{{ statusMap[scope.row.status] }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="140">
              <template #default="scope">
                <el-button @click.stop="editStudent(scope.row)" type="primary" size="small">编辑</el-button>
                <el-button @click.stop="deleteStudent(scope.row.id)" type="danger" size="small"
                  style="margin-left: 8px;">删除</el-button>
              </template>
            </el-table-column>
          </el-table>

          <div v-if="!filteredStudents.length && !searchQuery" class="no-students">
            <el-empty description="暂无学生数据" />
          </div>

          <div v-if="!filteredStudents.length && searchQuery" class="no-students">
            <el-empty image="search" description="没有找到匹配的学生" />
          </div>

          <div class="pagination">
            <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange"
              :current-page="currentPage" :page-sizes="[5, 10, 15]" :page-size="pageSize"
              layout="total, sizes, prev, pager, next, jumper" :total="allStudents.length">
            </el-pagination>
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
  User,
  Search,
  School,
  Check,
  Warning
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const currentDepartment = ref('all')
const departments = ref([
  { value: 'all', label: '全部院系' },
  { value: 'cs', label: '计算机学院' },
  { value: 'math', label: '数学学院' },
  { value: 'phy', label: '物理学院' },
  { value: 'che', label: '化学学院' }
])

const grades = ref([
  { value: 'all', label: '全部年级' },
  { value: '2021', label: '2021级' },
  { value: '2022', label: '2022级' },
  { value: '2023', label: '2023级' },
  { value: '2024', label: '2024级' }
])

const classes = ref([
  { value: 'all', label: '全部班级' },
  { value: 'class1', label: '1班' },
  { value: 'class2', label: '2班' },
  { value: 'class3', label: '3班' },
  { value: 'class4', label: '4班' }
])

const statusMap = {
  normal: '正常在校',
  sick: '病假',
  absent: '缺勤',
  leave: '请假',
  graduate: '已毕业'
}

const hoveredStat = ref(null)
const searchQuery = ref('')
const filterGrade = ref('all')
const filterClass = ref('all')
const currentPage = ref(1)
const pageSize = ref(5)
const selectedRowKeys = ref([])
const selectedStudents = ref([])

const allStudents = ref([
  {
    id: 1,
    studentId: '2021001',
    name: '张三',
    major: '计算机科学',
    grade: '2021',
    class: 'class1',
    phone: '13800138000',
    gender: 'male',
    dormitory: '1号楼101',
    status: 'normal',
    admissionDate: '2021-09-01',
    birthdate: '2003-05-12'
  },
  {
    id: 2,
    studentId: '2021002',
    name: '李四',
    major: '计算机科学',
    grade: '2021',
    class: 'class1',
    phone: '13900139000',
    gender: 'female',
    dormitory: '2号楼201',
    status: 'sick',
    admissionDate: '2021-09-01',
    birthdate: '2003-08-23'
  },
  {
    id: 3,
    studentId: '2022001',
    name: '王五',
    major: '软件工程',
    grade: '2022',
    class: 'class2',
    phone: '13700137000',
    gender: 'male',
    dormitory: '1号楼102',
    status: 'normal',
    admissionDate: '2022-09-01',
    birthdate: '2004-03-15'
  },
  {
    id: 4,
    studentId: '2022002',
    name: '赵六',
    major: '软件工程',
    grade: '2022',
    class: 'class2',
    phone: '13600136000',
    gender: 'female',
    dormitory: '2号楼202',
    status: 'leave',
    admissionDate: '2022-09-01',
    birthdate: '2004-11-30'
  },
  {
    id: 5,
    studentId: '2023001',
    name: '钱七',
    major: '数据科学',
    grade: '2023',
    class: 'class3',
    phone: '13500135000',
    gender: 'male',
    dormitory: '1号楼103',
    status: 'normal',
    admissionDate: '2023-09-01',
    birthdate: '2005-07-08'
  },
  {
    id: 6,
    studentId: '2023002',
    name: '孙八',
    major: '数据科学',
    grade: '2023',
    class: 'class3',
    phone: '13400134000',
    gender: 'female',
    dormitory: '2号楼203',
    status: 'absent',
    admissionDate: '2023-09-01',
    birthdate: '2005-09-21'
  },
  {
    id: 7,
    studentId: '2021003',
    name: '周九',
    major: '人工智能',
    grade: '2021',
    class: 'class4',
    phone: '13300133000',
    gender: 'male',
    dormitory: '1号楼104',
    status: 'graduate',
    admissionDate: '2021-09-01',
    birthdate: '2003-02-17'
  },
  {
    id: 8,
    studentId: '2022003',
    name: '吴十',
    major: '人工智能',
    grade: '2022',
    class: 'class4',
    phone: '13200132000',
    gender: 'female',
    dormitory: '2号楼204',
    status: 'normal',
    admissionDate: '2022-09-01',
    birthdate: '2004-06-30'
  }
])

const stats = computed(() => [
  {
    value: allStudents.value.length,
    label: '总学生数',
    icon: User,
    color: '#6366f1'
  },
  {
    value: allStudents.value.filter(s => s.status === 'normal').length,
    label: '正常在校',
    icon: Check,
    color: '#10b981'
  },
  {
    value: allStudents.value.filter(s => s.status !== 'normal' && s.status !== 'graduate').length,
    label: '特殊状态',
    icon: Warning,
    color: '#f59e0b'
  },
  {
    value: new Set(allStudents.value.map(s => s.major)).size,
    label: '专业数量',
    icon: School,
    color: '#ef4444'
  }
])

const filteredStudents = computed(() => {
  let filtered = allStudents.value

  if (currentDepartment.value !== 'all') {
    filtered = filtered.filter(student => {
      const majorMap = {
        cs: ['计算机科学', '软件工程'],
        math: ['数据科学'],
        phy: [],
        che: []
      }
      return majorMap[currentDepartment.value].includes(student.major)
    })
  }

  if (filterGrade.value !== 'all') {
    filtered = filtered.filter(student => student.grade === filterGrade.value)
  }

  if (filterClass.value !== 'all') {
    filtered = filtered.filter(student => student.class === filterClass.value)
  }

  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(student =>
      student.name.toLowerCase().includes(query) ||
      student.studentId.toLowerCase().includes(query) ||
      student.major.toLowerCase().includes(query)
    )
  }

  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filtered.slice(start, end)
})

const getTagType = (status) => {
  return status === 'normal' ? 'success' :
    status === 'sick' ? 'warning' :
      status === 'leave' ? 'info' :
        status === 'absent' ? 'danger' : 'default'
}

const hoverStat = (index) => {
  hoveredStat.value = index
}

const unhoverStat = (index) => {
  if (hoveredStat.value === index) {
    hoveredStat.value = null
  }
}

const viewStudentDetail = (student) => {
  ElMessage.info(`查看学生详情: ${student.name}`)
}

const editStudent = (student) => {
  ElMessage.success(`进入编辑模式: ${student.name}`)
}

const deleteStudent = (studentId) => {
  const student = allStudents.value.find(s => s.id === studentId)
  if (student) {
    allStudents.value = allStudents.value.filter(s => s.id !== studentId)
    ElMessage.success(`已删除学生: ${student.name}`)
  }
}

const handleSelectionChange = (selection) => {
  selectedRowKeys.value = selection.map(row => row.id)
  selectedStudents.value = selection
}

const batchDeleteStudents = () => {
  if (selectedStudents.value.length === 0) {
    ElMessage.warning('请先选择要删除的学生')
    return
  }
  
  ElMessageBox.confirm(
    `确定要删除选中的 ${selectedStudents.value.length} 名学生吗？`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  )
  .then(() => {
    // 删除选中的学生
    const idsToDelete = selectedStudents.value.map(s => s.id)
    allStudents.value = allStudents.value.filter(s => !idsToDelete.includes(s.id))
    ElMessage.success('批量删除成功')
    selectedRowKeys.value = []
    selectedStudents.value = []
  })
  .catch(() => {
    // 取消删除
  })
}

const handleSizeChange = (newSize) => {
  pageSize.value = newSize
}

const handleCurrentChange = (newPage) => {
  currentPage.value = newPage
}

onMounted(() => {
  console.log('学生管理系统已加载')
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

.student-management-container {
  display: flex;
  flex-direction: column;
  gap: 30px;
  margin: 0 auto;
  padding: 0px 15px;
  --mouse-x: 0;
}

.main-content {
  display: flex;
  flex-direction: column;
  gap: 30px;
}

.modern-card {
  position: relative;
  border-radius: 8px;
  padding: 20px;
  transition: all 0.15s ease;
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
    transition: all 0.15s ease;
    position: relative;
    overflow: hidden;
    border: 1px solid rgba(226, 232, 240, 0.6);

    .dark & {
      background: rgba(30, 41, 59, 0.9);
      border-color: rgba(74, 85, 104, 0.4);
    }

    &:hover {
      box-shadow: 0 10px 25px -5px rgba(0, 0, 0, 0.1);
      border-color: rgba(199, 210, 254, 0.8);
    }

    .dark &:hover {
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

.student-table {
  margin-bottom: 24px;
  min-width: 100%;

  .el-table__header {
    th {
      background-color: rgba(242, 243, 245, 0.8);
      font-weight: 600;
      color: #333;

      .dark & {
        background-color: rgba(51, 65, 85, 0.8);
        color: var(--dark-text-primary);
      }
    }
  }

  .el-table__row {
    &:hover {
      background-color: rgba(239, 246, 255, 0.5);

      .dark & {
        background-color: rgba(71, 85, 105, 0.5);
      }
    }

    &.current-row {
      background-color: rgba(226, 232, 240, 0.8);

      .dark & {
        background-color: rgba(100, 116, 139, 0.3);
      }
    }
  }

  .el-tag {
    padding: 3px 8px;
    font-size: 12px;
  }

  .ellipsis-cell {
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
    max-width: 100%;
  }
}

.no-students {
  padding: 30px 0;
  display: flex;
  justify-content: center;
  align-items: center;
}

.pagination {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>