import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
	state: {
		hasLogin: uni.getStorageSync("hasLogin"),
		sessionKey: uni.getStorageSync("sessionKey"),
	},
	mutations: {
		login(state, userInfo) {			
			state.hasLogin = uni.getStorageSync("hasLogin");
			state.sessionKey = uni.getStorageSync("sessionKey");
		},
		logout(state) {
			state.hasLogin = false;
			state.sessionKey = null;
		}
	}
})

export default store
