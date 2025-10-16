<template>
  <div class="profile-container">
    <el-container>
      <!-- 头部导航 -->
      <el-header class="profile-header">
        <div class="header-content">
          <h1>个人资料</h1>
          <el-button @click="$router.go(-1)" size="small">
            <el-icon><ArrowLeft /></el-icon>
            返回
          </el-button>
        </div>
      </el-header>
      
      <!-- 主要内容区域 -->
      <el-main class="profile-main">
        <div class="profile-content">
          <el-row :gutter="20">
            <el-col :span="24">
              <el-card v-loading="loading">
                <template #header>
                  <div class="card-header">
                    <span>基本信息</span>
                    <el-tag :type="userProfile?.role === 'DOCTOR' ? 'success' : 'primary'" size="small">
                      {{ userProfile?.role === 'DOCTOR' ? '医生' : '用户' }}
                    </el-tag>
                  </div>
                </template>
                
                <el-descriptions :column="2" border>
                  <el-descriptions-item label="姓名">
                    {{ userProfile?.name || '未设置' }}
                  </el-descriptions-item>
                  <el-descriptions-item label="用户名">
                    {{ userProfile?.username || '未设置' }}
                  </el-descriptions-item>
                  <el-descriptions-item label="健康ID">
                    {{ userProfile?.healthId || '未设置' }}
                  </el-descriptions-item>
                  <el-descriptions-item label="手机号">
                    {{ userProfile?.phone || '未设置' }}
                  </el-descriptions-item>
                  <el-descriptions-item label="注册时间" :span="2">
                    {{ formatDate(userProfile?.createdAt) }}
                  </el-descriptions-item>
                </el-descriptions>
              </el-card>
            </el-col>
            
            <!-- 医生专用信息 -->
            <el-col :span="24" v-if="userProfile?.role === 'DOCTOR'">
              <el-card>
                <template #header>
                  <div class="card-header">
                    <span>医生信息</span>
                  </div>
                </template>
                
                <el-descriptions :column="2" border>
                  <el-descriptions-item label="医疗执照号">
                    {{ doctorInfo?.licenseId || '未设置' }}
                  </el-descriptions-item>
                  <el-descriptions-item label="专业领域">
                    {{ getSpecializationText(doctorInfo?.specialization) || '未设置' }}
                  </el-descriptions-item>
                  <el-descriptions-item label="验证状态" :span="2">
                    <el-tag 
                      :type="doctorInfo?.verified ? 'success' : 'warning'"
                    >
                      {{ doctorInfo?.verified ? '已验证' : '待验证' }}
                    </el-tag>
                  </el-descriptions-item>
                </el-descriptions>
              </el-card>
            </el-col>
            
            <!-- 操作按钮 -->
            <el-col :span="24">
              <el-card>
                <template #header>
                  <span>账户操作</span>
                </template>
                
                <el-space>
                  <el-button type="primary" @click="handleEditProfile">
                    <el-icon><Edit /></el-icon>
                    编辑资料
                  </el-button>
                  <el-button type="warning" @click="handleChangePassword">
                    <el-icon><Lock /></el-icon>
                    修改密码
                  </el-button>
                </el-space>
              </el-card>
            </el-col>
          </el-row>
        </div>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { ArrowLeft, Edit, Lock } from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'
import axios from 'axios'

const authStore = useAuthStore()

const loading = ref(false)
const userProfile = ref(null)
const doctorInfo = ref(null)

onMounted(async () => {
  await fetchProfile()
})

const getSpecializationText = (code) => {
  const specializationMap = {
    1: '内科',
    2: '外科',
    3: '儿科',
    4: '妇产科',
    5: '眼科',
    6: '耳鼻喉科',
    7: '皮肤科',
    8: '精神科',
    9: '急诊科',
    10: '其他'
  }
  return specializationMap[code] || '未知'
}

const fetchProfile = async () => {
  try {
    loading.value = true
    userProfile.value = await authStore.fetchProfile()
    
    // 如果是医生，获取医生信息
    if (userProfile.value?.role === 'DOCTOR') {
      // 这里可以添加获取医生详细信息的API调用
      doctorInfo.value = {
        licenseId: 123456789,
        specialization: 1,
        verified: false
      }
    }
  } catch (error) {
    console.error('Failed to fetch profile:', error)
    ElMessage.error('获取用户信息失败')
  } finally {
    loading.value = false
  }
}

const formatDate = (dateString) => {
  if (!dateString) return '未知'
  return new Date(dateString).toLocaleString('zh-CN')
}

const handleEditProfile = () => {
  ElMessage.info('编辑功能开发中...')
}


const handleChangePassword = () => {
  ElMessage.info('修改密码功能开发中...')
}
</script>

<style scoped>
.profile-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.profile-header {
  background: white;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  padding: 0;
}

.header-content {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  padding: 0 20px;
}

.header-content h1 {
  margin: 0;
  color: #333;
  font-size: 24px;
}

.profile-main {
  padding: 20px;
}

.profile-content {
  max-width: 1000px;
  margin: 0 auto;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-card {
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}
</style>
