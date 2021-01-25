import {dataService} from '@services/data.service'

export const state = () => ({
  exams: []
})

export const getters = {
  exams: state => () => {
    return state.exams
  }
}

export const mutations = {
  setExams (state, exams) {
    state.exams = exams
  }
}

export const actions = {
  async LOAD_EXAMS ({ commit }, axios) {
    const exams = await dataService.loadExams(axios)
    if (exams) {
      commit('setExams', exams)
    }
  },
  ADD_EXAM ({}, data) {
    await dataService.addExam(data.axios, data)
    dispatch('LOAD_EXAMS', data.axios)
  }
}

export default {
  state,
  actions,
  mutations,
  getters
};
  