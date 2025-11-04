<template>
	<view class="login">
		<image class="logo" :src="logo"></image>
		<text class="txt1">申请获取以下权限</text>
		<text class="txt2">获取你的登录信息</text>
		<u-button @click="login" :custom-style="btnStyle">一键登录</u-button>
		<text class="txt3" @click="goBack">暂不登录</text>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance
	} from 'vue'
	import {
		onLoad
	} from '@dcloudio/uni-app'

	const {
		proxy
	} = getCurrentInstance()

	const btnStyle = reactive({
		color: "#fff",
		backgroundColor: '#333333'
	})
	const logo = ref("")

	function goBack() {
		uni.reLaunch({
			url: '/pages/index/index'
		})
	}

	function getSysInfo() {
		proxy.$H.get("system/config").then(res => {
			if (res.code == 0) {
				logo.value = res.logo
			}
		})
	}

	async function login() {
		uni.showLoading({
			mask: true,
			title: '正在登录中'
		})
		let code = await getLoginCode()
		proxy.$H.post('user/miniWxlogin', {
			code: code
		}).then(res => {
			uni.hideLoading()
			if (res.code === 0) {
				uni.setStorageSync("hasLogin", true)
				uni.setStorageSync("token", res.token)
				getUserInfo()
				uni.switchTab({
					url: '/pages/index/index'
				})
			} else if (res.code == 701) {
				uni.showToast({
					title: res.msg,
					icon: "none",
					duration: 2000
				})
			}
		})
	}

	function getUserInfo() {
		proxy.$H.get("user/userInfo").then(res => {
			uni.setStorageSync("userInfo", res.result)
		})
	}

	function getLoginCode() {
		return new Promise((resolve, reject) => {
			uni.login({
				provider: 'weixin',
				success: function(loginRes) {
					resolve(loginRes.code)
				}
			})
		})
	}

	onLoad(() => {
		getSysInfo()
	})
</script>

<style lang="scss">
	.login {
		display: flex;
		flex-direction: column;
		padding: 100rpx;
	}

	.login .logo {
		width: 230rpx;
		height: 230rpx;
		margin: 50rpx auto;
	}

	.login .txt1 {
		margin-bottom: 10rpx;
	}

	.login .txt2 {
		color: #999;
		margin-bottom: 50rpx;
	}

	.login .txt3 {
		color: #8c8c8c;
		margin-top: 30rpx;
		text-align: center;
	}
</style>