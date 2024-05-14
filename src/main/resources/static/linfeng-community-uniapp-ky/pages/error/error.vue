<template>
	<view class="error">
		<text class="title">页面迷路啦~</text>
		<text class="info">请确认访问地址是否有误</text>
		<u-button type="primary" class="btn-home" @click="goHome">返回首页</u-button>
		<!-- #ifdef MP-WEIXIN -->
		<u-button type="success" @click="goWebsite">前往官网演示</u-button>
		<!-- #endif -->
		<!-- #ifdef H5 -->
		<u-button type="success" @click="goWebsite">前往官网</u-button>
		<!-- #endif -->
		
	</view>
</template>
<script>
	export default {
		data() {
			return {

			};
		},
		methods: {
			goHome() {
				uni.switchTab({
					url: '/pages/index/index'
				})
			},
			goWebsite() {
				// #ifdef H5
				window.open("https://net.linfeng.tech")
				// #endif
				// #ifdef MP-WEIXIN
				uni.navigateToMiniProgram({
					appId: 'wx1d4a8967c92bda9b',
					path: 'pages/index/index',
					success: res => {
						console.log("打开成功", res);
					},
					fail: err => {
						console.log(err);
					}
				})
				// #endif

			}

		}
	}
</script>
<style lang="scss" scoped>
	.error {
		display: flex;
		align-items: center;
		justify-content: center;
		flex-direction: column;
		height: 80vh;

		.title {
			margin-bottom: 100rpx;
			color: #aaaaff;
			font-weight: 600;
			font-size: 60rpx;
		}

		.info {
			margin-bottom: 20rpx;
			color: #333;
			font-weight: 500;
			font-size: 40rpx;
		}

		.btn-home {
			margin-bottom: 40rpx;
		}
	}
</style>