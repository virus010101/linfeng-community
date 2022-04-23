<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="父级id" prop="pid">
      <el-input v-model="dataForm.pid" placeholder="父级id"></el-input>
    </el-form-item>
    <!-- <el-form-item label="评论类型:1帖子" prop="type">
      <el-input v-model="dataForm.type" placeholder="评论类型:1帖子"></el-input>
    </el-form-item> -->
    <el-form-item label="评论作者ID" prop="uid">
      <el-input v-model="dataForm.uid" placeholder="评论作者ID"></el-input>
    </el-form-item>
    <el-form-item label="被回复用户ID" prop="toUid">
      <el-input v-model="dataForm.toUid" placeholder="被回复用户ID"></el-input>
    </el-form-item>
    <el-form-item label="评论帖子ID" prop="postId">
      <el-input v-model="dataForm.postId" placeholder="评论帖子ID"></el-input>
    </el-form-item>
    <el-form-item label="评论内容" prop="content">
      <el-input v-model="dataForm.content" placeholder="评论内容"></el-input>
    </el-form-item>
    <el-form-item label="评论状态" prop="status">
      <el-input v-model="dataForm.status" placeholder="评论状态"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="创建时间"></el-input>
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
          id: 0,
          pid: '',
          type: '',
          uid: '',
          toUid: '',
          postId: '',
          content: '',
          status: '',
          createTime: ''
        },
        dataRule: {
          pid: [
            { required: true, message: '父级id不能为空', trigger: 'blur' }
          ],
          // type: [
          //   { required: true, message: '评论类型:1帖子不能为空', trigger: 'blur' }
          // ],
          uid: [
            { required: true, message: '评论作者ID不能为空', trigger: 'blur' }
          ],
          toUid: [
            { required: true, message: '被回复用户ID不能为空', trigger: 'blur' }
          ],
          postId: [
            { required: true, message: '评论帖子ID不能为空', trigger: 'blur' }
          ],
          content: [
            { required: true, message: '评论内容不能为空', trigger: 'blur' }
          ],
          status: [
            { required: true, message: '评论状态不能为空', trigger: 'blur' }
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
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.id) {
            this.$http({
              url: this.$http.adornUrl(`/admin/comment/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.pid = data.comment.pid
                this.dataForm.type = data.comment.type
                this.dataForm.uid = data.comment.uid
                this.dataForm.toUid = data.comment.toUid
                this.dataForm.postId = data.comment.postId
                this.dataForm.content = data.comment.content
                this.dataForm.status = data.comment.status
                this.dataForm.createTime = data.comment.createTime
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
              url: this.$http.adornUrl(`/admin/comment/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'pid': this.dataForm.pid,
                'type': this.dataForm.type,
                'uid': this.dataForm.uid,
                'toUid': this.dataForm.toUid,
                'postId': this.dataForm.postId,
                'content': this.dataForm.content,
                'status': this.dataForm.status,
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
      }
    }
  }
</script>
