<template>
  <el-dialog
    :title="!dataForm.mId ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <!-- <el-form-item label="发送者uid" prop="fromUid">
      <el-input v-model="dataForm.fromUid" placeholder="发送者uid"></el-input>
    </el-form-item>
    <el-form-item label="接收者uid" prop="toUid">
      <el-input v-model="dataForm.toUid" placeholder="接收者uid"></el-input>
    </el-form-item>
    <el-form-item label="帖子id" prop="postId">
      <el-input v-model="dataForm.postId" placeholder="帖子id"></el-input>
    </el-form-item> -->
    <el-form-item label="推送标题" prop="title">
      <el-input v-model="dataForm.title" placeholder="推送标题"></el-input>
    </el-form-item>
    <el-form-item label="消息内容" prop="content">
      <el-input v-model="dataForm.content" placeholder="消息内容"></el-input>
    </el-form-item>
    <!-- <el-form-item label="0未读，1已读" prop="status">
      <el-input v-model="dataForm.status" placeholder="0未读，1已读"></el-input>
    </el-form-item>
    <el-form-item label="1为点赞，2为评论  3为收藏 4为关注  5为推送文章 6私聊" prop="type">
      <el-input v-model="dataForm.type" placeholder="1为点赞，2为评论  3为收藏 4为关注  5为推送文章 6私聊"></el-input>
    </el-form-item>
    <el-form-item label="创建时间" prop="createTime">
      <el-input v-model="dataForm.createTime" placeholder="创建时间"></el-input>
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
    data () {
      return {
        visible: false,
        dataForm: {
          mId: 0,
          fromUid: 0,
          toUid: 0,
          postId: 0,
          title: '',
          content: '',
          status: 0,
          type: 5,
          createTime: ''
        },
        dataRule: {
          title: [
            { required: true, message: '推送标题不能为空', trigger: 'blur' }
          ],
          content: [
            { required: true, message: '消息内容不能为空', trigger: 'blur' }
          ],
          
        }
      }
    },
    methods: {
      init (id) {
        this.dataForm.mId = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.mId) {
            this.$http({
              url: this.$http.adornUrl(`/admin/message/info/${this.dataForm.mId}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.fromUid = data.message.fromUid
                this.dataForm.toUid = data.message.toUid
                this.dataForm.postId = data.message.postId
                this.dataForm.title = data.message.title
                this.dataForm.content = data.message.content
                this.dataForm.status = data.message.status
                this.dataForm.type = data.message.type
                this.dataForm.createTime = data.message.createTime
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
              url: this.$http.adornUrl(`/admin/message/${!this.dataForm.mId ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'mId': this.dataForm.mId || undefined,
                'fromUid': this.dataForm.fromUid,
                'toUid': this.dataForm.toUid,
                'postId': this.dataForm.postId,
                'title': this.dataForm.title,
                'content': this.dataForm.content,
                'status': this.dataForm.status,
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
      }
    }
  }
</script>
