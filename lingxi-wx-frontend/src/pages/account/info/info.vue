<template>
    <view class="accountInfoPage">
        <van-cell @click="jump(0)" title="昵称" is-link :value="currentUser.username" />
        <van-cell @click="jump(1)" title="性别" is-link :value="genderList[currentUser.gender]" />
        <van-cell @click="jump(2)" title="电话" is-link :value="currentUser.phone" />
        <van-cell @click="jump(3)" title="邮箱" is-link :value="currentUser.email" />
        <van-cell @click="jump(4)" title="个人简介" is-link :value="currentUser.userProfile" />
        <van-cell @click="jump(5)" title="标签" is-link />
    </view>
</template>

<script setup>
import { computed, reactive, ref } from 'vue';
import { useUserStore } from '@/store/user';
import { onLoad } from '@dcloudio/uni-app';
import { tagStrToList } from '@/utils/tagUtil';
import { genderList } from '@/constants/CommonConstant';
const store = useUserStore();
const currentUser = computed(() => store.loginUser);
const tagList = ref([]);
const jump = (index) => {
    uni.navigateTo({
        url: `/pages/account/setting/setting?index=${index}`
    });
};
onLoad(() => {
    tagList.value = tagStrToList(currentUser.tags);
});
</script>

<style lang="scss">
.accountInfoPage {
    height: 100%;
}
</style>
