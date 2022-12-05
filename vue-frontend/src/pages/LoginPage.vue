<template>
  <form @submit.prevent="login" enctype="multipart/form-data">
    <h4>Вход</h4>
    <input
        v-model="user.login"
        type="text"
        placeholder="Введите логин"
    />
    <input
        v-model="user.password"
        type="password"
        placeholder="Введите пароль"
    />
    <button
        type="submit"
    >
      Войти
    </button>
  </form>
</template>

<script>


import UserService from "@/services/UserService";

export default {
  name: "LoginPage",
  data(){
    return {
      user:{},
    }
  },
  methods: {
    login() {
       UserService.login(this.user).then(
          response => {
            console.log("токен "+response.data)
            localStorage.setItem('jwt', response.data)
          }
      ).then(()=>window.location.href = "/");
    },
  }
}



</script>

<style scoped>

</style>