<template>
  <div class="teaching-plan-dashboard" :class="{ 'dark': isDarkMode }" @mousemove="handleMouseMove">
    <!-- 教学计划概览卡片 -->
    <div class="plan-overview-card modern-card" ref="overviewRef">
      <div class="card-header">
        <h2>
          <el-icon>
            <Notebook />
          </el-icon>
          教学计划概览
        </h2>
        <div class="semester-selector">
          <el-select v-model="currentSemester" placeholder="选择学期" size="small" class="modern-select">
            <el-option v-for="item in semesters" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </div>
      </div>

      <div class="plan-stats">
        <div class="stats-grid">
          <div class="stat-card" v-for="(stat, index) in stats" :key="index" :style="{ '--card-color': stat.color }">
            <div class="stat-icon">
              <div class="icon-bg"></div>
              <el-icon :size="24">
                <component :is="stat.icon" />
              </el-icon>
            </div>
            <div class="stat-content">
              <div class="stat-value">
                <animated-number :value="stat.value" :formatValue="formatStatValue" :duration="800" />
              </div>
              <div class="stat-label">{{ stat.label }}</div>
            </div>
            <div class="stat-trend" v-if="stat.trend">
              <el-icon :color="stat.trend > 0 ? '#f56c6c' : '#67c23a'">
                <CaretTop v-if="stat.trend > 0" />
                <CaretBottom v-else />
              </el-icon>
              <span :style="{ color: stat.trend > 0 ? '#f56c6c' : '#67c23a' }">
                {{ Math.abs(stat.trend) }}%
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 教学计划内容区域 -->
    <div class="teaching-plan-content">
      <!-- 当前教学计划 -->
      <div class="current-plans modern-card" ref="currentPlansRef">
        <div class="card-header">
          <h3>
            <el-icon>
              <Calendar />
            </el-icon>
            当前教学计划
          </h3>
          <div class="plan-actions">
            <el-button type="primary" size="small" @click="createNewPlan">
              <el-icon>
                <Plus />
              </el-icon>
              创建计划
            </el-button>
            <el-button type="text" size="small" @click="importPlan">
              <el-icon>
                <Upload />
              </el-icon>
              导入计划
            </el-button>
          </div>
        </div>

        <el-tabs v-model="activeTab" type="border-card" class="plan-tabs">
          <el-tab-pane label="进行中" name="ongoing">
            <el-table :data="ongoingPlans" border stripe class="plan-table">
              <el-table-column prop="courseName" label="课程名称" min-width="180" />
              <el-table-column prop="teacher" label="授课教师" width="120" />
              <el-table-column prop="startDate" label="开始日期" width="120" />
              <el-table-column prop="endDate" label="结束日期" width="120" />
              <el-table-column prop="progress" label="完成进度" width="140">
                <template #default="scope">
                  <el-progress :percentage="scope.row.progress" :color="planProgressColor(scope.row.progress)"
                    :stroke-width="12" />
                </template>
              </el-table-column>
              <el-table-column label="操作" width="180">
                <template #default="scope">
                  <el-button type="text" size="small" @click="viewPlanDetails(scope.row)">
                    查看详情
                  </el-button>
                  <el-button type="text" size="small" @click="editPlan(scope.row)">
                    编辑
                  </el-button>
                  <el-button type="text" size="small" @click="deletePlan(scope.row)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="empty-state" v-if="ongoingPlans.length === 0">
              <el-empty description="暂无进行中的教学计划"></el-empty>
            </div>
          </el-tab-pane>
          <el-tab-pane label="已完成" name="completed">
            <el-table :data="completedPlans" border stripe class="plan-table">
              <el-table-column prop="courseName" label="课程名称" min-width="180" />
              <el-table-column prop="teacher" label="授课教师" width="120" />
              <el-table-column prop="startDate" label="开始日期" width="120" />
              <el-table-column prop="endDate" label="结束日期" width="120" />
              <el-table-column prop="completionDate" label="完成日期" width="120" />
              <el-table-column label="操作" width="120">
                <template #default="scope">
                  <el-button type="text" size="small" @click="viewPlanDetails(scope.row)">
                    查看详情
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="empty-state" v-if="completedPlans.length === 0">
              <el-empty description="暂无已完成的教学计划"></el-empty>
            </div>
          </el-tab-pane>
          <el-tab-pane label="草稿" name="draft">
            <el-table :data="draftPlans" border stripe class="plan-table">
              <el-table-column prop="courseName" label="课程名称" min-width="180" />
              <el-table-column prop="teacher" label="授课教师" width="120" />
              <el-table-column prop="lastUpdated" label="最后更新" width="120" />
              <el-table-column label="操作" width="180">
                <template #default="scope">
                  <el-button type="text" size="small" @click="viewPlanDetails(scope.row)">
                    查看详情
                  </el-button>
                  <el-button type="text" size="small" @click="editPlan(scope.row)">
                    编辑
                  </el-button>
                  <el-button type="text" size="small" @click="deletePlan(scope.row)">
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
            <div class="empty-state" v-if="draftPlans.length === 0">
              <el-empty description="暂无草稿状态的教学计划"></el-empty>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>

      <!-- 教学计划详情 -->
      <div class="plan-details modern-card" v-if="selectedPlan" ref="detailsRef">
        <div class="card-header">
          <h3>
            <el-icon>
              <Document />
            </el-icon>
            {{ selectedPlan.courseName }} - 教学计划详情
          </h3>
          <div class="plan-actions">
            <el-button type="primary" size="small" @click="editPlan(selectedPlan)">
              <el-icon>
                <Edit />
              </el-icon>
              编辑计划
            </el-button>
            <el-button type="text" size="small" @click="exportPlan(selectedPlan)">
              <el-icon>
                <Download />
              </el-icon>
              导出计划
            </el-button>
          </div>
        </div>

        <div class="plan-summary">
          <div class="summary-item">
            <span class="label">课程代码:</span>
            <span class="value">{{ selectedPlan.courseCode }}</span>
          </div>
          <div class="summary-item">
            <span class="label">授课教师:</span>
            <span class="value">{{ selectedPlan.teacher }}</span>
          </div>
          <div class="summary-item">
            <span class="label">学分:</span>
            <span class="value">{{ selectedPlan.credits }}</span>
          </div>
          <div class="summary-item">
            <span class="label">计划周期:</span>
            <span class="value">{{ selectedPlan.startDate }} - {{ selectedPlan.endDate }}</span>
          </div>
          <div class="summary-item">
            <span class="label">当前进度:</span>
            <span class="value">
              <el-progress :percentage="selectedPlan.progress" :color="planProgressColor(selectedPlan.progress)"
                :stroke-width="16" class="inline-progress" />
              {{ selectedPlan.progress }}%
            </span>
          </div>
        </div>

        <el-tabs type="card" class="plan-detail-tabs">
          <el-tab-pane label="课程目标">
            <div class="plan-section">
              <h4>课程目标</h4>
              <div class="goal-item" v-for="(goal, index) in selectedPlan.goals" :key="index">
                <el-badge :value="index + 1" type="primary" class="goal-badge"></el-badge>
                <p>{{ goal.description }}</p>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="教学内容">
            <div class="plan-section">
              <h4>教学内容安排</h4>
              <el-table :data="selectedPlan.lessons" border class="lesson-table">
                <el-table-column prop="week" label="周次" width="80" />
                <el-table-column prop="topic" label="主题" min-width="200" />
                <el-table-column prop="content" label="教学内容" min-width="300" />
                <el-table-column prop="teachingMethod" label="教学方法" width="120" />
                <el-table-column prop="learningOutcomes" label="学习成果" min-width="200" />
              </el-table>
            </div>
          </el-tab-pane>
          <el-tab-pane label="评估方式">
            <div class="plan-section">
              <h4>评估方式与权重</h4>
              <el-table :data="selectedPlan.assessments" border class="assessment-table">
                <el-table-column prop="type" label="评估类型" width="120" />
                <el-table-column prop="description" label="描述" min-width="200" />
                <el-table-column prop="weight" label="权重" width="100">
                  <template #default="scope">
                    {{ scope.row.weight }}%
                  </template>
                </el-table-column>
                <el-table-column prop="date" label="日期" width="120" />
              </el-table>
            </div>
          </el-tab-pane>
          <el-tab-pane label="参考资料">
            <div class="plan-section">
              <h4>参考资料</h4>
              <ul class="resource-list">
                <li v-for="(resource, index) in selectedPlan.resources" :key="index">
                  <el-icon>
                    <Document />
                  </el-icon>
                  <span>{{ resource.title }}</span>
                  <span class="resource-author"> {{ resource.author }}</span>
                  <span class="resource-publisher"> ({{ resource.publisher }}, {{ resource.year }})</span>
                </li>
              </ul>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <!-- 历史教学计划 -->
    <div class="history-plans modern-card" ref="historyRef">
      <div class="card-header">
        <h3>
          <el-icon>
            <Timer />
          </el-icon>
          历史教学计划
        </h3>
        <div class="history-filter">
          <el-input v-model="historySearch" placeholder="搜索计划" size="small" clearable>
            <template #suffix>
              <el-icon>
                <Search />
              </el-icon>
            </template>
          </el-input>
        </div>
      </div>

      <el-table :data="filteredHistoryPlans" border stripe class="history-table">
        <el-table-column prop="semester" label="学期" width="120" />
        <el-table-column prop="courseName" label="课程名称" min-width="180" />
        <el-table-column prop="courseCode" label="课程代码" width="120" />
        <el-table-column prop="teacher" label="授课教师" width="120" />
        <el-table-column prop="completionDate" label="完成日期" width="120" />
        <el-table-column label="操作" width="120">
          <template #default="scope">
            <el-button type="text" size="small" @click="viewPlanDetails(scope.row)">
              查看详情
            </el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination" v-if="filteredHistoryPlans.length > 0">
        <el-pagination @size-change="handleSizeChange" @current-change="handleCurrentChange" :current-page="currentPage"
          :page-sizes="[5, 10, 20]" :page-size="pageSize" :total="filteredHistoryPlans.length"
          layout="total, sizes, prev, pager, next" small />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import {
  Notebook,
  Calendar,
  Document,
  Timer,
  Search,
  Plus,
  Edit,
  Upload,
  Download,
  CaretTop,
  CaretBottom,
  Check,
  Star
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

const isDarkMode = ref(false)

const currentSemester = ref('2023-2024-2')
const semesters = ref([
  { value: '2023-2024-2', label: '2023-2024学年第二学期' },
  { value: '2023-2024-1', label: '2023-2024学年第一学期' },
  { value: '2022-2023-2', label: '2022-2023学年第二学期' },
  { value: '2022-2023-1', label: '2022-2023学年第一学期' }
])

const activeTab = ref('ongoing')
const selectedPlan = ref(null)
const currentPage = ref(1)
const pageSize = ref(10)
const historySearch = ref('')


const teachingPlans = ref([
  {
    id: 1,
    courseName: '数据结构与算法',
    courseCode: 'CS101',
    teacher: '李教授',
    credits: 4,
    startDate: '2023-09-01',
    endDate: '2024-01-15',
    progress: 65,
    status: 'ongoing',
    goals: [
      { description: '掌握常见数据结构如数组、链表、栈、队列、树和图的基本概念和操作' },
      { description: '理解算法设计的基本方法，包括分治法、动态规划和贪心算法' },
      { description: '能够分析算法的时间复杂度和空间复杂度' },
      { description: '能够应用数据结构和算法解决实际问题' }
    ],
    lessons: [
      { week: 1, topic: '课程介绍与算法分析', content: '课程概述、算法效率分析、大O表示法', teachingMethod: '讲授', learningOutcomes: '理解算法分析的基本概念' },
      { week: 2, topic: '线性数据结构', content: '数组、链表、栈和队列的实现与应用', teachingMethod: '讲授+实验', learningOutcomes: '能够实现和应用基本线性数据结构' },
      { week: 3, topic: '递归与分治法', content: '递归算法设计、分治法策略', teachingMethod: '讲授+讨论', learningOutcomes: '掌握递归和分治法的算法设计' },
      { week: 4, topic: '树与二叉树', content: '树的基本概念、二叉树的遍历和实现', teachingMethod: '讲授+实验', learningOutcomes: '理解树结构和二叉树操作' },
      { week: 5, topic: '二叉搜索树', content: '二叉搜索树的插入、删除和查找操作', teachingMethod: '讲授+实验', learningOutcomes: '能够实现和应用二叉搜索树' },
      { week: 6, topic: '堆和优先队列', content: '堆的概念、最大堆和最小堆的实现', teachingMethod: '讲授+讨论', learningOutcomes: '理解堆结构和优先队列的应用' },
      { week: 7, topic: '图结构', content: '图的表示、遍历算法', teachingMethod: '讲授+实验', learningOutcomes: '掌握图的基本概念和遍历方法' },
      { week: 8, topic: '期中考试复习', content: '前半学期内容总结与复习', teachingMethod: '讨论+答疑', learningOutcomes: '巩固前半学期所学知识' },
      { week: 9, topic: '期中考试', content: '数据结构与算法期中考试', teachingMethod: '考试', learningOutcomes: '评估前半学期学习成果' },
      { week: 10, topic: '排序算法', content: '常见排序算法：冒泡排序、选择排序、插入排序、快速排序', teachingMethod: '讲授+实验', learningOutcomes: '理解各种排序算法的原理和性能' },
      { week: 11, topic: '高级排序算法', content: '归并排序、堆排序、基数排序', teachingMethod: '讲授+讨论', learningOutcomes: '掌握高级排序算法的实现和应用' },
      { week: 12, topic: '查找算法', content: '哈希表、散列函数、冲突解决', teachingMethod: '讲授+实验', learningOutcomes: '理解哈希表的原理和应用' },
      { week: 13, topic: '动态规划', content: '动态规划的基本概念和应用', teachingMethod: '讲授+案例分析', learningOutcomes: '掌握动态规划的算法设计' },
      { week: 14, topic: '贪心算法', content: '贪心算法的基本概念和应用', teachingMethod: '讲授+案例分析', learningOutcomes: '理解贪心算法的适用场景' },
      { week: 15, topic: '课程总结与项目展示', content: '课程内容总结、学生项目展示', teachingMethod: '讨论+展示', learningOutcomes: '巩固全学期所学知识' },
      { week: 16, topic: '期末考试复习', content: '全学期内容总结与复习', teachingMethod: '讨论+答疑', learningOutcomes: '为期末考试做准备' }
    ],
    assessments: [
      { type: '期中考试', description: '前半学期内容考试', weight: 30, date: '2023-10-25' },
      { type: '实验作业', description: '数据结构实现与算法设计', weight: 20, date: '2023-12-10' },
      { type: '课程项目', description: '综合应用数据结构与算法解决实际问题', weight: 30, date: '2024-01-05' },
      { type: '期末考试', description: '全学期内容考试', weight: 40, date: '2024-01-20' }
    ],
    resources: [
      { title: '数据结构与算法分析', author: 'Mark Allen Weiss', publisher: '机械工业出版社', year: '2012' },
      { title: '算法导论', author: 'Thomas H. Cormen', publisher: '麻省理工学院出版社', year: '2009' },
      { title: 'Python数据结构与算法分析', author: 'Brad Miller', publisher: '人民邮电出版社', year: '2015' }
    ]
  },
  {
    id: 2,
    courseName: '计算机网络',
    courseCode: 'CS102',
    teacher: '王老师',
    credits: 3,
    startDate: '2023-09-01',
    endDate: '2024-01-15',
    progress: 75,
    status: 'ongoing',
    goals: [
      { description: '理解计算机网络的基本概念和体系结构' },
      { description: '掌握网络层、传输层和应用层的协议和工作原理' },
      { description: '了解网络安全的基本概念和常用技术' },
      { description: '能够设计和实现简单的网络应用' }
    ],
    lessons: [
      { week: 1, topic: '计算机网络概述', content: '网络定义、分类、拓扑结构、网络协议', teachingMethod: '讲授', learningOutcomes: '理解计算机网络的基本概念' },
      { week: 2, topic: '物理层与数据链路层', content: '传输介质、编码技术、MAC地址、以太网', teachingMethod: '讲授+实验', learningOutcomes: '掌握物理层和数据链路层的工作原理' },
      { week: 3, topic: '网络层', content: 'IP协议、路由算法、IPv4/IPv6', teachingMethod: '讲授+讨论', learningOutcomes: '理解网络层的功能和IP协议' },
      { week: 4, topic: '传输层', content: 'TCP和UDP协议、端口号、连接建立与释放', teachingMethod: '讲授+实验', learningOutcomes: '掌握传输层协议的工作原理' },
      { week: 5, topic: '应用层', content: 'HTTP、SMTP、DNS等应用层协议', teachingMethod: '讲授+案例分析', learningOutcomes: '了解常见应用层协议的工作原理' },
      { week: 6, topic: '网络安全基础', content: '加密技术、认证、防火墙、入侵检测', teachingMethod: '讲授+讨论', learningOutcomes: '理解网络安全的基本概念' },
      { week: 7, topic: '网络编程', content: 'Socket编程、网络应用开发', teachingMethod: '讲授+实验', learningOutcomes: '掌握基本的网络编程技术' },
      { week: 8, topic: '期中考试复习', content: '前半学期内容总结与复习', teachingMethod: '讨论+答疑', learningOutcomes: '巩固前半学期所学知识' },
      { week: 9, topic: '期中考试', content: '计算机网络期中考试', teachingMethod: '考试', learningOutcomes: '评估前半学期学习成果' },
      { week: 10, topic: '无线网络', content: 'Wi-Fi技术、移动网络、蓝牙', teachingMethod: '讲授+讨论', learningOutcomes: '了解无线网络的工作原理' },
      { week: 11, topic: '网络管理与优化', content: '网络性能指标、故障诊断、QoS', teachingMethod: '讲授+案例分析', learningOutcomes: '掌握网络管理和优化的基本方法' },
      { week: 12, topic: '网络新技术', content: 'SDN、NFV、物联网网络', teachingMethod: '讲授+讨论', learningOutcomes: '了解网络领域的最新发展' },
      { week: 13, topic: '网络安全进阶', content: '高级加密技术、VPN、网络攻击与防御', teachingMethod: '讲授+案例分析', learningOutcomes: '深入理解网络安全技术' },
      { week: 14, topic: '网络应用开发实践', content: '综合网络应用开发项目', teachingMethod: '实验+指导', learningOutcomes: '应用所学知识开发网络应用' },
      { week: 15, topic: '课程总结与项目展示', content: '课程内容总结、学生项目展示', teachingMethod: '讨论+展示', learningOutcomes: '巩固全学期所学知识' },
      { week: 16, topic: '期末考试复习', content: '全学期内容总结与复习', teachingMethod: '讨论+答疑', learningOutcomes: '为期末考试做准备' }
    ],
    assessments: [
      { type: '期中考试', description: '前半学期内容考试', weight: 30, date: '2023-10-28' },
      { type: '实验作业', description: '网络编程与配置', weight: 25, date: '2023-12-15' },
      { type: '课程项目', description: '设计并实现一个网络应用', weight: 25, date: '2024-01-08' },
      { type: '期末考试', description: '全学期内容考试', weight: 40, date: '2024-01-22' }
    ],
    resources: [
      { title: '计算机网络：自顶向下方法', author: 'Andrew S. Tanenbaum', publisher: '机械工业出版社', year: '2012' },
      { title: 'TCP/IP详解', author: 'Douglas E. Comer', publisher: '电子工业出版社', year: '2000' },
      { title: '网络安全基础教程', author: 'Andrew S. Tanenbaum', publisher: '清华大学出版社', year: '2015' }
    ]
  },
  {
    id: 3,
    courseName: '操作系统',
    courseCode: 'CS103',
    teacher: '张教授',
    credits: 4,
    startDate: '2023-09-01',
    endDate: '2024-01-15',
    progress: 50,
    status: 'ongoing',
    goals: [
      { description: '理解操作系统的基本概念和功能' },
      { description: '掌握进程管理、内存管理和文件系统的工作原理' },
      { description: '了解操作系统的设计和实现方法' },
      { description: '能够使用和配置常见的操作系统' }
    ],
    lessons: [
      { week: 1, topic: '操作系统概述', content: '操作系统的定义、功能、发展历程', teachingMethod: '讲授', learningOutcomes: '理解操作系统的基本概念' },
      { week: 2, topic: '进程与线程', content: '进程的概念、状态转换、线程的概念', teachingMethod: '讲授+讨论', learningOutcomes: '掌握进程和线程的基本概念' },
      { week: 3, topic: '进程调度', content: '调度算法、CPU调度、多处理器调度', teachingMethod: '讲授+实验', learningOutcomes: '理解进程调度的工作原理' },
      { week: 4, topic: '进程同步与通信', content: '临界区问题、信号量、管程', teachingMethod: '讲授+案例分析', learningOutcomes: '掌握进程同步和通信的方法' },
      { week: 5, topic: '死锁', content: '死锁的概念、预防、避免和检测', teachingMethod: '讲授+讨论', learningOutcomes: '理解死锁的原理和处理方法' },
      { week: 6, topic: '内存管理', content: '内存分配策略、虚拟内存、页面置换算法', teachingMethod: '讲授+实验', learningOutcomes: '掌握内存管理的工作原理' },
      { week: 7, topic: '文件系统', content: '文件和目录结构、文件系统实现', teachingMethod: '讲授+讨论', learningOutcomes: '理解文件系统的组织和实现' },
      { week: 8, topic: '期中考试复习', content: '前半学期内容总结与复习', teachingMethod: '讨论+答疑', learningOutcomes: '巩固前半学期所学知识' },
      { week: 9, topic: '期中考试', content: '操作系统期中考试', teachingMethod: '考试', learningOutcomes: '评估前半学期学习成果' },
      { week: 10, topic: '输入输出系统', content: 'I/O硬件、I/O软件、磁盘调度', teachingMethod: '讲授+案例分析', learningOutcomes: '掌握I/O系统的工作原理' },
      { week: 11, topic: '安全与保护', content: '操作系统安全、访问控制、加密', teachingMethod: '讲授+讨论', learningOutcomes: '理解操作系统安全和保护机制' },
      { week: 12, topic: '分布式系统', content: '分布式系统的概念、通信机制', teachingMethod: '讲授+讨论', learningOutcomes: '了解分布式操作系统的基本概念' },
      { week: 13, topic: '操作系统实例分析', content: 'Linux和Windows操作系统分析', teachingMethod: '讲授+实验', learningOutcomes: '掌握常见操作系统的使用和配置' },
      { week: 14, topic: '操作系统设计与实现', content: '操作系统的架构设计、微内核与宏内核', teachingMethod: '讲授+讨论', learningOutcomes: '了解操作系统的设计和实现方法' },
      { week: 15, topic: '课程总结与项目展示', content: '课程内容总结、学生项目展示', teachingMethod: '讨论+展示', learningOutcomes: '巩固全学期所学知识' },
      { week: 16, topic: '期末考试复习', content: '全学期内容总结与复习', teachingMethod: '讨论+答疑', learningOutcomes: '为期末考试做准备' }
    ],
    assessments: [
      { type: '期中考试', description: '前半学期内容考试', weight: 30, date: '2023-10-22' },
      { type: '实验作业', description: '操作系统编程项目', weight: 25, date: '2023-12-05' },
      { type: '课程项目', description: '实现一个小型操作系统模块', weight: 25, date: '2024-01-02' },
      { type: '期末考试', description: '全学期内容考试', weight: 40, date: '2024-01-18' }
    ],
    resources: [
      { title: '现代操作系统', author: 'Andrew S. Tanenbaum', publisher: '机械工业出版社', year: '2012' },
      { title: '操作系统概念', author: 'Abraham Silberschatz', publisher: '高等教育出版社', year: '2014' },
      { title: 'Linux内核设计与实现', author: 'Robert Love', publisher: '机械工业出版社', year: '2013' }
    ]
  },
  {
    id: 4,
    courseName: '高等数学（下）',
    courseCode: 'MATH102',
    teacher: '刘教授',
    credits: 5,
    startDate: '2022-09-01',
    endDate: '2023-01-15',
    progress: 100,
    completionDate: '2023-01-20',
    status: 'completed',
    goals: [
      { description: '掌握多元函数微积分的基本理论和方法' },
      { description: '理解常微分方程的基本概念和求解方法' },
      { description: '能够应用数学知识解决实际问题' },
      { description: '培养逻辑思维和数学建模能力' }
    ]
  },
  {
    id: 5,
    courseName: '线性代数',
    courseCode: 'MATH103',
    teacher: '陈老师',
    credits: 4,
    startDate: '2022-09-01',
    endDate: '2023-01-15',
    progress: 100,
    completionDate: '2023-01-20',
    status: 'completed',
    goals: [
      { description: '掌握矩阵和行列式的基本理论和运算' },
      { description: '理解线性方程组和向量空间的概念' },
      { description: '掌握特征值和特征向量的计算方法' },
      { description: '能够应用线性代数知识解决实际问题' }
    ]
  },
  {
    id: 6,
    courseName: '大学物理',
    courseCode: 'PHYS101',
    teacher: '黄教授',
    credits: 4,
    startDate: '2022-02-15',
    endDate: '2022-06-30',
    progress: 100,
    completionDate: '2022-07-05',
    status: 'completed',
    goals: [
      { description: '掌握经典力学的基本理论和方法' },
      { description: '理解热学和电磁学的基本概念' },
      { description: '能够应用物理知识分析和解决实际问题' },
      { description: '培养科学思维和实验能力' }
    ]
  },
  {
    id: 7,
    courseName: '英语（二）',
    courseCode: 'ENGL102',
    teacher: '吴老师',
    credits: 3,
    startDate: '2022-02-15',
    endDate: '2022-06-30',
    progress: 100,
    completionDate: '2022-07-05',
    status: 'completed',
    goals: [
      { description: '提高英语阅读和写作能力' },
      { description: '掌握学术英语的表达方式' },
      { description: '能够进行学术论文的阅读和写作' },
      { description: '培养跨文化交流能力' }
    ]
  },
  {
    id: 8,
    courseName: '离散数学',
    courseCode: 'MATH101',
    teacher: '郑教授',
    credits: 4,
    startDate: '2022-09-01',
    endDate: '2023-01-15',
    progress: 100,
    completionDate: '2023-01-20',
    status: 'completed',
    goals: [
      { description: '掌握集合、逻辑和函数的基本理论' },
      { description: '理解图论和组合数学的基本概念' },
      { description: '能够应用离散数学知识解决计算机科学问题' },
      { description: '培养抽象思维和逻辑推理能力' }
    ]
  },
  {
    id: 9,
    courseName: '人工智能基础',
    courseCode: 'CS201',
    teacher: '赵教授',
    credits: 4,
    startDate: '2023-02-15',
    endDate: '2023-06-30',
    progress: 100,
    completionDate: '2023-07-05',
    status: 'completed',
    goals: [
      { description: '理解人工智能的基本概念和发展历程' },
      { description: '掌握机器学习的基本算法和模型' },
      { description: '了解自然语言处理和计算机视觉的应用' },
      { description: '能够应用人工智能技术解决实际问题' }
    ]
  },
  {
    id: 10,
    courseName: '数据库系统',
    courseCode: 'CS104',
    teacher: '孙老师',
    credits: 4,
    startDate: '2023-09-01',
    endDate: '2024-01-15',
    progress: 30,
    status: 'draft',
    lastUpdated: '2023-10-10'
  }
])


const ongoingPlans = computed(() => teachingPlans.value.filter(plan => plan.status === 'ongoing'))
const completedPlans = computed(() => teachingPlans.value.filter(plan => plan.status === 'completed'))
const draftPlans = computed(() => teachingPlans.value.filter(plan => plan.status === 'draft'))

const filteredHistoryPlans = computed(() => {
  if (!historySearch.value.trim()) return completedPlans.value

  const searchTerm = historySearch.value.toLowerCase().trim()
  return completedPlans.value.filter(plan =>
    plan.courseName.toLowerCase().includes(searchTerm) ||
    plan.courseCode.toLowerCase().includes(searchTerm) ||
    plan.teacher.toLowerCase().includes(searchTerm)
  )
})

const stats = computed(() => [
  {
    value: ongoingPlans.value.length,
    label: '进行中计划',
    icon: Calendar,
    color: '#6366f1',
    trend: 10
  },
  {
    value: completedPlans.value.length,
    label: '已完成计划',
    icon: Check,
    color: '#10b981',
    trend: 15
  },
  {
    value: draftPlans.value.length,
    label: '草稿计划',
    icon: Document,
    color: '#f59e0b',
    trend: -5
  },
  {
    value: avgPlanProgress.value,
    label: '平均进度',
    icon: Star,
    color: '#409eff',
    trend: 3.2
  }
])

const avgPlanProgress = computed(() => {
  if (ongoingPlans.value.length === 0) return 0
  const totalProgress = ongoingPlans.value.reduce((sum, plan) => sum + plan.progress, 0)
  return Math.round(totalProgress / ongoingPlans.value.length)
})


const planProgressColor = (progress) => {
  if (progress >= 80) return '#67c23a'
  if (progress >= 60) return '#e6a23c'
  return '#f56c6c'
}

const viewPlanDetails = (plan) => {
  selectedPlan.value = { ...plan }
  ElMessage.info(`查看教学计划详情: ${plan.courseName}`)
}

const editPlan = (plan) => {
  ElMessage.info(`编辑教学计划: ${plan.courseName}`)
}

const deletePlan = (plan) => {
  ElMessageBox.confirm(
    `确定要删除教学计划 "${plan.courseName}" 吗?`,
    '确认删除',
    {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    }
  ).then(() => {
    const index = teachingPlans.value.findIndex(p => p.id === plan.id)
    if (index !== -1) {
      teachingPlans.value.splice(index, 1)
      if (selectedPlan.value && selectedPlan.value.id === plan.id) {
        selectedPlan.value = null
      }
      ElMessage.success('教学计划已成功删除')
    }
  }).catch(() => {
    ElMessage.info('已取消删除')
  })
}

const createNewPlan = () => {
  ElMessage.info('创建新的教学计划')
}

const importPlan = () => {
  ElMessage.info('导入教学计划')
}

const exportPlan = (plan) => {
  ElMessage.info(`导出教学计划: ${plan.courseName}`)
}

const handleSizeChange = (size) => {
  pageSize.value = size
}

const handleCurrentChange = (page) => {
  currentPage.value = page
}

const formatStatValue = (value) => {
  return typeof value === 'number' && !Number.isInteger(value) ? value.toFixed(1) : value
}

onMounted(() => {
})
</script>

<style scoped lang="scss">
.teaching-plan-dashboard {
  display: flex;
  flex-direction: column;
  transition: background-color 0.3s ease;
  gap: 30px;
  padding: 0 15px;
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

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
  padding-bottom: 0.75rem;
  border-bottom: 1px solid rgba(226, 232, 240, 0.5);

  .dark & {
    border-color: rgba(74, 85, 104, 0.3);
  }

  h2 {
    font-size: 1.25rem;
    margin: 0;
    display: flex;
    align-items: center;
    font-weight: 600;
    color: #1e293b;
    letter-spacing: -0.025em;

    .dark & {
      color: rgba(248, 250, 252, 0.9);
    }

    .el-icon {
      margin-right: 0.75rem;
      font-size: 1.5rem;
      color: #6366f1;

      .dark & {
        color: #818cf8;
      }
    }
  }

  h3 {
    font-size: 1.125rem;
    margin: 0;
    display: flex;
    align-items: center;
    font-weight: 600;
    color: #333;
    letter-spacing: -0.025em;

    .dark & {
      color: rgba(247, 250, 252, 0.9);
    }

    .el-icon {
      margin-right: 0.75rem;
      font-size: 1.25rem;
      color: #409eff;

      .dark & {
        color: #63b3ed;
      }
    }
  }
}

.modern-select {
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

    .dark & {
      background-color: rgba(30, 41, 59, 0.7);
      border-color: rgba(74, 85, 104, 0.5);
      color: rgba(248, 250, 252, 0.9);

      &:hover {
        border-color: #4f46e5;
      }

      &:focus {
        border-color: #6366f1;
        box-shadow: 0 0 0 3px rgba(99, 102, 241, 0.3);
      }
    }
  }
}

.teaching-plan-content {
  display: flex;
  flex-direction: column;
  gap: 30px
}

.plan-stats {
  margin-top: 20px;
}

.plan-tabs {
  margin-top: 10px;
}

.plan-actions {
  display: flex;
  gap: 10px;
}

.plan-summary {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
  gap: 16px;
  margin-bottom: 24px;

  .summary-item {
    display: flex;
    align-items: center;

    .label {
      font-size: 14px;
      color: #909399;
      margin-right: 8px;
      min-width: 80px;

      .dark & {
        color: rgba(255, 255, 255, 0.6);
      }
    }

    .value {
      font-size: 14px;
      color: #333;

      .dark & {
        color: rgba(255, 255, 255, 0.9);
      }
    }

    .inline-progress {
      display: inline-block;
      width: 120px;
      margin-right: 8px;
    }
  }
}

.plan-section {
  margin-bottom: 24px;

  h4 {
    font-size: 16px;
    font-weight: 500;
    margin-bottom: 12px;
    color: #333;

    .dark & {
      color: rgba(255, 255, 255, 0.9);
    }
  }
}

.goal-item {
  display: flex;
  margin-bottom: 12px;

  .goal-badge {
    margin-right: 12px;
  }
}

.lesson-table,
.assessment-table {
  .el-table__header th {
    background-color: rgba(245, 247, 250, 0.7);

    .dark & {
      background-color: rgba(40, 45, 55, 0.7);
    }
  }
}

.resource-list {
  list-style: none;
  padding: 0;
  margin: 0;

  li {
    display: flex;
    align-items: center;
    margin-bottom: 8px;
    padding: 8px 0;
    border-bottom: 1px solid #ebeef5;

    .dark & {
      border-color: rgba(255, 255, 255, 0.1);
    }

    .el-icon {
      margin-right: 8px;
      color: #606266;

      .dark & {
        color: rgba(255, 255, 255, 0.7);
      }
    }

    .resource-author {
      margin-left: 8px;
      color: #909399;

      .dark & {
        color: rgba(255, 255, 255, 0.5);
      }
    }

    .resource-publisher {
      margin-left: 4px;
      color: #909399;

      .dark & {
        color: rgba(255, 255, 255, 0.5);
      }
    }
  }
}

.empty-state {
  padding: 40px 0;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}

@media (max-width: 768px) {
  .plan-summary {
    grid-template-columns: 1fr;
  }

  .card-header {
    flex-direction: column;
    align-items: flex-start;

    h3 {
      margin-bottom: 12px;
    }

    .plan-actions {
      width: 100%;
    }
  }
}
</style>