<template>
  <div>
    <!-- 面包屑导航 -->
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">Home page</el-breadcrumb-item>
      <el-breadcrumb-item>Rights management</el-breadcrumb-item>
      <el-breadcrumb-item>List of roles</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card>
      <!-- 添加角色按钮区域 -->
      <el-row>
        <el-col>
          <el-button type="primary" @click="addDialogVisible = true"
            >Add roles</el-button
          >
        </el-col>
      </el-row>

      <!-- 角色列表区域 -->
      <el-table :data="rolelist" border stripe>
        <!-- 展开列 -->
        <el-table-column type="expand">
          <template slot-scope="scope">
            <el-row :class="['bdbottom',i1===0? 'bdtop':'','vcenter']" v-for="(item1,i1) in scope.row.children" :key="item1.id">
             <!-- 渲染一级权限 -->
              <el-col :span="5">
                <el-tag  closable @close="removeRightById(scope.row,item1.id)">{{item1.authName}}</el-tag>
                <i class="el-icon-caret-right"></i>
              </el-col>
              <!-- 渲染二级和三级权限 -->
              <el-col :span="19">
                <!-- 通过for循环 嵌套渲染二级权限 -->
                <el-row :class="[i2===0 ? '':'bdtop','vcenter']" v-for="(item2,i2) in item1.children" :key="item2.id">
                  <el-col :span="6">
                    <el-tag type="success" closable  @close="removeRightById(scope.row,item2.id)">{{item2.authName}}</el-tag>
                    <i class="el-icon-caret-right"></i>
                  </el-col>
                  <el-col :span="18">
                    <el-tag v-for="(item3) in item2.children" :key="item3.id" type="warning" closable @close="removeRightById(scope.row,item3.id)">   
                      {{item3.authName}}
                    </el-tag>
                  </el-col>
                </el-row>
              </el-col>
            </el-row>
          
          </template>
        </el-table-column>
        <!-- 索引列 -->
        <el-table-column type="index"></el-table-column>
        <el-table-column label="Role name" prop="roleName"></el-table-column>
        <el-table-column label="Role description" prop="roleDesc"></el-table-column>
        <el-table-column label="operate" width="300px">
          <template slot-scope="scope">
            <el-button size="mini" type="primary" icon="el-icon-edit" @click="showEditDialog(scope.row.id)"
              >edit</el-button
            >
            <el-button size="mini" type="danger" icon="el-icon-delete" @click="removeRoles(scope.row.id)"
              >Delete</el-button
            >
            <el-button size="mini" type="warning" icon="el-icon-setting"  @click="showSetRightDialog(scope.row)"
              >Assign permissions</el-button
            >
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 添加用户的对话框 -->
    <el-dialog
      title="Add users"
      :visible.sync="addDialogVisible"
      width="50%"
      @close="dialogClosed"
    >
      <!-- 主题区 -->
      <el-form
        :model="AddRoleForm"
        :rules="AddRoleFormRules"
        ref="AddRoleFormRef"
        label-width="70px"
      >
        <el-form-item label="Role name" prop="roleName">
          <el-input v-model="AddRoleForm.roleName"></el-input>
        </el-form-item>
        <!-- prop为规则 -->
        <el-form-item label="Role description">
          <el-input v-model="AddRoleForm.roleDesc"></el-input>
        </el-form-item>
      </el-form>
      <!-- 底部区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">Elimination</el-button>
        <el-button type="primary" @click="addRoleInfo">Are you sure? </el-button>
      </span>
    </el-dialog>

    <!-- 编辑用户的对话框 -->
    <el-dialog
      title="Edit the user"
      :visible.sync="editDialog"
      width="50%"
      @close="dialogClosed"
    >
      <!-- 主题区 -->
      <el-form
        :model="EditRoleForm"
        :rules="AddRoleFormRules"
        ref="AddRoleFormRef"
        label-width="70px"
      >
        <el-form-item label="Role name" prop="roleName">
          <el-input v-model="EditRoleForm.roleName"></el-input>
        </el-form-item>
        <!-- prop为规则 -->
        <el-form-item label="Role description">
          <el-input v-model="EditRoleForm.roleDesc"></el-input>
        </el-form-item>
      </el-form>


      <!-- 底部区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">Elimination</el-button>
        <el-button type="primary" @click="editRoleInfo()">Are you sure </el-button>
      </span>
    </el-dialog>

       <!-- 分配权限的对话框 -->
    <el-dialog
      title="Assign permissions"
      :visible.sync="SetRightDialog"
      width="50%"
      @close="setRightDialogClosed"
    >
      <!-- 主题区 -->
      <el-tree ref='treeRef' :data="rightslist" :props="defaultProps" show-checkbox node-key="id" default-expand-all :default-checked-keys="defKeys"></el-tree>

      
      <!-- 底部区域 -->
      <span slot="footer" class="dialog-footer">
        <el-button @click="addDialogVisible = false">Elimination</el-button>
        <el-button type="primary" @click="allotRights()">Are you sure? </el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
export default {
  data() {
    return {
      rolelist: [],
      addDialogVisible: false,
      AddRoleForm: {
        roleName: "",
        roleDesc: "",
        roleId: "",
      },
      EditRoleForm:{
        roleName: "",
        roleDesc: "",
        roleId: "",
      },
      AddRoleFormRules: {
        username: [{ required: true, message: "Please enter a username", trigger: blur }],
      },
      editDialog:false,
      SetRightDialog:false,
      rightslist:[],
      // 树形控件属性绑定对象
       defaultProps: {
          children: 'children',
          label: 'authName'
        },
        // 默认选定的节点id值数组
        defKeys:[],
        // 当前分配权限的roleid
        roleId:''
    };
  },
  created() {
    this.getRolesList();
  },
  methods: {
    getRolesList() {
      this.$http.get("roles").then((res) => {
        if (res.data.status != 10000) {
          return this.$message.error("Failed to get list of roles");
        }
        this.rolelist = res.data.data;
        console.log(this.rolelist);
      });
    },
    // 点击按钮，添加新用户
    addRoleInfo() {
      this.$refs.AddRoleFormRef.validate(async (valid) => {
        if (valid !== true) {
          return this.$message.error("Please input correct user name");
        }
        const { data: res } = await this.$http.post("roles", this.AddRoleForm);
        console.log(res.meta.status);
        if (res.meta.status !== 201) {
          return this.$message.error("添加用户失败！");
        }
        this.$message.success("添加用户成功");
        this.addDialogVisible = false;

        this.getRolesList();
      });
    },
    dialogClosed() {
      this.$refs.AddRoleFormRef.resetFields();
    },
    showEditDialog(id) {
      this.$http.get('roles/'+ id).then(res=>{
        this.EditRoleForm = res.data.data
        this.editDialog=true
      })
    },
   editRoleInfo() {
     this.$http
          .put("roles/" + this.EditRoleForm.roleId, {
            roleName: this.EditRoleForm.roleName,
            roleDesc:this.EditRoleForm.roleDesc,
          })
          .then((response) => {
            if (response.data.meta.status !== 200) {
              return this.$message.error("更新用户信息失败");
            }
            this.editDialogVisible = false;
            this.getRolesList();
            this.$message.success("更新用户信息成功");
          });
   },
   removeRoles(id) {
     this.$http.delete('roles/'+ id).then(res=>{
       if(res.data.meta.status!==200) return this.$message.error('删除角色失败')
       this.$message.success("删除角色成功")
       this.getRolesList()
     })
   },
  //  根据id 删除对应权限
  removeRightById(role,rightId) {
    //弹框提示用户是否要删除
      this.$confirm('此操作将永久删除该文件, 是否继续?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          this.$message({
            type: 'success',
            message: '删除成功!'
          });
          this.$http.delete('roles/' +role.id+'/'+'rights/'+rightId).then(res=>{
            if(res.data.meta.status!=200) {
              return this.$message.error('删除权限失败')
            }
            role.children=res.data.data
          })
        }).catch( ()=>{
          return this.$message.info('用户取消了删除')
        })

    
  },
  // 展示分配权限的对话框
  showSetRightDialog(role){
    // 获取所有权限的数据
    this.roleId = role.id
    this.$http.get('rights/tree').then(res=>{
      if(res.data.meta.status!==200) {
        return this.$message.error('获取权限数据失败')
      }
      this.rightslist=res.data.data
    })
    // 递归获取三级节点的id
    this.getLeafKeys(role,this.defKeys)
    this.SetRightDialog=true
  },
  // 通过递归的形式，获取角色下所有三级权限的id，并保存到defkeys数组中
  getLeafKeys(node,arr) {
    // 如果node节点不包含children属性，则是三级节点
    if(!node.children) {
      return arr.push(node.id)
    }

    node.children.forEach(item=> {
      this.getLeafKeys(item,arr)}
      )
  },
  // 监听分配权限对话框的关闭事件
  setRightDialogClosed() {
    this.defKeys = []
  },
  // 点击为角色分配权限
  allotRights() {
    const keys = [
      ...this.$refs.treeRef.getCheckedKeys(),
      ...this.$refs.treeRef.getHalfCheckedKeys()
    ]
    const idStr = keys.join(',')
    this.$http.post(`roles/${this.roleId}/rights`,{rids:idStr}).then(res=>{
      if(res.data.meta.status!==200) {
        return this.$message.error('分配权限失败')
      }
      this.$message.success('分配权限成功')
      this.getRolesList()
      this.SetRightDialog=false
    })
    
  }
  },
};
</script>

<style>
.el-tag {
  margin: 7px;
}

.bdtop{
  border-top:1px solid #eee
}

.bdbottom {
  border-bottom: 1px solid #eee;
}

.vcenter {
  display: flex;
  align-items: center;
}
</style>