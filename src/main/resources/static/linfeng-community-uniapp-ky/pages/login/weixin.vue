<template>
	<view class="login">
		<image class="logo" :src="logo"></image>
		<text class="txt1">申请获取以下权限</text>
		<text class="txt2">获取你的公开信息（昵称、头像等）</text>
		<u-button @click="login" :custom-style="btnStyle">授权登录</u-button>
		<text class="txt3" @click="goBack">暂不登录</text>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				btnStyle: {
					color: "#fff",
					backgroundColor: '#333333'
				},
				logo: ""
			};
		},
		onLoad() {
			this.getSysInfo();
		},
		methods: {
			goBack() {
				uni.reLaunch({
					url: '/pages/index/index'
				});
			},
			getSysInfo() {
				this.$H.get("system/miniConfig").then(res => {
					this.logo = res.logo;
				})
			},

			async login() {
				uni.showLoading({
					mask: true,
					title: '正在登陆'
				});
				var that = this;
				// let userInfo = await this.getUserProfile();
				let code = await this.getLoginCode();
				that.$H.post('user/miniWxlogin', {
					code: code,
					// username: userInfo.nickName,
					// avatar: userInfo.avatarUrl,
				}).then(res => {

					if (res.code === 0) {
						uni.setStorageSync("hasLogin", true);
						uni.setStorageSync("token", res.token);
						uni.switchTab({
							url: '/pages/index/index'
						});
						that.getUserInfo();
					}
					uni.hideLoading();
				})



			},


			getUserInfo() {
				this.$H.get("user/userInfo").then(res => {
					uni.setStorageSync("userInfo", res.result)
				})
			},
			getLoginCode() {
				return new Promise((resolve, reject) => {
					uni.login({
						provider: 'weixin',
						success: function(loginRes) {
							resolve(loginRes.code);
						}
					});
				});
			},
			// getUserProfile() {
			// 	return new Promise((resolve, reject) => {
			// 		wx.getUserProfile({
			// 			lang: 'zh_CN',
			// 			desc: '用于完善会员资料',
			// 			success: res => {
			// 				resolve(res.userInfo);
			// 			}
			// 		});
			// 	});
			// }

		}
	}
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
