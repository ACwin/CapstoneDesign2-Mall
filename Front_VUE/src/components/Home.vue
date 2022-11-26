<template>
  <el-container class="home_container">
    <!-- 头部区 -->
    <el-header>
      <div>
        <img src="../assets/management.png" alt="" />
        <span>E-commerce background management system</span>
      </div>
      <el-button type="info" @click="logout">logout</el-button>
    </el-header>
    <!-- 主体区 -->
    <el-container>
      <!-- 侧边栏 -->
      <el-aside :width="isCollapse ? '64px' : '200px'">
          <div class="toggle-button" @click="toggleCollapse()">|||</div>
        <!-- 侧边栏菜单区 -->
       <!-- router :指定router,通过index绑定的值来跳转 -->
        <el-menu
          background-color="#333744"
          text-color="#fff"
          active-text-color="#409BFF"
          unique-opened
          :collapse="isCollapse"
          :collapse-transition="false"
          router 
          :default-active='$route.path'
        >
          <!-- 一级菜单 每个菜单必须有自己唯一的index 只接受字符串-->
          <el-submenu
            :index="item.id + ''"
            v-for="item in menulist"
            :key="item.id"
          >
            <!-- 一级菜单模板区 -->
            <template slot="title">
              <i :class="iconsObj[item.id]"></i>
              <span>{{ item.authName }}</span>
            </template>
            <!-- 二级菜单 -->
            <el-menu-item
              :index="'/' + subItem.path"
              v-for="subItem in item.children"
              :key="subItem.id"
            >
              <!-- 二级菜单模板区 -->
              <template slot="title">
                <i class="el-icon-menu"></i>
                <span>{{ subItem.authName }}</span>
              </template>
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <!-- 右侧主体 -->
      <el-main>
        <router-view></router-view>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>


export default {
  data() {
    return {
      menulist: [
  {
    "id": 125,
    "authName": "User management",
    "path": "users",
    "children": [
      {
        "id": 110,
        "authName": "User Information",
        "path": "users",
        "children": [],
        "order": null
      }
    ],
    "order": 1
  },

  {
    "id": 101,
    "authName": "Goods management",
    "path": "goods",
    "children": [
      {
        "id": 104,
        "authName": "List of goods",
        "path": "goods",
        "children": [],
        "order": 1
      },
      {
        "id": 115,
        "authName": "Cart list",
        "path": "cart",
        "children": [],
        "order": 2
      },
      {
        "id": 121,
        "authName": "Product category",
        "path": "categories",
        "children": [],
        "order": 3
      }
    ],
    "order": 3
  },
  {
    "id": 102,
    "authName": "Order management",
    "path": "orders",
    "children": [
      {
        "id": 107,
        "authName": "List of orders",
        "path": "orders",
        "children": [],
        "order": null
      }
    ],
    "order": 4
  },
  
],
      iconsObj: {
        125: "iconfont icon-Usergroup",
        103: "iconfont icon-quanxian",
        101: "iconfont icon-shangpin",
        102: "iconfont icon-dingdan",
        145: "iconfont icon-shuju",
      },
      isCollapse: false
    };
  },
  created() {
    // this.getMenuList();
  },
  methods: {
    async logout() {
      //退出原理：清空token 重定向到登录页
      window.sessionStorage.clear();
      const { data: res } = await this.$http.post("user/logout");
      if (res.status != 10000) return this.$message.error("log out failed");
          //  弹窗提示
          this.$message.success("log out successful");
        
      
      this.$router.push('/login');
    },
    async getMenuList() {
      const { data: res } = await this.$http.get("menus");
      console.log(res)

      this.menulist = res.data;
      console.log(this.menulist)
    
     
   }
    
    },
    // 点击按钮实现菜单的折叠与展开
    toggleCollapse(){
        this.isCollapse = !this.isCollapse
    }
  }
;
</script>
<style>
.home_container {
  height: 100%;
}
.el-header {
  display: flex;
  justify-content: space-between;
  background-color: #373d41;
  padding-left: 0;
  align-items: center;
}

.el-header img {
  width: 50px;
  height: 50px;
}
.el-header span {
  color: #fff;
  font-size: 15px;
  margin-left: 10px;
}

.el-aside {
  background-color: #333744;
}
.el-aside .el-menu {
    border-right: 0;
}
.el-main {
  background-color: #eaedf1;
}
.iconfont {
    margin-right: 10px;
}
.toggle-button {
    background-color: #4A5064;
    color: #fff;
    font-size: 10px;
    line-height: 24px;
    text-align: center;
    letter-spacing: 0.2em;
    cursor: pointer
}
</style>