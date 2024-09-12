import { ref } from 'vue';
import { defineStore } from 'pinia';

export enum LoginStatus {
    Init,
    Waiting,
    Success
}

export const useWsLoginStore = defineStore('wsLogin', () => {
    const loginStatus = ref(LoginStatus.Init);

    function resetLoginState() {
        loginStatus.value = LoginStatus.Init;
    }

    return {
        loginStatus,
        resetLoginState
    };
});
