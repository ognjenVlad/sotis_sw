import {authService} from '@services/auth.service'

export const state = () => ({
  user: null
})

export const getters = {
  getUser: state => () => {
    return state.user
  }
}

export const mutations = {
  setUser (state, user) {
    state.user = user
  }
}

export const actions = {
  SET_USER ({ commit }, user) {
    commit('setUser', user)
  },
  async LOGIN ({ commit }, data) {
    const userData = await authService.login(data.axios, data)
    if (userData) {
      localStorage.setItem('authtoken', userData);
      const user = window.atob(userData.split('.')[1])
      commit('setUser', JSON.parse(user))
      return true
    }
    return false
  },
  LOGOUT ({ commit }) {
    localStorage.removeItem('authtoken');
    commit('setUser', null);
  },
  // eslint-disable-next-line no-unused-vars
  async REGISTER ({commit}, data) {
    return await authService.register(data.axios, data)
  }
}

export default {
  state,
  actions,
  mutations,
  getters
};
  