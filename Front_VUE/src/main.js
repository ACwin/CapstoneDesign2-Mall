import Vue from 'vue'
import App from './App.vue'
import router from './router'
import Element from 'element-ui'
import axios from 'axios'
import TreeTable from 'vue-table-with-tree-grid'
import VueQuillEditor from 'vue-quill-editor'
import 'quill/dist/quill.core.css' // import styles
import 'quill/dist/quill.snow.css' // for snow theme
import 'quill/dist/quill.bubble.css' // for bubble theme
import 'element-ui/lib/theme-chalk/index.css';
// 导入全局样式表
import  './assets/css/global.css';
// 导入字体图标
import  './assets/fonts/iconfont.css' 
Vue.component('tree-table',TreeTable)
Vue.use(VueQuillEditor, /* { default global options } */)
Vue.use(Element)
Vue.prototype.$target = "http://localhost:8888/api"
Vue.config.productionTip = false
// axios.defaults.baseURL=`http://timemeetyou.com:8889/api/private/v1/`

axios.defaults.baseURL=`api`;
axios.defaults.withCredentials=true;
// 通过axios请求拦截器添加token,保证拥有获取数据的权限
axios.interceptors.request.use(config => {
  // 为请求头对象，添加token验证的authorization字段，保证有权限的api调用成功
  config.headers.Authorization = window.sessionStorage.getItem('token')
  return config
})
Vue.prototype.$http = axios
// Vue.prototype.$confirm = MessageBox.confirm
Vue.filter('dateFormat',function(originVal){
  const dt=new Date(originVal)
  
  const y=dt.getFullYear()
  const m=(dt.getMonth()+1+'').padStart(2,'0')
  const d = (dt.getDate() + '').padStart(2,'0')

  const hh=(dt.getHours() + '').padStart(2,'0')
  const mm=(dt.getMinutes() + '').padStart(2,'0')
  const ss=(dt.getSeconds() + '').padStart(2,'0')

  return `${y}-${m}-${d} ${hh}:${mm}:${ss}`
})
const vm =new Vue({
  router,
  Element,
  el:'#app',
  render: h => h(App)
})
console.log(vm);