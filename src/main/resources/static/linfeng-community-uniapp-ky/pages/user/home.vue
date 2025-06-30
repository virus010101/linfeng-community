<template>
	<view class="profile-container">
		<view class="profile-info">
			<image class="avatar" :src="userInfo.avatar" mode="aspectFill"></image>
			<view class="username">
				{{userInfo.username}}
				<text class="iconfont icon-nan" v-if="userInfo.gender=='男'"></text>
				<text class="iconfont icon-nv" v-else-if="userInfo.gender=='女'"></text>
			</view>
			<view class="desc">{{userInfo.intro}}</view>
			<text class="desc" v-if="userInfo.city">IP:{{userInfo.city}}</text>
			<text class="desc" v-else>IP属地:未知</text>
			<view class="stats">
				<view class="stat-item">
					<text class="number">{{userInfo.follow}}</text>
					<text class="label">关注</text>
				</view>
				<view class="stat-divider"></view>
				<view class="stat-item">
					<text class="number">{{userInfo.fans}}</text>
					<text class="label">粉丝</text>
				</view>
			</view>
			<view class="actions" v-show="!userInfo.isFollow" @click="follow">
				<button class="follow-btn">关注</button>
			</view>
			<view class="actions" v-show="userInfo.isFollow" @click="cancelFollow">
				<button class="cancelfollow-btn">取关</button>
			</view>
		</view>

		<view class="content-tabs">
			<view class="tab active">动态</view>
		</view>
		<post-list :list="postList" :loadStatus="loadStatus"></post-list>
	</view>
</template>

<script>
	import postList from '@/components/post-list/post-list.vue';
	export default {
		components: {
			postList
		},
		data() {
			return {
				loading: true,
				background: {
					backgroundColor: 'unset'
				},
				tabs: [{
					tab_name: '帖子'
				}],
				current: 1,
				uid: 0,
				postList: [],
				topicList: [],
				userInfo: {},
				userJson: "",
				loadStatus: "loading",
				page: 1
			};
		},
		onLoad(options) {
			if (options.uid) {
				this.uid = options.uid;
			} else {
				this.uid = 0
			}
			this.getUserInfo();
			this.getPostList();
		},
		onReachBottom() {
			this.page++;
			this.getPostList();
		},
		methods: {
			follow() {
				this.$H.post('user/addFollow', {
					id: this.userInfo.uid
				}).then(res => {
					if (res.code === 0) {
						this.userInfo.isFollow = true;
					}
					this.$u.toast(res.msg);
				})
			},
			cancelFollow() {
				this.$H.post('user/cancelFollow', {
					id: this.userInfo.uid
				}).then(res => {
					if (res.code === 0) {
						this.userInfo.isFollow = false;
					}
					this.$u.toast(res.msg);
				})
			},

			getPostList() {
				this.loadStatus = "loading";
				this.$H.post('post/list', {
					page: this.page,
					uid: this.uid
				}).then(res => {
					this.postList = this.postList.concat(res.result.data);
					if (res.result.current_page === res.result.last_page || res.result.last_page === 0) {
						this.loadStatus = "nomore";
					} else {
						this.loadStatus = "loadmore"
					}
				})
			},
			getUserInfo() {
				this.$H.post('user/userInfoById', {
					uid: this.uid
				}).then(res => {
					if (res.code == 0) {
						this.userInfo = res.result;
						if (res.result.gender === 1) {
							this.userInfo.gender = '男'
						} else if (res.result.gender === 2) {
							this.userInfo.gender = '女'
						} else {
							this.userInfo.gender = '保密'
						}
					} else {
						this.$u.toast(res.msg)
						setTimeout(function() {
							uni.switchTab({
								url: '/pages/index/index'
							});
						}, 1500);
					}
					this.loading = false;
				})
			}
		}
	}
</script>

<style>
	.profile-container {
		min-height: 100vh;
		background-color: #f8f9fa;
	}

	.header {
		padding: 44rpx 32rpx;
		display: flex;
		justify-content: space-between;
		align-items: center;
	}

	.back-icon,
	.more-icon {
		font-size: 40rpx;
		color: #333;
	}

	.profile-info {
		padding: 0 32rpx;
		display: flex;
		flex-direction: column;
		align-items: center;
		background-color: #ffffff;
		background-image: url(../../static/img/bg.jpeg);
	}

	.avatar {
		width: 160rpx;
		height: 160rpx;
		border-radius: 50%;
		margin-bottom: 24rpx;
	}

	.username {
		font-size: 36rpx;
		font-weight: bold;
		margin-bottom: 12rpx;
		display: flex;
		align-items: center;
		gap: 8rpx;
	}

	.badge {
		font-size: 28rpx;
	}

	.desc {
		font-size: 28rpx;
		color: #666;
		margin-bottom: 32rpx;
	}

	.stats {
		display: flex;
		align-items: center;
		gap: 48rpx;
		margin-bottom: 32rpx;
	}

	.stat-item {
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.stat-divider {
		width: 2rpx;
		height: 24rpx;
		background-color: #e0e0e0;
	}

	.number {
		font-size: 32rpx;
		font-weight: bold;
		color: #333;
	}

	.label {
		font-size: 28rpx;
		color: #666;
	}

	.actions {
		display: flex;
		gap: 24rpx;
		margin-bottom: 48rpx;
	}

	.follow-btn {
		background-color: #232323;
		color: #ffffff;
		border-radius: 32rpx;
		font-size: 26rpx;
	}

	.cancelfollow-btn {
		background-color: #efefef;
		color: #000000;
		border-radius: 32rpx;
		font-size: 26rpx;
	}

	.content-tabs {
		border-bottom: 1rpx solid #e0e0e0;
		padding: 0 32rpx;
	}

	.tab {
		display: inline-block;
		padding: 24rpx 0;
		font-size: 32rpx;
		color: #333;
		position: relative;
	}

	.tab.active::after {
		content: '';
		position: absolute;
		bottom: 0;
		left: 0;
		width: 48rpx;
		height: 4rpx;
		background-color: #aa55ff;
	}
</style>