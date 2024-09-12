import { ws_url } from '@/constants/CommonConstant';
import type { LoginSuccessResType, WsReqMsgContentType, OnStatusChangeType } from '@/types/wsType';
import { WsResponseMessageType } from '@/types/wsType';
import type { MessageType } from '@/types/types';
import { OnlineEnum } from '@/enum';
import { useWsLoginStore, LoginStatus } from '@/store/ws';
import { useUserStore } from '@/store/user';
import { useChatStore } from '@/store/chat';
import { useGlobalStore } from '@/store/global';
class Worker {
    // 心跳 timer
    heartTimer: number | null = null;
    // 重连次数上限
    reconnectCountMax = 10;
    reconnectCount = 0;
    // 重连 timer
    timer: null | number = null;
    // 重连🔐
    lockReconnect = false;
    // 重连🔐
    token: null | string = null;

    // 发消息给主进程
    postMsg = ({ type, value }: { type: string; value?: object }) => {
        //self.postMessage(JSON.stringify({ type, value }));
    };
    // 往 ws 发消息
    connectionSend = (value: any) => {
        uni.sendSocketMessage({
            data: JSON.stringify(value)
        });
    };

    // 发送心跳 10s 内发送
    sendHeartPack = () => {
        // 10s 检测心跳
        this.heartTimer = setInterval(() => {
            // 心跳消息类型 2
            this.connectionSend({ type: 2 });
        }, 9900);
    };
    // 清除❤️跳 timer
    clearHeartPackTimer = () => {
        if (this.heartTimer) {
            clearInterval(this.heartTimer);
            this.heartTimer = null;
        }
    };

    onCloseHandler = () => {
        this.clearHeartPackTimer();
        // 已经在连接中就不重连了
        if (this.lockReconnect) return;

        // 标识重连中
        this.lockReconnect = true;

        // 清除 timer，避免任务堆积。
        if (this.timer) {
            clearTimeout(this.timer);
            this.timer = null;
        }
        // 达到重连次数上限
        if (this.reconnectCount >= this.reconnectCountMax) {
            this.reconnectCount = 0;
            return;
        }

        // 断线重连
        this.timer = setTimeout(() => {
            this.initConnection(this.token as string);
            this.reconnectCount++;
            // 标识已经开启重连任务
            this.lockReconnect = false;
        }, 2000);
    };

    // ws 连接 error
    onConnectError = () => {
        this.onCloseHandler();
        this.postMsg({ type: 'error' });
    };
    // ws 连接 close
    onConnectClose = () => {
        this.onCloseHandler();
        this.token = null;
        this.postMsg({ type: 'close' });
    };
    // ws 连接成功
    onConnectOpen = () => {
        this.postMsg({ type: 'open' });
        // 心跳❤️检测
        this.sendHeartPack();
    };
    // ws 连接 接收到消息
    onConnectMsg = (e: any) => {
        console.log(e);
        const loginStore = useWsLoginStore();
        const userStore = useUserStore();
        const chatStore = useChatStore();
        const params: { type: string; value: unknown } = JSON.parse(e.data);
        switch (params.type) {
            // 登录成功
            case WsResponseMessageType.LoginSuccess: {
                const { token, ...rest } = params.data as LoginSuccessResType;
                uni.setStorageSync('user_info', JSON.stringify(rest));
                loginStore.loginStatus = LoginStatus.Success;
                // 获取会话列表
                chatStore.getSessionList(true);
                break;
            }
            // 收到消息
            case WsResponseMessageType.ReceiveMessage: {
                chatStore.pushMsg(params.data as MessageType);
                break;
            }
            // 用户下线
            case WsResponseMessageType.OnOffLine: {
                const data = params.data as OnStatusChangeType;
                break;
            }
            // 用户 token 过期
            case WsResponseMessageType.TokenExpired: {
                //userStore.loginUser = {};
                uni.clearStorageSync();
                loginStore.loginStatus = LoginStatus.Init;
                uni.reLaunch({
                    url: '/pages/login/login'
                });
                break;
            }
            default: {
                console.log('接收到未处理类型的消息:', params);
                break;
            }
        }
    };
    // 初始化 ws 连接
    initConnection = (token: string) => {
        console.log(token);
        this.token = token;
        // 建立链接
        uni.connectSocket({
            url: `${ws_url}${this.token ? `?token=${this.token}` : ''}`
        }).then((res) => {
            uni.reLaunch({
                url: '/pages/home/home'
            });
        });
        // 关闭连接
        uni.onSocketClose(this.onConnectClose);
        // 收到消息
        uni.onSocketMessage(this.onConnectMsg);
        // 建立链接
        uni.onSocketOpen(this.onConnectOpen);
        // 连接错误
        uni.onSocketError(this.onConnectError);
    };
}
const worker = new Worker();
export default worker;
