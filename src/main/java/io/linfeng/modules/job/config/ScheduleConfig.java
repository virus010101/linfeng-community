/**
 * -----------------------------------
 * 林风社交论坛开源版本请务必保留此注释头信息
 * 开源地址: https://gitee.com/virus010101/linfeng-community
 * 商业版详情查看: https://www.linfengtech.cn
 * 商业版购买联系技术客服	 QQ:  3582996245
 * 可正常分享和学习源码，不得转卖或非法牟利！
 * Copyright (c) 2021-2025 linfeng all rights reserved.
 * 版权所有 ，侵权必究！
 * -----------------------------------
 */
package io.linfeng.modules.job.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * 定时任务配置
 *
 */
@Configuration
public class ScheduleConfig {

    @Bean
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource) {
        SchedulerFactoryBean factory = new SchedulerFactoryBean();
        factory.setDataSource(dataSource);

        //quartz参数
        Properties prop = new Properties();
        prop.put("org.quartz.scheduler.instanceName", "ProjectScheduler");
        prop.put("org.quartz.scheduler.instanceId", "AUTO");
        //线程池配置
        prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
        prop.put("org.quartz.threadPool.threadCount", "20");
        prop.put("org.quartz.threadPool.threadPriority", "5");
        //JobStore配置
        prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
        //集群配置
        prop.put("org.quartz.jobStore.isClustered", "true");
        prop.put("org.quartz.jobStore.clusterCheckinInterval", "15000");
        prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");

        prop.put("org.quartz.jobStore.misfireThreshold", "12000");
        prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
        prop.put("org.quartz.jobStore.selectWithLockSQL", "SELECT * FROM {0}LOCKS UPDLOCK WHERE LOCK_NAME = ?");

        //PostgreSQL数据库，需要打开此注释
        //prop.put("org.quartz.jobStore.driverDelegateClass", "org.quartz.impl.jdbcjobstore.PostgreSQLDelegate");

        factory.setQuartzProperties(prop);

        factory.setSchedulerName("ProjectScheduler");
        //延时启动
        factory.setStartupDelay(30);
        factory.setApplicationContextSchedulerContextKey("applicationContextKey");
        //可选，QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
        factory.setOverwriteExistingJobs(true);
        //设置自动启动，默认为true
        factory.setAutoStartup(true);

        return factory;
    }
}
