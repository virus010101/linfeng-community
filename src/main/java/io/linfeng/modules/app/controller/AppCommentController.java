/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 可正常分享和学习源码，不得用于非法牟利！
 * 商业版购买联系技术客服 QQ: 3582996245
 * Copyright (c) 2021-2023 linfeng all rights reserved.
 * 演示站点:https://www.linfeng.tech
 * 版权所有，侵权必究！
 * -----------------------------------
 */
package io.linfeng.modules.app.controller;


import io.linfeng.common.utils.AppPageUtils;
import io.linfeng.common.utils.R;
import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.admin.service.CommentService;
import io.linfeng.modules.app.annotation.Login;
import io.linfeng.modules.app.annotation.LoginUser;
import io.linfeng.modules.app.param.AddThumbsForm;
import io.linfeng.modules.app.service.CommentThumbsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 * @author linfeng
 * @date 2022/7/27 15:35
 *
 */
@Api(tags = "用户端——评论")
@RestController
@RequestMapping("app/comment")
public class AppCommentController {

    @Autowired
    private CommentService commentService;
    @Autowired
    private CommentThumbsService commentThumbsService;

    /**
     * 评论列表
     */
    @GetMapping("/list")
    @ApiOperation("评论列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "postId", value = "帖子id", paramType = "query",dataType = "Integer", required = true),
            @ApiImplicitParam(name = "page", value = "分页页码",paramType = "query", dataType = "Integer", required = true)
    })
    public R list(@RequestParam("postId")Integer postId,
                  @RequestParam("page")Integer page){
        AppPageUtils pages =commentService.queryCommentPage(postId,page);

        return R.ok().put("result", pages);
    }


    /**
     * 评论区的点赞
     */
    @Login
    @PostMapping("/thumbs")
    @ApiOperation("评论区的点赞")
    public R thumbs(@RequestBody AddThumbsForm request,
                    @ApiIgnore @LoginUser AppUserEntity user){

        commentThumbsService.addThumbs(request,user);
        return R.ok();
    }

    /**
     * 取消评论区的点赞
     */
    @Login
    @PostMapping("/cancelThumbs")
    @ApiOperation("取消评论区的点赞")
    public R cancelThumbs(@RequestBody AddThumbsForm request,
                          @ApiIgnore @LoginUser AppUserEntity user){

        commentThumbsService.cancelThumbs(request,user);
        return R.ok();
    }
}
