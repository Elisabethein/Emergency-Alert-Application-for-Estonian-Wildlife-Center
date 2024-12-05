import { createRouter, createWebHistory } from 'vue-router';
import { useStore } from 'vuex';
import KnowledgeBase from '../pages/KnowledgeBase.vue';
import StatisticsPage from '../pages/StatisticsPage.vue';
import TicketManagement from '../pages/TicketManagementPage.vue';
import UserManagement from '../pages/UserManagementPage.vue';
import RegisterPage from "../pages/RegisterPage.vue";
import WelcomePage from "@/pages/WelcomePage.vue";
import LoginPage from "@/pages/LoginPage.vue";
import ProfilePage from "@/pages/ProfilePage.vue";
import AlertPage from "@/pages/AlertPage.vue";
import ApplicationManagement from "@/pages/ApplicationManagement.vue";
import MyCasesPage from '@/pages/MyCasesPage.vue';
import SettingsPage from "@/pages/SettingsPage.vue";

const routes = [
    {
        path: '/',
        name: 'WelcomePage',
        component: WelcomePage,
    },
    {
        path: '/register',
        name: 'Register',
        component: RegisterPage,
    },
    {
        path: "/login",
        name: "Login",
        component: LoginPage,
    },
    {
        path: "/alert",
        name: "Alert",
        component: AlertPage,
    },
    {
        path: '/ticket-management',
        name: 'TicketManagement',
        component: TicketManagement,
        meta: { requiresAuth: true },
    },
    {
        path: '/my-cases',
        name: 'MyCases',
        component: MyCasesPage,
    },
    {
        path: '/knowledge-base',
        name: 'KnowledgeBase',
        component: KnowledgeBase,
        meta: { requiresAuth: true },
    },
    {
        path: '/statistics',
        name: 'Statistics',
        component: StatisticsPage,
        meta: { requiresAuth: true },
    },

    {
        path: '/user-management',
        name: 'UserManagement',
        component: UserManagement,
        meta: { requiresAuth: true},
    },
    {
        path: "/profile",
        name: "ProfilePage",
        component: ProfilePage,
        meta: { requiresAuth: true },
    },
    {
        path: "/application-management",
        name: "ApplicationManagement",
        component: ApplicationManagement,
        meta: { requiresAuth: true, requiresRoles: ['Juhtkond', 'Päevajuht', 'Piirkonnajuht']  },
    },
    {
        path: "/settings",
        name: "Settings",
        component: SettingsPage,
        meta: { requiresAuth: true, requiresRoles: ['Juhtkond'] },
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});
router.beforeEach((to, from, next) => {
    const store = useStore();
    const isLoggedIn = store.getters.isLoggedIn;

    // Check if the user is accessing the root route (/)
    if (to.name === 'WelcomePage' && isLoggedIn) {
        // If the user is logged in and visiting '/', redirect to KnowledgeBase
        store.dispatch('loadUser')
            .then(() => {
                store.state.loggedIn = true;
                next({ name: 'KnowledgeBase' });
            })
            .catch(() => {
                store.commit('logout');
                next({ name: 'Login' });
            });
    }
    // Check if the route requires authentication
    else if (to.matched.some(record => record.meta.requiresAuth) && !isLoggedIn) {
        next({ name: 'Login' }); // Redirect to Login page if not authenticated
    }
    // Check if the user has the required role to access the route
    else if (to.matched.some(record => record.meta.requiresRoles)) {
        const requiredRoles = to.meta.requiresRoles;
        const userRoles = store.getters.getRoles;

        const hasRequiredRole = requiredRoles.some(role => userRoles.includes(role));

        if (hasRequiredRole) {
            next();
        } else {
            next({ name: 'KnowledgeBase' });
        }
    } else {
        next();
    }
});

export default router;
