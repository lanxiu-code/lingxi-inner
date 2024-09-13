<template>
    <!-- use-chat-record-mode：开启聊天记录模式 -->
    <!-- use-virtual-list：开启虚拟列表模式 -->
    <!-- cell-height-mode：设置虚拟列表模式高度不固定 -->
    <!-- safe-area-inset-bottom：开启底部安全区域适配 -->
    <!-- bottom-bg-color：设置slot="bottom"容器的背景色，这里设置为和chat-input-bar的背景色一致 -->
    <z-paging
        ref="paging"
        empty-view-text="暂无聊天记录"
        use-chat-record-mode
        use-virtual-list
        cell-height-mode="dynamic"
        safe-area-inset-bottom
        bottom-bg-color="#f8f8f8"
        :auto-clean-list-when-reload="false"
        @virtualListChange="virtualListChange"
        @query="queryList"
        @keyboardHeightChange="keyboardHeightChange"
        @hidedKeyboard="hidedKeyboard"
    >
        <!-- style="transform: scaleY(-1)"必须写，否则会导致列表倒置！！！ -->
        <!-- 注意不要直接在chat-item组件标签上设置style，因为在微信小程序中是无效的，请包一层view -->
        <!--        <template #cell="{ item, zp_index }">
            <view style="transform: scaleY(-1)">
                <ChatItem :item="item"></ChatItem>
            </view>
        </template> -->
        <view
            class="chat-message-item"
            :id="`zp-id-${zp_index}`"
            :key="item.zp_index"
            v-for="(item, zp_index) in dataList"
        >
            <view style="transform: scaleY(-1)">
                <ChatItem :item="item"></ChatItem>
            </view>
        </view>
        <!-- 底部聊天输入框 -->
        <template #bottom>
            <ChatInputBar ref="inputBar" @send="doSend"></ChatInputBar>
        </template>
    </z-paging>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { getMsgPage } from '@/servers/api/chatController';
import { onLoad, onShow } from '@dcloudio/uni-app';
import { useChatStore } from '@/store/chat';
import { sendMsg } from '@/servers/api/chatController';
import { useGlobalStore } from '@/store/global';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
import { useCachedStore } from '@/store/cached';
const paging = ref(null);
const cachedStore = useCachedStore();
const inputBar = ref(null);
const chatStore = useChatStore();
const dataList = ref([]);
const globalStore = useGlobalStore();
const messageOptions = computed(() => chatStore.currentMessageOptions);
const newMsgFlag = ref(false);
const virtualListChange = (vList) => {
    dataList.value = vList;
    const lastMsg = dataList.value[0];
    if (lastMsg) {
        chatStore.currentLastMessageMap.set(lastMsg.message.id * 1, lastMsg);
    }
};
const queryList = (pageNo, pageSize) => {
    if (messageOptions.value.isLast) {
        paging.value.complete(true);
    } else {
        loadData();
    }
};
const loadData = async (size = 20) => {
    const data = await chatStore.getMsgList(size);
    paging.value.complete(data);
};
uni.$on('updateMsgList', () => {
    chatStore.currentMessageOptions.cursor = null;
    chatStore.currentMessageOptions.isLast = false;
    paging.value?.reload();
});
// 监听键盘高度改变，请不要直接通过uni.onKeyboardHeightChange监听，否则可能导致z-paging内置的键盘高度改变监听失效（如果不需要切换表情面板则不用写）
const keyboardHeightChange = (res) => {
    inputBar.value.updateKeyboardHeightChange(res);
};

// 用户尝试隐藏键盘，此时如果表情面板在展示中，应当通知chatInputBar隐藏表情面板（如果不需要切换表情面板则不用写）
const hidedKeyboard = () => {
    inputBar.value.hidedKeyboard();
};

// 发送新消息
const doSend = (msg, type) => {
    uni.showLoading({
        title: '发送中...'
    });
    sendMsg({
        roomId: globalStore.currentSession.roomId,
        msgType: type,
        body: {
            content: msg
        }
    }).finally(() => {
        // paging.value.complete(dataList.value);
        uni.hideLoading();
    });
};
onShow(() => {
    chatStore.currentMessageOptions.cursor = null;
    chatStore.currentMessageOptions.isLast = false;
});
</script>

<style lang="scss"></style>
