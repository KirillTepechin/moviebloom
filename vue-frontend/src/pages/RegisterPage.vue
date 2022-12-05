<template>
  <form @submit.prevent="register" enctype="multipart/form-data">
    <h4>Регистрация</h4>
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
    <Multiselect
        placeholder="Роль"
        mode="single"
        v-model="user.role"
        :options="['USER', 'ADMIN']"
    />
    <button
        type="submit"
    >
      Зарегистрироваться
    </button>
  </form>
</template>

<script>
import UserService from "@/services/UserService";
import Multiselect from "@vueform/multiselect";

export default {
  name: "RegisterPage",
  components: {
    Multiselect,
  },
  data(){
    return {
      user:{login:'', password: '', role: 'USER'},
    }
  },
  methods: {
    register() {
      UserService.register(this.user).then((response)=> {
        if(response.status!==400) window.location.href = "/auth"
      });
    },
  }
}
</script>

<style scoped>

</style>