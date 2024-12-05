import { createApp } from 'vue';
import App from './App.vue';
import router from './routes/router.js';
import store from './store/store.js';

const app = createApp(App);

// Use the router
app.use(router);
// Use the store
app.use(store);
// Mount the app
app.mount('#app');
