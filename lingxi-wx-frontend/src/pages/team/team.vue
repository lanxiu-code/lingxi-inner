<template>
    <view class="teamPage">
        <GlobalNoticeBar />
        <van-search
            shape="round"
            :value="searchParams.name"
            placeholder="搜索队伍"
            use-action-slot
            @change="(event) => (searchParams.name = event.detail)"
        >
            <view slot="action" @tap="loadData">搜索</view>
        </van-search>
        <van-tabs color="#accbee" :active="0" @change="onChange">
            <van-tab title="公开" name="0"></van-tab>
            <van-tab title="加密" name="2"></van-tab>
        </van-tabs>
        <TeamList ref="teamListRef" :isLocal="true" :listData="teamList" :useCache="false" />
        <CustomFab url="/pages/team/add/add" />
    </view>
</template>

<script setup>
import { onLoad, onShow } from '@dcloudio/uni-app';
import { reactive, ref, watch } from 'vue';
import { listTeamVoByPage } from '@/servers/api/teamController';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
const teamList = ref([]);
const teamListRef = ref(null);
const searchParams = reactive({
    pageSize: 10,
    current: 1,
    status: 0,
    name: ''
});
const onChange = (event) => {
    searchParams.status = event.detail.name * 1;
};

const loadData = async () => {
    let res = await listTeamVoByPage(searchParams);
    if (res.data.code === ResponseCodeEnum.SUCCESS) {
        teamList.value = res.data.data.records;
        teamListRef.value.changeLocalData(teamList.value);
    }
};
watch([() => searchParams.status], () => loadData());
// onLoad(() => loadData());
onShow(() => {
    // console.log(123);
    loadData();
});
</script>

<style lang="scss">
.teamPage {
    height: 100%;
    padding: 15rpx;
    box-sizing: border-box;
    .van-search__action {
        margin-right: 10rpx;
    }
}
</style>
