import axios from '@ohos/axios'
import Constant from '../common/Constant';
import { ResponseData } from '../model/DataModel';

const request = axios.create({
  baseURL: Constant.baseUrl,
  timeout: 3000
});

// 添加请求拦截器
request.interceptors.request.use(config => {
  // 对请求数据做点什么
  const token:string = AppStorage.Get("token")
  if(token){
    config.headers.Authorization = token;
    config.headers.setContentType("application/json");
  }
  return config;
}, error => {
  // 对请求错误做些什么
  return Promise.reject(error);
});


// 添加响应拦截器
// @ts-ignore
request.interceptors.response.use(function (response) {
  // 对响应数据做点什么
  return response.data;
}, function (error) {
  // 对响应错误做点什么
  return Promise.reject(error);
});

export default request