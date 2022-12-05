<template>
  <div>
    <div class="card text-forestgreen bg-black">
      <div class="card-horizontal">
        <div class="img-square-wrapper">
          <img v-bind:src="'/img/' + movie.poster">
        </div>
        <div class="card-body">
          <h1 class="card-title">{{ movie.name + ' (' + movie.year + ')' }}</h1>
          <h5 class="card-text">{{ movie.description }}</h5>
          <table class="table" style="color: whitesmoke">
            <thead>
            <th>Жанры</th>
            <th>Актеры</th>
            </thead>
            <tbody>
            <tr>
              <th>{{ this.movie.genres.join(', ') }}</th>
              <td>{{ this.movie.actors.map((actor) => actor.firstName + ' ' + actor.lastName).join(', ') }}</td>
            </tr>
            </tbody>
          </table>
          <div style="display:flex">
            <h3 style="padding-top: 50px; margin-top:50px">Ваша оценка:</h3>
            <star-rating style="width:30%; margin:10px;padding:50px" @click="rate(this.movie.id)"
                         @update:rating="setRating"
                         v-bind:increment="0.5"
                         v-bind:max-rating="10"
                         v-bind:rating="getRate(this.movie.id)"
                         v-bind:clearable="true"
                         v-bind:animate="true"
                         inactive-color="grey"
                         active-color="forestgreen"
                         v-bind:star-size="25">
            </star-rating>
            <h3 style="margin:50px;padding:50px">Оценка moviebloom: {{ movie.averageRate }}</h3>

          </div>

        </div>
      </div>
    </div>
  </div>
</template>

<script>
import MovieService from "@/services/MovieService";
import RateService from "@/services/RateService";
import StarRating from "vue-star-rating";
import UserService from "@/services/UserService";

export default {
  name: "MoviePage",
  components: {StarRating},
  data() {
    return {
      movie: {},
      user: {},
      currentRate: 0,
    }
  },
  methods: {
    setRating(rating) {
      this.currentRate = rating;
    },
    rate(movieId) {
      if (this.currentRate === 0) {
        RateService.unrate(movieId)
        console.log("unrate")
      } else {
        RateService.rate(movieId, this.currentRate)
        console.log("rate")
      }
    },
    getRate(movieId) {
      const res = this.user.rates.find(rate => rate.movie === movieId)
      return res != null ? res.rate : 0
    },
    getUser() {
      UserService.me().then(response => this.user = response.data)
    },
    getMovie() {
      MovieService.getMovieData(this.$route.params.id).then(response => {
        this.movie = response.data
      })
      console.log(this.movie)
    }
  },
  created() {
    this.getMovie()
    this.getUser()
  }
}
</script>

<style>
.card-horizontal {
  display: flex;
  flex: 1 1 auto;
  margin: 25px;
  padding: 25px
}

</style>