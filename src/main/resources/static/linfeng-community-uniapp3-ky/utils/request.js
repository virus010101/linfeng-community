import config from './config.js';
export default {
	request(options = {}) {
		return new Promise((resolve, reject) => {
			let url = options.url;
			if (url.indexOf("http://") == -1 && url.indexOf("https://") == -1) {
				options.url = config.domain + url;
			}
			options.header.token = uni.getStorageSync("token");
			options.complete = (response) => {
				if (response.statusCode == 200 || response.statusCode == 0) {
					if (response.data.code == 401 || response.data.code == 420) {
						// #ifdef MP-WEIXIN
						uni.navigateTo({
							url: "/pages/login/weixin"
						})
						// #endif

						// #ifdef H5
						uni.navigateTo({
							url: "/pages/login/login"
						})
						// #endif
					} else if (response.data.code == 500) {
						uni.showToast({
							title: response.data.msg,
							icon: "none",
							duration: 2000
						});
					} else if (response.data.code == 701) {
						//封号
						uni.showToast({
							title: response.data.msg,
							icon: "none",
							duration: 2000
						});
						uni.removeStorageSync("hasLogin");
						uni.removeStorageSync("token");
						uni.switchTab({
							url: "/pages/index/index"
						})
					}

					resolve(response.data)
				} else {
					uni.showToast({
						title: '请求异常！',
						icon: "none"
					});
				}
			}

			uni.request(options)
		})
	},

	post(url, data = {}, header = {}) {

		let options = {
			url: url,
			data: data,
			header: header,
			method: "POST"
		}

		return this.request(options);
	},

	get(url, data = {}, header = {}) {
		let options = {
			url: url,
			data: data,
			header: header
		}

		return this.request(options);
	}
};