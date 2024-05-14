
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


    /**
     *  终端
     */
    public static final String H5 = "H5";
    public static final String WXAPP = "WXAPP";


    /**
     * 用户是否禁用
     */
    public static final Integer USER_NORMAL = 0;
    public static final Integer USER_BANNER = 1;



    /**
     * 评论状态
     * 0 下架  1正常
     */
    public static final Integer COMMENT_DOWN = 0;
    public static final Integer COMMENT_NORMAL = 1;


    /**
     * 手机验证码长度
     */
    public static final Integer SMS_SIZE = 6;

    /**
     * 手机验证码前缀
     */
    public static final String SMS_PREFIX = "code_";

    public static final String DEFAULT_TAG = "新人";

    /**
     * 注册账户默认分配头像
     */
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
