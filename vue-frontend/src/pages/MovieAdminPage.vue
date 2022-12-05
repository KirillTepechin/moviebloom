<template>
  <MovieForm @create="createMovie" @update="updateMovie" :movieUpdate="movieUpdate"/>
  <MovieList @delete="deleteMovie" @set="setMovie" :movies="movies" :movieUpdate="movieUpdate"/>
</template>

<script>
import MovieService from "@/services/MovieService";
import MovieForm from "@/components/MovieForm";
import MovieList from "@/components/MovieList";

export default {
  name: "MovieAdminPage",
  components: {MovieList, MovieForm},
  data() {
    let movieUpdate;
    return {
      movies: [],
      movieUpdate: movieUpdate
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
    createMovie(movie){
      this.movies.push(movie)
    },
    deleteMovie(id){
      this.movies.splice(this.getIndex(this.movies, id), 1)
    },
    setMovie(movie){
      this.movieUpdate = movie
    },
    updateMovie(movie){
      let index = this.getIndex(this.movies, movie.id);
      this.movies.splice(index, 1, movie);
    },
    getIndex(list, id) {
      for (let i = 0; i < list.length; i++ ) {
        if (list[i].id === id) {
          return i;
        }
      }
      return -1;
    }

  },
  created(){
    this.getMovies()
  }
}
</script>

<style>

</style>