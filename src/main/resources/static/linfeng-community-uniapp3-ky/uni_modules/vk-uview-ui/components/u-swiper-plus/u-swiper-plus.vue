<template>
	<view
		class="u-swiper"
		:style="{
			backgroundColor: bgColor,
			height: $u.addUnit(height),
			borderRadius: $u.addUnit(radius)
		}"
	>
		<view
			class="u-swiper__loading"
			v-if="loading"
		>
			<u-loading mode="circle"></u-loading>
		</view>
		<swiper
			v-else
			class="u-swiper__wrapper"
			:style="{
				height: $u.addUnit(height),
			}"
			@change="change"
			:circular="circular"
			:interval="interval"
			:duration="duration"
			:autoplay="autoplayCom"
			:current="currentIndex"
			:currentItemId="currentItemId"
			:previousMargin="$u.addUnit(previousMargin)"
			:nextMargin="$u.addUnit(nextMargin)"
			:acceleration="acceleration"
			:displayMultipleItems="displayMultipleItemsCom"
			:easingFunction="easingFunction"
		>
			<swiper-item
				class="u-swiper__wrapper__item"
				v-for="(item, index) in list"
				:key="index"
			>
				<view
					class="u-swiper__wrapper__item__wrapper"
					:style="[itemStyle(index)]"
				>
					<!-- 在nvue中，image图片的宽度默认为屏幕宽度，需要通过flex:1撑开，另外必须设置高度才能显示图片 -->
					<image
						class="u-swiper__wrapper__item__wrapper__image"
						v-if="getItemType(item) === 'image'"
						:src="getSource(item)"
						:mode="imgMode"
						@tap="clickHandler(index)"
						:style="{
							height: $u.addUnit(height),
							borderRadius: $u.addUnit(radius)
						}"
					></image>
					<!-- #ifdef H5 || MP-WEIXIN || APP-NVUE -->
					<!-- 只有h5和微信小程序和nvue可以直接播放视频 -->
					<video
						class="u-swiper__wrapper__item__wrapper__video"
						v-if="getItemType(item) === 'video'"
						:id="`video-${index}`"
						:enable-progress-gesture="false"
						:src="getSource(item)"
						:poster="getPoster(item,'auto')"
						:title="showTitle && $u.test.object(item) && item.title ? item.title : ''"
						:style="{
							height: $u.addUnit(height)
						}"
						:controls="true"
						:show-mute-btn="true"
						:autoplay="videoAutoplayFn(index)"
						:muted="videoMuted"
						:object-fit="item.objectFit"
						@tap="clickHandler(index)"
						@ended="videoEnded(index)"
					></video>
					<!-- #endif -->
					<!-- #ifndef H5 || MP-WEIXIN || APP-NVUE-->
					<!-- 其他端只能展示图片，点击图片播放视频 -->
					<image
						v-if="getItemType(item) === 'video'"
						class="u-swiper__wrapper__item__wrapper__image"
						:src="getPoster(item)"
						mode="aspectFill"
						:style="{
							height: $u.addUnit(height),
							borderRadius: $u.addUnit(radius)
						}"
					></image>
					<image
						v-if="getItemType(item) === 'video'"
						class="play-btn"
						src="data:image/png;base64,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"
						mode="widthFix"
						@tap.stop="previewVideo(index)"
					></image>
					<!-- #endif -->
					<text
						v-if="showTitle && $u.test.object(item) && item.title && $u.test.image(getSource(item))"
						class="u-swiper__wrapper__item__wrapper__title u-line-1"
					>{{ item.title }}</text>
				</view>
			</swiper-item>
		</swiper>
		<view class="u-swiper__indicator" :style="[$u.addStyle(indicatorStyle)]">
			<slot name="indicator">
				<u-swiper-indicator
					v-if="!loading && indicator && !showTitle"
					:indicatorActiveColor="indicatorActiveColor"
					:indicatorInactiveColor="indicatorInactiveColor"
					:length="list.length"
					:current="currentIndex"
					:indicatorMode="indicatorMode"
				></u-swiper-indicator>
			</slot>
		</view>

		<u-popup v-model="videoPopup.show" mode="center" :duration="0" :blur="10" :closeable="true">
			<video
				v-if="videoPopup.show"
				class="u-swiper__wrapper__item__wrapper__video"
				:enable-progress-gesture="false"
				:src="videoPopup.url"
				:poster="videoPopup.poster"
				style="height: 60vh;"
				:controls="true"
				:show-mute-btn="true"
				:autoplay="true"
				:muted="false"
				object-fit="contain"
				@ended="videoEnded(index)"
			></video>
		</u-popup>
	</view>
</template>

<script>
	import uSwiperIndicator from "./components/u-swiper-indicator"
	/**
	 * Swiper 轮播图
	 * @description 该组件一般用于导航轮播，广告展示等场景,可开箱即用，
	 * @tutorial https://www.uviewui.com/components/swiper.html
	 * @property {Array}			list					轮播图数据
	 * @property {Boolean}			indicator				是否显示面板指示器（默认 false ）
	 * @property {String}			indicatorActiveColor	指示器非激活颜色（默认 '#FFFFFF' ）
	 * @property {String}			indicatorInactiveColor	指示器的激活颜色（默认 'rgba(255, 255, 255, 0.35)' ）
	 * @property {String | Object}	indicatorStyle			指示器样式，可通过bottom，left，right进行定位
	 * @property {String}			indicatorMode			指示器模式（默认 'line' ）
	 * @property {Boolean}			autoplay				是否自动切换（默认 true ）
	 * @property {String | Number}	current					当前所在滑块的 index（默认 0 ）
	 * @property {String}			currentItemId			当前所在滑块的 item-id ，不能与 current 被同时指定
	 * @property {String | Number}	interval				滑块自动切换时间间隔（ms）（默认 3000 ）
	 * @property {String | Number}	duration				滑块切换过程所需时间（ms）（默认 300 ）
	 * @property {Boolean}			circular				播放到末尾后是否重新回到开头（默认 false ）
	 * @property {String | Number}	previousMargin			前边距，可用于露出前一项的一小部分，nvue和支付宝不支持（默认 0 ）
	 * @property {String | Number}	nextMargin				后边距，可用于露出后一项的一小部分，nvue和支付宝不支持（默认 0 ）
	 * @property {Boolean}			acceleration			当开启时，会根据滑动速度，连续滑动多屏，支付宝不支持（默认 false ）
	 * @property {Number}			displayMultipleItems	同时显示的滑块数量，nvue、支付宝小程序不支持（默认 1 ）
	 * @property {String}			easingFunction			指定swiper切换缓动动画类型， 只对微信小程序有效（默认 'default' ）
	 * @property {String}			keyName					list数组中指定对象的目标属性名（默认 'url' ）
	 * @property {String}			imgMode					图片的裁剪模式（默认 'aspectFill' ）
	 * @property {String | Number}	height					组件高度（默认 130 ）
	 * @property {String}			bgColor					背景颜色（默认 	'#f3f4f6' ）
	 * @property {String | Number}	radius					组件圆角，数值或带单位的字符串（默认 4 ）
	 * @property {Boolean}			loading					是否加载中（默认 false ）
	 * @property {Boolean}			showTitle				是否显示标题，要求数组对象中有title属性（默认 false ）
	 * @event {Function(index)}	click	点击轮播图时触发	index：点击了第几张图片，从0开始
	 * @event {Function(index)}	change	轮播图切换时触发(自动或者手动切换)	index：切换到了第几张图片，从0开始
	 * @example	<u-swiper :list="list4" keyName="url" :autoplay="false"></u-swiper>
	 */
	export default {
		name: 'u-swiper',
		emits: ["click", "change"],
		components:{
			uSwiperIndicator
		},
		props: {
			// 列表数组，元素可为字符串，如为对象可通过keyName指定目标属性名
			list: {
				type: [Array, null],
				default () {
					return [];
				}
			},
			// 是否显示面板指示器
			indicator: {
			    type: Boolean,
			    default: false
			},
			// 指示器非激活颜色
			indicatorActiveColor: {
			    type: String,
			    default: "#FFFFFF"
			},
			// 指示器的激活颜色
			indicatorInactiveColor: {
			    type: String,
			    default: "rgba(255, 255, 255, 0.35)"
			},
			// 指示器样式，可通过bottom，left，right进行定位
			indicatorStyle: {
			    type: [String, Object],
			    default: '',
			},
			// 指示器模式，line-线型，dot-点型
			indicatorMode: {
			    type: String,
			    default: 'line',
			},
			// 是否自动切换
			autoplay: {
			    type: Boolean,
			    default: true
			},
			// 当前所在滑块的 index
			current: {
			    type: [String, Number],
			    default: 0
			},
			// 当前所在滑块的 item-id ，不能与 current 被同时指定
			currentItemId: {
			    type: String,
			    default: '',
			},
			// 滑块自动切换时间间隔
			interval: {
			    type: [String, Number],
			    default: 3000
			},
			// 滑块切换过程所需时间
			duration: {
			    type: [String, Number],
			    default: 300
			},
			// 播放到末尾后是否重新回到开头
			circular: {
			    type: Boolean,
			    default: false
			},
			// 前边距，可用于露出前一项的一小部分，nvue和支付宝不支持
			previousMargin: {
			    type: [String, Number],
			    default: 0
			},
			// 后边距，可用于露出后一项的一小部分，nvue和支付宝不支持
			nextMargin: {
			    type: [String, Number],
			    default: 0
			},
			// 当开启时，会根据滑动速度，连续滑动多屏，支付宝不支持
			acceleration: {
			    type: Boolean,
			    default: false
			},
			// 同时显示的滑块数量，nvue、支付宝小程序不支持
			displayMultipleItems: {
			    type: Number,
			    default: 1
			},
			// 指定swiper切换缓动动画类型，有效值：default、linear、easeInCubic、easeOutCubic、easeInOutCubic
			// 只对微信小程序有效
			easingFunction: {
			    type: String,
			    default: 'default'
			},
			// list数组中指定对象的目标属性名
			keyName: {
			    type: String,
			    default: 'url'
			},
			// 图片的裁剪模式
			imgMode: {
			    type: String,
			    default: 'aspectFill'
			},
			// 组件高度
			height: {
			    type: [String, Number],
			    default: 130
			},
			// 背景颜色
			bgColor: {
			    type: String,
			    default: '#f3f4f6'
			},
			// 组件圆角，数值或带单位的字符串
			radius: {
			    type: [String, Number],
			    default: 4
			},
			// 是否加载中
			loading: {
			    type: Boolean,
			    default: false
			},
			// 是否显示标题，要求数组对象中有title属性
			showTitle: {
			    type: Boolean,
			    default: false
			},
			// 视频是否自动播放
			videoAutoplay: {
			    type: Boolean,
			    default: false
			},
			// 视频是否静音播放
			videoMuted: {
			    type: Boolean,
			    default: false
			},
		},
		data() {
			return {
				$u: uni.$u,
				currentIndex: 0,
				videoPopup: {
					show: false,
					url: ""
				}
			}
		},
		watch: {
			current(val, preVal) {
				if(val === preVal) return;
				this.currentIndex = val; // 和上游数据关联上
			},

			// #ifdef H5 || MP-WEIXIN || APP-NVUE
			currentIndex(newVal, oldVal){
				if (this.checkIsVideo(oldVal)) {
					// 如果是视频，则暂停
					this.videoPopup.show = false;
					this.videoPopup.url = "";
					let videoContext = uni.createVideoContext(`video-${oldVal}`, this);
					videoContext.pause();
					videoContext.exitFullScreen();
				}
				if (this.checkIsVideo(newVal) && this.videoAutoplay) {
					// 如果是视频，则播放
					let videoContext = uni.createVideoContext(`video-${newVal}`, this);
					videoContext.play();
				}
			}
			// #endif
		},
		computed: {
			itemStyle() {
				return index => {
					const style = {}
					// #ifndef APP-NVUE || MP-TOUTIAO
					// 左右流出空间的写法不支持nvue和头条
					// 只有配置了此二值，才加上对应的圆角，以及缩放
					if (this.nextMargin && this.previousMargin) {
						style.borderRadius = uni.$u.addUnit(this.radius)
						if (index !== this.currentIndex) style.transform = 'scale(0.92)'
					}
					// #endif
					return style
				}
			},
			mutedCom() {
				// #ifdef H5
				return true;
				// #endif
				// #ifndef H5
				return false;
				// #endif
			},
			currentIsVideoCom(){
				let { currentIndex } = this;
				return this.checkIsVideo(currentIndex);
			},
			autoplayCom() {
				let { autoplay, currentIsVideoCom } = this;
				return currentIsVideoCom ? false : autoplay;
			},
			displayMultipleItemsCom(){
				let { displayMultipleItems, list } = this;
				if (list && list.length > 0) {
					return displayMultipleItems;
				} else {
					return 0
				}
			}
		},
		methods: {
      getItemType(item) {
        if (typeof item === 'string') return uni.$u.test.video(this.getSource(item)) ? 'video' : 'image'
        if (typeof item === 'object' && this.keyName) {
          if (!item.type) return uni.$u.test.video(this.getSource(item)) ? 'video' : 'image'
          if (item.type === 'image') return 'image'
          if (item.type === 'video') return 'video'
          return 'image'
        }
      },
			// 获取目标路径，可能数组中为字符串，对象的形式，额外可指定对象的目标属性名keyName
			getSource(item) {
				if (typeof item === 'string') return item
				if (typeof item === 'object' && this.keyName) return item[this.keyName]
				else uni.$u.error('请按格式传递列表参数')
				return ''
			},
			// 轮播切换事件
			change(e) {
				// 当前的激活索引
				const {
					current
				} = e.detail
				this.currentIndex = current
				this.$emit('change', e.detail)
			},
			// 当一个轮播item为视频时，获取它的视频海报
			getPoster(item, mode) {
				let { height } = this;
				if (mode === "auto") {
					return;
				}
				if (typeof item === 'object' && item.poster) {
					return item.poster;
				}
				let url = this.getSource(item);
				if (url.indexOf("cdn.bspapp.com")>-1 || url.indexOf("aliyuncs.com")>-1) {
					// 只有阿里云oss才支持
					let poster = `${url}?x-oss-process=video/snapshot,t_2000,f_jpg,w_0,h_0,m_fast,ar_auto`;
					return poster;
				} else {
					return;
				}
			},
			// 点击某个item
			clickHandler(index) {
				this.$emit('click', index)
			},
			// 下一页
			nextPage(){
				let { currentIndex=0, list=[] } = this;
				currentIndex++;
				currentIndex = currentIndex % list.length;
				this.currentIndex = currentIndex;
			},
			// 视频播放完毕后
			videoEnded(index){
				if (!this.videoPopup.show) {
					setTimeout(() => {
						this.nextPage();
					}, 1000);
				}
			},
			// 检测当前轮播显示的是否是视频
			checkIsVideo(index){
				let { list, currentIndex } = this;
				if (typeof index === "undefined") index = currentIndex;
				let item = list[index];
				return this.getItemType(item) === "video" ? true : false;
			},
			// 预览视频
			previewVideo(index){
				let { list, currentIndex } = this;
				if (typeof index === "undefined") index = currentIndex;
				let item = list[index];
				this.videoPopup.show = true;
				this.videoPopup.url = this.getSource(item);
			},
			videoAutoplayFn(index){
				let { list, current, videoAutoplay } = this;
				if (index === current && videoAutoplay) {
					return true;
				} else {
					return false;
				}
			}
		}
	}
</script>

<style lang="scss" scoped>
	@import "../../libs/css/style.components.scss";

	.u-swiper {
		@include vue-flex;
		justify-content: center;
		align-items: center;
		position: relative;
		overflow: hidden;

		&__wrapper {
			flex: 1;

			&__item {
				flex: 1;

				&__wrapper {
					@include vue-flex;
					position: relative;
					overflow: hidden;
					transition: transform 0.3s;
					flex: 1;

					&__image {
						flex: 1;
					}

					&__video {
						flex: 1;
					}

					&__title {
						position: absolute;
						background-color: rgba(0, 0, 0, 0.3);
						bottom: 0;
						left: 0;
						right: 0;
						font-size: 28rpx;
						padding: 12rpx 24rpx;
						color: #FFFFFF;
						flex: 1;
					}
				}
			}
		}

		&__indicator {
			position: absolute;
			bottom: 10px;
		}

		.play-btn{
			position: absolute;
			width: 100rpx;
			height: 100rpx;
			top:calc(50% - 50rpx);
			left:calc(50% - 50rpx);
			display: block;
			z-index: 99;
		}
	}
</style>
