<template>
  <div class="mod-config">
    <el-form
      :inline="true"
      :model="dataForm"
      @keyup.enter.native="getDataList()"
    >
      <el-form-item>
        <!-- <el-input
          v-model="dataForm.key"
          placeholder="参数名"
          clearable
        ></el-input> -->
      </el-form-item>
      <el-form-item>
        <!-- <el-button @click="getDataList()">查询</el-button> -->
        <!-- <el-button
          v-if="isAuth('admin:topic:save')"
          type="primary"
          @click="addOrUpdateHandle()"
          >新增</el-button> -->
        <el-button
          v-if="isAuth('admin:topic:delete')"
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
        label="圈子id"
      >
      </el-table-column>
      <el-table-column
        prop="uid"
        header-align="center"
        align="center"
        label="创建用户id"
      >
      </el-table-column>
      <el-table-column
        prop="cateId"
        header-align="center"
        align="center"
        label="分类id"
      >
      </el-table-column>
      <el-table-column
        prop="topicName"
        header-align="center"
        align="center"
        label="圈子名称"
      >
      </el-table-column>
      <el-table-column
        prop="description"
        header-align="center"
        align="center"
        label="描述"
      >
      </el-table-column>
      <el-table-column
        prop="coverImage"
        header-align="center"
        align="center"
        width="100"
        label="圈子头像"
      >
        <template slot-scope="scope">
          <img style="width: 40px; height: 40px" :src="scope.row.coverImage" />
        </template>
      </el-table-column>
      <el-table-column
        prop="bgImage"
        header-align="center"
        align="center"
        width="100"
        label="背景图"
      >
        <template slot-scope="scope">
          <img style="width: 80px; height: 50px" :src="scope.row.bgImage" />
        </template>
      </el-table-column>
      <!-- <el-table-column
        prop="topType"
        header-align="center"
        align="center"
        label="推荐类型"
      >
        <template slot-scope="scope">
          <div>
            <el-tag v-if="scope.row.topType == 1" type="success"
              >首页推荐</el-tag
            >
            <el-tag v-else-if="scope.row.topType == 2" type="warning"
              >未知</el-tag
            >
            <el-tag v-else type="danger">圈子推荐</el-tag>
          </div>
        </template>
      </el-table-column> -->
      <el-table-column
        prop="status"
        header-align="center"
        align="center"
        label="圈子状态"
      >
        <template slot-scope="scope">
          <div>
            <el-tag v-if="scope.row.status == 0" type="success">正常</el-tag>
            <el-tag v-else type="danger">禁用</el-tag>
          </div>
        </template>
      </el-table-column>
      <!-- <el-table-column
        prop="indexRecommend"
        header-align="center"
        align="center"
        label="首页推荐"
      >
      </el-table-column> -->
      <el-table-column
        prop="userNum"
        header-align="center"
        align="center"
        label="加入人数"
      >
      </el-table-column>
      <el-table-column
        prop="createTime"
        header-align="center"
        align="center"
        width="170"
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
  </div>
</template>

<script>
import AddOrUpdate from "./topic-add-or-update";
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
        url: this.$http.adornUrl("/admin/topic/list"),
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
          url: this.$http.adornUrl("/admin/topic/delete"),
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
  },
};
</script>
