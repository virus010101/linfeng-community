<template>
	<view>
		<view @click="goPostAdd" class="add-post">
			<u-icon name="edit-pen" color="#ffffff" size="50"></u-icon>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {

			};
		},
		methods: {
			goPostAdd() {
				uni.navigateTo({
					url: '/pages/post/add'
				})
			}
		}
	}
</script>

<style lang="scss" scoped>
	.add-post {
		position: fixed;
		right: 20rpx;
		bottom: 180rpx;
		width: 100rpx;
		height: 100rpx;
		border-radius: 50%;
		background-color: #333333;
		box-shadow: 0 0 20rpx #999;
		display: flex;
		justify-content: center;
		align-items: center;
	}
</style>
