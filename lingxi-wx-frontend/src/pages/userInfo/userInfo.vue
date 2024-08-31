<template>
    <view class="userInfoPage">
        <AccountHome :isMe="false" :currentUser="currentUser" />
    </view>
</template>

<script setup>
import { reactive, ref, computed } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { getUserVoById } from '@/servers/api/userController';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
const userInfo = reactive({});
const currentUser = computed(() => userInfo);

onLoad(async (option) => {
    let res = await getUserVoById({ id: option.userId });
    if (res.data.code === ResponseCodeEnum.SUCCESS) {
        Object.assign(userInfo, res.data.data);
    }
});
</script>

<style lang="scss">
.userInfoPage {
    height: 100%;
}
</style>
