import axios from "axios";

let baseURL = process.env.VUE_APP_HPPM_API_URL;
let instance = axios.create({
  baseURL: baseURL,
  // headers: {
  //     "Content-type": "application/json"
  // }
});

export const AXIOS = instance