<template>
  <div>
    <el-breadcrumb separator="/">
      <el-breadcrumb-item :to="{ path: '/home' }">Home page</el-breadcrumb-item>
      <el-breadcrumb-item>Statistics</el-breadcrumb-item>
      <el-breadcrumb-item>data report</el-breadcrumb-item>
    </el-breadcrumb>

    <el-card>
         <div id="main" style="width: 100%; height: 400px"></div>
    </el-card>
  </div>
</template>
 
<script>
import echarts from 'echarts'
import _ from 'lodash'
export default {
  data () {
    return {
      options: {
        title: {
          text: '用户来源'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'cross',
            label: {
              backgroundColor: '#E9EEF3'
            }
          }
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: [
          {
            boundaryGap: false
          }
        ],
        yAxis: [
          {
            type: 'value'
          }
        ]
      }
    }
  },
  created() {},
//   dom元素渲染完毕后
  async mounted () {
    const myChart = echarts.init(document.getElementById('main'))
    const { data: res } = await this.$http.get('reports/type/1')
    console.log(res)
    if (res.meta.status !== 200) {
      return this.$message.error('Fail to get data to draw the graph!')
    }
    const option = _.merge(res.data, this.options)
    myChart.setOption(option)
  },
  methods: {},
};
</script>
 
<style>
</style>