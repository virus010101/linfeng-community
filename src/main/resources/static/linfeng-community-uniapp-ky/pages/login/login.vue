<template>
	<view class="lf-login-register">
		<image class="logo" :src="logo" mode="aspectFit" />
		<view class="title">欢迎使用{{title}}</view>
		<u-form :model="form" ref="uForm">
			<u-form-item>
				<u-input v-model="form.mobile" placeholder="请输入手机号" />
			</u-form-item>
			<u-form-item>
				<u-input v-model="form.code" placeholder="请输入验证码" />
				<u-button slot="right" size="mini" @click="getCode">{{tips}}</u-button>
				<u-verification-code :seconds="60" @end="end" @start="start" ref="uCode" @change="codeChange">
				</u-verification-code>
			</u-form-item>
		</u-form>
		<view class="button-login">
			<u-button v-show="form.mobile && form.code" type="primary" @click="phoneLogin" shape="circle">登录/注册</u-button>
			<u-button v-show="!form.mobile || !form.code" type="success" shape="circle">登录/注册</u-button>
		</view>
		<view class="lf-bottom" @click="goIndex"><text>——— 暂不登录，再看看 ———</text></view>
		
	</view>
</template>

<script>
	export default {
		data() {
			return {
				form: {
					mobile: "",
					code: ""
				},
				tips: '验证码',
				logo: '',
				title: this.$c.miniappName
			};
		},
		onLoad() {
			this.getSysInfo();
		},
		methods: {
			goIndex(){
				uni.switchTab({
					url:"/pages/index/index"
				})
			},
			getSysInfo() {
				this.$H.get("system/config").then(res => {
					if (res.code == 0) {
						this.logo = res.logo;
					}
				})
			},
			phoneLogin() {
				uni.showLoading({
					mask: true,
					title: '登录中'
				});
				this.$H.post("user/smsLogin", this.form).then(res => {
					if (res.code == 0) {
						uni.setStorageSync("hasLogin", true);
						uni.setStorageSync("token", res.token);
						uni.setStorageSync("linfeng", "lf");
						uni.switchTab({
							url: '/pages/index/index'
						});
					}

					uni.hideLoading();
				})
			},
			codeChange(text) {
				this.tips = text;
			},
			getCode() {
				let phoneCodeVerification = /^[1][3-9][0-9]{9}$/;
				if (this.form.mobile == '') {
					this.$u.toast('请输入手机号');
				} else if (!phoneCodeVerification.test(this.form.mobile)) {
					this.$u.toast('请输入规范的手机号');
				} else {

					if (this.$refs.uCode.canGetCode) {
						uni.showLoading({
							title: '正在获取验证码'
						})
						this.$H.post("user/sendSmsCode", {
							mobile: this.form.mobile
						}).then(res => {
							if (res.code == 0) {
								this.$refs.uCode.start();
							}
							this.$u.toast(res.msg);
							uni.hideLoading();
						})
					} else {
						this.$u.toast('倒计时结束后再发送');
					}
				}
			},
			end() {},
			start() {}
		}
	}
</script>

<style lang="scss" scoped>
	.lf-login-register {
		padding: 20rpx 50rpx;
		text-align: center;
	}

	.logo {
		width: 200rpx;
		height: 200rpx;
		margin-top: 100rpx;
	}

	.title {
		font-size: 44rpx;
		font-weight: 600;
		margin-bottom: 50rpx;
	}

	.button-login {
		margin-top: 100rpx;
	}
	
	.lf-bottom {
		text-align: center;
		margin: 150rpx 0 0 0;
		color: #666;
	}
	
	.lf-bottom text {
		margin-left: 20rpx;
		color: #aaaaaa;
		font-size: 27rpx;
	}
	


</style>