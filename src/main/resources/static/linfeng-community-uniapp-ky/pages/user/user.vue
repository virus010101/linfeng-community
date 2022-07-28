<template>
	<view>
		<view class="head">
			<block v-if="hasLogin">
				<view class="userinfo" @click="goUser">
					<u-avatar :src="userInfo.avatar"></u-avatar>
					<view class="username">
						<text>{{ userInfo.username }}</text>
						<text class="sub-txt">{{ userInfo.intro }}</text>
					</view>
					<u-icon name="arrow-right" class="arrow-right"></u-icon>
				</view>
			</block>
			<block v-else>
				<view class="btn-login">
					<!-- #ifdef H5 -->
					<u-button type="default" shape="circle" @click="phoneLogin" plain>登录</u-button>
					<!-- #endif -->
					<!-- #ifdef MP-WEIXIN -->
					<u-button type="default" shape="circle" @click="wxLogin" plain>登录</u-button>
					<!-- #endif -->
				</view>
			</block>
			<u-grid :col="4" :border="false" style="margin: 20rpx 0;" @click="toNav">
				<u-grid-item index="/pages/my/user?type=2">
					<text>{{ userInfo.fans || 0}}</text>
					<view class="grid-text">粉丝</view>
				</u-grid-item>
				<u-grid-item index="/pages/my/user?type=1">
					<text>{{ userInfo.follow || 0 }}</text>
					<view class="grid-text">关注</view>
				</u-grid-item>
				<u-grid-item index="/pages/my/post?type=2">
					<text>{{ userInfo.postNum || 0 }}</text>
					<view class="grid-text">帖子</view>
				</u-grid-item>
				<u-grid-item index="">
					<text>{{ userInfo.integral || 0 }}</text>
					<view class="grid-text">积分</view>
				</u-grid-item>
			</u-grid>
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
				<!-- #ifdef MP-WEIXIN -->
				<u-grid-item>
					<button open-type="contact" class="u-reset-button">
						<image class="gn-icon" style="margin-bottom: unset;" src="/static/m_8.png"></image>
						<view class="grid-text">客服</view>
					</button>
				</u-grid-item>
				<!-- #endif -->
			</u-grid>
		</view>
		<!-- 发贴入口 -->
		<add-post-tag></add-post-tag>
	</view>
</template>

<script>
	import addPostTag from '../../components/add-post-tag/add-post-tag.vue';
	export default {
		components: {
			addPostTag
		},
		data() {
			return {
				userInfo: '',
				hasLogin: false
			};
		},
		computed: {
			msgCount() {
				return this.$store.state.messegeNum
			}
		},
		onLoad() {
			//#ifdef MP-WEIXIN
			wx.showShareMenu({
				withShareTicket: true,
				menus: ['shareAppMessage', 'shareTimeline']
			});
			//#endif
		},
		onShow() {
			if (uni.getStorageSync('hasLogin')) {
				this.getUserInfo();
				this.hasLogin = true;
			} else {
				this.hasLogin = false;
			}
			// this.getMsgNum();
		},
		onShareAppMessage(res) {
			let imgURL = 'http://pic.linfeng.tech/logo.png';
			return {
				title: this.$c.miniappName,
				path: '/pages/index/index',
				imageUrl: imgURL
			};
		},
		methods: {
			getMsgNum() {
				this.$H.post('message/num').then(res => {
					this.$store.state.messegeNum = [0, 0, 0, res.result.allCount, 0];
				});
			},
			phoneLogin() {
				uni.navigateTo({
					url: '/pages/login/login'
				});
			},
			wxLogin() {
				uni.navigateTo({
					url: '/pages/login/weixin'
				});
			},
			getUserInfo() {
				this.$H.get('user/userInfo').then(res => {
					this.userInfo = res.result;
				});
			},
			goUser() {
				uni.navigateTo({
					url: '/pages/user/edit'
				});
			},
			toNav(url) {
				uni.navigateTo({
					url: url
				});
			}
		}
	};
</script>

<style>
	page {
		background-color: #f5f5f5;
	}
</style>
<style lang="scss" scoped>
	.head {
		padding: 20rpx;
		background-color: #fff;
	
	.sub-txt {
			font-size: 24rpx;
			color: #616161;
			display: block;
			display: -webkit-box;
			-webkit-box-orient: vertical;
			-webkit-line-clamp: 1;
			overflow: hidden;
		}

		margin-bottom: 20rpx;
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
		border-radius: 20rpx;
		margin: 20rpx;
		overflow: hidden;

		.block-title {
			background-color: #fff;
			padding: 20rpx;
		}
	}
</style>
