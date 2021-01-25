import {API_ENDPOINT} from '@constants/endpoints'

const ENDPOINTS = {
  DOMAINS: 'domain',
  EXAMS: 'exam'
}

export const dataService = {
  loadExams,
  loadDomains,
  addDomain
};

async function loadDomains(axios) {
  const domains = await axios.get(API_ENDPOINT + ENDPOINTS.DOMAINS)
  return domains.data
}

async function loadExams(axios) {
  const exams = await axios.get(API_ENDPOINT + ENDPOINTS.EXAMS)
  return exams.data
}

async function addDomain(axios, data) {
  return await axios.post(API_ENDPOINT + ENDPOINTS.EXAMS, data)
}