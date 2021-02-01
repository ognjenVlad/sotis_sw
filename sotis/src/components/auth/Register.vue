<template>
  <div>
    <h3>Add new user</h3>
    <b-form class="register-form" @submit.prevent="register">
      <b-form-input
        v-model="username"
        placeholder="Username"
        required
      ></b-form-input>
      <b-form-input
        v-model="password"
        placeholder="Password"
        type="password"
        required
      ></b-form-input>
      <b-form-input
        v-model="email"
        placeholder="Email"
        type="email"
        required
      ></b-form-input>
      <b-form-input
        v-model="firstName"
        placeholder="First name"
        required
      ></b-form-input>
      <b-form-input
        v-model="lastName"
        placeholder="Last name"
        required
      ></b-form-input>
      <b-form-input
        v-if="role === 'STUDENT_ROLE'"
        v-model="studentId"
        placeholder="ID"
        required
      ></b-form-input>
      <b-form-select v-model="role">
        <option value="STUDENT_ROLE">Student</option>
        <option value="PROFESSOR_ROLE">Professor</option>
      </b-form-select>
      <b-button style="margin-top:2rem" type="submit" variant="primary">Add User</b-button>
    </b-form>
    <div class="alert alert-success message" role="alert" v-if="result">
      Student created successfully!
    </div>
    <div class="alert alert-danger message" role="alert" v-if="showErrors">
      Something went wrong!
    </div>
  </div>
</template>

<script>

export default {
  data () {
      return {
          firstName: '',
          lastName: '',
          username: '',
          password: '',
          email: '',
          studentId: '',
          role: 'STUDENT_ROLE',
          showErrors: false,
          result: null
      }
  },
  methods: {
    async register () {
      try {
        this.result = await this.$store.dispatch('REGISTER', {
          axios: this.axios,
          username: this.username,
          password: this.password,
          lastName: this.lastName,
          firstName: this.firstName,
          studentId: this.studentId,
          role: this.role,
          email: this.email
        })
        console.log(this.result)
      } catch (e) {
        this.showErrors = true
        console.log(e)
      }
    }
  }
};
</script>


<style scoped lang="scss">
.register-form {
  display: flex;
  flex-direction: column;
  max-width: 500px;
  align-items: center;
  text-align: center;
  margin: auto;

  > input {
    margin-bottom: 1rem;
  }
}

</style>
