<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">Home page</el-breadcrumb-item>
      <el-breadcrumb-item>Order management</el-breadcrumb-item>
      <el-breadcrumb-item>List of orders</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card>
      <el-row>
        <el-col :span="8">
          <el-input v-model="queryInfo.query" placeholder="Please enter the content">
            <el-button slot="append" icon="el-icon-search" @click="getOrderList()"></el-button>
          </el-input>
        </el-col>
      </el-row>

      <el-table :data="orderlist" border stripe>
        <el-table-column type="index"></el-table-column>
        <el-table-column label="Order number" prop="orderNo"></el-table-column>
        <el-table-column label="Order price" prop="totalPrice"></el-table-column>
        <el-table-column label="Whether to pay" prop="paymentType">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.paymentType === 1" type="success"
              >Paid</el-tag
            >
            <el-tag v-else type="danger">Non-payment</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="Shipping time" prop="deliveryTime">
          <template slot-scope="scope">
            {{ scope.row.deliveryTime}}
          </template>
        </el-table-column>
        <el-table-column label="Time of order" prop="createTime">
          <template slot-scope="scope">
            {{ scope.row.createTime | dateFormat }}
          </template>
        </el-table-column>
        <el-table-column label="operate">
          <template slot-scope="">
            <el-button
              size="mini"
              type="primary"
              icon="el-icon-edit"
              @click="showBox"
              >edit</el-button
            >
            <el-button size="mini" type="success" icon="el-icon-location" @click="showProgressBox"
              >location</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <!-- 分页区 -->
      <el-pagination
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
        :current-page="queryInfo.pagenum"
        :page-sizes="[10, 20, 30, 40]"
        :page-size="queryInfo.pagesize"
        layout="total, sizes, prev, pager, next, jumper"
        :total="total"
      >
      </el-pagination>
    </el-card>

    <el-dialog title="Modify the address" :visible.sync="addressVisible" width="50%" @close="addressDialogClosed">
      <el-form
        :model="addressForm"
        :rules="addressRules"
        ref="addressFormRef"
        label-width="100px"
        class="demo-ruleForm"
      >
        <el-form-item label="Province/county" prop="address1">
          <el-cascader :options="cityData" v-model="addressForm.address1"></el-cascader>
        </el-form-item>
        <el-form-item label="Full address" prop="address2">
          <el-input v-model="addressForm.address2"></el-input>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="addressVisible = false">Elimination</el-button>
        <el-button type="primary" @click="addressVisible = false"
          >OK</el-button
        >
      </span>
    </el-dialog>

      <el-dialog title="Logistics progress" :visible.sync="ProgressVisible" width="50%" @close="addressDialogClosed">
      
      <span slot="footer" class="dialog-footer">
        <el-button @click="ProgressVisible = false">Elimination</el-button>
        <el-button type="primary" @click="ProgressVisible = false"
          >OK</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>
import cityData from './citydata.js'
export default {
  data() {
    return {
      queryInfo: {
        pageNum: 1,
        pageSize: 10,
      },
      total: 0,
      orderlist: [],
      addressVisible: false,
      addressForm: {
          address1:[],
          address2:'',
      },
      addressRules: {
          address1: [
               { required: true, message: 'Please select a province, city and county', trigger: 'blur' },
          ],
          address2: [
               { required: true, message: 'Please fill in the detailed address', trigger: 'blur' },
          ]
      },
      cityData:cityData,
      ProgressVisible:false,
      progressInfo: []
    };
  },
  created() {
    this.getOrderList();
  },
  methods: {
    getOrderList() {
      this.$http.get("order/list", { params: this.queryInfo }).then((res) => {
        if (res.data.status !== 10000) {
          return this.$message.error("Failed to get order");
        }
      
        this.total = res.data.data.total;
        this.orderlist = res.data.data.list;
        console.log(this.orderlist)
      });
    },
    handleSizeChange(newsize) {
      this.queryInfo.pagesize = newsize;
      this.getOrderList();
    },
    handleCurrentChange(newpage) {
      this.queryInfo.pagenum = newpage;
      this.getOrderList();
    },
    showBox() {
      this.addressVisible = true;
    },
    addressDialogClosed() {
        this.$refs.addressFormRef.resetFields()
    },
    showProgressBox() {
        this.$http.get('/kuaidi/804909574412544580').then(res=>{
            if(res.data.meta.status!==200) {
                return this.$message.error('获取进度失败')
            }
            this.progressInfo = res.data.data
        })
        console.log(this.progressInfo);
        this.ProgressVisible=true
    }
  },
};
</script>

<style>
.el-cascader {
    width: 100%;
}
</style>