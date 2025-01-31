// @ts-ignore
/* eslint-disable */
import request from '@/utils/request';

/** 此处后端没有提供注释 POST /tag/add */
export async function addTag(body: API.TagAddRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseLong>('/tag/add', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /tag/delete */
export async function deleteTag(body: API.DeleteRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/tag/delete', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /tag/edit */
export async function editTag(body: API.TagEditRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponseBoolean>('/tag/edit', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 GET /tag/get/vo */
export async function getTagVoById(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params: API.getTagVOByIdParams,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseTagVO>('/tag/get/vo', {
    method: 'GET',
    params: {
      ...params,
    },
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /tag/list/page/vo */
export async function listTagVoByPage(body: API.TagQueryRequest, options?: { [key: string]: any }) {
  return request<API.BaseResponsePageTagVO>('/tag/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}

/** 此处后端没有提供注释 POST /tag/my/list/page/vo */
export async function listMyTagVoByPage(
  body: API.TagQueryRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageTagVO>('/tag/my/list/page/vo', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}
