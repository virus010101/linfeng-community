/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 可正常分享和学习源码，不得用于非法牟利！
 * 商业版购买联系技术客服 QQ: 3582996245
 * Copyright (c) 2021-2025 linfeng all rights reserved.
 * 演示站点:https://www.linfengtech.cn
 * 版权所有，侵权必究！
 * -----------------------------------
 */
package io.linfeng.modules.app.controller;

import io.linfeng.common.exception.LinfengException;
import io.linfeng.common.utils.FileCheckUtil;
import io.linfeng.common.utils.R;
import io.linfeng.modules.oss.cloud.OSSFactory;
import io.linfeng.modules.oss.entity.SysOssEntity;
import io.linfeng.modules.oss.service.SysOssService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * APP文件上传
 *
 */
@RestController
@RequestMapping("app/common")
@Api(tags = "用户端——App文件上传")
public class AppOssController {

	/**
	 * 允许的图片类型
	 */
	private static final Set<String> ALLOWED_IMAGE_TYPES = new HashSet<>(Arrays.asList(
			".jpg", ".jpeg", ".png", ".gif"
	));

	/**
	 * 允许的视频类型
	 */
	private static final Set<String> ALLOWED_VIDEO_TYPES = new HashSet<>(Arrays.asList(
			".mp4"
	));

	@Value("${oss.max-size}")
	private Long maxSize;

	@Autowired
	private SysOssService sysOssService;


	@ApiOperation("APP端文件上传")
	@PostMapping("/upload")
	public R upload(@RequestParam("Image") MultipartFile file) throws Exception {
		if (file.isEmpty()) {
			throw new LinfengException("上传文件不能为空");
		}

		// 校验文件类型
		String originalFilename = file.getOriginalFilename();
		if (originalFilename == null || originalFilename.lastIndexOf(".") == -1) {
			throw new LinfengException("上传文件格式不正确");
		}
		String suffix = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();

		boolean isImage = ALLOWED_IMAGE_TYPES.contains(suffix);
		boolean isVideo = ALLOWED_VIDEO_TYPES.contains(suffix);

		if (!isImage && !isVideo) {
			throw new LinfengException("只支持图片和视频格式上传");
		}

		FileCheckUtil.checkSize(maxSize, file.getSize());
		//上传文件
		String url = OSSFactory.build().uploadSuffix(file.getBytes(), suffix);

		//保存文件信息
		SysOssEntity ossEntity = new SysOssEntity();
		ossEntity.setUrl(url);
		ossEntity.setCreateDate(new Date());
		sysOssService.save(ossEntity);

		return R.ok().put("result", url);
	}



}
