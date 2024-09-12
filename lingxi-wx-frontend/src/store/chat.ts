import { defineStore } from 'pinia';
import { reactive, ref, computed, watch } from 'vue';
import type { MessageType, SessionItem } from '@/types/types';
import { getRoomPage, getContactDetail } from '@/servers/api/contactController';
import { useGlobalStore } from './global';
import cloneDeep from 'lodash/cloneDeep';
import { MarkEnum, MsgEnum, RoomTypeEnum } from '@/enum';
import { getMsgPage } from '@/servers/api/chatController';
import { computedTimeBlock } from '@/utils/computedTime';
import { useCachedStore } from './cached';
import { ResponseCodeEnum } from '@/enum/ResponseCodeEnum';
export const pageSize = 20;
// 标识是否第一次请求
let isFirstInit = false;
export const useChatStore = defineStore('chat', () => {
    const sessionList = ref<SessionItem[]>([]); // 会话列表
    const sessionOptions = reactive({ isLast: false, cursor: undefined });
    const globalStore = useGlobalStore();
    const cachedStore = useCachedStore();
    const currentRoomId = computed(() => globalStore.currentSession?.roomId);
    const currentRoomType = computed(() => globalStore.currentSession?.type);
    // 将消息列表转换为数组
    const chatMessageList = computed(() => [...(currentMessageMap.value?.values() || [])]);
    // 消息Map
    const messageMap = reactive<Map<number, Map<number, MessageType>>>(
        new Map([[currentRoomId.value, new Map()]])
    );
    const messageOptions = reactive<
        Map<number, { isLast: boolean; isLoading: boolean; cursor: string }>
    >(new Map([[currentRoomId.value, { isLast: false, isLoading: false, cursor: '' }]]));
    // 回复消息映射
    const currentMessageMap = computed({
        get: () => {
            const current = messageMap.get(currentRoomId.value as number);
            if (current === undefined) {
                messageMap.set(currentRoomId.value, new Map());
            }
            return messageMap.get(currentRoomId.value as number);
        },
        set: (val) => {
            messageMap.set(currentRoomId.value, val as Map<number, MessageType>);
        }
    });
    const currentMessageOptions = computed({
        get: () => {
            const current = messageOptions.get(currentRoomId.value as number);
            if (current === undefined) {
                messageOptions.set(currentRoomId.value, {
                    isLast: false,
                    isLoading: true,
                    cursor: ''
                });
            }
            return messageOptions.get(currentRoomId.value as number);
        },
        set: (val) => {
            messageOptions.set(
                currentRoomId.value,
                val as { isLast: boolean; isLoading: boolean; cursor: string }
            );
        }
    });

    // 获取消息列表
    const getMsgList = async (size = pageSize) => {
        currentMessageOptions.value && (currentMessageOptions.value.isLoading = true);
        const res = await getMsgPage({
            pageSize: size,
            cursor: currentMessageOptions.value?.cursor,
            roomId: currentRoomId.value
        });
        const data = res.data.data;
        if (!data) return [];
        const computedList = computedTimeBlock(data.list);

        /** 收集需要请求用户详情的 uid */
        const uidCollectYet: Set<number> = new Set(); // 去重用
        computedList.forEach((msg) => {
            // 查询消息发送者的信息
            uidCollectYet.add(msg.fromUser.id);
        });
        // 获取用户信息缓存
        cachedStore.getBatchUserInfo([...uidCollectYet]);

        if (currentMessageOptions.value) {
            currentMessageOptions.value.cursor = data.cursor;
            currentMessageOptions.value.isLast = data.isLast;
            currentMessageOptions.value.isLoading = false;
        }
        return computedList;
    };

    watch(currentRoomId, (val, oldVal) => {
        if (oldVal !== undefined && val !== oldVal) {
            // 切换的 rooms是空数据的话就请求消息列表
            //getMsgList();
        }
    });
    // 获取会话列表
    const getSessionList = async (isFresh = false) => {
        if (!isFresh) return;
        let searchParams = {
            pageSize: sessionList.value.length > pageSize ? sessionList.value.length : pageSize
        };
        if (!isFresh || sessionOptions.cursor) {
            searchParams.cursor = sessionOptions.cursor;
        }
        let res = await getRoomPage(searchParams);
        const data = res.data.data;
        if (!data) return;
        isFresh
            ? sessionList.value.splice(0, sessionList.length, ...data.list)
            : sessionList.value.push(...data.list);
        sessionOptions.cursor = data.cursor;
        sessionOptions.isLast = data.isLast;
        sortAndUniqueSessionList();
        if (!isFirstInit) {
            isFirstInit = true;
            globalStore.currentSession.roomId = data.list[0].roomId;
            globalStore.currentSession.type = data.list[0].type;
        }
    };
    // 标记已读数为 0
    const markSessionRead = (roomId: number) => {
        const index = sessionList.value.findIndex((item) => item.roomId === roomId);
        const unreadCount = sessionList.value[index]?.unreadCount || 0;
        if (index >= 0) {
            sessionList.value[index].unreadCount = 0;
        }
        return unreadCount;
    };
    const updateSessionLastActiveTime = (roomId: number, room?: SessionItem) => {
        const index = sessionList.value.findIndex((item) => item.roomId == roomId);
        const session = sessionList.value[index];
        if (session) {
            Object.assign(session, { activeTime: Date.now() });
            sessionList.value = [session, ...sessionList.value];
        } else if (room) {
            const newItem = cloneDeep(room);
            newItem.activeTime = Date.now();
            sessionList.value = [newItem, ...sessionList.value];
        }
        sortAndUniqueSessionList();
    };
    const pushMsg = async (msg: MessageType) => {
        const current = messageMap.get(msg.message.roomId);
        current?.set(msg.message.id, msg);
        uni.$emit('updateMsgList');
        // 发完消息就要刷新会话列表，
        // 如果当前会话已经置顶了，可以不用刷新
        if (
            globalStore.currentSession &&
            globalStore.currentSession.roomId !== msg.message.roomId
        ) {
            let result = undefined;
            if (!current) {
                result = await getContactDetail({ id: msg.message.roomId });
            }
            updateSessionLastActiveTime(msg.message.roomId, result.data.data);
        }
        // 聊天记录计数
        if (currentRoomId.value !== msg.message.roomId) {
            const index = sessionList.value.findIndex((item) => item.roomId === msg.message.roomId);
            const item = sessionList.value[index];
            if (item) {
                item.unreadCount += 1;
                sessionList.value[index] = item;
            }
        }
    };
    /** 会话列表去重并排序 */
    const sortAndUniqueSessionList = () => {
        const temp: Record<string, SessionItem> = {};
        sessionList.value.forEach((item) => (temp[item.roomId] = item));
        sessionList.value.splice(0, sessionList.value.length, ...Object.values(temp));
        sessionList.value.sort((pre, cur) => cur.activeTime - pre.activeTime);
    };
    return {
        getSessionList,
        sessionList,
        chatMessageList,
        pushMsg,
        getMsgList,
        messageMap,
        currentRoomId,
        currentMessageOptions,
        markSessionRead
    };
});
