// @ts-ignore
/* eslint-disable */
import request from '@/utils/request';

/** 此处后端没有提供注释 POST /c_api/chat/msg */
export async function sendMsg(body: API.ChatMessageReq, options?: { [key: string]: any }) {
  return request<API.BaseResponseChatMessageResp>('/c_api/chat/msg', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 PUT /c_api/chat/msg/read */
export async function msgRead(body: API.ChatMessageMemberReq, options?: { [key: string]: any }) {
  return request<API.BaseResponseVoid>('/c_api/chat/msg/read', {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /c_api/chat/public/msg/page */
export async function getMsgPage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getMsgPageParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseCursorPageBaseRespChatMessageResp>('/c_api/chat/public/msg/page', {
    method: 'GET',
    params: {
      ...params,
      messagePageReq: undefined,
      ...params['messagePageReq'],
    },
    ...(options || {}),
  });
}
