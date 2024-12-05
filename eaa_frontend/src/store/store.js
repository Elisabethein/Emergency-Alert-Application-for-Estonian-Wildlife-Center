// src/store.js
import { createStore } from 'vuex';

const store = createStore({
    state() {
        return {
            loggedIn: localStorage.getItem('loggedIn') === 'true',
            roles: [],
            regions: [],
            user: null,
            token: localStorage.getItem('token') || null,
        };
    },
    mutations: {
        login(state, { user, roles, regions, token }) {
            state.loggedIn = true;
            state.user = user; // Store user information
            state.roles = roles; // Store user roles
            state.regions = regions; // Store user regions
            state.token = token; // Store the token
            localStorage.setItem('loggedIn', 'true'); // Save login status
            localStorage.setItem('user', JSON.stringify(user)); // Save user info
            localStorage.setItem('roles', JSON.stringify(roles)); // Save roles
            localStorage.setItem('regions', JSON.stringify(regions)); // Save regions
            localStorage.setItem('token', token); // Save the token
        },
        logout(state) {
            state.loggedIn = false;
            state.user = null; // Clear user info
            state.roles = []; // Clear roles
            state.regions = []; // Clear regions
            state.token = null; // Clear token
            localStorage.setItem('loggedIn', 'false'); // Save logout status
            localStorage.removeItem('user'); // Remove user info from localStorage
            localStorage.removeItem('roles'); // Remove roles from localStorage
            localStorage.removeItem('regions'); // Remove regions from localStorage
            localStorage.removeItem('token'); // Remove token from localStorage
        },
        setUser(state, user) {
            state.user = user; // Set user info
            localStorage.setItem('user', JSON.stringify(user)); // Save user info
        },
        setRoles(state, roles) {
            state.roles = roles; // Set user roles
            localStorage.setItem('roles', JSON.stringify(roles)); // Save roles
        },
        setRegions(state, regions) {
            state.regions = regions; // Set user regions
            localStorage.setItem('regions', JSON.stringify(regions)); // Save regions
        },
        setToken(state, token) {
            state.token = token; // Set the token
            localStorage.setItem('token', token); // Save the token
        },
    },
    actions: {
        login({ commit }, payload) {
            commit('login', payload); // Call login mutation with payload
        },
        logout({ commit }) {
            commit('logout'); // Call logout mutation
        },
        loadUser({ commit }) {
            const user = JSON.parse(localStorage.getItem('user'));
            const roles = JSON.parse(localStorage.getItem('roles'));
            const regions = JSON.parse(localStorage.getItem('regions'));
            const token = localStorage.getItem('token'); // Retrieve token from localStorage

            if (token && user) {
                commit('setUser', user); // Restore user info
                commit('setRoles', roles); // Restore roles
                commit('setRegions', regions); // Restore regions
                commit('setToken', token); // Restore token
            }
        },
    },
    getters: {
        isLoggedIn: (state) => state.loggedIn,
        getUser: (state) => state.user,
        getRoles: (state) => state.roles,
        getRegions: (state) => state.regions,
        getToken: (state) => state.token, // Getter for the token
    },
});

export default store;
