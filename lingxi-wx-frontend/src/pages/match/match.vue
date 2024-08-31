<template>
    <view class="matchPage">
        <view class="card" v-if="!userListData.length">
            <van-row>
                <view style="font-size: 40rpx; font-weight: bold">智能匹配</view>
            </van-row>
            <view style="font-size: 34rpx; font-weight: bold">匹配数量</view>
            <van-slider
                use-button-slot
                bar-height="5px"
                active-color="#accbee"
                :value="matchNum"
                min="0"
                max="10"
                @change="(event) => (matchNum = event.detail)"
            >
                <view class="custom-button" slot="button">{{ matchNum }}</view>
            </van-slider>
            <van-button
                :loading="loading"
                round
                block
                type="primary"
                @click="onMatch"
                color="linear-gradient(90deg,#4883FE 10%, #7264FF 50%, #9F5EF6 80%)"
            >
                一键匹配
            </van-button>
        </view>
        <UserList
            v-else
            :isLocal="true"
            :listData="userListData"
            :showTop="false"
            :useCache="false"
        />
    </view>
</template>

<script setup>
import { onLoad } from '@dcloudio/uni-app';
import { listTagVoByPage } from '@/servers/api/tagController';
import { reactive, ref } from 'vue';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
import { matchUsers } from '@/servers/api/userController';
const loading = ref(false);
const matchNum = ref(5);
const userListData = ref([]);
const searchParams = reactive({
    pageSize: 7,
    current: 1,
    isParent: 0
});
const tagList = ref([]);
const onMatch = async () => {
    loading.value = true;
    let res = await matchUsers({ num: matchNum.value });
    if (res.data.code === ResponseCodeEnum.SUCCESS) {
        userListData.value = res.data.data;
    }
    loading.value = false;
};
onLoad(async () => {
    let res = await listTagVoByPage(searchParams);
    if (res.data.code === ResponseCodeEnum.SUCCESS) {
        tagList.value = res.data.data.records;
    }
});
</script>

<style lang="scss">
.matchPage {
    padding: 10rpx;
    box-sizing: border-box;
    height: 100%;
    background: linear-gradient(#433da2 10%, #645ff1 50%, #2d3367 100%);
    .card {
        margin-top: 50%;
        display: flex;
        width: 100%;
        flex-direction: column;
        justify-content: space-evenly;
        padding: 20rpx;
        box-sizing: border-box;
        background-color: white;
        border-radius: 30rpx;
        height: 400rpx;
        .van-button {
            margin-top: 50rpx;
        }
        .custom-button {
            border-radius: 50%;
            color: white;
            background-color: #a1c4fd;
            height: 50rpx;
            width: 50rpx;
            text-align: center;
            line-height: 50rpx;
        }
    }
}
</style>
