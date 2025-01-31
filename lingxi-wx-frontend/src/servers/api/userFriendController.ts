// @ts-ignore
/* eslint-disable */
import request from '@/utils/request';

/** 此处后端没有提供注释 POST /c_api/user/friend/apply */
export async function apply(body: API.FriendApplyReq, options?: { [key: string]: any }) {
  return request<API.BaseResponseVoid>('/c_api/user/friend/apply', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
