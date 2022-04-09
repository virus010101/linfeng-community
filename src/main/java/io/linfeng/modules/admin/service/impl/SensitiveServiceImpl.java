package io.linfeng.modules.admin.service.impl;

import io.linfeng.common.exception.LinfengException;
import io.linfeng.common.utils.Constant;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.linfeng.common.utils.PageUtils;
import io.linfeng.common.utils.Query;

import io.linfeng.modules.admin.dao.SensitiveDao;
import io.linfeng.modules.admin.entity.SensitiveEntity;
import io.linfeng.modules.admin.service.SensitiveService;


@Service("sensitiveService")
public class SensitiveServiceImpl extends ServiceImpl<SensitiveDao, SensitiveEntity> implements SensitiveService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SensitiveEntity> page = this.page(
                new Query<SensitiveEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public Boolean checkContent(String content) {
        SensitiveEntity sensitive = this.getById(1);
        if (sensitive == null || sensitive.getSensitiveWord().isEmpty()) {
            return false;
        }
        if (sensitive.getState().equals(Constant.SENSITIVE_CLOSE)) {
            return false;
        }
        //校验是否存在敏感词
        String[] split = sensitive.getSensitiveWord().split(",");
        for (String word : split) {
            if (content.contains(word)) {
                //如果存在敏感词
                if (Constant.DEAL_BANNER.equals(sensitive.getHandleMeasures())) {
                    throw new LinfengException("内容违规禁止发布");
                } else {
                    //设置评论敏感词状态为需审核
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void checkPostContent(String content) {
        SensitiveEntity sensitive = this.getById(1);
        if (sensitive == null || sensitive.getSensitiveWord().isEmpty()) {
            return;
        }
        if (sensitive.getState().equals(Constant.SENSITIVE_CLOSE)) {
            return;
        }
        //校验是否存在敏感词
        String[] split = sensitive.getSensitiveWord().split(",");
        for (String word : split) {
            if (content.contains(word)) {
                //如果存在敏感词 直接打回
                throw new LinfengException("内容含敏感词禁止发布");
            }
        }
    }

}