<template>
    <z-paging
        ref="paging"
        use-page-scroll
        :use-cache="useCache"
        cache-key="teamList"
        v-model="teamList"
        @query="queryList"
        :refresher-enabled="false"
        loading-more-no-more-text="我也是有底线的！"
        auto-show-back-to-top
        safe-area-inset-bottom
    >
        <view @click="toDetail(team.id)" v-for="team in teamList" :key="team.id">
            <van-card :desc="team.description" :title="team.name">
                <view slot="thumb">
                    <van-image fit="cover" width="80" height="80" round :src="team.icon" />
                </view>
                <view slot="tags">
                    <van-tag style="margin-right: 10rpx" color="#ffe1e1" text-color="#ad0000">
                        {{ teamStatus[team.status] }}
                    </van-tag>
                </view>
                <template #bottom>
                    <view>队伍人数：{{ team.currentNum }}/{{ team.maxNum }}</view>
                    <view>
                        过期时间：{{ dayjs(team.expireTime).format('YYYY-MM-DD HH:mm:ss') }}
                    </view>
                    <view>
                        发布时间：{{ dayjs(team.createTime).format('YYYY-MM-DD HH:mm:ss') }}
                    </view>
                </template>
            </van-card>
        </view>
    </z-paging>
</template>

<script setup>
import { onLoad, onPullDownRefresh, onPageScroll, onReachBottom } from '@dcloudio/uni-app';
import useZPaging from '@/uni_modules/z-paging/components/z-paging/js/hooks/useZPaging.js';
import { reactive, ref } from 'vue';
import dayjs from 'dayjs';
import { listTeamVoByPage } from '@/servers/api/teamController';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
const teamList = ref([]);
const teamStatus = ref(['公开', '私有', '加密']);
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
        default: false
    },
    showTop: {
        type: Boolean,
        required: false,
        default: true
    }
});
useZPaging(paging);
const toDetail = (id) => {
    uni.navigateTo({
        url: `/pages/team/detail/detail?id=${id}`
    });
};
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
const loadData = async () => {
    let res = await listTeamVoByPage(searchParams);
    if (res.data.code === ResponseCodeEnum.SUCCESS) {
        paging.value.complete(res.data.data.records);
    } else {
        paging.value.complete(false);
    }
    uni.stopPullDownRefresh();
};
const changeLocalData = (data) => {
    paging.value.setLocalPaging(data);
};
defineExpose({ changeLocalData });
</script>
