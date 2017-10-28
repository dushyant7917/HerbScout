import axios from 'axios';
import router from './../router/index';
import store from './../store';

const ERROR_MSG = 'Something Happened';
const BASE_URL = 'http://localhost:3000';

var handleError = (error, message) => {
    if (error)
        if (error.response)
            if (error.response.status === 403) {
                store.commit('removeUser');
                router.push({
                    path: '/login'
                });
                return {
                    error: message
                };
            }

    console.log(error);
    return {
        error: message
    };
};

var loginUser = (username, password) => {
    return axios.post(`${BASE_URL}/auth/login`, {
            username: username,
            password: password
        })
        .then(response => {
            return response.data;
        })
        .catch(error => {
            return handleError(error, 'Unable to login. Please try again');
        });
};

var registerUser = (username, password) => {
    return axios.post(`${BASE_URL}/auth/register`, {
            username: username,
            password: password
        })
        .then(response => {
            return response.data;
        })
        .catch(error => {
            return handleError(error,
                'Unable to register user. If its our problem, we are looking into it.');
        });
};

var sendPlantImage = (fileBase64) => {
    return axios.post(`${BASE_URL}/search/plant_image`, {
            base64Content: fileBase64
        })
        .then(response => {
            return response.data;
        })
        .catch(error => {
            return handleError(error, ERROR_MSG);
        });
};

var searchPlantInfo = (searchText) => {
    let encodedData = encodeURIComponent(searchText);

    return axios.get(`${BASE_URL}/search/plant_info?query=${encodedData}`)
        .then(response => {
            return response.data;
        })
        .catch(error => {
            return handleError(error, ERROR_MSG);
        });
};

var getSpecificPlant = (plantName) => {
    return axios.get(`${BASE_URL}/search/get_specific_plant?name=${plantName}`)
        .then(response => {
            return response.data;
        })
        .catch(error => {
            return handleError(error, ERROR_MSG)
        });
};

var autoComplete = (query) => {
    return axios.get(`${BASE_URL}/search/auto_complete?query=${query}`)
        .then(response => {
            return response.data;
        })
        .catch(error => {
            return handleError(error, ERROR_MSG);
        });
};

export {
    loginUser,
    registerUser,
    sendPlantImage,
    searchPlantInfo,
    getSpecificPlant,
    autoComplete
};
