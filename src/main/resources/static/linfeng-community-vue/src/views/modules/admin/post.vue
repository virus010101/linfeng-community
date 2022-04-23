<template>
  <div class="mod-config">
    <el-form
      :inline="true"
      :model="dataForm"
      @keyup.enter.native="getDataList()"
    >
      <!-- <el-form-item>
        <el-input
          v-model="dataForm.key"
          placeholder="参数名"
          clearable
        ></el-input>
      </el-form-item> -->
      <el-form-item>
        <!-- <el-button @click="getDataList()">查询</el-button> -->
        <!-- <el-button
          v-if="isAuth('admin:post:save')"
          type="primary"
          @click="addOrUpdateHandle()"
          >新增</el-button
        > -->
        <el-button
          v-if="isAuth('admin:post:delete')"
          type="danger"
          @click="deleteHandle()"
          :disabled="dataListSelections.length <= 0"
          >批量删除</el-button
        >
      </el-form-item>
    </el-form>
    <el-table
      :data="dataList"
      border
      v-loading="dataListLoading"
      @selection-change="selectionChangeHandle"
      style="width: 100%"
    >
      <el-table-column
        type="selection"
        header-align="center"
        align="center"
        width="50"
      >
      </el-table-column>
      <el-table-column
        prop="id"
        header-align="center"
        align="center"
        label="id"
      >
      </el-table-column>
      <el-table-column
        prop="userInfo.username"
        header-align="center"
        align="center"
        label="用户名"
      >
      </el-table-column>
      <el-table-column
        prop="userInfo.avatar"
        header-align="center"
        align="center"
        label="头像"
      >
        <template slot-scope="scope">
          <img
            style="width: 40px; height: 40px"
            :src="scope.row.userInfo.avatar"
          />
        </template>
      </el-table-column>
      <el-table-column
        prop="topicName"
        header-align="center"
        align="center"
        label="圈子"
      >
      </el-table-column>
      <el-table-column
        prop="title"
        header-align="center"
        align="center"
        label="标题"
      >
      </el-table-column>
      <el-table-column
        prop="content"
        header-align="center"
        align="center"
        width="200"
        label="内容"
      >
      </el-table-column>
      <el-table-column
        prop="type"
        header-align="center"
        align="center"
        label="帖子类型"
      >
        <template slot-scope="scope">
          <div>
            <el-tag v-if="scope.row.type == 1" type="success">图文</el-tag>
            <el-tag v-else-if="scope.row.type == 2" type="warning">视频</el-tag>
            <el-tag v-else-if="scope.row.type == 3" type="info">文章</el-tag>
            <el-tag v-else type="danger">投票</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="media"
        header-align="center"
        align="center"
        label="文件"
      >
        <template slot-scope="scope">
          <img
            v-if="scope.row.type == 1 && scope.row.media[0]"
            style="width: 80px; height: 50px"
            :src="scope.row.media[0]"
          />
          <!-- <video
            v-if="scope.row.type == 2"
            class="myVideo"
            id="myVideo"
            :src="scope.row.media[0]"
            enable-danmu
            danmu-btn
            controls
          ></video> -->
          <el-button v-if="scope.row.type == 2" type="text" @click="openVideo(scope.row.media[0])">点击预览</el-button>
        </template>
      </el-table-column>
      <el-table-column
        prop="readCount"
        header-align="center"
        align="center"
        label="浏览量"
      >
      </el-table-column>
      <el-table-column
        prop="commentCount"
        header-align="center"
        align="center"
        label="评论数"
      >
      </el-table-column>
      <el-table-column
        prop="collectionCount"
        header-align="center"
        align="center"
        label="点赞数"
      >
      </el-table-column>
      <el-table-column
        prop="discussTitle"
        header-align="center"
        align="center"
        label="话题"
      >
      </el-table-column>
      <el-table-column
        prop="postTop"
        header-align="center"
        align="center"
        label="置顶"
      >
        <template slot-scope="scope">
          <div>
            <el-tag v-if="scope.row.postTop == 1" type="success">是</el-tag>
            <el-tag v-else type="danger">否</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="status"
        header-align="center"
        align="center"
        label="状态"
      >
        <template slot-scope="scope">
          <div>
            <el-tag v-if="scope.row.status == 0" type="success">上架</el-tag>
             <el-tag v-else-if="scope.row.status ==1" type="warning">待审核</el-tag>
            <el-tag v-else type="danger">下架</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="address"
        header-align="center"
        align="center"
        label="发帖地址"
      >
      </el-table-column>

      <el-table-column
        prop="createTime"
        header-align="center"
        align="center"
        label="创建时间"
      >
      </el-table-column>
      <el-table-column
        fixed="right"
        header-align="center"
        align="center"
        width="150"
        label="操作"
      >
        <template slot-scope="scope">
          <el-button
            type="text"
            size="small"
            @click="addOrUpdateHandle(scope.row.id)"
            >修改</el-button
          >
          <el-button
            type="text"
            size="small"
            @click="deleteHandle(scope.row.id)"
            >删除</el-button
          >
        </template>
      </el-table-column>
    </el-table>
    <el-pagination
      @size-change="sizeChangeHandle"
      @current-change="currentChangeHandle"
      :current-page="pageIndex"
      :page-sizes="[10, 20, 50, 100]"
      :page-size="pageSize"
      :total="totalPage"
      layout="total, sizes, prev, pager, next, jumper"
    >
    </el-pagination>
    <!-- 弹窗, 新增 / 修改 -->
    <add-or-update
      v-if="addOrUpdateVisible"
      ref="addOrUpdate"
      @refreshDataList="getDataList"
    ></add-or-update>

    <el-dialog
      title="视频预览"
      :visible.sync="dialogVisible"
      width="30%"
      :before-close="handleClose"
    >
          <video
            class="video"
            :src="this.videoUrl"
            enable-danmu
            danmu-btn
            controls
          ></video>
      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible = false"
          >确 定</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import AddOrUpdate from "./post-add-or-update";
export default {
  data() {
    return {
      dataForm: {
        key: "",
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      dialogVisible: false,
      videoUrl:""
    };
  },
  components: {
    AddOrUpdate,
  },
  activated() {
    this.getDataList();
  },
  methods: {
    // 获取数据列表
    getDataList() {
      this.dataListLoading = true;
      this.$http({
        url: this.$http.adornUrl("/admin/post/list"),
        method: "get",
        params: this.$http.adornParams({
          page: this.pageIndex,
          limit: this.pageSize,
          key: this.dataForm.key,
        }),
      }).then(({ data }) => {
        if (data && data.code === 0) {
          this.dataList = data.page.list;
          this.totalPage = data.page.totalCount;
        } else {
          this.dataList = [];
          this.totalPage = 0;
        }
        this.dataListLoading = false;
      });
    },
    // 每页数
    sizeChangeHandle(val) {
      this.pageSize = val;
      this.pageIndex = 1;
      this.getDataList();
    },
    // 当前页
    currentChangeHandle(val) {
      this.pageIndex = val;
      this.getDataList();
    },
    // 多选
    selectionChangeHandle(val) {
      this.dataListSelections = val;
    },
    // 新增 / 修改
    addOrUpdateHandle(id) {
      this.addOrUpdateVisible = true;
      this.$nextTick(() => {
        this.$refs.addOrUpdate.init(id);
      });
    },
    // 删除
    deleteHandle(id) {
      var ids = id
        ? [id]
        : this.dataListSelections.map((item) => {
            return item.id;
          });
      this.$confirm(
        `确定对[id=${ids.join(",")}]进行[${id ? "删除" : "批量删除"}]操作?`,
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      ).then(() => {
        this.$http({
          url: this.$http.adornUrl("/admin/post/delete"),
          method: "post",
          data: this.$http.adornData(ids, false),
        }).then(({ data }) => {
          if (data && data.code === 0) {
            this.$message({
              message: "操作成功",
              type: "success",
              duration: 1500,
              onClose: () => {
                this.getDataList();
              },
            });
          } else {
            this.$message.error(data.msg);
          }
        });
      });
    },

    handleClose(done) {
        this.$confirm('确认关闭？')
          .then(_ => {
            done();
          })
          .catch(_ => {});
    },
    openVideo(url){
      this.dialogVisible=true;
      this.videoUrl=url;
    }
  },
};
</script>
<style lang="scss" scoped>
     .video {
       width: 100%;
       margin-bottom: 10px;
     }
</style>