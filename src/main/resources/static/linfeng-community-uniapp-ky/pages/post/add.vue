<template>
	<view class="container">
		<u-line></u-line>
		<input v-model="form.title" class="title-input" placeholder="标题" />
		<textarea placeholder="说些什么叭..." :auto-height="true" maxlength="-1" v-model="form.content"
			class="content-display"></textarea>
		<u-line></u-line>
		<!-- 上传图片 -->
		<block v-if="form.type == 1">
			<u-upload ref="uUpload" :size-type="['original']" name="Image" :max-count="4" :header="header"
				:action="uploadImgUrl" @on-uploaded="submit" :auto-upload="false"></u-upload>
		</block>
		<u-line></u-line>
		<!-- 分类 -->
		<view @click="chooseClass" class="choose-item">
			<u-icon class="icon add-icon" name="file-text-fill" color="#999" size="40"></u-icon>
			<text class="txt">{{ cateName || '选择帖子分类' }}</text>
			<u-icon class="u-icon" name="arrow-right"></u-icon>
		</view>
		<view class="button-style">
			<u-button v-if="form.type == 1" :custom-style="btnStyle" @click="uploadImg" shape="circle">发布</u-button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				btnStyle: {
					color: "#fff",
					backgroundColor: '#333333'
				},
				uploadImgUrl: this.$c.domain + 'common/upload',
				form: {
					title: '',
					type: 1,
					topicId: 2,
					discussId: '',
					content: '',
					media: [],
					longitude: 0,
					latitude: 0,
					address: '',
					cut: 0, //分类id
					pay: '',
				},
				cateName: '',
				header: {
					token: uni.getStorageSync('token')
				},

			};
		},
		onLoad(options) {
			if (!uni.getStorageSync("hasLogin")) {
				this.$u.toast('请先登录哦');
			}
		},
		methods: {
			chooseClass() {
				uni.navigateTo({
					url: "category"
				})
			},
			uploadImg() {

				if (!this.form.content) {
					this.$u.toast('内容不能为空');
					return;
				}
				if (!this.form.title) {
					this.$u.toast('标题不能为空');
					return;
				}

				uni.showLoading({
					mask: true,
					title: '发布中'
				});
				this.$refs.uUpload.upload();
			},

			submit(e) {
				uni.showLoading({
					mask: true,
					title: '发布中'
				});

				let mediaList = [];
				e.forEach(function(item, index) {
					mediaList.push(item.response.result);
				});

				this.form.media = mediaList;

				this.$H.post('post/addPost', this.form).then(res => {
					if (res.code == 0) {
						uni.redirectTo({
							url: '/pages/post/post?id=' + res.result
						});
					}
					uni.hideLoading();
				});
			}
		}
	};
</script>

<style lang="scss" scoped>
	.container{
		padding: 30rpx;
	}
	.title-input {
		border-bottom: 1px solid #F5F5F5;
		margin: 20rpx 0;
		padding: 20rpx 0;
	}

	.content-display {
		width: 100%;
		padding: 20rpx 0;
		min-height: 300rpx;
	}

	.choose-item {
		display: flex;
		align-items: center;
		padding: 20rpx;
		border-bottom: 1px solid #F5F5F5;

		&:last-child {
			border: 0;
		}

		.txt {
			margin-left: 20rpx;
		}

		.sw {
			margin-left: 300rpx;
		}

		.inputStyle {
			margin-left: 60rpx;
			width: 400rpx;
		}

		.radio {
			margin-left: 320rpx;
		}

		.icon {
			width: 40rpx;
			height: 40rpx;
		}

		.u-icon {
			margin-left: auto;
			color: #999;
		}

		.add-icon {
			margin-left: 0;
		}
	}

	.button-style {
		margin-top: 50rpx;
		color: #F4F4F5;
	}
</style>
