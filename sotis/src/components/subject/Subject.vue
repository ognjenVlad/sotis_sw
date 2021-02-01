<template>
  <div>
    <h3 style="margin: 2rem">Create new subject</h3>
    <b-form @submit.prevent="createSubject" class="form" inline>
      <label class="sr-only" for="inline-form-input-name">Subject title</label>
      <b-form-input
        id="inline-form-input-name"
        class="mb-2 mr-sm-2 mb-sm-0"
        v-model="title"
        placeholder="Title"
      ></b-form-input>
      <b-button type="submit" variant="primary">Create</b-button>
    </b-form>
    <div class="alert alert-success message" role="alert" v-if="result">
      Subject created successfully!
    </div>
    <div class="alert alert-danger message" role="alert" v-if="showErrors">
      {{error}}
    </div>
  </div>
</template>

<script>

export default {
  data () {
    return {
      title: '',
      result: null,
      showErrors: false,
      error: 'Something went wrong!'
    }
  },
  methods: {
    async createSubject () {
      try {
        this.result = await this.$store.dispatch('ADD_SUBJECT', {axios: this.axios, title: this.title})
        this.title = ''
        this.showErrors = false
      } catch (e) {
        this.showErrors = true
        this.error = e.response.data
      }
    }
  },
};
</script>


<style>
.form {
  flex: 1;
  display: flex;
  justify-content: center;
  align-items: center;
}

.message {
  margin-top: 3rem !important;
  max-width: 40%;
  align-items: center;
  text-align: center;
  margin: auto;
}
</style>
