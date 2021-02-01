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
import 'vue-select/dist/vue-select.css';
import vSelect from 'vue-select'

Vue.component('v-select', vSelect)
Vue.use(BootstrapVue)
Vue.use(IconsPlugin)
Vue.config.productionTip = false

// Vue.http.interceptors.push({

//   request: function (request){
//     console.log(request)
//     request.headers['Auth-Token'] = localStorage.getItem('authtoken');
//     return request
//   },
// });
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

axios.defaults.headers.post['Content-Type'] ='application/json'
axios.defaults.headers['Content-Type'] = 'application/json'
axios.interceptors.request.use(
  (config) => {
    let token = localStorage.getItem('authtoken');
    if (token) {
      config.headers['X-Auth-Token'] = `${ token }`;
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
