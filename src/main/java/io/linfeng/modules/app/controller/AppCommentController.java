package io.linfeng.modules.app.controller;


import io.linfeng.common.utils.AppPageUtils;
import io.linfeng.common.utils.R;
import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.admin.service.CommentService;
import io.linfeng.modules.app.annotation.Login;
import io.linfeng.modules.app.annotation.LoginUser;
import io.linfeng.modules.app.form.AddThumbsForm;
import io.linfeng.modules.app.service.CommentThumbsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author linfeng
 * @date 2022/7/27 15:35
 */
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
    public R list(@RequestParam("postId")Integer postId,@RequestParam("page")Integer page){
        AppPageUtils pages =commentService.queryCommentPage(postId,page);

        return R.ok().put("result", pages);
    }


    /**
     * 评论区的点赞
     */
    @Login
    @PostMapping("/thumbs")
    public R thumbs(@RequestBody AddThumbsForm request, @LoginUser AppUserEntity user){

        commentThumbsService.addThumbs(request,user);
        return R.ok();
    }

    /**
     * 取消评论区的点赞
     */
    @Login
    @PostMapping("/cancelThumbs")
    public R cancelThumbs(@RequestBody AddThumbsForm request,@LoginUser AppUserEntity user){

        commentThumbsService.cancelThumbs(request,user);
        return R.ok();
    }
}
