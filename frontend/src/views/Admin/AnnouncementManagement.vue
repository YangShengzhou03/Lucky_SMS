<template>
  <div class="announcement-management">
    <div class="search-filter">
      <el-input
        v-model="searchQuery"
        placeholder="搜索标题/内容"
        clearable
        style="width: 300px; margin-right: 16px;"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      
      <el-select v-model="filterType" placeholder="公告类型" clearable style="width: 150px; margin-right: 16px;">
        <el-option label="通知公告" value="NOTICE" />
        <el-option label="校园招聘" value="RECRUITMENT" />
        <el-option label="学术讲座" value="LECTURE" />
        <el-option label="活动通知" value="EVENT" />
        <el-option label="紧急通知" value="URGENT" />
        <el-option label="教学安排" value="TEACHING" />
        <el-option label="成果申报" value="ACHIEVEMENT" />
        <el-option label="培训通知" value="TRAINING" />
        <el-option label="检查通知" value="INSPECTION" />
      </el-select>
      
      <el-select v-model="filterDepartment" placeholder="部门" clearable style="width: 150px; margin-right: 16px;">
        <el-option label="计算机学院" value="CS" />
        <el-option label="软件学院" value="SE" />
        <el-option label="信息学院" value="IS" />
        <el-option label="数学学院" value="MA" />
        <el-option label="外语学院" value="FL" />
        <el-option label="教务处" value="AA" />
        <el-option label="科研处" value="RA" />
        <el-option label="教师发展中心" value="TDC" />
        <el-option label="教学质量监控中心" value="QMC" />
        <el-option label="就业指导中心" value="EC" />
      </el-select>
      
      <el-button type="primary" @click="searchAnnouncements">搜索</el-button>
      
      <el-button type="primary" @click="showAddAnnouncementDialog">
        <el-icon><Plus /></el-icon>
        新增公告
      </el-button>
    </div>

    <el-table
      :data="filteredAnnouncements"
      stripe
      v-loading="loading"
      style="width: 100%; margin-top: 20px;"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="announcementId" label="ID" width="80" />
      <el-table-column prop="title" label="标题" width="200" show-overflow-tooltip />
      <el-table-column prop="content" label="内容" width="300" show-overflow-tooltip />
      <el-table-column prop="announcementTypeName" label="类型" width="120" />
      <el-table-column prop="departmentName" label="发布部门" width="120" />
      <el-table-column label="优先级" width="100">
        <template #default="{ row }">
          <el-tag :type="getPriorityType(row.priority)">{{ getPriorityText(row.priority) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="受众" width="100">
        <template #default="{ row }">
          <el-tag>{{ getAudienceText(row.targetAudience) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="getStatusType(row.statusName)">{{ row.statusName }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="publishTime" label="发布时间" width="160" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="editAnnouncement(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteAnnouncement(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="totalAnnouncements"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <el-dialog
      v-model="announcementDialogVisible"
      :title="dialogTitle"
      width="700px"
      @close="resetForm"
    >
      <el-form :model="announcementForm" :rules="rules" ref="announcementFormRef" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="announcementForm.title" placeholder="请输入公告标题" />
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <el-input
            v-model="announcementForm.content"
            type="textarea"
            :rows="6"
            placeholder="请输入公告内容"
          />
        </el-form-item>
        <el-form-item label="公告类型" prop="announcementTypeId">
          <el-select v-model="announcementForm.announcementTypeId" placeholder="请选择公告类型" style="width: 100%;">
            <el-option label="通知公告" :value="1" />
            <el-option label="校园招聘" :value="2" />
            <el-option label="学术讲座" :value="3" />
            <el-option label="活动通知" :value="4" />
            <el-option label="紧急通知" :value="5" />
            <el-option label="教学安排" :value="6" />
            <el-option label="成果申报" :value="7" />
            <el-option label="培训通知" :value="8" />
            <el-option label="检查通知" :value="9" />
          </el-select>
        </el-form-item>
        <el-form-item label="发布部门" prop="departmentId">
          <el-select v-model="announcementForm.departmentId" placeholder="请选择发布部门" style="width: 100%;">
            <el-option label="计算机学院" :value="1" />
            <el-option label="软件学院" :value="2" />
            <el-option label="信息学院" :value="3" />
            <el-option label="数学学院" :value="4" />
            <el-option label="外语学院" :value="5" />
            <el-option label="教务处" :value="6" />
            <el-option label="科研处" :value="7" />
            <el-option label="教师发展中心" :value="8" />
            <el-option label="教学质量监控中心" :value="9" />
            <el-option label="就业指导中心" :value="10" />
          </el-select>
        </el-form-item>
        <el-form-item label="优先级" prop="priority">
          <el-radio-group v-model="announcementForm.priority">
            <el-radio :label="0">普通</el-radio>
            <el-radio :label="1">重要</el-radio>
            <el-radio :label="2">紧急</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="受众" prop="targetAudience">
          <el-radio-group v-model="announcementForm.targetAudience">
            <el-radio label="ALL">全体</el-radio>
            <el-radio label="STUDENT">学生</el-radio>
            <el-radio label="TEACHER">教师</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="announcementDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import { getAnnouncementList, createAnnouncement, updateAnnouncement, deleteAnnouncement } from '@/api/admin'

const loading = ref(false)
const searchQuery = ref('')
const filterType = ref('')
const filterDepartment = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const totalAnnouncements = ref(0)
const announcements = ref([])

const announcementDialogVisible = ref(false)
const dialogTitle = ref('新增公告')
const announcementFormRef = ref(null)
const isEdit = ref(false)
const currentAnnouncementId = ref(null)

const announcementForm = ref({
  title: '',
  content: '',
  announcementTypeId: 1,
  departmentId: null,
  priority: 0,
  targetAudience: 'ALL'
})

const rules = {
  title: [{ required: true, message: '请输入公告标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入公告内容', trigger: 'blur' }],
  announcementTypeId: [{ required: true, message: '请选择公告类型', trigger: 'change' }]
}

const filteredAnnouncements = computed(() => {
  return announcements.value
})

const getPriorityType = (priority) => {
  const types = { 0: '', 1: 'warning', 2: 'danger' }
  return types[priority] || ''
}

const getPriorityText = (priority) => {
  const texts = { 0: '普通', 1: '重要', 2: '紧急' }
  return texts[priority] || '普通'
}

const getAudienceText = (audience) => {
  const texts = { ALL: '全体', STUDENT: '学生', TEACHER: '教师' }
  return texts[audience] || '全体'
}

const getStatusType = (status) => {
  const types = { '正常': 'success', '禁用': 'danger', '已删除': 'info' }
  return types[status] || ''
}

const loadAnnouncements = async () => {
  loading.value = true
  try {
    const params = {
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchQuery.value || undefined,
      type: filterType.value || undefined,
      department: filterDepartment.value || undefined
    }
    const response = await getAnnouncementList(params)
    if (response.code === 200) {
      announcements.value = response.data.list || []
      totalAnnouncements.value = response.data.total || 0
    }
  } catch (error) {
    ElMessage.error('加载公告列表失败')
  } finally {
    loading.value = false
  }
}

const searchAnnouncements = () => {
  currentPage.value = 1
  loadAnnouncements()
}

const showAddAnnouncementDialog = () => {
  isEdit.value = false
  dialogTitle.value = '新增公告'
  resetForm()
  announcementDialogVisible.value = true
}

const editAnnouncement = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑公告'
  currentAnnouncementId.value = row.announcementId
  announcementForm.value = {
    title: row.title,
    content: row.content,
    announcementTypeId: row.announcementTypeId,
    departmentId: row.departmentId,
    priority: row.priority,
    targetAudience: row.targetAudience
  }
  announcementDialogVisible.value = true
}

const submitForm = async () => {
  if (!announcementFormRef.value) return
  
  await announcementFormRef.value.validate(async (valid) => {
    if (!valid) return
    
    try {
      const data = { ...announcementForm.value }
      
      if (isEdit.value) {
        data.announcementId = currentAnnouncementId.value
        const response = await updateAnnouncement(data)
        if (response.code === 200) {
          ElMessage.success('更新成功')
          announcementDialogVisible.value = false
          loadAnnouncements()
        } else {
          ElMessage.error(response.message || '更新失败')
        }
      } else {
        data.publisherId = 1
        const response = await createAnnouncement(data)
        if (response.code === 200) {
          ElMessage.success('创建成功')
          announcementDialogVisible.value = false
          loadAnnouncements()
        } else {
          ElMessage.error(response.message || '创建失败')
        }
      }
    } catch (error) {
      ElMessage.error(isEdit.value ? '更新失败' : '创建失败')
    }
  })
}

const resetForm = () => {
  if (announcementFormRef.value) {
    announcementFormRef.value.resetFields()
  }
  announcementForm.value = {
    title: '',
    content: '',
    announcementTypeId: 1,
    departmentId: null,
    priority: 0,
    targetAudience: 'ALL'
  }
}

const handleSizeChange = (val) => {
  pageSize.value = val
  loadAnnouncements()
}

const handleCurrentChange = (val) => {
  currentPage.value = val
  loadAnnouncements()
}

onMounted(() => {
  loadAnnouncements()
})
</script>

<style scoped>
.announcement-management {
  padding: 20px;
}

.search-filter {
  display: flex;
  align-items: center;
  margin-bottom: 20px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}
</style>
