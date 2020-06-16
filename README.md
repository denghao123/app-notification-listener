# app-notification-listener
监听安卓任意app通知栏消息内容，并将内容发送到服务器数据库

## 背景
监听消息通知用处很多，比如大家乐见的监听微信/支付宝到账通知实现支付回调。经过几天折腾，写了这个demo, 打通了从APP（安卓）获取**任意**消息通知并发送到服务端（PHP+MySql）的完整流程。 

## 实现思路
继承安卓消息类`NotificationListenerService`，在调用`onListenerConnected`、`onNotificationPosted`、`onNotificationRemoved`等方法时执行监听逻辑。

## 效果预览

#### APP界面：
<img src="https://denghao.me/usr/uploads/2020/06/3107826462.gif" style="max-width:500px;width:100%;" />

#### 后台界面：
![app-notice-admin.png][2]

## 后台地址
[https://denghao.me/special/appNotice/list.php][4]

## 提醒
-  Android非我专业, 这个demo是自己参考各方资料后拼凑出来的，仅供学习使用。 
-  跑起APP需要安装一堆软件和依赖包，请作好心理准备。

## 联系我
-  [https://denghao.me][3]
-  Email: jie4038[at]qq.com


  [1]: https://denghao.me/usr/uploads/2020/06/1599131392.jpg
  [2]: https://denghao.me/usr/uploads/2020/06/831407762.png
  [3]: https://denghao.me
  [4]: https://denghao.me/special/appNotice/list.php
