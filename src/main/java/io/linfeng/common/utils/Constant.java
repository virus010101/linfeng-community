
package io.linfeng.common.utils;

/**
 * 常量
 *
 */
public class Constant {
	/** 超级管理员ID */
	public static final int SUPER_ADMIN = 1;
    /**
     * 当前页码
     */
    public static final String PAGE = "page";
    /**
     * 每页显示记录数
     */
    public static final String LIMIT = "limit";
    /**
     * 排序字段
     */
    public static final String ORDER_FIELD = "sidx";
    /**
     * 排序方式
     */
    public static final String ORDER = "order";
    /**
     *  升序
     */
    public static final String ASC = "asc";


    public static final String CLASSID = "classId";


    public static final String ORDER_DESC_ID = "id desc";


    public static final String ORDER_DESC_READCOUNT = "read_count desc";

    public static final Integer NOT_READ = 0;
    public static final Integer HAS_READ = 1;

    /**
     * 用户是否禁用
     */
    public static final Integer USER_NORMAL = 0;
    public static final Integer USER_BANNER = 1;

    /**
     * 圈子是否禁用
     */
    public static final Integer TOPIC_NORMAL = 0;
    public static final Integer TOPIC_BANNER = 1;
    /**
     * 敏感词是否开启审核
     */
    public static final Integer SENSITIVE_CLOSE = 0;
    public static final Integer SENSITIVE_OPEN = 1;
    /**
     * 敏感词处理措施 1-禁止发布 2-需审核
     */
    public static final Integer DEAL_BANNER = 1;
    public static final Integer DEAL_EXAMINE = 2;

    /**
     * 1为点赞，2为评论  3为收藏 4为关注  5为推送通知 6私聊
     */
    public static final Integer STAR = 1;
    public static final Integer COMMENT = 2;
    public static final Integer COLLECT = 3;
    public static final Integer WATCH = 4;
    public static final Integer PUSHARTICLE = 5;
    public static final Integer CHAT = 6;

    public static final String TITLE_WATCH = "关注通知";
    public static final String TITLE_COMMENT = "评论通知";
    public static final String TITLE_STAR = "点赞通知";
    public static final String TITLE_COLLECT = "点赞收藏通知";
    public static final String TITLE_CHAT = "私聊通知";
    public static final String TITLE_VIOLATION = "违规通知";


    public static final String CONTENT_WATCH = "用户【{}】关注了你";
    public static final String CONTENT_STAR = "用户【{}】点赞了你的评论:{}";
    public static final String CONTENT_COMMENT = "用户【{}】评论了你的动态[{}]:{}";
    public static final String CONTENT_COMMENT_REPLY = "用户【{}】回复了你在动态[{}]下的评论:{}";
    public static final String CONTENT_POST_STAR = "用户【{}】点赞收藏了你的动态:{}";
    public static final String ADMIN_POST_DOWN = "你的动态【{}】由于违反社区规定已被删除";


    /**
     * 手机验证码长度
     */
    public static final Integer SMS_SIZE = 6;
    public static final String DEAULT_HEAD = "http://pic.linfeng.tech/test/20220126/4515fc2cbed74d0b9163d35a12bd4c3b.png";

    /**
	 * 菜单类型
	 */
    public enum MenuType {
        /**
         * 目录
         */
    	CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
    
    /**
     * 定时任务状态
     * 
     * @author chenshun
     * @email sunlightcs@gmail.com
     * @date 2016年12月3日 上午12:07:22
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
    	NORMAL(0),
        /**
         * 暂停
         */
    	PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }
        
        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    public enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

}
