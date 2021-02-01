import {dataService} from '@services/data.service'

export const state = () => ({
  domains: [],
  subjects: []
})

export const getters = {
  domains: (state) => () => {
    return state.domains
  },
  subjects: (state) => () => {
    return state.subjects
  }
}

export const mutations = {
  setDomains (state, domains) {
    state.domains = domains
  },
  setSubjects (state, subjects) {
    state.subjects = subjects
  }
}

export const actions = {
  async LOAD_DOMAINS ({ commit }, axios) {
    const domains = await dataService.loadDomains(axios)
    if (domains) {
      commit('setDomains', domains)
    }
  },
  async LOAD_SUBJECTS({ commit }, axios) {
    const subjects = await dataService.loadSubjects(axios)
    if (subjects) {
      console.log(subjects)
      commit('setSubjects', subjects)
    }
  },
  async ADD_SUBJECT ({dispatch}, data) {
    const result = await dataService.addSubject(data.axios, {title: data.title, teacher: ''})
    dispatch('LOAD_EXAMS', data.axios)
    return result
  },
  // eslint-disable-next-line no-unused-vars
  async DELETE_SUBJECT ({commit}, data) {
    return await dataService.deleteSubject(data.axios, data)
  },
  // eslint-disable-next-line no-unused-vars
  async DELETE_USER_FROM_SUBJECT ({commit}, data) {
    await dataService.deleteUserFromSubject(data.axios, data)
  }
}

export default {
  state,
  actions,
  mutations,
  getters
};
  