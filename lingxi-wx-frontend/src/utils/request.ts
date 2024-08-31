import axios from 'axios';
import query from 'querystring';
// import { UniAdapter } from 'uniapp-axios-adapter';
import { local_url, remote_url } from '@/constants/CommonConstant';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
const codeMessage = {
    200: '服务器成功返回请求的数据。',
    400: '发出的请求有错误，服务器没有进行新建或修改数据的操作。',
    401: '用户没有权限（令牌、用户名、密码错误）。',
    403: '用户得到授权，但是访问是被禁止的。',
    404: '发出的请求针对的是不存在的记录，服务器没有进行操作。',
    500: '服务器发生错误，请检查服务器。',
    501: '操作错误'
};
const request = axios.create({
    baseURL: local_url,
    timeout: 3000,
    withCredentials: true
    // adapter: UniAdapter
});

function combineURLs(baseURL: any, relativeURL: any) {
    if (Object.prototype.toString.call(relativeURL).includes('String')) {
        return baseURL.replace(/\/+$/, '') + '/' + relativeURL.replace(/^\/+/, '');
    } else if (!relativeURL) {
        return baseURL;
    } else {
        const params = query.stringify(relativeURL);
        return baseURL + '?' + params;
    }
    // return relativeURL
    //     ? baseURL.replace(/\/+$/, '') + '/' + relativeURL.replace(/^\/+/, '')
    //     : baseURL;
}
function buildFullPath(baseURL: any, requestedURL: any) {
    if (baseURL && !buildURL(requestedURL)) {
        return combineURLs(baseURL, requestedURL);
    }
    return requestedURL;
}
function buildURL(url: any) {
    return /^([a-z][a-z\d+\-.]*:)?\/\//i.test(url);
}

function settle(resolve: any, reject: any, response: any) {
    if (response.status) {
        resolve(response);
    } else {
        reject(response);
    }
}

request.defaults.adapter = function (config) {
    return new Promise((resolve, reject) => {
        if (typeof config.data === 'string') config.data = JSON.parse(config.data); //TODO GET会变成string
        uni.request({
            //@ts-ignore
            method: config.method.toUpperCase(),
            url: combineURLs(buildFullPath(config.baseURL, config.url), config.params),
            header: { ...config.headers },
            data: config.data,
            complete: (response) => {
                response = {
                    //@ts-ignore
                    data: response.data,
                    //@ts-ignore
                    status: response.statusCode,
                    errMsg: response.errMsg,
                    //@ts-ignore
                    headers: response.header,
                    config: config.data
                };
                settle(resolve, reject, response);
            }
        });
    });
};
// 添加请求拦截器
request.interceptors.request.use(
    function (config) {
        // 在发送请求之前做些什么
        let cookie = uni.getStorageSync('cookie');
        if (cookie) {
            config.headers['Cookie'] = cookie;
        }
        return config;
    },
    function (error) {
        // 对请求错误做些什么
        return Promise.reject(error);
    }
);

// 添加响应拦截器
request.interceptors.response.use(
    function (response) {
        // 2xx 范围内的状态码都会触发该函数。
        // 对响应数据做点什么
        const { data } = response;
        // 未登录
        if (data.code === ResponseCodeEnum.UNLOGIN) {
            // 当前页面路径
            const path = getCurrentPages()[0].route;
            uni.showToast({
                title: '请先登录',
                icon: 'error',
                duration: 1500
            });
            if (!path?.includes('login') && !path?.includes('register')) {
                uni.redirectTo({
                    url: '/pages/login/login'
                });
            }
        }
        return response;
    },
    function (error) {
        // 超出 2xx 范围的状态码都会触发该函数。
        // 对响应错误做点什么
        return Promise.reject(error);
    }
);

export default request;
