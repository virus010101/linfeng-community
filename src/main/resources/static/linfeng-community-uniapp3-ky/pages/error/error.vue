<template>
	<view class="page">
		<view class="body">
			<text class="code">404</text>
			<text class="title">页面不存在</text>
			<text class="desc">请确认访问地址是否有误</text>

			<view class="notice">
				<view class="notice-head">
					<u-icon name="info-circle" size="30" color="#606266"></u-icon>
					<text class="notice-title">免责声明</text>
				</view>
				<text class="notice-text">本软件为开源版，非商用软件。开源社区可免费下载使用。</text>
				<view class="source" @click="copyGitee">
					<text class="source-url">https://gitee.com/virus010101/linfeng-community</text>
					<u-icon name="file-text" size="28" color="#606266"></u-icon>
				</view>
			</view>

			<u-button shape="circle" :custom-style="btnStyle" @click="goHome">
				<view class="btn-inner">
					<u-icon name="home" size="32" color="#ffffff"></u-icon>
					<text class="btn-label">返回首页</text>
				</view>
			</u-button>
		</view>
	</view>
</template>

<script setup>
	import { reactive } from 'vue'

	const btnStyle = reactive({
		color: '#fff',
		backgroundColor: '#333333',
		height: '88rpx',
		marginTop: '48rpx'
	})

	function goHome() {
		uni.switchTab({
			url: '/pages/index/index'
		})
	}

	function copyGitee() {
		uni.setClipboardData({
			data: 'https://gitee.com/virus010101/linfeng-community',
			success: () => {
				uni.showToast({
					title: '项目地址已复制',
					icon: 'none',
					duration: 2000
				})
			}
		})
	}
</script>

<style lang="scss" scoped>
	.page {
		min-height: 100vh;
		background: #f7f8fa;
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.body {
		width: 100%;
		padding: 80rpx 48rpx;
		box-sizing: border-box;
	}

	.code {
		display: block;
		text-align: center;
		font-size: 96rpx;
		line-height: 1;
		font-weight: 600;
		color: #1a1a1a;
		letter-spacing: 4rpx;
	}

	.title {
		display: block;
		text-align: center;
		margin-top: 24rpx;
		font-size: 36rpx;
		font-weight: 500;
		color: #1a1a1a;
	}

	.desc {
		display: block;
		text-align: center;
		margin-top: 16rpx;
		font-size: 28rpx;
		color: #909399;
	}

	.notice {
		margin-top: 64rpx;
		padding: 32rpx;
		background: #fff;
		border: 1rpx solid #eee;
		border-radius: 16rpx;
	}

	.notice-head {
		display: flex;
		align-items: center;
	}

	.notice-title {
		margin-left: 12rpx;
		font-size: 28rpx;
		font-weight: 500;
		color: #303133;
	}

	.notice-text {
		display: block;
		margin-top: 20rpx;
		font-size: 26rpx;
		line-height: 1.7;
		color: #606266;
	}

	.source {
		display: flex;
		align-items: center;
		margin-top: 24rpx;
		padding: 20rpx 24rpx;
		background: #f7f8fa;
		border-radius: 12rpx;
	}

	.source-url {
		flex: 1;
		margin-right: 16rpx;
		font-size: 24rpx;
		line-height: 1.5;
		color: #606266;
		word-break: break-all;
	}

	.btn-inner {
		display: flex;
		align-items: center;
		justify-content: center;
	}

	.btn-label {
		margin-left: 12rpx;
		font-size: 30rpx;
		color: #fff;
	}
</style>
