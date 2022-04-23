<template>
  <el-dialog
    :title="!dataForm.id ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm" :rules="dataRule" ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <el-form-item label="用户id" prop="uid">
      <el-input v-model="dataForm.uid" placeholder="用户id"></el-input>
    </el-form-item>
    <el-form-item label="圈子id" prop="topicId">
      <el-input v-model="dataForm.topicId" placeholder="圈子id"></el-input>
    </el-form-item>
    <el-form-item label="话题id" prop="discussId">
      <el-input v-model="dataForm.discussId" placeholder="话题id"></el-input>
    </el-form-item>
    <el-form-item label="投票id" prop="voteId">
      <el-input v-model="dataForm.voteId" placeholder="投票id"></el-input>
    </el-form-item>
    <el-form-item label="标题" prop="title">
      <el-input v-model="dataForm.title" placeholder="标题"></el-input>
    </el-form-item>
    <el-form-item label="内容" prop="content">
      <el-input v-model="dataForm.content" placeholder="内容"></el-input>
    </el-form-item>
    <el-form-item label="文件" prop="media">
      <el-input v-model="dataForm.media" placeholder="文件"></el-input>
    </el-form-item>
    <el-form-item label="浏览量" prop="readCount">
      <el-input v-model="dataForm.readCount" placeholder="浏览量"></el-input>
    </el-form-item>
    <el-form-item label="置顶" prop="postTop">
      <el-input v-model="dataForm.postTop" placeholder="置顶"></el-input>
    </el-form-item>
    <el-form-item label="帖子类型：1 图文 ，2视频 ，3文章，4投票" prop="type">
      <el-input v-model="dataForm.type" placeholder="帖子类型：1 图文 ，2视频 ，3文章，4投票"></el-input>
    </el-form-item>
    <el-form-item label="地址名称" prop="address">
      <el-input v-model="dataForm.address" placeholder="地址名称"></el-input>
    </el-form-item>
    <el-form-item label="经度" prop="longitude">
      <el-input v-model="dataForm.longitude" placeholder="经度"></el-input>
    </el-form-item>
    <el-form-item label="纬度" prop="latitude">
      <el-input v-model="dataForm.latitude" placeholder="纬度"></el-input>
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
          uid: '',
          topicId: '',
          discussId: '',
          voteId: '',
          title: '',
          content: '',
          media: '',
          readCount: '',
          postTop: '',
          type: '',
          address: '',
          longitude: '',
          latitude: '',
          createTime: ''
        },
        dataRule: {
          uid: [
            { required: true, message: '用户id不能为空', trigger: 'blur' }
          ],
          topicId: [
            { required: true, message: '圈子id不能为空', trigger: 'blur' }
          ],
          discussId: [
            { required: true, message: '话题id不能为空', trigger: 'blur' }
          ],
          voteId: [
            { required: true, message: '投票id不能为空', trigger: 'blur' }
          ],
          title: [
            { required: true, message: '标题不能为空', trigger: 'blur' }
          ],
          content: [
            { required: true, message: '内容不能为空', trigger: 'blur' }
          ],
          media: [
            { required: true, message: '文件不能为空', trigger: 'blur' }
          ],
          readCount: [
            { required: true, message: '浏览量不能为空', trigger: 'blur' }
          ],
          postTop: [
            { required: true, message: '置顶不能为空', trigger: 'blur' }
          ],
          type: [
            { required: true, message: '帖子类型：1 图文 ，2视频 ，3文章，4投票不能为空', trigger: 'blur' }
          ],
          address: [
            { required: true, message: '地址名称不能为空', trigger: 'blur' }
          ],
          longitude: [
            { required: true, message: '经度不能为空', trigger: 'blur' }
          ],
          latitude: [
            { required: true, message: '纬度不能为空', trigger: 'blur' }
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
              url: this.$http.adornUrl(`/admin/post/info/${this.dataForm.id}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.uid = data.post.uid
                this.dataForm.topicId = data.post.topicId
                this.dataForm.discussId = data.post.discussId
                this.dataForm.voteId = data.post.voteId
                this.dataForm.title = data.post.title
                this.dataForm.content = data.post.content
                this.dataForm.media = data.post.media
                this.dataForm.readCount = data.post.readCount
                this.dataForm.postTop = data.post.postTop
                this.dataForm.type = data.post.type
                this.dataForm.address = data.post.address
                this.dataForm.longitude = data.post.longitude
                this.dataForm.latitude = data.post.latitude
                this.dataForm.createTime = data.post.createTime
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
              url: this.$http.adornUrl(`/admin/post/${!this.dataForm.id ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'id': this.dataForm.id || undefined,
                'uid': this.dataForm.uid,
                'topicId': this.dataForm.topicId,
                'discussId': this.dataForm.discussId,
                'voteId': this.dataForm.voteId,
                'title': this.dataForm.title,
                'content': this.dataForm.content,
                'media': this.dataForm.media,
                'readCount': this.dataForm.readCount,
                'postTop': this.dataForm.postTop,
                'type': this.dataForm.type,
                'address': this.dataForm.address,
                'longitude': this.dataForm.longitude,
                'latitude': this.dataForm.latitude,
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
