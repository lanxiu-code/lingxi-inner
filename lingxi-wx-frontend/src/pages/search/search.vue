<template>
    <view class="searchPage">
        <CustomTags ref="customTags" />
        <Blank />
        <van-row>
            <van-col offset="2" span="20">
                <van-button block plain round type="info" @click="onSearch">搜索</van-button>
            </van-col>
        </van-row>
    </view>
</template>

<script setup>
import { onLoad } from '@dcloudio/uni-app';
import { reactive, ref } from 'vue';
const customTags = ref(null);
const onSearch = async () => {
    const selectedTags = customTags.value.getSelectedTags();
    uni.navigateTo({
        url: `/pages/searchResult/searchResult?params=${JSON.stringify({
            tags: selectedTags
        })}`
    });
};
</script>

<style lang="scss">
.searchPage {
    height: 100%;
    padding: 15rpx;
    box-sizing: border-box;
    .van-sidebar-item--selected {
        border-color: #accbee;
    }
    .van-tree-select__item--active {
        color: #accbee;
    }
    .tag {
        margin-right: 10rpx;
    }
}
</style>
