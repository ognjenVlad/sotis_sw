import Vue from 'vue';
import Vuex from 'vuex';

import user from '@store/modules/user.module';
import domain from '@store/modules/domains.module';

Vue.use(Vuex);
export const store = new Vuex.Store({
  modules: {
    user,
    domain
  }
});