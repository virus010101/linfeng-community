import propsDefaultValue from './props-default-value.js'

export default {
	props: {
		// 主题颜色
		type: {
			type: String,
			default: propsDefaultValue.type
		},
		// 是否显示
		show: {
			type: Boolean,
			default: propsDefaultValue.show
		},
		// 显示的值
		text: {
			type: [String, Number],
			default: propsDefaultValue.text
		},
		// 前置图标
		prefixIcon: {
			type: String,
			default: propsDefaultValue.prefixIcon
		},
		// 后置图标
		suffixIcon: {
			type: String,
			default: propsDefaultValue.suffixIcon
		},
		// 文本处理的匹配模式
		// text-普通文本，price-价格，phone-手机号，name-姓名，date-日期，link-超链接
		mode: {
			type: String,
			default: propsDefaultValue.mode
		},
		// mode=link下，配置的链接
		href: {
			type: String,
			default: propsDefaultValue.href
		},
		// 格式化规则
		format: {
			type: [String, Function],
			default: propsDefaultValue.format
		},
		// mode=phone时，点击文本是否拨打电话
		call: {
			type: Boolean,
			default: propsDefaultValue.call
		},
		// 小程序的打开方式
		openType: {
			type: String,
			default: propsDefaultValue.openType
		},
		// 是否粗体，默认normal
		bold: {
			type: Boolean,
			default: propsDefaultValue.bold
		},
		// 是否块状
		block: {
			type: Boolean,
			default: propsDefaultValue.block
		},
		// 文本显示的行数，如果设置，超出此行数，将会显示省略号
		lines: {
			type: [String, Number],
			default: propsDefaultValue.lines
		},
		// 文本颜色
		color: {
			type: String,
			default: propsDefaultValue.color
		},
		// 字体大小
		size: {
			type: [String, Number],
			default: propsDefaultValue.size
		},
		// 图标的样式
		iconStyle: {
			type: [Object, String],
			default: propsDefaultValue.iconStyle
		},
		// 文字装饰，下划线，中划线等，可选值 none|underline|line-through
		decoration: {
			type: String,
			default: propsDefaultValue.decoration
		},
		// 外边距，对象、字符串，数值形式均可
		margin: {
			type: [Object, String, Number],
			default: propsDefaultValue.margin
		},
		// 文本行高
		lineHeight: {
			type: [String, Number],
			default: propsDefaultValue.lineHeight
		},
		// 文本对齐方式，可选值left|center|right
		align: {
			type: String,
			default: propsDefaultValue.align
		},
		// 文字换行，可选值break-word|normal|anywhere
		wordWrap: {
			type: String,
			default: propsDefaultValue.wordWrap
		},
		copyText: {
			type: String,
			default: propsDefaultValue.copyText
		}
	}
}
