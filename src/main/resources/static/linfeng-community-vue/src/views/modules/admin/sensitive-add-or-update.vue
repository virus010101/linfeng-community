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
      <el-form-item label="敏感词库" prop="sensitiveWord">
        <el-input
          v-model="dataForm.sensitiveWord"
          placeholder="敏感词库"
        ></el-input>
        <p class="formInfo">敏感词请用英文逗号","隔开</p>
      </el-form-item>
      <el-form-item label="是否开启" prop="state">
        <el-radio-group v-model="dataForm.state">
          <el-radio :label="1">是</el-radio>
          <el-radio :label="0">否</el-radio>
        </el-radio-group>
      </el-form-item>
      <!-- <el-form-item label="处理措施" prop="handleMeasures">
        <el-radio-group v-model="dataForm.handleMeasures">
          <el-radio :label="1">禁止发布</el-radio>
          <el-radio :label="2">需要审核</el-radio>
        </el-radio-group>
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
      visible: false,
      dataForm: {
        id: 0,
        sensitiveWord: "",
        state: "",
        handleMeasures: "",
      },
      dataRule: {
        sensitiveWord: [
          { required: true, message: "敏感词库不能为空", trigger: "blur" },
        ],
        state: [
          {
            required: true,
            message: "是否开启 1-是 0-否不能为空",
            trigger: "blur",
          },
        ],
        handleMeasures: [
          {
            required: true,
            message: "处理措施  1-禁止发布 2-需审核不能为空",
            trigger: "blur",
          },
        ],
      },
    };
  },
  methods: {
    init(id) {
      this.dataForm.id = id || 0;
      this.visible = true;
      this.$nextTick(() => {
        this.$refs["dataForm"].resetFields();
        if (this.dataForm.id) {
          this.$http({
            url: this.$http.adornUrl(
              `/admin/sensitive/info/${this.dataForm.id}`
            ),
            method: "get",
            params: this.$http.adornParams(),
          }).then(({ data }) => {
            if (data && data.code === 0) {
              this.dataForm.sensitiveWord = data.sensitive.sensitiveWord;
              this.dataForm.state = data.sensitive.state;
              this.dataForm.handleMeasures = data.sensitive.handleMeasures;
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
              `/admin/sensitive/${!this.dataForm.id ? "save" : "update"}`
            ),
            method: "post",
            data: this.$http.adornData({
              id: this.dataForm.id || undefined,
              sensitiveWord: this.dataForm.sensitiveWord,
              state: this.dataForm.state,
              handleMeasures: this.dataForm.handleMeasures,
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
<style lang="scss">
.formInfo {
  line-height: 0px;
  color: #f14a4a;
}

</style>
