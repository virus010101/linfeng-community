<template>
	<view>
		<post-list :list="postList" :loadStatus="loadStatus"></post-list>
	</view>
</template>

<script>
	import postList from '@/components/post-list/post-list.vue';
	export default {
		components: {
			postList
		},
		data() {
			return {
				postList: [],
				loadStatus: "loading",
				page: 1,
				type: 1, //1 点赞帖子   2 我的帖子
			};
		},
		onLoad(options) {
			this.type = options.type
			if (options.type == 1) {
				this.getCollectPostList();
			} else if (options.type == 2) {
				this.getMyPostList();
			}

		},
		onReachBottom() {
			if (this.type == 1) {
				this.page++;
				this.getCollectPostList();
			} else if (this.type == 2) {
				this.page++;
				this.getMyPostList();
			}
		},
		methods: {
			getCollectPostList() {
				this.loadStatus = "loading";
				this.$H.get('post/myCollectPost', {
					page: this.page
				}).then(res => {
					if (res.result.data) {
						this.postList = this.postList.concat(res.result.data);
						if (res.result.current_page >= res.result.total || res.result.last_page === 0) {
							this.loadStatus = "nomore";
						} else {
							this.loadStatus = "loadmore"
						}
					} else {
						this.loadStatus = "nomore";
					}
				})
			},
			getMyPostList() {
				this.loadStatus = "loading";
				this.$H.get('post/myPost', {
					page: this.page
				}).then(res => {
					this.postList = this.postList.concat(res.result.data);
					if (res.result.current_page >= res.result.total || res.result.last_page === 0) {
						this.loadStatus = "nomore";
					} else {
						this.loadStatus = "loadmore"
					}
				})
			}
		}
	}
</script>

<style lang="scss">
	page {
		background-color: #f5f5f5;
	}
</style>
