<script>
	export default {
		onLaunch: function() {
			console.log('App Launch')
		},
		onShow: function() {
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		},
		onPageNotFound() {
			// 跳转到 404 页面：
			uni.redirectTo({
				url: "pages/error/error"
			});
		}
	}
</script>

<style lang="scss">
	@import "./uni_modules/vk-uview-ui/index.scss";
	@import "static/css/iconfont.css";
</style>