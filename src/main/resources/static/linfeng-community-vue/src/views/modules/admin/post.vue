<template>
  <div class="mod-config">
    <el-form
      :inline="true"
      :model="dataForm"
      @keyup.enter.native="getDataList()"
    >
      <el-form-item>
        <el-input
          v-model="dataForm.key"
          placeholder="帖子内容或标题搜索"
          clearable
        ></el-input>
      </el-form-item>
      <el-form-item>
      <!-- 选择框 -->
      <el-select v-model="dataForm.status" clearable placeholder="请选择状态">
        <el-option
          v-for="item in options"
          :key="item.value"
          :label="item.label"
          :value="item.value">
        </el-option>
     </el-select>

      <el-button @click="getDataList()">查询</el-button>
      <el-button @click="refresh()">重置</el-button>
      
        <!-- <el-button
          v-if="isAuth('admin:post:delete')"
          type="danger"
          @click="statusHandle()"
          :disabled="dataListSelections.length <= 0"
          >批量审核</el-button
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
        label="归属"
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
          <el-button v-if="scope.row.type == 2" type="text" @click="openVideo(scope.row.media[0])">点击预览</el-button>
          <el-button v-if="scope.row.type == 1 && scope.row.media[0]" type="text" @click="openPic(scope.row.media)">点击查看</el-button>
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
          <el-button v-if="scope.row.status==1 || scope.row.status==2"
            type="text"
            size="small"
            @click="statusHandle(scope.row.id)"
            >上架</el-button
          >
          <el-button v-if="scope.row.status==0"
            type="text"
            size="small"
            @click="statusDownHandle(scope.row.id)"
            >下架</el-button
          >

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

    <el-dialog
      title="图片预览"
      :visible.sync="dialogVisible2"
      width="60%"
      :before-close="handleClose"
    >
    
    <div class="position">图片展示</div>
    <div class="images">
      <div v-for="(item, index) in media" :key="index" class="image-middle">  
        <el-card shadow="hover" :body-style="{ padding: '10px' }" >     
        <img :src="media[index]" class="image" @click="goPic(media[index])"/> 
        <div style="text-align:center;padding-top:12px">
        <span>图{{index}}</span>
        </div>
        </el-card>
      </div>
    </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible2 = false">取 消</el-button>
        <el-button type="primary" @click="dialogVisible2 = false"
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
        status:'',
      },
      dataList: [],
      pageIndex: 1,
      pageSize: 10,
      totalPage: 0,
      dataListLoading: false,
      dataListSelections: [],
      addOrUpdateVisible: false,
      dialogVisible: false,
      dialogVisible2: false,
      videoUrl:"",
      media: [],
       options: [{
          value: 0,
          label: '正常'
        }, {
          value: 1,
          label: '待审核'
        }, {
          value: 2,
          label: '已下架'
        }],
        // value: ''
    };
  },
  components: {
    AddOrUpdate,
  },
  activated() {
    this.getDataList();
  },
  methods: {
    refresh(){
        this.dataForm.key='';
        this.dataForm.status='';
        this.pageIndex=1;
        this.pageSize=10;
        this.getDataList();
    },
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
          status:this.dataForm.status
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
    statusDownHandle(id){
            var ids = id
        ? [id]
        : this.dataListSelections.map((item) => {
            return item.id;
          });
      this.$confirm(
        `确定对[id=${ids.join(",")}]进行[${id ? "下架" : "批量下架"}]操作?`,
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      ).then(() => {
        this.$http({
          url: this.$http.adornUrl("/admin/post/down"),
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
    // 审核
    statusHandle(id) {
      var ids = id
        ? [id]
        : this.dataListSelections.map((item) => {
            return item.id;
          });
      this.$confirm(
        `确定对[id=${ids.join(",")}]进行[${id ? "上架" : "批量上架"}]操作?`,
        "提示",
        {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning",
        }
      ).then(() => {
        this.$http({
          url: this.$http.adornUrl("/admin/post/up"),
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
    },
    openPic(media){
      this.dialogVisible2=true;
      this.media=media;
    },
    goPic(url){
      console.log('==>',url);
      window.open(url);
    }


  },
};
</script>
<style lang="scss" scoped>
.video {
  width: 100%;
  margin-bottom: 10px;
}
.position {
  margin-left: 15px;
  font-size: 30px;
  font-weight: 600;
}
/* 图片总布局，样式 */
.images{
  display: flex;
  margin-top: 20px;
  margin-left: 21px;
  margin-right: 20px;
  flex-wrap: wrap;
}
/* 图片之间 */
.image-middle{
  margin-right: 15px;
  margin-bottom: 15px;
}
/* 单张图片样式 */
.image{
  width:110px;
  height: 110px;
}

</style>