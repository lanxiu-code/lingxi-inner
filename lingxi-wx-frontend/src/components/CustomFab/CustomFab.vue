<template>
    <uni-fab
        :pattern="pattern"
        :horizontal="horizontal"
        :vertical="vertical"
        :direction="direction"
        :popMenu="false"
        @fabClick="fabClick"
    ></uni-fab>
</template>

<script setup>
import { reactive, ref } from 'vue';
const horizontal = ref('right');
const vertical = ref('bottom');
const direction = ref('horizontal');

const pattern = reactive({
    backgroundColor: '#fff',
    buttonColor: '#accbee',
    iconColor: '#fff'
});
const props = defineProps({
    url: {
        type: String,
        required: true
    }
});
const fabClick = () => {
    uni.navigateTo({
        url: props.url
    });
};
</script>
