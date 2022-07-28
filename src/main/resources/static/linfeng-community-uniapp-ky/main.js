import App from './App'
import store from "./store"
import uView from "uview-ui";
import config from './utils/config.js';
import request from './utils/request.js';

Vue.use(uView);
// 挂载Vuex
Vue.prototype.$store = store;
// 配置文件
Vue.prototype.$c = config;
// http请求
Vue.prototype.$H = request;

// #ifndef VUE3
import Vue from 'vue'
Vue.config.productionTip = false
App.mpType = 'app'
const app = new Vue({
    ...App,
	store
})
app.$mount()
// #endif

// #ifdef VUE3
import { createSSRApp } from 'vue'
export function createApp() {
  const app = createSSRApp(App)
  return {
    app
  }
}
// #endif