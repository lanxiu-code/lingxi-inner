<template>
    <view>
        <van-button type="primary" block @click="jump">微信登录</van-button>
        <button open-type="chooseAvatar" @chooseavatar="onChoose">获取信息</button>
        <button open-type="getPhoneNumber" @getphonenumber="getPhoneNumber">手机登录</button>
        <input type="nickname" class="weui-input" placeholder="请输入昵称" />
    </view>
</template>

<script setup>
import { userLoginByWx } from '@/servers/api/userController';
const onChoose = (e) => {
    console.log(e);
};
const getPhoneNumber = (e) => {
    console.log(e.detail.code); // 动态令牌
    console.log(e.detail.errMsg); // 回调信息（成功失败都会返回）
    console.log(e.detail.errno); // 错误码（失败时返回）
};
const jump = async () => {
    // let wxResult = await uni.login({
    //     provider: 'weixin'
    // });

    // let res = await userLoginByWx({ code: wxResult.code });
    // console.log(res);
    // uni.getUserInfo({
    //     provider: 'weixin',
    //     success: function (infoRes) {
    //         console.log('用户昵称为：' + JSON.stringify(infoRes));
    //     }
    // });
    uni.getUserProfile({
        desc: '查看用户信息，保存数据',
        success: function (infoRes) {
            console.log(JSON.stringify(infoRes));
        }
    });
};
</script>

<style lang="less"></style>
