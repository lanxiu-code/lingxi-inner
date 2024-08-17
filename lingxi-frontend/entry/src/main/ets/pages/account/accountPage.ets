@Extend(Column) function container(){
  .width("100%")
  .height("100%")
}
@Extend(Column) function header(){
  .padding(20)
  .backgroundImage($r("app.media.bg"),ImageRepeat.NoRepeat)
  .backgroundImageSize({width:"100%",height:"100%"})
  .backgroundImagePosition(Alignment.Center)
  .justifyContent(FlexAlign.End)
  .width("100%")
}
@Extend(Column) function content(){
  .padding(20)
  .width("100%")
}
@Extend(Image) function userAvatar(){
  .width(80)
  .borderRadius(50)
}
@Extend(Text) function nickName(){
  .fontWeight(FontWeight.Bold)
  .fontSize(20)
  .fontColor("white")
}
@Extend(Text) function tags(){
  .backgroundColor("#accbee")
  .fontWeight(FontWeight.Bold)
  .fontSize(15)
  .fontColor("white")
  .padding(3)
}
@Extend(Text) function userProfile(){
  .fontColor("white")
  .fontWeight(FontWeight.Bold)
}
@Extend(Row) function item(){
  .padding({bottom:10})
  .width("95%")
  .border({width:{bottom:1}})
  .borderColor("#e2e2e2")
  .margin({bottom:10})
}
@Extend(Text) function textCommon(){
  .fontColor("white")
  .fontWeight(FontWeight.Bold)
}
@Styles function widthStyle(){
  .width("100%")
}

@Entry
@Component
struct AccountPage {
  @State message: string = 'Hello World'
  itemList:any[] = [
    {id:1,name:"标签管理"},
    {id:2,name:"已加入队伍"},
    {id:3,name:"已创建队伍"},
  ]
  build() {
    Column({space:10}){
      Column({space:10}){
        Row(){
          Column(){
            Image($r("app.media.avatar")).userAvatar()
          }
          Column({space:8}){
            Text("蓝朽").nickName()
            Text("java").tags()
          }.height(50).width(50)
        }.widthStyle()
        Row(){
          Text("这个人很懒什么都没留下").userProfile()
        }.widthStyle()
        Row({space:10}){
          Column(){
            Row({space:10}){
              Text("关注：20").textCommon()
              Text("粉丝：10").textCommon()
            }.widthStyle()
          }.layoutWeight(1)
          Column(){
            Image($r("app.media.setting"))
              .width(25).height(25)
          }
          .layoutWeight(1)
          .alignItems(HorizontalAlign.End)
          .onClick(()=>{
            // todo 跳转到用户信息页
          })
        }
        .widthStyle()
      }.header()
      Column(){
        ForEach(this.itemList,(item:any,index:number)=>{
          Row(){
            Column(){
              Text(item.name)
            }
            .alignItems(HorizontalAlign.Start)
            .layoutWeight(1)
            Column(){
              Image($r("app.media.right"))
                .width(20)
                .height(20)
            }
            .alignItems(HorizontalAlign.End)
            .layoutWeight(1)
          }.item().onClick(()=>{
            // todo 跳转到对应页面
            console.log(item.id)
          })
        })
        Button("退出登录")
          .backgroundColor("#797979")
          .height(40)
          .width("100%")
          .onClick(()=>{
            // todo 退出登录
          })
      }.content()
    }.container()
  }
}

