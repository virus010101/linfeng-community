<template>
	<view class="post-feed">
		<view v-for="(item, index) in list" :key="item.id" class="post-card" @click="jump(item)">
			<view class="post-head">
				<view class="avatar-wrap" @click.stop="toUser(item.uid)">
					<u-avatar class="avatar" :src="item.userInfo.avatar" :show-level="item.userInfo.type == 1"
						level-bg-color="#222"></u-avatar>
				</view>
				<view class="post-meta">
					<view class="meta-row">
						<view class="uname">
							<text v-if="item.userInfo.type == 1" class="tag tag-official">官方</text>
							<text class="username">{{ item.userInfo.username.substring(0, 10) }}</text>
						</view>
						<view class="post-tags" v-if="item.postTop > 0 || item.status == 1 || item.status == 2">
							<text v-if="item.postTop > 0" class="tag tag-top">置顶</text>
							<text v-if="item.status == 1" class="tag tag-status">审核中</text>
							<text v-if="item.status == 2" class="tag tag-status">已下架</text>
						</view>
					</view>
					<text class="time">{{ item.createTime }}</text>
				</view>
			</view>

			<view class="post-content">
				<rich-text class="post-text" :nodes="item.content"></rich-text>
				<view v-if="item.type == 1 && item.media && item.media.length" class="media-wrap">
					<view v-if="item.media.length == 1" class="media-one">
						<image :lazy-load="true" mode="aspectFill" class="media-img" :src="item.media[0]"
							@tap.stop="previewImage(item.media[0], item.media)"></image>
					</view>
					<view v-else-if="item.media.length == 2" class="media-two">
						<image :lazy-load="true" v-for="(mediaItem, index2) in item.media" :key="index2"
							@tap.stop="previewImage(mediaItem, item.media)" mode="aspectFill" class="media-img"
							:src="mediaItem"></image>
					</view>
					<view v-else-if="item.media.length == 3" class="media-three">
						<image :lazy-load="true" v-for="(mediaItem, index2) in item.media" :key="index2"
							@tap.stop="previewImage(mediaItem, item.media)" mode="aspectFill" class="media-img"
							:src="mediaItem"></image>
					</view>
					<view v-else-if="item.media.length >= 4" class="media-four">
						<image :lazy-load="true" v-for="(mediaItem, index2) in item.media.slice(0, 4)" :key="index2"
							@tap.stop="previewImage(mediaItem, item.media)" mode="aspectFill" class="media-img"
							:src="mediaItem"></image>
					</view>
				</view>
			</view>

			<view class="address" v-if="item.address">
				<u-icon class="address-icon" name="map-fill" size="24" color="#999"></u-icon>
				<text class="address-text">{{ item.address }}</text>
			</view>

			<view class="post-foot">
				<view class="foot-item">
					<text class="iconfont icon-yuedu"></text>
					<text class="foot-num">{{ item.readCount }}</text>
				</view>
				<view class="foot-item" @click.stop="item.isCollection ? cancelCollection(item.id, index) : addCollection(item.id, index)">
					<text v-if="item.isCollection" class="iconfont icon-lujing foot-icon-active"></text>
					<text v-else class="iconfont icon-shoucang"></text>
					<text class="foot-num">{{ item.collectionCount }}</text>
				</view>
				<view class="foot-item">
					<text class="iconfont icon-pinglun"></text>
					<text class="foot-num">{{ item.commentCount }}</text>
				</view>
			</view>
		</view>

		<view v-if="list.length === 0 && loadStatus == 'nomore'">
			<u-empty margin-top="100" text="暂无内容" mode="favor"></u-empty>
		</view>
		<view v-else class="loadmore-wrap">
			<u-loadmore :status="loadStatus" />
		</view>
	</view>
</template>

<script setup>
	import {
		getCurrentInstance
	} from 'vue'
	const props = defineProps({
		list: {
			type: Array,
			default: () => []
		},
		loadStatus: {
			type: String,
			default: ''
		},
		uid: {
			type: Number,
			default: undefined
		},
	})
	const {
		proxy
	} = getCurrentInstance()

	function cancelCollection(id, index) {
		proxy.$H
			.post('post/cancelCollection', {
				id: id
			})
			.then(res => {
				if (res.code === 0) {
					props.list[index].isCollection = false
					props.list[index].collectionCount--
				}
			})
	}

	function addCollection(id, index) {
		proxy.$H
			.post('post/addCollection', {
				id: id,
				uid: props.list[index].uid
			})
			.then(res => {
				if (res.code === 0) {
					props.list[index].isCollection = true
					props.list[index].collectionCount++
				}
			})
	}

	function previewImage(url, urls) {
		uni.previewImage({
			current: url,
			urls: urls
		})
	}

	function jump(e) {
		uni.navigateTo({
			url: '/pages/post/post?id=' + e.id
		})
	}

	function toUser(uid) {
		uni.navigateTo({
			url: '/pages/user/home?uid=' + uid
		})
	}
</script>

<style lang="scss" scoped>
	.post-feed {
		background: #f5f5f5;
		min-height: 200rpx;
	}

	.post-card {
		background: #fff;
		padding: 28rpx 32rpx 24rpx;
		margin-bottom: 16rpx;

		&:active {
			background: #fafafa;
		}
	}

	.post-head {
		display: flex;
		align-items: flex-start;
	}

	.avatar-wrap {
		flex-shrink: 0;
		margin-right: 20rpx;
	}

	.avatar {
		width: 76rpx;
		height: 76rpx;
	}

	.post-meta {
		flex: 1;
		min-width: 0;
		padding-top: 4rpx;
	}

	.meta-row {
		display: flex;
		align-items: center;
		justify-content: space-between;
		gap: 16rpx;
	}

	.uname {
		display: flex;
		align-items: center;
		min-width: 0;
		flex: 1;
	}

	.username {
		font-size: 30rpx;
		font-weight: 600;
		color: #222;
		line-height: 42rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.post-tags {
		display: flex;
		align-items: center;
		flex-shrink: 0;
		gap: 8rpx;
	}

	.tag {
		font-size: 20rpx;
		line-height: 32rpx;
		padding: 0 10rpx;
		border-radius: 4rpx;
	}

	.tag-official {
		color: #fff;
		background: #222;
		margin-right: 8rpx;
	}

	.tag-top {
		color: #666;
		background: #f0f0f0;
	}

	.tag-status {
		color: #c0392b;
		background: #fdf0ee;
	}

	.time {
		display: block;
		margin-top: 6rpx;
		font-size: 24rpx;
		color: #999;
		line-height: 34rpx;
	}

	.post-content {
		margin-top: 20rpx;
	}

	.post-text {
		display: block;
		font-size: 30rpx;
		color: #333;
		line-height: 1.55;
		word-break: break-all;
		overflow: hidden;
	}

	.media-wrap {
		margin-top: 16rpx;
	}

	.media-img {
		display: block;
		width: 100%;
		border-radius: 8rpx;
		background: #f5f5f5;
	}

	.media-one .media-img {
		max-height: 520rpx;
	}

	.media-two {
		display: flex;
		gap: 8rpx;

		.media-img {
			flex: 1;
			width: 0;
			height: 280rpx;
		}
	}

	.media-three {
		display: flex;
		gap: 8rpx;

		.media-img {
			flex: 1;
			width: 0;
			height: 210rpx;
		}
	}

	.media-four {
		display: flex;
		flex-wrap: wrap;
		gap: 8rpx;

		.media-img {
			width: calc((100% - 8rpx) / 2);
			height: 240rpx;
		}
	}

	.address {
		display: inline-flex;
		align-items: center;
		max-width: 100%;
		margin-top: 16rpx;
		padding: 6rpx 16rpx;
		background: #f7f7f7;
		border-radius: 6rpx;
	}

	.address-icon {
		flex-shrink: 0;
		margin-right: 6rpx;
	}

	.address-text {
		font-size: 22rpx;
		color: #888;
		line-height: 32rpx;
		overflow: hidden;
		text-overflow: ellipsis;
		white-space: nowrap;
	}

	.post-foot {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-top: 24rpx;
		padding-top: 20rpx;
		border-top: 1rpx solid #f0f0f0;
	}

	.foot-item {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;
		color: #666;
		font-size: 26rpx;
		line-height: 1;

		.iconfont {
			font-size: 36rpx;
			color: #888;
		}

		.foot-icon-active {
			color: #d23b3b;
		}
	}

	.foot-num {
		margin-left: 8rpx;
		font-size: 26rpx;
		color: #666;
	}

	.loadmore-wrap {
		padding: 24rpx 0 40rpx;
		background: #f5f5f5;
	}
</style>
