import {md5} from '../utils/md5'
import {JSEncrypt} from "../utils/jsencrypt/lib/index"
import {PUB_KEY} from "../common/Constant"
export const getTimestamp = ()=>{
  return Math.floor(Date.now() / 1000);
}
export const rsaEncrypt = (text:string)=>{
  return new Promise((resolve, reject) => {
    let encrypt = new JSEncrypt();
    encrypt.setPublicKey(PUB_KEY)
    let result = encrypt.encrypt(text)
    resolve(result)
  })
}

export const getSign = (param,key)=>{

  let sortParam = Object.keys(param).sort();

  let signature = ""

  for (const element of sortParam) {
    let val = param[element]
    if(val == "" || val == null) continue

    signature += element + "=" + param[element] + "&"

  }

  signature = signature.substring(0,signature.length - 1)

  signature += key

  return md5(signature)
}
