import httpClient from "../http-common";

httpClient.interceptors.request.use((request) => {
  let token = localStorage.getItem("token");
  if (token) {
    request.headers.Authorization = `Bearer ${token}`;
  }
  return request;
});
const getAll = (path) => {
  return httpClient.get(path);
};

const create = (path, data) => {
  return httpClient.post(path, data);
};

const get = (path, id) => {
  return httpClient.get(`${path}/${id}`);
};

const update = (path, data) => {
  return httpClient.put(`${path}`, data);
};

const remove = (path, id) => {
  return httpClient.delete(`/${path}/${id}`);
};

const getSubordinate = (path, id, subordinate) => {
  return httpClient.get(`/${path}/${id}/${subordinate}`);
};

const uploadFile = (path, data) => {
  return httpClient.post(`/${path}`, data);
};
const login = (data) => {
  return httpClient.post("auth/login", data);
};

const validateUserName = (userName) => {
  return httpClient.get(`users/validate-username/${userName}`);
};

const validateEmail = (email) => {
  return httpClient.get(`users/validate-email/${email}`);
};
const validateTermName = (data) => {
  return httpClient.post("terms/existed-validate", data);
};

const verifyToken = (token) => {
  return httpClient.get(`auth/verify-token-session/${token}`)
}

const getPopularTopics = (path) => {
  return httpClient.get(path)
}

const service = {
  getAll,
  create,
  get,
  update,
  remove,
  getSubordinate,
  uploadFile,
  login,
  validateEmail,
  validateUserName,
  validateTermName,
  getPopularTopics,
  verifyToken,
};
export default service;
