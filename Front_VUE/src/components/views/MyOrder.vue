<template>
  <div>
    <el-breadcrumb separator="/">
     
      <el-breadcrumb-item style="margin-left:20px;margin-top:20px">List of orders</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card>


      <el-table :data="orderlist" border stripe>
        <el-table-column type="index"></el-table-column>
        <el-table-column label="Order number" prop="orderNo"></el-table-column>
        <el-table-column label="Order price" prop="totalPrice"></el-table-column>
        <el-table-column label="Order status" prop="orderStatus">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.orderStatus === 10" type="success"
              >Pending payments</el-tag
            >
            <el-tag v-else type="danger">Canceled</el-tag>
          </template>
        </el-table-column>
 
        <el-table-column label="Time of order" prop="createTime">
          <template slot-scope="scope">
            {{ scope.row.createTime | dateFormat }}
          </template>
        </el-table-column>
        <el-table-column label="operate">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="danger"
              icon="el-icon-close"
              @click="deleteOrder(scope.row)"
              >Cancel the order</el-button
            >
            <el-button size="mini" type="success" icon="el-icon-circle-plus" @click="showProgressBox(scope.row)"
              >Order details</el-button
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
      <el-button type="primary" icon="el-icon-arrow-left" round style=" margin-top: 6%" @click="goback()">go back</el-button>
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

      <el-dialog title="Order details" :visible.sync="ProgressVisible" width="50%" @close="addressDialogClosed">
          <el-table :data="productInfo" border stripe>
        <el-table-column label="Product Name" prop="productName"></el-table-column>
        <el-table-column label="Payment amount" prop="totalPrice"></el-table-column>
      </el-table>
            <el-table :data="progressInfo" border stripe>
        <el-table-column label="addressee" prop="receiverName"></el-table-column>
        <el-table-column label="Recipient phone" prop="receiverMobile"></el-table-column>
        <el-table-column label="Shipping address" prop="receiverAddress"></el-table-column>
        
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="ProgressVisible = false">Cancle</el-button>
        <el-button type="primary" @click="ProgressVisible = false"
          >OK</el-button
        >
      </span>
    </el-dialog>
  </div>
</template>

<script>

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
               { required: true, message: 'Please select the province, region, and county', trigger: 'blur' },
          ],
          address2: [
               { required: true, message: 'Please fill in the full address', trigger: 'blur' },
          ]
      },
      ProgressVisible:false,
      progressInfo: []
    };
  },
  created() {
    this.getOrderList();
    this.hasUser = this.$route.query.hasUser;
    this.username =this.$route.query.username;
  },
  methods: {
    getOrderList() {
      this.$http.get("order/list", { params: this.queryInfo }).then((res) => {
        if (res.data.status !== 10000) {
          return this.$message.error("Failed to obtain the order");
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
    showProgressBox(element) {
        
        this.$http.get('order/detail',{params:{orderNo:element.orderNo}}).then(res=>{
            if(res.data.status!==10000) {
                return this.$message.error('Failed to obtain progress. Procedure')
            }
            this.productInfo = res.data.data.orderItemVOList
            var tmp = new Array();
            tmp.push(res.data.data)
           this.progressInfo = tmp

             console.log(this.progressInfo);
           
        })
       
        this.ProgressVisible=true
    },
    deleteOrder(element){
        this.$http.post("order/cancel", { orderNo: element.orderNo }).then((res) => {
        if (res.data.status !== 10000) {
          return this.$message.error("Order cancellation failed");
        }
        this.$message.success("取消订单成功");
        this.getOrderList();

      });
        
    },
           goback(){
      this.$router.push({name:'Mall',params: {hasUser:this.hasUser,username:this.username}});
    },
  },
};
</script>

<style>
.el-cascader {
    width: 100%;
}
</style>