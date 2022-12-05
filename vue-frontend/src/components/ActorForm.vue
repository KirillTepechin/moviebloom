<template>
  <form @submit.prevent="saveActor" enctype="multipart/form-data">
    <h4>Добавление актера</h4>
    <input
        v-model="actor.firstName"
        type="text"
        placeholder="Имя"
    />
    <input
        v-model="actor.lastName"
        type="text"
        placeholder="Фамилия"
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
</template>

<script>
import actorService from "@/services/ActorService";

export default {
  props: {
    actorUpdate: {
      id: '',
      firstName: '',
      lastName: '',
      photo: null,
    }

  },
  data() {
    return {
      actor: {...this.actorUpdate}
    }
  },
  watch: {
    actorUpdate() {
      //this.$props.actorUpdate.photo = null
      this.actor.firstName = this.$props.actorUpdate.firstName
      this.actor.lastName = this.$props.actorUpdate.lastName
      this.actor.id = this.$props.actorUpdate.id
    }
  },
  methods: {

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
      this.actor.photo = file;
    },

    saveActor() {
      if (this.actor.id != null) {
        actorService.updateActor({
          id: this.actor.id,
          firstName: this.actor.firstName,
          lastName: this.actor.lastName,
          photo: this.actor.photo
        }).then((response) => {
          this.$emit("update", response.data)
          this.actor.firstName = ""
          this.actor.lastName = ""
          this.actor.photo = null
          this.$refs.myFileInput.value = ""
        });
      } else {
        actorService.createActor({
          firstName: this.actor.firstName,
          lastName: this.actor.lastName,
          photo: this.actor.photo

        }).then((response) => {
          this.$emit("create", response.data)
          this.actor.firstName = ""
          this.actor.lastName = ""
          this.actor.photo = null
          this.$refs.myFileInput.value = ""
        });
      }
    }
  },
}

</script>

<style scoped>

form {
  display: flex;
  flex-direction: column;
}
</style>