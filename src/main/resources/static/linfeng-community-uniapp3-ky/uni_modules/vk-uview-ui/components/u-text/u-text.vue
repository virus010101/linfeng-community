<template>
	<view class="u-text" :class="[]"
		v-if="show"
		:style="{
			margin: margin,
			justifyContent: align === 'left' ? 'flex-start' : align === 'center' ? 'center' : 'flex-end'
		}"
		@tap="clickHandler"
	>
		<text :class="['u-text__price', type && `u-text__value--${type}`]" v-if="mode === 'price'" :style="[valueStyle]">￥</text>
		<view class="u-text__prefix-icon" v-if="prefixIcon">
			<u-icon :name="prefixIcon" :customStyle="$u.addStyle(iconStyle)"></u-icon>
		</view>
		<u-link v-if="mode === 'link'" :href="href" underLine :text="value"></u-link>
		<template v-else-if="openType && isMp">
			<button
				class="u-reset-button u-text__value"
				:style="[valueStyle]"
				:class="[
						type && `u-text__value--${type}`,
				]"
				:data-index="index"
				:openType="openType"
				:lang="lang"
				:session-from="sessionFrom"
				:send-message-title="sendMessageTitle"
				:send-message-path="sendMessagePath"
				:send-message-img="sendMessageImg"
				:show-message-card="showMessageCard"
				:app-parameter="appParameter"
				@getuserinfo="onGetUserInfo"
				@contact="onContact"
				@getphonenumber="onGetPhoneNumber"
				@error="onError"
				@launchapp="onLaunchApp"
				@opensetting="onOpenSetting"
			>
				{{ value }}
			</button>
		</template>
		<text
			v-else
			class="u-text__value"
			:style="[valueStyle]"
			:class="[
					type && `u-text__value--${type}`,
					lines && `u-line-${lines}`
			]"
		>{{ value }}</text>
		<view class="u-text__suffix-icon" v-if="suffixIcon">
			<u-icon :name="suffixIcon" :customStyle="$u.addStyle(iconStyle)"></u-icon>
		</view>
	</view>
</template>

<script>
	import value from './value.js'
	import mixin from './libs/mixin/mixin.js'
	import button from './libs/mixin/button.js'
	import openType from './libs/mixin/openType.js'
	import addUnit from './libs/function/addUnit.js';
	import props from './props.js'

	/**
	 * Text 文本
	 * @description 此组件集成了文本类在项目中的常用功能，包括状态，拨打电话，格式化日期，*替换，超链接...等功能。 您大可不必在使用特殊文本时自己定义，text组件几乎涵盖您能使用的大部分场景。
	 * @tutorial https://www.uviewui.com/components/loading.html
	 * @property {String} 					type		主题颜色
	 * @property {Boolean} 					show		是否显示（默认 true ）
	 * @property {String | Number}			text		显示的值
	 * @property {String}					prefixIcon	前置图标
	 * @property {String} 					suffixIcon	后置图标
	 * @property {String} 					mode		文本处理的匹配模式 text-普通文本，price-价格，phone-手机号，name-姓名，date-日期，link-超链接
	 * 	@value text						普通文本（默认）
	 * 	@value price					价格
	 * 	@value phone					手机号
	 * 	@value name						姓名
	 * 	@value date						日期
	 * 	@value link						超链接
	 * @property {String} 					href		mode=link下，配置的链接
	 * @property {String | Function} 		format		格式化规则
	 * @property {Boolean} 					call		mode=phone时，点击文本是否拨打电话（默认 false ）
	 * @property {String} 					openType	小程序的打开方式
	 * @property {Boolean} 					bold		是否粗体，默认normal（默认 false ）
	 * @property {Boolean} 					block		是否块状（默认 false ）
	 * @property {String | Number} 			lines		文本显示的行数，如果设置，超出此行数，将会显示省略号
	 * @property {String} 					color		文本颜色（默认 '#303133' ）
	 * @property {String | Number} 			size		字体大小（默认 30rpx ）
	 * @property {Object | String} 			iconStyle	图标的样式 （默认 {fontSize: '30rpx'} ）
	 * @property {String} 					decoration	文字装饰，下划线，中划线等，可选值 none|underline|line-through（默认 'none' ）
	 * 	@value none						none 不处理（默认）
	 * 	@value underline 			underline 下划线
	 * 	@value line-through 	line-through 中划线
	 * @property {Object | String | Number}	margin		外边距，对象、字符串，数值形式均可（默认 0 ）
	 * @property {String | Number} 			lineHeight	文本行高
	 * @property {String} 		align		文本对齐方式，可选值left|center|right（默认 'left' ）
	 * 	@value left						左对齐（默认）
	 * 	@value center 				居中
	 * 	@value right 					右对齐
	 * @property {String} 			wordWrap	文字换行，可选值break-word|normal|anywhere（默认 'normal' ）
	 * 	@value normal 					normal 不换行（默认）
	 * 	@value break-word				break-word 换行
	 * 	@value anywhere					anywhere 换行
	 * @property {Boolean} 			copy		点击文字后是否复制，默认false
	 * @event {Function} click  点击触发事件
	 * @example <u-text text="我用十年青春,赴你最后之约"></u-text>
	 * @example <u--text text="我用十年青春,赴你最后之约"></u--text>
	 */
	export default {
		name: 'u-text',
		emits: ["click","copy"],
		// #ifdef MP-WEIXIN
		// 将自定义节点设置成虚拟的，更加接近Vue组件的表现，能更好的使用flex属性
		options: {
			virtualHost: true
		},
		// #endif
		// #ifdef MP
		mixins: [mixin, value, button, openType, props],
		// #endif
		// #ifndef MP
		mixins: [mixin, value, props],
		// #endif
		computed: {
			valueStyle() {
				const style = {
					textDecoration: this.decoration,
					fontWeight: this.bold ? 'bold' : 'normal',
					wordWrap: this.wordWrap,
					fontSize: addUnit(this.size)
				};
				!this.type && (style.color = this.color);
				this.isNvue && this.lines && (style.lines = this.lines);
				this.lineHeight && (style.lineHeight = addUnit(this.lineHeight));
				!this.isNvue && this.block && (style.display = 'block');
				return uni.$u.deepMerge(style, uni.$u.addStyle(this.customStyle));
			},
			isNvue() {
				let nvue = false;
				// #ifdef APP-NVUE
				nvue = true;
				// #endif
				return nvue;
			},
			isMp() {
				let mp = false;
				// #ifdef MP
				mp = true;
				// #endif
				return mp;
			}
		},
		data() {
			return {};
		},
		methods: {
			clickHandler() {
				// 如果为手机号模式，拨打电话
				if (this.call && this.mode === 'phone') {
					uni.makePhoneCall({
						phoneNumber: this.text
					});
				} else if (this.copyText) {
					uni.setClipboardData({
						data: this.copyText,
						success: () => {
							this.$emit('copy', this.copyText);
						}
					})
				}
				this.$emit('click');
			}
		}
	}
</script>

<style lang="scss" scoped>
	@import "./theme.scss";

	/* #ifndef APP-NVUE */
	// 由于uView是基于nvue环境进行开发的，此环境中普通元素默认为flex-direction: column;
	// 所以在非nvue中，需要对元素进行重置为flex-direction: column; 否则可能会表现异常
	view,
	scroll-view,
	swiper-item {
		display: flex;
		flex-direction: column;
		flex-shrink: 0;
		flex-grow: 0;
		flex-basis: auto;
		align-items: stretch;
		align-content: flex-start;
	}
	/* #endif */

	.u-text {
		@include flex(row);
		align-items: center;
		flex-wrap: nowrap;
		flex: 1;
		/* #ifndef APP-NVUE */
		width: 100%;
		/* #endif */

		&__price {
			font-size: 14px;
			color: $u-content-color;
		}

		&__value {
			font-size: 14px;
			@include flex;
			color: $u-content-color;
			flex-wrap: wrap;
			// flex: 1;
			text-overflow: ellipsis;
			align-items: center;

			&--primary {
				color: $u-primary;
			}

			&--warning {
				color: $u-warning;
			}

			&--success {
				color: $u-success;
			}

			&--info {
				color: $u-info;
			}

			&--error {
				color: $u-error;
			}

			&--main {
				color: $u-main-color;
			}

			&--content {
				color: $u-content-color;
			}

			&--tips {
				color: $u-tips-color;
			}

			&--light {
				color: $u-light-color;
			}
		}
	}
	/* #ifdef MP */
	.u-text{
		flex: inherit;
		width: inherit;
	}
	/* #endif */
</style>
