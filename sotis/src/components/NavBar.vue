<template>
  <div>
    <b-navbar toggleable="lg" type="dark" variant="dark">
      <b-navbar-brand><router-link to="/">Testing Platform</router-link></b-navbar-brand>

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item v-if="!user"><router-link to="/login">Login</router-link></b-nav-item>
          <b-nav-item v-if="isAdmin"><router-link  to="/register">Add User</router-link></b-nav-item>
          <b-nav-item><router-link to="/domains">Subjects</router-link></b-nav-item>
          <!-- <b-nav-item v-if="isProfessor || isAdmin"><router-link to="/add-subject">Add subject</router-link></b-nav-item> -->
          <b-nav-item><router-link to="/exams">Exams</router-link></b-nav-item>
          <!-- <b-nav-item><router-link to="/graph">Show Graph</router-link></b-nav-item> -->
          <!-- <b-nav-item><router-link to="/add-graph">Add Graph</router-link></b-nav-item> -->
        </b-navbar-nav>

        <b-navbar-nav class="ml-auto">
          <b-nav-item v-if="user"><span style="cursor: pointer" @click="logout">Logout</span></b-nav-item>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
  </div>
</template>
<script>

import {ROLES} from '@constants/roles'
export default {
  computed: {
    user () {
      return this.$store.getters['getUser']()
    },
    isProfessor () {
      return this.user && this.user.roles === ROLES.PROFESSOR
    },
    isAdmin () {
      return this.user && this.user.roles === ROLES.ADMIN
    }
  },
  methods: {
    logout() {
      this.$store.dispatch('LOGOUT')
      this.$router.push({path: '/login'})
    }
  },
};
</script>
<style style="scss">

</style>