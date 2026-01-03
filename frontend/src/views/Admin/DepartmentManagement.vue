<template>
  <div class="department-management">
    <div class="search-filter">
      <el-input
        v-model="searchQuery"
        placeholder="搜索部门名称/部门代码"
        clearable
        style="width: 300px; margin-right: 16px;"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      
      <el-button type="primary" @click="searchDepartments">搜索</el-button>
      
      <el-button type="primary" @click="showAddDepartmentDialog">
        <el-icon><Plus /></el-icon>
        新增部门
      </el-button>
    </div>

    <div class="department-stats">
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-value">{{ statistics.totalDepartments }}</div>
          <div class="stat-label">部门总数</div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-value">{{ statistics.totalTeachers }}</div>
          <div class="stat-label">教师总数</div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-value">{{ statistics.totalStudents }}</div>
          <div class="stat-label">学生总数</div>
        </div>
      </el-card>
      <el-card class="stat-card">
        <div class="stat-content">
          <div class="stat-value">{{ statistics.totalCourses }}</div>
          <div class="stat-label">课程总数</div>
        </div>
      </el-card>
    </div>

    <el-table
      :data="departments"
      stripe
      v-loading="loading"
      style="width: 100%; margin-top: 20px;"
      row-key="departmentId"
      :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="departmentCode" label="部门代码" width="120" />
      <el-table-column prop="departmentName" label="部门名称" width="200" />
      <el-table-column label="部门类型" width="100">
        <template #default="{ row }">
          <el-tag :type="getTypeTag(row.departmentType)">{{ row.departmentType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="parentName" label="上级部门" width="150" />
      <el-table-column prop="managerName" label="负责人" width="100" />
      <el-table-column prop="phone" label="联系电话" width="130" />
      <el-table-column prop="email" label="邮箱" width="180" />
      <el-table-column label="用户统计" width="120">
        <template #default="{ row }">
          <div class="user-stats">
            <span class="teacher-count">{{ row.teacherCount || 0 }}</span>
            <span class="student-count">{{ row.studentCount || 0 }}</span>
          </div>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="getStatusTag(row.status)">{{ getStatusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createTime" label="创建时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="editDepartment(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteDepartment(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <el-dialog
      v-model="departmentDialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form :model="departmentForm" :rules="rules" ref="departmentFormRef" label-width="100px">
        <el-form-item label="部门代码" prop="departmentCode">
          <el-input v-model="departmentForm.departmentCode" placeholder="请输入部门代码" />
        </el-form-item>
        <el-form-item label="部门名称" prop="departmentName">
          <el-input v-model="departmentForm.departmentName" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="部门类型" prop="departmentType">
          <el-select v-model="departmentForm.departmentType" placeholder="请选择部门类型" style="width: 100%;">
            <el-option label="学院" value="学院" />
            <el-option label="系部" value="系部" />
            <el-option label="行政" value="行政" />
            <el-option label="后勤" value="后勤" />
          </el-select>
        </el-form-item>
        <el-form-item label="上级部门" prop="parentId">
          <el-select v-model="departmentForm.parentId" placeholder="请选择上级部门" style="width: 100%;" clearable>
            <el-option v-for="dept in parentDepartments" :key="dept.departmentId" 
                       :label="dept.departmentName" :value="dept.departmentId" />
          </el-select>
        </el-form-item>
        <el-form-item label="负责人" prop="managerName">
          <el-input v-model="departmentForm.managerName" placeholder="请输入负责人姓名" />
        </el-form-item>
        <el-form-item label="联系电话" prop="phone">
          <el-input v-model="departmentForm.phone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="departmentForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="部门地址" prop="address">
          <el-input v-model="departmentForm.address" placeholder="请输入部门地址" />
        </el-form-item>
        <el-form-item label="部门描述" prop="description">
          <el-input
            v-model="departmentForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入部门描述"
          />
        </el-form-item>
        <el-form-item label="部门状态" prop="status">
          <el-radio-group v-model="departmentForm.status">
            <el-radio label="active">正常</el-radio>
            <el-radio label="inactive">停用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="departmentDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import { getDepartmentList, addDepartment, updateDepartment, deleteDepartment as apiDeleteDepartment } from '@/api/admin'

const loading = ref(false)
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const departmentDialogVisible = ref(false)
const isAdd = ref(false)
const departmentFormRef = ref()

const departmentForm = ref({
  departmentId: '',
  departmentCode: '',
  departmentName: '',
  departmentType: '',
  parentId: '',
  parentName: '',
  managerName: '',
  phone: '',
  email: '',
  address: '',
  description: '',
  status: 'active',
  teacherCount: 0,
  studentCount: 0
})

const rules = {
  departmentCode: [
    { required: true, message: '请输入部门代码', trigger: 'blur' }
  ],
  departmentName: [
    { required: true, message: '请输入部门名称', trigger: 'blur' }
  ],
  departmentType: [
    { required: true, message: '请选择部门类型', trigger: 'change' }
  ],
  managerName: [
    { required: true, message: '请输入负责人姓名', trigger: 'blur' }
  ],
  phone: [
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ]
}

const statistics = ref({
  totalDepartments: 0,
  totalUsers: 0,
  totalTeachers: 0,
  totalStudents: 0,
  totalCourses: 0
})

const departments = ref([])

const parentDepartments = computed(() => {
  return departments.value.filter(dept => !dept.parentId)
})

const dialogTitle = computed(() => {
  return isAdd.value ? '新增部门' : '编辑部门'
})

const getTypeTag = (type) => {
  const types = {
    '学院': 'danger',
    '系部': 'warning',
    '行政': 'success',
    '后勤': 'info'
  }
  return types[type] || 'info'
}

const getStatusTag = (status) => {
  const types = {
    active: 'success',
    inactive: 'danger'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    active: '正常',
    inactive: '停用'
  }
  return texts[status] || '未知'
}

const showAddDepartmentDialog = () => {
  isAdd.value = true
  departmentDialogVisible.value = true
  resetForm()
}

const editDepartment = (department) => {
  isAdd.value = false
  departmentDialogVisible.value = true
  departmentForm.value = { ...department }
}

const deleteDepartment = async (department) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除部门 "${department.departmentName}" 吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    loading.value = true
    const res = await apiDeleteDepartment(department.departmentId)
    
    if (res.code === 200) {
      ElMessage.success('删除成功')
      await loadDepartments()
      await loadStatistics()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  } finally {
    loading.value = false
  }
}

const submitForm = async () => {
  if (!departmentFormRef.value) return
  
  try {
    await departmentFormRef.value.validate()
    loading.value = true
    
    if (isAdd.value) {
      const res = await addDepartment(departmentForm.value)
      if (res.code === 200) {
        ElMessage.success('新增部门成功')
        departmentDialogVisible.value = false
        resetForm()
        await loadDepartments()
        await loadStatistics()
      } else {
        ElMessage.error(res.message || '新增失败')
      }
    } else {
      const res = await updateDepartment(departmentForm.value)
      if (res.code === 200) {
        ElMessage.success('更新部门成功')
        departmentDialogVisible.value = false
        resetForm()
        await loadDepartments()
        await loadStatistics()
      } else {
        ElMessage.error(res.message || '更新失败')
      }
    }
  } catch (error) {
    ElMessage.error('表单验证失败')
  } finally {
    loading.value = false
  }
}

const resetForm = () => {
  departmentForm.value = {
    departmentId: '',
    departmentCode: '',
    departmentName: '',
    departmentType: '',
    parentId: '',
    parentName: '',
    managerName: '',
    phone: '',
    email: '',
    address: '',
    description: '',
    status: 'active',
    teacherCount: 0,
    studentCount: 0
  }
}

const searchDepartments = () => {
  currentPage.value = 1
  loadDepartments()
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  loadDepartments()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadDepartments()
}

const loadDepartments = async () => {
  loading.value = true
  try {
    const res = await getDepartmentList({
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchQuery.value
    })
    
    if (res.code === 200) {
      departments.value = res.data.list || []
      total.value = res.data.total || 0
    } else {
      ElMessage.error(res.message || '获取部门列表失败')
    }
  } catch (error) {
    ElMessage.error('获取部门列表失败')
    console.error('获取部门列表失败:', error)
  } finally {
    loading.value = false
  }
}

const loadStatistics = async () => {
  try {
    const res = await getDepartmentList({
      page: 1,
      size: 10000,
      keyword: ''
    })
    
    if (res.code === 200) {
      const allDepartments = res.data.list || []
      const flatDepartments = allDepartments.flatMap(dept => [dept, ...(dept.children || [])])
      
      statistics.value = {
        totalDepartments: flatDepartments.length,
        totalUsers: flatDepartments.reduce((sum, dept) => sum + (dept.teacherCount || 0) + (dept.studentCount || 0), 0),
        totalTeachers: flatDepartments.reduce((sum, dept) => sum + (dept.teacherCount || 0), 0),
        totalStudents: flatDepartments.reduce((sum, dept) => sum + (dept.studentCount || 0), 0),
        totalCourses: flatDepartments.reduce((sum, dept) => sum + (dept.courseCount || 0), 0)
      }
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

onMounted(() => {
  loadDepartments()
  loadStatistics()
})
</script>

<style scoped lang="scss">
.department-management {
  padding: 20px;
}

.search-filter {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.department-stats {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 20px;
  margin-bottom: 20px;
}

.stat-card {
  text-align: center;
  border: none;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.stat-content {
  padding: 20px;
}

.stat-value {
  font-size: 32px;
  font-weight: bold;
  color: #409eff;
  margin-bottom: 8px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  padding: 20px;
  background: white;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

.el-table {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
}

:deep(.el-table__header) {
  background: #f5f7fa;
}

:deep(.el-table__body) {
  background: white;
}

.user-stats {
  display: flex;
  gap: 8px;
  align-items: center;
}

.teacher-count {
  color: #409eff;
  font-weight: 500;
}

.student-count {
  color: #67c23a;
  font-weight: 500;
}

.el-row {
  margin-bottom: 0;
}

.el-col {
  margin-bottom: 0;
}
</style>
