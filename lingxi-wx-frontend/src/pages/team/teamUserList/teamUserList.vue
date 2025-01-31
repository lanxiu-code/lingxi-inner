<template>
    <view class="teamUserListPage">
        <UserList
            v-if="userListData.length"
            :isLocal="true"
            :showTop="false"
            :listData="userListData"
        />
    </view>
</template>

<script setup>
import { onLoad } from '@dcloudio/uni-app';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
import { reactive, ref } from 'vue';
import { listUserVo } from '@/servers/api/teamController';
const userListData = ref([]);

const loadData = async (id) => {
    let res = await listUserVo({ teamId: id });
    if (res.data.code === ResponseCodeEnum.SUCCESS) {
        userListData.value = res.data.data;
    }
};
onLoad((option) => {
    loadData(option.teamId);
});
</script>

<style lang="scss">
.teamUserListPage {
    height: 100%;
    padding: 15rpx;
    box-sizing: border-box;
}
</style>
