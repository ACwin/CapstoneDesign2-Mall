<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">Home page</el-breadcrumb-item>
      <el-breadcrumb-item>User management</el-breadcrumb-item>
      <el-breadcrumb-item>User Information</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <!-- 栅格布局  搜索与添加区域-->
      <el-row :gutter="10">

        <el-col :span="4">
          <el-button type="primary" @click="addDialogVisible = true"
            >Modify the personality signature</el-button
          >
        </el-col>
      </el-row>

      <!-- 用户列表区域 -->
      <el-table :data="userList" border stripe>
        <el-table-column label="User ID" prop="id"></el-table-column>
        <el-table-column label="name" prop="username"></el-table-column>
        <el-table-column label="Personalized signature" prop="personalizedSignature"></el-table-column>
        <el-table-column label="Role (1 for normal user, 2 for administrator)" prop="role"></el-table-column>

      </el-table>


    </el-card>

    <!-- 改变个性签名的对话框 -->
    <el-dialog
      title="Modify the personality signature"
      :visible.sync="addDialogVisible"
      width="50%"
      @close="addDialogClosed"
    >
      <!-- 主题区 -->
      <el-form
        :model="addForm"
        :rules="addFormRules"
        ref="ruleForm"
        label-width="70px"
      >
        <el-form-item label="Personalized signature" prop="personalizedSignature">
          <el-input v-model="addForm.personalizedSignature"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">Elimination</el-button>
        <el-button type="primary" @click="addUser">Are you sure </el-button>
      </span>
    </el-dialog>


  </div>
</template>

<script>
export default {
  data() {

    return {
      // 获取用户列表的参数
      queryInfo: {
        query: "",
        pagenum: 1,
        pagesize: 2,
      },
      userList: [],
      total: 0,
      // 控制添加用户对话框的显示与隐藏
      addDialogVisible: false,
      // 添加用户的表单数据
      addForm: {
        personalizedSignature: "",
      },
      // 添加表单的验证规则对象
      addFormRules: {
        personalizedSignature: [
          { required: true, message: "Please enter a personalized signature", trigger: blur },
          {
            min: 3,
            max: 20,
            message: "The length of the signature is 3~20 characters",
            trigger: "blur",
          },
        ]
      },
      // 控制修改用户对话框的显示与隐藏
      editDialogVisible: false,
      editForm: "",
      setRoleDialog: false,

    };
  },
  created() {
    this.getUserList();
  },
  methods: {
    //  async getUserList() {
    //   const { data: res } = await this.$http.get("users", {
    //     params: this.queryInfo,
    //   });
    //   if (res.meta.status !== 200) {
    //     return this.$message.error("获取用户列表失败");
    //   }
    //   this.userList = res.data.users;
    //   this.total = res.data.total;
    //   console.log(res);
    // }
    async getUserList() {
      const { data: res } = await this.$http.get("user");
      console.log(res);
      // this.userList = res.data.users;
      // this.total = res.data.total;
      var a = new Array();
      a.push(res.data)
      this.userList = a;
      console.log(this.userList)

    },
 

    
    // 监听添加用户对话框的关闭事件
    addDialogClosed() {
      this.$refs.ruleForm.resetFields();
    },
    // 点击按钮，添加新用户
    addUser() {
      this.$refs.ruleForm.validate(async (valid) => {
        if (!valid) return;
        // 没问题的话就发起网络请求
        const { data: res } = await this.$http.post("user/update", this.addForm);
        if (res.status != 10000) {
          return this.$message.error("Failed to modify personality signature！");
        }
        this.$message.success("The custom signature was modified successfully");
        this.addDialogVisible = false;

        this.getUserList();
      });
      // 展示编辑用户的对话框
    },


    // 点击按钮，分配角色
    savaRoleInfo() {
      if(!this.selectRoleId) {
        return this.$message.error('Select the role you want to assign')
      }
      this.$http.put(`users/${this.userInfo.id}/role`,{rid:this.selectRoleId}).then(res=>{
        if(res.data.meta.status!==200) {
          return this.$message.error('Failed to update role')
        }
        console.log(res.data);
        this.$message.success('The role update succeeded')
        this.getUserList()
        this.setRoleDialog=false
      })
    }
  },
};
</script>

<style>
</style>