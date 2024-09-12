<template>
    <view class="chat-item">
        <!--            <text class="chat-time" v-if="item.message.sendTime && item.message.sendTime.length">
            {{ formatTimestamp(item.message.sendTime) }}
        </text> -->
        <view :class="{ 'chat-container': true, 'chat-location-me': isCurrentUser }">
            <view class="chat-icon-container">
                <image class="chat-icon" :src="item.fromUser.avatarUrl" mode="aspectFill" />
            </view>
            <view class="chat-content-container">
                <text :class="{ 'chat-user-name': true, 'chat-location-me': isCurrentUser }">
                    {{ item.fromUser.username }}
                </text>
                <!-- 消息归属地 -->
                <text class="user-ip">({{ `ip:${item.fromUser.locPlace || '未知'}` }})</text>
                <!-- 消息发送时间 -->

                <text class="send-time" v-if="true">
                    {{ formatTimestamp(item.message.sendTime) }}
                </text>
                <view
                    class="chat-text-container-super"
                    :style="[{ justifyContent: isCurrentUser ? 'flex-end' : 'flex-start' }]"
                >
                    <view
                        :class="{
                            'chat-text-container': true,
                            'chat-text-container-me': isCurrentUser
                        }"
                    >
                        <text :class="{ 'chat-text': true, 'chat-text-me': isCurrentUser }">
                            {{ item.message.body.content }}
                        </text>
                    </view>
                </view>
            </view>
        </view>
    </view>
</template>

<script setup lang="ts">
import { useUserStore } from '@/store/user';
import { formatTimestamp } from '@/utils/computedTime';
import { computed, nextTick } from 'vue';
const props = defineProps({
    item: {
        type: Object,
        required: true,
        default: {}
    }
});
const userStore = useUserStore();
const isCurrentUser = computed(() => props.item.fromUser.id === userStore?.loginUser.id);
</script>

<style lang="scss">
.chat-item {
    display: flex;
    flex-direction: column;
    padding: 20rpx;
}
.chat-time {
    padding: 4rpx 0rpx;
    text-align: center;
    font-size: 22rpx;
    color: #aaaaaa;
}
.chat-container {
    display: flex;
    flex-direction: row;
}
.chat-location-me {
    flex-direction: row-reverse;
    text-align: right;
}
.chat-icon-container {
    margin-top: 12rpx;
}
.chat-icon {
    width: 80rpx;
    height: 80rpx;
    border-radius: 50%;
    background-color: #eeeeee;
}
.chat-content-container {
    margin: 0rpx 15rpx;
}
.chat-user-name,
.user-ip,
.send-time {
    font-size: 26rpx;
    color: #888888;
}

.chat-text-container {
    text-align: left;
    background-color: #f1f1f1;
    border-radius: 8rpx;
    padding: 10rpx 15rpx;
    margin-top: 10rpx;
    /* #ifndef APP-NVUE */
    max-width: 500rpx;
    /* #endif */
}
.chat-text-container-me {
    background-color: #007aff;
}
.chat-text-container-super {
    display: flex;
    flex-direction: row;
}
.chat-text {
    font-size: 28rpx;
    /* #ifndef APP-NVUE */
    word-break: break-all;
    /* #endif */
    /* #ifdef APP-NVUE */
    max-width: 500rpx;
    /* #endif */
}
.chat-text-me {
    color: white;
}
</style>
