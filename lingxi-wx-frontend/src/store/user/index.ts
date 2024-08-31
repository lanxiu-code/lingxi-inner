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
            const cookie = res.headers['Set-Cookie'].split(';')[0];
            uni.setStorageSync('cookie', cookie);
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
