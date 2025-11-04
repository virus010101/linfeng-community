<template>
	<view>
		<view>
			<view class="detail-container">
				<view class="user-item">
					<image @click="jumpUser(postDetail.uid)" :src="postDetail.userInfo.avatar"></image>
					<view class="user-item-user">
						<text class="user-name">{{ postDetail.userInfo.username }}</text>
						<view class="user-intro">{{ postDetail.userInfo.intro }}</view>
					</view>
					<u-button v-show="!isAuthor&&postDetail.isFollow" size="mini" style="float:right;font-size: 14px;"
						@click="cancelFollow">已关注</u-button>
					<u-button v-show="!isAuthor&&!postDetail.isFollow" size="mini"
						style="float:right;font-size: 14px;background-color: #333;color: #fff;"
						@click="follow">关注</u-button>
					<u-button v-show="isAuthor" size="mini" type="error" style="float:right;font-size: 14px;"
						@click="deletePost">删除</u-button>
				</view>
				<view class="icon">
					<text>{{ postDetail.createTime  }}</text>
				</view>
				<view class="hr"></view>
				<view class="post-title">{{ postDetail.title }}</view>
				<rich-text class="post-text" :nodes="postDetail.content"></rich-text>
				<!-- 图片 -->
				<view v-if="postDetail.type == 1">
					<!--一张图片-->
					<view v-if="postDetail.media.length == 1">
						<image class="img-style-1" @tap.stop="previewImage" mode="aspectFill"
							:data-current="postDetail.media[0]" :data-image="postDetail.media"
							:src="postDetail.media[0]">
						</image>
					</view>
					<!--二张图片-->
					<view v-if="postDetail.media.length == 2">
						<view class="img-style-2">
							<image v-for="(mediaItem, index2) in postDetail.media" :key="index2"
								@tap.stop="previewImage" mode="aspectFill" :data-current="mediaItem"
								:data-image="postDetail.media" :src="mediaItem">
							</image>
						</view>
					</view>
					<!--三张图片-->
					<view v-if="postDetail.media.length ==3">
						<view class="img-style-3">
							<image v-for="(mediaItem, index2) in postDetail.media" :key="index2"
								@tap.stop="previewImage" mode="aspectFill" :data-current="mediaItem"
								:data-image="postDetail.media" :src="mediaItem">
							</image>
						</view>
					</view>
					<!--四张图片-->
					<view v-if="postDetail.media.length == 4">
						<view class="img-style-4">
							<image v-for="(mediaItem, index2) in postDetail.media" :key="index2"
								@tap.stop="previewImage" mode="aspectFill" :data-current="mediaItem"
								:data-image="postDetail.media" :src="mediaItem">
							</image>
						</view>
					</view>

				</view>
			</view>

			<!--点赞、分享、评论-->
			<view class="menu-container">
				<view v-if="postDetail.isCollection">
					<view class="p-item" @click="cancelCollection">
						<text class="iconfont icon-lujing" style="color: #d81e06;"></text>
						<text>{{ postDetail.collectionCount}}</text>
					</view>
				</view>
				<view v-else>
					<view class="p-item" @click="addCollection">
						<text class="iconfont icon-shoucang"></text>
						<text>{{ postDetail.collectionCount }}</text>
					</view>
				</view>
				<view class="p-item" @click="focus = true">
					<text class="iconfont icon-pinglun"></text>
					<text>{{ postDetail.commentCount  || 0  }}</text>
				</view>
				<view class="p-item" @click="showShare = true">
					<text class="iconfont icon-forward"></text>
					<text>分享</text>
				</view>
			</view>
		</view>
		<u-line color="#d8d8d8"></u-line>
		<view class="comment-box">
			<view class="title">评论（{{ postDetail.commentCount }}）</view>
			<view style="margin-bottom: 40rpx;" v-for="(item, index) in commentList" :key="index">
				<view class="comment-item">
					<image @click="jumpUser(item.userInfo.uid)" class="avatar" :src="item.userInfo.avatar"></image>
					<view class="c-content">
						<view class="user">
							<text>{{ item.userInfo.username }}</text>
							<view v-if="item.isThumbs" @click.stop="cancelThumbs(item.id, index)" class="thumbs">
								<text class="num">{{ item.thumbs }}</text>
								<u-icon class="icon" size="40" name="heart-fill" color="#e62e00"></u-icon>
							</view>
							<view v-else @click.stop="onThumbs(item.id, index)" class="thumbs">
								<text class="num">{{ item.thumbs }}</text>
								<u-icon class="icon" size="40" name="heart-fill" color="#bfbfbf"></u-icon>
							</view>
						</view>
						<text class="c-txt">{{ item.content }}</text>
						<text class="time">{{ item.createTime  }}</text>
					</view>
				</view>
			</view>
			<!-- 加载状态 -->
			<view v-if="commentList.length > 0">
				<view style="margin: 30rpx;">
					<u-loadmore :status="loadStatus" />
				</view>
			</view>
			<view v-else>
				<u-empty text="暂无评论" mode="message"></u-empty>
			</view>
		</view>
		<view style="height: 100rpx;"></view>
		<!-- 评论输入框 -->
		<view class="comment-input">
			<textarea :placeholder="placeholder" @blur="onBlur" fixed="true" cursor-spacing="10" v-model="form.content"
				auto-height="true" placeholder-class="txt-placeholder" confirm-type="send"
				@confirm="addComment"></textarea>
			<u-button @click="addComment" :disabled="canSunbmit" :custom-style="btnStyle" style="border-radius: 0;">发布
			</u-button>
		</view>
		<!-- 分享选择弹窗 -->
		<u-popup v-model="showShare" mode="bottom" height="240rpx">
			<view class="post-share-container" @click="showShare = false">
				<!-- #ifdef MP -->
				<button open-type="share" class="share-item u-reset-button">
					<image src="/static/img/wechat.png"></image>
					<text>微信好友</text>
				</button>
				<!-- #endif -->
				<!-- #ifdef H5 -->
				<view class="share-item2" @click="copyPageUrl">
					<image src="/static/img/share.png"></image>
					<text>分享链接</text>
				</view>
				<!-- #endif -->
			</view>
		</u-popup>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance
	} from 'vue'
	import {
		onLoad,
		onReachBottom,
		onShareAppMessage,
		onShareTimeline
	} from '@dcloudio/uni-app'
	const {
		proxy
	} = getCurrentInstance()
	const btnStyle = reactive({
		color: "#fff",
		backgroundColor: '#333'
	})
	const postId = ref(0)
	const postDetail = reactive({
		comment: [],
		media: [],
		commentList: {
			data: []
		},
		userInfo: {}
	})
	const focus = ref(false)
	const canSunbmit = ref(false)
	const commentList = ref([])
	const placeholder = ref('文明发言哦')
	const form = reactive({
		pid: 0,
		type: 1,
		toUid: '',
		postId: '',
		content: ''
	})
	const showShare = ref(false)
	const shareBtnStyle = reactive({
		backgroundColor: '#333'
	})
	const page = ref(1)
	const loadStatus = ref('loadmore')
	const isAuthor = ref(false)

	function deletePost() {
		uni.showModal({
			title: '提示',
			content: '确定删除该帖子吗？',
			success: function(res) {
				if (res.confirm) {
					proxy.$H
						.post('post/deleteMyPost', {
							id: postId.value
						})
						.then(res => {
							if (res.code == 0) {
								uni.switchTab({
									url: "/pages/index/index"
								})
							}
						});
				}
			}
		});
	}

	function copyPageUrl() {
		uni.setClipboardData({
			data: proxy.$c.shareH5Url + 'pages/post/detail?id=' + postId.value,
			success: function() {
				uni.hideToast();
				uni.showToast({
					title: '复制成功可分享链接',
					icon: 'success',
					duration: 2000
				})
				showShare.value = false;
			}
		});
	}
	// 点赞
	function onThumbs(id, index, index2) {
		proxy.$H
			.post('comment/thumbs', {
				id: id
			})
			.then(res => {
				if (res.code == 0) {
					if (index2 || index2 == 0) {
						commentList.value[index].children[index2].isThumbs = true;
						commentList.value[index].children[index2].thumbs++;
					} else {
						commentList.value[index].isThumbs = true;
						commentList.value[index].thumbs++;
					}
				}
			});
	}
	// 取消点赞
	function cancelThumbs(id, index, index2) {
		proxy.$H
			.post('comment/cancelThumbs', {
				id: id
			})
			.then(res => {
				if (res.code == 0) {
					if (index2 || index2 == 0) {
						commentList.value[index].children[index2].isThumbs = false;
						commentList.value[index].children[index2].thumbs--;
					} else {
						commentList.value[index].isThumbs = false;
						commentList.value[index].thumbs--;
					}
				}
			});
	}
	// 输入框失去焦点时
	function onBlur() {
		placeholder.value = '留下你的精彩评论...';
		focus.value = false;
		// #ifdef MP
		form.pid = 0;
		// #endif
	}
	// 获取评论列表
	function getCommentList() {
		loadStatus.value = 'loading';
		proxy.$H
			.get('comment/list', {
				postId: postId.value,
				page: page.value
			})
			.then(res => {
				if (res.code == 0) {
					commentList.value = commentList.value.concat(res.result.data);
					if (res.result.current_page >= res.result.total || res.result.last_page === 0) {
						loadStatus.value = 'nomore';
					} else {
						loadStatus.value = 'loadmore';
					}
				}
			});
	}

	function jumpUser(uid) {
		uni.navigateTo({
			url: '/pages/user/home?uid=' + uid
		});
	}

	function addComment() {
		canSunbmit.value = true;
		if (form.content == '') {
			proxy.$u.toast('评论不能为空');
			canSunbmit.value = false;
			return;
		}
		uni.showLoading({
			mask: true,
			title: '发布中'
		});
		proxy.$H.post('post/addComment', form).then(res => {
			if (res.code == 0) {
				form.content = '';
				proxy.$u.toast('评论成功');
				page.value = 1;
				commentList.value = [];
				form.pid = 0;
				getCommentList();
			}
			canSunbmit.value = false;
			uni.hideLoading();
		});
	}

	function getPostDetail() {
		uni.showLoading({
			mask: true,
			title: '加载中'
		});
		proxy.$H
			.get('post/detail', {
				id: postId.value
			})
			.then(res => {
				if (res.code == 500) {
					proxy.$u.toast(res.msg);
					setTimeout(function() {
						uni.switchTab({
							url: '/pages/index/index'
						});
					}, 1500);
				}
				Object.assign(postDetail, res.result);
				if (uni.getStorageSync('hasLogin')) {
					proxy.$H.get('user/userInfo').then(res => {
						if (res.result.uid == postDetail.uid) {
							isAuthor.value = true
						}
					});
				}
				uni.hideLoading();
			});
	}

	function cancelCollection() {
		proxy.$H
			.post('post/cancelCollection', {
				id: postId.value
			})
			.then(res => {
				if (res.code === 0) {
					postDetail.isCollection = false;
					postDetail.collectionCount--;
				}
			});
	}

	function addCollection() {
		proxy.$H
			.post('post/addCollection', {
				id: postId.value,
				uid: postDetail.uid
			})
			.then(res => {
				if (res.code === 0) {
					postDetail.isCollection = true;
					postDetail.collectionCount++;
				}
			});
	}

	function addThumb() {
		proxy.$H
			.post('post/addThumb', {
				id: postId.value
			})
			.then(res => {
				if (res.code === 0) {
					postDetail.isThumb = true;
				}
			});
	}

	function cancelThumb() {
		proxy.$H
			.post('post/cancelThumb', {
				id: postId.value,
				uid: postDetail.uid
			})
			.then(res => {
				if (res.code === 0) {
					postDetail.isThumb = false;
				}
			});
	}

	function follow() {
		proxy.$H
			.post('user/addFollow', {
				id: postDetail.uid
			})
			.then(res => {
				if (res.code === 0) {
					postDetail.isFollow = true;
					proxy.$u.toast('关注成功')
				}
			});
	}

	function cancelFollow() {
		proxy.$H
			.post('user/cancelFollow', {
				id: postDetail.uid
			})
			.then(res => {
				if (res.code === 0) {
					postDetail.isFollow = false;
					proxy.$u.toast('已取消关注')
				}
			});
	}

	function previewImage(e) {
		uni.previewImage({
			current: e.currentTarget.dataset.current,
			urls: e.currentTarget.dataset.image
		});
	}

	onLoad((options) => {
		if (options.id) {
			postId.value = options.id;
		}
		form.postId = postId.value;
		getPostDetail();
		getCommentList();
		//#ifdef MP-WEIXIN
		wx.showShareMenu({
			withShareTicket: true,
			menus: ['shareAppMessage', 'shareTimeline']
		});
		//#endif
	})

	onReachBottom(() => {
		page.value++;
		getCommentList();
	})

	onShareAppMessage(() => {
		let imgURL = postDetail.media[0];
		return {
			title: postDetail.title,
			path: '/pages/post/post?id=' + postId.value,
			imageUrl: imgURL
		};
	})

	onShareTimeline(() => {
		let imgURL = postDetail.media[0];
		return {
			title: postDetail.title,
			imageUrl: imgURL,
			query: 'id=' + postId.value
		};
	})
</script>

<style lang="scss" scoped>
	.post-title {
		margin: 30rpx 0;
		font-weight: 700;
		font-size: 36rpx;
		line-height: 1.4;
		color: #333;
	}

	.detail-container {
		padding: 20rpx;
		background-color: #ffffff;
	}

	.icon {
		margin: 20rpx 0;

		text {
			font-size: 26rpx;
			color: #999;
			padding: 6rpx 16rpx;
			background-color: #f8f8f8;
			border-radius: 20rpx;
		}
	}

	.hr {
		height: 1rpx;
		background-color: #f5f5f5;
		margin: 20rpx 0;
	}

	/*关注*/

	.user-item {
		display: flex;

		.user-item-user {
			flex: 1;

			.user-intro {
				font-size: 12px;
				color: #999;
				display: -webkit-box;
				-webkit-box-orient: vertical;
				-webkit-line-clamp: 1;
				overflow: hidden;
				margin-top: 10rpx;
			}
		}

		image {
			width: 80rpx;
			height: 80rpx;
			float: left;
			margin-right: 10rpx;
			border-radius: 50%;
		}
	}

	.menu-container {
		margin: 30rpx;
		display: flex;
		font-size: 24rpx;
		color: #616161;

		.p-item {
			display: flex;
			align-items: center;

			.iconfont {
				font-size: 40rpx;
			}

			&:nth-child(2) {
				margin: 0 auto;
			}

			.iconfont {
				margin-right: 10rpx;
			}
		}
	}

	/*评论列表*/
	.comment-box {
		background-color: #ffffff;
		margin-top: 20rpx;
		padding: 20rpx;
	}

	.comment-box>.title {
		margin-bottom: 20rpx;
	}

	.comment-item {
		display: flex;
		padding: 20rpx;

		&:active {
			background-color: #F5F5F5;
		}

		.c-content {
			display: flex;
			flex-direction: column;
			flex: 1;

			.time {
				color: #999;
				font-size: 10px;
			}

			.user {
				display: flex;

				text {
					font-size: 30rpx;
					font-weight: 600;
					color: #333;

					&:hover {
						color: #2979ff;
					}
				}

				.thumbs {
					margin-left: auto;
					display: flex;
					align-items: center;
					color: #bfbfbf;

					.num {
						margin-right: 10rpx;
					}
				}
			}

			.c-txt {
				font-size: 28rpx;
				color: #666;
				line-height: 1.5;
				margin: 16rpx 0;
				word-break: break-all;
				white-space: pre-wrap;
			}
		}

		.avatar {
			width: 100rpx;
			height: 100rpx;
			border-radius: 50%;
			margin-right: 20rpx;
		}
	}

	.sub-comment-item {
		margin-left: 100rpx;
		padding: 20rpx;

		&:active {
			background-color: #F5F5F5;
		}

		.user {
			display: flex;
			align-items: center;

			.name {
				color: #616161;
			}

			.avatar {
				margin-right: 10rpx;
			}

			.u-head {
				flex: 1;
				display: flex;

				.thumbs {
					margin-left: auto;
					display: flex;
					align-items: center;
					color: #bfbfbf;

					.num {
						margin-right: 10rpx;
					}
				}
			}
		}

		.reply {
			.time {
				margin-left: auto;
				font-size: 20rpx;
				color: #999;
			}

			.name {
				display: inline-block;
				color: #55aaff;
				font-weight: 600;
			}
		}
	}

	.comment-input {
		position: fixed;
		bottom: 0;
		width: 100%;
		background-color: #fff;
		padding: 20rpx;
		display: flex;
		z-index: 999;
	}

	.comment-input textarea {
		background-color: #f5f5f5;
		padding: 20rpx;
		border-radius: 10rpx;
		min-height: 40rpx;
	}

	.comment-input .u-btn {
		margin-left: 10rpx;
	}


	.img-style-1 {
		display: block;
		width: 100%;
		max-height: 600rpx;
		border-radius: 5px;
	}

	.img-style-2 {
		display: flex;
	}

	.img-style-2 image {
		margin: 5rpx;
		border-radius: 5px;
		width: 100%;
		height: 294rpx;
	}

	.img-style-3 {
		display: flex;
		flex-wrap: wrap;
	}

	.img-style-3 image {
		width: 31.3%;
		height: 200rpx;
		margin: 1%;
		border-radius: 5px;
	}

	.img-style-4 {
		display: flex;
		flex-wrap: wrap;
	}

	.img-style-4 image {
		width: 48%;
		height: 320rpx;
		margin: 0.5%;
	}


	// 分享弹窗
	.post-share-container {
		display: flex;
		padding: 30rpx;
		width: 60%;
		margin: 0 auto;

		.share-item {
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;

			&:nth-child(1) {
				margin-right: auto;
			}

			image {
				width: 100rpx;
				height: 100rpx;
			}

			text {
				font-size: 24rpx;
				margin-top: 20rpx;
			}
		}

		.share-item2 {
			display: flex;
			flex-direction: column;
			justify-content: center;
			align-items: center;

			&:nth-child(1) {
				margin: auto;
			}

			image {
				width: 100rpx;
				height: 100rpx;
			}

			text {
				font-size: 24rpx;
				margin-top: 20rpx;
			}
		}
	}

	.post-text {
		white-space: pre-wrap;
	}
</style>