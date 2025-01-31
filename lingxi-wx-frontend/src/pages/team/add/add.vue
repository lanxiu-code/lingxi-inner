<template>
    <view class="addTeamPage">
        <van-cell-group>
            <van-field
                title-width="4em"
                clearable
                label="队伍名称"
                :value="teamInfo.name"
                @change="(event) => (teamInfo.name = event.detail)"
                placeholder="请输入队伍名称"
                :border="false"
            />
            <van-divider />
            <van-field
                title-width="4em"
                label="队伍描述"
                type="textarea"
                autosize
                :value="teamInfo.description"
                placeholder="请输入队伍描述"
                @change="(event) => (teamInfo.description = event.detail)"
                :maxlength="30"
                show-word-limit
                clearable
                :border="false"
            />
            <van-divider />
            <van-field
                title-width="4em"
                clearable
                label="队伍图标"
                :value="teamInfo.icon"
                @change="(event) => (teamInfo.icon = event.detail)"
                placeholder="请输入队伍图标"
                :border="false"
            />
            <van-divider />
            <van-row>
                <van-col span="5" offset="1">队伍人数</van-col>
                <van-col span="7" offset="1">
                    <van-stepper
                        min="0"
                        max="10"
                        integer
                        :value="teamInfo.maxNum"
                        @change="(event) => (teamInfo.maxNum = event.detail)"
                    />
                </van-col>
            </van-row>
            <van-divider />
            <van-row>
                <van-col span="5" offset="1">过期时间</van-col>
                <van-col span="15" offset="1">
                    <uni-datetime-picker
                        returnType="date"
                        type="datetime"
                        v-model="teamInfo.expireTime"
                    />
                </van-col>
            </van-row>
            <van-divider />
            <van-col span="5" offset="1" style="margin-right: 10rpx">队伍类型</van-col>
            <van-radio-group
                :value="teamInfo.status"
                @change="(event) => (teamInfo.status = event.detail)"
                direction="horizontal"
            >
                <van-radio name="0">公开</van-radio>
                <van-radio name="1">私有</van-radio>
                <van-radio name="2">加密</van-radio>
            </van-radio-group>
            <van-divider />
            <van-field
                v-show="teamInfo.status == TeamStatusEnum.SECRET"
                title-width="5em"
                clearable
                label="队伍密码"
                :value="teamInfo.password"
                @change="(event) => (teamInfo.password = event.detail)"
                type="password"
                placeholder="请输入密码"
                :border="false"
            />
            <van-button type="info" round block @click="onAdd">创建</van-button>
        </van-cell-group>
    </view>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { TeamStatusEnum } from '@/enum/TeamStatusEnum';
import { addTeam } from '@/servers/api/teamController';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
const teamInfo = reactive({
    name: null,
    description: null,
    icon: null,
    maxNum: null,
    expireTime: null,
    status: null,
    password: null
});

const onAdd = async () => {
    let res = await addTeam(teamInfo);
    if (res.data.code === ResponseCodeEnum.SUCCESS) {
        uni.showToast({
            title: '创建成功',
            duration: 500
        });
        setTimeout(() => {
            uni.navigateBack();
        }, 500);
    } else {
        uni.showToast({
            title: res.data.message,
            duration: 500,
            icon: 'error'
        });
    }
};
</script>

<style lang="scss">
.addTeamPage {
    padding: 10rpx;
    box-sizing: border-box;
    height: 100%;
    background: white;
    .van-radio-group--horizontal {
        display: inline-flex;
    }
}
</style>
