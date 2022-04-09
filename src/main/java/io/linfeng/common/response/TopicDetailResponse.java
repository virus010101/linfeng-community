package io.linfeng.common.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.linfeng.modules.admin.entity.AppUserEntity;
import io.linfeng.modules.admin.entity.DiscussEntity;
import io.linfeng.modules.app.entity.TopicTopEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author linfeng
 * @date 2022/1/23 17:28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TopicDetailResponse", description="圈子详情响应对象")
public class TopicDetailResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 圈子id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;
    /**
     * 创建用户id
     */
    private Integer uid;
    /**
     * 分类id
     */
    private Integer cateId;
    /**
     * 圈子名称
     */
    private String topicName;
    /**
     * 描述
     */
    private String description;
    /**
     * logo
     */
    private String coverImage;
    /**
     * 背景图
     */
    private String bgImage;
    /**
     * 推荐类型：0 不推荐， 1首页推荐， 2圈子页推荐
     */
    private Integer topType;
    /**
     * 圈子状态：0 正常 ，1禁用
     */
    private Integer status;
    /**
     * 是否首页推荐圈子内容
     */
    private Integer indexRecommend;
    /**
     * 加入人数
     */
    private Integer userNum;
    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 用户详情
     */
    private AppUserEntity userInfo;


    /**
     * 圈子的内容数量
     */
    private Integer postCount;
    /**
     * 已加入圈子的人数
     */
    private Integer userCount;

    /**
     * $topicPostCount = Db::name("post")->where("topic_id",$topicId)->count();
     * $topicUserCount = Db::name("user_topic")->where("topic_id",$topicId)->count();
     */

    /**
     * 圈子的管理员
     */
    private List<AppUserEntity> adminList;

    /**
     * 是否为圈主或管理员
     */
    private Boolean isAdmin;

    /**
     * 圈子话题
     */
    private List<DiscussEntity> discussList;
    /**
     * 是否加入
     */
    private Boolean isJoin;
    /**
     * 置顶内容
     */
    private List<TopicTopEntity> topPost;

}
