# 概述
容联·七陌V7 平台访客端安卓SDK
对应v7-访客端sdk渠道，可提供聊天页面以及整套的咨询进线流程给客户使用
# 环境
## 语言：
JAVA

## Android开发环境：

- Android Studio 3.2以上
- minSdkVersion：19
- targetSdkVersion：29
- gradle插件版本：最低 build:gradle:3.0.0
- gradle版本：最低 gradle-4.1

## 依赖库：
**moorKFDemo**
-`com.github.bumptech.glide:glide:4.9.0``(可选)`
**moorSdk**
- com.github.7moor-tech:7moor_baseLib_sdk
  |-org.greenrobot:eventbus:3.2.0
  |-com.google.code.gson:gson:2.8.5
  |-com.qiniu:qiniu-android-sdk:8.4.0
  |-com.j256.ormlite:ormlite-core:5.1
  |- com.j256.ormlite:ormlite-android:5.1
# 架构设计
##  软件架构：
整体采用MVC模式架构搭建，业务代码纯JAVA语言开发，ui代码使用xml布局文件编写。

![image.png](https://cdn.nlark.com/yuque/0/2022/png/1289814/1655696470219-0ea32ee0-30ec-4f7e-96ce-e9ccef68c3b1.png#clientId=u36702b32-6344-4&crop=0&crop=0&crop=1&crop=1&from=paste&height=290&id=ucdf505fb&margin=%5Bobject%20Object%5D&name=image.png&originHeight=357&originWidth=596&originalType=binary&ratio=1&rotation=0&showTitle=false&size=51020&status=done&style=none&taskId=u98553065-2dd0-49a9-ab3f-0c7c54d08a6&title=&width=484)

- M对应Model，代表业务数据
- V对应View,代表视图
- C对应Controller，代表控制器。

## Moudle介绍：
### APP：
项目入口Application与MainActivity。
MainActivity页面提供了初始化参数配置以及初始化sdk的流程演示。
ImageLoader为暴露在外可以由客户自定义配置图片加载框架的实现类
### moorKFMoudle:
访客咨询聊天实际落地模块，主要包含了Chat页面，多样式的消息布局，表情管理，预加载以及 ui控件等
### v7_SDK:
核心管理模块，提供长连接ws能力，数据库，网络请求，事件推送等底层能力。
其中包含的baselib为内部开发的基础能力sdk。

## 依赖图：
![V7 sdk 架构+流程图.jpg](https://cdn.nlark.com/yuque/0/2022/jpeg/1289814/1655693191522-630129e9-5600-4eaa-9c4c-ab5ac59de448.jpeg#clientId=u6370f1c5-2f29-4&crop=0&crop=0&crop=1&crop=1&from=drop&height=605&id=u53cef992&margin=%5Bobject%20Object%5D&name=V7%20sdk%20%E6%9E%B6%E6%9E%84%2B%E6%B5%81%E7%A8%8B%E5%9B%BE.jpg&originHeight=1579&originWidth=2381&originalType=binary&ratio=1&rotation=0&showTitle=false&size=259989&status=done&style=none&taskId=u75e7d9ab-a294-44fe-aead-a5a876a0876&title=&width=913)
# 代码介绍摘要
## sdk Demo页面

- 会话页面：MoorChatFragment.java
- 满意度评价弹窗：MoorEvaluationDialog.java
- 会话内容类型item：com.moor.imkf.Demo.multichat.multirow包下

具体消息类型如下:

| MoorCSRReceviedViewBinder
  | 满意度评价类型消息 |
| --- | --- |
| MoorFastBtnReceivedViewBinder
  | 横向快速点击按钮类型消息 |
| MoorFileReceivedViewBinder
 MoorFileSendViewBinder
  | 附件收发类型消息 |
| MoorFlowListMultiSelectViewBinder
  | 机器人数据多选类型消息 |
| MoorFlowListSingleHorizontalViewBinder
  | 机器人数据单选类型横向滑动消息 |
| MoorFlowListSingleVerticalViewBinder
  | 机器人数据单选类型竖向滑动消息 |
| MoorFlowListTextViewBinder
  | 机器人数据单选类型竖向文字列表消息 |
| MoorFlowListTwoViewBinder
  | 机器人数据两列滑动按钮 |
| MoorImageReceivedViewBinder
 MoorImageSendViewBinder
  | 图片收发类型消息 |
| MoorLogisticsReceviedViewBinder
  | 机器人类型物流节点数据类型消息 |
| MoorOrderListSendViewBinder
  | 发送订单卡片类型消息 |
| MoorRobotUseFulReceivedViewBinder
  | 机器人有无帮助类型消息 |
| MoorSystemReceivedViewBinder
  | 系统提示类型消息 |
| MoorTextReceivedViewBinder
 MoorTextSendViewBinder
  | 文本收发类型消息 |
| MoorVoiceSendViewBinder
  | 语音类型消息 |
| MoorXbotTabQuestionViewBinder
  | 机器人常见问题分组类型消息 |

