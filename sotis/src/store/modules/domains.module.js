import {dataService} from '@services/data.service'

export const state = () => ({
  domains: []
})

export const getters = {
  domains: state => () => {
    return state.domains
  }
}

export const mutations = {
  setDomains (state, domains) {
    state.domains = domains
  }
}

export const actions = {
  async LOAD_DOMAINS ({ commit }, axios) {
    const domains = await dataService.loadDomains(axios)
    if (domains) {
      commit('setDomains', domains)
    }
  },
  async ADD_DOMAIN ({dispatch}, data) {
    const result = await dataService.addDomain(data.axios, {title: data.title})
    dispatch('LOAD_EXAMS', data.axios)
    return result
  }
}

export default {
  state,
  actions,
  mutations,
  getters
};
  