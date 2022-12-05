<template>
  <div class="container">
    <h1 class="text-center"> Список фильмов</h1>
    <div class="input-group mb-3">
      <div class="input-group-prepend">
        <span style="color:whitesmoke; background-color: forestgreen" class="input-group-text" id="basic-addon3">Найти по названию</span>
      </div>
      <input type="text" v-model="search" class="form-control" id="basic-url" aria-describedby="basic-addon3">
    </div>

    <table  class="table" style="vertical-align: middle;" >
      <thead>
      <th v-if="this.userRole==='ADMIN'">Id</th>
      <th>Название</th>
      <th>Постер</th>
      <th>Описание</th>
      <th>Год</th>
      <th>Актеры</th>
      <th>Жанры</th>
      <th v-if="this.userRole==='ADMIN'"></th>
      </thead>
      <tbody>
      <tr v-for="movie in filter" :key="movie.id">
        <td v-if="this.userRole==='ADMIN'">{{movie.id}}</td>
        <td>
          <router-link :to="movieLink(movie.id)">
            {{movie.name}}
          </router-link>
        </td>
        <td><img v-bind:src="'/img/' + movie.poster" style="object-fit: scale-down; max-height: 100px"></td>
        <td>{{movie.description}}</td>
        <th>{{movie.year}}</th>
        <th>{{movie.actors.map((actor)=>actor.firstName+' '+actor.lastName).join(', ')}}</th>
        <th>{{movie.genres.join(', ')}}</th>
        <td v-if="this.userRole==='ADMIN'"><input width=20% type="button" value="Удалить" @click="deleteMovie(movie.id)" />
          <input type="button" value="Обновить" @click="updateMovie(movie)" /></td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import MovieService from "@/services/MovieService";
import UserService from "@/services/UserService";

export default {
  data(){
    return {userRole:"", search: '',}
  },
  props:{
    movies:[]
  },
  methods:{
    deleteMovie(id){
      MovieService.deleteMovie(id).then((response)=>{
        this.$emit("delete", response.data)
      });
    },
    updateMovie(movie){
      MovieService.getMovieData(movie.id).then((response)=>{
        this.$emit("set", response.data)
      })
    },
    me(){
      UserService.me().then(response => {
        this.userRole = response.data.role
      })
    },
    movieLink(movieId){
      return "/movie/"+movieId
    }

  },
  computed: {
    filter() {
      return this.movies.filter(item => item.name.indexOf(this.search) !== -1)
    },

  },
  created() {
    this.me()
  },

}
</script>

<style>

</style>