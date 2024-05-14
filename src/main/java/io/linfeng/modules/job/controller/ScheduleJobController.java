/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 商业版详情查看: https://www.linfeng.tech
 * 商业版购买联系技术客服	 QQ:  3582996245
 * 可正常分享和学习源码，不得转卖或非法牟利！
 * Copyright (c) 2021-2023 linfeng all rights reserved.
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.modules.job.controller;

import io.linfeng.common.annotation.SysLog;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.R;
import io.linfeng.common.validator.ValidatorUtils;
import io.linfeng.modules.job.entity.ScheduleJobEntity;
import io.linfeng.modules.job.service.ScheduleJobService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 定时任务
 *
 */
@Api(tags = "定时任务")
@RestController
@RequestMapping("/sys/schedule")
public class ScheduleJobController {
	@Autowired
	private ScheduleJobService scheduleJobService;


	@GetMapping("/list")
	@RequiresPermissions("sys:schedule:list")
	@ApiOperation("定时任务列表")
	public R list(@RequestParam Map<String, Object> params){
		PageUtils page = scheduleJobService.queryPage(params);

		return R.ok().put("page", page);
	}


	@GetMapping("/info/{jobId}")
	@RequiresPermissions("sys:schedule:info")
	@ApiOperation("定时任务详情")
	public R info(@PathVariable("jobId") Long jobId){
		ScheduleJobEntity schedule = scheduleJobService.getById(jobId);

		return R.ok().put("schedule", schedule);
	}


	@SysLog("保存定时任务")
	@PostMapping("/save")
	@RequiresPermissions("sys:schedule:save")
	@ApiOperation("保存定时任务")
	public R save(@RequestBody ScheduleJobEntity scheduleJob){
		ValidatorUtils.validateEntity(scheduleJob);

		scheduleJobService.saveJob(scheduleJob);

		return R.ok();
	}


	@SysLog("修改定时任务")
	@PostMapping("/update")
	@RequiresPermissions("sys:schedule:update")
	@ApiOperation("修改定时任务")
	public R update(@RequestBody ScheduleJobEntity scheduleJob){
		ValidatorUtils.validateEntity(scheduleJob);

		scheduleJobService.update(scheduleJob);

		return R.ok();
	}


	@SysLog("删除定时任务")
	@PostMapping("/delete")
	@RequiresPermissions("sys:schedule:delete")
	@ApiOperation("删除定时任务")
	public R delete(@RequestBody Long[] jobIds){
		scheduleJobService.deleteBatch(jobIds);

		return R.ok();
	}


	@SysLog("立即执行任务")
	@PostMapping("/run")
	@RequiresPermissions("sys:schedule:run")
	@ApiOperation("删除定时任务")
	public R run(@RequestBody Long[] jobIds){
		scheduleJobService.run(jobIds);

		return R.ok();
	}



	@SysLog("暂停定时任务")
	@PostMapping("/pause")
	@RequiresPermissions("sys:schedule:pause")
	@ApiOperation("暂停定时任务")
	public R pause(@RequestBody Long[] jobIds){
		scheduleJobService.pause(jobIds);

		return R.ok();
	}



	@SysLog("恢复定时任务")
	@PostMapping("/resume")
	@RequiresPermissions("sys:schedule:resume")
	@ApiOperation("恢复定时任务")
	public R resume(@RequestBody Long[] jobIds){
		scheduleJobService.resume(jobIds);

		return R.ok();
	}

}