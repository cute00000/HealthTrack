<template>
  <div class="dashboard-container">
    <el-container>
      <!-- 头部导航 -->
      <el-header class="dashboard-header">
        <div class="header-content">
          <h1>HealthTrack</h1>
          <div class="header-actions">
            <span class="welcome-text">
              欢迎，{{ authStore.getUserName }}
              <el-tag :type="authStore.getUserRole === 'DOCTOR' ? 'success' : 'primary'" size="small">
                {{ authStore.getUserRole === 'DOCTOR' ? '医生' : '用户' }}
              </el-tag>
            </span>
            <el-button @click="handleLogout" type="danger" size="small">
              退出登录
            </el-button>
          </div>
        </div>
      </el-header>
      
      <!-- 主要内容区域 -->
      <el-main class="dashboard-main">
        <div class="dashboard-content">
          <el-row :gutter="20">
            <!-- 用户信息卡片 -->
            <el-col :span="24">
              <el-card class="info-card">
                <template #header>
                  <div class="card-header">
                    <span>个人信息</span>
                    <el-button type="primary" size="small" @click="$router.push('/profile')">
                      查看详情
                    </el-button>
                  </div>
                </template>
                <el-row :gutter="20">
                  <el-col :span="8">
                    <div class="info-item">
                      <el-icon><User /></el-icon>
                      <span>{{ userProfile?.name || '未设置' }}</span>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="info-item">
                      <el-icon><User /></el-icon>
                      <span>{{ userProfile?.username || '未设置' }}</span>
                    </div>
                  </el-col>
                  <el-col :span="8">
                    <div class="info-item">
                      <el-icon><Phone /></el-icon>
                      <span>{{ userProfile?.phone || '未设置' }}</span>
                    </div>
                  </el-col>
                </el-row>
              </el-card>
            </el-col>
            
            <!-- 功能模块 -->
            <el-col :span="12" v-if="authStore.getUserRole === 'USER'">
              <el-card class="feature-card">
                <template #header>
                  <div class="card-header">
                    <el-icon><Calendar /></el-icon>
                    <span>预约管理</span>
                  </div>
                </template>
                <p>管理您的医疗预约</p>
                <el-button type="primary" size="small">查看预约</el-button>
              </el-card>
            </el-col>
            
            <el-col :span="12" v-if="authStore.getUserRole === 'USER'">
              <el-card class="feature-card">
                <template #header>
                  <div class="card-header">
                    <el-icon><Trophy /></el-icon>
                    <span>健康挑战</span>
                  </div>
                </template>
                <p>参与健康挑战活动</p>
                <el-button type="primary" size="small">查看挑战</el-button>
              </el-card>
            </el-col>
            
            <el-col :span="12" v-if="authStore.getUserRole === 'DOCTOR'">
              <el-card class="feature-card">
                <template #header>
                  <div class="card-header">
                    <el-icon><UserFilled /></el-icon>
                    <span>患者管理</span>
                  </div>
                </template>
                <p>管理您的患者信息</p>
                <el-button type="primary" size="small">查看患者</el-button>
              </el-card>
            </el-col>
            
            <el-col :span="12" v-if="authStore.getUserRole === 'DOCTOR'">
              <el-card class="feature-card">
                <template #header>
                  <div class="card-header">
                    <el-icon><Document /></el-icon>
                    <span>医疗记录</span>
                  </div>
                </template>
                <p>查看和管理医疗记录</p>
                <el-button type="primary" size="small">查看记录</el-button>
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
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { 
  User, 
  Phone, 
  Calendar, 
  Trophy, 
  UserFilled, 
  Document 
} from '@element-plus/icons-vue'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const authStore = useAuthStore()

const userProfile = ref(null)

onMounted(async () => {
  try {
    userProfile.value = await authStore.fetchProfile()
  } catch (error) {
    console.error('Failed to fetch profile:', error)
    ElMessage.error('获取用户信息失败')
  }
})

const handleLogout = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要退出登录吗？',
      '确认退出',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    
    authStore.logout()
    ElMessage.success('已退出登录')
    router.push('/login')
  } catch {
    // 用户取消退出
  }
}
</script>

<style scoped>
.dashboard-container {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.dashboard-header {
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

.header-actions {
  display: flex;
  align-items: center;
  gap: 15px;
}

.welcome-text {
  display: flex;
  align-items: center;
  gap: 10px;
  color: #666;
}

.dashboard-main {
  padding: 20px;
}

.dashboard-content {
  max-width: 1200px;
  margin: 0 auto;
}

.info-card, .feature-card {
  margin-bottom: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header .el-icon {
  margin-right: 8px;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 10px 0;
}

.info-item .el-icon {
  color: #409eff;
}

.feature-card {
  text-align: center;
  min-height: 150px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.feature-card p {
  color: #666;
  margin: 10px 0;
}
</style>
