// store.js
import { defineStore } from 'pinia';
import { computed, reactive } from 'vue';
import { getLoginUser, userLogin } from '@/servers/api/userController';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
export const useUserStore = defineStore('user', () => {
    const loginUser = reactive<API.LoginUserVO>({});
    const doLogin = async (data: API.UserLoginRequest) => {
        let res = await userLogin(data);
        if (res.data.code === ResponseCodeEnum.SUCCESS) {
            Object.assign(loginUser, res.data.data);
            //@ts-ignore
            console.log(res.headers);
            let cookie = null;
            if (res.headers.hasOwnProperty('set-cookie')) {
                cookie = res.headers['set-cookie']?.split(';')[0];
            } else {
                cookie = res.headers['Set-Cookie']?.split(';')[0];
            }
            uni.setStorageSync('cookie', cookie);
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
