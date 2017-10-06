import Vue from 'vue';
import Vuex from 'vuex';
Vue.use(Vuex);

const store = new Vuex.Store({
    strict: true,
    state: {
        user: null,
        token: ''
    },
    getters: {
        formatUsername(state) {
            if (!state.user)
                return '';

            return state.user.username.toLowerCase().split(' ').map((word) => {
                return word.replace(word[0], word[0].toUpperCase());
            }).join(' ');
        },
        getToken(state) {
            return state.token;
        },
        getUserData(state) {
            return state.user;
        },
        userExists(state) {
            return state.user ? true : false;
        }
    },
    mutations: {
        setUser(state, data) {
            state.user = data.user;
            state.token = data.token;
            window.localStorage.setItem('user', JSON.stringify(data));
        },
        removeUser(state) {
            state.user = null;
            state.token = '';
            window.localStorage.removeItem('user');
        }
    }
});

export default store;
