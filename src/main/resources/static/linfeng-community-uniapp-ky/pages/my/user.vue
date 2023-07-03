<template>
	<view>
		<user-list :list="userList" :loadStatus="loadStatus"></user-list>
	</view>
</template>

<script>
	import userList from '@/components/user-list/user-list.vue';
	export default {
		components: {
			userList
		},
		data() {
			return {
				userList: [],
				loadStatus: "loadmore",
				page: 1,
				type: 1, //1 关注   2 粉丝
			};
		},
		onLoad(options) {
			this.type = options.type
			if (options.type == 1) {
				this.getUserFollowList();
			} else if (options.type == 2) {
				this.getUserFansList();
			}
		},
		onReachBottom() {
			if (this.type == 1) {
				this.page++;
				this.getUserFollowList();
			} else if (this.type == 2) {
				this.page++;
				this.getUserFansList();
			}
		},
		methods: {
			getUserFollowList() {
				this.loadStatus = "loading";
				this.$H.get('user/follow', {
					page: this.page
				}).then(res => {
					if (res.result.data) {
						this.userList = this.userList.concat(res.result.data);
						if (res.result.current_page >= res.result.total || res.result.last_page === 0) {
							this.loadStatus = "nomore";
						} else {
							this.loadStatus = "loadmore"
						}
					} else {
						this.loadStatus = "loadmore"
					}

				})
			},
			getUserFansList() {
				this.loadStatus = "loading";
				this.$H.get('user/userFans', {
					page: this.page
				}).then(res => {
					if (res.result.data) {
						this.userList = this.userList.concat(res.result.data);
						if (res.result.current_page >= res.result.total || res.result.last_page === 0) {
							this.loadStatus = "nomore";
						} else {
							this.loadStatus = "loadmore"
						}
					} else {
						this.loadStatus = "loadmore"
					}

				})
			}
		}
	}
</script>

<style lang="scss">

</style>
