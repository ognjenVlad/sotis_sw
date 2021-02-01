import {API_ENDPOINT} from '@constants/endpoints'

const ENDPOINTS = {
  DOMAINS: 'domain',
  STUDENTS: 'user/student',
  SUBJECTS: 'subjects',
  FIRST_QUESTION: 'exam/first/',
  LOAD_QUESTION: 'ks/nextQuestion',
  SUBMIT_ANSWER: 'exam/submit-answer',
  STUDENT_DATA: 'studentData'
}

export const studentsService = {
  loadStudents,
  loadQuestion,
  submitAnswer,
  loadStudentData
};

async function loadStudents(axios) {
  const students = await axios.get(API_ENDPOINT + ENDPOINTS.STUDENTS)
  return students.data
}

async function loadQuestion(axios, data) {
  const question = await axios.post(API_ENDPOINT + ENDPOINTS.LOAD_QUESTION, {examId: data.examId, questionId: data.questionId, choiceId: data.choiceId})
  return question.data
}

async function submitAnswer (axios, data) {
  console.log(data)
  const question = await axios.post(API_ENDPOINT + ENDPOINTS.LOAD_QUESTION, {examId: data.examId, questionId: data.questionId, choiceId: data.choiceId})
  console.log(question.data)
  return question.data
}

async function loadStudentData (axios, data) {
  return await axios.get(API_ENDPOINT + ENDPOINTS.STUDENT_DATA + '/' + data.id)
}
