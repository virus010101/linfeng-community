<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
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
      <el-form-item label="创建用户id" prop="uid">
        <el-input v-model="dataForm.uid" placeholder="创建用户id"></el-input>
      </el-form-item>
      <el-form-item label="分类id" prop="cateId">
        <el-input v-model="dataForm.cateId" placeholder="分类id"></el-input>
      </el-form-item>
      <el-form-item label="圈子名称" prop="topicName">
        <el-input
          v-model="dataForm.topicName"
          placeholder="圈子名称"
        ></el-input>
      </el-form-item>
      <el-form-item label="描述" prop="description">
        <el-input v-model="dataForm.description" placeholder="描述"></el-input>
      </el-form-item>
      <!-- <el-form-item label="logo" prop="coverImage">
        <el-input v-model="dataForm.coverImage" placeholder="logo"></el-input>
      </el-form-item> -->
      <!-- <el-form-item label="背景图" prop="bgImage">
        <el-input v-model="dataForm.bgImage" placeholder="背景图"></el-input>
      </el-form-item> -->
      <el-form-item label="背景图" prop="bgImage">
        <el-upload
          class="avatar-uploader"
          :action="url"
          :show-file-list="false"
          :on-success="handleBgImageSuccess"
        >
          <img v-if="dataForm.bgImage" :src="dataForm.bgImage" class="avatar" />
          <i v-else class="el-icon-plus avatar-uploader-icon" />
        </el-upload>
        <p class="formInfo">建议尺寸：400*300像素，jpg、png图片类型</p>
      </el-form-item>

      <el-form-item label="圈子头像" prop="coverImage">
        <el-upload
          class="avatar-uploader"
          :action="url"
          :show-file-list="false"
          :on-success="handleLogoSuccess"
        >
          <img v-if="dataForm.coverImage" :src="dataForm.coverImage" class="avatar" />
          <i v-else class="el-icon-plus avatar-uploader-icon" />
        </el-upload>
        <p class="formInfo">建议尺寸：100*100像素，jpg、png图片类型</p>
      </el-form-item>

      <!-- <el-form-item label="推荐类型" prop="topType">
        <el-radio-group v-model="dataForm.topType">
          <el-radio :label="1">首页推荐</el-radio>
          <el-radio :label="0">不推荐</el-radio>
          <el-radio :label="2">圈子页推荐</el-radio>
        </el-radio-group>
      </el-form-item> -->
      <el-form-item label="圈子状态" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="0">正常</el-radio>
          <el-radio :label="1">禁用</el-radio>
        </el-radio-group>
      </el-form-item>
      <!-- <el-form-item label="是否首页推荐圈子内容" prop="indexRecommend">
        <el-input
          v-model="dataForm.indexRecommend"
          placeholder="是否首页推荐圈子内容"
        ></el-input>
      </el-form-item> -->
      <el-form-item label="加入人数" prop="userNum">
        <el-input v-model="dataForm.userNum" placeholder="加入人数"></el-input>
      </el-form-item>
      <!-- <el-form-item label="创建时间" prop="createTime">
        <el-input
          v-model="dataForm.createTime"
          placeholder="创建时间"
        ></el-input>
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
        id: 0,
        uid: "",
        cateId: "",
        topicName: "",
        description: "",
        coverImage: "",
        bgImage: "",
        topType: 0,
        status: 1,
        indexRecommend: 1,
        userNum: 0,
        createTime: "",
      },
      dataRule: {
        uid: [
          { required: true, message: "创建用户id不能为空", trigger: "blur" },
        ],
        cateId: [
          { required: true, message: "分类id不能为空", trigger: "blur" },
        ],
        topicName: [
          { required: true, message: "圈子名称不能为空", trigger: "blur" },
        ],
        description: [
          { required: true, message: "描述不能为空", trigger: "blur" },
        ],
        coverImage: [
          { required: true, message: "logo不能为空", trigger: "blur" },
        ],
        bgImage: [
          { required: true, message: "背景图不能为空", trigger: "blur" },
        ],
        topType: [
          {
            required: true,
            message: "推荐类型不能为空",
            trigger: "blur",
          },
        ],
        status: [
          {
            required: true,
            message: "圈子状态不能为空",
            trigger: "blur",
          },
        ],
        // indexRecommend: [
        //   {
        //     required: true,
        //     message: "是否首页推荐圈子内容不能为空",
        //     trigger: "blur",
        //   },
        // ],
        // userNum: [
        //   { required: true, message: "加入人数不能为空", trigger: "blur" },
        // ],
        // createTime: [
        //   { required: true, message: "创建时间不能为空", trigger: "blur" },
        // ],
      },
    };
  },
  methods: {
    init(id) {
      this.url = this.$http.adornUrl(
        `/sys/oss/upload?token=${this.$cookie.get("token")}`
      );
      this.dataForm.id = id || 0;
      this.visible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        if (this.dataForm.id) {
          this.$http({
            url: this.$http.adornUrl(`/admin/topic/info/${this.dataForm.id}`),
            method: "get",
            params: this.$http.adornParams(),
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.uid = data.topic.uid;
              this.dataForm.cateId = data.topic.cateId;
              this.dataForm.topicName = data.topic.topicName;
              this.dataForm.description = data.topic.description;
              this.dataForm.coverImage = data.topic.coverImage;
              this.dataForm.bgImage = data.topic.bgImage;
              this.dataForm.topType = data.topic.topType;
              this.dataForm.status = data.topic.status;
              this.dataForm.indexRecommend = data.topic.indexRecommend;
              this.dataForm.userNum = data.topic.userNum;
              this.dataForm.createTime = data.topic.createTime;
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
              `/admin/topic/${!this.dataForm.id ? "save" : "update"}`
            ),
            method: "post",
            data: this.$http.adornData({
              id: this.dataForm.id || undefined,
              uid: this.dataForm.uid,
              cateId: this.dataForm.cateId,
              topicName: this.dataForm.topicName,
              description: this.dataForm.description,
              coverImage: this.dataForm.coverImage,
              bgImage: this.dataForm.bgImage,
              topType: this.dataForm.topType,
              status: this.dataForm.status,
              indexRecommend: this.dataForm.indexRecommend,
              userNum: this.dataForm.userNum,
              createTime: this.dataForm.createTime,
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
    handleBgImageSuccess(response) {
      this.dataForm.bgImage = response.url;
      this.$forceUpdate();
    },
    handleLogoSuccess(response) {
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