import {API_ENDPOINT} from '@constants/endpoints'

const ENDPOINTS = {
  LOGIN: 'user/login',
  REGISTER: 'user/register'
}

export const authService = {
  login,
  register,
  logout
};

function logout() {
  localStorage.removeItem('authtoken');
}


async function login(axios, data) {
  const user = await axios.post(API_ENDPOINT + ENDPOINTS.LOGIN, {...data})
  return user.data
}

async function register(axios, data) {
  console.log(data)
  let user
  try {
    user = await axios.post(API_ENDPOINT + ENDPOINTS.REGISTER, {...data})

  } catch (e) {
    console.log(e)
  }
  console.log('AAA', user)
  return user.data
}