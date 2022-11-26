<!--
 * @Description: 列表组件，用于首页、全部商品页面的商品列表
 * @Author: YANG
 * @Date: 2022-11-07 16:23:00

 -->
<template>
<div>
        <!-- 分类标签 -->
    <!-- <div class="nav">
      <div class="product-nav">
        <div class="title">分类</div>
        <el-tabs v-model="activeName" type="card">
          <el-tab-pane
            v-for="item in totalCate"
            :key="item.category_id"
            :label="item.category_name"
            :name="''+item.category_id"
          />
        </el-tabs>
      </div>
    </div> -->
    <!-- 分类标签END -->
    <!-- <div class="list-detail">
      <list :list="category"></list>
    </div> -->

    <el-menu   :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
  <el-menu-item index="1111111111">Product category:</el-menu-item>
  <el-submenu v-for="item in category" :key="item.id"  index="2">
    <template slot="title"  :name="item.name">{{item.name}}</template>
    <el-menu-item v-for="items in item.childCategory" :key="items.id" @click="nu(items.id)">{{items.name}}</el-menu-item>
  </el-submenu>
</el-menu>
    <div id="myList" class="myList">
    <ul>
      <li v-for="item in this.productListRst" :key="item.id">
        <router-link :to="{ path: '/goods/details', query: {productID:item.id,hasUser:hasUser,username:username} }">
          <img :src="$target +item.image" alt />
          <h2>{{item.name}}</h2>
          <h3>{{item.product_title}}</h3>
          <p>
            <span>{{item.price}}元</span>
          </p>
        </router-link>
      </li>

    </ul>
  
  </div>
  <div> <el-button type="primary" icon="el-icon-arrow-left" round style=" position:fixed;margin-top: 40%" @click="goback()">返回</el-button></div>
</div>

  
</template>
<script>
  // import List from "../List.vue";
export default {

  name: "Goodsall",
// components: { List },
  // isMore为是否显示“浏览更多”
  data() {
    return {
      productListRst:'',
      productList:"",
      hasUser:"",
      username:"",
      category:"",
      totalCate:[],
      currentCate:[],
    };
  },
  created() {
    
    this.hasUser = this.$route.query.hasUser
    this.username =this.$route.query.username
    this.search =this.$route.query.search
    this.getGoodList();
    this.getCategory();
  },

  computed: {
    // 通过list获取当前显示的商品的分类ID，用于“浏览更多”链接的参数
    categoryID: function() {
      let categoryID = [];
      if (this.list != "") {
        for (let i = 0; i < this.list.length; i++) {
          const id = this.list[i].categoryId;
          if (!categoryID.includes(id)) {
            categoryID.push(id);
          }
        }
      }
      return categoryID;
    }
  },
  methods: {
    getCategory(){
      this.$http.get("category/list" ).then((res) => {
        if (res.data.status !== 10000) {
          return this.$message.error("Failed to get list of products");
        }
     
          this.category = res.data.data
          
          for( var i=0;i<this.category.length;i++){
            // console.log(this.category[i].id,this.category[i].name)
            this.totalCate.push({category_id:this.category[i].id,category_name:this.category[i].name})
            for(var j=0;j<this.category[i].childCategory.length;j++){
              // console.log(this.category[i].childCategory[j].id,this.category[i].childCategory[j].name)
              this.totalCate.push({category_id:this.category[i].childCategory[j].id,category_name:this.category[i].childCategory[j].name})
                for(var k=0;k<this.category[i].childCategory[j].childCategory[k];k++){
                  this.totalCate.push({category_id:this.category[i].childCategory[j].childCategory[k].id,category_name:this.category[i].childCategory[j].childCategory[k].name})
                }
            }
          }
      

  
      });

    },
    getGoodList() {
      this.$http.post("product/list",  {"pagenum": 1,
        "pagesize": 1000000,} ).then((res) => {
        if (res.data.status !== 10000) {
          return this.$message.error("Failed to get list of products");
        }
        this.productList = res.data.data.list;
        console.log("commodity")
        console.log(res.data.data.list)
        this.searchInputChange(this.search)
        if(this.productListRst.length===0) {
          this.$message.error("No related products were found")
        }
      });
    },
     goback(){
      this.$router.push({name:'Mall',params: {hasUser:this.hasUser,username:this.username}});
    },
    searchInputChange(value) {
      // 若未输入值，则展示所有数据
      if(null === value || undefined === value){
        this.productListRst = this.productList;
      } else {
        this.productListRst = []; // 结果列表置空
        let regStr =  '';
        // 初始化正则表达式
        for(let i=0; i<value.length; i++){
          regStr = regStr + '(' + value[i] + ')([\\s]*)'; //跨字匹配
        }
        let reg = new RegExp(regStr);
        for(let i=0; i<this.productList.length; i++){
          let name = this.productList[i].name; //按照名字匹配
          let regMatch = name.match(reg);
          if(null !== regMatch) {// 将匹配的数据放入结果列表中
             this.productListRst.push(this.productList[i]);
          }
          
        }
        
      }
   
    },
     handleSelect(key, keyPath) {
        console.log(key, keyPath);
      },
      nu(id){
        console.log(id)
        for(var i = 0;i<this.totalCate.length;i++){
          if(this.totalCate[i]["category_id"]===id){
            this.searchInputChange(this.totalCate[i]["category_name"])
          }
        }
      }
  }
};
</script>
<style scoped>
.myList ul li {
  z-index: 1;
  float: left;
  width: 234px;
  height: 280px;
  padding: 10px 0;
  margin: 0 0 14.5px 13.7px;
  background-color: white;
  -webkit-transition: all 0.2s linear;
  transition: all 0.2s linear;
  position: relative;
}
.myList ul li:hover {
  z-index: 2;
  -webkit-box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.1);
  -webkit-transform: translate3d(0, -2px, 0);
  transform: translate3d(0, -2px, 0);
}
.myList ul li img {
  display: block;
  width: 160px;
  height: 160px;
  background: url(../../assets/imgs/placeholder.png) no-repeat 50%;
  margin: 0 auto;
}
.myList ul li h2 {
  margin: 25px 10px 0;
  font-size: 14px;
  font-weight: 400;
  color: #333;
  text-align: center;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}
.myList ul li h3 {
  margin: 5px 10px;
  height: 18px;
  font-size: 12px;
  font-weight: 400;
  color: #b0b0b0;
  text-align: center;
  text-overflow: ellipsis;
  white-space: nowrap;
  overflow: hidden;
}
.myList ul li p {
  margin: 10px 10px 10px;
  text-align: center;
  color: #ff6700;
}
.myList ul li p .del {
  margin-left: 0.5em;
  color: #b0b0b0;
  text-decoration: line-through;
}
.myList #more {
  text-align: center;
  line-height: 280px;
}
.myList #more a {
  font-size: 18px;
  color: #333;
}
.myList #more a:hover {
  color: #ff6700;
}
.myList ul li .delete {
  position: absolute;
  top: 10px;
  right: 10px;
  display: none;
}
.myList ul li:hover .delete {
  display: block
}
.myList ul li .delete:hover {
  color: #ff6700;
}
</style>