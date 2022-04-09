package io.linfeng.common.response;

import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class AppUserResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 用户id
	 */
	private Integer uid;
	/**
	 * 手机号
	 */
	private String mobile;
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
	 * 省份
	 */
	private String province;
	/**
	 * 城市
	 */
	private String city;

	/**
	 * 个性签名
	 */
	private String intro;
	/**
	 * 积分
	 */
	private Integer integral;
	/**
	 * 最后登录ip
	 */
	private String lastLoginIp;
	/**
	 * 用户标签
	 */
	private List<String> tagStr;
	/**
	 * 0为普通用户  1官方账号 2马甲虚拟用户
	 */
	private Integer type;
	/**
	 * 关注
	 */
	private Integer follow;
	/**
	 * 粉丝
	 */
	private Integer fans;
	/**
	 * 动态数
	 */
	private Integer postNum;



}
