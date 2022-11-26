<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">Home page</el-breadcrumb-item>
      <el-breadcrumb-item>commodity management</el-breadcrumb-item>
      <el-breadcrumb-item>Add commodity</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card>
      <!-- 提示区 -->
      <el-alert
        title="Adding Product Information"
        type="info"
        center
        show-icon
        :closable="false"
      >
      </el-alert>


      <!-- tab栏区域 -->
      <el-form
        :model="addForm"
        :rules="addFormRules"
        ref="addFormRef"
        label-width="100px"
        label-position="top"
      >
        <el-tabs
          v-model="activeIndex"
          :tab-position="'left'"
          :before-leave="beforeTabLeave"
          @tab-click="tabClicked"
        >
          <el-tab-pane label="Basic Information" name="0">
            <el-form-item label="commodity name" prop="name">
              <el-input v-model="addForm.name"></el-input>
            </el-form-item>
            <el-form-item label="commodity price" prop="price">
              <el-input v-model="addForm.price" type="number"></el-input>
            </el-form-item>
            <el-form-item label="commodity category" prop="categoryId">
              <el-input v-model="addForm.categoryId" type="number"></el-input>
            </el-form-item>
            <el-form-item label="commodity stock" prop="stock">
              <el-input v-model="addForm.stock" type="number"></el-input>
            </el-form-item>
            <el-form-item label="commodity details" prop="detail">
              <el-input v-model="addForm.detail"></el-input>
            </el-form-item>
           
          </el-tab-pane>
     
  
          <!-- <el-tab-pane label="商品图片" name="3">
            <el-upload
              action="http://localhost:8888/api/admin/upload/file""
              :on-preview="handlePreview"
              :on-remove="handleRemove"
              list-type="picture"
              :headers="headerObj"
              :on-success="handleSuccess"
            >
              <el-button size="small" type="primary">点击上传</el-button>
            </el-upload>
          </el-tab-pane> -->
        
        </el-tabs>
      </el-form>
    </el-card>

    <el-dialog title="preview picture" :visible.sync="previewVisible" width="50%">
      <img :src="previewPath" alt="" class="preImg" />
    </el-dialog>
  </div>
</template>
 
<script>
import _ from 'lodash'
export default {
  name: "vueName",
  data() {
    return {
      activeIndex: "0",
      addForm: {
        name: "",
        price: 0,
        categoryId: 0,
       stock: 0,
        detail: ""
      },
      addFormRules: {
        name: [
          { required: true, message: "请输入商品名称", trigger: "blur" },
        ],
        price: [
          { required: true, message: "请输入商品价格", trigger: "blur" },
        ],
        categoryId: [
          { required: true, message: "请输入商品分类", trigger: "blur" },
        ],
       stock: [
          { required: true, message: "请输入商品股票", trigger: "blur" },
        ],
        detail: [
          { required: true, message: "请选择商品详情", trigger: "blur" },
        ],
      },
      catelist: [],
      manyTableData: [],
      onlyTableData: [],
      headerObj: {
        Authorization: window.sessionStorage.getItem("token"),
      },
      previewPath: "",
      previewVisible: false,
    };
  },
  created() {
    // this.getCateList();
  },
  methods: {
    // getCateList() {
    //   this.$http.get("categories").then((res) => {
    //     if (res.data.meta.status !== 200) {
    //       return this.$message.error("获取商品分类数据失败");
    //     }

    //     this.catelist = res.data.data;
    //   });
    // },
    handleChange() {
      if (this.addForm.detail.length !== 3) {
        this.addForm.detail = [];
      }
    },
    beforeTabLeave(activeName, oldActiveName) {
      if (oldActiveName === "0" && this.addForm.detail.length !== 3) {
        this.$message.error("Please select the category first");
        return false;
      }
    },
  
    handlePreview(file) {
      console.log(file);
      this.previewPath = file.response.data.url;
      this.previewVisible = true;
    },
    handleRemove(file) {
      console.log(file);
      const filePath = file.response.data.tmp_path;
      const i = this.addForm.pics.findIndex((x) => x.pic === filePath);
      this.addFrom.pics.splice(i, 1);
      console.log(this.addForm);
    },
    handleSuccess(res) {
      console.log(res);
      const picInfo = { pic: res.data.tmp_path };
      this.addForm.pics.push(picInfo);
      console.log(this.addForm);
    },
    add() {
      console.log("Item information to add", this.addForm);
      this.$refs.addFormRef.validate((valid) => {
        if (!valid) {
          return this.$message.error("Please fill in the necessary options");
        }
        // lodash cloneDeep(obj)
        const form= _.cloneDeep(this.addForm)
       form.detail = form.detail.join(",");
        this.manyTableData.forEach((item) => {
          const newInfo = {
            attr_id: item.attr_id,
            attr_value: item.attr_vals
          };
          this.addForm.attrs.push(newInfo);
        });
        this.onlyTableData.forEach((item) => {
          const newInfo = {
            attr_id: item.attr_id,
            attr_value: item.attr_vals
          };
          this.addForm.attrs.push(newInfo);
        });
        form.attrs = this.addForm.attrs
        console.log(form);
        this.$http.post("goods", form).then((res) => {
          this.$message.success("Item added successfully");
          console.log(res)
          this.$router.push("/goods");
        });
      });
    },
  },
  computed: {
    cateId() {
      if (this.addForm.detail.length === 3) {
        return this.addForm.detail[2];
      }
      return null;
    },
  },
};
</script>
 
<style>
.el-steps {
  margin: 15px 0;
}

.el-step__title {
  font-size: 13px;
}

.el-checkbox {
  margin: 0 5px 0 0;
}
.preImg {
  width: 100%;
}
.btn {
  margin-top: 10px;
}
</style>