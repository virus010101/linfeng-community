<template>
	<view>
		<view class="head">
			<view v-if="hasLogin">
				<view class="userinfo" @click="goUser">
					<u-avatar :src="userInfo.avatar"></u-avatar>
					<view class="username">
						<text>{{ userInfo.username }}</text>
						<text class="sub-txt">{{ userInfo.intro }}</text>
					</view>
					<u-icon name="arrow-right" class="arrow-right"></u-icon>
				</view>
			</view>
			<view v-else>
				<view class="btn-login">
					<!-- #ifdef H5 -->
					<u-button type="default" shape="circle" @click="phoneLogin" plain>登录</u-button>
					<!-- #endif -->
					<!-- #ifdef MP-WEIXIN -->
					<u-button type="default" shape="circle" @click="wxLogin" plain>登录</u-button>
					<!-- #endif -->
				</view>
			</view>
			<view class="lf_stats_grid">
				<view class="lf_stats_item" @click="toNav('/pages/my/user?type=2')">
					<text class="lf_stats_num">{{ userInfo.fans || 0}}</text>
					<text class="lf_stats_label">粉丝</text>
				</view>
				<view class="lf_stats_item" @click="toNav('/pages/my/user?type=1')">
					<text class="lf_stats_num">{{ userInfo.follow || 0 }}</text>
					<text class="lf_stats_label">关注</text>
				</view>
				<view class="lf_stats_item" @click="toNav('/pages/my/post?type=2')">
					<text class="lf_stats_num">{{ userInfo.postNum || 0 }}</text>
					<text class="lf_stats_label">帖子</text>
				</view>
				<view class="lf_stats_item">
					<text class="lf_stats_num">{{ userInfo.integral || 0 }}</text>
					<text class="lf_stats_label">积分</text>
				</view>
			</view>
		</view>
		<!-- 我的服务 -->
		<view class="block-wrap">
			<view class="block-title">我的服务</view>
			<u-grid :col="4" :border="false" style="margin: 20rpx 0;" @click="toNav">

				<u-grid-item index="/pages/my/post?type=2">
					<image class="gn-icon" src="/static/img/post.png"></image>
					<view class="grid-text">我的帖子</view>
				</u-grid-item>

				<u-grid-item index="/pages/my/post?type=1">
					<image class="gn-icon" src="/static/img/star.png"></image>
					<view class="grid-text">我的点赞</view>
				</u-grid-item>

				<u-grid-item index="/pages/my/user?type=1">
					<image class="gn-icon" src="/static/img/watch.png"></image>
					<view class="grid-text">我的关注</view>
				</u-grid-item>

				<u-grid-item index="/pages/my/user?type=2">
					<image class="gn-icon" src="/static/img/fans.png"></image>
					<view class="grid-text">我的粉丝</view>
				</u-grid-item>
				<u-grid-item index="/pages/user/home">
					<image class="gn-icon" src="/static/img/mine.png"></image>
					<view class="grid-text">我的主页</view>
				</u-grid-item>
				<!-- #ifdef MP-WEIXIN -->
				<u-grid-item>
					<button open-type="contact" class="u-reset-button">
						<image class="gn-icon" style="margin-bottom: unset;" src="/static/img/kefu.png"></image>
						<view class="grid-text">客服</view>
					</button>
				</u-grid-item>
				<!-- #endif -->
				<u-grid-item @click="commercialLink">
					<image class="gn-icon" src="/static/img/commercial-licensing.png"></image>
					<view style="color: #e50000;font-size: 12px;margin-bottom: 20rpx;">商业版</view>
				</u-grid-item>
			</u-grid>
		</view>
		<view class="lf-bottom-section" @click="commercialLink">
			<view class="bottom-info">
				林风社交论坛开源版
			</view>
			<view class="bottom-info-t">
				© www.linfengtech.cn
			</view>
		</view>

		<!-- 发贴入口 -->
		<add-post-tag></add-post-tag>
	</view>
</template>

<script setup>
	import { ref, getCurrentInstance } from 'vue'
	import { onLoad, onShow, onShareAppMessage } from '@dcloudio/uni-app'
	import AddPostTag from '@/components/add-post-tag/add-post-tag.vue'
	
	const { proxy } = getCurrentInstance()
	
	const userInfo = ref('')
	const hasLogin = ref(false)
	const shareCover = ref('')
	
	function phoneLogin() {
		uni.navigateTo({
			url: '/pages/login/login'
		})
	}
	
	function wxLogin() {
		uni.navigateTo({
			url: '/pages/login/weixin'
		})
	}
	
	function getUserInfo() {
		proxy.$H.get('user/userInfo').then(res => {
			userInfo.value = res.result
		})
	}
	
	function goUser() {
		uni.navigateTo({
			url: '/pages/user/edit'
		})
	}
	
	function toNav(url) {
		uni.navigateTo({
			url: url
		})
	}
	
	function getSysInfo() {
		proxy.$H.get('system/config').then(res => {
			shareCover.value = res.logo
		})
	}
	
	function commercialLink() {
		// #ifdef H5
		window.open("https://www.linfengtech.cn")
		// #endif
		// #ifdef MP-WEIXIN
		uni.navigateToMiniProgram({
			appId: 'wx1d4a8967c92bda9b',
			path: 'pages/index/index',
			success: res => {
				console.log("打开成功", res)
			},
			fail: err => {
				console.log(err)
			}
		})
		// #endif
	}
	
	onLoad(() => {
		//#ifdef MP-WEIXIN
		wx.showShareMenu({
			withShareTicket: true,
			menus: ['shareAppMessage', 'shareTimeline']
		})
		//#endif
		getSysInfo()
	})
	
	onShow(() => {
		if (uni.getStorageSync('hasLogin')) {
			getUserInfo()
			hasLogin.value = true
		} else {
			hasLogin.value = false
		}
	})
	
	onShareAppMessage((res) => {
		return {
			title: proxy.$c.miniappName,
			path: '/pages/index/index',
			imageUrl: shareCover.value
		}
	})
</script>

<style>
	page {
		background-color: #f5f5f5;
	}
</style>
<style lang="scss" scoped>
	.head {
		padding: 30rpx 20rpx;
		background: #ffffff;
		margin-bottom: 20rpx;

		.userinfo {
			padding: 20rpx 10rpx;

			.username {
				text {
					color: #333;
					font-size: 32rpx;
					font-weight: 600;
				}

				.sub-txt {
					color: #999;
					font-size: 24rpx;
					margin-top: 6rpx;
				}
			}
		}

		.nav-text {
			color: #666;
			font-size: 26rpx;
		}
	}

	.userinfo {
		display: flex;
		align-items: center;
		padding: 20rpx;
	}

	.userinfo .username {
		display: flex;
		flex-direction: column;
		margin-left: 20rpx;
	}

	.nav-text {
		color: #999;
		font-size: 14px;
		margin-bottom: 20rpx;
	}

	.grid-text {
		color: #999;
		font-size: 12px;
		margin-bottom: 20rpx;
	}

	.userinfo u-avatar {
		margin-right: 20rpx;
	}

	.userinfo .arrow-right {
		margin-left: auto;
	}

	.btn-login {
		margin: 40rpx 0;
	}

	.gn-icon {
		width: 60rpx;
		height: 60rpx;
		margin-bottom: 20rpx;
	}

	/*服务按钮*/
	.btn-wrap {
		display: flex;
		margin-top: 30rpx;
	}

	.btn-wrap .btn-contact {
		background-color: #fff;
		margin-left: 15rpx;
		margin-right: 15rpx;
		padding: 20rpx;
		line-height: unset;
		font-size: 12px;
		color: #999;
	}

	.btn-wrap .btn-contact:active {
		background-color: #f5f5f5;
	}

	.btn-wrap .btn-contact .txt {
		margin-top: 20rpx;
	}

	.btn-wrap .btn-contact::after {
		border: unset;
		position: unset;
	}

	.icon-size {
		font-size: 50rpx;
	}

	.block-wrap {
		background-color: #fff;
		margin: 20rpx;

		.block-title {
			padding: 30rpx 20rpx;
			font-size: 30rpx;
			color: #333;
			border-bottom: 1rpx solid #f5f5f5;
		}
	}

	.lf-grid-item {
		padding: 20rpx 0;

		.gn-icon {
			width: 56rpx;
			height: 56rpx;
			margin-bottom: 12rpx;
		}

		.grid-text {
			color: #666;
			font-size: 26rpx;
		}
	}

	.lf-bottom-section {
		text-align: center;
		padding: 40rpx 20rpx;

		.bottom-info {
			color: #999;
			font-size: 26rpx;
		}

		.bottom-info-t {
			color: #999;
			font-size: 24rpx;
			margin-top: 8rpx;
		}
	}

	.lf_stats_grid {
		display: flex;
		justify-content: space-around;
		padding: 30rpx 10rpx;
		background: #fff;
		margin: 20rpx 0;
		border-radius: 12rpx;
		box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.05);
	}

	.lf_stats_item {
		display: flex;
		flex-direction: column;
		align-items: center;
		position: relative;
		padding: 0 20rpx;
		transition: all 0.3s ease;
		
		&:hover {
			transform: translateY(-2rpx);
		}
		
		&:not(:last-child)::after {
			content: '';
			position: absolute;
			right: 0;
			top: 50%;
			transform: translateY(-50%);
			width: 1px;
			height: 40rpx;
			background: linear-gradient(180deg, transparent, #f0f0f0, transparent);
		}
	}

	.lf_stats_num {
		font-size: 40rpx;
		font-weight: 600;
		color: #333;
		line-height: 1.4;
		margin-bottom: 8rpx;
		
		&:hover {
			color: #2979ff;
		}
	}

	.lf_stats_label {
		font-size: 26rpx;
		color: #999;
		position: relative;
		
		&::after {
			content: '';
			position: absolute;
			bottom: -6rpx;
			left: 50%;
			transform: translateX(-50%);
			width: 0;
			height: 2rpx;
			background: #2979ff;
			transition: width 0.3s ease;
		}
		
		&:hover::after {
			width: 100%;
		}
	}
</style>