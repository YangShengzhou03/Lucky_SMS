<template>
  <div class="user-management">
    <div class="search-filter">
      <el-input
        v-model="searchQuery"
        placeholder="搜索用户名/姓名/手机号"
        clearable
        style="width: 300px; margin-right: 16px;"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      
      <el-button type="primary" @click="searchUsers">搜索</el-button>
      
      <el-button type="primary" @click="showAddUserDialog">
        <el-icon><Plus /></el-icon>
        新增用户
      </el-button>
    </div>

    <el-table
      :data="filteredUsers"
      stripe
      v-loading="loading"
      style="width: 100%; margin-top: 20px;"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="userId" label="用户ID" width="100" />
      <el-table-column prop="username" label="用户名" width="120" />
      <el-table-column prop="realName" label="姓名" width="100" />
      <el-table-column prop="phone" label="手机号" width="130" />
      <el-table-column prop="email" label="邮箱" width="180" />
      <el-table-column label="角色" width="100">
        <template #default="{ row }">
          <el-tag :type="getRoleType(row.role)">{{ getRoleText(row.role) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.status)">{{ getStatusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="department" label="部门/专业" width="150" />
      <el-table-column prop="createdAt" label="创建时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="editUser(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteUser(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="totalFilteredUsers"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <el-dialog
      v-model="userDialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="resetForm"
    >
      <el-form :model="userForm" :rules="rules" ref="userFormRef" label-width="100px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="userForm.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="userForm.realName" placeholder="请输入真实姓名" />
        </el-form-item>
        <el-form-item label="手机号" prop="phone">
          <el-input v-model="userForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="userForm.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="用户角色" prop="role">
          <el-select v-model="userForm.role" placeholder="请选择用户角色" style="width: 100%;">
            <el-option label="管理员" value="ADMIN" />
            <el-option label="教师" value="TEACHER" />
            <el-option label="学生" value="STUDENT" />
          </el-select>
        </el-form-item>
        <el-form-item label="部门/专业" prop="department">
          <el-input v-model="userForm.department" placeholder="请输入部门或专业" />
        </el-form-item>
        <el-form-item label="用户状态" prop="status">
          <el-radio-group v-model="userForm.status">
            <el-radio label="active">正常</el-radio>
            <el-radio label="inactive">禁用</el-radio>
            <el-radio label="locked">锁定</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item v-if="isAdd" label="初始密码" prop="password">
          <el-input v-model="userForm.password" type="password" placeholder="请输入初始密码" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="userDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'

const loading = ref(false)
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const userDialogVisible = ref(false)
const isAdd = ref(false)
const userFormRef = ref()

const userForm = ref({
  userId: '',
  username: '',
  realName: '',
  phone: '',
  email: '',
  role: '',
  department: '',
  status: 'active',
  password: ''
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { min: 3, max: 20, message: '用户名长度在 3 到 20 个字符', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入真实姓名', trigger: 'blur' }
  ],
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
    { pattern: /^1[3-9]\d{9}$/, message: '请输入正确的手机号', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择用户角色', trigger: 'change' }
  ],
  password: [
    { required: true, message: '请输入初始密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能少于 6 个字符', trigger: 'blur' }
  ]
}

const users = ref([
  {
    userId: 'U1001',
    username: 'admin',
    realName: '系统管理员',
    phone: '13800138000',
    email: 'admin@lucky.edu',
    role: 'ADMIN',
    status: 'active',
    department: '系统管理',
    createdAt: '2023-01-15 10:30:00'
  },
  {
    userId: 'T2001',
    username: 'zhangming',
    realName: '张明',
    phone: '13900139000',
    email: 'zhangming@lucky.edu',
    role: 'TEACHER',
    status: 'active',
    department: '计算机学院',
    createdAt: '2023-02-20 14:20:00'
  },
  {
    userId: 'S3001',
    username: 'lisi',
    realName: '李四',
    phone: '13700137000',
    email: 'lisi@lucky.edu',
    role: 'STUDENT',
    status: 'active',
    department: '计算机科学',
    createdAt: '2023-09-01 09:00:00'
  }
])

const filteredUsers = computed(() => {
  let filtered = users.value
  
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(user => 
      user.username.toLowerCase().includes(query) ||
      user.realName.toLowerCase().includes(query) ||
      user.phone.includes(query)
    )
  }
  
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filtered.slice(start, end)
})

const totalFilteredUsers = computed(() => {
  let filtered = users.value
  
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(user => 
      user.username.toLowerCase().includes(query) ||
      user.realName.toLowerCase().includes(query) ||
      user.phone.includes(query)
    )
  }
  
  return filtered.length
})

const dialogTitle = computed(() => {
  return isAdd.value ? '新增用户' : '编辑用户'
})

const getRoleType = (role) => {
  const types = {
    ADMIN: 'danger',
    TEACHER: 'warning',
    STUDENT: 'success'
  }
  return types[role] || 'info'
}

const getRoleText = (role) => {
  const texts = {
    ADMIN: '管理员',
    TEACHER: '教师',
    STUDENT: '学生'
  }
  return texts[role] || '未知'
}

const getStatusType = (status) => {
  const types = {
    active: 'success',
    inactive: 'danger',
    locked: 'warning'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    active: '正常',
    inactive: '禁用',
    locked: '锁定'
  }
  return texts[status] || '未知'
}

const showAddUserDialog = () => {
  isAdd.value = true
  userDialogVisible.value = true
  resetForm()
}

const editUser = (user) => {
  isAdd.value = false
  userDialogVisible.value = true
  userForm.value = { ...user }
}

const deleteUser = async (user) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户 "${user.realName}" 吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    users.value = users.value.filter(u => u.userId !== user.userId)
    ElMessage.success('删除成功')
  } catch {
    ElMessage.error('删除操作取消')
  }
}

const submitForm = async () => {
  if (!userFormRef.value) return
  
  try {
    await userFormRef.value.validate()
    
    if (isAdd.value) {
      const newUser = {
        userId: 'U' + Date.now(),
        ...userForm.value,
        createdAt: new Date().toLocaleString()
      }
      users.value.unshift(newUser)
      ElMessage.success('新增用户成功')
    } else {
      const index = users.value.findIndex(u => u.userId === userForm.value.userId)
      if (index !== -1) {
        users.value[index] = { ...users.value[index], ...userForm.value }
        ElMessage.success('更新用户成功')
      }
    }
    
    userDialogVisible.value = false
    resetForm()
  } catch (error) {
    ElMessage.error('表单验证失败')
  }
}

const resetForm = () => {
  userForm.value = {
    userId: '',
    username: '',
    realName: '',
    phone: '',
    email: '',
    role: '',
    department: '',
    status: 'active',
    password: ''
  }
}

const searchUsers = () => {
  currentPage.value = 1
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
}

const handleCurrentChange = (page) => {
  currentPage.value = page
}

onMounted(() => {
})
</script>

<style scoped lang="scss">
.user-management {
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
</style>