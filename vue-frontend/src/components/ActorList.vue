<template>
  <div class="container">
    <h1 class="text-center"> Список актеров</h1>
    <div class="input-group mb-3">
      <div class="input-group-prepend">
        <span style="color:whitesmoke; background-color: forestgreen" class="input-group-text" id="basic-addon3">Найти актера</span>
      </div>
      <input type="text" v-model="search" class="form-control" id="basic-url" aria-describedby="basic-addon3">
    </div>
    <table class="table" style="vertical-align: middle" >
      <thead>
      <th v-if="this.userRole==='ADMIN'">Id</th>
      <th>Имя актера</th>
      <th>Фамилия актера</th>
      <th>Фото</th>
      <th v-if="this.userRole==='ADMIN'"></th>
      </thead>
      <tbody>
      <tr v-for="actor in filter" :key="actor.id">
        <td v-if="this.userRole==='ADMIN'">{{actor.id}}</td>
        <td>{{actor.firstName}}</td>
        <td>{{actor.lastName}}</td>
        <td><img v-bind:src="'/img/' + actor.photo" style="object-fit: scale-down;max-height: 100px"></td>
        <td v-if="this.userRole==='ADMIN'"><input width=20% type="button" value="Удалить" @click="deleteActor(actor.id)" />
        <input type="button" value="Обновить" @click="updateActor(actor)" /></td>
      </tr>
      </tbody>
    </table>
  </div>
</template>
<script>
import ActorService from "@/services/ActorService";
import UserService from "@/services/UserService";

export default{
  data(){
    return {userRole:"", search: '',}
  },
  props:{
    actors:[]
  },
  methods:{
    deleteActor(id){
      ActorService.deleteActor(id).then((response)=>{
        this.$emit("delete", response.data)
      });
    },
    updateActor(actor){
      ActorService.getActorData(actor.id).then((response)=>{
        this.$emit("set", response.data)
      })
    },
    me(){
      UserService.me().then(response => {
        this.userRole = response.data.role
      })
    },
    onlyUnique(value, index, self) {
      return self.indexOf(value) === index;
    }
  },
  computed: {
    filter() {
      return  this.actors.filter(item => (item.firstName).indexOf(this.search) !== -1||(item.lastName).indexOf(this.search) !== -1)
    },
  },
  created() {
    this.me()
  }
}
</script>
<style>
</style>