<template>
	<view class="login-container">
		<view class="login-header">
			<image class="logo" :src="logo" mode="aspectFit" />
			<view class="title">{{title}}</view>
			<view class="subtitle">欢迎回到您的社交论坛</view>
		</view>
		<view class="login-form-box">
			<u-form :model="form" ref="uForm" class="login-form">
				<u-form-item class="form-item">
					<view class="input-icon">
						<text class="iconfont icon-shouji"></text>
					</view>
					<u-input v-model="form.mobile" placeholder="请输入手机号" class="form-input" />
				</u-form-item>
				<u-form-item class="form-item">
					<view class="input-icon">
						<text class="iconfont icon-yanzhengma"></text>
					</view>
					<u-input v-model="form.code" placeholder="请输入验证码" class="form-input" />
					<u-button slot="right" size="mini" @click="getCode" class="code-btn">{{tips}}</u-button>
					<u-verification-code :seconds="60" @end="end" @start="start" ref="uCode" @change="codeChange">
					</u-verification-code>
				</u-form-item>
			</u-form>

			<view class="button-login">
				<u-button v-show="form.mobile && form.code" type="primary" @click="phoneLogin" class="login-btn">
					登录 / 注册
				</u-button>
				<u-button v-show="!form.mobile || !form.code" type="primary" class="login-btn disabled">
					登录 / 注册
				</u-button>
			</view>
		</view>
		<view class="login-footer">
			<view class="skip-login" @click="goIndex">
				<text>暂不登录，先去逛逛</text>
			</view>
			<view class="copyright">
				<text>林风社交论坛 · 开源版</text>
			</view>
		</view>
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

	const form = reactive({
		mobile: "",
		code: ""
	})
	const tips = ref('获取验证码')
	const logo = ref('')
	const title = ref(proxy.$c.miniappName)
	const uCode = ref(null)

	function goIndex() {
		uni.switchTab({
			url: "/pages/index/index"
		})
	}

	function getSysInfo() {
		proxy.$H.get("system/config").then(res => {
			if (res.code == 0) {
				logo.value = res.logo
			}
		})
	}

	function phoneLogin() {
		uni.showLoading({
			mask: true,
			title: '登录中'
		})
		proxy.$H.post("user/smsLogin", form).then(res => {
			uni.hideLoading()
			if (res.code == 0) {
				uni.setStorageSync("hasLogin", true)
				uni.setStorageSync("token", res.token)
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

	function codeChange(text) {
		tips.value = text
	}

	function getCode() {
		let phoneCodeVerification = /^[1][3-9][0-9]{9}$/
		if (form.mobile == '') {
			proxy.$u.toast('请输入手机号')
		} else if (!phoneCodeVerification.test(form.mobile)) {
			proxy.$u.toast('请输入规范的手机号')
		} else {
			if (uCode.value.canGetCode) {
				uni.showLoading({
					title: '正在获取验证码'
				})
				proxy.$H.post("user/sendSmsCode", {
					mobile: form.mobile
				}).then(res => {
					if (res.code == 0) {
						uCode.value.start()
					}
					proxy.$u.toast(res.msg)
					uni.hideLoading()
				})
			} else {
				proxy.$u.toast('倒计时结束后再发送')
			}
		}
	}

	function end() {}

	function start() {}

	onLoad(() => {
		getSysInfo()
	})
</script>

<style lang="scss" scoped>
	.login-container {
		min-height: 100vh;
		display: flex;
		flex-direction: column;
		background: linear-gradient(to bottom, #f8f9fa, #ffffff);
		padding: 0 50rpx;
	}

	.login-header {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding-top: 120rpx;
		margin-bottom: 80rpx;

		.logo {
			width: 180rpx;
			height: 180rpx;
			border-radius: 30rpx;
			box-shadow: 0 10rpx 30rpx rgba(0, 0, 0, 0.1);
		}

		.title {
			font-size: 48rpx;
			font-weight: 600;
			margin-top: 40rpx;
			color: #333;
			letter-spacing: 2rpx;
		}

		.subtitle {
			font-size: 28rpx;
			color: #999;
			margin-top: 20rpx;
		}
	}

	.login-form-box {
		background-color: #ffffff;
		border-radius: 20rpx;
		padding: 50rpx 40rpx;
		box-shadow: 0 10rpx 30rpx rgba(0, 0, 0, 0.05);

		.login-form {
			margin-bottom: 60rpx;

			.form-item {
				margin-bottom: 40rpx;
				display: flex;
				align-items: center;
				border-bottom: 1rpx solid #f0f0f0;
				padding-bottom: 20rpx;

				.input-icon {
					margin-right: 20rpx;

					.iconfont {
						font-size: 40rpx;
						color: #2979ff;
					}
				}

				.form-input {
					flex: 1;
				}

				.code-btn {
					background: #f5f7fa;
					color: #2979ff;
					border: none;
					font-size: 24rpx;
					padding: 0 30rpx;
					height: 70rpx;
					line-height: 70rpx;
					border-radius: 35rpx;
				}
			}
		}

		.button-login {
			margin-top: 60rpx;

			.login-btn {
				height: 90rpx;
				line-height: 90rpx;
				font-size: 32rpx;
				letter-spacing: 4rpx;
				border-radius: 45rpx;
				background: linear-gradient(to right, #2979ff, #5e9dff);
				box-shadow: 0 10rpx 20rpx rgba(41, 121, 255, 0.2);
				border: none;

				&.disabled {
					background: linear-gradient(to right, #a8c6ff, #c8d9f8);
					box-shadow: none;
				}
			}
		}
	}

	.login-footer {
		margin-top: auto;
		padding: 60rpx 0;
		display: flex;
		flex-direction: column;
		align-items: center;

		.skip-login {
			margin-bottom: 80rpx;

			text {
				color: #999;
				font-size: 28rpx;
				position: relative;

				&::before,
				&::after {
					content: "";
					position: absolute;
					top: 50%;
					width: 60rpx;
					height: 1rpx;
					background-color: #ddd;
				}

				&::before {
					left: -80rpx;
				}

				&::after {
					right: -80rpx;
				}
			}
		}

		.copyright {
			text {
				font-size: 24rpx;
				color: #bbb;
			}
		}
	}
</style>