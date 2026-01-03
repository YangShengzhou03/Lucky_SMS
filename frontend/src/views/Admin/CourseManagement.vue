<template>
  <div class="course-management">
    <div class="search-filter">
      <el-input v-model="searchQuery" placeholder="搜索课程名称/课程代码" clearable style="width: 300px; margin-right: 16px;">
        <template #prefix>
          <el-icon>
            <Search />
          </el-icon>
        </template>
      </el-input>

      <el-button type="primary" @click="searchCourses">搜索</el-button>
      
      <el-button type="primary" @click="showAddCourseDialog">
        <el-icon>
          <Plus />
        </el-icon>
        新增课程
      </el-button>
    </div>

    <el-table :data="courses" stripe v-loading="loading" style="width: 100%; margin-top: 20px;">
      <el-table-column type="selection" width="55" />
      <el-table-column prop="courseCode" label="课程代码" width="120" />
      <el-table-column prop="courseName" label="课程名称" width="200" />
      <el-table-column prop="teacherName" label="授课教师" width="120" />
      <el-table-column prop="department" label="所属院系" width="150" />
      <el-table-column prop="credit" label="学分" width="80" align="center" />
      <el-table-column prop="hours" label="学时" width="80" align="center" />
      <el-table-column label="课程类型" width="100">
        <template #default="{ row }">
          <el-tag :type="getCourseTypeTag(row.courseType)">{{ row.courseType }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="课程状态" width="100">
        <template #default="{ row }">
          <el-tag :type="getStatusTag(row.status)">{{ getStatusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="studentCount" label="选课人数" width="100" align="center" />
      <el-table-column prop="startDate" label="开始时间" width="120" />
      <el-table-column prop="endDate" label="结束时间" width="120" />
      <el-table-column label="操作" width="150" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="editCourse(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteCourse(row)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination v-model:current-page="currentPage" v-model:page-size="pageSize" :page-sizes="[10, 20, 50, 100]"
        :total="total" layout="total, sizes, prev, pager, next, jumper" @size-change="handleSizeChange"
        @current-change="handleCurrentChange" />
    </div>

    <el-dialog v-model="courseDialogVisible" :title="dialogTitle" width="700px" @close="resetForm">
      <el-form :model="courseForm" :rules="rules" ref="courseFormRef" label-width="100px">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="课程代码" prop="courseCode">
              <el-input v-model="courseForm.courseCode" placeholder="请输入课程代码" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="课程名称" prop="courseName">
              <el-input v-model="courseForm.courseName" placeholder="请输入课程名称" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="授课教师" prop="teacherId">
              <el-select v-model="courseForm.teacherId" placeholder="请选择授课教师" style="width: 100%;">
                <el-option v-for="teacher in teachers" :key="teacher.userId" :label="teacher.realName"
                  :value="teacher.userId" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="所属院系" prop="department">
              <el-select v-model="courseForm.department" placeholder="请选择所属院系" style="width: 100%;">
                <el-option label="计算机学院" value="计算机学院" />
                <el-option label="数学学院" value="数学学院" />
                <el-option label="物理学院" value="物理学院" />
                <el-option label="化学学院" value="化学学院" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="学分" prop="credit">
              <el-input-number v-model="courseForm.credit" :min="1" :max="10" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="学时" prop="hours">
              <el-input-number v-model="courseForm.hours" :min="16" :max="128" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="课程类型" prop="courseType">
              <el-select v-model="courseForm.courseType" placeholder="请选择课程类型" style="width: 100%;">
                <el-option label="必修课" value="必修课" />
                <el-option label="选修课" value="选修课" />
                <el-option label="实践课" value="实践课" />
                <el-option label="实验课" value="实验课" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="开始时间" prop="startDate">
              <el-date-picker v-model="courseForm.startDate" type="date" placeholder="选择开始时间" style="width: 100%;" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结束时间" prop="endDate">
              <el-date-picker v-model="courseForm.endDate" type="date" placeholder="选择结束时间" style="width: 100%;" />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="课程描述" prop="description">
          <el-input v-model="courseForm.description" type="textarea" :rows="3" placeholder="请输入课程描述" />
        </el-form-item>

        <el-form-item label="课程状态" prop="status">
          <el-radio-group v-model="courseForm.status">
            <el-radio label="pending">未开始</el-radio>
            <el-radio label="active">进行中</el-radio>
            <el-radio label="completed">已结束</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>

      <template #footer>
        <el-button @click="courseDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Search } from '@element-plus/icons-vue'
import { getCourseList, addCourse, updateCourse, deleteCourse as apiDeleteCourse, getTeacherList } from '@/api/admin'

const loading = ref(false)
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const courseDialogVisible = ref(false)
const isAdd = ref(false)
const courseFormRef = ref()

const courseForm = ref({
  courseId: '',
  courseCode: '',
  courseName: '',
  teacherId: '',
  teacherName: '',
  department: '',
  credit: 2,
  hours: 32,
  courseType: '',
  startDate: '',
  endDate: '',
  description: '',
  status: 'pending',
  studentCount: 0
})

const rules = {
  courseCode: [
    { required: true, message: '请输入课程代码', trigger: 'blur' }
  ],
  courseName: [
    { required: true, message: '请输入课程名称', trigger: 'blur' }
  ],
  teacherId: [
    { required: true, message: '请选择授课教师', trigger: 'change' }
  ],
  department: [
    { required: true, message: '请选择所属院系', trigger: 'change' }
  ],
  credit: [
    { required: true, message: '请输入学分', trigger: 'blur' }
  ],
  hours: [
    { required: true, message: '请输入学时', trigger: 'blur' }
  ],
  courseType: [
    { required: true, message: '请选择课程类型', trigger: 'change' }
  ],
  startDate: [
    { required: true, message: '请选择开始时间', trigger: 'change' }
  ],
  endDate: [
    { required: true, message: '请选择结束时间', trigger: 'change' }
  ]
}

const teachers = ref([])
const courses = ref([])

const dialogTitle = computed(() => {
  return isAdd.value ? '新增课程' : '编辑课程'
})

const getCourseTypeTag = (type) => {
  const types = {
    '必修课': 'danger',
    '选修课': 'warning',
    '实践课': 'success',
    '实验课': 'info'
  }
  return types[type] || 'info'
}

const getStatusTag = (status) => {
  const types = {
    active: 'success',
    completed: 'info',
    pending: 'warning'
  }
  return types[status] || 'info'
}

const getStatusText = (status) => {
  const texts = {
    active: '进行中',
    completed: '已结束',
    pending: '未开始'
  }
  return texts[status] || '未知'
}

const showAddCourseDialog = () => {
  isAdd.value = true
  courseDialogVisible.value = true
  resetForm()
}

const editCourse = (course) => {
  isAdd.value = false
  courseDialogVisible.value = true
  courseForm.value = { ...course }
}

const deleteCourse = async (course) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除课程 "${course.courseName}" 吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    loading.value = true
    const res = await apiDeleteCourse(course.courseId)
    
    if (res.code === 200) {
      ElMessage.success('删除成功')
      await loadCourses()
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
  if (!courseFormRef.value) return

  try {
    await courseFormRef.value.validate()
    loading.value = true

    const teacher = teachers.value.find(t => t.userId === courseForm.value.teacherId)
    if (teacher) {
      courseForm.value.teacherName = teacher.realName
    }

    if (isAdd.value) {
      const res = await addCourse(courseForm.value)
      if (res.code === 200) {
        ElMessage.success('新增课程成功')
        courseDialogVisible.value = false
        resetForm()
        await loadCourses()
      } else {
        ElMessage.error(res.message || '新增失败')
      }
    } else {
      const res = await updateCourse(courseForm.value)
      if (res.code === 200) {
        ElMessage.success('更新课程成功')
        courseDialogVisible.value = false
        resetForm()
        await loadCourses()
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
  courseForm.value = {
    courseId: '',
    courseCode: '',
    courseName: '',
    teacherId: '',
    teacherName: '',
    department: '',
    credit: 2,
    hours: 32,
    courseType: '',
    startDate: '',
    endDate: '',
    description: '',
    status: 'pending',
    studentCount: 0
  }
}

const searchCourses = () => {
  currentPage.value = 1
  loadCourses()
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  loadCourses()
}

const handleCurrentChange = (page) => {
  currentPage.value = page
  loadCourses()
}

const loadCourses = async () => {
  loading.value = true
  try {
    const res = await getCourseList({
      page: currentPage.value,
      size: pageSize.value,
      keyword: searchQuery.value
    })
    
    if (res.code === 200) {
      courses.value = res.data.list || []
      total.value = res.data.total || 0
    } else {
      ElMessage.error(res.message || '获取课程列表失败')
    }
  } catch (error) {
    ElMessage.error('获取课程列表失败')
    console.error('获取课程列表失败:', error)
  } finally {
    loading.value = false
  }
}

const loadTeachers = async () => {
  try {
    const res = await getTeacherList()
    if (res.code === 200) {
      teachers.value = res.data || []
    }
  } catch (error) {
    console.error('获取教师列表失败:', error)
  }
}

onMounted(() => {
  loadCourses()
  loadTeachers()
})
</script>

<style scoped lang="scss">
.course-management {
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

.el-row {
  margin-bottom: 0;
}

.el-col {
  margin-bottom: 0;
}
</style>
