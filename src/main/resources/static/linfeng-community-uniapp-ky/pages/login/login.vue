<template>
	<view class="login-register">
		<view class="title">请先登录/注册</view>
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
			<u-button v-show="form.mobile && form.code" type="success" @click="phoneLogin" shape="circle">登录</u-button>
			<u-button v-show="!form.mobile || !form.code" type="default" shape="circle">登录</u-button>
		</view>
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
				tips: '验证码'
			};
		},
		methods: {
			phoneLogin() {
				uni.showLoading({
					mask: true,
					title: '登录中'
				});
				this.$H.post("user/smsLogin", this.form).then(res => {
					if (res.code == 0) {
						uni.setStorageSync("hasLogin", true);
						uni.setStorageSync("token", res.token);
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
				if(this.form.mobile==''){
					this.$u.toast('请输入手机号');
				}else if(!phoneCodeVerification.test(this.form.mobile)){
					this.$u.toast('请输入规范的手机号');
				}else{

				if (this.$refs.uCode.canGetCode) {
					
					// 模拟向后端请求验证码
					uni.showLoading({
						title: '正在获取验证码'
					})

					this.$H.post("user/sendSmsCode", {
						mobile: this.form.mobile
					}).then(res => {
						if (res.code == 0) {
							uni.hideLoading();
							this.$refs.uCode.start();
							this.$u.toast(res.msg);
							
						}
					})
				} else {
					this.$u.toast('倒计时结束后再发送');
				}
				}
			},
			end() {
			},
			start() {
			}
		}
	}
</script>

<style lang="scss" scoped>
	.login-register{
		padding: 20rpx 50rpx;
	}
	
	.button-login{
		margin-top: 100rpx;
	}
	
	.title{
		font-size: 40rpx;
		font-weight: 600;
		margin-bottom: 50rpx;
	}
</style>
