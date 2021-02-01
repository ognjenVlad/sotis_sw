import {API_ENDPOINT} from '@constants/endpoints'

const ENDPOINTS = {
  KS: 'ks',
  GRAPH_DATA: 'ks/problems',
  GENERATE_REAL_KS: 'ks'
}

export const graphService = {
  loadGraph,
  addGraph,
  loadGraphData,
  generateGraph
};

async function loadGraph(axios) {
  const graph = await axios.get(API_ENDPOINT + ENDPOINTS.KS)
  return graph.data
}

async function addGraph(axios, data) {
  const edges = data.graph.links.map((item) => {
    return {source: item.sid, destination: item.tid}
  })
  const nodes = data.graph.nodes.map((item) => {
    return {id: item.id}
  })
  const graph = await axios.put(API_ENDPOINT + ENDPOINTS.KS, {nodes, edges, subject_id: data.subject_id})
  return graph.data
}

async function loadGraphData(axios, id) {
  const graph = await axios.get(API_ENDPOINT + ENDPOINTS.GRAPH_DATA + '/' + id)
  return graph.data
}

async function generateGraph(axios, examId) {
  const graph = await axios.get(API_ENDPOINT + ENDPOINTS.GENERATE_REAL_KS + '/' + examId)
  return graph.data
}