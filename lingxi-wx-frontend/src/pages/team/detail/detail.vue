<template>
    <view class="teamDetailPage">
        <view class="top">
            <van-image round width="100" height="100" :src="teamObj.icon" />
            <view>{{ teamObj.name }}</view>
            <van-row>
                <text style="margin-right: 10rpx">当前人数：{{ teamObj.currentNum }}</text>
                <text>最大人数：{{ teamObj.maxNum }}</text>
            </van-row>
        </view>
        <view class="content">
            <van-cell-group>
                <van-cell title="创建人" :value="teamObj?.user?.username" />
                <van-cell title="队伍描述" :label="teamObj.description" border="{{ false }}" />
                <van-cell
                    title="成员列表"
                    is-link
                    link-type="navigateTo"
                    :url="`/pages/team/teamUserList/teamUserList?teamId=${teamObj.id}`"
                />
                <van-cell title="队伍状态" :value="teamStatus[teamObj.status]" />
                <van-cell
                    title="过期时间"
                    :value="dayjs(teamObj.expireTime).format('YYYY-MM-DD HH:mm:ss')"
                />
                <van-cell
                    title="创建时间"
                    :value="dayjs(teamObj.createTime).format('YYYY-MM-DD HH:mm:ss')"
                />
            </van-cell-group>
        </view>
        <Blank></Blank>
        <van-button round @click="onJoin" v-if="!teamObj.hasJoin" type="primary" block>
            加入队伍
        </van-button>
        <van-button @click="onExit" round v-else type="danger" block>退出队伍</van-button>
        <van-dialog
            use-slot
            title="房间密码"
            :show="isShow"
            show-cancel-button
            confirm-button-open-type="getUserInfo"
            @cancel="onCancel"
            @confirm="onConfirm"
        >
            <van-field
                title-width="2em"
                clearable
                type="password"
                label="密码"
                :value="teamPassword"
                placeholder="请输入队伍密码"
                border="{{ false }}"
                @change="(event) => (teamPassword = event.detail)"
            />
        </van-dialog>
    </view>
</template>

<script setup>
import { onLoad } from '@dcloudio/uni-app';
import { reactive, ref } from 'vue';
import dayjs from 'dayjs';
import { getTeamVoById, joinTeam, quitTeam } from '@/servers/api/teamController';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
import { TeamStatusEnum } from '@/enum/TeamStatusEnum';
const teamObj = reactive({});
const isShow = ref(false);
const teamPassword = ref(null);
const teamStatus = ref(['公开', '私有', '加密']);
const joinTeamAPI = async () => {
    let res = await joinTeam({ teamId: teamObj.id, password: teamPassword.value });
    if (res.data.code === ResponseCodeEnum.SUCCESS) {
        uni.showToast({
            title: '加入成功',
            duration: 1500,
            icon: 'success'
        });
        setTimeout(() => {
            uni.navigateBack();
        }, 1500);
    } else {
        uni.showToast({
            title: res.data.message,
            duration: 1500,
            icon: 'error'
        });
    }
};
const onJoin = () => {
    if (teamObj.status === TeamStatusEnum.SECRET) {
        isShow.value = true;
    } else {
        joinTeamAPI();
    }
};
const onExit = async () => {
    let res = await quitTeam({ teamId: teamObj.id });
    if (res.data.code === ResponseCodeEnum.SUCCESS) {
        uni.showToast({
            title: '退出成功',
            duration: 1500,
            icon: 'success'
        });
        setTimeout(() => {
            uni.navigateBack();
        }, 1500);
    } else {
        uni.showToast({
            title: res.data.message,
            duration: 1500,
            icon: 'error'
        });
    }
};
const onConfirm = () => {
    if (!teamPassword.value) return;
    isShow.value = false;
    joinTeamAPI();
};
const onCancel = () => {
    teamPassword.value = null;
    isShow.value = false;
};
onLoad(async (option) => {
    let res = await getTeamVoById({ id: option.id });
    if (res.data.code === ResponseCodeEnum.SUCCESS) {
        Object.assign(teamObj, res.data.data);
    }
});
</script>

<style lang="scss">
.teamDetailPage {
    height: 100%;
    padding: 15rpx;
    box-sizing: border-box;
    .top {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        align-items: center;
        height: 350rpx;
    }
}
</style>
