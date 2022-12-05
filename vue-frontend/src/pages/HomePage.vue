<template>
  <div class="container">
    <h1 class="text-center"> Топ 20 Moviebloom</h1>
    <table class="table" style="vertical-align: middle;" >
      <thead>
      <th></th>
      <th></th>
      <th></th>
      <th>Ваша оценка</th>
      <th>Оценка Moviebloom</th>
      </thead>
      <tbody>
      <tr v-for="movie in movies" :key="movie.id">
        <td>{{movies.indexOf(movie)+1}}</td>
        <td>{{movie.name}}</td>
        <td><img v-bind:src="'/img/' + movie.poster" style="object-fit: scale-down;max-height: 100px"></td>
        <td style="width:30%">
          <star-rating @click="rate(movie.id)" @update:rating="setRating"
                       v-bind:increment="0.5"
                       v-bind:rating = "getRate(movie.id)"
                       v-bind:max-rating="10"
                       v-bind:clearable="true"
                       v-bind:animate="true"
                       inactive-color="grey"
                       active-color="forestgreen"
                       v-bind:star-size="25">
          </star-rating>
        </td>
        <td style="color:forestgreen" >{{movie.averageRate}}</td>
      </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import MovieService from "@/services/MovieService";
import StarRating from 'vue-star-rating'
import UserService from "@/services/UserService";
import RateService from "@/services/RateService";

export default {
  name: "HomePage",
  components: {
    StarRating
  },
  data() {
    return {
      movies: [],
      rates:[],
      currentRate:0,
    }
  },
  methods: {
    setRating(rating){
        this.currentRate = rating;
    },
    rate(movieId){
        if(this.currentRate===0){
          RateService.unrate(movieId)
          console.log("unrate")
        }
        else{
          RateService.rate(movieId, this.currentRate)
          console.log("rate")
        }
    },
    getMovies() {
      MovieService.getTop20().then(
          response => {
            this.movies = response.data
          }
      );
    },
    getRates(){
      UserService.me().then(response=>{
        this.rates = response.data.rates
      });
    },
    getRate(movie){
      if(this.rates){
        const res = this.rates.find(rate=>rate.movie===movie)
        return res!=null?res.rate:0
      }
    }
  },
  created(){
    this.getMovies()
    this.getRates()

  }
}
</script>

<style scoped>

</style>