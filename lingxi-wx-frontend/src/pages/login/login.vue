<template>
    <view class="loginPage">
        <view class="loginForm">
            <view class="title">
                <van-image round width="100" height="100" src="/static/logo.jpg" />
                <text user-select>灵犀</text>
            </view>
            <van-cell-group>
                <van-field
                    title-width="2em"
                    clearable
                    label="账号"
                    :value="loginInfo.userAccount"
                    @change="(event:any) => (loginInfo.userAccount = event.detail)"
                    placeholder="请输入账号"
                    border="{{ false }}"
                />
                <van-field
                    title-width="2em"
                    clearable
                    label="密码"
                    :value="loginInfo.userPassword"
                    @change="(event:any) => (loginInfo.userPassword = event.detail)"
                    type="password"
                    placeholder="请输入密码"
                    border="{{ false }}"
                />
            </van-cell-group>
            <Blank />
            <van-row gutter="4">
                <van-col span="10" offset="2">
                    <van-button type="primary" size="normal" block @click="submit">登录</van-button>
                </van-col>
                <van-col span="10">
                    <van-button plain type="info" size="normal" block @click="toRegister">
                        注册
                    </van-button>
                </van-col>
            </van-row>
        </view>
    </view>
</template>

<script setup lang="ts">
import { reactive } from 'vue';
import { useUserStore } from '@/store/user';
const store = useUserStore();
const loginInfo: API.UserLoginRequest = reactive({
    userAccount: '',
    userPassword: ''
});
const toRegister = () => {
    uni.reLaunch({
        url: '/pages/register/register'
    });
};
const submit = async () => {
    await store.doLogin(loginInfo);
};
</script>

<style lang="scss">
.loginPage {
    position: relative;
    height: 100%;
    overflow: hidden;
    .loginForm {
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
