<template>

  <ActorForm @create="createActor" @update="updateActor" :actorUpdate="actorUpdate"/>
  <ActorList @delete="deleteActor" @set="setActor" :actors="actors" :actorUpdate="actorUpdate"/>

</template>

<script>
import ActorList from "@/components/ActorList";
import ActorForm from "@/components/ActorForm";
import ActorService from "@/services/ActorService";

export default {
  name: 'ActorAdminPage',
  components: {
    ActorForm,
    ActorList
  },
  data(){
    let actorUpdate;
    return {
      actors:[],
      actorUpdate: actorUpdate
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
    createActor(actor){
      this.actors.push(actor)
    },
    deleteActor(actor){
      this.actors.splice(this.getIndex(this.actors, actor.id), 1)
    },
    setActor(actor){
      // let index = this.getIndex(this.actors, actor.id);
      // this.actors.splice(index, 1, actor);
      this.actorUpdate = actor
    },
    updateActor(actor){
      let index = this.getIndex(this.actors, actor.id);
      this.actors.splice(index, 1, actor);
      console.log(actor)
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
    this.getActors()
  }
}
</script>

<style>

</style>
