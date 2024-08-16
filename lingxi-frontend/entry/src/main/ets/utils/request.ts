// 引入包名
import http from '@ohos.net.http';
import appManager from '@ohos.app.ability.appManager';
import {BASE_URL} from '../common/Constant'
let httpRequest = http.createHttp();
// 用于订阅HTTP响应头，此接口会比request请求先返回。可以根据业务需要订阅此消息
// 从API 8开始，使用on('headersReceive', Callback)替代on('headerReceive', AsyncCallback)。 8+
httpRequest.on('headersReceive', (header) => {
  console.info('header: ' + JSON.stringify(header));
});
const request = (url,requestHeader)=>{
  const header = {
    'Content-Type': 'application/json',
  }
  const token = AppStorage.Get("token")
  if(token){
    header['Authorization'] = `Bearer ${token}`
  }
  return new Promise((resolve,reject)=>{
    httpRequest.request(
      BASE_URL+url,
      {
        ...requestHeader,
        header,
        readTimeout: 60000,
        connectTimeout: 60000
      },
      (err, data) => {
      if (!err) {
        resolve(data)
      } else {
        reject(err)
        httpRequest.destroy();
      }
    }
    );
  })
}

export default request
