// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue';
import App from './App';
import Vuetify from 'vuetify';
import * as VueGoogleMaps from 'vue2-google-maps';

import store from './store';
import router from './router/index';

import 'vuetify/dist/vuetify.min.css';
import './assets/index.css';

Vue.config.productionTip = false;
Vue.use(Vuetify);
Vue.use(store);
Vue.use(VueGoogleMaps, {
    load: {
        key: 'AIzaSyAbsq_M1w2w6u6g2zR6VoblO7_g5iwitiY',
        libraries: 'places'
    }
});

Vue.filter('formatName', (value) => {
    return value.split('-').map(element => {
        return element.charAt(0).toUpperCase() + element.slice(1);
    }).join(' ');
});

router.beforeEach((to, from, next) => {
    if (window.localStorage.getItem('user') != null && store.state.user === null)
        store.commit('setUser', JSON.parse(window.localStorage.getItem('user')));
    if (to.path === '/') {
        if (window.localStorage.getItem('user') !== null)
            router.push({
                path: 'dashboard'
            });
        else
            next();
    } else if (to.path === '/dashboard') {
        if (window.localStorage.getItem('user') === null)
            router.push({
                path: 'login'
            });
        else
            next();
    } else
        next();
});

/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    store,
    template: '<App/>',
    components: {
        App
    }
})
