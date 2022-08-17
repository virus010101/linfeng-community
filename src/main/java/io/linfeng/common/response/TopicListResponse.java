/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 商业版演示站点: https://www.linfeng.tech
 * 商业版购买联系技术客服	 QQ:  3582996245
 * 可正常分享和学习源码，不得转卖或非法牟利！
 * Copyright (c) 2021-2023 linfeng all rights reserved.
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.common.response;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.linfeng.modules.admin.entity.AppUserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;

/**
 * @author linfeng
 * @date 2022/1/21 17:28
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="TopicListResponse对象", description="文章响应对象")
public class TopicListResponse implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 圈子id
     */
//    @ApiModelProperty(value = "圈子id")
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
}
