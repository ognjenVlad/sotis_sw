import Vue from 'vue';
import Vuex from 'vuex';

import auth from '@store/modules/auth.module';
import domain from '@store/modules/domains.module';
import exams from '@store/modules/exams.module'
import users from '@store/modules/users.module'
import graph from '@store/modules/graph.module'
import students from '@store/modules/students.module'


Vue.use(Vuex);
export const store = new Vuex.Store({
  modules: {
    auth,
    domain,
    exams,
    users,
    graph,
    students
  }
});