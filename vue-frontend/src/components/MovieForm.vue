<template>
  <form @submit.prevent="saveMovie" enctype="multipart/form-data">
    <h4>Добавление фильма</h4>
    <input
        v-model="movie.name"
        type="text"
        placeholder="Название"
    />
    <textarea v-model="movie.description"
              type="text"
              placeholder="Описание">
    </textarea>
    <input
        v-model="movie.year"
        type="number"
        placeholder="Год"
    />
    <Multiselect
        placeholder="Выберите жанры"
        :searchable = true
        :hide-selected = false
        mode="tags"
        v-model:value="movie.genres"
        v-model="movie.genres"
        :options="genresData"
    />
    <Multiselect
        placeholder="Выберите актеров"
        :searchable="true"
        :hide-selected = false
        mode="tags"
        v-model="movie.actors"
        v-model:value="movie.actors"
        value-prop="id"
        label="fl"
        :options="actorsData"
    />
    <input
        @change="onFileChange"
        type="file"
        ref="myFileInput"
    />

    <button
        type="submit"
    >
      Добавить
    </button>
  </form>
  <transition-group name="p-message" tag="div">
    <Message v-for="msg of messages" :severity="msg.severity" :key="msg.id">{{msg.content}}</Message>
  </transition-group>
</template>

<script>
import movieService from "@/services/MovieService";
import Multiselect from '@vueform/multiselect'
import ActorService from "@/services/ActorService";


export default {
  name: "MovieForm",
  components: {
    Multiselect,
  },
  props: {
    movieUpdate: {
      id: '',
      name: '',
      genres: [],
      actors:[],
      year: 0,
      poster: null,
      description:''
    }

  },

  data() {
    return {

      movie: {...this.movieUpdate},
      actorsData:[] ,
      genresData: [
        "COMEDY",
        "THRILLER",
        "HORROR",
        "DRAMA",
        "ROMANCE",
        "SCI_FI",
        "CRIME",
        "FANTASY",
        "MUSIC",
        "ACTION",
        "ADVENTURE"]
    }
  },
  watch: {
    movieUpdate() {
      //this.$props.actorUpdate.photo = null
      this.movie.name = this.$props.movieUpdate.name
      this.movie.genres = this.$props.movieUpdate.genres
      this.movie.actors = this.$props.movieUpdate.actors.map((actor)=>actor.id)
      this.movie.year = this.$props.movieUpdate.year
      this.movie.description = this.$props.movieUpdate.description

      this.movie.id = this.$props.movieUpdate.id
    }
  },
  methods: {

    getActors() {
      ActorService.getActors().then(
          response => {
            const  res = response.data
            res.forEach(val=>val["fl"]=val.firstName+' '+val.lastName)
            this.actorsData = res
          }
      );
    },
    onFileChange(e) {
      var files = e.target.files || e.dataTransfer.files;
      if (!files.length)
        return;
      this.createImage(files[0]);
    },
    createImage(file) {
      var reader = new FileReader();
      var vm = this;

      reader.onload = (e) => {
        vm.image = e.target.result;
      };
      reader.readAsDataURL(file);
      this.movie.poster = file;
    },

    saveMovie() {
      console.log(this.movie)
      if (this.movie.id != null) {
        const mov={
          name: this.movie.name,
          actorsIds: this.movie.actors,
          genres: this.movie.genres,
          year: this.movie.year,
          description: this.movie.description,
        }
        movieService.updateMovie(this.movie.id,{
          movie: mov,
        }).then((response) => {
          if(this.movie.poster!=null && this.movie.poster!==this.movieUpdate.poster){
            movieService.uploadFile(response.data.id,{poster: this.movie.poster}).then((response)=>this.$emit("update",response.data))
          }
          else{
            this.$emit("update", response.data)
          }

          this.$refs.myFileInput.value = ""
          this.movie.name = ""
          this.movie.genres = []
          this.movie.actors = []
          this.movie.year = 0
          this.movie.poster = null
          this.movie.description = ""
        });
      } else {
        const mov={
          name: this.movie.name,
          actorsIds: this.movie.actors,
          genres: this.movie.genres,
          year: this.movie.year,
          description: this.movie.description,
        }
        movieService.createMovie({
          movie: mov,
          file: this.movie.poster
        }).then((response) => {
          if(this.movie.poster!=null){
           movieService.uploadFile(response.data.id,{poster: this.movie.poster}).then((response)=>this.$emit("create",response.data))
          }
          else{
            this.$emit("create", response.data)
          }

          this.$refs.myFileInput.value = ""
          this.movie.name = ""
          this.movie.genres = []
          this.movie.actors = []
          this.movie.year = 0
          this.movie.poster = null
          this.movie.description = ""
        });
      }
    }
  },
  created() {
    this.getActors()
  }
}
</script>
<style src="@vueform/multiselect/themes/default.css"></style>

<style>
form {
  display: flex;
  flex-direction: column;
}
</style>