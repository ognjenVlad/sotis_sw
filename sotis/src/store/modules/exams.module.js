import {dataService} from '@services/data.service'

export const state = () => ({
  exams: [],
  addedAnswers: [],
  addedQuestions: [],
  question: null
})

export const getters = {
  exams: state => () => {
    return state.exams
  },
  addedAnswers: state => () => {
    return state.addedAnswers
  },
  addedQuestions: state => () => {
    return state.addedQuestions
  },
  currentQuesiton: state => () => {
    return state.question
  }
}

export const mutations = {
  setExams (state, exams) {
    state.exams = exams
  },
  addAnswer (state, answer) {
    state.addedAnswers.push(answer)
  },
  addQuestion (state, question) {
    question.choices = [...state.addedAnswers]
    state.addedAnswers = []
    state.addedQuestions.push(question)
  },
  deleteAnswer (state, answer) {
    const index = state.addedAnswers.findIndex(item => {
      return item.id === answer.id
    })
    state.addedAnswers.splice(index, 1)
  },
  deleteQuestion (state, question) {
    const index = state.addedQuestions.findIndex(item => {
      return item.id === question.id
    })
    state.addedQuestions.splice(index, 1)
  },
  createExam (state, exam) {
    state.addedQuestions = []
    state.exams.push(exam)
  }
}

export const actions = {
  async LOAD_EXAMS ({ commit }, axios) {
    const exams = await dataService.loadExams(axios)
    if (exams) {
      commit('setExams', exams)
    }
  },
  async ADD_EXAM ({dispatch}, data) {
    await dataService.addExam(data.axios, data)
    dispatch('LOAD_EXAMS', data.axios)
  },
  ADD_ANSWER ({commit}, data) {
    commit('addAnswer', data.answer)
  },
  DELETE_ANSWER ({commit}, data) {
    commit('deleteAnswer', data.answer)
  },
  ADD_QUESTION ({commit}, data) {
    commit('addQuestion', data)
  },
  DELETE_QUESTION ({commit}, data) {
    commit('deleteQuestion', data.question)
  },
  async CREATE_EXAM ({commit, state}, data) {
    data.questions = [...state.addedQuestions]
    const exam = await dataService.addExam(data.axios, data)
    if (exam) {
      commit('createExam', data)
    }
  },
  async DELETE_EXAM ({commit}, data) {
    const exam = await dataService.deleteExam(data.axios, data)
    if (exam) {
      commit('deleteExam', data)
    }
  }
}

export default {
  state,
  actions,
  mutations,
  getters
};
  