<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">Home page</el-breadcrumb-item>
      <el-breadcrumb-item>Merchandise management</el-breadcrumb-item>
      <el-breadcrumb-item>List of parameters</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card>
      <!-- 警告区域 -->
      <el-alert
        title="Note: Setting parameters is only allowed for third-level classification"
        type="warning"
        :closable="false"
        show-icon
      >
      </el-alert>
      <!-- 选择商品分类区域 -->
      <el-row class="cat_opt">
        <el-col>
          <span>Select a product category：</span>
          <!-- 选择商品分类的级联选择框 -->
          <el-cascader
            v-model="selecteCateKeys"
            :options="catelist"
            :props="{
              expandTrigger: 'hover',
              value: 'cat_id',
              label: 'cat_name',
              children: 'children',
            }"
            @change="handleChange"
          ></el-cascader>
        </el-col>
      </el-row>

      <!-- tab 页签区域 -->
      <el-tabs v-model="activeName" @tab-click="handleTabClick">
        <!-- 添加动态参数的面板 -->
        <el-tab-pane label="Dynamic parameters" name="many">
          <el-button
            type="primary"
            size="mini"
            :disabled="isBtnDisabled"
            @click="addDialogVisible = true"
            >Add parameters</el-button
          >
          <!-- 动态参数表格 -->
          <el-table :data="manyTableData" border stripe>
            <el-table-column type="expand">
              <template slot-scope="scope">
                <el-tag
                  v-for="(item, i) in scope.row.attr_vals"
                  :key="i"
                  closable
                  @close="handleClose(i,scope.row)"
                >
                  {{ item }}
                </el-tag>
                <el-input
                  class="input-new-tag"
                  v-if="scope.row.inputVisible"
                  v-model="scope.row.inputValue"
                  ref="saveTagInput"
                  size="small"
                  @keyup.enter.native="handleInputConfirm(scope.row)"
                  @blur="handleInputConfirm(scope.row)"
                >
                </el-input>
                <el-button
                  v-else
                  class="button-new-tag"
                  size="small"
                  @click="showInput(scope.row)"
                  >+ New Tag</el-button
                >
              </template>
            </el-table-column>
            <el-table-column type="index"></el-table-column>
            <el-table-column
              label="Parameter name"
              prop="attr_name"
            ></el-table-column>
            <el-table-column label="operate">
              <template slot-scope="scope">
                <el-button
                  size="mini"
                  type="primary"
                  icon="el-icon-edit"
                  @click="showEditDialog(scope.row.attr_id)"
                  >edit</el-button
                >
                <el-button
                  size="mini"
                  type="danger"
                  icon="el-icon-delete"
                  @click="removeEditDialog(scope.row.attr_id)"
                  >Delete</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
        <!-- 添加静态属性的面板 -->
        <el-tab-pane label="Static properties" name="only">
          <el-button
            type="primary"
            size="mini"
            :disabled="isBtnDisabled"
            @click="addDialogVisible = true"
            >Add properties</el-button
          >
          <!-- 静态属性表格 -->
          <el-table :data="onlyTableData" border stripe>
              <el-table-column type="expand">
              <template slot-scope="scope">
                <el-tag
                  v-for="(item, i) in scope.row.attr_vals"
                  :key="i"
                  closable
                  @close="handleClose(i,scope.row)"
                >
                  {{ item }}
                </el-tag>
                <el-input
                  class="input-new-tag"
                  v-if="scope.row.inputVisible"
                  v-model="scope.row.inputValue"
                  ref="saveTagInput"
                  size="small"
                  @keyup.enter.native="handleInputConfirm(scope.row)"
                  @blur="handleInputConfirm(scope.row)"
                >
                </el-input>
                <el-button
                  v-else
                  class="button-new-tag"
                  size="small"
                  @click="showInput(scope.row)"
                  >+ New Tag</el-button
                >
              </template>
            </el-table-column>
            <el-table-column type="index"></el-table-column>
            <el-table-column
              label="Property name"
              prop="attr_name"
            ></el-table-column>
            <el-table-column label="operate">
              <template slot-scope="">
                <el-button
                  size="mini"
                  type="primary"
                  icon="el-icon-edit"
                  @click="showEditDialog(scope.row.attr_id)"
                  >edit</el-button
                >
                <el-button
                  size="mini"
                  type="danger"
                  icon="el-icon-delete"
                  @click="removeEditDialog(scope.row.attr_id)"
                  >Delete</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 添加参数的对话框 -->
    <el-dialog
      :title="'Add to' + titleText"
      :visible.sync="addDialogVisible"
      width="50%"
      @close="addDialogClosed"
    >
      <el-form
        ref="addFormRef"
        :model="addForm"
        label-width="80px"
        :rules="addFormRules"
      >
        <el-form-item :label="titleText" prop="attr_name">
          <el-input v-model="addForm.attr_name"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">Elimination</el-button>
        <el-button type="primary" @click="addParams()">OK</el-button>
      </span>
    </el-dialog>

    <!-- 修改参数的对话框 -->
    <el-dialog
      :title="'revise' + titleText"
      :visible.sync="editDialogVisible"
      width="50%"
      @close="editDialogClosed"
    >
      <el-form
        ref="editFormRef"
        :model="editForm"
        label-width="80px"
        :rules="editFormRules"
      >
        <el-form-item :label="titleText" prop="attr_name">
          <el-input v-model="editForm.attr_name"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="editDialogVisible = false">Elimination</el-button>
        <el-button type="primary" @click="editParams()">OK</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      catelist: [],
      //   级联选择器双向绑定的数组
      selecteCateKeys: [],
      //   被激活的页签名称
      activeName: "many",
      manyTableData: [],
      onlyTableData: [],
      addDialogVisible: false,
      addForm: {
        attr_name: "",
      },
      addFormRules: {
        attr_name: [
          { required: true, message: "Please enter a parameter name", trigger: "blur" },
        ],
      },
      editDialogVisible: false,
      editForm: {},
      editFormRules: {
        attr_name: [
          { required: true, message: "Please enter a parameter name", trigger: "blur" },
        ],
      },
      inputVisible: false,
      inputValue: '',

    };
  },
  created() {
    this.getCateList();
  },
  methods: {
    getCateList() {
      this.$http.get("categories").then((res) => {
        if (res.data.meta.status !== 200) {
          return this.$message.error("Failed to get product category");
        }
        this.catelist = res.data.data;
      });
    },
    // 级联选择框选中项变化，会触发这个函数
    handleChange() {
      this.getParamsData();
    },
    // tab页签点击事件的处理函数
    handleTabClick() {
      this.getParamsData();
    },
    // 获取参数的列表数据
    getParamsData() {
      if (this.selecteCateKeys.length !== 3) {
        this.selecteCateKeys = [];
        this.manyTableData = [];
        this.onlyTableData = []
        return;
      }

      //   根据所选分类的id,和当前所处的面板，获取对应的参数
      this.$http
        .get(`categories/${this.cateId}/attributes`, {
          params: { sel: this.activeName },
        })
        .then((res) => {
          if (res.data.meta.status !== 200) {
            return this.$message.error("Failed to get parameter list！");
          }

          res.data.data.forEach((item) => {
            item.attr_vals = item.attr_vals ? item.attr_vals.split(" ") : [];
            item.inputVisible=false
            item.inputValue = ''
          });
          console.log(res.data.data);
          if (this.activeName === "many") {
            this.manyTableData = res.data.data;
          }
          this.onlyTableData = res.data.data;
        });
    },
    addDialogClosed() {
      this.$refs.addFormRef.resetFields();
    },
    addParams() {
      this.$refs.addFormRef.validate((valid) => {
        if (!valid) return;
        this.$http
          .post(`categories/${this.cateId}/attributes`, {
            attr_name: this.addForm.attr_name,
            attr_sel: this.activeName,
          })
          .then((res) => {
            if (res.data.meta.status !== 201) {
              return this.$message.error("Failed to add parameter");
            }
            this.$message.success("The parameter was added successfully");
            this.addDialogVisible = false;
            this.getParamsData();
          });
      });
    },
    showEditDialog(attr_id) {
      this.editDialogVisible = true;
      this.$http
        .get(`categories/${this.cateId}/attributes/${attr_id}`, {
          params: { attr_sel: this.activeName },
        })
        .then((res) => {
          if (res.data.meta.status !== 200) {
            return this.$message.error("Failed to get parameter");
          }

          this.editForm = res.data.data;
        });
    },
    editDialogClosed() {
      this.$refs.editFormRef.resetFields();
    },
    editParams() {
      this.$refs.editFormRef.validate((valid) => {
        if (!valid) return;
        this.$http
          .put(
            `categories/${this.cateId}/attributes/${this.editForm.attr_id}`,
            {
              attr_name: this.editForm.attr_name,
              attr_sel: this.activeName,
            }
          )
          .then((res) => {
            if (res.data.meta.status !== 200) {
              return this.$message.error("Failed to modify parameters");
            }
            this.$message.success("The parameter modification is successful");
            this.editDialogVisible = false;
            this.getParamsData();
          });
      });
    },
    removeEditDialog(attr_id) {
      this.$confirm("This action will permanently delete the classification, do you want to continue?", "prompt", {
        confirmButtonText: "Are you sure",
        cancelButtonText: "Cancel",
        type: "warning",
      })
        .then((res) => {
          console.log(res);
          this.$http
            .delete(`categories/${this.cateId}/attributes/${attr_id}`)
            .then((res) => {
              if (res.data.meta.status !== 200) {
                return this.$message.error("Failed to delete user");
              }
              this.$message.success("The user was deleted successfully");
              this.getParamsData();
            });
        })
        .catch((err) => {
          console.log(err);

          return this.$message.info("The deletion has been canceled");
        });
    },
    handleInputConfirm(row) {
      if(row.inputValue.trim().length===0) {
        row.inputValue=''
        row.inputVisible=false
        return
      }
      // 如果没有return 则需要后续处理
      row.attr_vals.push(row.inputValue.trim())
      row.inputValue = ''
      row.inputVisible=false
      // 需要发起请求，保存这次操作
      this.$http.put(`categories/${this.cateId}/attributes/${row.attr_id}`,{
        attr_name:row.attr_name,
        attr_sel:row.attr_sel,
        attr_vals: row.attr_vals.join(' ')
      }).then(res=> {
        if (res.data.meta.status !== 200) {
              return this.$message.error("Failed to modify parameters");
            }
            this.$message.success('The parameter modification is successful')
      })
    },
    showInput(row) {
      row.inputVisible=true
      this.$nextTick(() => {
          this.$refs.saveTagInput.$refs.input.focus();
        });
    },
    handleClose(i,row) {
      row.attr_vals.splice(i,1)
      this.$http.put(`categories/${this.cateId}/attributes/${row.attr_id}`,{
        attr_name:row.attr_name,
        attr_sel:row.attr_sel,
        attr_vals: row.attr_vals.join(' ')
      }).then(res=> {
        if (res.data.meta.status !== 200) {
              return this.$message.error("Failed to modify parameters");
            }
            this.$message.success('The parameter modification is successful')
      })
    }
  },
  computed: {
    //   如果按钮需要被禁用，则返回true,否则返回false
    isBtnDisabled() {
      if (this.selecteCateKeys.length !== 3) {
        return true;
      }
      return false;
    },
    //   当前选中的三级分类的id
    cateId() {
      if (this.selecteCateKeys.length === 3) {
        return this.selecteCateKeys[2];
      }
      return null;
    },
    // 动态计算标题的文本
    titleText() {
      if (this.activeName === "many") {
        return "Dynamic parameters";
      }
      return "Static properties";
    },
  },
};
</script>

<style>
.cat_opt {
  margin: 15px 0;
}
.el-tag {
  margin: 10px;
}
.input-new-tag {
  width: 120px;
}
</style>