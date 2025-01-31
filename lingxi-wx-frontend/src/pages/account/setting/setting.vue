<template>
    <view class="accountSettingPage">
        <van-cell-group>
            <template v-if="currentIndex == 0">
                <van-field
                    title-width="2em"
                    label="昵称"
                    :value="updateInfo.username"
                    placeholder="请输入用户名"
                    @change="onChange('username', $event)"
                    clearable
                />
            </template>
            <template v-else-if="currentIndex == 1">
                <van-row>
                    <van-col span="3">
                        <text>性别</text>
                    </van-col>
                    <van-col span="8">
                        <van-radio-group
                            :value="updateInfo.gender"
                            @change="onChange('gender', $event)"
                            direction="horizontal"
                        >
                            <van-radio name="1">男</van-radio>
                            <van-radio name="0">女</van-radio>
                        </van-radio-group>
                    </van-col>
                </van-row>
            </template>
            <template v-else-if="currentIndex == 2">
                <van-field
                    title-width="3em"
                    :value="updateInfo.phone"
                    label="手机号"
                    type="number"
                    placeholder="请输入手机号"
                    @change="onChange('phone', $event)"
                    clearable
                />
            </template>
            <template v-else-if="currentIndex == 3">
                <van-field
                    title-width="2em"
                    label="邮箱"
                    :value="updateInfo.email"
                    placeholder="请输入邮箱"
                    @change="onChange('email', $event)"
                    clearable
                />
            </template>
            <template v-else-if="currentIndex == 4">
                <van-field
                    title-width="4em"
                    label="个人简介"
                    type="textarea"
                    autosize
                    :value="updateInfo.userProfile"
                    placeholder="请输入个人简介"
                    @change="onChange('userProfile', $event)"
                    :maxlength="30"
                    show-word-limit
                    clearable
                />
            </template>
            <CustomTags ref="customTags" :isShow="currentIndex == 5" />
        </van-cell-group>
        <Blank></Blank>
        <van-button @click="onSave" type="primary" block>保存</van-button>
    </view>
</template>

<script setup>
import { reactive, ref } from 'vue';
import { onLoad } from '@dcloudio/uni-app';
import { updateMyUser } from '@/servers/api/userController';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
import { useUserStore } from '@/store/user';
const store = useUserStore();
const titleList = ['昵称', '性别', '电话', '邮箱', '个人简介', '标签'];
const currentIndex = ref(0);
const updateInfo = reactive({});
const customTags = ref(null);
const onChange = (field, e) => {
    updateInfo[field] = e.detail;
};
const onSave = async () => {
    if (currentIndex.value == 5) {
        const selectedTags = customTags.value.getSelectedTags();
        updateInfo.tags = JSON.stringify(selectedTags);
    }
    if (Object.keys(updateInfo) <= 0) return;
    let res = await updateMyUser(updateInfo);
    if (res.data.code === ResponseCodeEnum.SUCCESS) {
        await store.getCurrentUser();
        uni.showToast({
            title: '保存成功',
            icon: 'success',
            duration: 1500
        });
        setTimeout(() => {
            uni.navigateBack();
        }, 1500);
    }
};
onLoad((option) => {
    currentIndex.value = option.index;
    uni.setNavigationBarTitle({
        title: '更改' + titleList[option.index]
    });
});
</script>

<style lang="scss">
.accountSettingPage {
    height: 100%;
    padding: 20rpx;
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
