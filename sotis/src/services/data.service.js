import {API_ENDPOINT} from '@constants/endpoints'

const ENDPOINTS = {
  DOMAINS: 'domain',
  EXAMS: 'exam',
  SUBJECTS: 'subject'
}

export const dataService = {
  loadExams,
  loadDomains,
  addSubject,
  loadSubjects,
  addStudentToSubject,
  addExam,
  deleteExam,
  deleteSubject,
  editExam
};

async function loadDomains(axios) {
  const domains = await axios.get(API_ENDPOINT + ENDPOINTS.DOMAINS)
  return domains.data
}

async function editExam(axios, data) {
  const exam = await axios.post(API_ENDPOINT + ENDPOINTS.EXAMS, data)
  return exam.data
}

async function addExam(axios, data) {
  return await axios.put(API_ENDPOINT + ENDPOINTS.EXAMS, data)
}

async function loadExams(axios) {
  const exams = await axios.get(API_ENDPOINT + ENDPOINTS.EXAMS + '/all')
  return exams.data
}

async function addSubject(axios, data) {
  return await axios.put(API_ENDPOINT + ENDPOINTS.SUBJECTS, data)
}

async function loadSubjects(axios) {
  const subjects = await axios.get(API_ENDPOINT + ENDPOINTS.SUBJECTS)
  return subjects.data
}

async function addStudentToSubject(axios, data) {
  return await axios.post(API_ENDPOINT + ENDPOINTS.SUBJECTS, data)
}

async function deleteExam(axios, data) {
  return await axios.delete(API_ENDPOINT + ENDPOINTS.EXAMS + '/' + data.id)
}

async function deleteSubject(axios, data) {
  return await axios.delete(API_ENDPOINT + ENDPOINTS.SUBJECTS + '/' + data.id)
}
