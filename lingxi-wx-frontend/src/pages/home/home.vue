<template>
    <view class="homePage">
        <GlobalNoticeBar />
        <Blank>
            <template #content>
                <van-cell-group inset>
                    <van-cell
                        title="搭子匹配"
                        label="快速帮你找到志同道合的朋友"
                        url="/pages/match/match"
                    />
                </van-cell-group>
            </template>
        </Blank>
        <UserList ref="userListRef" :showTop="false" />
    </view>
</template>

<script setup>
import { ref } from 'vue';
import { onPullDownRefresh } from '@dcloudio/uni-app';
const userListRef = ref(null);
onPullDownRefresh(() => {
    console.log('开始下拉刷新');
    // userListRef.value.loadData();
});
</script>

<style lang="scss">
.homePage {
    height: 100%;
    padding: 15rpx;
    box-sizing: border-box;
}
</style>
