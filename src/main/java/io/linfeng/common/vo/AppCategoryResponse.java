package io.linfeng.common.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @author linfeng
 */
@Data
public class AppCategoryResponse implements Serializable {
    private static final long serialVersionUID = 1L;


    /**
     * 分类id
     */
    private Integer cateId;
    /**
     * 分类名称
     */
    private String cateName;
    /**
     * 是否推荐
     */
    private Integer isTop;
    /**
     * 图片
     */
    private String coverImage;
}
