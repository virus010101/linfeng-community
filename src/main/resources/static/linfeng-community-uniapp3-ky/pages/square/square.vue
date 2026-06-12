<template>
	<view>
		<!-- navbar -->
		<u-navbar :is-back="false" :border-bottom="false">
			<u-icon name="plus-circle" :size="40" class="add-icon" @click="goPostAdd"></u-icon>
			<u-tabs :list="pageTab" active-color="#000" v-model="pageCurrent" @change="pageTabChange"></u-tabs>
		</u-navbar>
		<!-- 创作广场 -->
		<view v-show="pageCurrent == 0">
			<!-- 分类 -->
			<view class="tabs-wrap">
				<u-tabs :list="classList" name="cateName"active-color="#000" v-model="current" @change="tabChange"></u-tabs>
			</view>
			<view v-show="current == 0">
				<!-- 轮播图 -->
				<view class="wrap">
					<u-swiper :list="swiperList" name="img" border-radius="20" mode="dot" :effect3d="true"></u-swiper>
				</view>
			</view>
			<u-line></u-line>
			<!-- 帖子列表 -->
			<post-list :list="postList" :loadStatus="loadPostStatus"></post-list>
		</view>
		<!-- 发帖达人（查询本周发帖活跃用户） -->
		<view v-show="pageCurrent == 1" class="ranking-container">
			<view class="ranking-header">
				<view class="ranking-title">发帖达人榜</view>
				<view class="ranking-subtitle">本周最活跃创作者</view>
			</view>
			
			<!-- 排行榜TOP3 -->
			<view class="top-container" v-if="userList.length > 0">
				<view class="top">
					<!-- 第二名 -->
					<view class="top-item top-item-2" v-if="userList.length >= 2 && userList[1]">
						<view class="top-rank">2</view>
						<image class="top-item-avatar" :src="userList[1].avatar"></image>
						<text class="top-item-name">{{userList[1].username}}</text>
						<view class="top-item-score">+{{userList[1].postNumber}} 帖</view>
					</view>
					<!-- 第一名 -->
					<view class="top-item top-item-1" v-if="userList.length >= 1 && userList[0]">
						<view class="top-rank">1</view>
						<image class="top-item-avatar" :src="userList[0].avatar"></image>
						<text class="top-item-name">{{userList[0].username}}</text>
						<view class="top-item-score">+{{userList[0].postNumber}} 帖</view>
					</view>
					<!-- 第三名 -->
					<view class="top-item top-item-3" v-if="userList.length >= 3 && userList[2]">
						<view class="top-rank">3</view>
						<image class="top-item-avatar" :src="userList[2].avatar"></image>
						<text class="top-item-name">{{userList[2].username}}</text>
						<view class="top-item-score">+{{userList[2].postNumber}} 帖</view>
					</view>
				</view>
			</view>
			
			<!-- 排名列表 -->
			<view class="ranking-list-container">
				<view class="ranking-list-header">更多达人</view>
				<view class="ranking" v-if="userList.length > 0">
					<view class="ranking-list-item" v-for="(item,index) in userList" :key="index">
						<view class="rank-number">{{index+1}}</view>
						<view class="ranking-list-nickname" @click="goUser(item.uid)">
							<image :src="item.avatar"></image>
							<text class="nickname">{{item.username}}</text>
						</view>
						<view class="rank-score">+{{item.postNumber}}</view>
					</view>
				</view>
			</view>
		</view>
		<!-- 发帖入口 -->
		<add-post-tag></add-post-tag>
	</view>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance
	} from 'vue'
	import {
		onLoad,
		onShow,
		onReachBottom
	} from '@dcloudio/uni-app'
	import PostList from '@/components/post-list/post-list.vue'
	import AddPostTag from '@/components/add-post-tag/add-post-tag.vue'
	const pageCurrent = ref(0)
	const current = ref(0)
	const pageTab = ref([{
			name: '广场'
		},
		{
			name: '达人'
		}
	])
	const classList = ref([{
		cateId: 0,
		cateName: '推荐'
	}])
	const swiperList = ref([])
	const postList = ref([])
	const loadPostStatus = ref('loadmore')
	const classId = ref(0)
	const page = ref(1)
	const userList = ref([])
	const loadStatus = ref('nomore')
	const currentRank = ref(0)
	const {
		proxy
	} = getCurrentInstance()

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
				if (res.result.current_page >= res.result.last_page || res.result.total === 0) {
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
	page{
		background-color: #f7f8fa;
	}
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
	
	/* 发帖达人模块样式 */
	.ranking-container {
		padding: 30rpx;
	}
	
	.ranking-header {
		margin-bottom: 40rpx;
		.ranking-title {
			font-size: 40rpx;
			font-weight: 600;
			color: #333;
		}
		.ranking-subtitle {
			font-size: 26rpx;
			color: #999;
			margin-top: 10rpx;
		}
	}
	
	.top-container {
		background: #fff;
		border-radius: 24rpx;
		padding: 40rpx 20rpx;
		margin-bottom: 30rpx;
		box-shadow: 0 2rpx 20rpx rgba(0, 0, 0, 0.04);
	}

	.top {
		width: 100%;
		margin: auto;
		display: flex;
		justify-content: center;
		align-items: flex-end;

		.top-item {
			width: 200rpx;
			display: flex;
			flex-direction: column;
			position: relative;
			align-items: center;
			margin: 0 15rpx;

			.top-rank {
				width: 40rpx;
				height: 40rpx;
				background: #ddd;
				color: #fff;
				border-radius: 50%;
				display: flex;
				justify-content: center;
				align-items: center;
				font-size: 24rpx;
				font-weight: bold;
				margin-bottom: 10rpx;
			}

			.top-item-avatar {
				border-radius: 50%;
				width: 120rpx;
				height: 120rpx;
				border: 6rpx solid #e0e0e0;
			}

			.top-item-score {
				font-size: 24rpx;
				color: #666;
				margin-top: 10rpx;
				background: #f5f5f5;
				padding: 6rpx 16rpx;
				border-radius: 20rpx;
			}

			.top-item-name {
				margin: 10rpx 0;
				font-size: 28rpx;
				font-weight: 500;
				color: #333;
			}
			
			&.top-item-1 {
				.top-rank {
					background: linear-gradient(135deg, #ffd700, #ffa500);
				}
				.top-item-avatar {
					width: 140rpx;
					height: 140rpx;
					border-color: #ffd700;
				}
				.top-item-score {
					background: linear-gradient(135deg, #fff5e6, #fff);
					color: #e88a00;
				}
			}
			
			&.top-item-2 {
				.top-rank {
					background: linear-gradient(135deg, #c0c0c0, #a0a0a0);
				}
				.top-item-avatar {
					border-color: #c0c0c0;
				}
				.top-item-score {
					background: #f8f8f8;
					color: #888;
				}
			}
			
			&.top-item-3 {
				.top-rank {
					background: linear-gradient(135deg, #cd7f32, #a0522d);
				}
				.top-item-avatar {
					border-color: #cd7f32;
				}
				.top-item-score {
					background: #fdf5f0;
					color: #a0522d;
				}
			}
		}
	}

	.ranking-list-container {
		background: #fff;
		border-radius: 24rpx;
		padding: 10rpx 0;
		box-shadow: 0 2rpx 20rpx rgba(0, 0, 0, 0.04);
	}
	
	.ranking-list-header {
		font-size: 28rpx;
		font-weight: 600;
		color: #333;
		padding: 30rpx 30rpx 10rpx;
		border-bottom: 1rpx solid #f0f0f0;
	}

	.ranking {
		width: 100%;
		border-radius: 30rpx;
		background: #fff;
		box-sizing: border-box;
		overflow: hidden;

		.ranking-list-item {
			height: 110rpx;
			display: flex;
			align-items: center;
			padding: 0 30rpx;
			transition: background-color 0.2s;
			
			&:active {
				background-color: #fafafa;
			}

			.rank-number {
				width: 60rpx;
				font-size: 28rpx;
				font-weight: bold;
				color: #999;
			}

			.rank-score {
				color: #ff9d00;
				font-size: 26rpx;
				font-weight: 500;
				margin-left: auto;
			}

			.ranking-list-nickname {
				display: flex;
				align-items: center;
				flex: 1;

				image {
					width: 70rpx;
					height: 70rpx;
					border-radius: 50%;
					margin-right: 20rpx;
				}

				.nickname {
					font-size: 28rpx;
					color: #333;
				}
			}
		}
	}
</style>