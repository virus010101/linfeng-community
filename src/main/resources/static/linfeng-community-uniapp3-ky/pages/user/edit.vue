<template>
	<view class="edit-page">
		<view class="group-card">
			<u-form ref="uForm" label-width="auto" :label-style="labelStyle" :border-bottom="true">
				<u-form-item label="头像" input-align="right" :border-bottom="true">
					<view class="avatar-row" @click.stop="onAvatar">
						<u-avatar :src="userInfo.avatar" size="100"></u-avatar>
					</view>
				</u-form-item>
				<u-form-item label="昵称" right-icon="arrow-right" :border-bottom="true">
					<u-input v-model="username" input-align="right" :custom-style="inputStyle"
						placeholder-style="color: #8e8e93" placeholder="请输入昵称" />
				</u-form-item>
				<u-form-item label="手机号" right-icon="arrow-right" :border-bottom="true" @click="onMobile">
					<u-input v-model="mobile" disabled input-align="right" :custom-style="inputStyle" />
				</u-form-item>
				<u-form-item label="性别" right-icon="arrow-right" :border-bottom="true">
					<u-input @click="openGender" :placeholder="userInfo.gender || '请选择'" input-align="right"
						:custom-style="inputStyle" placeholder-style="color: #8e8e93" />
				</u-form-item>
				<u-form-item label="个性签名" right-icon="arrow-right" :border-bottom="false">
					<u-input v-model="intro" input-align="right" :custom-style="inputStyle"
						placeholder-style="color: #8e8e93" placeholder="写点什么" />
				</u-form-item>
			</u-form>
		</view>

		<view class="action-block">
			<u-button :hair-line="false" :custom-style="saveBtnStyle" @click="saveInfo">保存个人信息</u-button>
		</view>

		<view class="group-card group-card--logout">
			<u-button :hair-line="false" :custom-style="btnStyle" @click="outlogin">退出登录</u-button>
		</view>

		<u-select v-model="showGender" :list="gender" @confirm="saveGender"></u-select>
	</view>
</template>

<script setup>
	import {
		ref,
		reactive,
		getCurrentInstance
	} from 'vue'
	import {
		onShow
	} from '@dcloudio/uni-app'

	const {
		proxy
	} = getCurrentInstance()

	const labelStyle = {
		fontWeight: '600',
		fontSize: '30rpx',
		color: '#000000'
	}

	const inputStyle = {
		fontSize: '30rpx',
		color: '#3c3c43'
	}

	const saveBtnStyle = reactive({
		color: '#ffffff',
		backgroundColor: '#000000',
		border: 'none',
		borderRadius: '24rpx',
		height: '96rpx',
		fontSize: '32rpx',
		fontWeight: '600'
	})

	const btnStyle = reactive({
		color: '#ff3b30',
		backgroundColor: '#ffffff',
		border: 'none',
		borderRadius: '20rpx',
		height: '96rpx',
		fontSize: '32rpx',
		fontWeight: '400'
	})

	const userInfo = reactive({})
	const showGender = ref(false)
	const gender = [{
			value: 1,
			label: "男"
		},
		{
			value: 2,
			label: "女"
		},
		{
			value: 0,
			label: "保密"
		}
	]
	const username = ref("")
	const intro = ref("")
	const mobile = ref("")

	function openGender() {
		showGender.value = true
	}

	function saveInfo() {
		if (!username.value.trim()) {
			proxy.$u.toast('昵称不能为空')
			return
		}
		if (!intro.value.trim()) {
			proxy.$u.toast('个性签名不能为空')
			return
		}
		proxy.$H.post("user/userInfoEdit", {
			username: username.value,
			intro: intro.value
		}).then(res => {
			if (res.code == 0) {
				proxy.$u.toast('个人信息更新成功')
			}
		})
	}

	function saveGender(index) {
		let genderValue = index[0].value
		proxy.$H.post("user/userInfoEdit", {
			gender: genderValue
		}).then(res => {
			if (res.code == 0) {
				userInfo.gender = index[0].label
				proxy.$u.toast('性别更新成功')
			}
		})
	}

	function getUserInfo() {
		proxy.$H.get("user/userInfo").then(res => {
			Object.assign(userInfo, res.result)
			username.value = res.result.username
			intro.value = res.result.intro
			mobile.value = res.result.mobile
			if (res.result.gender === 1) {
				userInfo.gender = '男'
			} else if (res.result.gender === 2) {
				userInfo.gender = '女'
			} else {
				userInfo.gender = '保密'
			}
		})
	}

	function outlogin() {
		uni.removeStorageSync("hasLogin")
		uni.removeStorageSync("token")
		uni.switchTab({
			url: "/pages/index/index"
		})
	}

	function onAvatar() {
		uni.chooseImage({
			count: 1,
			sizeType: ['original', 'compressed'],
			sourceType: ['album'],
			success: function(res) {
				uni.showLoading({
					mask: true,
					title: "上传头像中"
				})
				uni.uploadFile({
					url: proxy.$c.domain + 'common/upload',
					filePath: res.tempFilePaths[0],
					name: 'Image',
					header: {
						token: uni.getStorageSync("token")
					},
					success: (uploadFileRes) => {
						let data = JSON.parse(uploadFileRes.data)
						if (data.code == 0) {
							updateAvatar(data.result)
							uni.hideLoading()
						}
					}
				})
			}
		})
	}

	function updateAvatar(avatar) {
		proxy.$H.post("user/userInfoEdit", {
			avatar: avatar
		}).then(res => {
			if (res.code == 0) {
				userInfo.avatar = avatar
				proxy.$u.toast('头像更新成功')
			}
		})
	}

	function onMobile() {
		proxy.$u.toast('手机号禁止修改')
	}

	onShow(() => {
		getUserInfo()
	})
</script>

<style>
	page {
		background-color: #f2f2f7;
	}
</style>

<style lang="scss" scoped>
	.edit-page {
		min-height: 100vh;
		padding: 24rpx 32rpx 48rpx;
		box-sizing: border-box;
	}

	.group-card {
		background: #ffffff;
		border-radius: 20rpx;
		overflow: hidden;

		::v-deep .u-form-item {
			padding: 28rpx 32rpx;
			font-size: 30rpx;
			line-height: 42rpx;
		}

		::v-deep .u-form-item--left__content__label {
			font-weight: 600 !important;
			color: #000 !important;
		}

		::v-deep .u-form-item--right__content__icon {
			color: #c7c7cc;
		}

		::v-deep .u-border-bottom:after {
			border-color: rgba(60, 60, 67, 0.12);
		}

		::v-deep .u-input__input,
		::v-deep .uni-input-input {
			color: #3c3c43 !important;
		}

		::v-deep .u-form-item--right__content__slot {
			flex: 1;
			min-width: 0;
		}
	}

	.avatar-row {
		width: 100%;
		display: flex;
		justify-content: flex-end;
		align-items: center;
	}

	.group-card--logout {
		margin-top: 32rpx;
		padding: 0;

		::v-deep .u-btn {
			width: 100%;
		}
	}

	.action-block {
		margin: 40rpx 0 0;

		::v-deep .u-btn {
			width: 100%;
		}
	}
</style>