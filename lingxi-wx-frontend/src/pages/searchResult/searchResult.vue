<template>
    <view class="searchResultPage">
        <UserList
            v-if="userListData.length"
            :isLocal="true"
            :listData="userListData"
            :useCache="false"
        />
        <UserList v-else :isLocal="true" :listData="[]" :useCache="false" />
    </view>
</template>

<script setup>
import { onLoad } from '@dcloudio/uni-app';
import { searchUsers } from '@/servers/api/userController';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
import { reactive, ref } from 'vue';
const userListData = ref([]);

const loadData = async (params) => {
    let res = await searchUsers(params);
    if (res.data.code === ResponseCodeEnum.SUCCESS) {
        userListData.value = res.data.data;
    }
};
onLoad((option) => {
    let params = JSON.parse(option.params);
    loadData(params);
});
</script>

<style lang="scss">
.searchResultPage {
    height: 100%;
    padding: 15rpx;
    box-sizing: border-box;
}
</style>
