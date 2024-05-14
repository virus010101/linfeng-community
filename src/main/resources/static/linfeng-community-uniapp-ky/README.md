### 一、启动方式

林风社交论坛用户端linfeng-community-uniapp-ky启动方式：

- 将项目导入HBuilderX编辑器，初次使用请在项目根目录执行npm install安装依赖，一定要执行npm install安装uview依赖，才能运行。


- 运行到H5端请在HBuilderX执行`运行`—>`运行到浏览器`


- 运行到微信小程序需要注意，先在`mainfest.json`—>`微信小程序配置`，设置小程序APPID，然后再`运行`—>`运行到小程序模拟器`—>`微信开发者工具`


- 启动后台前端vue项目，在后台管理系统的配置中心配置小程序AppID和密钥后，才能在uniapp用户端实现微信登录。

- 在后台管理系统还需要配置阿里云或者七牛云云存储信息，才能在uniapp用户端实现图片的上传。
- 注意：开源版适配H5和微信小程序端。



### 二、简介

**标准版和开源版的区别**：

https://net.linfeng.tech/version/version.html

开源版运行效果：

 [查看开源版移动端效果图](https://pic.linfeng.tech/test/20230724/7bace9859cef4e2ca4a23a4a687a248e.gif)

了解更多请查看：

[林风社交论坛官网](https://net.linfeng.tech) 