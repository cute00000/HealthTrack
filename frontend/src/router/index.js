import { createRouter, createWebHistory } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const routes = [
  {
    path: '/',
    redirect: '/login'
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/Login.vue'),
    meta: { requiresGuest: true }
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('@/views/Register.vue'),
    meta: { requiresGuest: true }
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('@/views/Dashboard.vue'),
    meta: { requiresAuth: true, userType: 'USER' }
  },
  {
    path: '/doctor-dashboard',
    name: 'DoctorDashboard',
    component: () => import('@/views/DoctorDashboard.vue'),
    meta: { requiresAuth: true, userType: 'DOCTOR' }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('@/views/Profile.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const authStore = useAuthStore()
  
  console.log('Navigation:', { to: to.path, from: from.path, authenticated: authStore.isAuthenticated })
  
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    console.log('Redirecting to login: requires auth but not authenticated')
    next('/login')
  } else if (to.meta.requiresGuest && authStore.isAuthenticated) {
    console.log('Redirecting to dashboard: authenticated user trying to access guest page')
    // 根据用户类型跳转到不同的dashboard
    const userType = authStore.getUserRole
    if (userType === 'DOCTOR') {
      next('/doctor-dashboard')
    } else {
      next('/dashboard')
    }
  } else if (to.meta.userType && to.meta.userType !== authStore.getUserRole) {
    console.log('Redirecting to dashboard: user type mismatch')
    // 如果用户类型不匹配，跳转到对应的dashboard
    const userType = authStore.getUserRole
    if (userType === 'DOCTOR') {
      next('/doctor-dashboard')
    } else {
      next('/dashboard')
    }
  } else {
    console.log('Navigation allowed')
    next()
  }
})

export default router
