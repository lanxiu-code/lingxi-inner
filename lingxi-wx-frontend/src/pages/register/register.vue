<template>
    <view class="registerPage">
        <view class="registerForm">
            <view class="title">
                <van-image round width="100" height="100" src="/static/logo.jpg" />
                <text user-select>灵犀</text>
            </view>
            <van-cell-group>
                <van-field
                    title-width="2em"
                    clearable
                    label="账号"
                    :value="registerInfo.userAccount"
                    @change="(event:any) => (registerInfo.userAccount = event.detail)"
                    placeholder="请输入账号"
                    border="{{ false }}"
                />
                <van-field
                    title-width="2em"
                    clearable
                    label="密码"
                    :value="registerInfo.userPassword"
                    type="password"
                    @change="(event:any) => (registerInfo.userPassword = event.detail)"
                    placeholder="请输入密码"
                    border="{{ false }}"
                />
                <van-field
                    title-width="4em"
                    clearable
                    label="确认密码"
                    :value="registerInfo.checkPassword"
                    type="password"
                    @change="(event:any) => (registerInfo.checkPassword = event.detail)"
                    placeholder="再次输入密码"
                    border="{{ false }}"
                />
            </van-cell-group>
            <Blank />
            <van-row gutter="4">
                <van-col span="10" offset="2">
                    <van-button type="info" size="normal" block @click="submit">注册</van-button>
                </van-col>
                <van-col span="10">
                    <van-button type="primary" size="normal" block @click="toLogin">
                        已有账号，去登录
                    </van-button>
                </van-col>
            </van-row>
        </view>
    </view>
</template>

<script setup lang="ts">
import { reactive } from 'vue';
import { useUserStore } from '@/store/user';
import { userRegister } from '@/servers/api/userController';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
const store = useUserStore();
const registerInfo: API.UserRegisterRequest = reactive({
    userAccount: '',
    userPassword: '',
    checkPassword: ''
});
const submit = async () => {
    console.log(registerInfo);
    let res = await userRegister(registerInfo);
    if (res.data.code === ResponseCodeEnum.SUCCESS) {
        uni.showToast({
            title: '注册成功',
            duration: 1000
        });
        setTimeout(() => {
            uni.reLaunch({
                url: '/pages/login/login'
            });
        }, 1000);
    }
};
const toLogin = () => {
    uni.reLaunch({
        url: '/pages/login/login'
    });
};
</script>

<style lang="scss">
.registerPage {
    position: relative;
    height: 100%;
    overflow: hidden;
    .registerForm {
        margin-top: 40%;
        .title {
            display: flex;
            flex-direction: column;
            justify-content: center;
            font-size: 50rpx;
            text-align: center;
            margin-bottom: 50rpx;
        }
    }
}
</style>
