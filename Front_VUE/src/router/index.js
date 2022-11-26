import Vue from 'vue'
import VueRouter from 'vue-router'
const Login = () => import(/* webpackChunkName: "Login_Home_Welcome" */ '../components/Login')
const Login2 = () => import(/* webpackChunkName: "Login_Home_Welcome" */ '../components/Login2')
const Home = () => import(/* webpackChunkName: "Login_Home_Welcome" */ '../components/Home')
const Mall = () => import(/* webpackChunkName: "Login_Home_Welcome" */ '../components/Mall')
const Details = () => import(/* webpackChunkName: "Login_Home_Welcome" */ '../components/views/Details')
const Goodsall = () => import(/* webpackChunkName: "Login_Home_Welcome" */ '../components/views/Goodsall')
const ShoppingCart = () => import(/* webpackChunkName: "Login_Home_Welcome" */ '../components/views/ShoppingCart')
const MyOrder = () => import(/* webpackChunkName: "Login_Home_Welcome" */ '../components/views/MyOrder')

const Welcome = () => import(/* webpackChunkName: "Login_Home_Welcome" */ '../components/Welcome')
const Users = () => import(/* webpackChunkName: "Users_Rights_Roles" */ '../components/user/User')


const Roles =() =>import ('../components/power/Roles')
const Cate =() =>import('../components/goods/Cate')
const Cart =() =>import('../components/goods/Cart')
const List = () => import('../components/goods/List')
const Add = () => import('../components/goods/Add')
const Order = () => import('../components/order/Order')
const Report = () => import('../components/report/Report')
// 懒加载

Vue.use(VueRouter)
const routes = [{
    path: '/',
    redirect: '/home'

  },
  {
    path: '/login',
    component: Login
  },
  {
    path: '/login2',
    component: Login2
  },
  {
    path: '/mall',
    name:'Mall',
    component: Mall
  },
  {
    path: '/goods/details',
    name: 'Details',
    component: Details
  },
  {
    path: '/goodsall',
    name: 'Goodsall',
    component: Goodsall
  },
  {
    path: '/shoppingcart',
    name: 'ShoppingCart',
    component: ShoppingCart
  },
  {
    path: '/myorder',
    name: 'MyOrder',
    component: MyOrder
  },
  {
    path: '/home',
    component: Home,
    redirect:'/welcome',
    children:[
      {path: '/welcome', component: Welcome},
      {path: '/users', component:Users},
      {path: '/roles',component:Roles},
      {path: '/categories',component:Cate},
      {path: '/cart',component:Cart},
      {path: '/goods',component:List},
      {path: '/goods/add',component:Add},
      {path: '/orders',component:Order},
      {path: '/reports',component:Report},
  ]
  },


]
const router = new VueRouter({
  routes,
  // url模式
  // mode:'history'
})
// 如果用户没有登录，直接通过URL访问特定页面，需要重新导航到登录页面
// 所以需要添加beforeEach导航守卫
router.beforeEach((to,from,next) => {
  // to 将要访问的 路径
  // from 代表从哪个路径跳转而来
  // next 是一个函数，表示放行
  if(to.path ==='/mall') return next();
  if(to.path ==='/login') return next();
  if(to.path ==='/login2') return next();
  if(to.path ==='/goods/details') return next();
  if(to.path ==='/goodsall') return next();
  // 获取token
  const tokenStr=window.sessionStorage.getItem('token')
  if(!tokenStr) return next('/login2')
  // 存在的话直接放行
  next();
})


// 如果用户访问的登录页，直接放行
export default router