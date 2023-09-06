<template>
	<view>
		<view>
			<view class="detail-container">
				<view class="user-item">
					<image @click="jumpUser(postDetail.uid)" :src="postDetail.userInfo.avatar"></image>
					<view class="user-item-user">
						<text class="user-name">{{ postDetail.userInfo.username }}</text>
						<view class="userIntro">{{ postDetail.userInfo.intro }}</view>
					</view>
					<u-button v-show="postDetail.isFollow" size="mini" style="float:right;font-size: 14px;"
						@click="cancelFollow">已关注</u-button>
					<u-button v-show="!postDetail.isFollow" size="mini" style="float:right;font-size: 14px;"
						@click="follow">关注</u-button>
				</view>
				<view class="icon">
					<text>{{ postDetail.createTime  }}</text>
				</view>
				<view class="hr"></view>
				<view class="post-title">{{ postDetail.title }}</view>
				<rich-text class="post-text" :nodes="postDetail.content"></rich-text>
				<!-- 图片 -->
				<block v-if="postDetail.type == 1">
					<!--一张图片-->
					<block v-if="postDetail.media.length == 1">
						<image class="img-style-1" @tap.stop="previewImage" mode="aspectFill"
							:data-current="postDetail.media[0]" :data-image="postDetail.media"
							:src="postDetail.media[0]">
						</image>
					</block>
					<!--二张图片-->
					<block v-if="postDetail.media.length == 2">
						<view class="img-style-2">
							<image v-for="(mediaItem, index2) in postDetail.media" :key="index2"
								@tap.stop="previewImage" mode="aspectFill" :data-current="mediaItem"
								:data-image="postDetail.media" :src="mediaItem">
							</image>
						</view>
					</block>
					<!--三张图片-->
					<block v-if="postDetail.media.length ==3">
						<view class="img-style-3">
							<image v-for="(mediaItem, index2) in postDetail.media" :key="index2"
								@tap.stop="previewImage" mode="aspectFill" :data-current="mediaItem"
								:data-image="postDetail.media" :src="mediaItem">
							</image>
						</view>
					</block>
					<!--四张图片-->
					<block v-if="postDetail.media.length == 4">
						<view class="img-style-4">
							<image v-for="(mediaItem, index2) in postDetail.media" :key="index2"
								@tap.stop="previewImage" mode="aspectFill" :data-current="mediaItem"
								:data-image="postDetail.media" :src="mediaItem">
							</image>
						</view>
					</block>

				</block>
			</view>

			<!--点赞、分享、评论-->
			<view class="menu-container">
				<block v-if="postDetail.isCollection">
					<view class="p-item" @click="cancelCollection">
						<text class="iconfont icon-lujing" style="color: #d81e06;"></text>
						<text>{{ postDetail.collectionCount}}</text>
					</view>
				</block>
				<block v-else>
					<view class="p-item" @click="addCollection">
						<text class="iconfont icon-shoucang"></text>
						<text>{{ postDetail.collectionCount }}</text>
					</view>
				</block>
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
							<block v-if="item.isThumbs">
								<view @click.stop="cancelThumbs(item.id, index)" class="thumbs">
									<text class="num">{{ item.thumbs }}</text>
									<u-icon class="icon" size="40" name="heart-fill" color="#e62e00"></u-icon>
								</view>
							</block>
							<block v-else>
								<view @click.stop="onThumbs(item.id, index)" class="thumbs">
									<text class="num">{{ item.thumbs }}</text>
									<u-icon class="icon" size="40" name="heart-fill" color="#bfbfbf"></u-icon>
								</view>
							</block>
						</view>
						<text class="c-txt">{{ item.content }}</text>
						<text class="time">{{ item.createTime  }}</text>
					</view>
				</view>
			</view>
			<!-- 加载状态 -->
			<block v-if="commentList.length > 0">
				<view style="margin: 30rpx;">
					<u-loadmore :status="loadStatus" />
				</view>
			</block>
			<block v-else>
				<u-empty text="暂无评论" mode="message"></u-empty>
			</block>
		</view>
		<view style="height: 100rpx;"></view>
		<!-- 评论输入框 -->
		<view class="comment-input">
			<textarea :placeholder="placeholder" @blur="onBlur" :focus="focus" fixed="true" cursor-spacing="10"
				v-model="form.content" auto-height="true" placeholder-class="txt-placeholder" confirm-type="send"
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
	</view>
</template>

<script>
	export default {
		data() {
			return {
				btnStyle: {
					color: "#fff",
					backgroundColor: '#55aaff'
				},
				postId: 0,
				postDetail: {
					comment: [],
					media: [],
					commentList: {
						data: []
					},
					topicInfo: {
						topicName: ''
					}
				},
				focus: false,
				canSunbmit: false,
				commentList: [],
				placeholder: '文明发言哦',
				form: {
					pid: 0,
					type: 1,
					toUid: '',
					postId: '',
					content: ''
				},
				showShare: false,
				shareBtnStyle: {
					backgroundColor: '#333'
				},
				page: 1,
				loadStatus: 'loadmore',

			};
		},
		onLoad(options) {
			this.postId = options.id;

			if (options.scene) {
				this.postId = options.scene;
			}
			if (options.mid) {
				this.messageRead(options.mid);
			}

			this.form.postId = this.postId;
			this.getPostDetail();
			this.getCommentList();

			//#ifdef MP-WEIXIN
			wx.showShareMenu({
				withShareTicket: true,
				menus: ['shareAppMessage', 'shareTimeline']
			});
			//#endif
		},
		onReachBottom() {
			this.page++;
			this.getCommentList();
		},
		onShareAppMessage(res) {
			let imgURL;
			if (this.postDetail.media.length > 0) {
				imgURL = this.postDetail.media[0];
			}
			return {
				title: this.postDetail.title,
				path: '/pages/post/post?id=' + this.postId,
				imageUrl: imgURL
			};
		},
		onShareTimeline() {
			let imgURL = (imgURL = this.postDetail.media[0]);
			return {
				title: this.postDetail.title,
				imageUrl: imgURL,
				query: 'id=' + this.postId
			};
		},
		methods: {
			messageRead(mid) {
				this.$H
					.post('message/readMessage', {
						postId: this.postId,
						mid: mid
					})
					.then(res => {

					});
			},
			copyPageUrl() {
				let that = this;
				uni.setClipboardData({
					data: this.$c.shareH5Url + 'pages/post/detail?id=' + this.postId,
					success: function() {
						uni.hideToast();
						uni.showToast({
							title: '复制成功快分享给好友叭~',
							icon: 'success',
							duration: 2000,
						})
						that.showShare = false;
					}
				});
			},
			// 点赞
			onThumbs(id, index, index2) {
				this.$H
					.post('comment/thumbs', {
						id: id
					})
					.then(res => {
						if (res.code == 0) {
							if (index2 || index2 == 0) {
								this.commentList[index].children[index2].isThumbs = true;
								this.commentList[index].children[index2].thumbs++;
							} else {
								this.commentList[index].isThumbs = true;
								this.commentList[index].thumbs++;
							}
						}
					});
			},
			// 取消点赞
			cancelThumbs(id, index, index2) {
				this.$H
					.post('comment/cancelThumbs', {
						id: id
					})
					.then(res => {
						if (res.code == 0) {
							if (index2 || index2 == 0) {
								this.commentList[index].children[index2].isThumbs = false;
								this.commentList[index].children[index2].thumbs--;
							} else {
								this.commentList[index].isThumbs = false;
								this.commentList[index].thumbs--;
							}
						}
					});
			},
			// 输入框失去焦点时
			onBlur() {
				this.placeholder = '留下你的精彩评论...';
				this.focus = false;
				// #ifdef MP
				this.form.pid = 0;
				// #endif
			},
			// 获取评论列表
			getCommentList() {
				this.loadStatus = 'loading';
				this.$H
					.get('comment/list', {
						postId: this.postId,
						page: this.page
					})
					.then(res => {
						if (res.code == 0) {
							this.commentList = this.commentList.concat(res.result.data);

							if (res.result.current_page >= res.result.total || res.result.last_page === 0) {
								this.loadStatus = 'nomore';
							} else {
								this.loadStatus = 'loadmore';
							}
						}
					});
			},
			jumpUser(uid) {
				uni.navigateTo({
					url: '/pages/user/home?uid=' + uid
				});
			},
			addComment() {
				this.canSunbmit = true;
				if (this.form.content == '') {
					this.$u.toast('评论不能为空');
					this.canSunbmit = false;
					return;
				}

				uni.showLoading({
					mask: true,
					title: '发布中'
				});
				this.$H.post('post/addComment', this.form).then(res => {
					if (res.code == 0) {
						this.form.content = '';
						this.$u.toast('评论成功');
						this.page = 1;
						this.commentList = [];
						this.form.pid = 0;
						this.getCommentList();
					}
					this.canSunbmit = false;
					uni.hideLoading();
				});
			},
			getPostDetail() {
				uni.showLoading({
					mask: true,
					title: '加载中'
				});
				this.$H
					.get('post/detail', {
						id: this.postId
					})
					.then(res => {
						if (res.code == 500) {
							this.$u.toast(res.msg);
							setTimeout(function() {
								uni.switchTab({
									url: '/pages/index/index'
								});
							}, 1500);
						}
						this.postDetail = res.result;
						uni.hideLoading();
					});
			},
			cancelCollection() {
				this.$H
					.post('post/cancelCollection', {
						id: this.postId
					})
					.then(res => {
						if (res.code === 0) {
							this.postDetail.isCollection = false;
							this.postDetail.collectionCount--;
						}
					});
			},
			addCollection() {
				this.$H
					.post('post/addCollection', {
						id: this.postId,
						uid: this.postDetail.uid
					})
					.then(res => {
						if (res.code === 0) {
							this.postDetail.isCollection = true;
							this.postDetail.collectionCount++;
						}
					});
			},
			addThumb() {
				this.$H
					.post('post/addThumb', {
						id: this.postId
					})
					.then(res => {
						if (res.code === 0) {
							this.postDetail.isThumb = true;
						}
					});
			},
			cancelThumb() {
				this.$H
					.post('post/cancelThumb', {
						id: this.postId,
						uid: this.postDetail.uid
					})
					.then(res => {
						if (res.code === 0) {
							this.postDetail.isThumb = false;
						}
					});
			},
			follow() {
				this.$H
					.post('user/addFollow', {
						id: this.postDetail.uid
					})
					.then(res => {
						if (res.code === 0) {
							this.postDetail.isFollow = true;
						} else {
							this.$refs.uToast.show({
								title: res.msg,
								type: 'error'
							});
						}
					});
			},
			cancelFollow() {
				this.$H
					.post('user/cancelFollow', {
						id: this.postDetail.uid
					})
					.then(res => {
						if (res.code === 0) {
							this.postDetail.isFollow = false;
						}
					});
			},
			previewImage(e) {
				uni.previewImage({
					current: e.currentTarget.dataset.current, // 当前显示图片的http链接
					urls: e.currentTarget.dataset.image // 需要预览的图片http链接列表
				});
			}
		}
	};
</script>

<style lang="scss" scoped>
	.post-title {
		margin: 20rpx 0;
	}

	.detail-container {
		padding: 20rpx;
		background-color: #ffffff;
	}

	.icon text {
		font-size: 12px;
		color: #999;
		margin-right: 20rpx;
	}

	/*关注*/

	.user-item {
		display: flex;

		.user-item-user {
			flex: 1;

			.userIntro {
				font-size: 12px;
				color: #999;
				display: -webkit-box;
				-webkit-box-orient: vertical;
				-webkit-line-clamp: 1;
				overflow: hidden;
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
