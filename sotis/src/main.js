import Vue from 'vue'
import App from './App.vue'
import VueRouter from 'vue-router'
import axios from 'axios'
import VueAxios from 'vue-axios'
import { routes } from './constants/routes.js'
import { store } from '@store'
import { BootstrapVue, IconsPlugin } from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.use(BootstrapVue)
Vue.use(IconsPlugin)
Vue.config.productionTip = false

const router = new VueRouter({
  routes,
  mode: 'history'
})

router.beforeEach((to, from, next) => {
  console.log(to)
  const loggedIn = localStorage.getItem('authtoken');
  if (!loggedIn && !['/login'].includes(to.path)) {
    return next('/login');
  } else if (loggedIn && ['/login'].includes(to.path)) {
    return next('/')
  }

  next();
})

axios.interceptors.request.use(
  (config) => {
    let token = localStorage.getItem('authtoken');
    if (token) {
      config.headers['Auth-Token'] = `${ token }`;
    }

    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

Vue.use(VueRouter)
Vue.use(VueAxios, axios)

new Vue({
  router,
  store,
  render: h => h(App),
}).$mount('#app')
