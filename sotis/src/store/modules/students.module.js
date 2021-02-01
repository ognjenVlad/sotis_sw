import {studentsService} from '@services/students.service'

export const state = () => ({
  question: null,
  studentData: null
})

export const getters = {
  currentQuestion: state => () => {
    return state.question
  },
  studentData: state => () => {
    return state.studentData
  },
}

export const mutations = {
  setQuestion(state, question) {
    state.question = question
  },
  setStudentData(state, data) {
    state.studentData = data
  }
}

export const actions = {
  async LOAD_EXAMS_FIRST_QUESTION ({commit}, data) {
    const question = await studentsService.loadExamsFirstQuestion(data.axios, data.id)
    commit('setQuestion', question)
  },
  async LOAD_QUESTION ({commit}, data) {
    const question = await studentsService.loadQuestion(data.axios, data)
    commit('setQuestion', question)
  },
  async SUBMIT_ANSWER ({commit}, data) {
    const nextQuestion = await studentsService.submitAnswer(data.axios, {examId: data.examId, questionId: data.questionId, choiceId: data.choiceId})
    commit('setQuestion', nextQuestion)
    // await dispatch('LOAD_NEXT_QUESTION', {id: data.examId})
  },
  async LOAD_STUDENT_DATA ({commit}, data) {
    const result = await studentsService.loadStudentData(data.axios, data.id)
    commit('setStudentData', result)
  },
  RESET_CURRENT_QUESTION ({commit}) {
    commit('setQuestion', null)
  }
}

export default {
  state,
  actions,
  mutations,
  getters
};
  