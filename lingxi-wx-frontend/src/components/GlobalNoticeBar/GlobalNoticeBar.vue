<template>
    <view class="globalNoticeBar">
        <van-notice-bar left-icon="volume-o" :text="text" />
    </view>
</template>
<script setup>
const props = defineProps({
    text: {
        type: String,
        required: false,
        default: '有bug请联系管理人员,QQ:1341322187'
    }
});
</script>
<style lang="scss">
.globalNoticeBar {
}
</style>
