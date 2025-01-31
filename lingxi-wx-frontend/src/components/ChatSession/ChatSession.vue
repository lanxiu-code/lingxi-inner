<template>
    <z-paging
        ref="paging"
        use-virtual-list
        @virtualListChange="virtualListChange"
        @query="queryList"
        preload-page="5"
        empty-view-text="暂无会话"
        loading-more-no-more-text="我也是有底线的！"
        :force-close-inner-list="true"
        :auto-clean-list-when-reload="false"
        cell-height-mode="dynamic"
        auto-show-back-to-top
    >
        <template #top>
            <GlobalNoticeBar v-show="showTop" />
        </template>
        <view
            @click="onSelectSession(item)"
            class="chat-message-item"
            :id="`zp-id-${zp_index}`"
            :key="item.zp_index"
            v-for="(item, zp_index) in virtualList"
        >
            <view class="item-wrapper">
                <uni-badge
                    class="uni-badge-left-margin"
                    :text="item.unreadCount"
                    absolute="rightTop"
                    size="small"
                >
                    <van-image fit="cover" round width="50" height="52" :src="item.avatar" />
                </uni-badge>
                <view class="message-info">
                    <view style="white-space: nowrap">
                        <text class="person">{{ item.name }}</text>
                        <text v-if="true" class="tag">官方</text>
                    </view>
                    <view class="message-message">{{ item.text }}</view>
                </view>
                <text class="message-time">{{ item.activeTime }}</text>
            </view>
        </view>
    </z-paging>
</template>
<script setup lang="ts">
import { onShow } from '@dcloudio/uni-app';
import { ref, computed } from 'vue';
import { useChatStore } from '@/store/chat';
import { formatTimestamp } from '@/utils/computedTime';
import { MsgEnum, RoomTypeEnum } from '@/enum';
import { useGlobalStore } from '@/store/global';
import renderReplyContent from '@/utils/renderReplyContent';
const virtualList = ref([]);
const globalStore = useGlobalStore();
const paging = ref(null);
const chatStore = useChatStore();
const sessionList = computed(() =>
    chatStore.sessionList.map((item) => {
        // 最后一条消息内容
        const lastMsg = Array.from(chatStore.lastMessageMap.get(item.roomId)?.values() || []).slice(
            -1
        )[0];
        let LastUserMsg = '';
        if (lastMsg) {
            const lastMsgUserName = lastMsg.fromUser;
            LastUserMsg =
                lastMsg.message?.type === MsgEnum.RECALL
                    ? `${lastMsgUserName.username}:'撤回了一条消息'`
                    : renderReplyContent(
                          lastMsgUserName.username,
                          lastMsg.message?.type,
                          lastMsg.message?.body?.content || lastMsg.message?.body
                      );
        }
        return {
            ...item,
            tag: '官方',
            text: LastUserMsg || item.text || '欢迎使用灵犀~',
            activeTime: formatTimestamp(item?.activeTime)
        };
    })
);
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
//监听虚拟列表数组改变并赋值给virtualList进行重新渲染
const virtualListChange = (vList) => {
    virtualList.value = vList;
};
const onSelectSession = (item) => {
    globalStore.currentSession.roomId = item.roomId;
    globalStore.currentSession.type = item.roomType;
    uni.navigateTo({
        url: `/pages/message/chatBox/chatBox?title=${item.name}`
    });
};
const queryList = (current, pageSize) => {
    chatStore.getSessionList();
    paging.value.complete(sessionList.value);
};
uni.$on('updateUnReadCount', () => {
    globalStore.currentSession.roomId = null;
    paging.value?.reload();
});
onShow(() => {
    globalStore.currentSession.roomId = null;
    paging?.value?.reload();
});
</script>
<style lang="scss">
.chat-message-item {
    width: 90%;
    height: 110rpx;
    margin: 15rpx auto 10rpx auto;
    padding: 12rpx 10rpx;
    color: #e9e9eb;
    cursor: pointer;
    background-color: #323644;
    border-radius: 8rpx;
    .item-wrapper {
        position: relative;
        display: flex;
        align-items: center;
        justify-content: space-between;
        height: 100%;
        pointer-events: none;
        .message-info {
            padding: 0 10rpx;
            margin-right: auto;
            .person {
                font-size: 30rpx;
            }
            .tag {
                display: inline-block;
                padding: 1rpx 4rpx;
                margin-left: 4rpx;
                font-size: 20rpx;
                font-weight: bold;
                color: #c59512;
                background-color: #efe2bb;
                border-radius: 4rpx;
                transform: scale(0.8);
            }
            .message-message {
                display: -webkit-box;
                width: 100%;
                overflow: hidden;
                font-size: 20rpx;
                color: #999;
                text-overflow: ellipsis;
                -webkit-box-orient: vertical;
                -webkit-line-clamp: 1;
                word-break: break-word;
                margin-top: 10rpx;
            }
        }
        .message-time {
            font-size: 20rpx;
            color: #999;
            white-space: nowrap;
        }
        &:hover {
            background-color: #484d5f;
            transition: 0.3s;
        }

        &:not(:first-child) {
            margin-top: 4px;
        }

        &.active {
            background-color: #2c3e50;
        }
    }
}
</style>
