<template>
	<view>
		<u-navbar :is-back="false" z-index="99999">
			<u-tabs :list="tabList" font-size="35" name="cateName" bg-color="#fff" :current="current"
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

<script>
	import postList from '../../components/post-list/post-list.vue';
	import addPostTag from '../../components/add-post-tag/add-post-tag.vue';
	export default {
		components: {
			postList,
			addPostTag
		},
		data() {
			return {
				sessionUid: uni.getStorageSync('userInfo').uid,
				loadStatus1: 'loadmore',
				loadStatus2: 'loadmore',
				page1: 1,
				page2: 1,
				shareCover: '',
				followUserPost: [],
				lastPost: [],
				tabList: [{
						name: '关注'
					},
					{
						name: '最新'
					}
				],
				current: 1,

			};
		},
		computed: {
			msgCount() {
				return this.$store.state.messegeNum
			}
		},
		onShareAppMessage(res) {
			return {
				title: this.$c.miniappName,
				path: '/pages/index/index',
				imageUrl: this.shareCover
			};
		},
		onLoad() {
			this.getLastPost();
		},
		onShow() {
			// this.getMsgNum();
		},
		onReachBottom() {
			if (this.current === 0) {
				this.page1++;
				this.getFollowUserPost();
			}

			if (this.current === 1) {
				this.page2++;
				this.getLastPost();
			}
		},
		onPullDownRefresh() {
			if (this.current === 0) {
				this.page1 = 1;
				this.followUserPost = [];
				this.getFollowUserPost();
				// this.getMsgNum();
			}
			if (this.current === 1) {
				this.page2 = 1;
				this.lastPost = [];
				this.getLastPost();
			}
			uni.stopPullDownRefresh();
		},
		methods: {
			getMsgNum() {
				this.$H.post('message/num').then(res => {
					this.$store.state.messegeNum = [0, 0, 0, res.result.allCount, 0];
				});
			},
			tabChange(index) {
				this.current = index;
				this.followUserPost = [];
				this.lastPost = [];

				if (index === 0) {
					this.page1 = 1;
					this.getFollowUserPost();
					this.getMsgNum();
				}
				if (index === 1) {
					this.page2 = 1;
					this.getLastPost();
				}
			},
			getSysInfo() {
				this.$H.get('system/miniConfig').then(res => {
					this.shareCover = res.result.intro;
				});
			},
			// 获取关注用户帖子
			getFollowUserPost() {
				this.loadStatus1 = 'loading';
				this.$H
					.get('post/followUserPost', {
						page: this.page1
					})
					.then(res => {
						if (res.code == 0 && res.result) {
							this.followUserPost = this.followUserPost.concat(res.result.data);
							if (res.result.current_page >= res.result.total || res.result.last_page === 0) {
								this.loadStatus1 = 'nomore';
							} else {
								this.loadStatus1 = 'loadmore';
							}
						} else {
							this.loadStatus1 = 'nomore';
						}

					});
			},
			//获取最新帖子
			getLastPost() {
				this.loadStatus2 = 'loading';
				this.$H
					.get('post/lastPost', {
						page: this.page2
					})
					.then(res => {
						this.lastPost = this.lastPost.concat(res.result.data);
						if (res.result.current_page >= res.result.total || res.result.last_page === 0) {
							this.loadStatus2 = 'nomore';
						} else {
							this.loadStatus2 = 'loadmore';
						}
					});
			},
		}
	};
</script>

<style>
	page {
		background-color: #F5F5F5;
	}
</style>
<style lang="scss" scoped>

</style>
