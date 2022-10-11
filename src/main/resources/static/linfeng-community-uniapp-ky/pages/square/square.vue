<template>
	<view>
		<!-- navbar -->
		<u-navbar :is-back="false" :border-bottom="false">
			<u-icon name="search" :size="40" class="search-wrap" @click="toSearch"></u-icon>
			<u-tabs :list="pageTab" :current="pageCurrent" @change="pageTabChange"></u-tabs>

		</u-navbar>
		<!-- 创作广场 -->
		<view v-show="pageCurrent == 0">
			<!-- 分类 -->
			<view class="tabs-wrap">
				<u-tabs :list="classList" name="cateName" :current="current" @change="tabChange"></u-tabs>
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
		<!-- 发帖达人 -->
		<view v-show="pageCurrent == 1">
			<navigator :url="'/pages/user/home?uid=' + item.uid" class="user-item" hover-class="none"
				v-for="(item, index) in userList" :key="index">
				<view v-if="index < 10" class="user-index-hot">{{ index + 1 }}</view>
				<view v-else class="user-index">{{ index + 1 }}</view>
				<image class="avatar" mode="aspectFill" :src="item.avatar"></image>
				<view class="right">
					<text class="username">{{ item.username }}</text>
					<view class="tag-wrap">
						<text class="tag" :key="index2">{{ item.intro }}</text>
						<text class="tag" :key="index2">+{{ item.postNumber }}</text>
					</view>
				</view>
			</navigator>
			<!-- 加载状态 -->
			<block v-if="userList.length === 0 && loadStatus == 'nomore'">
				<u-empty margin-top="100" text="暂无内容" mode="favor"></u-empty>
			</block>
			<block v-else>
				<view style="margin: 30rpx 0;">
					<u-loadmore :status="loadStatus" />
				</view>
			</block>
		</view>


		<!-- 发帖入口 -->
		<add-post-tag></add-post-tag>
	</view>
</template>

<script>
	import addPostTag from '../../components/add-post-tag/add-post-tag.vue'
	export default {
		data() {
			return {
				pageCurrent: 0,
				current: 0,
				pageTab: [{
						name: '创作广场'
					},
					{
						name: '发帖达人'
					}
				],
				classList: [{
					cateId: 0,
					cateName: '推荐'
				}],
				swiperList: [],
				postList: [],
				loadPostStatus: 'loadmore',
				classId: 0,
				page: 1,
				userList: [],
			}
		},
		onLoad() {
			this.getBannerList();
			this.getPostList();
			this.getClassList();
			this.getUserRanking();
		},
		onReachBottom() {
			if (this.pageCurrent == 0) {
				this.page++;
				this.getPostList()
			}
			if (this.pageCurrent == 1) {
				this.userList = [];
				this.getUserRanking();
			}
		},
		onPullDownRefresh() {
			if (this.pageCurrent == 0) {
				this.page = 1
				this.pageList = []
				this.getPostList()
			}
			if (this.pageCurrent == 1) {
				this.userList = [];
				this.getUserRanking();
			}
		},
		methods: {
			getBannerList() {
				this.$H.get('link/list').then(res => {
					if (res.code == 0) {
						this.swiperList = res.result
					}
				})
			},
			toSearch() {
				uni.navigateTo({
					url: '/pages/search/search'
				})
			},
			pageTabChange(index) {
				this.pageCurrent = index
			},
			tabChange(index) {
				this.current = index
				this.page = 1
				this.classId = this.classList[index].cateId
				this.postList = []
				this.getPostList()
			},
			getClassList() {
				this.$H.get('topic/classList').then(res => {
					console.log(res.result)
					this.classList = this.classList.concat(res.result)
				})
			},
			getUserRanking() {
				this.$H
					.post('user/userRank')
					.then(res => {
						this.userList = res.result;
					});
			},
			// 根据分页和分类展示帖子列表
			getPostList() {
				console.log('classId:', this.classId)
				this.loadPostStatus = 'loading';
				this.$H
					.post('post/list', {
						classId: this.classId,
						page: this.page
					})
					.then(res => {
						this.postList = this.postList.concat(res.result.data);
						if (res.result.current_page === res.result.last_page || res.result.last_page === 0) {
							this.loadPostStatus = 'nomore';
						} else {
							this.loadPostStatus = 'loadmore';
						}
					});
			},
		}
	}
</script>

<style lang="scss" scoped>
	.search-wrap {
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

	// 用户列表
	.user-item {
		margin: 30rpx;
		padding: 20rpx;
		display: flex;
		border-bottom: 1px solid #f5f5f5;

		.user-index-hot {
			margin-right: 20rpx;
			color: #fff;
			background-image: linear-gradient(#e64340, #ffaac3);
			width: 55rpx;
			height: 55rpx;
			border-radius: 50%;
			text-align: center;
			line-height: 55rpx;
		}

		.user-index {
			margin-right: 20rpx;
			color: #aaaaff;
			font-weight: bold;
			display: flex;
			justify-content: center;
			align-items: center;
		}

		.avatar {
			width: 100rpx;
			height: 100rpx;
			border-radius: 10rpx;
			margin-right: 20rpx;
		}

		.right {
			flex: 1;

			.username {
				font-weight: bold;
			}

			.tag-wrap {
				font-size: 20rpx;

				.tag {
					display: inline-block;
					padding: 5rpx 20rpx;
					border-radius: 10rpx;
					margin-right: 20rpx;
					margin-bottom: 20rpx;
					background-color: #7da9bd;

					&:nth-child(2n) {
						background-color: #ccb3ff;
					}

				}
			}
		}
	}
</style>
