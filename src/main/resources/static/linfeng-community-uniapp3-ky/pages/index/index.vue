<template>
	<view>
		<u-navbar :is-back="false" z-index="99999">
			<u-tabs :list="tabList" font-size="35" name="cateName" inactive-color="#949494" v-model="current"
				@change="tabChange"></u-tabs>
		</u-navbar>
		<!-- 最新 -->
		<view v-if="current === 1">
			<post-list :list="lastPost" :loadStatus="loadStatus2"></post-list>
		</view>
		<!-- 关注 -->
		<view v-if="current === 0">
			<post-list :list="followUserPost" :loadStatus="loadStatus1"></post-list>
		</view>
		<!-- 发贴入口 -->
		<add-post-tag></add-post-tag>
	</view>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance
	} from 'vue'
	import {
		onLoad,
		onReachBottom,
		onPullDownRefresh,
		onShareAppMessage
	} from '@dcloudio/uni-app'
	import PostList from '@/components/post-list/post-list.vue'
	import AddPostTag from '@/components/add-post-tag/add-post-tag.vue'
	const loadStatus1 = ref('loadmore')
	const loadStatus2 = ref('loadmore')
	const page1 = ref(1)
	const page2 = ref(1)
	const shareCover = ref('')
	const followUserPost = ref([])
	const lastPost = ref([])
	const tabList = ref([{
			name: '关注'
		},
		{
			name: '最新'
		}
	])
	const current = ref(1)
	const {
		proxy
	} = getCurrentInstance()

	function tabChange(index) {
		current.value = index
		followUserPost.value = []
		lastPost.value = []
		if (index === 0) {
			page1.value = 1
			getFollowUserPost()
		} else if (index === 1) {
			page2.value = 1
			getLastPost()
		}
	}

	function getFollowUserPost() {
		loadStatus1.value = 'loading'
		proxy.$H
			.get('post/followUserPost', {
				page: page1.value
			})
			.then(res => {
				if (res.code == 0 && res.result) {
					followUserPost.value = followUserPost.value.concat(res.result.data)
					if (res.result.current_page >= res.result.total || res.result.last_page === 0) {
						loadStatus1.value = 'nomore'
					} else {
						loadStatus1.value = 'loadmore'
					}
				} else {
					loadStatus1.value = 'nomore'
				}
			})
	}

	function getLastPost() {
		loadStatus2.value = 'loading'
		proxy.$H
			.get('post/lastPost', {
				page: page2.value
			})
			.then(res => {
				lastPost.value = lastPost.value.concat(res.result.data)
				if (res.result.current_page >= res.result.total || res.result.last_page === 0) {
					loadStatus2.value = 'nomore'
				} else {
					loadStatus2.value = 'loadmore'
				}
			})
	}
	onLoad(() => {
		getLastPost()
	})
	onReachBottom(() => {
		if (current.value === 0) {
			page1.value++
			getFollowUserPost()
		} else if (current.value === 1) {
			page2.value++
			getLastPost()
		}
	})
	onPullDownRefresh(() => {
		if (current.value === 0) {
			page1.value = 1
			followUserPost.value = []
			getFollowUserPost()
		} else if (current.value === 1) {
			page2.value = 1
			lastPost.value = []
			getLastPost()
		}
		uni.stopPullDownRefresh()
	})
	onShareAppMessage(() => {
		return {
			title: proxy.$c.miniappName,
			path: '/pages/index/index',
			imageUrl: shareCover.value
		}
	})
</script>

<style>
	page {
		background-color: #F5F5F5;
	}
</style>