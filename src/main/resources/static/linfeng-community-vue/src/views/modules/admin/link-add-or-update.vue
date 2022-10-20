<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="标题" prop="title">
      <el-input v-model="dataForm.title" placeholder="标题"></el-input>
    </el-form-item>
      <el-form-item label="图片" prop="coverImage">
        <el-upload
          class="avatar-uploader"
          :action="url"
          :show-file-list="false"
          :on-success="handleIconSuccess"
        >
          <img
            v-if="dataForm.img"
            :src="dataForm.img"
            class="avatar"
          />
          <i v-else class="el-icon-plus avatar-uploader-icon" />
        </el-upload>
        <p class="formInfo">建议尺寸：500*250像素，jpg、png图片类型</p>
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
    data () {
      return {
        url: "",
        visible: false,
        dataForm: {
          id: 0,
          title: '',
          url: '',
          img: '',
          type: '',
          createTime: ''
        },
        dataRule: {
          title: [
            { required: true, message: '标题不能为空', trigger: 'blur' }
          ],
          url: [
            { required: true, message: '路径不能为空', trigger: 'blur' }
          ],
          img: [
            { required: true, message: '图片不能为空', trigger: 'blur' }
          ],
          type: [
            { required: true, message: '3圈子页轮播图不能为空', trigger: 'blur' }
          ],
          createTime: [
            { required: true, message: '创建时间不能为空', trigger: 'blur' }
          ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.id = id || 0
        this.url = this.$http.adornUrl(
        `/sys/oss/upload?token=${this.$cookie.get("token")}`
        );
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/admin/link/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.title = data.link.title
                this.dataForm.url = data.link.url
                this.dataForm.img = data.link.img
                this.dataForm.type = data.link.type
                this.dataForm.createTime = data.link.createTime
              }
            })
          }
        })
      },
      // 表单提交
      dataFormSubmit () {
        this.$refs['dataForm'].validate((valid) => {
          if (valid) {
            this.$http({
              url: this.$http.adornUrl(`/admin/link/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'title': this.dataForm.title,
                'url': this.dataForm.url,
                'img': this.dataForm.img,
                'type': this.dataForm.type,
                'createTime': this.dataForm.createTime
              })
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.$message({
                  message: '操作成功',
                  type: 'success',
                  duration: 1500,
                  onClose: () => {
                    this.visible = false
                    this.$emit('refreshDataList')
                  }
                })
              } else {
                this.$message.error(data.msg)
              }
            })
          }
        })
      },
    handleIconSuccess(response) {
      this.dataForm.img = response.url;
      this.$forceUpdate();
    },
    }
  }
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