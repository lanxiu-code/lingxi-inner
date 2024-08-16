import http from '@ohos.net.http'
import request from '../utils/request'
// 获取验证码
export const getCode = (data:any)=>{
  return request("/code/send",
    {
      method:http.RequestMethod.POST,
      extraData: data
    })
}

// 登录
export const doAuthLogin = (data:any)=>{
  return request("/code/send",
    {
      method:http.RequestMethod.POST,
      extraData: data
    })
}

// 注册
export const doAccountRegister = (data:any)=>{
  return request("/account/register",
    {
      method:http.RequestMethod.POST,
      extraData: data
    })
}

// 获取个人信息
export const getAccountInfo = ()=>{
  return request("/account/me",
    {
      method:http.RequestMethod.GET,
    })
}
