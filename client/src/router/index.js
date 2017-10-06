import Vue from 'vue';
import Router from 'vue-router';
import Home from './../components/Home';
import Login from './../components/Login';
import Search from './../components/Search';

Vue.use(Router);

export default new Router({
    mode: 'history',
    routes: [{
            path: '/',
            name: 'Home',
            component: Home
        }, {
            path: '/login',
            name: 'Login',
            component: Login
        },
        {
            path: '/dashboard',
            name: 'DashBoard',
            component: Home
        },
        {
            path: '/search',
            component: Search,
            name: 'Search'
        },
        {
            path: '*',
            redirect: '/'
        }
    ]
});
