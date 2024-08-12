import { ResponseData } from '../model/DataModel'
import request from '../utils/request'
// 获取车辆信息
export const getCarInfoAPI = ()=>{
  return request<ResponseData>({
    url: '/api/recharge/vehicle/selected',
    method: 'GET'
  })
}
