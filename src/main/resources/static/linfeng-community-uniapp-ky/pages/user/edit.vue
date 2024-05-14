<template>
	<view>
		<view class="info-container">
			<u-form ref="uForm" label-width="auto">
				<u-form-item label="头像">
					<u-avatar @click="onAvatar" mode="square" slot="right" :src="userInfo.avatar" size="100"></u-avatar>
				</u-form-item>
				<u-form-item label="昵称" right-icon="arrow-right">
					<u-input  :placeholder="userInfo.username"
						 input-align="right" :disabled="true" />
				</u-form-item>
				<u-form-item label="性别" right-icon="arrow-right">
					<u-input @click="openGender" :placeholder="userInfo.gender"
						input-align="right" />
				</u-form-item>
				<u-form-item label="个性签名" right-icon="arrow-right">
					<u-input  :placeholder="userInfo.intro" :disabled="true"
						input-align="right" />
				</u-form-item>
			</u-form>
		</view>
		<view class="out-btn">
			<u-button :custom-style="btnStyle" @click="outlogin">退出登录</u-button>
		</view>
		<!-- 性别选择 -->
		<u-select v-model="showGender" :list="gender" @confirm="saveGender"></u-select>
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
				form: {},
				userInfo: {},
				showGender: false,
				gender: [{
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
			};
		},
		onShow(options) {
			this.getUserInfo();
		},
		methods: {
			openGender(){
				this.showGender = true
			},
			// 修改性别
			saveGender(index) {
				let gender = index[0].value;
				this.$H.post("user/userInfoEdit", {
					gender: gender
				}).then(res => {
					if (res.code == 0) {
						this.userInfo.gender = index[0].label
					}
				})
			},
			getUserInfo() {
				this.$H.get("user/userInfo").then(res => {
					this.userInfo = res.result
					if (res.result.gender === 1) {
						this.userInfo.gender = '男'
					} else if (res.result.gender === 2) {
						this.userInfo.gender = '女'
					} else {
						this.userInfo.gender = '保密'
					}


				})
			},
			outlogin() {
				uni.removeStorageSync("hasLogin");
				uni.removeStorageSync("token");
				uni.removeStorageSync("userInfo");
				uni.switchTab({
					url: "/pages/index/index"
				})
			},
			onAvatar() {
				let that = this;
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
							url: that.$c.domain + 'common/upload',
							filePath: res.tempFilePaths[0],
							name: 'Image',
							header: {
								token: uni.getStorageSync("token")
							},
							success: (uploadFileRes) => {
								let data = JSON.parse(uploadFileRes.data)
								if (data.code == 0) {
									that.updateAvatar(data.result)
									uni.hideLoading();
								}
							}
						});
					}
				});

			},
			updateAvatar(avatar) {
				this.$H.post("user/userInfoEdit", {
					avatar: avatar
				}).then(res => {
					if (res.code == 0) {
						this.userInfo.avatar = avatar;
					}
				})
			}
		}
	}
</script>
<style scoped>
	.info-container {
		padding: 20rpx;
		background-color: #FFFFFF;
	}

	.out-btn {
		margin: 40rpx 30rpx;
	}
</style>
