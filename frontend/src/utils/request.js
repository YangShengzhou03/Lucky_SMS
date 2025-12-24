import axios from 'axios';
import router from '@/router';
import { ElMessage } from 'element-plus';

const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API || 'http://localhost:8080/',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json;charset=utf-8'
  }
});

service.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    if (config.method === 'get') {
      config.params = { ...config.params, t: Date.now() };
    }
    
    return config;
  },
  (error) => {
    console.error('请求错误:', error);
    return Promise.reject(error);
  }
);

service.interceptors.response.use(
  (response) => {
    const res = response.data;
    
    if (res.code !== 200) {
      ElMessage.error(res.message || '请求失败');
      
      if (res.code === 401) {
        localStorage.removeItem('token');
        router.push('/login');
      }
      
      return Promise.reject(new Error(res.message || '请求失败'));
    } else {
      return res;
    }
  },
  (error) => {
    let errorMsg = '网络错误，请稍后重试';
    
    if (error.response) {
      switch (error.response.status) {
        case 401:
          errorMsg = '登录过期，请重新登录';
          localStorage.removeItem('token');
          router.push('/login');
          break;
        case 403:
          errorMsg = '没有权限访问';
          break;
        case 404:
          errorMsg = '接口不存在';
          break;
        case 500:
          errorMsg = '服务器内部错误';
          break;
        default:
          errorMsg = `请求错误: ${error.response.status}`;
      }
    } else if (error.request) {
      errorMsg = '服务器无响应，请检查网络连接';
    } else {
      errorMsg = '请求配置错误: ' + error.message;
    }
    
    ElMessage.error(errorMsg);
    console.error('响应错误:', error);
    
    return Promise.reject(error);
  }
);

export default service;