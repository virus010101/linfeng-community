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

<script setup>
	import { ref, reactive, getCurrentInstance } from 'vue'
	import { onLoad, onReachBottom } from '@dcloudio/uni-app'
	import PostList from '@/components/post-list/post-list.vue'
	
	const { proxy } = getCurrentInstance()
	
	const loading = ref(true)
	const uid = ref(0)
	const postList = ref([])
	const userInfo = reactive({})
	const loadStatus = ref("loading")
	const page = ref(1)
	
	function follow() {
		proxy.$H.post('user/addFollow', {
			id: userInfo.uid
		}).then(res => {
			if (res.code === 0) {
				userInfo.isFollow = true
			}
			proxy.$u.toast(res.msg)
		})
	}
	
	function cancelFollow() {
		proxy.$H.post('user/cancelFollow', {
			id: userInfo.uid
		}).then(res => {
			if (res.code === 0) {
				userInfo.isFollow = false
			}
			proxy.$u.toast(res.msg)
		})
	}
	
	function getPostList() {
		loadStatus.value = "loading"
		proxy.$H.post('post/list', {
			page: page.value,
			uid: uid.value
		}).then(res => {
			postList.value = postList.value.concat(res.result.data)
			if (res.result.current_page === res.result.last_page || res.result.last_page === 0) {
				loadStatus.value = "nomore"
			} else {
				loadStatus.value = "loadmore"
			}
		})
	}
	
	function getUserInfo() {
		proxy.$H.post('user/userInfoById', {
			uid: uid.value
		}).then(res => {
			if (res.code == 0) {
				Object.assign(userInfo, res.result)
				if (res.result.gender === 1) {
					userInfo.gender = '男'
				} else if (res.result.gender === 2) {
					userInfo.gender = '女'
				} else {
					userInfo.gender = '保密'
				}
			} else {
				proxy.$u.toast(res.msg)
				setTimeout(function() {
					uni.switchTab({
						url: '/pages/index/index'
					})
				}, 1500)
			}
			loading.value = false
		})
	}
	
	onLoad((options) => {
		if (options.uid) {
			uid.value = options.uid
		} else {
			uid.value = 0
		}
		getUserInfo()
		getPostList()
	})
	
	onReachBottom(() => {
		page.value++
		getPostList()
	})
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