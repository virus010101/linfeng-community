package io.linfeng.modules.admin.service.impl;

import io.linfeng.common.exception.LinfengException;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.Query;

import io.linfeng.modules.admin.dao.CategoryDao;
import io.linfeng.modules.admin.entity.CategoryEntity;
import io.linfeng.modules.admin.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveCategory(CategoryEntity category) {
        Integer count = this.lambdaQuery().eq(CategoryEntity::getCateName, category.getCateName()).count();
        if(count!=0){
            throw new LinfengException("分类名不能重复");
        }
        this.save(category);
    }

}