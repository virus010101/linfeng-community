import { createStore } from 'vuex'

const store = createStore({
	state() {
		return {
			hasLogin: uni.getStorageSync("hasLogin"),
		}
	},
	mutations: {
		login(state, userInfo) {
			state.hasLogin = uni.getStorageSync("hasLogin")
		},
		logout(state) {
			state.hasLogin = false
		}
	}
})

export default store
