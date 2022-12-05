<template>
  <MovieList :movies="movies"/>

</template>

<script>
import MovieList from "@/components/MovieList";
import MovieService from "@/services/MovieService";

export default {
  name: "MoviesPage",
  components: {MovieList},
  data() {
    return {
      movies: [],
    }
  },
  methods: {
    getMovies() {
      MovieService.getMovies().then(
          response => {
            this.movies = response.data;
          }
      );
    },
  },
  created(){
    this.getMovies()
  }
}

</script>

<style scoped>

</style>