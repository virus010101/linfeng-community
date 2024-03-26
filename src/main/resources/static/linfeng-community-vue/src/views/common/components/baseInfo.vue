<template>
  <div class="divBox" style="padding-bottom: 0">
    <el-row :gutter="20" class="baseInfo">
      <el-col v-bind="grid" class="ivu-mb">
        <el-card :bordered="false" dis-hover :padding="12">
          <div class="acea-row row-between-wrapper">
            <div class="acea-row align-center">
              <span class="main_tit">评论数量</span>
            </div>
            <el-tag type="primary">近一天</el-tag>
          </div>
          <div class="content" v-if="viewData">
            <span class="content-number spBlock my15">{{ viewData.yesterdayCommentCount }}</span>
            <el-divider></el-divider>
            <div class="acea-row row-between-wrapper">
              <span class="content-time">总评论</span>
              <span class="content-time">{{ viewData.commentCount }}条</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col v-bind="grid" class="ivu-mb">
        <el-card :bordered="false" dis-hover :padding="12">
          <div class="acea-row row-between-wrapper">
            <div class="acea-row align-center">
              <span class="main_tit">用户量</span>
            </div>
            <el-tag type="primary">总量</el-tag>
          </div>
          <div class="content" v-if="viewData">
            <span class="content-number spBlock my15">{{
              viewData.totalUser
            }}</span>
            <el-divider></el-divider>
            <div class="acea-row row-between-wrapper">
              <span class="content-time">普通游客</span>
              <span class="content-time">{{ viewData.totalUser }}</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col v-bind="grid" class="ivu-mb">
        <el-card :bordered="false" dis-hover :padding="12">
          <div class="acea-row row-between-wrapper">
            <div class="acea-row align-center">
              <span class="main_tit">帖子数</span>
            </div>
            <el-tag type="primary">全部</el-tag>
          </div>
          <div class="content" v-if="viewData">
            <span class="content-number spBlock my15">{{
              viewData.totalPost
            }}</span>
            <el-divider></el-divider>
            <div class="acea-row row-between-wrapper">
              <span class="content-time">待审核</span>
              <span class="content-time">{{ viewData.totalPostOfReview }}条</span>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col v-bind="grid" class="ivu-mb">
        <el-card :bordered="false" dis-hover :padding="12">
          <div class="acea-row row-between-wrapper">
            <div class="acea-row align-center">
              <span class="main_tit">新增用户</span>
            </div>
            <el-tag type="primary">今日</el-tag>
          </div>
          <div class="content" v-if="viewData">
            <span class="content-number spBlock my15">{{
              viewData.newUserNum
            }}</span>
            <el-divider></el-divider>
            <div class="acea-row row-between-wrapper">
              <span class="content-time">昨日数据</span>
              <span class="content-time">{{ viewData.yesterdayNewUserNum }} 人</span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
        <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>林风社交论坛系统开源版</h2>
        <p>
          林风社交论坛uniapp小程序/H5/APP版本是前后端分离的社交论坛问答发帖/BBS/SNS项目。 
          项目分为Uniapp用户端（兼容H5、微信小程序）和Vue2后台管理端 包括完整的权限处理。
        </p>
        <h2>林风社交论坛系统标准商业版</h2>
        <p>
          林风社交论坛标准商业版包括（兼容H5、微信小程序、APP端、微信公众号端）和Vue3后台管理端。
          包括：图文帖，长文贴，短视频，私密圈子，公开圈子，私聊，微信支付（小程序/H5/app/公众号），付费贴，积分签到，钱包充值，积分余额兑换，话题标签，抽奖大转盘，手机号邮箱登录，虚拟用户发帖，举报，第三方广告，会员模块，即时通讯IM ，好友模块，投票，打赏，用户经验等级等丰富功能
        </p>
        <p>
          <b>当前版本:</b> <span>开源版</span>
        </p>
        <p>
          <b>项目官网:</b> <a href="https://net.linfeng.tech" target="_blank">https://net.linfeng.tech</a><br>
          <b>公司官网:</b> <a href="https://www.linfengtech.cn" target="_blank">https://www.linfengtech.cn</a><br>
          <b>微信公众号:</b> <span>林风科技</span>
        </p>
        <p>
          <b>标准版后台演示:</b> <a href="https://dev.linfeng.tech" target="_blank">https://dev.linfeng.tech</a>
        </p>
        
        <p>
          <el-button
            type="primary"
            size="mini"
            icon="el-icon-cloudy"
            plain
            @click="goTo('https://gitee.com/virus010101/linfeng-community')"
            >开源下载</el-button
          >
          <el-button
            size="mini"
            icon="el-icon-s-home"
            plain
            @click="goTo('https://net.linfeng.tech')"
            >访问项目官网</el-button
          >
          <el-button
            type="danger"
            size="mini"
            icon="el-icon-cloudy"
            plain
            @click="goTo('https://net.linfeng.tech/version/version.html')"
            >功能清单</el-button
          >
          <el-button
            type="warning"
            size="mini"
            icon="el-icon-cloudy"
            plain
            @click="goTo('https://net.linfeng.tech/commercial.html')"
            >商业版咨询</el-button
          >
        </p>
      </el-col>

      <el-col :sm="24" :lg="12" style="padding-left: 50px">
        <el-row>
          <el-col :span="12">
            <h2>技术栈</h2>
          </el-col>
        </el-row>
        <el-row>
          <el-col :span="6">
            <h4>后端技术</h4>
            <ul>
              <li>SpringBoot</li>
              <li>Shiro</li>
              <li>JWT</li>
              <li>MyBatisPlus</li>
              <li>Quartz</li>
              <li>...</li>
            </ul>
          </el-col>
          <el-col :span="6">
            <h4>前端技术</h4>
            <ul>
              <li>Uniapp</li>
              <li>Vue</li>
              <li>Axios</li>
              <li>Sass</li>
              <li>...</li>
            </ul>
          </el-col>
        </el-row>
      </el-col>
    </el-row>
    <el-divider />
  </div>
</template>
<script>
export default {
  data() {
    return {
      grid: { xl: 6, lg: 6, md: 12, sm: 12, xs: 24 },
      viewData: {},
    };
  },
  methods: {
    goTo(href) {
      window.open(href, "_blank");
    },
    statisticsOrder() {
      this.dataListLoading = true;
      this.$http({
        url: this.$http.adornUrl("/admin/statistics/home"),
        method: "get",
      }).then(({ data }) => {
        if (data && data.code === 0) {
          // console.log(data.result)
          this.viewData = data.result;
        }
        this.dataListLoading = false;
      });
    },
  },
  mounted() {
    this.statisticsOrder();
  },
};
</script>
<style scoped lang="scss">
.acea-row{
  display: flex;
  flex-wrap: wrap;
}
.row-between-wrapper{
  justify-content: space-between;
}
.ivu-mb {
  margin-bottom: 20px;
}

.up,
.el-icon-caret-top {
  color: #f5222d;
  font-size: 12px;
  opacity: 1 !important;
}

.down,
.el-icon-caret-bottom {
  color: #39c15b;
  font-size: 12px;
}

.main_tit {
  color: #333;
  font-size: 14px;
  font-weight: 500;
}

.content-time {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.main_badge {
  width: 30px;
  height: 30px;
  border-radius: 5px;
  margin-right: 10px;
  background: #2c90ff;
  color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
}

.my15 {
  margin: 15px 0 15px;
}

.align-center {
  align-items: center;
}

.baseInfo {
  /deep/ .el-card__header {
    padding: 15px 20px !important;
  }
}

.content {
  &-number {
    font-size: 30px;
    font-weight: 600;
    font-family: PingFangSC-Semibold, PingFang SC;
    color: #333;
  }

  &-time {
    font-size: 14px;
    color: #333333;
    font-weight: 400;
  }
}
</style>