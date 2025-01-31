// store.js
import { defineStore } from 'pinia';
import { computed, reactive } from 'vue';
import { getLoginUser, getToken, userLogin } from '@/servers/api/userController';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
import ws from '@/utils/websocket';
export const useUserStore = defineStore('user', () => {
    const loginUser = reactive({});
    const doLogin = async (data: API.UserLoginRequest) => {
        let res = await userLogin(data);
        if (res.data.code === ResponseCodeEnum.SUCCESS) {
            Object.assign(loginUser, res.data.data);
            let cookie = null;
            if (res.headers.hasOwnProperty('set-cookie')) {
                cookie = res.headers['set-cookie']?.split(';')[0];
            } else {
                cookie = res.headers['Set-Cookie']?.split(';')[0];
            }
            uni.setStorageSync('cookie', cookie);
            const tokenRes = await getToken();
            uni.setStorageSync('token', tokenRes.data.data);
            try {
                await ws.closeWebSocket();
            } catch (e) {
                console.log('websocket已是断开状态');
            }
            ws.initConnect();
        } else {
            uni.showToast({
                title: res.data.message,
                duration: 1500,
                icon: 'error'
            });
        }
    };
    const getCurrentUser = async () => {
        let res = await getLoginUser();
        if (res.data.code === ResponseCodeEnum.SUCCESS) {
            Object.assign(loginUser, res.data.data);
        }
    };
    return { loginUser, doLogin, getCurrentUser };
});
