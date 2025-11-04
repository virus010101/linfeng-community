<template>
	<view>
		<post-list :list="postList" :loadStatus="loadStatus"></post-list>
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
	import PostList from '@/components/post-list/post-list.vue'
	const postList = ref([])
	const loadStatus = ref("loading")
	const page = ref(1)
	const type = ref(1) // 1 点赞帖子 2 我的帖子
	const {
		proxy
	} = getCurrentInstance()

	function getCollectPostList() {
		loadStatus.value = "loading"
		proxy.$H.get('post/myCollectPost', {
			page: page.value
		}).then(res => {
			if (res.result.data) {
				postList.value = postList.value.concat(res.result.data)
				if (res.result.current_page >= res.result.total || res.result.last_page === 0) {
					loadStatus.value = "nomore"
				} else {
					loadStatus.value = "loadmore"
				}
			} else {
				loadStatus.value = "nomore"
			}
		})
	}

	function getMyPostList() {
		loadStatus.value = "loading"
		proxy.$H.get('post/myPost', {
			page: page.value
		}).then(res => {
			postList.value = postList.value.concat(res.result.data)
			if (res.result.current_page >= res.result.total || res.result.last_page === 0) {
				loadStatus.value = "nomore"
			} else {
				loadStatus.value = "loadmore"
			}
		})
	}
	onLoad((options) => {
		type.value = options.type
		if (options.type == 1) {
			getCollectPostList()
		} else if (options.type == 2) {
			getMyPostList()
		}
	})
	onReachBottom(() => {
		if (type.value == 1) {
			page.value++
			getCollectPostList()
		} else if (type.value == 2) {
			page.value++
			getMyPostList()
		}
	})
</script>

<style lang="scss">
	page {
		background-color: #f5f5f5;
	}
</style>