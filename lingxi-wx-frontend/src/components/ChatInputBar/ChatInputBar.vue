<template>
    <view class="chat-input-bar-container">
        <view class="chat-input-bar">
            <view class="chat-input-container">
                <!-- :adjust-position="false"必须设置，防止键盘弹窗自动上顶，交由z-paging内部处理 -->
                <input
                    :focus="focus"
                    class="chat-input"
                    v-model="msg"
                    :adjust-position="false"
                    confirm-type="send"
                    type="text"
                    placeholder="请输入内容"
                    @confirm="sendClick"
                />
            </view>
            <!-- 表情图标（如果不需要切换表情面板则不用写） -->
            <view class="emoji-container">
                <image class="emoji-img" :src="`/static/emoji.png`" @click="emojiChange"></image>
            </view>
            <view class="chat-input-send" @click="sendClick">
                <text class="chat-input-send-text">发送</text>
            </view>
        </view>
        <!--  表情面板，这里使用height控制隐藏显示是为了有高度变化的动画效果（如果不需要切换表情面板则不用写） -->
        <view
            class="emoji-panel-container"
            :style="[{ height: emojiType === 'keyboard' ? '400rpx' : '0px' }]"
        >
            <scroll-view scroll-y style="height: 100%; flex: 1">
                <view class="emoji-panel">
                    <text
                        class="emoji-panel-text"
                        v-for="(item, index) in emojis"
                        :key="index"
                        @click="emojiClick(item)"
                    >
                        {{ item }}
                    </text>
                </view>
            </scroll-view>
        </view>
    </view>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { emojis } from '@/constants/emojis';
import { MsgEnum } from '@/enum';

const msg = ref('');

const focus = ref(false);
const emojiType = ref('');
const emit = defineEmits(['send']);
const updateKeyboardHeightChange = (res: any) => {
    if (res.height > 0) {
        // 键盘展开，将emojiType设置为emoji
        emojiType.value = 'emoji';
    }
};

// 点击了切换表情面板/键盘（如果不需要切换表情面板则不用写）
const emojiChange = () => {
    uni.$emit('emojiTypeChange', emojiType.value);
    if (emojiType.value === 'keyboard') {
        // 点击了键盘，展示键盘
        focus.value = true;
    } else {
        // 点击了切换表情面板
        focus.value = false;
        // 隐藏键盘
        uni.hideKeyboard();
    }
    emojiType.value = !emojiType.value || emojiType.value === 'emoji' ? 'keyboard' : 'emoji';
};
// 点击了某个表情，将其插入输入内容中（如果不需要切换表情面板则不用写）
const emojiClick = (text: any) => {
    msg.value += text;
};
const hidedKeyboard = () => {
    if (emojiType.value === 'keyboard') {
        emojiType.value = '';
    }
};
// 点击了发送按钮
const sendClick = () => {
    if (!msg.value.length) return;
    emit('send', msg.value, MsgEnum.TEXT);
    msg.value = '';
};
defineExpose({ hidedKeyboard, updateKeyboardHeightChange });
</script>

<style lang="scss">
.chat-input-bar {
    display: flex;
    flex-direction: row;
    align-items: center;
    border-top: solid 1px #f5f5f5;
    background-color: #f8f8f8;

    padding: 10rpx 20rpx;
}
.chat-input-container {
    flex: 1;
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    padding: 15rpx;
    background-color: white;
    border-radius: 10rpx;
}
.chat-input {
    flex: 1;
    font-size: 28rpx;
}
.emoji-container {
    width: 54rpx;
    height: 54rpx;
    margin: 10rpx 0rpx 10rpx 20rpx;
}
.emoji-img {
    width: 54rpx;
    height: 54rpx;
}
.chat-input-send {
    background-color: #007aff;
    margin: 10rpx 10rpx 10rpx 20rpx;
    border-radius: 10rpx;
    width: 110rpx;
    height: 60rpx;
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    justify-content: center;
    align-items: center;
}
.chat-input-send-text {
    color: white;
    font-size: 26rpx;
}
.emoji-panel-container {
    background-color: #f8f8f8;
    overflow: hidden;
    transition-property: height;
    transition-duration: 0.15s;
    /* #ifndef APP-NVUE */
    will-change: height;
    /* #endif */
}
.emoji-panel {
    font-size: 30rpx;
    /* #ifndef APP-NVUE */
    display: flex;
    /* #endif */
    flex-direction: row;
    flex-wrap: wrap;
    padding-right: 10rpx;
    padding-left: 15rpx;
    padding-bottom: 10rpx;
}
.emoji-panel-text {
    font-size: 50rpx;
    margin-left: 15rpx;
    margin-top: 20rpx;
}
</style>
