import axios from 'axios';

const api = axios.create({
  baseURL: import.meta.env.VIT_API_URL || 'http://localhost:8080/api',
  withCredentials: true, // sem isso o browser descarta o Set-Cookie e nunca reenvia a sessão
  headers: {
    'Content-Type': 'application/json',
  },
  
});

export default api;