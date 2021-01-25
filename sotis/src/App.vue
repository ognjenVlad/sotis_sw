<template>
  <div id="app">
    <!-- <img style="width: 50px" src="@assets/logo.png" alt=""> -->
    <nav-bar></nav-bar>
    <router-view></router-view>
  </div>
</template>

<script>

import NavBar from '@components/NavBar'
export default {
  name: 'App',
  components: {
    NavBar
  },
  created () {
    const userData = localStorage.getItem('authtoken')
    if (userData) {
      const user = window.atob(userData.split('.')[1])
      this.$store.dispatch('SET_USER', JSON.parse(user))
    }
  }
}
</script>

<style style="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

a {
  color: white !important;
}
</style>
