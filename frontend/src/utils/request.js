// src/utils/request.js
import axios from 'axios';

// 1. 创建实例，配置基础信息
const service = axios.create({
  baseURL: process.env.VUE_APP_BASE_API, // 基础地址（可从环境变量获取）
  timeout: 5000, // 超时时间（毫秒）
  headers: {
    'Content-Type': 'application/json;charset=utf-8' // 默认请求头
  }
});

// 2. 请求拦截器（发送请求前处理）
service.interceptors.request.use(
  (config) => {
    // 示例1：添加token（如从localStorage获取）
    const token = localStorage.getItem('token');
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    
    // 示例2：处理GET请求的缓存问题
    if (config.method === 'get') {
      config.params = { ...config.params, t: Date.now() }; // 添加时间戳防缓存
    }
    
    return config;
  },
  (error) => {
    // 请求错误（如参数错误）
    return Promise.reject(error);
  }
);

// 3. 响应拦截器（接收响应后处理）
service.interceptors.response.use(
  (response) => {
    // 只处理2xx状态码的响应
    const res = response.data;
    
    // 示例：根据后端约定的code处理业务逻辑
    if (res.code !== 200) { // 假设200为成功状态码
      // 错误提示（可结合UI库的Message组件）
      console.error(`错误码: ${res.code}, 信息: ${res.message}`);
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
          errorMsg = '登录过期效，请重新登录';
          // 可在此处跳转登录页
          // router.push('/login');
          break;
        case 403:
          errorMsg = '没有权限权限访问';
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
    }
    
    console.error(errorMsg);
    return Promise.reject(error);
  }
);

// 4. 导出封装后的实例
export default service;