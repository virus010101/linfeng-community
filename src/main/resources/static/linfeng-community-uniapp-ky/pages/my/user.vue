<template>
	<view>
		<view class="tab-box">
			<view 
				v-for="(item, index) in tabBars" 
				:key="index" 
				class="tab-item" 
				:class="type == item.id ? 'active' : ''"
				@click="changeTab(item.id)"
			>
				{{ item.name }}
			</view>
		</view>
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
				tabBars: [
					{id: 1, name: '关注'},
					{id: 2, name: '粉丝'}
				]
			};
		},
		onLoad(options) {
			this.type = options.type || 1
			this.loadData()
		},
		onReachBottom() {
			this.page++
			this.loadData()
		},
		methods: {
			changeTab(id) {
				if(this.type !== id) {
					this.type = id
					this.page = 1
					this.userList = []
					this.loadData()
				}
			},
			loadData() {
				if(this.type == 1) {
					this.getUserFollowList()
				} else {
					this.getUserFansList()
				}
			},
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
