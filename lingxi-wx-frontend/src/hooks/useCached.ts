import { computed, toValue, type Ref } from 'vue';
import type { ComputedRef } from 'vue';
import { useCachedStore } from '@/store/cached';

/**
 * 统一获取用户信息 hook
 * @param uid 用户 ID
 * @description 引入该Hook后，可响应式获取用户信息
 */
export const useUserInfo = (uid?: number | ComputedRef<number | undefined> | Ref<number>) => {
    const cachedStore = useCachedStore();
    const userInfo = computed(
        () => (uid && cachedStore.userCachedList[toValue(uid as number)]) || {}
    );

    // 如果没有就请求
    const resultUid = toValue(uid as number);
    if (resultUid && Object.keys(userInfo.value).length === 0) {
        cachedStore.getBatchUserInfo([resultUid]);
    }
    return userInfo;
};
