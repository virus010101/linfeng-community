package io.linfeng.modules.app.form;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


@Data
public class AddPostForm implements Serializable {

    private static final long serialVersionUID = 1L;


    /**
     * 圈子id
     */
    @NotNull(message = "无参数圈子id")
    private Integer topicId;
    /**
     * 话题id
     */
    private Integer discussId;

    /**
     * 标题
     */
    @Length(max = 20, message = "标题不能超过20个字符")
    @NotBlank(message = "参数有误")
    private String title;
    /**
     * 内容
     */
    @Length(max = 400, message = "内容不能超过400个字符")
    @NotBlank(message = "参数有误")
    private String content;
    /**
     * 文件
     */
    private List<String> media;

    /**
     * 帖子类型：1 图文 ，2视频 ，3文章，4投票
     */
    @NotNull(message = "无参数type")
    private Integer type;
    /**
     * 地址名称
     */
    private String address;
    /**
     * 经度
     */
    private Double longitude;
    /**
     * 纬度
     */
    private Double latitude;


}
