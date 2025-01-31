// @ts-ignore
/* eslint-disable */
import request from '@/utils/request';

/** 此处后端没有提供注释 GET /c_api/room/get/roomId */
export async function getRoomId(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getRoomIdParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong>('/c_api/room/get/roomId', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
