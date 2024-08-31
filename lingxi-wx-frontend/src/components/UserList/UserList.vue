<template>
    <z-paging
        ref="paging"
        use-page-scroll
        :use-cache="useCache"
        cache-key="userList"
        v-model="userList"
        @query="queryList"
        :refresher-enabled="false"
        loading-more-no-more-text="我也是有底线的！"
        auto-show-back-to-top
        safe-area-inset-bottom
    >
        <template #top>
            <GlobalNoticeBar v-show="showTop" />
        </template>
        <view @click="toUserInfo(user.id)" v-for="user in userList" :key="user.id">
            <van-card :desc="user.userProfile" :title="user.username">
                <view slot="thumb">
                    <van-image width="80" height="80" round :src="user.avatarUrl" />
                </view>
                <view slot="tags">
                    <van-tag
                        style="margin-right: 10rpx"
                        v-for="(tag, index) in tagStrToList(user.tags)"
                        :key="index"
                        color="#ffe1e1"
                        text-color="#ad0000"
                    >
                        {{ tag }}
                    </van-tag>
                </view>
            </van-card>
        </view>
    </z-paging>
</template>

<script setup>
import { onLoad, onPullDownRefresh, onPageScroll, onReachBottom } from '@dcloudio/uni-app';
import useZPaging from '@/uni_modules/z-paging/components/z-paging/js/hooks/useZPaging.js';
import { listUserVoByPage } from '@/servers/api/userController';
import { reactive, ref } from 'vue';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
import { tagStrToList } from '@/utils/tagUtil';
const userList = ref([]);
const paging = ref(null);
// props
const props = defineProps({
    isLocal: {
        type: Boolean,
        required: false,
        default: false
    },
    listData: {
        type: Array,
        required: false,
        default: []
    },
    useCache: {
        type: Boolean,
        required: false,
        default: true
    },
    showTop: {
        type: Boolean,
        required: false,
        default: true
    }
});
useZPaging(paging);
const searchParams = reactive({
    pageSize: 10,
    current: 1
});
const queryList = (current, pageSize) => {
    if (!props.isLocal) {
        Object.assign(searchParams, { ...searchParams, current, pageSize });
        loadData();
    } else {
        paging.value.setLocalPaging(props.listData);
    }
};
const toUserInfo = (userId) => {
    if (!userId) return;
    uni.navigateTo({
        url: `/pages/userInfo/userInfo?userId=${userId}`
    });
};
const loadData = async () => {
    let res = await listUserVoByPage(searchParams);
    if (res.data.code === ResponseCodeEnum.SUCCESS) {
        paging.value.complete(res.data.data.records);
    }
    uni.stopPullDownRefresh();
};
defineExpose({ loadData });
</script>

<style lang="scss"></style>
