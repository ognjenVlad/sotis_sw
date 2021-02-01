import Home from '@components/Home'
import Login from '@components/auth/Login'
import Register from '@components/auth/Register'
import Domains from '@components/Domains'
import Subject from '@components/subject/Subject'
import Exams from '@components/exam/Exams'
import Exam from '@components/exam/Exam'
import AddExam from '@components/exam/AddExam'
import AddStudent from '@components/students/AddStudent'
import Graphs from '@components/graphs/Graphs'
import Test from '@components/students/Test'


export const routes = [
    { path: '/', component: Home },
    { path: '/login', component: Login },
    { path: '/register', component: Register },
    { path: '/domains', component: Domains },
    { path: '/add-subject', component: Subject },
    { path: '/exams', component: Exams },
    { path: '/exam', component: Exam },
    { path: '/new-exam', component: AddExam },
    { path: '/add-student', component: AddStudent },
    { path: '/graphs', component: Graphs },
    { path: '/test', component: Test },
    { path: '*', redirect: '/' }
];
  