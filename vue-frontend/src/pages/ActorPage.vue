<template>
  <ActorList :actors="actors"/>
</template>

<script>
import ActorList from "@/components/ActorList";
import ActorService from "@/services/ActorService";

export default {
  name: "ActorPage",
  components: {
    ActorList
  },
  data(){
    return {
      actors:[],
    }
  },

  methods: {
    getActors() {
      ActorService.getActors().then(
          response => {
            this.actors = response.data;
          }
      );
    },

  },
  created(){
    this.getActors()
  }
}
</script>

<style scoped>

</style>