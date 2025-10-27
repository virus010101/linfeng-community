import App from './App'
import store from "./store"
import config from './utils/config.js';
import request from './utils/request.js';
import { createSSRApp } from 'vue'
import uView from './uni_modules/vk-uview-ui';




export function createApp() {
  const app = createSSRApp(App)
  // 使用 store
  app.use(store)
  // 使用 uView UI
  app.use(uView)
  
  app.config.globalProperties.$H = request
  app.config.globalProperties.$store = store
  app.config.globalProperties.$c = config
  return {
    app
  }
}
