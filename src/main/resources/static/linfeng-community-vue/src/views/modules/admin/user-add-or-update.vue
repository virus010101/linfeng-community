<template>
  <el-dialog
    :title="!dataForm.uid ? '新增' : '修改'"
    :close-on-click-modal="false"
    :visible.sync="visible">
    <el-form :model="dataForm"  ref="dataForm" @keyup.enter.native="dataFormSubmit()" label-width="80px">
    <!-- <el-form-item label="手机号" prop="mobile">
      <el-input v-model="dataForm.mobile" placeholder="手机号"></el-input>
    </el-form-item>
    <el-form-item label="用户名" prop="username">
      <el-input v-model="dataForm.username" placeholder="用户名"></el-input>
    </el-form-item> -->
    <!-- <el-form-item label="密码" prop="password">
      <el-input v-model="dataForm.password" placeholder="密码"></el-input>
    </el-form-item> -->
    <!-- <el-form-item label="用户组" prop="groupId">
      <el-input v-model="dataForm.groupId" placeholder="用户组"></el-input>
    </el-form-item>
    <el-form-item label="头像" prop="avatar">
      <el-input v-model="dataForm.avatar" placeholder="头像"></el-input>
    </el-form-item>
    <el-form-item label="性别(0未知，1男，2女)" prop="gender">
      <el-input v-model="dataForm.gender" placeholder="性别(0未知，1男，2女)"></el-input>
    </el-form-item>
    <el-form-item label="省份" prop="province">
      <el-input v-model="dataForm.province" placeholder="省份"></el-input>
    </el-form-item>
    <el-form-item label="城市" prop="city">
      <el-input v-model="dataForm.city" placeholder="城市"></el-input>
    </el-form-item>
    <el-form-item label="小程序openid" prop="openid">
      <el-input v-model="dataForm.openid" placeholder="小程序openid"></el-input>
    </el-form-item>
    <el-form-item label="公众号openid" prop="mpOpenid">
      <el-input v-model="dataForm.mpOpenid" placeholder="公众号openid"></el-input>
    </el-form-item>
    <el-form-item label="unionid" prop="unionid">
      <el-input v-model="dataForm.unionid" placeholder="unionid"></el-input>
    </el-form-item> -->
    <el-form-item label="状态" prop="status">
        <el-radio-group v-model="dataForm.status">
          <el-radio :label="1">禁用</el-radio>
          <el-radio :label="0">正常</el-radio>
        </el-radio-group>
    </el-form-item>
    <!-- <el-form-item label="个性签名" prop="intro">
      <el-input v-model="dataForm.intro" placeholder="个性签名"></el-input>
    </el-form-item>
    <el-form-item label="积分" prop="integral">
      <el-input v-model="dataForm.integral" placeholder="积分"></el-input>
    </el-form-item>
    <el-form-item label="最后登录ip" prop="lastLoginIp">
      <el-input v-model="dataForm.lastLoginIp" placeholder="最后登录ip"></el-input>
    </el-form-item>
    <el-form-item label="用户标签" prop="tagStr">
      <el-input v-model="dataForm.tagStr" placeholder="用户标签"></el-input>
    </el-form-item> -->
    <el-form-item label="类型" prop="type">
        <el-radio-group v-model="dataForm.type">
          <el-radio :label="1">官方账号</el-radio>
          <el-radio :label="0">普通用户</el-radio>
        </el-radio-group>
    </el-form-item>
    <!-- <el-form-item label="更新时间" prop="updateTime">
      <el-input v-model="dataForm.updateTime" placeholder="更新时间"></el-input>
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
          uid: 0,
          mobile: '',
          username: '',
          password: '',
          groupId: '',
          avatar: '',
          gender: '',
          province: '',
          city: '',
          openid: '',
          mpOpenid: '',
          unionid: '',
          status: '',
          intro: '',
          integral: '',
          lastLoginIp: '',
          tagStr: '',
          type: '',
          updateTime: '',
          createTime: ''
        }
       
      }
    },
    methods: {
      init (id) {
        this.dataForm.uid = id || 0
        this.visible = true
        this.$nextTick(() => {
          this.$refs['dataForm'].resetFields()
          if (this.dataForm.uid) {
            this.$http({
              url: this.$http.adornUrl(`/admin/user/info/${this.dataForm.uid}`),
              method: 'get',
              params: this.$http.adornParams()
            }).then(({data}) => {
              if (data && data.code === 0) {
                this.dataForm.mobile = data.user.mobile
                this.dataForm.username = data.user.username
                this.dataForm.password = data.user.password
                this.dataForm.groupId = data.user.groupId
                this.dataForm.avatar = data.user.avatar
                this.dataForm.gender = data.user.gender
                this.dataForm.province = data.user.province
                this.dataForm.city = data.user.city
                this.dataForm.openid = data.user.openid
                this.dataForm.mpOpenid = data.user.mpOpenid
                this.dataForm.unionid = data.user.unionid
                this.dataForm.status = data.user.status
                this.dataForm.intro = data.user.intro
                this.dataForm.integral = data.user.integral
                this.dataForm.lastLoginIp = data.user.lastLoginIp
                this.dataForm.tagStr = data.user.tagStr
                this.dataForm.type = data.user.type
                this.dataForm.updateTime = data.user.updateTime
                this.dataForm.createTime = data.user.createTime
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
              url: this.$http.adornUrl(`/admin/user/${!this.dataForm.uid ? 'save' : 'update'}`),
              method: 'post',
              data: this.$http.adornData({
                'uid': this.dataForm.uid || undefined,
                'mobile': this.dataForm.mobile,
                'username': this.dataForm.username,
                'password': this.dataForm.password,
                'groupId': this.dataForm.groupId,
                'avatar': this.dataForm.avatar,
                'gender': this.dataForm.gender,
                'province': this.dataForm.province,
                'city': this.dataForm.city,
                'openid': this.dataForm.openid,
                'mpOpenid': this.dataForm.mpOpenid,
                'unionid': this.dataForm.unionid,
                'status': this.dataForm.status,
                'intro': this.dataForm.intro,
                'integral': this.dataForm.integral,
                'lastLoginIp': this.dataForm.lastLoginIp,
                'tagStr': this.dataForm.tagStr,
                'type': this.dataForm.type,
                'updateTime': this.dataForm.updateTime,
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
