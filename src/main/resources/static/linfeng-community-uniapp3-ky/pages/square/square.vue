<template>
	<view>
		<!-- navbar -->
		<u-navbar :is-back="false" :border-bottom="false">
			<u-icon name="plus-circle" :size="40" class="add-icon" @click="goPostAdd"></u-icon>
			<u-tabs :list="pageTab" v-model="pageCurrent" @change="pageTabChange"></u-tabs>
		</u-navbar>
		<!-- 创作广场 -->
		<view v-show="pageCurrent == 0">
			<!-- 分类 -->
			<view class="tabs-wrap">
				<u-tabs :list="classList" name="cateName" v-model="current" @change="tabChange"></u-tabs>
			</view>
			<view v-show="current == 0">
				<!-- 轮播图 -->
				<view class="wrap">
					<u-swiper :list="swiperList" name="img" border-radius="20" mode="dot" :effect3d="true"></u-swiper>
				</view>
			</view>
			<!-- 帖子列表 -->
			<post-list :list="postList" :loadStatus="loadPostStatus"></post-list>
		</view>
		<!-- 发帖达人（查询本月发帖活跃用户） -->
		<view v-show="pageCurrent == 1">
			<view style="background-color: #aaaaff;">
				<view class="tab">
				</view>
				<view class="top" v-if="userList.length > 0">
					<view class="top-item" v-if="userList.length >= 2 && userList[1]">
						<image class="top-item-avatar" :src="userList[1].avatar"></image>
						<text class="top-item-name">{{userList[1].username}}</text>
						<text class="top-item-score">+{{userList[1].postNumber}}</text>
					</view>
					<view class="top-item">
						<image class="top-item-avatar" :src="userList[0].avatar"></image>
						<text class="top-item-name">{{userList[0].username}}</text>
						<text class="top-item-score">+{{userList[0].postNumber}}</text>
					</view>
					<view class="top-item" v-if="userList.length >= 3 && userList[2]">
						<image class="top-item-avatar" :src="userList[2].avatar"></image>
						<text class="top-item-name">{{userList[2].username}}</text>
						<text class="top-item-score">+{{userList[2].postNumber}}</text>
					</view>
				</view>
				<view class="ranking">
					<view v-if="userList.length > 0">
						<view class="ranking-list-item" v-for="(item,index) in userList" :key="index">
							<text class="ranking-list-number">{{index+1}}</text>
							<view class="ranking-list-nickname" @click="goUser(item.uid)">
								<image :src="item.avatar"></image>
								<text>{{item.username}}</text>
							</view>
							<text class="ranking-list-score">+{{item.postNumber}}</text>
						</view>
					</view>
				</view>
			</view>
			<view v-if="userList.length == 0">
				<u-empty margin-top="100" text="暂无排名" mode="favor"></u-empty>
			</view>
		</view>
		<!-- 发帖入口 -->
		<add-post-tag></add-post-tag>
	</view>
</template>

<script setup>
	import { ref, getCurrentInstance } from 'vue'
	import { onLoad, onShow, onReachBottom } from '@dcloudio/uni-app'
	import PostList from '@/components/post-list/post-list.vue'
	import AddPostTag from '@/components/add-post-tag/add-post-tag.vue'
	const pageCurrent = ref(0)
	const current = ref(0)
	const pageTab = ref([
		{ name: '广场' },
		{ name: '达人' }
	])
	const classList = ref([{ cateId: 0, cateName: '推荐' }])
	const swiperList = ref([])
	const postList = ref([])
	const loadPostStatus = ref('loadmore')
	const classId = ref(0)
	const page = ref(1)
	const userList = ref([])
	const loadStatus = ref('nomore')
	const currentRank = ref(0)
	const { proxy } = getCurrentInstance()
	function tabChangeRank() {
		currentRank.value = currentRank.value ? 0 : 1
	}
	function getBannerList() {
		proxy.$H.get('link/list').then(res => {
			if (res.code == 0) {
				swiperList.value = res.result
			}
		})
	}
	function toSearch() {
		uni.showToast({
			icon: 'none',
			title: '暂无搜索'
		})
	}
	function goPostAdd() {
		uni.navigateTo({
			url: '/pages/post/add'
		})
	}
	function goUser(uid) {
		uni.navigateTo({
			url: "/pages/user/home?uid=" + uid
		})
	}
	function goLogin() {
		// #ifdef MP-WEIXIN
		uni.navigateTo({
			url: "/pages/login/weixin"
		})
		// #endif
		// #ifdef H5
		uni.navigateTo({
			url: "/pages/login/login"
		})
		// #endif
	}
	function pageTabChange(index) {
		pageCurrent.value = index
	}
	function tabChange(index) {
		current.value = index
		page.value = 1
		classId.value = classList.value[index].cateId
		postList.value = []
		getPostList()
	}
	function getClassList() {
		proxy.$H.get('category/classList').then(res => {
			classList.value = classList.value.concat(res.result)
		})
	}
	function getUserRanking() {
		proxy.$H
			.post('user/userRank')
			.then(res => {
				userList.value = res.result
			})
	}
	function getPostList() {
		if (!uni.getStorageSync("hasLogin")) {
			proxy.$u.toast('请先登录哦')
			return
		}
		loadPostStatus.value = 'loading'
		proxy.$H
			.post('post/list', {
				classId: classId.value,
				page: page.value
			})
			.then(res => {
				if (page.value == 1) {
					postList.value = []
				}
				postList.value = postList.value.concat(res.result.data)
				if (res.result.current_page === res.result.last_page || res.result.last_page === 0) {
					loadPostStatus.value = 'nomore'
				} else {
					loadPostStatus.value = 'loadmore'
				}
			})
	}
	onLoad(() => {
		getBannerList()
		getClassList()
		getUserRanking()
	})
	onShow(() => {
		getPostList()
	})
	onReachBottom(() => {
		if (pageCurrent.value == 0) {
			page.value++
			getPostList()
		} else if (pageCurrent.value == 1) {
			userList.value = []
			getUserRanking()
		}
	})
</script>

<style lang="scss" scoped>
	.add-icon {
		margin-left: 20rpx;
		margin-right: 30%;
	}

	.tabs-wrap {
		position: sticky;
		z-index: 999;
	}

	.wrap {
		padding: 0 40rpx;
	}

	.tab {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 25rpx;
		color: #fff;
		margin-bottom: 25rpx;

		view {
			height: 70rpx;
			width: 220rpx;
			line-height: 70rpx;
			box-sizing: border-box;
			border: 1px solid #fff;
			font-size: 16px;
			text-align: center;
			font-weight: bold;

			&:nth-child(1) {
				border-radius: 35rpx 0 0 35rpx;
			}

			&:nth-child(2) {
				border-radius: 0 35rpx 35rpx 0;
			}
		}

		.tab-active {
			background: #fff;
			color: #000000;
		}
	}

	.top {
		width: 660rpx;
		margin: auto;
		display: flex;
		justify-content: space-between;
		align-items: flex-end;

		.top-item {
			width: 200rpx;
			height: 300rpx;
			display: flex;
			flex-direction: column;
			position: relative;
			align-items: center;
			border-radius: 100rpx 100rpx 0 0;
			color: #fff;

			&:nth-child(1) {
				.top-item-avatar {
					border: 8rpx solid #c7c7c7;
				}
			}

			&:nth-child(2) {
				height: 320rpx;

				.top-item-avatar {
					border: 8rpx solid #ffff00;
				}
			}

			&:nth-child(3) {
				.top-item-avatar {
					border: 8rpx solid #aa5500;
				}
			}

			.top-item-avatar {
				border-radius: 50%;
				width: 140rpx;
				height: 140rpx;
			}

			.top-item-score {
				font-size: 16px;
			}

			.top-item-name {
				margin: 10rpx 0;
			}
		}
	}

	.ranking {
		width: 700rpx;
		border-radius: 30rpx;
		margin: auto;
		background: #fff;
		box-sizing: border-box;
		padding: 20rpx;

		.ranking-list-item {
			height: 110rpx;
			display: flex;
			align-items: center;
			font-size: 14px;

			.ranking-list-number {
				display: block;
				width: 70rpx;
				color: #777;
			}

			.ranking-list-score {
				display: block;
				width: 70rpx;
				color: #E28935;
				font-size: 16px;
			}

			.ranking-list-nickname {
				display: flex;
				align-items: center;
				width: calc(100% - 140rpx);

				image {
					width: 80rpx;
					height: 80rpx;
					border-radius: 50%;
					margin-right: 20rpx;
				}

				text {
					width: auto;
				}
			}
		}
	}
</style>