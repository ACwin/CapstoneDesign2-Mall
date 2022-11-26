<!--
 * @Description: 商品详情页面组件
 * @Author: YANG
 * @Date: 2022-10-16 20:20:26
 * @LastEditors: YANG

 -->
<template>
  <div id="details">
    <!-- 头部 -->
    <div class="page-header">
      <div class="title">
        <p>{{productDetails.name}}</p>
        <div class="list">
          <ul>
            <li>
              <router-link to>overview</router-link>
            </li>
            <li>
              <router-link to>parameter</router-link>
            </li>
            <li>
              <router-link to>User reviews</router-link>
            </li>
          </ul>
        </div>
      </div>
    </div>
    <!-- 头部END -->

    <!-- 主要内容 -->
    <div class="main">
      <!-- 左侧商品轮播图 -->
      <div class="block">
        <div>
          
          <img
            style="height:330px;weight:300px"
            :src="$target + productDetails.image"
            :alt="productDetails.detail"
          />
        </div>
         <div> <el-button type="primary" icon="el-icon-arrow-left" round style=" margin-top: 6%" @click="goback()">go back</el-button></div>
      </div>
      
      <!-- 左侧商品轮播图END -->

      <!-- 右侧内容区 -->
      <div class="content">
        <h1 class="name">{{productDetails.name}}</h1>
        <p class="intro">{{productDetails.detail}}</p>
        <p class="store">Higo Mall</p>
        <div class="price">
          <span>{{productDetails.price}}RMB</span>
          <span
            v-show="productDetails.price != productDetails.price"
            class="del"
          >{{productDetails.price}}RMB</span>
        </div>
        <div class="pro-list">
          <span class="pro-name">{{productDetails.name}}</span>
          <span class="pro-price">
            <span>{{productDetails.price}}RMB</span>
            <span
              v-show="productDetails.price != productDetails.price"
              class="pro-del"
            >{{productDetails.price}}RMB</span>
          </span>
          <p class="price-sum">total : {{productDetails.price}}RMB</p>
        </div>
       
        <!-- 内容区底部按钮 -->
        <div class="button">
          <el-button class="shop-cart" :disabled="dis" @click="addShoppingCart">Add to cart</el-button>
        </div>
        <!-- 内容区底部按钮END -->
        <div class="pro-policy">
          <ul>
            <li>
              <i class="el-icon-circle-check"></i> Higo Mall
            </li>
            <li>
              <i class="el-icon-circle-check"></i> Higo shipments
            </li>
            <li>
              <i class="el-icon-circle-check"></i> 7 days no reason to return
            </li>
            <li>
              <i class="el-icon-circle-check"></i> 7 days price protection
            </li>
          </ul>
        </div>
      </div>
      <!-- 右侧内容区END -->
    </div>
    <!-- 主要内容END -->
  </div>
</template>
<script>

export default {
  data() {
    return {
      dis: false, // 控制“加入购物车按钮是否可用”
      productID: "", // 商品id
      hasUser:"",//用户是否登录
      username:"",
      productDetails: "", // 商品详细信息
      productPicture: "" // 商品图片
    };
  },
  // 通过路由获取商品id
  created(){
    this.productID = this.$route.query.productID;
   this.hasUser = this.$route.query.hasUser
    this.username =this.$route.query.username
  },
  watch: {
    // 监听商品id的变化，请求后端获取商品数据
    productID: function(val) {
      this.getDetails(val);
      this.getDetailsPicture(val);
    }
  },
  methods: {
    // ...mapActions(["unshiftShoppingCart", "addShoppingCartNum"]),
    // 获取商品详细信息
    getDetails(val) {
      this.$http.post("product/detail",  {
          "id": val
        } ).then((res) => {
        if (res.data.status !== 10000) {
          return this.$message.error("Failed to get list of products");
        }
        console.log(res.data)
          this.productDetails = res.data.data;
       
      });
      
    },
    // 获取商品图片
    getDetailsPicture(val) {
      this.$axios
        .post("/api/product/getDetailsPicture", {
          productID: val
        })
        .then(res => {
          this.productPicture = res.data.ProductPicture;
        })
        .catch(err => {
          return Promise.reject(err);
        });
    },
    // 加入购物车
    addShoppingCart() {
      // 判断是否登录,没有登录则显示登录组件
      if (!this.hasUser) {
        this.$router.push('/login2')
        return;
      }
      this.$http
          .post("/cart/add" , {productId:this.productID,count:1})
          .then((res) => {
            if (res.data.status !== 10000) {
              return this.$message.error("Failed to add cart");
            }
            this.$message.success("Add cart successfully");
            this.getGoodList();
            this.editCateDialogVisible = false;
          })
    },
    goback(){
      this.$router.push({name:'Mall',params: {hasUser:this.hasUser,username:this.username}});
    }
  }
};
</script>
<style>
/* 头部CSS */
#details .page-header {
  height: 64px;
  margin-top: -20px;
  z-index: 4;
  background: #fff;
  border-bottom: 1px solid #e0e0e0;
  -webkit-box-shadow: 0px 5px 5px rgba(0, 0, 0, 0.07);
  box-shadow: 0px 5px 5px rgba(0, 0, 0, 0.07);
}
#details .page-header .title {
  width: 1225px;
  height: 64px;
  line-height: 64px;
  font-size: 18px;
  font-weight: 400;
  color: #212121;
  margin: 0 auto;
}
#details .page-header .title p {
  float: left;
}
#details .page-header .title .list {
  height: 64px;
  float: right;
}
#details .page-header .title .list li {
  float: left;
  margin-left: 20px;
}
#details .page-header .title .list li a {
  font-size: 14px;
  color: #616161;
}
#details .page-header .title .list li a:hover {
  font-size: 14px;
  color: #ff6700;
}
/* 头部CSS END */

/* 主要内容CSS */
#details .main {
  width: 1225px;
  height: 560px;
  padding-top: 30px;
  margin: 0 auto;
}
#details .main .block {
  float: left;
  width: 560px;
  height: 560px;
}
#details .el-carousel .el-carousel__indicator .el-carousel__button {
  background-color: rgba(163, 163, 163, 0.8);
}
#details .main .content {
  float: left;
  margin-left: 25px;
  width: 640px;
}
#details .main .content .name {
  height: 30px;
  line-height: 30px;
  font-size: 24px;
  font-weight: normal;
  color: #212121;
}
#details .main .content .intro {
  color: #b0b0b0;
  padding-top: 10px;
}
#details .main .content .store {
  color: #ff6700;
  padding-top: 10px;
}
#details .main .content .price {
  display: block;
  font-size: 18px;
  color: #ff6700;
  border-bottom: 1px solid #e0e0e0;
  padding: 25px 0 25px;
}
#details .main .content .price .del {
  font-size: 14px;
  margin-left: 10px;
  color: #b0b0b0;
  text-decoration: line-through;
}
#details .main .content .pro-list {
  background: #f9f9fa;
  padding: 30px 60px;
  margin: 50px 0 50px;
}
#details .main .content .pro-list span {
  line-height: 30px;
  color: #616161;
}
#details .main .content .pro-list .pro-price {
  float: right;
}
#details .main .content .pro-list .pro-price .pro-del {
  margin-left: 10px;
  text-decoration: line-through;
}
#details .main .content .pro-list .price-sum {
  color: #ff6700;
  font-size: 24px;
  padding-top: 20px;
}
#details .main .content .button {
  height: 55px;
  margin: 10px 0 20px 0;
}
#details .main .content .button .el-button {
  float: left;
  height: 55px;
  font-size: 16px;
  color: #fff;
  border: none;
  text-align: center;
}
#details .main .content .button .shop-cart {
  width: 340px;
  background-color: #ff6700;
}
#details .main .content .button .shop-cart:hover {
  background-color: #f25807;
}

#details .main .content .button .like {
  width: 260px;
  margin-left: 40px;
  background-color: #b0b0b0;
}
#details .main .content .button .like:hover {
  background-color: #757575;
}
#details .main .content .pro-policy li {
  float: left;
  margin-right: 20px;
  color: #b0b0b0;
}
/* 主要内容CSS END */
</style>