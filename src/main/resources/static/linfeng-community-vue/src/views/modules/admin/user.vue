<template>
  <div class="mod-config">
    <el-form
      :inline="true"
      :model="dataForm"
      @keyup.enter.native="getDataList()"
    >
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
        prop="uid"
        header-align="center"
        align="center"
        label="用户id"
      >
      </el-table-column>
      <el-table-column
        prop="username"
        header-align="center"
        align="center"
        label="用户名"
      >
      </el-table-column>
      <el-table-column
        prop="avatar"
        header-align="center"
        align="center"
        label="头像"
        width="100"
      >
        <template slot-scope="scope">
          <img style="width: 40px; height: 40px" :src="scope.row.avatar" />
        </template>
      </el-table-column>
      <el-table-column
        prop="gender"
        header-align="center"
        align="center"
        label="性别"
      >
        <template slot-scope="scope">
          <div>
            <el-tag v-if="scope.row.gender == 1" type="success">男</el-tag>
            <el-tag v-else-if="scope.row.gender == 0">保密</el-tag>
            <el-tag v-else type="danger">女</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="city"
        header-align="center"
        align="center"
        label="城市"
      >
      </el-table-column>
      <el-table-column
        prop="mobile"
        header-align="center"
        align="center"
        label="手机号"
      >
      </el-table-column>
      <el-table-column
        prop="openid"
        header-align="center"
        align="center"
        width="150"
        label="小程序openid"
      >
      </el-table-column>
      <el-table-column
        prop="status"
        header-align="center"
        align="center"
        label="状态"
      >
        <template slot-scope="scope">
          <div>
            <el-tag v-if="scope.row.status == 0" type="success">正常</el-tag>
            <el-tag v-else-if="scope.row.status == 1" type="warning"
              >禁用</el-tag
            >
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="intro"
        header-align="center"
        align="center"
        label="个性签名"
      >
      </el-table-column>
      <el-table-column
        prop="tagStr"
        header-align="center"
        align="center"
        label="用户标签"
      >
      </el-table-column>
      <el-table-column
        prop="type"
        header-align="center"
        align="center"
        label="用户类型"
      >
        <template slot-scope="scope">
          <div>
            <el-tag v-if="scope.row.type == 1" type="success">官方账号</el-tag>
            <el-tag v-else-if="scope.row.type == 0">普通用户</el-tag>
            <el-tag v-else type="danger">虚拟用户</el-tag>
          </div>
        </template>
      </el-table-column>
      <el-table-column
        prop="updateTime"
        header-align="center"
        align="center"
        label="更新时间"
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
            @click="addOrUpdateHandle(scope.row.uid)"
            >修改</el-button
          >
          <el-button
            v-if="scope.row.status == 0"
            type="text"
            size="small"
            @click="banHandle(scope.row.uid)"
            >禁用</el-button
          >
          <el-button
            v-if="scope.row.status == 1"
            type="text"
            size="small"
            @click="openBanHandle(scope.row.uid)"
            >解禁</el-button
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
import AddOrUpdate from "./user-add-or-update";
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
        url: this.$http.adornUrl("/admin/user/list"),
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
            return item.uid;
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
          url: this.$http.adornUrl("/admin/user/delete"),
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
    // 禁用
    banHandle(id) {
      this.$confirm(`确定对该用户禁用操作?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl(`/admin/user/ban/${id}`),
          method: "get",
          params: this.$http.adornParams(),
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

    // 解除禁用
    openBanHandle(id) {
      this.$confirm(`确定对该用户解除禁用操作?`, "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      }).then(() => {
        this.$http({
          url: this.$http.adornUrl(`/admin/user/openBan/${id}`),
          method: "get",
          params: this.$http.adornParams(),
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
