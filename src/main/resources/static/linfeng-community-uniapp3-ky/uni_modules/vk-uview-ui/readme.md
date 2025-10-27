# vk-uview-ui

> 插件市场地址： [点击前往](https://ext.dcloud.net.cn/plugin?name=vk-uview-ui)

## `vk-uview-ui` 与 `uview-ui` 的关系？

`vk-uview-ui` 是 `VK` 基于 `uview-ui 1.8.3` 版本改造而来，其本质依然还是 `uview-ui`，是 `VK` 的珍藏版，由 `VK` 维护。

`uview-ui` 是 `vk-unicloud 快速开发框架` 之 `client端框架` 中推荐的UI，很多 `VK框架` 用户都在用  `uview-ui`

由于 `uview-ui` 作者目前重心全在 `uView 2.0` 的 `nvue` 版本上，故没有精力维护 `uView 1.0`，因此作者贡献出了自己珍藏的 `vk-uview-ui` 版本。
 
> 在这里，也感谢 `uview-ui` 作者的开源奉献，再次为开源点赞。 同时 `vk-uview-ui` 也是无条件开源。

## `vk-uview-ui` 与 `uview-ui 1.8.3` 功能上有什么区别？

* 1、最大的亮点：同时支持 `Vue3.0` 和 `Vue2.0`，你没看错，现在 `uview-ui` 支持 `Vue3.0` 了（这应该是目前为数不多的 `uniapp Vue3.0` 组件库）（2021-11-18）
* 2、以 `uni_modules` 模块形式发布，方便一键更新（同时组件会自动按需加载，无需手动写 `easycom` 规则（注意，你需要把之前写的 `easycom` 规则删除，在项目根目录的 `pages.json` 中删除）
* 3、对部分组件进行了微调，其中大部分调整不会影响你之前的代码运行（如车牌号键盘点击1次中文后会自动切英文+数字键盘），但有以下调整可能会影响你代码运行，请看完后再考虑是否有必要使用。

* * 3.1、`u-picker` 的选择省市区的静态数据源使用 unicloud 中 `opendb-city-china` 表内的数据生成（这样前后端地址数据源保持了一致），但由于数据源变动了，可能会对你有些影响。同时该组件还新增了 `addressDiscern` 方法 可用于智能识别收货信息,如在 `张三 13888888888 上海市嘉定区希望路1333弄是啊我庭12号楼1203` 中识别姓名、手机号、地址（支持多种格式）
* * 3.2、删除了 `uview-ui` 内置的 `国际化语言` 功能，（改造成 `Vue3` 比较麻烦，故作者干脆直接删除了）
* * 3.3、还有其他一些细微变动（这个版本是几个月前就已经改造完的，在 uniapp 刚出Vue3的时候就开始了，但是由于当时的 uniapp Vue3 bug真的太多，当时并没有发布，可以想象一下作者当时碰到bug满天飞的场景）
* * 3.4、Vue3.0中， uniapp 的 Vue3 版本在H5 APP（非nvue）微信小程序 中兼容性尚可，其他小程序目前还不行。

> [vk-uview-ui文档](https://vkuviewdoc.fsq.pub/components/icon.html)

___目前大的问题应该没有了，作者自己的项目也是用此组件库开发，因此你无需担心后期无人维护。___

如果你了解了以上须知，那么请继续阅读快速上手。

## 快速上手之Vue2.0

即使是 `Vue2` 版本，同样比原版 `uView1.0` 做了一些优化，如（车牌号键盘点击1次中文后会自动切英文，倒计时支持毫秒等等）

支持：H5、App(vue)、微信小程序、支付宝小程序（其他小程序未测试）

* 1、main.js 引入 vk-uview-ui 

```js
import uView from './uni_modules/vk-uview-ui';
Vue.use(uView);
```

* 2、App.vue 引入基础样式（注意style标签需声明scss属性支持）

```html
<style lang="scss">
	@import "./uni_modules/vk-uview-ui/index.scss";
</style>
```

* 3、uni.scss 引入全局 scss 变量文件

```css
@import "@/uni_modules/vk-uview-ui/theme.scss";
```

## 快速上手之Vue3.0

不建议把老项目 升级到 Vue3.0 (升级非常麻烦，建议新项目才考虑是否使用Vue3.0)

支持：H5、App(vue)、微信小程序（其他小程序未测试）

目前 发现Vue3 H5版本一个重大bug（与此UI框架无关），调试开发没问题，发布到前端托管时样式会错乱，已确定是HBX的问题，等待HBX修复此问题。（hbx3.3.0版本已修复此问题）

* 1、前置步骤：修改 `manifest.json` 内的 `vue` 版本为 `vue3`
* 2、项目根目录新增 `index.html` 文件，文件代码为
```html
<!DOCTYPE html>
<html lang="zh-CN">
	<head>
		<meta charset="UTF-8" />
		<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0" />
		<title></title>
		<!--preload-links-->
		<!--app-context-->
		<!-- 配置H5的 web图标static/logo.png -->
		<link rel="icon" href="./static/logo.png" />
	</head>
	<body>
		<div id="app">
			<!--app-html-->
		</div>
		<script type="module" src="/main.js"></script>
	</body>
</html>
```

* 3、main.js 引入 vk-uview-ui 

```js
// 引入 uView UI
import uView from './uni_modules/vk-uview-ui';

import { createSSRApp } from 'vue'

export function createApp() {
  const app  = createSSRApp(App)
  
  // 使用 uView UI
  app.use(uView)
  
  return { app }
}

```

* 4、App.vue 引入基础样式（注意style标签需声明scss属性支持）

```html
<style lang="scss">
	@import "./uni_modules/vk-uview-ui/index.scss";
</style>
```

* 5、uni.scss 引入全局 scss 变量文件

```css
@import "@/uni_modules/vk-uview-ui/theme.scss";
```


