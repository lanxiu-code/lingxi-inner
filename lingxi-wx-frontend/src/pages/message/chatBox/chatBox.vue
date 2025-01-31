<template>
    <view class="chatBox">
        <ChatList />
    </view>
</template>

<script setup>
import { onLoad } from '@dcloudio/uni-app';
import { useGlobalStore } from '@/store/global';
import { getRoomId } from '@/servers/api/roomController';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
const globalStore = useGlobalStore();
onLoad(async (option) => {
    uni.setNavigationBarTitle({
        title: option.title || '聊天页面'
    });
    if (option.targetUserId) {
        let res = await getRoomId({ targetUserId: option.targetUserId });
        if (res.data.code === ResponseCodeEnum.SUCCESS) {
            globalStore.currentSession.roomId = res.data.data * 1;
        }
    }
});
</script>

<style lang="scss">
.chatBox {
}
</style>
