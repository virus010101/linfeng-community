<template>
	<view class="page">
		<view class="tab-box">
			<view v-for="(item, index) in tabBars" :key="index" class="tab-item"
				:class="type == item.id ? 'active' : ''" @click="changeTab(item.id)">
				<text class="tab-name">{{ item.name }}</text>
				<view class="tab-line"></view>
			</view>
		</view>
		<user-list :list="userList" :loadStatus="loadStatus"></user-list>
	</view>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance
	} from 'vue'
	import {
		onLoad,
		onReachBottom
	} from '@dcloudio/uni-app'
	import UserList from '@/components/user-list/user-list.vue'
	const userList = ref([])
	const loadStatus = ref("loadmore")
	const page = ref(1)
	const type = ref(1) // 1 关注 2 粉丝
	const tabBars = ref([{
			id: 1,
			name: '关注'
		},
		{
			id: 2,
			name: '粉丝'
		}
	])
	const {
		proxy
	} = getCurrentInstance()

	function changeTab(id) {
		if (type.value !== id) {
			type.value = id
			page.value = 1
			userList.value = []
			loadData()
		}
	}

	function loadData() {
		if (type.value == 1) {
			getUserFollowList()
		} else {
			getUserFansList()
		}
	}

	function getUserFollowList() {
		loadStatus.value = "loading"
		proxy.$H.get('user/follow', {
			page: page.value
		}).then(res => {
			if (res.result.data) {
				userList.value = userList.value.concat(res.result.data)
				if (res.result.current_page >= res.result.last_page || res.result.total === 0) {
					loadStatus.value = "nomore"
				} else {
					loadStatus.value = "loadmore"
				}
			} else {
				loadStatus.value = "loadmore"
			}
		})
	}

	function getUserFansList() {
		loadStatus.value = "loading"
		proxy.$H.get('user/userFans', {
			page: page.value
		}).then(res => {
			if (res.result.data) {
				userList.value = userList.value.concat(res.result.data)
				if (res.result.current_page >= res.result.last_page || res.result.total === 0) {
					loadStatus.value = "nomore"
				} else {
					loadStatus.value = "loadmore"
				}
			} else {
				loadStatus.value = "loadmore"
			}
		})
	}
	onLoad((options) => {
		type.value = options.type || 1
		loadData()
	})
	onReachBottom(() => {
		page.value++
		loadData()
	})
</script>

<style lang="scss" scoped>
	.page {
		min-height: 100vh;
		background: #fff;
	}

	.tab-box {
		display: flex;
		height: 88rpx;
		background: #fff;
		border-bottom: 1rpx solid #f0f0f0;
		position: sticky;
		top: 0;
		z-index: 10;
	}

	.tab-item {
		flex: 1;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		position: relative;
	}

	.tab-name {
		font-size: 30rpx;
		color: #949494;
		line-height: 1;
	}

	.tab-line {
		position: absolute;
		bottom: 0;
		width: 40rpx;
		height: 4rpx;
		border-radius: 4rpx;
		background: transparent;
	}

	.tab-item.active {
		.tab-name {
			color: #000;
			font-weight: 600;
		}

		.tab-line {
			background: #000;
		}
	}
</style>