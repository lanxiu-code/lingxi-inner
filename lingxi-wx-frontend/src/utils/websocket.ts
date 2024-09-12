import type { WsReqMsgContentType } from '@/types/wsType';

import worker from './worker';

class WS {
    #tasks: WsReqMsgContentType[] = [];
    // 重连🔐
    #connectReady = false;

    constructor() {
        this.initConnect();
        // 收到消息
    }

    initConnect = () => {
        const token = uni.getStorageSync('token');
        if (!token) return;
        worker.initConnection(token);
    };

    // 关闭websocket
    closeWebSocket = async () => await uni.closeSocket();
    // 重置一些属性
    #onClose = () => {
        this.#connectReady = false;
    };

    #dealTasks = () => {
        this.#connectReady = true;
    };

    #send(msg: WsReqMsgContentType) {
        worker.connectionSend(
            `{"type":"message","value":${typeof msg === 'string' ? msg : JSON.stringify(msg)}}`
        );
    }

    send = (params: WsReqMsgContentType) => {
        if (this.#connectReady) {
            this.#send(params);
        } else {
            // 放到队列
            this.#tasks.push(params);
        }
    };
}
const ws = new WS();
export default ws;
