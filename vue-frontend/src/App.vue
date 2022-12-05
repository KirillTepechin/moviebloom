<template>
  <nav class="navbar sticky-top navbar-expand navbar-dark bg-black">
      <a style="background-color:white; color: forestgreen; width: 20%" class="navbar-brand" href="/">
        <img src="/favicon.ico" alt="">
        moviebloom
      </a>
      <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
          <ul class="navbar-nav me-auto mb-2 mb-lg-0" id="add">
            <li v-if="user.role!=='ADMIN'" class="nav-item">
              <a class="nav-link" href="/movies">Фильмы </a>
            </li>
            <li v-if="user.role!=='ADMIN'" class="nav-item">
              <a class="nav-link" href="/actors">Актеры </a>
            </li>
            <li v-if="user.role==='ADMIN'" class="nav-item">
              <a class="nav-link" href="/admin/movies">Фильмы(админ) </a>
            </li>
            <li v-if="user.role==='ADMIN'" class="nav-item">
              <a class="nav-link" href="/admin/actors">Актеры(админ) </a>
            </li>
            <p>Лучший фильм - </p>
            <a id="best"></a>
            <p id="rate"></p>
          </ul>
          <ul class="nav navbar-nav navbar-right">
            <li class="nav-item">
              <a class="nav-link"  href="/auth">Войти </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="/register">Зарегистрироваться </a>
            </li>
            <li class="nav-item" v-if="this.user.role">
              <a class="nav-link" v-on:click="logout" href="/auth">Выйти </a>
            </li>
            <li class="nav-item" v-if="this.user.role">
              <p>{{this.user.login}}</p>
            </li>
          </ul>
      </div>
  </nav>
  <router-view></router-view>
</template>

<script>
import UserService from "@/services/UserService";
import SockJS from 'sockjs-client'
import { Stomp } from '@stomp/stompjs'
import $ from 'jquery'

let stompClient = null

export default {
  name: "App",
  data(){
    return {user:{}}
  },
  methods:{
    connect() {
      const url = 'http://localhost:9000/gs-guide-websocket'
      stompClient = Stomp.over(() => new SockJS(url));
      stompClient.debug = () => {}
      stompClient.connect({},(frame) => {
        console.log("Connected -"+frame)
        stompClient.subscribe('/topic/activity', message => {
          this.showBest(JSON.parse(message.body))
        })
      })
    },
    showBest(message){
      $("#best").replaceWith('<a id="best" href='+"http://localhost:8080/movie/"+ message.id +">" +  message.name + '</a>')
      $("#rate").replaceWith('<p id="rate">Оценка:'+message.averageRate+'</p>')
      console.log(message)
    },
    getUser(){
      UserService.me().then((response) => {
        this.user = response.data != null ? response.data : null
      })
    },
    logout(){
      localStorage.setItem('jwt','')
      this.user.role = ''
      this.user.login = ''
    }
  },
  created() {
    this.getUser()
  },
  mounted() {
    this.connect()
  }
}
</script>

<style>
#app {
  font-family: Rubik,ui-rounded;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: forestgreen;
  background-color: black;
}
#best,#rate{
  padding-left:5px;
}
form > button{
  background-color: forestgreen;
  color: whitesmoke;
  width: 20%;
  height: 50px;
  border-radius: 99em;
  border-color: whitesmoke;
  align-self: flex-end;

}
input[type=button]{
  background-color: forestgreen	;
  color: whitesmoke;
  border-radius: 99em;
  border-color: white;
}
::-webkit-file-upload-button {
  background: forestgreen;
  color: whitesmoke;
  padding: 1em;
  border-color: whitesmoke;
  border-radius: 99em;
}
#app > div > table{
  color:whitesmoke;
}
</style>