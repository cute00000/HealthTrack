import { defineStore } from 'pinia'
import axios from 'axios'

export const useAuthStore = defineStore('auth', {
  state: () => ({
    token: localStorage.getItem('token') || null,
    user: null,
    isAuthenticated: false
  }),

  getters: {
    getUserRole: (state) => state.user?.role || null,
    getUserName: (state) => state.user?.username || null,
    getUserEmail: (state) => state.user?.username || null
  },

  actions: {
    setToken(token) {
      this.token = token
      localStorage.setItem('token', token)
      axios.defaults.headers.common['Authorization'] = `Bearer ${token}`
    },

    setUser(user) {
      this.user = user
      this.isAuthenticated = true
    },

    login(credentials) {
      return axios.post('/api/auth/login', credentials)
        .then(response => {
          const { token, ...userData } = response.data
          this.setToken(token)
          this.setUser(userData)
          return response.data
        })
    },

    register(userData) {
      return axios.post('/api/auth/register', userData)
        .then(response => {
          const { token, ...userData } = response.data
          this.setToken(token)
          this.setUser(userData)
          return response.data
        })
    },

    logout() {
      this.token = null
      this.user = null
      this.isAuthenticated = false
      localStorage.removeItem('token')
      delete axios.defaults.headers.common['Authorization']
    },

    fetchProfile() {
      if (!this.token) return Promise.reject('No token')
      
      return axios.get('/api/user/profile')
        .then(response => {
          this.setUser(response.data)
          return response.data
        })
    }
  }
})