<template>
  <el-dialog
    :title="!dataForm.uid ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible"
  >
    <el-form
      :model="dataForm"
      ref="dataForm"
      @keyup.enter.native="dataFormSubmit()"
      label-width="80px"
    >
      <el-form-item label="状态" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="1">禁用</el-radio>
          <el-radio :label="0">正常</el-radio>
        </el-radio-group>
      </el-form-item>
      <el-form-item label="类型" prop="type">
        <el-radio-group v-model="dataForm.type">
          <el-radio :label="1">官方账号</el-radio>
          <el-radio :label="0">普通用户</el-radio>
        </el-radio-group>
      </el-form-item>
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
      visible: false,
      dataForm: {
        uid: 0,
        mobile: "",
        username: "",
        password: "",
        groupId: "",
        avatar: "",
        gender: "",
        province: "",
        city: "",
        openid: "",
        mpOpenid: "",
        unionid: "",
        status: "",
        intro: "",
        integral: "",
        lastLoginIp: "",
        tagStr: "",
        type: "",
        updateTime: "",
        createTime: "",
      },
    };
  },
  methods: {
    init(id) {
      this.dataForm.uid = id || 0;
      this.visible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        if (this.dataForm.uid) {
          this.$http({
            url: this.$http.adornUrl(`/admin/user/info/${this.dataForm.uid}`),
            method: "get",
            params: this.$http.adornParams(),
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.mobile = data.user.mobile;
              this.dataForm.username = data.user.username;
              this.dataForm.password = data.user.password;
              this.dataForm.groupId = data.user.groupId;
              this.dataForm.avatar = data.user.avatar;
              this.dataForm.gender = data.user.gender;
              this.dataForm.province = data.user.province;
              this.dataForm.city = data.user.city;
              this.dataForm.openid = data.user.openid;
              this.dataForm.mpOpenid = data.user.mpOpenid;
              this.dataForm.unionid = data.user.unionid;
              this.dataForm.status = data.user.status;
              this.dataForm.intro = data.user.intro;
              this.dataForm.integral = data.user.integral;
              this.dataForm.lastLoginIp = data.user.lastLoginIp;
              this.dataForm.tagStr = data.user.tagStr;
              this.dataForm.type = data.user.type;
              this.dataForm.updateTime = data.user.updateTime;
              this.dataForm.createTime = data.user.createTime;
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
              `/admin/user/${!this.dataForm.uid ? "save" : "update"}`
            ),
            method: "post",
            data: this.$http.adornData({
              uid: this.dataForm.uid || undefined,
              mobile: this.dataForm.mobile,
              username: this.dataForm.username,
              password: this.dataForm.password,
              groupId: this.dataForm.groupId,
              avatar: this.dataForm.avatar,
              gender: this.dataForm.gender,
              province: this.dataForm.province,
              city: this.dataForm.city,
              openid: this.dataForm.openid,
              mpOpenid: this.dataForm.mpOpenid,
              unionid: this.dataForm.unionid,
              status: this.dataForm.status,
              intro: this.dataForm.intro,
              integral: this.dataForm.integral,
              lastLoginIp: this.dataForm.lastLoginIp,
              tagStr: this.dataForm.tagStr,
              type: this.dataForm.type,
              updateTime: this.dataForm.updateTime,
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
  },
};
</script>
