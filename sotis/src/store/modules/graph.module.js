import {graphService} from '@services/graphs.service'

export const state = () => ({
  subjectGraph: {},
  examGraph: {},
  addedLinks: [],
  graphNodes: []
})

export const getters = {
  examGraph: state => () => {
    return state.examGraph
  },
  subjectGraph: state => () => {
    return state.subjectGraph
  },
  addedLinks: state => () => {
    return state.addedLinks
  },
  graphNodes: state => () => {
    return state.graphNodes
  }
}

export const mutations = {
  setExamGraph (state, graph) {
    state.examGraph = graph
  },
  setSubjectGraph (state, graph) {
    state.subjectGraph = graph
  },
  addLink (state, link) {
    state.addedLinks.push(link)
  },
  setGraphData (state, data) {
    state.graphNodes = data.nodes
    state.addedLinks = data.edges
  },
  resetAddedGraphData (state) {
    state.graphNodes = []
    state.addedLinks = []
  },
  removeLink (state, data) {
    const index = state.addedLinks.findIndex(item => {
      return item.source === data.source && item.destination === data.destination
    })
    state.addedLinks.splice(index, 1)
  }
}

export const actions = {
  async LOAD_SUBJECT_GRAPH ({ commit }, data) {
    const graph = await graphService.loadGraph(data.axios, data.id)
    if (graph) {
      commit('setSubjectGraph', graph)
    }
  },
  async ADD_GRAPH ({ commit }, data) {
    const graph = await graphService.addGraph(data.axios, data)
    if (graph) {
      commit('setSubjectGraph', graph)
    }
  },
  RESET_ADDED_GRAPH_DATA({ commit }) {
    commit('resetAddedGraphData')
  },
  ADD_LINK ({commit}, data) {
    commit('addLink', {source: data.source, destination: data.destination})
  },
  DELETE_LINK ({commit}, data) {
    commit('removeLink', {source: data.source, destination: data.destination})
  },
  RESET_GRAPH ({commit}) {
    commit('setExamGraph', null)
  },
  async LOAD_SUBJECT_GRAPH_DATA({commit}, data) {
    const graph = await graphService.loadGraphData(data.axios, data.subjectId)
    console.log(graph)
    if (graph) {
      commit('setGraphData', graph)
      commit('setSubjectGraph', graph)
    }
  },
  async GENERATE_EXAM_GRAPH ({commit}, data) {
    const graph = await graphService.generateGraph(data.axios, data.exam_id)
    console.log(graph)
    if (graph) {
      commit('setExamGraph', graph)
    }
  }
}

export default {
  state,
  actions,
  mutations,
  getters
};
  