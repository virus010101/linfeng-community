/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 商业版详情查看: https://www.linfengtech.cn
 * 商业版购买联系技术客服		QQ:  3582996245
 * 可正常分享和学习源码，不得转卖或非法牟利！
 * Copyright (c) 2021-2025 linfeng all rights reserved.
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 用户端用户信息响应体
 */
@Data
public class AppUserInfoShortResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	private Integer uid;

	/**
	 * 用户名
	 */
	private String username;

	/**
	 * 头像
	 */
	private String avatar;

	/**
	 * 性别(0未知，1男，2女)
	 */
	private Integer gender;

	/**
	 * 城市
	 */
	private String city;

	/**
	 * 个性签名
	 */
	private String intro;




}
