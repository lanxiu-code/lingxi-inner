<script setup>
import { ref, reactive } from 'vue';
</script>

<template>
    <view class="messagePage">
        <ChatSession />
    </view>
</template>

<style lang="scss">
.messagePage {
    background-color: #272a37;
    height: 100%;
    padding: 10rpx;
    box-sizing: border-box;
}
</style>
