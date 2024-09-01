<template>
    <view class="accountHomePage">
        <view
            class="top"
            :style="`background-image: url('https://img1.baidu.com/it/u=1211742471,3915088278&fm=253&fmt=auto&app=120&f=JPEG?w=1299&h=800');`"
        >
            <view>
                <van-row>
                    <van-col span="6">
                        <van-image
                            fit="cover"
                            round
                            width="80"
                            height="80"
                            :src="currentUser.avatarUrl"
                        />
                    </van-col>
                    <van-col span="15">
                        <text class="nickName">{{ currentUser.username }}</text>
                        <van-tag
                            v-for="(tag, index) in tagStrToList(currentUser.tags)"
                            :key="index"
                            type="primary"
                            size="medium"
                            style="margin-right: 10rpx"
                        >
                            {{ tag }}
                        </van-tag>
                    </van-col>
                </van-row>
                <van-row>
                    <text class="description">{{ currentUser.userProfile }}</text>
                </van-row>
                <van-row>
                    <van-col span="5">
                        <text class="follow">关注 10</text>
                    </van-col>
                    <van-col span="5">
                        <text class="fan">粉丝 20</text>
                    </van-col>
                    <van-col span="3" offset="8" v-show="!isMe">
                        <text class="fan">关注</text>
                    </van-col>
                    <van-col span="3" v-show="!isMe">
                        <van-icon
                            @click="jump"
                            size="1.5rem"
                            class-prefix="iconfont icon-xiaoxi"
                            name="extra"
                        />
                    </van-col>
                    <van-col span="2" offset="12" v-show="isMe">
                        <van-icon
                            @click="jump"
                            size="1.5rem"
                            class-prefix="iconfont icon-shezhi"
                            name="extra"
                        />
                    </van-col>
                </van-row>
            </view>
        </view>
        <view class="content">
            <van-cell
                v-if="isMe"
                is-link
                title="标签管理"
                link-type="navigateTo"
                url="/pages/account/setting/setting?index=5"
            >
                <template #icon>
                    <van-icon
                        style="margin-right: 8rpx"
                        size="1rem"
                        class-prefix="iconfont icon-yanshoubiaoqianguanli"
                        name="extra"
                    />
                </template>
            </van-cell>
            <van-cell
                is-link
                title="已加入队伍"
                link-type="navigateTo"
                :url="`/pages/account/team/team?title=已加入队伍&userId=${currentUser.id}&flag=1`"
            >
                <template #icon>
                    <van-icon
                        style="margin-right: 8rpx"
                        size="1rem"
                        class-prefix="iconfont icon-tuandui"
                        name="extra"
                    />
                </template>
            </van-cell>
            <van-cell
                is-link
                title="已创建队伍"
                link-type="navigateTo"
                :url="`/pages/account/team/team?title=已创建队伍&userId=${currentUser.id}&flag=0`"
            >
                <template #icon>
                    <van-icon
                        style="margin-right: 8rpx"
                        size="1rem"
                        class-prefix="iconfont
                    icon-renqunbaochuangjian"
                        name="extra"
                    />
                </template>
            </van-cell>
        </view>
    </view>
</template>

<script setup>
import { reactive, ref, computed } from 'vue';
import { useUserStore } from '@/store/user';
import { onLoad } from '@dcloudio/uni-app';
import { tagStrToList } from '@/utils/tagUtil';
// props
const props = defineProps({
    isMe: {
        type: Boolean,
        required: false,
        default: true
    },
    currentUser: {
        type: Object,
        required: true,
        default: {}
    }
});
const jump = () => {
    uni.navigateTo({
        url: '/pages/account/info/info'
    });
};
</script>

<style lang="scss">
.accountHomePage {
    height: 100%;
    .top {
        display: flex;
        flex-direction: column;
        justify-content: flex-end;
        padding: 20rpx;
        height: 400rpx;
        background-repeat: no-repeat;
        background-position: center;
        background-size: 100% 100%;
        color: white;
        .nickName {
            line-height: 80rpx;
            display: block;
            font-size: 30rpx;
        }
        .description {
            display: block;
            margin: 20rpx 0;
            font-size: 24rpx;
        }
    }
}
</style>
