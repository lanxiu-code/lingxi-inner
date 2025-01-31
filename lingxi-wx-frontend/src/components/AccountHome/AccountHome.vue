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
                        <text class="follow">
                            关注 {{ currentUser?.followCount ? currentUser?.followCount : 0 }}
                        </text>
                    </van-col>
                    <van-col span="5">
                        <text class="fan">
                            粉丝 {{ currentUser?.fansCount ? currentUser?.fansCount : 0 }}
                        </text>
                    </van-col>
                    <van-col span="5" offset="6" v-show="!isMe">
                        <text
                            class="applyFriend"
                            :style="{ background: currentUser.isFriend ? '#c0c0c0' : '#425a8b' }"
                            @click="applyFriend"
                        >
                            {{ currentUser.isFriend ? '已关注' : '关注' }}
                        </text>
                    </van-col>
                    <van-col span="3" v-show="!isMe">
                        <van-icon
                            @click="
                                jump(
                                    false,
                                    `/pages/message/chatBox/chatBox?title=${currentUser.username}&targetUserId=${currentUser.id}`
                                )
                            "
                            size="1.5rem"
                            class-prefix="iconfont icon-xiaoxi"
                            name="extra"
                        />
                    </van-col>
                    <van-col span="2" offset="12" v-show="isMe">
                        <van-icon
                            @click="jump(false, '/pages/account/info/info')"
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
import { useChatStore } from '@/store/chat';
import { onLoad } from '@dcloudio/uni-app';
import { tagStrToList } from '@/utils/tagUtil';
import { apply } from '@/servers/api/userFriendController';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
const chatStore = useChatStore();
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

const jump = (flag, path) => {
    if (flag) {
        uni.switchTab({
            url: path
        });
    } else {
        uni.navigateTo({
            url: path
        });
    }
};
const applyFriend = async () => {
    let res = await apply({
        msg: '请求好友',
        targetUid: props.currentUser.id
    });
    if (res.data.code === ResponseCodeEnum.SUCCESS) {
        uni.showToast({
            title: '关注成功'
        });
        chatStore.getSessionList();
        setTimeout(() => {
            uni.navigateBack();
        }, 1000);
    }
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
        .applyFriend {
            background: #425a8b;
            padding: 10rpx 20rpx 10rpx 20rpx;
            border-radius: 50rpx;
        }
    }
}
</style>
