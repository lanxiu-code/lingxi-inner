<template>
    <view class="accountPage">
        <AccountHome :currentUser="currentUser" />
    </view>
</template>

<script setup>
import { reactive, ref, computed } from 'vue';
import { useUserStore } from '@/store/user';
import { tagStrToList } from '@/utils/tagUtil';
const store = useUserStore();
const currentUser = computed(() => store.loginUser);
</script>

<style lang="scss">
.accountPage {
    height: 100%;
}
</style>
