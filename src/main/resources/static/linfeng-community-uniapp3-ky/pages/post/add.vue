<template>
	<view class="container">
		<u-line></u-line>
		<view class="input-wrapper">
			<input v-model="form.title" class="title-input" placeholder="标题" maxlength="30" />
			<text class="char-count">{{ form.title.length }}/30</text>
		</view>
		<view class="textarea-wrapper">
			<textarea placeholder="说些什么叭..." :auto-height="true" maxlength="2000" v-model="form.content"
				class="content-display"></textarea>
			<text class="char-count">{{ form.content.length }}/2000</text>
		</view>
		<u-line></u-line>
		<!-- 上传图片 -->
		<view v-if="form.type == 1" class="upload-wrapper">
			<!-- <view class="upload-header">
				<text class="upload-title">上传图片</text>
				<text class="upload-tip">最多4张</text>
			</view> -->
			<u-upload ref="uploadRef" :size-type="['original']" name="Image" :max-count="4" :header="header"
				:action="uploadImgUrl" @on-uploaded="submit" :auto-upload="false"></u-upload>
		</view>
		<u-line></u-line>
		<!-- 分类 -->
		<view @click="chooseClass" class="choose-item">
			<u-icon class="icon add-icon" name="file-text-fill" color="#999" size="40"></u-icon>
			<text class="txt">{{ cateName || '选择帖子分类' }}</text>
			<u-icon class="u-icon" name="arrow-right"></u-icon>
		</view>
		<view class="button-style">
			<u-button :custom-style="btnStyle" @click="uploadImg" shape="circle">发布</u-button>
		</view>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance,
		onUnmounted
	} from 'vue'
	import {
		onLoad
	} from '@dcloudio/uni-app'
	const {
		proxy
	} = getCurrentInstance()
	const btnStyle = reactive({
		color: '#fff',
		backgroundColor: '#333333'
	})
	const uploadImgUrl = proxy.$c.domain + 'common/upload'
	const form = reactive({
		title: '',
		type: 1,
		topicId: 2,
		discussId: '',
		content: '',
		media: [],
		longitude: 0,
		latitude: 0,
		address: '',
		cut: 0,
		pay: ''
	})
	const cateName = ref('')
	const header = reactive({
		token: uni.getStorageSync('token')
	})
	const uploadRef = ref(null)

	function chooseClass() {
		uni.navigateTo({
			url: 'category'
		})
	}

	function handleCategorySelect(data) {
		form.cut = data.id;
		cateName.value = data.name;
	}

	function uploadImg() {
		if (!form.content) {
			proxy.$u.toast('内容不能为空')
			return
		}
		if (!form.title) {
			proxy.$u.toast('标题不能为空')
			return
		}
		uni.showLoading({
			mask: true,
			title: '发布中'
		})
		uploadRef.value?.upload()
	}

	function submit(e) {
		uni.showLoading({
			mask: true,
			title: '发布中'
		})
		const mediaList = []
		e.forEach(function(item) {
			mediaList.push(item.response.result)
		})
		form.media = mediaList
		proxy.$H.post('post/addPost', form).then(res => {
			if (res.code == 0) {
				uni.redirectTo({
					url: '/pages/post/post?id=' + res.result
				})
			}
			uni.hideLoading()
		})
	}

	onLoad(() => {
		if (!uni.getStorageSync('hasLogin')) {
			proxy.$u.toast('请先登录哦')
		}
		// 监听分类选择事件
		uni.$on('selectCategory', handleCategorySelect)
	})

	onUnmounted(() => {
		// 清理事件监听
		uni.$off('selectCategory', handleCategorySelect)
	})
</script>

<style lang="scss" scoped>
	.container {
		padding: 30rpx;
	}

	.input-wrapper {
		position: relative;
		margin: 20rpx 0;
		padding: 20rpx 0;
		border-bottom: 1px solid #F5F5F5;
		
		.title-input {
			width: 100%;
			padding-right: 100rpx;
		}
		
		.char-count {
			position: absolute;
			right: 0;
			bottom: 30rpx;
			font-size: 24rpx;
			color: #999;
		}
	}

	.textarea-wrapper {
		position: relative;
		padding: 20rpx 0;
		
		.content-display {
			width: 100%;
			min-height: 300rpx;
			padding-right: 100rpx;
		}
		
		.char-count {
			position: absolute;
			right: 0;
			bottom: 30rpx;
			font-size: 24rpx;
			color: #999;
		}
	}

	.upload-wrapper {
		padding: 20rpx 0;
		
		.upload-header {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 20rpx;
			
			.upload-title {
				font-size: 28rpx;
				font-weight: 500;
				color: #333;
			}
			
			.upload-tip {
				font-size: 24rpx;
				color: #999;
			}
		}
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