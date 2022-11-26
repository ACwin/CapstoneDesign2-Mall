<template>
  <div class="login_container">
    <div class="login_box">
      <div class="avatar_box">
        <img src="../assets/my_log.png" alt="" />
      </div>
      <el-form
        ref="loginFormRef"
        :model="loginForm"
        :rules="loginFormRules"
        label-width="0px"
        class="login_form"
      >
      
        <el-form-item prop="username">
          <el-input
            v-model="loginForm.username"
            prefix-icon="iconfont icon-caidan07"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="loginForm.password"
            type="password"
            prefix-icon="iconfont icon-xxhdpiCombinedShape"
          ></el-input>
        </el-form-item>
        <!-- 按钮区 -->
        <el-form-item class="btns">
          <el-button type="primary" @click="mallIn" >Mall homepage</el-button>
          <!-- <el-button type="primary" @click="adminlogin">管理员登录</el-button> -->
          <el-button type="primary" @click="login"> user login</el-button>
          <!-- <el-button type="info" @click="resetLoginForm()">重置</el-button> -->
          <el-button type="info" @click="register">register</el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      // 表单绑定对象
      loginForm: {
        // 在输入框中双向绑定属性
        username: "mumumu",
        password: "12345678",
      },
      // 表单验证规则对象
      loginFormRules: {
        // 验证用户名是否合法
        username: [
          { required: true, message: "Please enter a username", trigger: "blur" },
          {
            min: 3,
            max: 10,
            message: "The length is between 3 and 10 characters",
            trigger: "blur",
          },
        ],
        // 验证密码是否合法
        password: [
          { required: true, message: "Please enter a password", trigger: "blur" },
          { min: 3, max: 15, message: "Length: Between 3 and 15 characters", trigger: "blur" },
        ],
      },
    };
  },
  methods: {
    resetLoginForm() {
      this.$refs.loginFormRef.resetFields();
    },
    // 表单预验证,valid为bool值
    login() {
      this.$refs.loginFormRef.validate(async (valid) => {
        if (!valid) return;
        const{ data : res}=await this.$http.post("login", this.loginForm);
          console.log(res)
          // if (res.meta.status != 200) return this.$message.error("登录失败");
          if (res.status != 10000) return this.$message.error("Login failed");
          //  弹窗提示
          this.$message.success("Login successfully");
          //  将登录成功之后的token，保存到客户端的sessionStorage中
          // 项目中除了登录之外的api接口，必须在登录之后才能访问
          // token只应在当前网站打开期间生效，所以将token保存在sessionStorage中
          // console.log(res);
          window.sessionStorage.setItem("token", res.data.token);
          this.$router.push({name:'Mall',params: {hasUser:1,username:this.loginForm.username}});
          
        
      },
      
      
      );
    },
    adminlogin() {
      this.$refs.loginFormRef.validate(async (valid) => {
        if (!valid) return;
        const{ data : res}=await this.$http.post("adminLogin", this.loginForm);
          console.log(res)
          // if (res.meta.status != 200) return this.$message.error("登录失败");
          if (res.status != 10000) return this.$message.error("Not an administrator, login failed");
          //  弹窗提示
          this.$message.success("Login successfully");
          //  将登录成功之后的token，保存到客户端的sessionStorage中
          // 项目中除了登录之外的api接口，必须在登录之后才能访问
          // token只应在当前网站打开期间生效，所以将token保存在sessionStorage中
          // console.log(res);
          window.sessionStorage.setItem("token", res.data.token);
          this.$router.push('/home');
          // 通过编程式导航跳转到后台主页，路由地址是/home
        
      },
    
      
      );
    },
    register() {
      this.$refs.loginFormRef.validate(async (valid) => {
        if (!valid) return;
        const{ data : res}=await this.$http.post("register", this.loginForm);
          console.log(res)
          // if (res.meta.status != 200) return this.$message.error("登录失败");
          if (res.status != 10000) return this.$message.error("Registration failed");
          //  弹窗提示
          this.$message.success("Registration successful");
          //  将登录成功之后的token，保存到客户端的sessionStorage中
          // 项目中除了登录之外的api接口，必须在登录之后才能访问
          // token只应在当前网站打开期间生效，所以将token保存在sessionStorage中
          // console.log(res);
          window.sessionStorage.setItem("token", res.data.token);
          
          // 通过编程式导航跳转到后台主页，路由地址是/home
        
      },
      
      
      );
    },
    mallIn() {
            this.$router.push('/mall');
    }
  },
};
</script>

<style>
.login_container {
  background-image:url('../assets/bg.jpeg');
  height: 100%;
}
.login_box {
  width: 450px;
  height: 300px;
  background-color: rgb(247, 248, 251);
  border-radius: 3px;
  position: absolute;
  left: 50%;
  top: 50%;
  transform: translate(-50%, -50%);
}
.avatar_box {
  height: 130px;
  width: 130px;
  border: 1px solid #eee;
  border-radius: 50%;
  padding: 10px;
  box-shadow: 0 0 10px #ddd;
  position: absolute;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: #fff;
}
.avatar_box img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  background-color: #eee;
}
.btns {
  display: flex;
  justify-content: flex-end;
}
.login_form {
  width: 100%;
  position: absolute;
  bottom: 0;
  padding: 0 20px;
  box-sizing: border-box;
}
</style>