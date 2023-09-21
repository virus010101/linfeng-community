<template>
	<view>
		<view style="position: absolute;">
			<u-navbar :custom-back="onBack" back-icon-color="#0c0000" :background="background" :border-bottom="false">
			</u-navbar>
		</view>
		<view class="container">
			<view class="info-wrap">
				<u-avatar class="avatar" :src="userInfo.avatar" :show-level='userInfo.type == 1'
					level-bg-color="#8072f3" size="130"></u-avatar>
				<view class="user-style">
					<view class="username">{{userInfo.username}}
						<text class="iconfont icon-nan kong" v-if="userInfo.gender=='男'"></text>
						<text class="iconfont icon-nv kong" v-else-if="userInfo.gender=='女'"></text>
					</view>

					<text class="desc">{{userInfo.intro}}</text>
					<text class="desc" v-if="userInfo.city">IP:{{userInfo.city}}</text>
					<text class="desc" v-else>IP属地:未知</text>

					<view class="btn-box" v-if="currUid!=uid">
						<u-button v-show="!userInfo.isFollow" @click="follow" :custom-style="btnStyle" class="btn"
							shape="circle" size="mini">
							<u-icon name="plus"></u-icon>
							<text>关注</text>
						</u-button>
						<u-button v-show="userInfo.isFollow" @click="cancelFollow" :custom-style="btnStyle2" class="btn"
							shape="circle" size="mini">
							<text>已关注</text>
						</u-button>
						<u-button :custom-style="btnStyle2" @click="chat" shape="circle" size="mini">
							<text style="margin: 0 15rpx;">私信</text>
						</u-button>
					</view>
				</view>
			</view>
			<!-- 帖子 -->
			<view>
				<view class="title-desc">发布的动态</view>
				<post-list :list="postList" :loadStatus="loadStatus"></post-list>
			</view>
		</view>

		<!-- 发贴入口 -->
		<add-post-tag></add-post-tag>

	</view>
</template>

<script>
	import postList from '@/components/post-list/post-list.vue';
	import addPostTag from '@/components/add-post-tag/add-post-tag.vue';
	export default {
		components: {
			postList
		},
		data() {
			return {
				loading: true,
				btnStyle: {
					color: "#fff",
					backgroundColor: '#000000'
				},
				btnStyle2: {
					border: '1px solid #000000',
					color: "#000000"
				},
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
				page: 1,
				currUid: 0
			};
		},
		onLoad(options) {
			this.uid = options.uid;
			this.getUserInfo();
			this.getPostList();
			if (uni.getStorageSync('userInfo').uid) {
				this.currUid = uni.getStorageSync('userInfo').uid;
			}
		},
		onReachBottom() {
			this.page++;
			this.getPostList();
		},
		methods: {
			onBack() {
				uni.navigateBack();
			},
			follow() {
				this.$H.post('user/addFollow', {
					id: this.userInfo.uid
				}).then(res => {
					if (res.code === 0) {
						this.userInfo.isFollow = true;
					} else {
						this.$u.toast(res.msg);
					}
				})
			},
			cancelFollow() {
				this.$H.post('user/cancelFollow', {
					id: this.userInfo.uid
				}).then(res => {
					if (res.code === 0) {
						this.userInfo.isFollow = false;
					}
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
			chat() {
				this.$u.toast('开源版暂未开放')
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

						let user = {
							uid: res.result.uid,
							username: res.result.username,
							avatar: res.result.avatar,
						}
						this.userJson = JSON.stringify(user)
						uni.setNavigationBarTitle({
							title: this.userInfo.username
						});
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
	page {
		background-color: #f5f5f5;
	}
</style>
<style lang="scss" scoped>
	.container {
		padding: 30rpx;
		position: relative;
		top: 100rpx;
		width: 100%;
	}

	.info-wrap {
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.avatar {
		width: 130rpx;
		height: 130rpx;
		border-radius: 100rpx !important;
		z-index: 999;
		margin: 150rpx 550rpx 100rpx 50rpx;
	}

	.info-wrap .user-style {
		background-color: #FFFFFF;
		border-radius: 30rpx;
		padding: 30rpx;
		position: absolute;
		top: 85rpx;
		width: 100%;
		box-shadow: 5rpx 10rpx 20rpx #e6e6e6;
		display: flex;
		flex-direction: column;
		align-items: center;
	}

	.info-wrap .user-style .username {
		font-size: 34rpx;
		font-weight: bold;
		margin: 10rpx 10rpx 10rpx 90rpx;

		.kong {
			margin-left: 25rpx;
		}
	}

	.info-wrap .user-style .num-box {
		font-size: 24rpx;
		margin: 20rpx 0;
		text-align: center;
	}

	.info-wrap .user-style .num-box .txt {
		color: #999;
		margin-left: 5rpx;
	}

	.info-wrap .user-style .num-box text {
		margin-right: 30rpx;
	}

	.info-wrap .user-style .desc {
		font-size: 28rpx;
		color: #999;
		margin: 1rpx 20rpx 20rpx 140rpx;
	}

	.info-wrap .user-style .btn-box {
		margin: 0rpx 30rpx 10rpx 90rpx;
	}

	.info-wrap .user-style .btn-box .btn {
		margin-right: 20rpx;
	}

	.tab-box {
		margin-top: 30rpx;
		margin-bottom: 30rpx;
	}

	.info-c {
		display: flex;
		flex-direction: column;
	}

	.info-c>text {
		margin-bottom: 20rpx;
		color: #999;
	}

	.info-c .level-box {
		margin-bottom: 20rpx;
		display: flex;
		align-items: center;
		color: #999;

		.level {
			font-size: 20rpx;
			color: #fff;
			padding: 5rpx 10rpx;
			background-color: #333333;
			border-radius: 10rpx;
			margin-right: 10rpx;
		}
	}
	.title-desc {
		margin: auto;
		font-size: 32rpx;
		color: #565656;
		font-weight: 500;
	}
</style>
