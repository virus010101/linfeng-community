<template>
	<view>
		<view class="info-container">
			<u-form ref="uForm" label-width="auto">
				<u-form-item label="头像"  input-align="right">
					<u-avatar @click="onAvatar" mode="square" slot="right" :src="userInfo.avatar" size="100"></u-avatar>
				</u-form-item>
				<u-form-item label="昵称" right-icon="arrow-right">
					<u-input  v-model="username"
						 input-align="right"/>
				</u-form-item>
				<u-form-item label="手机号" right-icon="arrow-right">
					<u-input  v-model="mobile" disabled
						input-align="right"/>
				</u-form-item>
				<u-form-item label="性别" right-icon="arrow-right">
					<u-input @click="openGender" :placeholder="userInfo.gender"
						input-align="right"/>
				</u-form-item>
				<u-form-item label="个性签名" right-icon="arrow-right">
					<u-input  v-model="intro"
						input-align="right"/>
				</u-form-item>
			</u-form>
		</view>
		<view class="save-btn">
			<u-button :custom-style="saveBtnStyle" @click="saveInfo">保存个人信息</u-button>
		</view>
		<view class="out-btn">
			<u-button :custom-style="btnStyle" @click="outlogin">退出登录</u-button>
		</view>
		<!-- 性别选择 -->
		<u-select v-model="showGender" :list="gender" @confirm="saveGender"></u-select>
	</view>
</template>

<script setup>
	import { ref, reactive, getCurrentInstance } from 'vue'
	import { onShow } from '@dcloudio/uni-app'
	
	const { proxy } = getCurrentInstance()
	
	const saveBtnStyle = reactive({
		color: "#fff",
		backgroundColor: '#8f8fd6'
	})
	
	const btnStyle = reactive({
		color: "#000000",
		backgroundColor: '#fff'
	})
	
	const userInfo = reactive({})
	const showGender = ref(false)
	const gender = [
		{ value: 1, label: "男" },
		{ value: 2, label: "女" },
		{ value: 0, label: "保密" }
	]
	const username = ref("")
	const intro = ref("")
	const mobile = ref("")
	
	function openGender() {
		showGender.value = true
	}
	
	function saveInfo() {
		if (!username.value) {
			proxy.$u.toast('昵称不能为空')
			return
		}
		if (!intro.value) {
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
	
	// 修改性别
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
	
	onShow(() => {
		getUserInfo()
	})
</script>
<style scoped>
	.info-container {
		padding: 20rpx;
		background-color: #FFFFFF;
	}
	.save-btn {
		margin: 40rpx 30rpx;
	}
	.out-btn {
		margin: 40rpx 30rpx;
	}
</style>
