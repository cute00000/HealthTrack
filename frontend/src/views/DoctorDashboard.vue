<template>
  <div class="doctor-dashboard">
    <div class="dashboard-header">
      <h1>医生工作台</h1>
      <div class="user-info">
        <span>欢迎，{{ authStore.getUserName }}</span>
        <el-button type="danger" @click="handleLogout">退出登录</el-button>
      </div>
    </div>
    
    <div class="dashboard-content">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-card class="dashboard-card">
            <template #header>
              <div class="card-header">
                <span>患者管理</span>
              </div>
            </template>
            <p>管理您的患者信息和病历</p>
            <el-button type="primary">查看患者</el-button>
          </el-card>
        </el-col>
        
        <el-col :span="8">
          <el-card class="dashboard-card">
            <template #header>
              <div class="card-header">
                <span>预约管理</span>
              </div>
            </template>
            <p>查看和管理患者预约</p>
            <el-button type="primary">查看预约</el-button>
          </el-card>
        </el-col>
        
        <el-col :span="8">
          <el-card class="dashboard-card">
            <template #header>
              <div class="card-header">
                <span>个人资料</span>
              </div>
            </template>
            <p>管理您的个人信息和执业信息</p>
            <el-button type="primary" @click="$router.push('/profile')">编辑资料</el-button>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import { ElMessage } from 'element-plus'

const router = useRouter()
const authStore = useAuthStore()

const handleLogout = () => {
  authStore.logout()
  ElMessage.success('已退出登录')
  router.push('/login')
}
</script>

<style scoped>
.doctor-dashboard {
  padding: 20px;
}

.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid #e4e7ed;
}

.dashboard-header h1 {
  color: #409eff;
  margin: 0;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.dashboard-content {
  margin-top: 20px;
}

.dashboard-card {
  height: 200px;
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.dashboard-card p {
  margin: 15px 0;
  color: #666;
}
</style>