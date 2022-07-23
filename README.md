# linfeng-community

#### 1.介绍

社交论坛uniapp小程序/H5版本基于SpringBoot2+MybatisPlus+Shiro+jwt+Redis+Vue+Uniapp的前后端分离的社交论坛问答发帖/BBS项目 项目分为Uniapp用户端（兼容H5和微信小程序版本）和Vue后台管理端（包括完整的权限处理）， 基于以下技术栈开发：SpringBoot2、MybatisPlus、Shiro、jwt、Redis、Vue、Uniapp、MySQL5.7。

#### 2.软件演示

##### 2.1移动端H5演示版本（手机扫码浏览/输入手机号登录）https://www.linfeng.tech

![移动端H5二维码](https://github.com/virus010101/linfeng-community/blob/main/images/er.png)

后台管理系统演示地址：https://dev.linfeng.tech

账号 ： test         密码：  123456

##### 2.2移动端效果截图

<img src="https://github.com/virus010101/linfeng-community/blob/main/images/showPic11.png"/>

![](https://github.com/virus010101/linfeng-community/blob/main/images/showPic12.png)

![](https://github.com/virus010101/linfeng-community/blob/main/images/showPic13.png)

![](https://github.com/virus010101/linfeng-community/blob/main/images/showPic14.png)

![](https://github.com/virus010101/linfeng-community/blob/main/images/showPic04.png)

##### 2.3后台管理端效果截图

![](https://github.com/virus010101/linfeng-community/blob/main/images/showPic05.png)

![](https://github.com/virus010101/linfeng-community/blob/main/images/showPic06.png)

![](https://github.com/virus010101/linfeng-community/blob/main/images/showPic07.png)

![](https://github.com/virus010101/linfeng-community/blob/main/images/showPic08.png)

![](https://github.com/virus010101/linfeng-community/blob/main/images/showPic09.png)

![](https://github.com/virus010101/linfeng-community/blob/main/images/showPic10.png)

![](https://github.com/virus010101/linfeng-community/blob/main/images/.png)

#### 3.安装教程 

1.  配置数据库和redis。先启动redis，再启动后端api服务
2.  数据库请使用MySQL5.7，其他版本可能会有问题
3.  配置后台前端  先npm install 下载依赖后，再npm run dev即可

#### 4.必看说明

1.**后台前端的代码**在**\src\main\resources\static\linfeng-community-vue**目录下！

2.**后台管理系统和后台管理系统后端API已经全部开源**。

**用户端代码未开源**，需要源码联系客服（收费分为mini版本和标准版）。

<img src="https://github.com/virus010101/linfeng-community/blob/main/images/mine.jpg" style="zoom: 25%;" />



3. SQL文件开源的，在QQ群，开源不易，**请左上角star后备注gitee的用户名加QQ群（640700429）获取**

<img src="https://github.com/virus010101/linfeng-community/blob/main/images/qrcode.jpg" style="zoom:25%;" />

#### 5.版本更新记录



***当前版本V1.4***

**V1.4发布**
2022.7.24

【新增】1.虚拟用户自动注册发帖 *【重磅】*
【新增】2.后台统一管理可配置项设置 *【重磅】*
【新增】3.后台管理系统首页增加echarts图和快捷入口 *【重磅】*
【新增】4.个人中心菜单在管理端可配置化 *【重磅】*
【新增】5.轮播图新增跳转链接包括外链和页面链接
【新增】6.新增帖子上下架系统消息通知的处理
【新增】7.新增圈子页公告栏通知
【优化】8.后台管理系统添加敏感操作日志记录
【优化】9.h5端登录页面重构升级 *【重磅】*
【修复】10.修复帖子列表由于置顶帖导致分页查询不全的问题
【修复】11.修复圈子解散和删除存在的bug



2022.7.1

**V1.3.2发布**

1.新增投票贴

2.私聊模块前端样式重构升级

3.系统通知消息统一管理

4.修复苹果机型无法格式化时间的问题

5.修复瀑布流模式下h5可能存在的不兼容

**V1.3.1**

<u>2022.6.8</u>

1.修复移动端分页功能存在的底部提示bug

2.修复圈子管理员修改圈子背景图无效的bug

3.修复圈子人数展示不统一的问题

4.修复一键已读为空时的异常报错

5.优化圈子查询代码

6.优化帖子分页查询组装代码

7.新增子评论回复时间展示

8.新增帖子列表组件置顶图标展示属性

9.新增发帖时选择话题

10.发帖时间改为新格式

11.新增帖子瀑布流展示风格组件

<u>2022.5.14</u>

**V1.3（正式版）发布**

1.优化私聊功能及时性

2.优化阿里云短信验证码和测试阶段的处理

3.优化话题标签删除导致的问题

4.新增用户端圈子管理员圈内删贴功能

5.新增微信小程序端支付

6.新增私信列表历史消息

7.新增付费贴

8.新增积分签到

9.新增余额钱包体系&用户积分体系

10.新增可配置消息清理定时任务

11.新增用户端父子评论长按删除

12.新增管理端模糊查询

13.新增后台管理端用户余额充值

14.修复私信排序问题和查询可能存在的问题

15.修复quartz定时任务暂停和恢复的bug

16.新增h5微信支付

17.新增视频模块和充值模块显示配置

18.新增积分兑换余额模块

<u>2022.5.1</u>

**V1.2.3发布**

1.优化私聊功能及时性

2.优化阿里云短信验证码和测试阶段的处理

3.优化话题标签删除导致的问题

4.新增用户端圈子管理员圈内删贴功能

5.新增微信小程序端支付

6.新增私信列表历史消息

7.新增付费贴

<u>2022.4.24</u>

**V1.2.2发布**

1.修复H5端回复评论bug； 

2.修复H5端分享图标布局； 

3.修复H5登录同步圈子人数；

4.优化H5端圈子页布局； 

5.优化无效token处理； 

6.新增H5端删除个人帖子选项；

<u>2022.04.17</u>

**V1.2.1发布**

1.新增防止重复提交注解

2.扩大匿名访问权限

3.优化小程序登录

4.新增后台前端面板统计数据

5.移动端用户手机号脱敏处理

6.新增帖子上下架处理



<u>2022.04.04</u>

**V1.2发布**

 1.优化私聊模块

 2.新增置顶功能

 3.优化UI 

4.优化消息已读未读的处理 

5.其他bug修复



<u>2022.03.05</u>

**V1.1发布**

移动端适配H5

移动端分享功能优化

移动端其他bug修复



<u>2022.01.29</u>

**V1.0发布**

