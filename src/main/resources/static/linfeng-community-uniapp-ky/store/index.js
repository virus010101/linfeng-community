import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

const store = new Vuex.Store({
	state: {
		hasLogin: uni.getStorageSync("hasLogin"),
	},
	mutations: {
		login(state, userInfo) {			
			state.hasLogin = uni.getStorageSync("hasLogin");
		},
		logout(state) {
			state.hasLogin = false;
		}
	}
})

export default store
