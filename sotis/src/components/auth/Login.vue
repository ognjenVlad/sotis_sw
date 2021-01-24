<template>
  <div>
    <h3 style="margin: 2rem">Login</h3>
    <b-form @submit.prevent="login" class="login" inline>
      <label class="sr-only" for="inline-form-input-name">Username</label>
      <b-form-input
        id="inline-form-input-name"
        class="mb-2 mr-sm-2 mb-sm-0"
        v-model="username"
        placeholder="Username"
      ></b-form-input>

      <label class="sr-only" for="inline-form-input-name">Password</label>
      <b-form-input
        id="inline-form-input-name"
        class="mb-2 mr-sm-2 mb-sm-0"
        v-model="password"
        placeholder="Username"
        type="password"
      ></b-form-input>

      <b-button type="submit" variant="primary">Login</b-button>
    </b-form>
    <div class="alert alert-danger message" role="alert" v-if="showErrors">
      Something went wrong!
    </div>
  </div>
</template>

<script>

export default {
  data () {
    return {
      username: '',
      password: '',
      submitted: false,
      showErrors: false
    }
  },
  methods: {
    async login () {
      try {
        const loggedIn = await this.$store.dispatch('LOGIN', {axios: this.axios, username: this.username, password: this.password})
        if (loggedIn) this.$router.push({path: '/'})
      } catch (e) {
        this.showErrors = true
      }
    }
  },
};
</script>


<style scoped>
.login {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-button {
  width: 100%;
  margin-top: 40px;
}
.login-form {
  width: 290px;
}
.register {
  margin-top: 10px;
}
</style>
