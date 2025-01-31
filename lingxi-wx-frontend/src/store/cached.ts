import { defineStore } from 'pinia';
import { reactive, ref } from 'vue';
import { isDiffNow10Min } from '@/utils/computedTime';
import { getUserVoBatch } from '@/servers/api/userController';

export const useCachedStore = defineStore('cached', () => {
    const userCachedList = reactive<Record<number, Partial<API.LoginUserVO>>>({});
    /** 批量获取用户详细信息 */
    const getBatchUserInfo = async (uids: number[]) => {
        if (!uids.length) return;
        const res = await getUserVoBatch(uids);
        res.data.data?.forEach((item) => {
            // 更新最后更新时间。
            const curItemResult = {
                ...item,
                lastModifyTime: Date.now()
            };
            userCachedList[item.id] = curItemResult;
        });
    };
    return { userCachedList, getBatchUserInfo };
});
