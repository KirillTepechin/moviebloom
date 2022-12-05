import * as VueRouter from 'vue-router'
import { createApp } from 'vue'
import App from './App'
import '../node_modules/bootstrap/dist/css/bootstrap.css';
import axios from "axios";

import PrimeVue from 'primevue/config';

import ActorAdminPage from "@/pages/ActorAdminPage";
import HomePage from "@/pages/HomePage";
import MovieAdminPage from "@/pages/MovieAdminPage";
import LoginPage from "@/pages/LoginPage";
import RegisterPage from "@/pages/RegisterPage";
import MoviesPage from "@/pages/MoviesPage";
import ActorPage from "@/pages/ActorPage";
import MoviePage from "@/pages/MoviePage";

const NotFound = { template: '<h2>Страница не найдена(</h2>' }
const routes = [
    { path: '/', component: HomePage, meta:{reload: true} },
    { path: '/admin/actors',component: ActorAdminPage },
    { path: '/admin/movies',component: MovieAdminPage },
    { path: '/auth',component: LoginPage },
    { path: '/register',component: RegisterPage },
    { path: '/movies',component: MoviesPage },
    { path: '/actors',component: ActorPage },
    { path: '/movie/:id',component: MoviePage },
    { path: '/:pathMatch(.*)*', component: NotFound },

]
const router = new VueRouter.createRouter({
    history: VueRouter.createWebHistory(),
    routes // сокращённая запись для `routes: routes`
})

const token = localStorage.getItem('jwt')
if (token) {
    axios.defaults.headers.common['Authorization'] = 'Bearer '+token
}
axios.interceptors.response.use( null,function (error) {
    if(error.response.status===403){
        alert("Недостаточно прав")
    }
    else if(error.response.status===404){
        alert(error.response.data)
    }
    else if(error.response.status ===400 && error.response.data.toString().startsWith("Пользователь")){
        alert(error.response.data)
    }
    else if(error.response.status ===400){
        alert("Проверьте данные форм")
    }
    console.log(error)
});

createApp(App)
    .use(router)
    .use(PrimeVue)
    .provide('$axios', axios)
    .mount('#app')

