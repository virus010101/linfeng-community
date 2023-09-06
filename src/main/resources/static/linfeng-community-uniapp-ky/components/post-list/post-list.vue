<template>
	<view>
		<block v-for="(item, index) in list" :key="item.id">
			<view @click="jump(item)">
				<view class="post-item">
					<view class="post-list-item">
						<view @click.stop="toUser(item.uid)">
							<u-avatar class="avatar" :src="item.userInfo.avatar" :show-level='item.userInfo.type == 1'
								level-bg-color="#000000"></u-avatar>
						</view>
						<view class="center">
							<view class="post-top-box">
								<view class="uname">
									<text v-if="item.userInfo.type == 1" class="official">官方</text>
									<text class="username">{{ item.userInfo.username.substring(0, 10) }}</text>
								</view>
								<view class="">
									<text v-if="item.postTop>0" class="officials">置顶</text>
									<text v-if="item.status==1" class="officials">审核中</text>
									<text v-if="item.status==2" class="officials">已下架</text>
								</view>

							</view>
							<view>
								<text class="time">{{ item.createTime}}</text>
							</view>
						</view>
					</view>

					<view class="post-content">
						<rich-text class="post-text" :nodes="item.content"></rich-text>
						<!-- 帖子类型 -->
						<block v-if="item.type == 1">
							<!--一张图片-->
							<block v-if="item.media.length == 1">
								<image :lazy-load="true" mode="aspectFill" class="img-style-1" :src="item.media[0]"
									@tap.stop="previewImage(item.media[0], item.media)"></image>
							</block>
							<!--二张图片-->
							<block v-if="item.media.length == 2">
								<view class="img-style-2">
									<image :lazy-load="true" v-for="(mediaItem, index2) in item.media" :key="index2"
										@tap.stop="previewImage(mediaItem, item.media)" mode="aspectFill"
										:src="mediaItem"></image>
								</view>
							</block>
							<!--三张图片-->
							<block v-if="item.media.length == 3">
								<view class="img-style-3">
									<image :lazy-load="true" v-for="(mediaItem, index2) in item.media" :key="index2"
										@tap.stop="previewImage(mediaItem, item.media)" mode="aspectFill"
										:src="mediaItem"></image>
								</view>
							</block>
							<!--四张图片-->
							<block v-if="item.media.length == 4">
								<view class="img-style-4">
									<image :lazy-load="true" v-for="(mediaItem, index2) in item.media" :key="index2"
										@tap.stop="previewImage(mediaItem, item.media)" mode="aspectFill"
										:src="mediaItem"></image>
								</view>
							</block>
						</block>
					</view>

					<!-- 位置 -->
					<view class="address" v-if="item.address">
						<u-icon class="icon" name="map-fill"></u-icon>
						<text>{{ item.address }}</text>
					</view>
					<!-- 底部 -->
					<view class="post-list-bottom">
						<view class="p-item margin50">
							<text class="iconfont icon-quanzi"></text>
							<text class="count">{{ item.readCount }}</text>
						</view>
						<view v-show="item.isCollection" class="p-item" @click.stop="cancelCollection(item.id,index)">
							<u-icon name="heart-fill" color="#cc0000"></u-icon>
							<text class="count">{{ item.collectionCount }}</text>
						</view>
						<view v-show="!item.isCollection" class="p-item" @click.stop="addCollection(item.id,index)">
							<u-icon name="heart"></u-icon>
							<text class="count">{{ item.collectionCount }}</text>
						</view>
						<view class="p-item margin50">
							<text class="iconfont icon-pinglun"></text>
							<text class="count">{{ item.commentCount }}</text>
						</view>
					</view>
				</view>
			</view>
		</block>
		<!-- 加载状态 -->
		<block v-if="list.length === 0 && loadStatus == 'nomore'">
			<u-empty margin-top="100" text="暂无内容" mode="favor"></u-empty>
		</block>
		<block v-else>
			<view style="margin: 30rpx 0;">
				<u-loadmore :status="loadStatus" />
			</view>
		</block>
	</view>
</template>

<script>
	export default {
		name: 'post-list',
		props: {
			list: Array,
			loadStatus: String,
			uid: Number,
		},
		data() {
			return {

			};
		},

		methods: {

			cancelCollection(id, index) {
				this.$H
					.post('post/cancelCollection', {
						id: id
					})
					.then(res => {
						if (res.code === 0) {
							this.list[index].isCollection = false;
							this.list[index].collectionCount--;
						}
					});
			},
			addCollection(id, index) {
				this.$H
					.post('post/addCollection', {
						id: id,
						uid: this.list[index].uid
					})
					.then(res => {
						if (res.code === 0) {
							this.list[index].isCollection = true;
							this.list[index].collectionCount++;
						}
					});
			},
			previewImage(url, urls) {
				uni.previewImage({
					current: url, // 当前显示图片的http链接
					urls: urls // 需要预览的图片http链接列表
				});
			},

			jump(e) {
				let url;

				// 图文
				if (e.type == 1 || e.type == 4) {
					url = '/pages/post/post?id=' + e.id;
				}

				uni.navigateTo({
					url: url
				});
			},

			toUser(uid) {
				uni.navigateTo({
					url: '/pages/user/home?uid=' + uid
				});
			}
		}
	};
</script>

<style lang="scss" scoped>
	.post-item {
		background: #fff;
		padding: 20rpx;
		margin-bottom: 20rpx;

		.post-content {
			margin-top: 20rpx;

			.img-style-1 {
				display: block;
				width: 100%;
				height: 600rpx;
				border-radius: 5px;
				overflow: hidden;

			}

			.img-style-2 {
				display: flex;

				image {
					margin: 5rpx;

					width: 100%;
					height: 305rpx;
				}
			}

			.img-style-3 {
				display: flex;
				flex-wrap: wrap;

				image {
					width: 31.3%;
					height: 200rpx;
					margin: 0.6%;

				}
			}

			.img-style-4 {
				display: flex;
				flex-wrap: wrap;

				image {
					width: 48%;
					height: 320rpx;
					margin: 0.5%;

				}
			}
		}

		.address {
			display: flex;
			font-size: 20rpx;
			background-color: #F5F5F5;
			border-radius: 20rpx;
			display: inline-block;
			padding: 5rpx 20rpx;
			margin: 20rpx 0;

			.icon {
				margin-right: 5rpx;
			}
		}
	}
	.post-top-box{
		display: flex;
		align-items: center;
		justify-content: space-between;
	}
	.uname{
		display: flex;
		align-items: center;
	}
	.post-list-item {
		display: flex;
		align-items: center;

		.avatar {
			width: 85rpx;
			height: 85rpx;
			border-radius: 50%;
			margin-right: 20rpx;
		}

		.center {
			flex: 1;
			display: flex;
			flex-direction: column;
			font-size: 24rpx;
			color: #999;

			.username {
				font-size: 32rpx;
				font-weight: 600;
				color: #616161;
			}

			.official {
				display: inline-block;
				width: 65rpx;
				height: 35rpx;
				font-size: 20rpx;
				font-weight: bold;
				text-align: center;
				line-height: 35rpx;
				color: #ffffff;
				background-color: #000000;

				border-radius: 30rpx;
				margin-right: 10rpx;
			}

			.officials {
				display: inline-block;
				font-size: 25rpx;
				color: #ffffff;
				background-color: #c0392b;
				padding: 2rpx 8rpx;
				border-radius: 10rpx;
				margin-left: 30rpx;
			}
		}

		.right {
			height: 85rpx;

			.arrow-down {
				color: #999;
			}
		}
	}

	.post-text {
		display: block;
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 10;
		white-space: pre-wrap;
		overflow: hidden;
	}

	.post-list-bottom {
		display: flex;
		margin: 20rpx 0;

		.p-item {
			margin: 0 auto;
			color: #616161;
			display: flex;
			align-items: center;

			.count {
				margin-left: 10rpx;
				font-size: 28rpx;
			}
		}

		.p-item[hidden] {
			display: none !important;
		}
	}
</style>