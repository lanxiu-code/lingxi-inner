<template>
    <view class="teamPage">
        <van-tabs color="#accbee" :active="0" @change="onChange">
            <van-tab title="公开" name="0"></van-tab>
            <van-tab title="加密" name="2"></van-tab>
        </van-tabs>
        <TeamList ref="teamListRef" :isLocal="true" :listData="teamList" :useCache="false" />
    </view>
</template>

<script setup lang="ts">
import { onLoad } from '@dcloudio/uni-app';
import { reactive, ref, watch } from 'vue';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
import { listMyJoinTeamVoByPage, listMyTeamVoByPage } from '@/servers/api/teamController';
export interface Option {
    userId: string;
    title: string;
    flag: number;
}
const flag = ref(1);
const teamList = ref([]);
const teamListRef = ref(null);
const searchParams = reactive({
    pageSize: 10,
    current: 1,
    status: 0,
    userId: null
});
const onChange = (event: any) => {
    searchParams.status = event.detail.name * 1;
};
const loadData = async () => {
    let res = null;
    if (flag.value == 1) {
        res = await listMyJoinTeamVoByPage(searchParams);
    } else {
        res = await listMyTeamVoByPage(searchParams);
    }
    if (res.data.code === ResponseCodeEnum.SUCCESS) {
        teamList.value = res.data.data.records;
        teamListRef.value.changeLocalData(teamList.value);
    } else {
        teamListRef.value.changeLocalData([]);
    }
};
watch([() => searchParams.status], () => loadData(), { deep: true });
onLoad((option: Option) => {
    uni.setNavigationBarTitle({
        title: option.title
    });
    searchParams.userId = option.userId;
    flag.value = option.flag;
    loadData();
});
</script>

<style lang="scss">
.teamPage {
    height: 100%;
    padding: 15rpx;
    box-sizing: border-box;
}
</style>
