<template>
  <el-dialog
    :title="!dataForm.config ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="配置值" prop="value">
      <el-input v-model="dataForm.value" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="配置拓展" prop="extend">
      <el-input v-model="dataForm.extend" placeholder=""></el-input>
    </el-form-item>
    <el-form-item label="附属配置" prop="intro">
      <el-input v-model="dataForm.intro" placeholder=""></el-input>
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
        visible: false,
        dataForm: {
          config: 0,
          value: '',
          extend: '',
          intro: ''
        },
        dataRule: {
          // value: [
          //   { required: true, message: '不能为空', trigger: 'blur' }
          // ],
          // extend: [
          //   { required: true, message: '不能为空', trigger: 'blur' }
          // ],
          // intro: [
          //   { required: true, message: '不能为空', trigger: 'blur' }
          // ]
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.config = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.config) {
            this.$http({
              url: this.$http.adornUrl(`/admin/system/info/${this.dataForm.config}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.value = data.system.value
                this.dataForm.extend = data.system.extend
                this.dataForm.intro = data.system.intro
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
              url: this.$http.adornUrl(`/admin/system/${!this.dataForm.config ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'config': this.dataForm.config || undefined,
                'value': this.dataForm.value,
                'extend': this.dataForm.extend,
                'intro': this.dataForm.intro
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
      }
    }
  }
</script>
