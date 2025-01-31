import { defineStore } from 'pinia';
import { reactive, ref, watch } from 'vue';
import { RoomTypeEnum } from '@/enum';
import { msgRead } from '@/servers/api/chatController';
import { useChatStore } from '@/store/chat';
export const useGlobalStore = defineStore('global', () => {
    const chatStore = useChatStore();
    const currentSession = reactive<{ roomId: number; type: RoomTypeEnum }>({
        roomId: 1,
        type: RoomTypeEnum.Group
    });
    const unReadMark = reactive<{ newFriendUnreadCount: number; newMsgUnreadCount: number }>({
        newFriendUnreadCount: 0,
        newMsgUnreadCount: 0
    });
    // 切换会话的时候重置消息已读数查询
    watch(currentSession, (val) => {
        // 标记房间最新消息已读
        msgRead({ roomId: val.roomId });
        const unreadCount = chatStore.markSessionRead(val.roomId);
        const resultCount = unReadMark.newMsgUnreadCount - unreadCount;
        unReadMark.newMsgUnreadCount = resultCount > 0 ? resultCount : 0;
    });
    return {
        currentSession
    };
});
