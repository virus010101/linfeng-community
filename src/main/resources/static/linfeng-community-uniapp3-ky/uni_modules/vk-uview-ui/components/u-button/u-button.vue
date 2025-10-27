<template>
	<button
		id="u-wave-btn"
		class="u-btn u-line-1 u-fix-ios-appearance"
		:class="[
			'u-size-' + sizeCom,
			plain ? 'u-btn--' + type + '--plain' : '',
			loading ? 'u-loading' : '',
			shape == 'circle' ? 'u-round-circle' : '',
			hairLine ? showHairLineBorder : 'u-btn--bold-border',
			'u-btn--' + type,
			disabled ? `u-btn--${type}--disabled` : '',
		]"
		:hover-start-time="Number(hoverStartTime)"
		:hover-stay-time="Number(hoverStayTime)"
		:disabled="disabled"
		:form-type="formType"
		:open-type="openType"
		:app-parameter="appParameter"
		:hover-stop-propagation="hoverStopPropagation"
		:send-message-title="sendMessageTitle"
		send-message-path="sendMessagePath"
		:lang="lang"
		:data-name="dataName"
		:session-from="sessionFrom"
		:send-message-img="sendMessageImg"
		:show-message-card="showMessageCard"
		@getphonenumber="getphonenumber"
		@getuserinfo="getuserinfo"
		@error="error"
		@opensetting="opensetting"
		@launchapp="launchapp"
		@chooseavatar="chooseavatar"
		:style="[customStyle, {
			overflow: ripple ? 'hidden' : 'visible'
		}]"
		@tap.stop="click($event)"
		:hover-class="getHoverClass"
		:loading="loading"
	>
		<slot></slot>
		<view
			v-if="ripple"
			class="u-wave-ripple"
			:class="[waveActive ? 'u-wave-active' : '']"
			:style="{
				top: rippleTop + 'px',
				left: rippleLeft + 'px',
				width: fields.targetWidth + 'px',
				height: fields.targetWidth + 'px',
				'background-color': rippleBgColor || 'rgba(0, 0, 0, 0.15)'
			}"
		></view>
	</button>
</template>

<script>
/**
 * button 按钮
 * @description Button 按钮
 * @tutorial https://www.uviewui.com/components/button.html
 * @property {String} size 按钮的大小
 *  @value large 				大
 * 	@value normal 			常规
 * 	@value mini 				小
 * @property {Boolean} ripple 是否开启点击水波纹效果
 * @property {String} ripple-bg-color 水波纹的背景色，ripple为true时有效
 * @property {String} type 按钮的样式类型
 *  @value info  				默认按钮
 * 	@value primary  		主要按钮
 * 	@value error 				危险按钮
 * 	@value warning  		警告按钮
 * 	@value success 			成功按钮
 * @property {Boolean} plain 按钮是否镂空，背景色透明
 * @property {Boolean} disabled 是否禁用
 * @property {Boolean} hair-line 是否显示按钮的细边框(默认true)
 * @property {Boolean} shape 按钮外观形状，见文档说明
 *  @value square  		矩形
 * 	@value circle  		圆角
 * @property {Boolean} loading 按钮名称前是否带 loading 图标(App-nvue 平台，在 ios 上为雪花，Android上为圆圈)
 * @property {String} form-type 用于 <form> 组件，点击分别会触发 <form> 组件的 submit/reset 事件
 * @property {String} openType 开放能力
 * 	@value feedback  						通用 - 打开“意见反馈”页面，用户可提交反馈内容并上传日志（App、微信小程序、QQ小程序）
 * 	@value share  							通用 - 触发用户转发（微信小程序、百度小程序、支付宝小程序、字节跳动小程序、飞书小程序、QQ小程序、快手小程序、京东小程序、360小程序）
 * 	@value getUserInfo  				通用 - 获取用户信息（微信小程序、百度小程序、QQ小程序、快手小程序、京东小程序、360小程序）
 * 	@value contact  						通用 - 打开客服会话，如果用户在会话中点击消息卡片后返回应用，可以从 回调中获得具体信息（微信小程序、百度小程序、快手小程序、字节小程序）
 * 	@value getPhoneNumber  			通用 - 获取用户手机号（微信小程序、百度小程序、字节跳动小程序、支付宝小程序、快手小程序、京东小程序。App平台另见一键登录）
 * 	@value launchApp  					通用 - 小程序中打开APP，可以通过app-parameter属性设定向APP传的参数	（微信小程序、QQ小程序、快手小程序、京东小程序）
 * 	@value openSetting  				通用 - 打开授权设置页（微信小程序、QQ小程序、百度小程序、快手小程序、京东小程序、360小程序）
 * 	@value chooseAvatar  				微信小程序 - 获取用户头像
 * 	@value uploadDouyinVideo  	抖音小程序 - 发布抖音视频
 * 	@value im  									抖音小程序 - 跳转到抖音IM客服
 * 	@value getAuthorize  				支付宝小程序 - 授权
 * 	@value lifestyle  					支付宝小程序 - 关注生活号
 * 	@value contactShare  				支付宝小程序 - 分享到通讯录好友
 * 	@value openGroupProfile  		QQ小程序 - 呼起QQ群资料卡页面，可以通过group-id属性设定需要打开的群资料卡的群号，同时manifest.json中必须配置groupIdList
 * 	@value openGuildProfile  		QQ小程序 - 呼起频道页面，可以通过guild-id属性设定需要打开的频道ID	
 * 	@value openPublicProfile  	QQ小程序 - 打开公众号资料卡，可以通过public-id属性设定需要打开的公众号资料卡的号码，同时manifest.json中必须配置publicIdList
 * 	@value shareMessageToFriend QQ小程序 - 在自定义开放数据域组件中,向指定好友发起分享据
 * 	@value addFriend  					QQ小程序 - 添加好友， 对方需要通过该小程序进行授权，允许被加好友后才能调用成功用户授权
 * 	@value addColorSign  				QQ小程序 - 添加彩签，点击后添加状态有用户提示，无回调
 * 	@value addGroupApp  				QQ小程序 - 添加群应用（只有管理员或群主有权操作），添加后给button绑定@addgroupapp事件接收回调数据
 * 	@value addToFavorites  			QQ小程序 - 收藏当前页面，点击按钮后会触发Page.onAddToFavorites方法
 * 	@value chooseAddress  			百度小程序 - 选择用户收货地址
 * 	@value chooseInvoiceTitle		百度小程序 - 选择用户发票抬头
 * 	@value login  							百度小程序 - 登录，可以从@login回调中确认是否登录成功
 * 	@value subscribe  					百度小程序 - 订阅类模板消息，需要用户授权才可发送
 * 	@value favorite  						快手小程序 - 触发用户收藏
 * 	@value watchLater  					快手小程序 - 触发用户稍后再看
 * 	@value openProfile  				快手小程序 - 触发打开用户主页
 * @property {String} data-name 额外传参参数，用于小程序的data-xxx属性，通过target.dataset.name获取
 * @property {String} hover-class 指定按钮按下去的样式类。当 hover-class="none" 时，没有点击态效果(App-nvue 平台暂不支持)
 * @property {Number} hover-start-time 按住后多久出现点击态，单位毫秒
 * @property {Number} hover-stay-time 手指松开后点击态保留时间，单位毫秒
 * @property {Object} custom-style 对按钮的自定义样式，对象形式，见文档说明
 * @event {Function} click 按钮点击
 * @event {Function} getphonenumber open-type="getPhoneNumber"时有效
 * @event {Function} getuserinfo 用户点击该按钮时，会返回获取到的用户信息，从返回参数的detail中获取到的值同uni.getUserInfo
 * @event {Function} error 当使用开放能力时，发生错误的回调
 * @event {Function} opensetting 在打开授权设置页并关闭后回调
 * @event {Function} launchapp 打开 APP 成功的回调
 * @event {Function} chooseavatar 获取用户头像，可以从@chooseavatar回调中获取到头像信息，open-type="chooseAvatar"时有效
 * @example <u-button>月落</u-button>
 */
export default {
	name: 'u-button',
  emits: ["click", "getphonenumber", "getuserinfo", "error", "opensetting", "launchapp", "chooseavatar"],
	props: {
		// 是否细边框
		hairLine: {
			type: Boolean,
			default: true
		},
		// 按钮的预置样式，default，primary，error，warning，success
		type: {
			type: String,
			default: 'default'
		},
		// 按钮尺寸，default，medium，mini
		size: {
			type: String,
			default: 'default'
		},
		// 按钮形状，circle（两边为半圆），square（带圆角）
		shape: {
			type: String,
			default: 'square'
		},
		// 按钮是否镂空
		plain: {
			type: Boolean,
			default: false
		},
		// 是否禁止状态
		disabled: {
			type: Boolean,
			default: false
		},
		// 是否加载中
		loading: {
			type: Boolean,
			default: false
		},
		// 开放能力，具体请看uniapp稳定关于button组件部分说明
		// https://uniapp.dcloud.io/component/button
		openType: {
			type: String,
			default: ''
		},
		// 用于 <form> 组件，点击分别会触发 <form> 组件的 submit/reset 事件
		// 取值为submit（提交表单），reset（重置表单）
		formType: {
			type: String,
			default: ''
		},
		// 打开 APP 时，向 APP 传递的参数，open-type=launchApp时有效
		// 只微信小程序、QQ小程序有效
		appParameter: {
			type: String,
			default: ''
		},
		// 指定是否阻止本节点的祖先节点出现点击态，微信小程序有效
		hoverStopPropagation: {
			type: Boolean,
			default: false
		},
		// 指定返回用户信息的语言，zh_CN 简体中文，zh_TW 繁体中文，en 英文。只微信小程序有效
		lang: {
			type: String,
			default: 'en'
		},
		// 会话来源，open-type="contact"时有效。只微信小程序有效
		sessionFrom: {
			type: String,
			default: ''
		},
		// 会话内消息卡片标题，open-type="contact"时有效
		// 默认当前标题，只微信小程序有效
		sendMessageTitle: {
			type: String,
			default: ''
		},
		// 会话内消息卡片点击跳转小程序路径，open-type="contact"时有效
		// 默认当前分享路径，只微信小程序有效
		sendMessagePath: {
			type: String,
			default: ''
		},
		// 会话内消息卡片图片，open-type="contact"时有效
		// 默认当前页面截图，只微信小程序有效
		sendMessageImg: {
			type: String,
			default: ''
		},
		// 是否显示会话内消息卡片，设置此参数为 true，用户进入客服会话会在右下角显示"可能要发送的小程序"提示，
		// 用户点击后可以快速发送小程序消息，open-type="contact"时有效
		showMessageCard: {
			type: Boolean,
			default: false
		},
		// 手指按（触摸）按钮时按钮时的背景颜色
		hoverBgColor: {
			type: String,
			default: ''
		},
		// 水波纹的背景颜色
		rippleBgColor: {
			type: String,
			default: ''
		},
		// 是否开启水波纹效果
		ripple: {
			type: Boolean,
			default: false
		},
		// 按下的类名
		hoverClass: {
			type: String,
			default: ''
		},
		// 自定义样式，对象形式
		customStyle: {
			type: Object,
			default() {
				return {};
			}
		},
		// 额外传参参数，用于小程序的data-xxx属性，通过target.dataset.name获取
		dataName: {
			type: String,
			default: ''
		},
		// 节流，一定时间内只能触发一次
		throttleTime: {
			type: [String, Number],
			default: 500
		},
		// 按住后多久出现点击态，单位毫秒
		hoverStartTime: {
			type: [String, Number],
			default: 20
		},
		// 手指松开后点击态保留时间，单位毫秒
		hoverStayTime: {
			type: [String, Number],
			default: 150
		},
		timerId: {
			type: [String, Number]
		},
	},
	computed: {
		// 当没有传bgColor变量时，按钮按下去的颜色类名
		getHoverClass() {
			// 如果开启水波纹效果，则不启用hover-class效果
			if (this.loading || this.disabled || this.ripple || this.hoverClass) return '';
			let hoverClass = '';
			hoverClass = this.plain ? 'u-' + this.type + '-plain-hover' : 'u-' + this.type + '-hover';
			return hoverClass;
		},
		// 在'primary', 'success', 'error', 'warning'类型下，不显示边框，否则会造成四角有毛刺现象
		showHairLineBorder() {
			if (['primary', 'success', 'error', 'warning'].indexOf(this.type) >= 0 && !this.plain) {
				return '';
			} else {
				return 'u-hairline-border';
			}
		},
		sizeCom() {
			const sizeMap = {
				mini: 'mini',
				small: 'mini',
				normal: 'medium',
				medium: 'medium',
				large: 'default'
			};
			return sizeMap[this.size] || 'default';
		}
	},
	data() {
		let btnTimerId = this.timerId || "button_" + Math.floor(Math.random() * 100000000 + 0);
		return {
			btnTimerId,
			rippleTop: 0, // 水波纹的起点Y坐标到按钮上边界的距离
			rippleLeft: 0, // 水波纹起点X坐标到按钮左边界的距离
			fields: {}, // 波纹按钮节点信息
			waveActive: false // 激活水波纹
		};
	},
	methods: {
		// 按钮点击
		click(e) {
			// 进行节流控制，每this.throttle毫秒内，只在开始处执行
			this.$u.throttle(() => {
				// 如果按钮时disabled和loading状态，不触发水波纹效果
				if (this.loading === true || this.disabled === true) return;
				// 是否开启水波纹效果
				if (this.ripple) {
					// 每次点击时，移除上一次的类，再次添加，才能触发动画效果
					this.waveActive = false;
					this.$nextTick(function() {
						this.getWaveQuery(e);
					});
				}
				this.$emit('click', e);
			}, this.throttleTime, true, this.btnTimerId);
		},
		// 查询按钮的节点信息
		getWaveQuery(e) {
			this.getElQuery().then(res => {
				// 查询返回的是一个数组节点
				let data = res[0];
				// 查询不到节点信息，不操作
				if (!data.width || !data.width) return;
				// 水波纹的最终形态是一个正方形(通过border-radius让其变为一个圆形)，这里要保证正方形的边长等于按钮的最长边
				// 最终的方形（变换后的圆形）才能覆盖整个按钮
				data.targetWidth = data.height > data.width ? data.height : data.width;
				if (!data.targetWidth) return;
				this.fields = data;
				let touchesX = '',
					touchesY = '';
				// #ifdef MP-BAIDU
				touchesX = e.changedTouches[0].clientX;
				touchesY = e.changedTouches[0].clientY;
				// #endif
				// #ifdef MP-ALIPAY
				touchesX = e.detail.clientX;
				touchesY = e.detail.clientY;
				// #endif
				// #ifndef MP-BAIDU || MP-ALIPAY
				touchesX = e.touches[0].clientX;
				touchesY = e.touches[0].clientY;
				// #endif
				// 获取触摸点相对于按钮上边和左边的x和y坐标，原理是通过屏幕的触摸点（touchesY），减去按钮的上边界data.top
				// 但是由于`transform-origin`默认是center，所以这里再减去半径才是水波纹view应该的位置
				// 总的来说，就是把水波纹的矩形（变换后的圆形）的中心点，移动到我们的触摸点位置
				this.rippleTop = touchesY - data.top - data.targetWidth / 2;
				this.rippleLeft = touchesX - data.left - data.targetWidth / 2;
				this.$nextTick(() => {
					this.waveActive = true;
				});
			});
		},
		// 获取节点信息
		getElQuery() {
			return new Promise(resolve => {
				let queryInfo = '';
				// 获取元素节点信息，请查看uniapp相关文档
				// https://uniapp.dcloud.io/api/ui/nodes-info?id=nodesrefboundingclientrect
				queryInfo = uni.createSelectorQuery().in(this);
				//#ifdef MP-ALIPAY
				queryInfo = uni.createSelectorQuery();
				//#endif
				queryInfo.select('.u-btn').boundingClientRect();
				queryInfo.exec(data => {
					resolve(data);
				});
			});
		},
		// 下面为对接uniapp官方按钮开放能力事件回调的对接
		getphonenumber(res) {
			this.$emit('getphonenumber', res);
		},
		getuserinfo(res) {
			this.$emit('getuserinfo', res);
		},
		error(res) {
			this.$emit('error', res);
		},
		opensetting(res) {
			this.$emit('opensetting', res);
		},
		launchapp(res) {
			this.$emit('launchapp', res);
		},
        chooseavatar(res) {
            this.$emit('chooseavatar', res);
 		}
	}
};
</script>

<style scoped lang="scss">
@import '../../libs/css/style.components.scss';
.u-btn::after {
	border: none;
}

.u-btn {
	position: relative;
	border: 0;
	//border-radius: 10rpx;
	/* #ifndef APP-NVUE */
	display: inline-flex;		
	/* #endif */
	// 避免边框某些场景可能被“裁剪”，不能设置为hidden
	overflow: visible;
	line-height: 1;
	@include vue-flex;
	align-items: center;
	justify-content: center;
	cursor: pointer;
	padding: 0 40rpx;
	z-index: 1;
	box-sizing: border-box;
	transition: all 0.15s;
	
	&--bold-border {
		border: 1px solid #ffffff;
	}
	
	&--default {
		color: $u-content-color;
		border-color: #c0c4cc;
		background-color: #ffffff;
	}
	
	&--primary {
		color: #ffffff;
		border-color: $u-type-primary;
		background-color: $u-type-primary;
	}
	
	&--success {
		color: #ffffff;
		border-color: $u-type-success;
		background-color: $u-type-success;
	}
	
	&--error {
		color: #ffffff;
		border-color: $u-type-error;
		background-color: $u-type-error;
	}
	
	&--warning {
		color: #ffffff;
		border-color: $u-type-warning;
		background-color: $u-type-warning;
	}
	
	&--default--disabled {
		color: #ffffff;
		border-color: #e4e7ed;
		background-color: #ffffff;
	}
	
	&--primary--disabled {
		color: #ffffff!important;
		border-color: $u-type-primary-disabled!important;
		background-color: $u-type-primary-disabled!important;
	}
	
	&--success--disabled {
		color: #ffffff!important;
		border-color: $u-type-success-disabled!important;
		background-color: $u-type-success-disabled!important;
	}
	
	&--error--disabled {
		color: #ffffff!important;
		border-color: $u-type-error-disabled!important;
		background-color: $u-type-error-disabled!important;
	}
	
	&--warning--disabled {
		color: #ffffff!important;
		border-color: $u-type-warning-disabled!important;
		background-color: $u-type-warning-disabled!important;
	}
	
	&--primary--plain {
		color: $u-type-primary!important;
		border-color: $u-type-primary-disabled!important;
		background-color: $u-type-primary-light!important;
	}
	
	&--success--plain {
		color: $u-type-success!important;
		border-color: $u-type-success-disabled!important;
		background-color: $u-type-success-light!important;
	}
	
	&--error--plain {
		color: $u-type-error!important;
		border-color: $u-type-error-disabled!important;
		background-color: $u-type-error-light!important;
	}
	
	&--warning--plain {
		color: $u-type-warning!important;
		border-color: $u-type-warning-disabled!important;
		background-color: $u-type-warning-light!important;
	}
	
	&--info {
		color: #ffffff;
		border-color: $u-type-info;
		background-color: $u-type-info;
	}
	&--info--disabled {
		color: #ffffff!important;
		border-color: $u-type-info-disabled!important;
		background-color: $u-type-info-disabled!important;
	}
	&--info--plain {
		color: $u-type-info!important;
		border-color: $u-type-info-disabled!important;
		background-color: $u-type-info-light!important;
	}
}

.u-hairline-border:after {
	content: ' ';
	position: absolute;
	pointer-events: none;
	// 设置为border-box，意味着下面的scale缩小为0.5，实际上缩小的是伪元素的内容（border-box意味着内容不含border）
	box-sizing: border-box;
	// 中心点作为变形(scale())的原点
	-webkit-transform-origin: 0 0;
	transform-origin: 0 0;
	left: 0;
	top: 0;
	width: 199.8%;
	height: 199.7%;
	-webkit-transform: scale(0.5, 0.5);
	transform: scale(0.5, 0.5);
	border: 1px solid currentColor;
	z-index: 1;
}

.u-wave-ripple {
	z-index: 0;
	position: absolute;
	border-radius: 100%;
	background-clip: padding-box;
	pointer-events: none;
	user-select: none;
	transform: scale(0);
	opacity: 1;
	transform-origin: center;
}

.u-wave-ripple.u-wave-active {
	opacity: 0;
	transform: scale(2);
	transition: opacity 1s linear, transform 0.4s linear;
}

.u-round-circle {
	border-radius: 100rpx;
}

.u-round-circle::after {
	border-radius: 100rpx;
}

.u-loading::after {
	background-color: hsla(0, 0%, 100%, 0.35);
}

.u-size-default {
	font-size: 30rpx;
	height: 80rpx;
	line-height: 80rpx;
}

.u-size-medium {
	/* #ifndef APP-NVUE */
	display: inline-flex;		
	/* #endif */
	width: auto;
	font-size: 26rpx;
	height: 70rpx;
	line-height: 70rpx;
	padding: 0 80rpx;
}

.u-size-mini {
	/* #ifndef APP-NVUE */
	display: inline-flex;		
	/* #endif */
	width: auto;
	font-size: 22rpx;
	padding-top: 1px;
	height: 50rpx;
	line-height: 50rpx;
	padding: 0 20rpx;
}

.u-primary-plain-hover {
	color: #ffffff !important;
	background: $u-type-primary-dark !important;
}

.u-default-plain-hover {
	color: $u-type-primary-dark !important;
	background: $u-type-primary-light !important;
}

.u-success-plain-hover {
	color: #ffffff !important;
	background: $u-type-success-dark !important;
}

.u-warning-plain-hover {
	color: #ffffff !important;
	background: $u-type-warning-dark !important;
}

.u-error-plain-hover {
	color: #ffffff !important;
	background: $u-type-error-dark !important;
}

.u-info-plain-hover {
	color: #ffffff !important;
	background: $u-type-info-dark !important;
}

.u-default-hover {
	color: $u-type-primary-dark !important;
	border-color: $u-type-primary-dark !important;
	background-color: $u-type-primary-light !important;
}

.u-primary-hover {
	background: $u-type-primary-dark !important;
	color: #fff;
}

.u-success-hover {
	background: $u-type-success-dark !important;
	color: #fff;
}

.u-info-hover {
	background: $u-type-info-dark !important;
	color: #fff;
}

.u-warning-hover {
	background: $u-type-warning-dark !important;
	color: #fff;
}

.u-error-hover {
	background: $u-type-error-dark !important;
	color: #fff;
}
</style>
