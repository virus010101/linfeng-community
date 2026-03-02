package io.linfeng.common.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author linfeng
 * @date 2026/3/2 10:22
 */
@Data
public class AppLinkResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Integer id;
    /**
     * 标题
     */
    private String title;
    /**
     * 路径
     */
    private String url;
    /**
     * 图片
     */
    private String img;
    /**
     * 3圈子页轮播图
     */
    private Integer type;
    /**
     * 创建时间
     */
    private Date createTime;
}
