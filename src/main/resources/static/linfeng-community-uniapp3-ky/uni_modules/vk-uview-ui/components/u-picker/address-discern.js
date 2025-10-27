class AddressDiscern {
	// 初始化数据源
	constructor(dataSource = {}) {
		this.dataSource = dataSource;
	}

	// 智能解析收货地址
	discern(text) {
		if (!text) return { code: -1, msg: "地址文本不能为空" };
		// 清理文本，去除多余空格
		text = text.trim();
		// 先解析地址，因为地址最容易识别
		const positionRes = this.regionDiscern(text);
		if (positionRes.code !== 0) return positionRes;
		// 再解析姓名和手机号
		const { name, mobile } = this.nameMobileDiscern(positionRes.not_address_text);
		const status = name && mobile && positionRes.data ? 1 : 0;
		let msg = "ok";
		if (status === 0) {
			msg = "未识别：";
			if (!name) msg += "姓名、";
			if (!mobile) msg += "手机号、";
			msg = msg.slice(0, -1);
		}
		const res = {
			code: 0,
			msg,
			status, // 1 表示全部识别成功，0 表示部分成功
			data: {
				name, // 姓名
				mobile, // 手机号
				position: positionRes.data, // 省市区街道信息
			}
		};
		return res;
	}

	// 智能识别省市区
	regionDiscern(addressText) {
		const { provinces, citys, areas } = this.dataSource;
		const province = {};
		const city = {};
		const area = {};
		let address = "";

		if (!addressText) return { code: -1, msg: "地址文本不能为空" };
		addressText = addressText.trim();

		// 获取省份索引
		const findProvinceIndex = () => {
			const index = provinces.findIndex(({ name }) => addressText.includes(name.substring(0, 2)));
			return index;
		};

		// 获取地级市索引
		const findCityIndex = (citys) => {
			const index = citys.findIndex(({ name }) => addressText.includes(name.slice(0, -1)));
			return index;
		};

		// 获取县级市索引
		const findAreaIndex = (areas) => {
			const index = areas.findIndex(({ name }) => {
				const reg = name.length > 2 ? `${name}|${name.slice(0, -1)}` : name;
				const areaRegExp = new RegExp(reg);
				if (areaRegExp.test(addressText)) {
					address = addressText.replace(areaRegExp, "{{~}}").split("{{~}}")[1] || "";
					address = address.split(new RegExp("[^\\u4e00-\\u9fa5a-zA-Z0-9+-（）()]+", "g"))[0];
					return true;
				}
				return false;
			});
			return index;
		};

		// 通过市倒推省份
		const findProvinceByCity = () => {
			for (let i = 0; i < citys.length; i++) {
				const index = findCityIndex(citys[i]);
				if (index !== -1) {
					return { provinceIndex: i, cityIndex: index };
				}
			}
			return { provinceIndex: -1, cityIndex: -1 };
		};

		// 通过区倒推市和省份
		const findProvinceByArea = () => {
			for (let i = 0; i < areas.length; i++) {
				for (let j = 0; j < areas[i].length; j++) {
					const index = findAreaIndex(areas[i][j]);
					if (index !== -1) {
						return { provinceIndex: i, cityIndex: j, areaIndex: index };
					}
				}
			}
			return { provinceIndex: -1, cityIndex: -1, areaIndex: -1 };
		};

		// 省
		let provinceIndex = findProvinceIndex();
		let cityIndex = -1;
		let areaIndex = -1;

		if (provinceIndex === -1) {
			// 如果省份没有找到，通过市倒推省份
			const cityResult = findProvinceByCity();
			provinceIndex = cityResult.provinceIndex;
			cityIndex = cityResult.cityIndex;
		}

		if (provinceIndex === -1) {
			// 如果市没有找到，通过区倒推市和省份
			const areaResult = findProvinceByArea();
			provinceIndex = areaResult.provinceIndex;
			cityIndex = areaResult.cityIndex;
			areaIndex = areaResult.areaIndex;
		}

		if (provinceIndex === -1) return { code: -1, msg: "省份没有找到，请输入正确的地址" };

		Object.assign(province, provinces[provinceIndex]);

		const cityList = citys[provinceIndex];
		// 市
		if (cityIndex === -1) {
			cityIndex = findCityIndex(cityList);
			if (cityIndex === -1) return { code: -1, msg: "地级市没有找到，请输入正确的地址" };
		}

		Object.assign(city, cityList[cityIndex]);

		// 区
		const areaList = areas[provinceIndex][cityIndex];
		if (areaIndex === -1) {
			areaIndex = findAreaIndex(areaList);
			if (areaIndex === -1) return { code: -1, msg: "县级市没有找到，请输入正确的地址" };
		}

		Object.assign(area, areaList[areaIndex]);

		const formatted_address = `${province.name}${city.name}${area.name}${address}`;
		const provinceName = province.name.substring(0, 2); // 省份名称前两个字
		const not_address_text = (addressText.substring(0, addressText.indexOf(provinceName)) + addressText.substring(addressText.indexOf(address) + address.length)).trim();
		return {
			code: 0,
			msg: "ok",
			data: {
				province,
				city,
				area,
				address,
				formatted_address
			},
			not_address_text
		};
	}

	// 智能解析姓名和手机号
	nameMobileDiscern(text) {
		if (!text) return { name: "", mobile: "" };

		let name = "";
		let mobile = "";

		// 手机号正则匹配（支持多种格式）
		const mobilePatterns = [
			new RegExp('1\\d{2}([\\s-]?\\d{4}){2}', 'g')
		];
		let mobileMatchText;
		// 查找手机号
		for (const pattern of mobilePatterns) {
			const matches = text.match(pattern);
			if (matches && matches.length > 0) {
				// 取第一个匹配的手机号，去除格式符号
				mobile = matches[0].replace(new RegExp("[\\s\\-\\.]", "g"), "");
				mobileMatchText = matches[0];
				break;
			}
		}

		// 从文本中移除手机号，便于后续识别姓名
		let cleanText = text;
		if (mobile) {
			cleanText = cleanText.replace(mobileMatchText, "");
		}

		// 处理常见的标签前缀（扩展更多模式）
		const labelPatterns = [
			new RegExp("^姓名[：:]\\s*"),
			new RegExp("^收货人[：:]\\s*"),
			new RegExp("^收件人[：:]\\s*"),
			new RegExp("^联系人[：:]\\s*"),
			new RegExp("^name[：:]\\s*", "i"),
			new RegExp("^收[：:]\\s*"),
			new RegExp("^人[：:]\\s*"),
			new RegExp("^电话[：:]\\s*"),
			new RegExp("^手机[：:]\\s*"),
			new RegExp("^tel[：:]\\s*", "i"),
			new RegExp("^phone[：:]\\s*", "i"),
			new RegExp("^mobile[：:]\\s*", "i"),
			new RegExp("^地址[：:]\\s*"),
			new RegExp("^address[：:]\\s*", "i"),
			new RegExp("^收货地址[：:]\\s*"),
			new RegExp("^收件地址[：:]\\s*"),
		];

		// 移除标签前缀
		for (const pattern of labelPatterns) {
			cleanText = cleanText.replace(pattern, "");
		}

		// 清理多余的空格和标点
		cleanText = cleanText.replace(new RegExp("\\s+", "g"), " ").trim();

		// 姓名识别规则（按优先级排序，更具体的模式在前）
		const namePatterns = [
			// 英文+称谓（如：vk先生、John先生、Mary女士等）- 最具体，优先匹配
			new RegExp("[A-Za-z]+[\\u4e00-\\u9fa5]{1,3}", "g"),
			// 中文姓名（2-6个汉字，支持复姓和少数民族姓名）
			new RegExp("[\\u4e00-\\u9fa5]{2,6}", "g"),
			// 英文姓名（支持多种格式：全名、简称、首字母等）
			new RegExp("[A-Za-z]+(\\s+[A-Za-z]+)*", "g"),
		];

		// 查找姓名
		for (const pattern of namePatterns) {
			const matches = cleanText.match(pattern);
			if (matches && matches.length > 0) {
				// 过滤掉一些明显不是姓名的词
				const validNames = matches.filter(match => {
					// 过滤掉常见的地址词汇
					const addressWords = ['省', '市', '区', '县', '镇', '村', '路', '街', '号', '楼', '室', '单元', '栋', '层', '小区', '大厦', '广场', '花园'];
					const isAddressWord = addressWords.some(word => match.includes(word));

					// 过滤掉常见的标签词汇
					const labelWords = ['姓名', '收货人', '收件人', '联系人', '收', '人', '电话', '手机', '地址', '收货', '收件', '联系'];
					const isLabelWord = labelWords.some(word => match.includes(word));

					// 过滤掉纯数字
					const isNumber = new RegExp("^\\d+$").test(match);

					// 过滤掉太短的词（但允许英文简称，如vk、ab等）
					const isTooShort = match.length < 2;

					// 过滤掉太长的词（中文超过8个字，英文超过25个字符可能是地址或其他信息）
					// 增加长度限制以支持"英文+称谓"的格式
					const isChinese = new RegExp("[\\u4e00-\\u9fa5]").test(match);
					const isEnglishWithTitle = new RegExp("^[A-Za-z]+[\\u4e00-\\u9fa5]{1,3}$").test(match);
					const isTooLong = isEnglishWithTitle ? false : (isChinese ? match.length > 8 : match.length > 25);

					// 过滤掉包含特殊字符的词（但允许英文中的空格和中文）
					const hasSpecialChars = isChinese ?
						new RegExp("[^\\u4e00-\\u9fa5a-zA-Z\\s]").test(match) :
						new RegExp("[^a-zA-Z\\s]").test(match);

					return !isAddressWord && !isLabelWord && !isNumber && !isTooShort && !isTooLong && !hasSpecialChars;
				});

				if (validNames.length > 0) {
					// 优先选择较短的姓名（通常姓名不会太长）
					name = validNames.sort((a, b) => a.length - b.length)[0];
					break;
				}
			}
		}

		// 如果没有通过正则找到姓名，尝试其他方法
		if (!name) {
			// 尝试从剩余文本中提取可能的姓名
			const remainingText = cleanText.trim();
			if (remainingText) {
				// 按空格或标点符号分割
				const parts = remainingText.split(new RegExp("[\\s，,。.！!？?；;：:]"));
				for (const part of parts) {
					const trimmedPart = part.trim();
					// 检查是否是中文姓名（2-6个汉字）、英文姓名（2-20个字母）或英文+称谓
					const isChineseName = new RegExp("^[\\u4e00-\\u9fa5]{2,6}$").test(trimmedPart);
					const isEnglishName = new RegExp("^[a-zA-Z]{2,20}$").test(trimmedPart);
					const isEnglishWithTitle = new RegExp("^[a-zA-Z]+[\\u4e00-\\u9fa5]{1,3}$").test(trimmedPart);

					if (isChineseName || isEnglishName || isEnglishWithTitle) {
						// 再次过滤地址词汇和标签词汇
						const addressWords = ['省', '市', '区', '县', '镇', '村', '路', '街', '号', '楼', '室', '单元', '栋', '层', '小区', '大厦', '广场', '花园'];
						const labelWords = ['姓名', '收货人', '收件人', '联系人', '收', '人', '电话', '手机', '地址', '收货', '收件', '联系'];
						const isAddressWord = addressWords.some(word => trimmedPart.includes(word));
						const isLabelWord = labelWords.some(word => trimmedPart.includes(word));
						if (!isAddressWord && !isLabelWord) {
							name = trimmedPart;
							break;
						}
					}
				}
			}
		}

		// 最终清理：去除姓名中的多余空格
		if (name) {
			name = name.replace(new RegExp("\\s+", "g"), "").trim();
		}

		return { name, mobile };
	}

}


export default AddressDiscern;