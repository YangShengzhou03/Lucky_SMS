<template>
  <div class="grade-management">
    <div class="search-filter">
      <el-input
        v-model="searchQuery"
        placeholder="搜索学生姓名/学号"
        clearable
        style="width: 300px; margin-right: 16px;"
      >
        <template #prefix>
          <el-icon><Search /></el-icon>
        </template>
      </el-input>
      
      <el-button type="primary" @click="searchGrades">搜索</el-button>
      
      <el-button type="primary" @click="showImportDialog">
        <el-icon><Upload /></el-icon>
        导入成绩
      </el-button>
    </div>

    <el-table
      :data="filteredGrades"
      stripe
      v-loading="loading"
      style="width: 100%; margin-top: 20px;"
    >
      <el-table-column type="selection" width="55" />
      <el-table-column prop="studentId" label="学号" width="120" />
      <el-table-column prop="studentName" label="学生姓名" width="100" />
      <el-table-column prop="courseName" label="课程名称" width="200" />
      <el-table-column prop="teacherName" label="授课教师" width="100" />
      <el-table-column prop="department" label="所属院系" width="150" />
      <el-table-column prop="credit" label="学分" width="80" align="center" />
      <el-table-column label="平时成绩" width="100" align="center">
        <template #default="{ row }">
          <span :class="{ 'highlight': row.regularScore < 60 }">{{ row.regularScore || '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="期中成绩" width="100" align="center">
        <template #default="{ row }">
          <span :class="{ 'highlight': row.midtermScore < 60 }">{{ row.midtermScore || '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="期末成绩" width="100" align="center">
        <template #default="{ row }">
          <span :class="{ 'highlight': row.finalScore < 60 }">{{ row.finalScore || '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="总成绩" width="100" align="center">
        <template #default="{ row }">
          <span :class="getGradeClass(row.totalScore)">{{ row.totalScore || '--' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="等级" width="80" align="center">
        <template #default="{ row }">
          <el-tag :type="getGradeTagType(row.gradeLevel)">{{ row.gradeLevel || '--' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" width="80">
        <template #default="{ row }">
          <el-tag :type="getStatusTag(row.status)">{{ getStatusText(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="examDate" label="考试时间" width="120" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="editGrade(row)">编辑</el-button>
          <el-button size="small" type="danger" @click="deleteGrade(row)">删除</el-button>
          <el-dropdown @command="handleCommand($event, row)">
            <el-button size="small">
              更多<el-icon class="el-icon--right"><arrow-down /></el-icon>
            </el-button>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="viewDetails">查看详情</el-dropdown-item>
                <el-dropdown-item command="printReport" divided>打印成绩单</el-dropdown-item>
                <el-dropdown-item command="recalculate">重新计算</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-container">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 50, 100]"
        :total="totalFilteredGrades"
        layout="total, sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <el-dialog
      v-model="gradeDialogVisible"
      title="编辑成绩"
      width="600px"
      @close="resetForm"
    >
      <el-form :model="gradeForm" :rules="rules" ref="gradeFormRef" label-width="100px">
        <el-form-item label="学生姓名">
          <el-input v-model="gradeForm.studentName" disabled />
        </el-form-item>
        <el-form-item label="课程名称">
          <el-input v-model="gradeForm.courseName" disabled />
        </el-form-item>
        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="平时成绩" prop="regularScore">
              <el-input-number
                v-model="gradeForm.regularScore"
                :min="0"
                :max="100"
                :precision="1"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="期中成绩" prop="midtermScore">
              <el-input-number
                v-model="gradeForm.midtermScore"
                :min="0"
                :max="100"
                :precision="1"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="期末成绩" prop="finalScore">
              <el-input-number
                v-model="gradeForm.finalScore"
                :min="0"
                :max="100"
                :precision="1"
                style="width: 100%;"
              />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="总成绩">
          <el-input v-model="gradeForm.totalScore" disabled />
        </el-form-item>
        <el-form-item label="等级">
          <el-input v-model="gradeForm.gradeLevel" disabled />
        </el-form-item>
        <el-form-item label="考试时间" prop="examDate">
          <el-date-picker
            v-model="gradeForm.examDate"
            type="datetime"
            placeholder="选择考试时间"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="gradeForm.remark"
            type="textarea"
            :rows="2"
            placeholder="请输入备注信息"
          />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="gradeDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitForm">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="importDialogVisible"
      title="导入成绩"
      width="500px"
    >
      <div class="import-content">
        <el-upload
          class="upload-demo"
          drag
          action="#"
          :before-upload="beforeUpload"
          :on-success="handleSuccess"
          :on-error="handleError"
          accept=".xlsx,.xls"
        >
          <el-icon class="el-icon--upload"><upload-filled /></el-icon>
          <div class="el-upload__text">
            将文件拖到此处，或<em>点击上传</em>
          </div>
          <template #tip>
            <div class="el-upload__tip">
              只能上传 xlsx/xls 文件，且不超过10MB
            </div>
          </template>
        </el-upload>
        
        <div class="template-download">
          <el-button type="text" @click="downloadTemplate">
            <el-icon><Download /></el-icon>
            下载导入模板
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Upload, Download, Search, ArrowDown, UploadFilled } from '@element-plus/icons-vue'

const loading = ref(false)
const searchQuery = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const gradeDialogVisible = ref(false)
const importDialogVisible = ref(false)
const gradeFormRef = ref()

const gradeForm = ref({
  gradeId: '',
  studentId: '',
  studentName: '',
  courseId: '',
  courseName: '',
  teacherName: '',
  department: '',
  credit: 0,
  regularScore: 0,
  midtermScore: 0,
  finalScore: 0,
  totalScore: 0,
  gradeLevel: '',
  examDate: '',
  remark: '',
  status: 'active'
})

const rules = {
  regularScore: [
    { type: 'number', min: 0, max: 100, message: '成绩必须在0-100之间', trigger: 'blur' }
  ],
  midtermScore: [
    { type: 'number', min: 0, max: 100, message: '成绩必须在0-100之间', trigger: 'blur' }
  ],
  finalScore: [
    { type: 'number', min: 0, max: 100, message: '成绩必须在0-100之间', trigger: 'blur' }
  ],
  examDate: [
    { required: true, message: '请选择考试时间', trigger: 'change' }
  ]
}

const statistics = ref({
  average: 0,
  highest: 0,
  lowest: 0,
  passRate: 0,
  excellentRate: 0
})

const grades = ref([
  {
    gradeId: 'G1001',
    studentId: 'S3001',
    studentName: '李四',
    courseId: 'C1001',
    courseName: '计算机基础',
    teacherName: '张明',
    department: '计算机学院',
    credit: 3,
    regularScore: 85,
    midtermScore: 78,
    finalScore: 82,
    totalScore: 81.5,
    gradeLevel: '良好',
    examDate: '2023-12-20 09:00',
    status: 'active'
  },
  {
    gradeId: 'G1002',
    studentId: 'S3002',
    studentName: '王五',
    courseId: 'C1001',
    courseName: '计算机基础',
    teacherName: '张明',
    department: '计算机学院',
    credit: 3,
    regularScore: 92,
    midtermScore: 88,
    finalScore: 95,
    totalScore: 92.5,
    gradeLevel: '优秀',
    examDate: '2023-12-20 09:00',
    status: 'active'
  },
  {
    gradeId: 'G1003',
    studentId: 'S3003',
    studentName: '赵六',
    courseId: 'C1002',
    courseName: '高等数学',
    teacherName: '李华',
    department: '数学学院',
    credit: 4,
    regularScore: 76,
    midtermScore: 72,
    finalScore: 68,
    totalScore: 70.5,
    gradeLevel: '及格',
    examDate: '2023-12-22 14:00',
    status: 'active'
  }
])

const filteredGrades = computed(() => {
  let filtered = grades.value
  
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(grade => 
      grade.studentName.toLowerCase().includes(query) ||
      grade.studentId.toLowerCase().includes(query)
    )
  }
  
  const start = (currentPage.value - 1) * pageSize.value
  const end = start + pageSize.value
  return filtered.slice(start, end)
})

const totalFilteredGrades = computed(() => {
  let filtered = grades.value
  
  if (searchQuery.value) {
    const query = searchQuery.value.toLowerCase()
    filtered = filtered.filter(grade => 
      grade.studentName.toLowerCase().includes(query) ||
      grade.studentId.toLowerCase().includes(query)
    )
  }
  
  return filtered.length
})

const getGradeClass = (score) => {
  if (!score) return ''
  if (score >= 90) return 'excellent'
  if (score >= 80) return 'good'
  if (score >= 60) return 'pass'
  return 'fail'
}

const getGradeTagType = (level) => {
  const types = {
    '优秀': 'success',
    '良好': 'info',
    '及格': 'warning',
    '不及格': 'danger'
  }
  return types[level] || 'info'
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
    inactive: '无效'
  }
  return texts[status] || '未知'
}

const calculateStatistics = () => {
  const validGrades = grades.value.filter(g => g.totalScore && g.status === 'active')
  if (validGrades.length === 0) {
    statistics.value = { average: 0, highest: 0, lowest: 0, passRate: 0, excellentRate: 0 }
    return
  }
  
  const scores = validGrades.map(g => g.totalScore)
  const passCount = validGrades.filter(g => g.totalScore >= 60).length
  const excellentCount = validGrades.filter(g => g.totalScore >= 90).length
  
  statistics.value = {
    average: (scores.reduce((a, b) => a + b, 0) / scores.length).toFixed(1),
    highest: Math.max(...scores),
    lowest: Math.min(...scores),
    passRate: ((passCount / validGrades.length) * 100).toFixed(1),
    excellentRate: ((excellentCount / validGrades.length) * 100).toFixed(1)
  }
}

const editGrade = (grade) => {
  gradeDialogVisible.value = true
  gradeForm.value = { ...grade }
}

const deleteGrade = async (grade) => {
  try {
    await ElMessageBox.confirm(
      `确定要删除学生 "${grade.studentName}" 的 "${grade.courseName}" 成绩吗？`,
      '确认删除',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )
    
    grades.value = grades.value.filter(g => g.gradeId !== grade.gradeId)
    calculateStatistics()
    ElMessage.success('删除成功')
  } catch {
    ElMessage.error('删除失败')
  }
}

const handleCommand = (command, grade) => {
  switch (command) {
    case 'viewDetails':
      viewGradeDetails(grade)
      break
    case 'printReport':
      printGradeReport(grade)
      break
    case 'recalculate':
      recalculateGrade(grade)
      break
  }
}

const viewGradeDetails = (grade) => {
  ElMessage.info(`查看成绩详情: ${grade.studentName} - ${grade.courseName}`)
}

const printGradeReport = (grade) => {
  ElMessage.success(`打印成绩单: ${grade.studentName} - ${grade.courseName}`)
}

const recalculateGrade = (grade) => {
  const regularWeight = 0.3
  const midtermWeight = 0.3
  const finalWeight = 0.4
  
  const totalScore = (grade.regularScore * regularWeight + 
                     grade.midtermScore * midtermWeight + 
                     grade.finalScore * finalWeight).toFixed(1)
  
  grade.totalScore = parseFloat(totalScore)
  grade.gradeLevel = calculateGradeLevel(grade.totalScore)
  
  ElMessage.success('成绩重新计算完成')
}

const calculateGradeLevel = (score) => {
  if (score >= 90) return '优秀'
  if (score >= 80) return '良好'
  if (score >= 70) return '中等'
  if (score >= 60) return '及格'
  return '不及格'
}

const submitForm = async () => {
  if (!gradeFormRef.value) return
  
  try {
    await gradeFormRef.value.validate()
    
    const regularWeight = 0.3
    const midtermWeight = 0.3
    const finalWeight = 0.4
    
    const totalScore = (gradeForm.value.regularScore * regularWeight + 
                       gradeForm.value.midtermScore * midtermWeight + 
                       gradeForm.value.finalScore * finalWeight).toFixed(1)
    
    gradeForm.value.totalScore = parseFloat(totalScore)
    gradeForm.value.gradeLevel = calculateGradeLevel(gradeForm.value.totalScore)
    
    const index = grades.value.findIndex(g => g.gradeId === gradeForm.value.gradeId)
    if (index !== -1) {
      grades.value[index] = { ...grades.value[index], ...gradeForm.value }
      calculateStatistics()
      ElMessage.success('更新成绩成功')
    }
    
    gradeDialogVisible.value = false
    resetForm()
  } catch (error) {
    ElMessage.error('表单验证失败')
  }
}

const resetForm = () => {
  gradeForm.value = {
    gradeId: '',
    studentId: '',
    studentName: '',
    courseId: '',
    courseName: '',
    teacherName: '',
    department: '',
    credit: 0,
    regularScore: 0,
    midtermScore: 0,
    finalScore: 0,
    totalScore: 0,
    gradeLevel: '',
    examDate: '',
    remark: '',
    status: 'active'
  }
}

const showImportDialog = () => {
  importDialogVisible.value = true
}

const beforeUpload = (file) => {
  const isExcel = file.type === 'application/vnd.ms-excel' || 
                 file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
  const isLt10M = file.size / 1024 / 1024 < 10

  if (!isExcel) {
    ElMessage.error('只能上传Excel文件!')
    return false
  }
  if (!isLt10M) {
    ElMessage.error('文件大小不能超过10MB!')
    return false
  }
  return true
}

const handleSuccess = () => {
  ElMessage.success('文件上传成功')
  importDialogVisible.value = false
}

const handleError = () => {
  ElMessage.error('文件上传失败')
}

const downloadTemplate = () => {
  ElMessage.success('模板下载成功')
}

const searchGrades = () => {
  currentPage.value = 1
}

const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
}

const handleCurrentChange = (page) => {
  currentPage.value = page
}

watch(grades, calculateStatistics, { deep: true })

onMounted(() => {
  calculateStatistics()
})
</script>

<style scoped lang="scss">
.grade-management {
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

.grade-statistics {
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

.highlight {
  color: #f56c6c;
  font-weight: bold;
}

.excellent {
  color: #67c23a;
  font-weight: bold;
}

.good {
  color: #409eff;
  font-weight: bold;
}

.pass {
  color: #e6a23c;
  font-weight: bold;
}

.fail {
  color: #f56c6c;
  font-weight: bold;
}

.import-content {
  text-align: center;
}

.template-download {
  margin-top: 20px;
}

.el-row {
  margin-bottom: 0;
}

.el-col {
  margin-bottom: 0;
}
</style>