// @ts-ignore
/* eslint-disable */
import request from '@/utils/request';

/** 此处后端没有提供注释 GET /c_api/chat/public/contact/detail */
export async function getContactDetail(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getContactDetailParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseChatRoomResp>('/c_api/chat/public/contact/detail', {
    method: 'GET',
    params: {
      ...params,
      idReqVO: undefined,
      ...params['idReqVO'],
    },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /c_api/chat/public/contact/page */
export async function getRoomPage(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getRoomPageParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseCursorPageBaseRespChatRoomResp>(
    '/c_api/chat/public/contact/page',
    {
      method: 'GET',
      params: {
        ...params,
        pageBaseReq: undefined,
        ...params['pageBaseReq'],
      },
      ...(options || {}),
    },
  );
}
