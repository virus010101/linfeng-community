/**
 * getSystemInfoSync的优化版本（在微信小程序不会提示API已过期的警告）
 */

function getSystemInfoSync() {
	// #ifdef MP-WEIXIN
	return getCompatibleSystemInfo();
	// #endif
	// #ifndef MP-WEIXIN
	return uni.getSystemInfoSync();
	// #endif
}

export default getSystemInfoSync;

// #ifdef MP-WEIXIN
function getCompatibleSystemInfo() {
	if (wx.canIUse('getDeviceInfo') && wx.canIUse('getWindowInfo') && wx.canIUse('getAppBaseInfo') && wx.canIUse('getSystemSetting')) {
		// 使用 try-catch 包裹 getDeviceInfo，避免 system 属性为 undefined 时报错
		let deviceInfo = {};
		try {
			deviceInfo = uni.getDeviceInfo() || {};
			// 确保 system 属性存在且为字符串
			if (!deviceInfo.system || typeof deviceInfo.system !== 'string') {
				deviceInfo.system = '';
			}
		} catch (e) {
			console.warn('getDeviceInfo error:', e);
			deviceInfo = {};
		}
		
		const windowInfo = uni.getWindowInfo();
		const appBaseInfo = uni.getAppBaseInfo();
		const systemSetting = uni.getSystemSetting();
		// const appAuthorizeSetting = wx.getAppAuthorizeSetting(); // 这个API效率低，不放在这里了
		// 最终会少 batteryLevel 属性和 appAuthorizeSetting 属性
		return {
			devicePixelRatio: windowInfo.pixelRatio,
			hostFontSizeSetting: appBaseInfo.fontSizeSetting,
			batteryLevel: 100, // 设置一个假的固定值进去，防止出错（如果再调用电量API这效率会比较低）
			...deviceInfo,
			...windowInfo,
			...appBaseInfo,
			...systemSetting,
			// ...appAuthorizeSetting,
		};
	} else {
		return uni.getSystemInfoSync();
	}
}
// #endif