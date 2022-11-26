<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">Home page</el-breadcrumb-item>
      <el-breadcrumb-item>Goods management</el-breadcrumb-item>
      <el-breadcrumb-item>List of goods</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card>
           <el-row
        ><el-col
          ><el-button type="primary" @click="showAddCateDialog()"
            >Add products</el-button
          ></el-col
        ></el-row
      >

      <!-- table表格区域 -->
      <el-table :data="goodslist" border stripe>
        <el-table-column type="index"></el-table-column>
        <el-table-column label="Product Name" prop="name"></el-table-column>
        <el-table-column
          label="Commodity price (RMB)"
          prop="price"
          width="95px"
        ></el-table-column>
        <el-table-column
          label="Product details"
          prop="detail"
          width="305px"
        ></el-table-column>
        <el-table-column label="Creation time" prop="createTime" width="140px">
          <template slot-scope="scope">
            {{ scope.row.createTime | dateFormat }}
          </template>
        </el-table-column>
         <el-table-column label="Update time" prop="updateTime" width="140px">
          <template slot-scope="scope">
            {{ scope.row.updateTime | dateFormat }}
          </template>
        </el-table-column>
        <el-table-column label="operate" width="180px">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="editGoods(scope.row)"
              >edit</el-button
            >
            <el-button
              size="mini"
              type="danger"
              icon="el-icon-delete"
              @click="removeById(scope.row)"
              >Delete</el-button
            >
                        <el-button
              size="mini"
              type="warning"
              icon="el-icon-shopping-cart-2"
              @click="addToChart(scope.row.id)"
              >Add to cart</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pagenum"
        :page-size="queryInfo.pagesize"
        :page-sizes="[10,20, 30, 40]"
        layout="total, sizes,prev, pager, next,jumper"
        :total="total"
        background
      >
      </el-pagination>
    </el-card>
        <!-- 添加商品的对话框 -->
    <el-dialog
      title="Add products"
      :visible.sync="addCateDialogVisible"
      width="50%"
      @close="addCateDialogClosed()"
    >
      <!-- 主体区 -->
      <el-form
        :model="addForm"
        :rules="addFormRules"
        ref="addCateFromRef"
        label-width="100px"
        class="demo-ruleForm"
      >
      
        <el-form-item label-width="150px"  label="Product Name" prop="name">
              <el-input v-model="addForm.name"></el-input>
            </el-form-item>
            <el-form-item label-width="150px" label="Commodity price" prop="price">
              <el-input v-model="addForm.price" type="number"></el-input>
            </el-form-item>
            <el-form-item  label-width="150px"  label="Commodity category" prop="categoryId">
              <el-input v-model="addForm.categoryId" type="number"></el-input>
            </el-form-item>
            <el-form-item  label-width="150px"  label="Commodity stocks" prop="stock">
              <el-input v-model="addForm.stock" type="number"></el-input>
            </el-form-item>
            <el-form-item label-width="150px"  label="Commodity details" prop="detail">
              <el-input v-model="addForm.detail"></el-input>
            </el-form-item>
            <el-upload
  class="upload-demo"
  action="http://localhost:8888/api/admin/upload/file"
  :on-preview="handlePreview"
  :on-remove="handleRemove"
  :file-list="fileList"
  :on-success="handleSuccess"
  list-type="picture">
  <el-button size="small" type="primary">Click Upload product image</el-button>
  <div slot="tip" class="el-upload__tip">Only JPG/PNG files can be uploaded</div>
</el-upload>

      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addCateDialogVisible = false">cancel</el-button>
        <el-button type="primary" @click="addGoods()">OK</el-button>
      </span>
    </el-dialog>

     <!-- 编辑商品的对话框 -->
    <el-dialog
      title="Modify the classification"
      :visible.sync="editCateDialogVisible"
      width="50%"
    >
      <el-form
        ref="editFormRef"
        :model="showCatelist"
        label-width="100px"
        :rules="EditCateFromRules"
      >
        <el-form-item  label-width="150px" label="Commodity Name" prop="name">
          <el-input v-model="showCatelist.name"></el-input>
        </el-form-item>
            <el-form-item  label-width="150px"  label="Commodity price" prop="price" >
          <el-input v-model="showCatelist.price" type="number"></el-input>
        </el-form-item>
            <el-form-item label-width="150px"  label="Commodity details" prop="detail">
          <el-input v-model="showCatelist.detail"></el-input>
            </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editCateDialogVisible = false">cancel</el-button>
        <el-button type="primary" @click="editGoodsInfo">OK</el-button>
      </span>
    </el-dialog>
    
  </div>
</template>

<script>
export default {
  data() {
    return {
      queryInfo: {
        pagenum: 1,
        pagesize: 20,
      },
      goodslist: [],
      total: 0,
      addCateDialogVisible: false,
      editCateDialogVisible: false,
      addForm: {
        // post请求发送的数据
        name:"",
        price: 0,
        categoryId: 0,
       stock: 0,
        detail:"",
        image:""
      },
      addFormRules: {
        name: [
          { required: true, message: "Please enter a product name", trigger: "blur" },
        ],
        price: [
          { required: true, message: "Please enter the price of the item", trigger: "blur" },
        ],
        categoryId: [
          { required: true, message: "Please enter a product category", trigger: "blur" },
        ],
       stock: [
          { required: true, message: "Please enter commodity stocks", trigger: "blur" },
        ],
        detail: [
          { required: true, message: "Please select a product listing", trigger: "blur" },
        ],
      },
      showCatelist: {
        id: "",
        name: "",
       categoryId: "",
       price: "",
       stock: "",
       detail: "",
       
      },
      EditCateFromRules: {
        name: [{ required: true, message: "Please enter a product name", trigger: "blur" }],
      },
    };
  },
  created() {
    this.getGoodList();
  },
  methods: {
    // getGoodList() {
    //   this.$http.get("goods", { params: this.queryInfo }).then((res) => {
    //     if (res.data.meta.status !== 200) {
    //       return this.$message.error("获取商品列表失败");
    //     }
    //     console.log(res.data);
    //     this.goodslist = res.data.data.goods;
    //     this.total = res.data.data.total;
    //   });
    // },
    getGoodList() {
      console.log(this.queryInfo.pagesize)
      this.$http.post("product/list",  this.queryInfo ).then((res) => {
        if (res.data.status !== 10000) {
          return this.$message.error("Failed to get list of products");
        }
        console.log(res.data);
        this.goodslist = res.data.data.list;
        this.total = res.data.data.total;
      });
    },
    handleSizeChange(newsize) {
      this.queryInfo.pagesize = newsize;
      this.getGoodList();
    },
    handleCurrentChange(newpage) {
      this.queryInfo.pagenum = newpage;
      this.getGoodList();
    },
   
    goAddpage() {
        this.$router.push('/goods/add')
    },
    showProgressBox() {

    },
    //监听图片上传成功的事件
handleSuccess(response){
    //1.拼接得到一个图片信息对象
    var data = response.data;
    var s = data.split("/")
    console.log(s)
    var obj = "/"+s[3]+"/"+s[4]
    this.addForm.image=obj;
    console.log(obj)
    //2.将图片信息对象push到pics数组中
    console.log(data)
    console.log(this.addForm);
}

    ,
    // 展示添加用户的对话框
    showAddCateDialog() {
      // 获取父级分类的数据列表
      // this.getParentCateList();
      this.addCateDialogVisible = true;
    },
    handleRemove(file, fileList) {
        console.log(file, fileList);
      },
      handlePreview(file) {
        console.log(file);
      },
    addGoods() {
      this.$refs.addCateFromRef.validate((valid) => {
        if (!valid) return;
        this.$http.post("/admin/product/add", this.addForm).then((res) => {
          if (res.data.status !== 10000) {
            return this.$message.error("Failed to add product");
          }
          this.$message.success("The product was added successfully");
          this.getGoodList();
          this.addCateDialogClosed = false;
        });
      });
    },
    editGoods(catelist) {
      console.log(catelist)
      this.editCateDialogVisible = true;
      this.showCatelist.id = catelist.id;
      this.showCatelist.name = catelist.name;
      this.showCatelist.categoryId = catelist.categoryId;
      this.showCatelist.price = catelist.price;
      this.showCatelist.stock = catelist.stock;
      this.showCatelist.detail = catelist.detail;
      
    },
     editGoodsInfo() {
      this.$refs.editFormRef.validate((valid) => {
        if (!valid) return;
        this.$http
          .post("/admin/product/update" , this.showCatelist)
          .then((res) => {
            if (res.data.status !== 10000) {
              return this.$message.error("Failed to edit item");
            }
            this.$message.success("Editing the item successfully");
            this.getGoodList();
            this.editCateDialogVisible = false;
          });
      });
    },
    removeById(element) {
      // 弹框询问用户是否删除数据
     
      this.$confirm("This action will permanently delete the item, whether to continue?", "prompt", {
        confirmButtonText: "Are you sure",
        cancelButtonText: "Cancel",
        type: "warning",
      })
        .then((res) => {
          console.log(res);
          
          this.$http.post("/admin/product/delete" ,{id:element.id}).then((res) => {
            if (res.data.status !== 10000) {
              return this.$message.error("Failed to delete listing");
            }
            this.$message.success("Item deletion successful");
            this.getGoodList();
          });
        })
        .catch((err) => {
          console.log(err);

          return this.$message.info("The deletion has been canceled");
        });
    },
    addToChart(productId){
      this.$http
          .post("/cart/add" , {productId:productId,count:1})
          .then((res) => {
            if (res.data.status !== 10000) {
              return this.$message.error("Failed to add cart");
            }
            this.$message.success("Add cart successfully");
            this.getGoodList();
            this.editCateDialogVisible = false;
          })},
    }
    }
  
  
;
</script>

<style>
</style>