<script setup lang="ts">
import { onLaunch, onShow, onHide } from '@dcloudio/uni-app';
import { useUserStore } from '@/store/user';
const store = useUserStore();
onLaunch(async () => {
    console.log('App Launch');

    await store.getCurrentUser();
});
onShow(() => {
    console.log('App Show');
});
onHide(() => {
    console.log('App Hide');
});
</script>
<style>
@import 'wxcomponents/vant/common/index.wxss';
@import 'static/iconfont/iconfont.css';
page {
    margin: 0;
    height: 100%;
    padding: 0;
    box-sizing: border-box;
    background-color: #f7fbfe;
}
.van-card {
    background-color: #fff !important;
    margin-bottom: 20rpx;
    border-radius: 15rpx;
}
</style>
