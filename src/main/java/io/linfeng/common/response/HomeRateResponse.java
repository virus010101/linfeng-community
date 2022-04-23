package io.linfeng.common.response;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * 面板统计数据对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ApiModel(value="HomeRateResponse对象", description="面板统计数据对象")
public class HomeRateResponse implements Serializable {

    private static final long serialVersionUID=1L;

    @ApiModelProperty(value = "总用户数")
    private Object totalUser;

    @ApiModelProperty(value = "总帖子数")
    private Object totalPost;

    @ApiModelProperty(value = "总待审核帖子数")
    private Object totalPostOfReview;

    @ApiModelProperty(value = "今日新增用户")
    private Object newUserNum;

    @ApiModelProperty(value = "昨日新增用户")
    private Object yesterdayNewUserNum;


}
