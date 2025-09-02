// axios实例
window.instance = axios.create({
    baseURL: 'http://127.0.0.1:8888',
    timeout: 30000
});

// 请求拦截器
instance.interceptors.request.use(
    config => {
        // 添加认证token
        const token = Cookies.get('userToken');
        if (token) {
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    error => {
        return Promise.reject(error);
    }
);

// 响应拦截器
instance.interceptors.response.use(
    response => {
        return response;
    },
    error => {
        if (error.response && error.response.status === 401) {
            // 认证失败，跳转到登录页
            Cookies.remove('userToken');
            Cookies.remove('userInfo');
            window.location.href = 'login.html';
        }
        return Promise.reject(error);
    }
);