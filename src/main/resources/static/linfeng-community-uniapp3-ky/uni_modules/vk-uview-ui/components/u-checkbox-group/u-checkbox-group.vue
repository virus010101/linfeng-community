<template>
	<view class="u-checkbox-group u-clearfix" :class="uFromData.inputAlign == 'right' ? 'flex-end' : ''"><slot></slot></view>
</template>

<script>
import Emitter from "../../libs/util/emitter.js";
/**
 * checkboxGroup 开关选择器父组件Group
 * @description 复选框组件一般用于需要多个选择的场景，该组件功能完整，使用方便
 * @tutorial https://www.uviewui.com/components/checkbox.html
 * @property {String Number} max 最多能选中多少个checkbox（默认999）
 * @property {String Number} size 组件整体的大小，单位rpx（默认40）
 * @property {Boolean} disabled 是否禁用所有checkbox（默认false）
 * @property {String Number} icon-size 图标大小，单位rpx（默认20）
 * @property {Boolean} label-disabled 是否禁止点击文本操作checkbox(默认false)
 * @property {String} width 宽度，需带单位
 * @property {String} width 宽度，需带单位
 * @property {String} shape 外观形状，shape-方形，circle-圆形(默认circle)
 * @property {Boolean} wrap 是否每个checkbox都换行（默认false）
 * @property {String} active-color 选中时的颜色，应用到所有子Checkbox组件（默认#2979ff）
 * @event {Function} change 任一个checkbox状态发生变化时触发，回调为一个对象
 * @example <u-checkbox-group></u-checkbox-group>
 */
export default {
	name: "u-checkbox-group",
	emits: ["update:modelValue", "input", "change"],
	mixins: [Emitter],
	props: {
		// 匹配某一个radio组件，如果某个radio的name值等于此值，那么这个radio就被会选中
		value: {
			type: [String, Number, Array, Boolean],
			default: ""
		},
		modelValue: {
			type: [String, Number, Array, Boolean],
			default: ""
		},
		// 最多能选中多少个checkbox
		max: {
			type: [Number, String],
			default: 999
		},
		// 所有选中项的 name
		// value: {
		// 	default: Array,
		// 	default() {
		// 		return []
		// 	}
		// },
		// 是否禁用所有复选框
		disabled: {
			type: Boolean,
			default: false
		},
		// 在表单内提交时的标识符
		name: {
			type: [Boolean, String],
			default: ""
		},
		// 是否禁止点击提示语选中复选框
		labelDisabled: {
			type: Boolean,
			default: false
		},
		// 形状，square为方形，circle为圆型
		shape: {
			type: String,
			default: "square"
		},
		// 选中状态下的颜色
		activeColor: {
			type: String,
			default: "#2979ff"
		},
		// 组件的整体大小
		size: {
			type: [String, Number],
			default: 34
		},
		// 每个checkbox占u-checkbox-group的宽度
		width: {
			type: String,
			default: "auto"
		},
		// 是否每个checkbox都换行
		wrap: {
			type: Boolean,
			default: false
		},
		// 图标的大小，单位rpx
		iconSize: {
			type: [String, Number],
			default: 20
		}
	},
	data() {
		return {
			values: [],
			uFromData: {
				inputAlign: "left"
			}
		};
	},
	created() {
		// 如果将children定义在data中，在微信小程序会造成循环引用而报错
		this.children = [];
	},
	mounted() {
		// 支付宝、头条小程序不支持provide/inject，所以使用这个方法获取整个父组件，在created定义，避免循环应用
		let parent = this.$u.$parent.call(this, "u-form");
		if (parent) {
			Object.keys(this.uFromData).map(key => {
				this.uFromData[key] = parent[key];
			});
		}
	},
	methods: {
		emitEvent(obj) {
			let values = this.values || [];
			if (obj.value) {
				let index = values.indexOf(obj.name);
				if (index === -1) {
					values.push(obj.name);
				}
			} else {
				let index = values.indexOf(obj.name);
				if (index > -1) {
					values.splice(index, 1);
				}
			}

			this.$emit("change", values);
			// 通过emit事件，设置父组件通过v-model双向绑定的值
			this.$emit("input", values);
			this.$emit("update:modelValue", values);
			// 发出事件，用于在表单组件中嵌入checkbox的情况，进行验证
			// 由于头条小程序执行迟钝，故需要用几十毫秒的延时
			setTimeout(() => {
				// 将当前的值发送到 u-form-item 进行校验
				this.dispatch("u-form-item", "onFieldChange", values);
			}, 60);
		},
		_emitEvent(obj) {
			let values = this.values || [];
			if (obj.value) {
				let index = values.indexOf(obj.name);
				if (index === -1) {
					values.push(obj.name);
				}
			} else {
				let index = values.indexOf(obj.name);
				if (index > -1) {
					values.splice(index, 1);
				}
			}
			//this.$emit("change", values);
		}
	}
};
</script>

<style lang="scss" scoped>
@import "../../libs/css/style.components.scss";

.u-checkbox-group {
	/* #ifndef MP || APP-NVUE */
	display: inline-flex;
	flex-wrap: wrap;
	/* #endif */
}
.u-checkbox-group.flex-end {
	/* #ifndef APP-NVUE */
	display: inline-flex;
	justify-content: flex-end;
	flex-wrap: wrap;
	/* #endif */
}
</style>
