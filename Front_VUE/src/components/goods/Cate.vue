<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">Home page</el-breadcrumb-item>
      <el-breadcrumb-item>Product management</el-breadcrumb-item>
      <el-breadcrumb-item>Product category</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card>
      <!-- 添加分类按钮 -->
      <el-row
        ><el-col
          ><el-button type="primary" @click="showAddCateDialog()"
            >Add a classification</el-button
          ></el-col
        ></el-row
      >

      <!-- 表格 -->
      <tree-table
        :data="catelist"
        :columns="columns"
        :selection-type="false"
        :expand-type="false"
        show-index
        index-text="#"
        border
        class="treetable"
      >

        <!-- 排序 -->
        <template slot="order" slot-scope="scope">
          <el-tag v-if="scope.row.type === 1" size="mini">Level one</el-tag>
          <el-tag
            v-else-if="scope.row.type === 2"
            size="mini"
            type="success"
            >Level two</el-tag
          >
          <el-tag v-else size="mini" type="warning">Level three</el-tag>
        </template>
        <!-- 操作 -->
        <template slot="opt" slot-scope="scope">
          <div style="display: flex">
            <el-button
              size="mini"
              type="primary"
              icon="el-icon-edit"
              @click="editCate(scope.row)"
              >edit</el-button
            >
            <el-button
              size="mini"
              type="danger"
              icon="el-icon-delete"
              @click="removeCate(scope.row)"
              >Delete</el-button
            >
          </div>
        </template>
      </tree-table>

      <!-- 分页区 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="querInfo.pagenum"
        :page-sizes="[ 25,  35]"
        :page-size="querInfo.pagesize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>

    <!-- 添加分类的对话框 -->
    <el-dialog
      title="Add a classification"
      :visible.sync="addCateDialogVisible"
      width="50%"
      @close="addCateDialogClosed()"
    >
      <!-- 主体区 -->
      <el-form
        :model="addCateFrom"
        :rules="addCateFromRules"
        ref="addCateFromRef"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="Classification name:" prop="name">
          <el-input v-model="addCateFrom.name"></el-input>
        </el-form-item>
        <el-form-item label="Acquisition sequence:" prop="orderNum">
          <el-input v-model="addCateFrom.orderNum"></el-input>
        </el-form-item>
        <el-form-item label="Parent classification:">
          <el-cascader
            v-model="selectedKeys"
            :options="ParentCateList"
            :props="{
              expandTrigger: 'hover',
              value: 'parentId',
              label: 'name',
            }"
            @change="parentCateChange()"
            clearable
            change-on-select
          ></el-cascader>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addCateDialogVisible = false">Elimination</el-button>
        <el-button type="primary" @click="addCate()">OK</el-button>
      </span>
    </el-dialog>

    <!-- 编辑分类的对话框 -->
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
        <el-form-item label="Category name" prop="name">
          <el-input v-model="showCatelist.name"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editCateDialogVisible = false">Elimination</el-button>
        <el-button type="primary" @click="editCateInfo">OK</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      querInfo: {
        pagenum: 1,
        pagesize: 20,
      },
      //   商品分类的数据列表
      catelist: [],
      //   总数据条数
      total: 0,
      //   为table指定列的定义
      columns: [
        {
          label: "Classification name",
          prop: "name",
        },
        {
          label: "sort",
          type: "template",
          template: "order",
        },
        {
          label: "operate",
          type: "template",
          template: "opt",
        },
      ],
      addCateDialogVisible: false,
      //   添加分类的表单数据对象
      addCateFrom: {
        // post请求发送的数据
        name: "",
        type: 0,
        parentId: 0,
        orderNum:0
      },
      // 添加分类表单的验证规则对象
      addCateFromRules: {
        name: [{ required: true, message: "Please enter a category name", trigger: "blur" }],
      },
      //   父级分类列表
      ParentCateList: [{name:"Fresh fruit",parentId:"3"},{name:"Seafood and aquatic products",parentId:"5"},{name:"Selected meats",parentId:"6"},{name:"Cold and frozen",parentId:"9"},{name:"Vegetables and eggs",parentId:"10"},{name:"Delicious mushrooms",parentId:"27"}],
      //   选中的父级分类的id数组
      selectedKeys: [3,5,6,9,10,27],
      editCateDialogVisible: false,
      showCatelist: {
        id: "",
        name: "",
        type: "",
        parentId: "",
        orderNum: "",
        
      },
      EditCateFromRules: {
        name: [{ required: true, message: "Please enter a category name", trigger: "blur" }],
      },
    };
  },
  created() {
    this.getCateList();
  },
  methods: {
    getCateList() {
      // this.$http.get("categories", { params: this.querInfo }).then((res) => {
      //   if (res.data.meta.status !== 200) {
      //     return this.$message.error("获取失败");
      //   }
      //   // console.log(res.data);
      //   this.catelist = res.data.data.result;
      //   console.log(this.catelist)
      //   this.total = res.data.data.total;
      //   console.log(this.total)
      // });

      this.$http
        .get("admin/category/list", {
          params: {
            pageNum: this.querInfo.pagenum,
            pageSize: this.querInfo.pagesize,
          },
        })
        .then((res) => {
          if (res.data.status !== 10000) {
            return this.$message.error("Acquisition failed");
          }

          var newList = new Array();
          var list = res.data.data.list;
          console.log(list);
          for (var i = 0; i < list.length; i++) {
            if (list[i].type === 1) newList.push(list[i]);
          }
          for (i = 0; i < newList.length; i++) {
            newList[i].children = new Array();
          }
          for (i = 0; i < list.length; i++) {
            if (list[i].type === 2) {
              var parentId = list[i].parentId;
              var j = 0;
              for (j in newList) {
                if (newList[j].id === parentId) {
                  newList[j].children.push(list[i]);
                  break;
                }
              }
            }
          }

          for (var ii = 0; ii < newList.length; ii++) {
            for (var jj = 0; jj < newList[ii].children.length; jj++) {
              newList[ii].children[jj].children = new Array();
            }
          }

          for (i = 0; i < list.length; i++) {
            if (list[i].type === 3) {
              console.log(list[i].name);
              parentId = list[i].parentId;

              for (j = 0; j < newList.length; j++) {
                for (var k = 0; k < newList[j].children.length; k++) {
                  if (newList[j].children[k].id === parentId) {
                    newList[j].children[k].children.push(list[i]);
                    break;
                  }
                }
              }
            }
          }
          
          this.catelist = newList;
          console.log(this.catelist);
          this.total = res.data.data.total;
          console.log(this.total);
        });
    },
    // 监听pagesize改变的事件
    handleSizeChange(newSize) {
      this.querInfo.pagesize = newSize;
      this.getCateList();
    },
    // 监听pagenum 改变
    handleCurrentChange(newPage) {
      this.querInfo.pagenum = newPage;
      this.getCateList();
    },
    // 展示添加用户的对话框
    showAddCateDialog() {
      // 获取父级分类的数据列表
      // this.getParentCateList();
      this.addCateDialogVisible = true;
    },
    // 获取父级分类的数据列表
    // getParentCateList() {
    //   this.$http.get("categories", { params: { type: 2 } }).then((res) => {
    //     if (res.data.meta.status !== 200) {
    //       return this.$message.error("获取失败");
    //     }
    //     // console.log(res.data);
    //     this.ParentCateList = res.data.data;
    //   });
    // },
    // 选择项发生变化
    parentCateChange() {
      console.log(this.selectedKeys);
      // 如果selectedKeys 的length 大于0 证明选中的父级分类
      if (this.selectedKeys.length > 0) {
        this.addCateFrom.parentId =
          this.selectedKeys[this.selectedKeys.length - 1];
          console.log(this.addCateFrom.parentId);
        this.addCateFrom.type = this.selectedKeys.length+1;
        console.log(this.addCateFrom.type);
        return;
      } else {
        this.addCateFrom.parentId = 0;
        this.addCateFrom.type = 0;
      }
    },
    // 点击确认按钮后，添加新的分类
    addCate() {
      this.$refs.addCateFromRef.validate((valid) => {
        if (!valid) return;
        this.$http.post("admin/category/add", this.addCateFrom).then((res) => {
          if (res.data.status !== 10000) {
            return this.$message.error("Failed to add classification");
          }
          this.$message.success("The classification was added successfully");
          this.getCateList();
          this.addCateDialogClosed = false;
        });
      });
    },
    addCateDialogClosed() {
      this.$refs.addCateFromRef.resetFields();
      this.selectedKeys = [];
      this.addCateFrom.type = 0;
      this.addCateFrom.parentId = 0;
    },
    editCate(catelist) {
      console.log(catelist)
      this.editCateDialogVisible = true;
      this.showCatelist.id = catelist.id;
      this.showCatelist.name = catelist.name;
      this.showCatelist.type = catelist.type;
      this.showCatelist.parentId = catelist.parentId;
      this.showCatelist.orderNum = catelist.orderNum;
      
    },
    editCateInfo() {
      this.$refs.editFormRef.validate((valid) => {
        if (!valid) return;
        this.$http
          .post("admin/category/update" , this.showCatelist)
          .then((res) => {
            if (res.data.status !== 10000) {
              return this.$message.error("编辑分类失败");
            }
            this.$message.success("编辑分类成功");
            this.getCateList();
            this.editCateDialogVisible = false;
          });
      });
    },
    removeCate(element) {
      // 弹框询问用户是否删除数据
     
      this.$confirm("This action permanently deletes the classification, whether to continue?", "prompt", {
        confirmButtonText: "Are you sure?",
        cancelButtonText: "Cancel",
        type: "warning",
      })
        .then((res) => {
          console.log(res);
          console.log(element.id)
          this.$http.post("admin/category/delete" ,{id:element.id}).then((res) => {
            if (res.data.status !== 10000) {
              return this.$message.error("Failed to delete user");
            }
            this.$message.success("The user was deleted successfully");
            this.getCateList();
          });
        })
        .catch((err) => {
          console.log(err);

          return this.$message.info("The deletion has been canceled");
        });
    },
  },
};
</script>

<style>
.treetable {
  margin-top: 10px;
}
.el-cascader {
  width: 100%;
}
</style>