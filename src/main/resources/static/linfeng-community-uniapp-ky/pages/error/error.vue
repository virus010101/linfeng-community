<template>
	<view class="error-container">
		<view class="background-decoration">
			<view class="circle-1"></view>
			<view class="circle-2"></view>
			<view class="circle-3"></view>
		</view>
		
		<view class="error-content">
			<view class="error-visual">
				<image class="error-img" src="/static/404.png" mode="aspectFit"></image>
				<view class="floating-elements">
					<view class="dot dot-1"></view>
					<view class="dot dot-2"></view>
					<view class="dot dot-3"></view>
				</view>
			</view>
			
			<view class="error-text">
				<text class="title">
					<text class="title-emoji">🧭</text>
					页面迷路啦~
				</text>
				<text class="subtitle">Oops! Page Not Found</text>
				<text class="description">请确认访问地址是否有误</text>
			</view>
			
			<view class="project-card">
				<view class="card-header">
					<view class="logo-placeholder">🌸</view>
					<view class="project-details">
						<text class="project-name">林风社交论坛开源版</text>
						<text class="project-desc">开源 · 免费 · 易用</text>
					</view>
				</view>
				<view class="project-link" @click="copyGitee">
					<text class="link-text">https://gitee.com/virus010101/linfeng-community</text>
					<view class="copy-icon">📋</view>
				</view>
			</view>
			
			<view class="action-buttons">
				<u-button type="primary" class="btn-primary" @click="goHome">
					<text class="btn-icon">🏠</text>
					返回首页
				</u-button>
				<!-- #ifdef MP-WEIXIN -->
				<u-button class="btn-secondary" @click="goWebsite">
					<text class="btn-icon">🌐</text>
					前往官网演示
				</u-button>
				<!-- #endif -->
				<!-- #ifdef H5 -->
				<u-button class="btn-secondary" @click="goWebsite">
					<text class="btn-icon">🌐</text>
					前往官网
				</u-button>
				<!-- #endif -->
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {};
		},
		methods: {
			goHome() {
				uni.switchTab({
					url: '/pages/index/index'
				})
			},
			goWebsite() {
				// #ifdef H5
				window.open("https://www.linfengtech.cn")
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
			},
			copyGitee() {
				uni.setClipboardData({
					data: 'https://gitee.com/virus010101/linfeng-community',
					success: () => {
						uni.showToast({
							title: '项目地址已复制到剪贴板 ✨',
							icon: 'none',
							duration: 2000
						});
					}
				});
			}
		}
	}
</script>

<style lang="scss" scoped>
	.error-container {
		position: relative;
		min-height: 100vh;
		background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
		overflow: hidden;
	}
	
	.background-decoration {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		z-index: 1;
		
		.circle-1, .circle-2, .circle-3 {
			position: absolute;
			border-radius: 50%;
			background: rgba(255, 255, 255, 0.1);
			animation: float 6s ease-in-out infinite;
		}
		
		.circle-1 {
			width: 200rpx;
			height: 200rpx;
			top: 10%;
			right: 20%;
			animation-delay: 0s;
		}
		
		.circle-2 {
			width: 120rpx;
			height: 120rpx;
			top: 70%;
			left: 15%;
			animation-delay: 2s;
		}
		
		.circle-3 {
			width: 80rpx;
			height: 80rpx;
			top: 40%;
			right: 10%;
			animation-delay: 4s;
		}
	}
	
	.error-content {
		position: relative;
		z-index: 2;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		min-height: 100vh;
		padding: 60rpx 40rpx;
	}
	
	.error-visual {
		position: relative;
		margin-bottom: 60rpx;
		
		.error-img {
			width: 320rpx;
			height: 320rpx;
			border-radius: 20rpx;
			box-shadow: 0 20rpx 40rpx rgba(0, 0, 0, 0.2);
			animation: bounce 2s ease-in-out infinite;
		}
		
		.floating-elements {
			position: absolute;
			top: 0;
			left: 0;
			width: 100%;
			height: 100%;
			
			.dot {
				position: absolute;
				width: 16rpx;
				height: 16rpx;
				background: #fff;
				border-radius: 50%;
				animation: twinkle 2s ease-in-out infinite;
			}
			
			.dot-1 {
				top: 20%;
				left: 80%;
				animation-delay: 0.5s;
			}
			
			.dot-2 {
				top: 60%;
				left: 10%;
				animation-delay: 1s;
			}
			
			.dot-3 {
				top: 80%;
				right: 20%;
				animation-delay: 1.5s;
			}
		}
	}
	
	.error-text {
		text-align: center;
		margin-bottom: 80rpx;
		
		.title {
			display: block;
			font-size: 56rpx;
			font-weight: 700;
			color: #fff;
			margin-bottom: 20rpx;
			text-shadow: 0 4rpx 8rpx rgba(0, 0, 0, 0.3);
			
			.title-emoji {
				margin-right: 20rpx;
				font-size: 60rpx;
			}
		}
		
		.subtitle {
			display: block;
			font-size: 28rpx;
			color: rgba(255, 255, 255, 0.8);
			margin-bottom: 30rpx;
			font-weight: 300;
			letter-spacing: 2rpx;
		}
		
		.description {
			display: block;
			font-size: 32rpx;
			color: rgba(255, 255, 255, 0.9);
			font-weight: 400;
		}
	}
	
	.project-card {
		background: rgba(255, 255, 255, 0.95);
		backdrop-filter: blur(20rpx);
		border-radius: 24rpx;
		padding: 40rpx;
		margin-bottom: 60rpx;
		width: 90%;
		max-width: 600rpx;
		box-shadow: 0 20rpx 60rpx rgba(0, 0, 0, 0.2);
		border: 1px solid rgba(255, 255, 255, 0.3);
		
		.card-header {
			display: flex;
			align-items: center;
			margin-bottom: 30rpx;
			
			.logo-placeholder {
				width: 80rpx;
				height: 80rpx;
				background: linear-gradient(45deg, #ff6b6b, #feca57);
				border-radius: 16rpx;
				display: flex;
				align-items: center;
				justify-content: center;
				font-size: 32rpx;
				margin-right: 24rpx;
			}
			
			.project-details {
				flex: 1;
				
				.project-name {
					display: block;
					font-size: 32rpx;
					font-weight: 600;
					color: #2c3e50;
					margin-bottom: 8rpx;
				}
				
				.project-desc {
					display: block;
					font-size: 24rpx;
					color: #7f8c8d;
					font-weight: 400;
				}
			}
		}
		
		.project-link {
			display: flex;
			align-items: center;
			justify-content: space-between;
			background: #f8f9fa;
			border-radius: 12rpx;
			padding: 24rpx;
			border: 2rpx dashed #dee2e6;
			transition: all 0.3s ease;
			
			&:active {
				transform: scale(0.98);
				background: #e9ecef;
			}
			
			.link-text {
				flex: 1;
				font-size: 24rpx;
				color: #495057;
				word-break: break-all;
			}
			
			.copy-icon {
				margin-left: 20rpx;
				font-size: 28rpx;
			}
		}
	}
	
	.action-buttons {
		display: flex;
		flex-direction: column;
		width: 90%;
		max-width: 600rpx;
		gap: 24rpx;
		
		.btn-primary, .btn-secondary {
			height: 96rpx;
			border-radius: 48rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			font-size: 32rpx;
			font-weight: 600;
			box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.2);
			transition: all 0.3s ease;
			
			.btn-icon {
				margin-right: 16rpx;
				font-size: 28rpx;
			}
			
			&:active {
				transform: translateY(2rpx);
				box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.3);
			}
		}
		
		.btn-primary {
			background: linear-gradient(45deg, #667eea, #764ba2);
			color: #fff;
			border: none;
		}
		
		.btn-secondary {
			background: rgba(255, 255, 255, 0.9);
			color: #667eea;
			border: 2rpx solid rgba(255, 255, 255, 0.3);
		}
	}
	
	@keyframes float {
		0%, 100% { transform: translateY(0px); }
		50% { transform: translateY(-20rpx); }
	}
	
	@keyframes bounce {
		0%, 20%, 50%, 80%, 100% { transform: translateY(0); }
		40% { transform: translateY(-20rpx); }
		60% { transform: translateY(-10rpx); }
	}
	
	@keyframes twinkle {
		0%, 100% { opacity: 0.3; transform: scale(1); }
		50% { opacity: 1; transform: scale(1.2); }
	}
</style>