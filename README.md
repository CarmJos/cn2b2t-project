# cn2b2t插件集合

[![ci](https://github.com/CarmJos/cn2b2t-parent/actions/workflows/maven.yml/badge.svg?branch=master)](https://github.com/CarmJos/cn2b2t-parent/actions/workflows/maven.yml)

cn2b2t服务器基础插件代码集合。

## 相关介绍

<img src="https://github.com/CarmJos/cn2b2t-parent/blob/master/_img/2b2t.png" />

本项目为讽刺China2B2T的圈钱行为而创立；后为向cn2b2t玩家提供良好的游玩环境，创造有中国特色的仿2b2t类生存服务器而继续维护。

在2019年因运营结构改变，cn2b2t被迫关服，并开放相关代码。

## 相关视频

### 来自开发者
- [China2b2t?不如我20分钟写的cn2b2t，真正无管理!](https://www.bilibili.com/video/BV15t411J7FA)
- [China2B2T处理问题的态度令人发指！国服不能有良性竞争？！](https://www.bilibili.com/video/BV1st411K7es)
- [听说2b2t是在抖音上突然火的？](https://www.bilibili.com/video/BV17t41177VP)
- [绝不走China2B2T的死路！高中程序员最终决定将CN2B2T转型！](https://www.bilibili.com/video/BV1Kt411P7F4)

### 来自其他UP主

- [【Minecraft】无政府无管理cn2b2t](https://www.bilibili.com/video/BV1Nt411M7xj) `是奥SA`
- [【CN2B2T】10多了个人在服务器生存了8天会发生什么？](https://www.bilibili.com/video/BV1E4411X73h) `是奥SA`
- [我的世界-cn2t2b服务器生存日记](https://www.bilibili.com/video/BV12J411u7Em) `咸鱼菜鸟c`
- [CN2b2t生存日记<第一集>](https://www.bilibili.com/video/BV1wE411D7vd) `StrugglingPotato`
- [cn2b2t新家园开辟成果](https://www.bilibili.com/video/BV1KE411R7Jf) `Jason_Stephen`

## 项目结构

### 2b2t-proxy

适用于cn2b2t的独立Proxy核心插件，适配墨瓷的相关系统。

- 登入服务器用户判断
- 分IP设置 MOTD消息、协议版本、在线人数与图标
- 提供维护模式设置
- 提供玩家传送的相关设定修正(适配翼龙)
- 提供Proxy端的用户数据读取

### 2b2t-core

提供cn2b2t的相关核心接口代码，来源于 `InkKettle` 项目。

- 快捷的GUI创建API
- 用户相关API
- 快捷的队伍管理API
- 快捷计分板API
- 数据库访问接口API
- 用户前缀、后缀显示API
- 颜色转换、时间转换、UUID转换等实用工具

### 2b2t-common

提供cn2b2t中部分基础的功能。

- 自动拓展世界边界
- 自动发放公告
- 定期自动重启(并支持赞助的人延迟)
- 聊天监听并根据赞助金额提供不同前缀颜色
- 计分板更新
- 捐赠系统与GUI
- 屏蔽其他玩家聊天消息的指令
- 个人设定相关指令
- 新玩家物品与保护

### 2b2t-logger

记录cn2b2t中玩家的相关行为。

#### 主要接口

```java
public class LoggerAPITest {
    public void playerLog(Player player) {
        LoggerManager.log(LoggerManager.LogType.CHAT, player, "这是一个log的测试");
    }
}
```

### 2b2t-death

修复(可能发生的)死亡复活点异常的问题。

### 2b2t-spawn

提供cn2b2t中的出生点保护。

## 支持与捐赠

若您觉得本插件做的不错，您可以捐赠支持我！

感谢您成为开源项目的支持者！

<img height=25% width=25% src="https://raw.githubusercontent.com/CarmJos/CarmJos/main/img/donate-code.jpg" />

## 开源协议

本项目源码采用 [GNU General Public License v3.0](https://opensource.org/licenses/GPL-3.0) 开源协议。
> ### 关于 GPL 协议
> GNU General Public Licence (GPL) 有可能是开源界最常用的许可模式。GPL 保证了所有开发者的权利，同时为使用者提供了足够的复制，分发，修改的权利：
>
> #### 可自由复制
> 你可以将软件复制到你的电脑，你客户的电脑，或者任何地方。复制份数没有任何限制。
> #### 可自由分发
> 在你的网站提供下载，拷贝到U盘送人，或者将源代码打印出来从窗户扔出去（环保起见，请别这样做）。
> #### 可以用来盈利
> 你可以在分发软件的时候收费，但你必须在收费前向你的客户提供该软件的 GNU GPL 许可协议，以便让他们知道，他们可以从别的渠道免费得到这份软件，以及你收费的理由。
> #### 可自由修改
> 如果你想添加或删除某个功能，没问题，如果你想在别的项目中使用部分代码，也没问题，唯一的要求是，使用了这段代码的项目也必须使用 GPL 协议。
>
> 需要注意的是，分发的时候，需要明确提供源代码和二进制文件，另外，用于某些程序的某些协议有一些问题和限制，你可以看一下 @PierreJoye 写的 Practical Guide to GPL Compliance 一文。使用 GPL 协议，你必须在源代码代码中包含相应信息，以及协议本身。
>
> *以上文字来自 [五种开源协议GPL,LGPL,BSD,MIT,Apache](https://www.oschina.net/question/54100_9455) 。*
