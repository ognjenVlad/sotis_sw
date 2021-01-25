import Home from '@components/Home'
import Login from '@components/auth/Login'
import Register from '@components/auth/Register'
import Domains from '@components/Domains'
import Subject from '@components/subject/Subject'

export const routes = [
    { path: '/', component: Home },
    { path: '/login', component: Login },
    { path: '/register', component: Register },
    { path: '/domains', component: Domains },
    { path: '/add-subject', component: Subject },
    { path: '*', redirect: '/' }
];
  