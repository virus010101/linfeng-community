<template>
	<view>
		<view v-for="(item, index) in userList" :key="index">
			<view @click="goUser(item.uid)" class="user-list-item">
				<u-avatar class="avatar" :src="item.avatar"></u-avatar>
				<view class="user">
					<text class="name">{{ item.username }}</text>
					<text v-if="item.gender == 1" class="iconfont icon-nan"></text>
					<text v-if="item.gender == 2" class="iconfont icon-nv"></text>
				</view>
				<u-button @click="follow(index, item.uid)" v-if="item.hasFollow === 0" class="btn-gz" type="default"
					size="mini">关注</u-button>
				<u-button @click="cancelFollow(index, item.uid)" v-if="item.hasFollow === 1" class="btn-gz"
					type="default" size="mini" plain>互相关注</u-button>
				<u-button @click="cancelFollow(index, item.uid)" v-if="item.hasFollow === 2" class="btn-gz"
					type="default" size="mini" plain>已关注</u-button>
			</view>
		</view>
		<!-- 加载状态 -->
		<view v-if="loadStatus != 'none'">
			<view v-if="list.length === 0 && loadStatus == 'nomore'">
				<u-empty margin-top="100" text="暂无用户" mode="favor"></u-empty>
			</view>
			<view v-else>
				<u-loadmore margin-bottom="50" margin-top="50" :status="loadStatus" />
			</view>
		</view>
	</view>
</template>

<script setup>
	import { ref, watch, getCurrentInstance } from 'vue'
	const props = defineProps({
		list: Array,
		loadStatus: String
	})
	const userList = ref([])
	watch(() => props.list, () => {
		userList.value = props.list
	}, { immediate: true })
	const { proxy } = getCurrentInstance()
	function goUser(uid) {
		uni.navigateTo({ url: '/pages/user/home?uid=' + uid })
	}
	function follow(index, uid) {
		proxy.$H
			.post('user/addFollow', { id: uid })
			.then(res => {
				if (res.code == 0) {
					userList.value[index].hasFollow = 2
				}
			})
	}
	function cancelFollow(index, uid) {
		proxy.$H
			.post('user/cancelFollow', { id: uid })
			.then(res => {
				if (res.code === 0) {
					userList.value[index].hasFollow = 0
				}
			})
	}
</script>

<style lang="scss" scoped>
	.user-list-item {
		display: flex;
		align-items: center;
		padding: 20rpx;
		border-bottom: 1px solid #f5f5f5;
		background-color: #ffffff;

		&:last-child {
			border-bottom: 0;
		}

	}

	.user-list-item .icon-nv {
		color: #ff4d94;
	}

	.user-list-item .icon-nan {
		color: #0091ff;
	}

	.user-list-item .avatar {
		margin-right: 20rpx;
	}

	.user-list-item .user .name {
		margin-right: 20rpx;
	}

	.user-list-item .user .iconfont {
		font-size: 12px;
	}

	/* #ifdef H5 */
	.user-list-item .btn-gz {
		margin-right: 20rpx;
	}

	/* #endif */
	/* #ifdef MP */
	.user-list-item .btn-gz {
		margin-left: auto;
	}

	/* #endif */
</style>
