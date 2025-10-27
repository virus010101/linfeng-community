<template>
	<view class="u-count-down">
		<slot>
			<text class="u-count-down__text" :style="customStyle">{{ formattedTime }}</text>
		</slot>
	</view>
</template>

<script>
import { isSameSecond, parseFormat, parseTimeData } from "./utils";
/**
 * u-count-down 倒计时
 * @description 该组件一般使用于某个活动的截止时间上，通过数字的变化，给用户明确的时间感受，提示用户进行某一个行为操作。
 * @tutorial https://uviewui.com/components/countDown.html
 * @property {String | Number}	timestamp		倒计时时长，单位ms （默认 0 ）
 * @property {String}			format		时间格式，DD-日，HH-时，mm-分，ss-秒，SSS-毫秒  （默认 'HH:mm:ss' ）
 * @property {Boolean}			autoStart	是否自动开始倒计时 （默认 true ）
 * @event {Function} end 倒计时结束时触发
 * @event {Function} change 倒计时变化时触发
 * @event {Function} start	开始倒计时
 * @event {Function} pause	暂停倒计时
 * @event {Function} reset	重设倒计时，若 auto-start 为 true，重设后会自动开始倒计时
 * @example <u-count-down :timestamp="timestamp"></u-count-down>
 */
export default {
	name: "u-count-down",  
	emits: ["change", "end", "finish"],
	props: {
		// 倒计时时长，单位ms
		timestamp: {
			type: [String, Number],
			default: 0
		},
		// 时间格式，DD-日，HH-时，mm-分，ss-秒，SSS-毫秒
		format: {
			type: String,
			default: "DD:HH:mm:ss"
		},
		// 是否自动开始倒计时
		autoStart: {
			type: Boolean,
			default: true
		},
		customStyle: {
			type: [String, Object],
			default: ""
		}
	},
	data() {
		return {
			timer: null,
			// 各单位(天，时，分等)剩余时间
			timeData: parseTimeData(0),
			// 格式化后的时间，如"03:23:21"
			formattedTime: "0",
			// 倒计时是否正在进行中
			runing: false,
			endTime: 0, // 结束的毫秒时间戳
			remainTime: 0 // 剩余的毫秒时间
		};
	},
	watch: {
		timestamp(n) {
			this.reset();
		},
		format(newVal, oldVal) {
			this.pause();
			this.start();
		}
	},
	mounted() {
		this.init();
	},
	methods: {
		init() {
			this.reset();
		},
		// 开始倒计时
		start() {
			if (this.runing) return;
			// 标识为进行中
			this.runing = true;
			// 结束时间戳 = 此刻时间戳 + 剩余的时间
			this.endTime = Date.now() + this.remainTime;
			this.toTick();
		},
		// 根据是否展示毫秒，执行不同操作函数
		toTick() {
			if (this.format.indexOf("SSS") > -1) {
				this.microTick();
			} else {
				this.macroTick();
			}
		},
		macroTick() {
			this.clearTimeout();
			// 每隔一定时间，更新一遍定时器的值
			// 同时此定时器的作用也能带来毫秒级的更新
			this.timer = setTimeout(() => {
				// 获取剩余时间
				const remain = this.getRemainTime();
				// 重设剩余时间
				if (!isSameSecond(remain, this.remainTime) || remain === 0) {
					this.setRemainTime(remain);
				}
				// 如果剩余时间不为0，则继续检查更新倒计时
				if (this.remainTime !== 0) {
					this.macroTick();
				}
			}, 30);
		},
		microTick() {
			this.clearTimeout();
			this.timer = setTimeout(() => {
				this.setRemainTime(this.getRemainTime());
				if (this.remainTime !== 0) {
					this.microTick();
				}
			}, 30);
		},
		// 获取剩余的时间
		getRemainTime() {
			// 取最大值，防止出现小于0的剩余时间值
			return Math.max(this.endTime - Date.now(), 0);
		},
		// 设置剩余的时间
		setRemainTime(remain) {
			this.remainTime = remain;
			// 根据剩余的毫秒时间，得出该有天，小时，分钟等的值，返回一个对象
			const timeData = parseTimeData(remain);
			this.$emit("change", timeData);
			// 得出格式化后的时间
			this.formattedTime = parseFormat(this.format, timeData);
			// 如果时间已到，停止倒计时
			if (remain <= 0) {
				this.pause(); 
				this.$emit("end");
				this.$emit("finish");
			}
		},
		// 重置倒计时
		reset() {
			this.pause();
			this.remainTime = this.timestamp;
			this.setRemainTime(this.remainTime);
			if (this.autoStart) {
				this.start();
			}
		},
		// 暂停倒计时
		pause() {
			this.runing = false;
			this.clearTimeout();
		},
		// 清空定时器
		clearTimeout() {
			clearTimeout(this.timer);
			this.timer = null;
		}
	},
	// #ifdef VUE2
	beforeDestroy() {
		this.clearTimeout();
	},
	// #endif
	
	// #ifdef VUE3
	beforeUnmount() {
		this.clearTimeout();
	},
	// #endif
};
</script>

<style lang="scss"></style>
