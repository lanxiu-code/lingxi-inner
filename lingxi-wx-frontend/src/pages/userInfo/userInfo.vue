<template>
    <view class="userInfoPage">
        <AccountHome :isMe="currentUser.id == userStore.loginUser?.id" :currentUser="currentUser" />
    </view>
</template>

<script setup>
import { reactive, ref, computed } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { useUserStore } from '@/store/user';
import { getUserVoById } from '@/servers/api/userController';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
const userInfo = reactive({});
const userStore = useUserStore();
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
