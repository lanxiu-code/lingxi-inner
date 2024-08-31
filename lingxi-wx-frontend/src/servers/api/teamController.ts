// @ts-ignore
/* eslint-disable */
import request from '@/utils/request';

/** 此处后端没有提供注释 POST /team/add */
export async function addTeam(body: API.TeamAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/team/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /team/delete */
export async function deleteTeam(body: API.DeleteRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/team/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /team/edit */
export async function editTeam(body: API.TeamEditRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/team/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /team/get/vo */
export async function getTeamVoById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getTeamVOByIdParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseTeamVO>('/team/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /team/join */
export async function joinTeam(body: API.TeamJoinRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/team/join', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /team/list/page/vo */
export async function listTeamVoByPage(
  body: API.TeamQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageTeamVO>('/team/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /team/my/join/page/vo */
export async function listMyJoinTeamVoByPage(
  body: API.TeamQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageTeamVO>('/team/my/join/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /team/my/list/page/vo */
export async function listMyTeamVoByPage(
  body: API.TeamQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageTeamVO>('/team/my/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /team/quit */
export async function quitTeam(body: API.TeamQuitRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/team/quit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /team/user/list/vo */
export async function listUserVo(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.listUserVOParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseListUserVO>('/team/user/list/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}
