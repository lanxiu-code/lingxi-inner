<template>
    <template v-if="isShow">
        <van-divider
            contentPosition="center"
            customStyle="color: #accbee; border-color: #accbee; font-size: 18px;"
        >
            已选标签
        </van-divider>
        <view class="">
            <van-tag
                v-for="(tag, index) in selectedTags"
                :key="index"
                color="#7232dd"
                size="large"
                class="tag"
            >
                {{ tag }}
            </van-tag>
        </view>

        <van-divider
            contentPosition="center"
            customStyle="color: #accbee; border-color: #accbee; font-size: 18px;"
        >
            选择标签
        </van-divider>
        <van-tree-select
            :items="tagItems"
            :main-active-index="mainActiveIndex"
            :active-id="activeId"
            :max="3"
            @click-nav="onClickNav"
            @click-item="onClickItem"
        />
    </template>
</template>

<script setup>
import { onLoad } from '@dcloudio/uni-app';
import { reactive, ref, onMounted } from 'vue';
import { listTagVoByPage } from '@/servers/api/tagController';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
import { tagListToTreeList } from '@/utils/tagUtil';
const props = defineProps({
    isShow: {
        type: Boolean,
        require: false,
        default: true
    }
});
const mainActiveIndex = ref(0);
const activeId = ref([]);
const selectedTags = ref([]);
const searchParams = reactive({
    pageSize: 10,
    current: 1
});
const tagItems = ref([]);
const onClickNav = ({ detail = {} }) => {
    mainActiveIndex.value = detail.index || 0;
};
const onClickItem = ({ detail = {} }) => {
    const index = activeId.value.indexOf(detail.id);
    const tagIndex = selectedTags.value.findIndex((item) => item == detail.text);
    if (index > -1) {
        activeId.value = activeId.value.slice(0, index).concat(activeId.value.slice(index + 1));
        selectedTags.value = selectedTags.value
            .slice(0, tagIndex)
            .concat(selectedTags.value.slice(tagIndex + 1));
    } else {
        activeId.value = [...activeId.value, detail.id];
        selectedTags.value = [...selectedTags.value, detail.text];
    }
};

const loadData = async () => {
    let res = await listTagVoByPage(searchParams);
    if (res.data.code === ResponseCodeEnum.SUCCESS) {
        tagItems.value = tagListToTreeList(res.data.data.records);
    }
};
const getSelectedTags = () => {
    return selectedTags.value;
};
defineExpose({
    getSelectedTags,
    loadData
});

onLoad(() => {
    loadData();
});
</script>

<style lang="scss"></style>
