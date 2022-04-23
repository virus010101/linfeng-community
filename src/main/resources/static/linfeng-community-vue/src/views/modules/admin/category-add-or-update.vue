<template>
  <el-dialog
    :title="!dataForm.cateId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible"
  >
    <el-form
      :model="dataForm"
      :rules="dataRule"
      ref="dataForm"
      @keyup.enter.native="dataFormSubmit()"
      label-width="80px"
    >
      <el-form-item label="分类名称" prop="cateName">
        <el-input v-model="dataForm.cateName" placeholder="分类名称"></el-input>
      </el-form-item>
      <el-form-item label="是否推荐" prop="isTop">
        <el-radio-group v-model="dataForm.isTop">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>

      <el-form-item label="分类图片" prop="coverImage">
        <el-upload
          class="avatar-uploader"
          :action="url"
          :show-file-list="false"
          :on-success="handleIconSuccess"
        >
          <img
            v-if="dataForm.coverImage"
            :src="dataForm.coverImage"
            class="avatar"
          />
          <i v-else class="el-icon-plus avatar-uploader-icon" />
        </el-upload>
        <p class="formInfo">建议尺寸：100*100像素，jpg、png图片类型</p>
      </el-form-item>
      <!-- <el-form-item label="图片" prop="coverImage">
      <el-input v-model="dataForm.coverImage" placeholder="图片"></el-input>
    </el-form-item> -->
    </el-form>
    <span slot="footer" class="dialog-footer">
      <el-button @click="visible = false">取消</el-button>
      <el-button type="primary" @click="dataFormSubmit()">确定</el-button>
    </span>
  </el-dialog>
</template>

<script>
export default {
  data() {
    return {
      url: "",
      visible: false,
      dataForm: {
        cateId: 0,
        cateName: "",
        isTop: 0,
        coverImage: "",
      },
      uploadVisible: false,
      dataRule: {
        cateName: [
          { required: true, message: "分类名称不能为空", trigger: "blur" },
        ],
        isTop: [
          { required: true, message: "是否推荐不能为空", trigger: "blur" },
        ],
        coverImage: [
          { required: true, message: "图片不能为空", trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    init(id) {
      this.dataForm.cateId = id || 0;
      this.url = this.$http.adornUrl(
        `/sys/oss/upload?token=${this.$cookie.get("token")}`
      );
      this.visible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        if (this.dataForm.cateId) {
          this.$http({
            url: this.$http.adornUrl(
              `/admin/category/info/${this.dataForm.cateId}`
            ),
            method: "get",
            params: this.$http.adornParams(),
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.cateName = data.category.cateName;
              this.dataForm.isTop = data.category.isTop;
              this.dataForm.coverImage = data.category.coverImage;
            }
          });
        }
      });
    },
    // 表单提交
    dataFormSubmit() {
      this.$refs["dataForm"].validate((valid) => {
        if (valid) {
          this.$http({
            url: this.$http.adornUrl(
              `/admin/category/${!this.dataForm.cateId ? "save" : "update"}`
            ),
            method: "post",
            data: this.$http.adornData({
              cateId: this.dataForm.cateId || undefined,
              cateName: this.dataForm.cateName,
              isTop: this.dataForm.isTop,
              coverImage: this.dataForm.coverImage,
            }),
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.$message({
                message: "操作成功",
                type: "success",
                duration: 1500,
                onClose: () => {
                  this.visible = false;
                  this.$emit("refreshDataList");
                },
              });
            } else {
              this.$message.error(data.msg);
            }
          });
        }
      });
    },
    handleIconSuccess(response) {
      this.dataForm.coverImage = response.url;
      this.$forceUpdate();
    },
  },
};
</script>
<style lang="scss">
.formInfo {
  line-height: 0px;
  color: #999999;
}
.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
}
.avatar-uploader .el-upload:hover {
  border-color: #409eff;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
}
.avatar {
  width: 100px;
  height: 100px;
  display: block;
}
</style>