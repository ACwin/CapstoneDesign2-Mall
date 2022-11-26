<template>
  <div class="home" id="home" name="home">
     <el-container>
      
        <!-- 顶部导航栏 -->
      <div class="topbar">
        <div class="nav">
          <ul>
            <li v-if="!this.hasUser">
              <el-button type="text" @click="login2">Administrator login</el-button>
              <el-button type="text" @click="login">login</el-button>
              <span class="sep">|</span>
              <el-button type="text" @click="login">register</el-button>
            </li>
            <li v-else>
              Welcome
              <el-popover placement="top" width="180" v-model="visible">
                <p>Are you sure you want to log out?</p>
                <div style="text-align: right; margin: 10px 0 0">
                  <el-button size="mini" type="text" @click="visible = false">Cancel</el-button>
                  <el-button type="primary" size="mini" @click="logout">OK</el-button>
                </div>
                <el-button type="text" slot="reference">{{this.$route.params.username}}</el-button>
              </el-popover>
            </li>
            <li>
              <router-link :to="{path:'/myorder',query: {hasUser:hasUser,username:username,} }">My Orders</router-link>
            </li>

            <li :class="getNum > 0 ? 'shopCart-full' : 'shopCart'">
              <router-link :to="{path:'/shoppingCart',query: {hasUser:hasUser,username:username} }">
                <i class="el-icon-shopping-cart-full"></i> Shopping Cart
              </router-link>
            </li>
          </ul>
        </div>
      </div>

       <!-- 顶栏容器 -->
      <el-header>
        <el-menu
          :default-active="activeIndex"
          class="el-menu-demo"
          mode="horizontal"                 
          active-text-color="#409eff"
          router
        >
          <div class="logo">
            <router-link to="/">
              <img width="80px"  src="../assets/my_log.png" alt />
            </router-link>
          </div>                                                
          <el-menu-item >Focus on fresh products</el-menu-item>              
      

          <div class="so">
            <el-input placeholder="Please enter your search" v-model="search">
              <el-button slot="append" icon="el-icon-search" @click="searchClick"></el-button>
            </el-input>
          </div>
        </el-menu>
      </el-header>

    <!--主体 -->
    <el-main>
    <!-- 轮播图 -->
    <div class="block" >
      <el-carousel height="460px">
        <el-carousel-item v-for="item in imgUrl" :key="item.index">
          <img style="height:460px; margin-left:450px" :src="item"  />
          
        </el-carousel-item>
      </el-carousel>
    </div>
    <!-- 轮播图END -->
    <div class="main-box">
      <div class="main">
        <!-- 商品展示区域 -->
        <div class="phone">
          <div class="box-hd">
            <div class="title">commodity</div>
          </div>
          <div class="box-bd">

            <div class="list">
              <MyList :list="goodsList" :isMore="true" :hasUser="hasUser" :username="username"></MyList>
            </div>
          </div>
        </div>
        
        


      </div>
    </div>
    </el-main>
     </el-container>
  </div>
</template>

<script>
import MyList from '../components/MyList.vue' 
import p1 from '../assets/imgs/lb01.png' 
import p2 from '../assets/imgs/lb02.png' 
import p3 from '../assets/imgs/lb03.png' 
import p4 from '../assets/imgs/lb04.png' 
import p5 from '../assets/imgs/lb05.png' 
import p6 from '../assets/imgs/lb06.png' 
import p7 from '../assets/imgs/lb07.png' 
export default {
  
  data() {
    return {
      carousel: "", // 轮播图数据
      phoneList: "", // 手机商品列表
      miTvList: "", // 小米电视商品列表
      applianceList: "", // 家电商品列表
      applianceHotList: "", //热门家电商品列表
      accessoryList: "", //配件商品列表
      accessoryHotList: "", //热门配件商品列表
      goodsList: "",
      protectingShellList: "", // 保护套商品列表
      chargerList: "", //充电器商品列表
      applianceActive: 1, // 家电当前选中的商品分类
      accessoryActive: 1 ,// 配件当前选中的商品分类
       queryInfo: {
        pagenum: 1,
        pagesize: 7,
      },
      hasUser:0, //没人登录
      username:"",
      activeIndex: "", // 头部导航栏选中的标签
      search: "", // 搜索条件
      register: false, // 是否显示注册组件
      visible: false, // 是否退出登录
      picList:['../assets/imgs/lb01.png','../assets/imgs/lb02.png'],
      imgUrl:[p1,p2,p3,p4,p5,p6,p7]
    };
  },
  
  created() {
    this.getGoodList();
    this.hasUser = this.$route.params.hasUser;
    this.username = this.$route.params.username;
  },
  components: {MyList},  
  methods: {
   getGoodList() {
      console.log(this.queryInfo.pagesize)
      this.$http.post("product/list",  this.queryInfo ).then((res) => {
        if (res.data.status !== 10000) {
          return this.$message.error("Failed to get list of products");
        }
        
        this.goodsList = res.data.data.list;
        this.total = res.data.data.total;
         console.log(this.goodsList)
      });
    },
    login2() {
     this.$router.push('/login')
    },
    login() {
     this.$router.push('/login2')
    },
    async logout(){
      window.sessionStorage.clear();
      const { data: res } = await this.$http.post("user/logout");
      if (res.status != 10000) return this.$message.error("log out failed");
          //  弹窗提示
          this.$message.success("log out successful");
        
      
      this.$router.push("/login2");
    },
        // 点击搜索按钮
    searchClick() {
      if (this.search != "") {
        // 跳转到全部商品页面,并传递搜索条件
        this.$router.push({ path: "/goodsall", query: { hasUser:this.hasUser,username:this.username,search: this.search } });
        this.search = "";
      }
    }
   
  }
};
</script>

<style scoped>
@import "../assets/css/index.css";
/* 全局CSS */
* {
  padding: 0;
  margin: 0;
  border: 0;
  list-style: none;
}
#app .el-header {
  padding: 0;
}
#app .el-main {
  min-height: 300px;
  padding: 20px 0;
}
#app .el-footer {
  padding: 0;
}
a,
a:hover {
  text-decoration: none;
}
/* 全局CSS END */

/* 顶部导航栏CSS */
.topbar {
  height: 40px;
  background-color: #3d3d3d;
  margin-bottom: 20px;
}
.topbar .nav {
  width: 1225px;
  margin: 0 auto;
}
.topbar .nav ul {
  float: right;
}
.topbar .nav li {
  float: left;
  height: 40px;
  color: #b0b0b0;
  font-size: 14px;
  text-align: center;
  line-height: 40px;
  margin-left: 20px;
}
.topbar .nav .sep {
  color: #b0b0b0;
  font-size: 12px;
  margin: 0 5px;
}
.topbar .nav li .el-button {
  color: #b0b0b0;
}
.topbar .nav .el-button:hover {
  color: #fff;
}
.topbar .nav li a {
  color: #b0b0b0;
}
.topbar .nav a:hover {
  color: #fff;
}
.topbar .nav .shopCart {
  width: 120px;
  background: #424242;
}
.topbar .nav .shopCart:hover {
  background: #fff;
}
.topbar .nav .shopCart:hover a {
  color: #ff6700;
}
.topbar .nav .shopCart-full {
  width: 120px;
  background: #ff6700;
}
.topbar .nav .shopCart-full a {
  color: white;
}
/* 顶部导航栏CSS END */

/* 顶栏容器CSS */
.el-header .el-menu {
  max-width: 1225px;
  margin: 0 auto;
}
.el-header .logo {
  height: 60px;
  width: 189px;
  float: left;
  margin-right: 100px;
}
.el-header .so {
  margin-top: 10px;
  width: 300px;
  float: right;
}
/* 顶栏容器CSS END */

/* 底栏容器CSS */
.footer {
  width: 100%;
  text-align: center;
  background: #2f2f2f;
  padding-bottom: 20px;
}
.footer .ng-promise-box {
  border-bottom: 1px solid #3d3d3d;
  line-height: 145px;
}
.footer .ng-promise-box {
  margin: 0 auto;
  border-bottom: 1px solid #3d3d3d;
  line-height: 145px;
}
.footer .ng-promise-box .ng-promise p a {
  color: #fff;
  font-size: 20px;
  margin-right: 210px;
  padding-left: 44px;
  height: 40px;
  display: inline-block;
  line-height: 40px;
  text-decoration: none;
  background: url("../assets/imgs/us-icon.png") no-repeat left 0;
}
.footer .github {
  height: 50px;
  line-height: 50px;
  margin-top: 20px;
}
.footer .github .github-but {
  width: 50px;
  height: 50px;
  margin: 0 auto;
  background: url("../assets/imgs/github.png") no-repeat;
}
.footer .mod_help {
  text-align: center;
  color: #888888;
}
.footer .mod_help p {
  margin: 20px 0 16px 0;
}

.footer .mod_help p a {
  color: #888888;
  text-decoration: none;
}
.footer .mod_help p a:hover {
  color: #fff;
}
.footer .mod_help p span {
  padding: 0 22px;
}
</style>