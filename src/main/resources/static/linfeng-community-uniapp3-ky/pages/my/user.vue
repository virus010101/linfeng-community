<template>
	<view>
		<view class="tab-box">
			<view v-for="(item, index) in tabBars" :key="index" class="tab-item"
				:class="type == item.id ? 'active' : ''" @click="changeTab(item.id)">
				{{ item.name }}
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
				if (res.result.current_page >= res.result.total || res.result.last_page === 0) {
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
				if (res.result.current_page >= res.result.total || res.result.last_page === 0) {
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

<style lang="scss">
	.tab-box {
		display: flex;
		height: 100rpx;
		border-bottom: 1rpx solid #f5f5f5;

		.tab-item {
			flex: 1;
			display: flex;
			justify-content: center;
			align-items: center;
			font-size: 30rpx;
			color: #333;
			position: relative;

			&.active {
				color: #007AFF;
				font-weight: bold;

				&::after {
					content: "";
					position: absolute;
					bottom: 0;
					left: 50%;
					transform: translateX(-50%);
					width: 120rpx;
					height: 4rpx;
					background-color: #007AFF;
					border-radius: 10rpx;
				}
			}
		}
	}
</style>