import axios from "axios";
import {networkMessage} from "@/utils/status";
import {ElMessage} from "element-plus";

// 设置超时时间
axios.defaults.timeout = 60000;

// 请求地址
axios.defaults.baseURL = import.meta.env.VITE_API_BASE_URL;

// 拦截器
axios.interceptors.request.use(
  config => {
    // 配置请求头
    if (config.url.includes('/code/uploadFile')) {
      Object.assign(config.headers, {
        'Content-Type': 'multipart/form-data'
      })
    } else {
      Object.assign(config.headers, {
        'Content-Type': 'application/json;charset=UTF-8'
      })
    }
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

axios.interceptors.response.use(
  response => {
    // todo 具体拦截逻辑，取决于接口状态码
    return response.data.content;
  },
  error => {
    const { response } = error;
    if (response) {
      // 请求已发出，但不在 2xx 的范围
      ElMessage.warning(networkMessage(response.status));
    } else {
      ElMessage.warning('网络连接异常，请稍候再试！');
    }
  }
);

export function request(requestParams: {
  url: string
  params?: any
  data?: any
  headers?: any
  type: string
}) {
  return new Promise((resolve, reject) => {
    let promise;
    const { url, headers, params, data, type } = requestParams;
    if (type.toUpperCase() === 'GET') {
      promise = axios({
        url,
        headers,
        params
      });
    } else if (type.toUpperCase() === 'POST') {
      promise = axios({
        method: 'POST',
        url,
        headers,
        params,
        data,
      });
    }

    promise?.then(res => {
      resolve(res);
    }).catch(err => {
      reject(err);
    });
  })
}
