import {rsaEncrypt,getTimestamp,getSign} from "../utils/EncryptUtil"
import {getCode,doAccountRegister} from '../api/common'
@Entry
@Component
struct Index {
  @State message: string = 'Hello World'
  build() {
    Row() {
      Column() {
        Button("加密").onClick(async ()=>{

        })
      }
      .width('100%')
    }
    .height('100%')
  }
}