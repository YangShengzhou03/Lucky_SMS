// src/utils/request.js
import axios from 'axios';
import router from '@/router';
import { ElMessage } from 'element-plus';

// 1. 创建实例，配置基础信息
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API || 'http://localhost:8081/api/', // 基础地址（从环境变量获取）
  timeout: 10000, // 超时时间（毫秒）
  headers: {
    'Content-Type': 'application/json;charset=utf-8' // 默认请求头
  }
});

// 2. 请求拦截器（发送请求前处理）
service.interceptors.request.use(
  (config) => {
    // 添加token（从localStorage获取）
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    // 处理GET请求的缓存问题
    if (config.method === 'get') {
      config.params = { ...config.params, t: Date.now() }; // 添加时间戳防缓存
    }
    
    return config;
  },
  (error) => {
    // 请求错误（如参数错误）
    console.error('请求错误:', error);
    return Promise.reject(error);
  }
);

// 3. 响应拦截器（接收响应后处理）
service.interceptors.response.use(
  (response) => {
    // 只处理2xx状态码的响应
    const res = response.data;
    
    // 根据后端约定的code处理业务逻辑
    if (res.code !== 200) { // 假设200为成功状态码
      // 错误提示
      ElMessage.error(res.message || '请求失败');
      
      // 处理特定错误码，如token过期
      if (res.code === 401) {
        // 清除token
        localStorage.removeItem('token');
        // 跳转到登录页
        router.push('/login');
      }
      
      return Promise.reject(new Error(res.message || '请求失败'));
    } else {
      return res; // 只返回data中的数据，简化调用
    }
  },
  (error) => {
    // 处理非2xx状态码的错误
    let errorMsg = '网络错误，请稍后重试';
    
    if (error.response) {
      // 服务器状态码处理
      switch (error.response.status) {
        case 401:
          errorMsg = '登录过期，请重新登录';
          // 清除token
          localStorage.removeItem('token');
          // 跳转到登录页
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
      // 请求已发出但没有收到响应
      errorMsg = '服务器无响应，请检查网络连接';
    } else {
      // 请求配置出错
      errorMsg = '请求配置错误: ' + error.message;
    }
    
    // 显示错误信息
    ElMessage.error(errorMsg);
    console.error('响应错误:', error);
    
    return Promise.reject(error);
  }
);

// 4. 导出封装后的实例
export default service;