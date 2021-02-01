import {studentsService} from '@services/students.service'
import {dataService} from '@services/data.service'

export const state = () => ({
  users: [],
  students: []
})

export const getters = {
  users: state => () => {
    return state.users
  },
  students: state => () => {
    return state.students
  }
}

export const mutations = {
  setUsers (state, users) {
    state.users = users
  },
  setStudents (state, students) {
    state.students = students
  },
  addUser (state, user) {
    state.users.push(user)
  }
}

export const actions = {
  async LOAD_USERS ({ commit }, axios) {
    const users = await dataService.loadUsers(axios)
    if (users) {
      commit('setUsers', users)
    }
  },
  async LOAD_STUDENTS ({ commit }, axios) {
    const students = await studentsService.loadStudents(axios)
    if (students) {
      commit('setStudents', students)
    }
  },
  // eslint-disable-next-line no-unused-vars
  async ADD_STUDENT_TO_SUBJECT ({dispatch}, data) {
    try {
      await dataService.addStudentToSubject(data.axios, {id: data.subject.id, title: data.subject.title, students: data.students})
      dispatch('LOAD_SUBJECTS', data.axios)
    } catch (e) {
      console.log(e)
    }
  }
}

export default {
  state,
  actions,
  mutations,
  getters
};
  