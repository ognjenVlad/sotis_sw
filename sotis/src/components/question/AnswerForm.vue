<template>
  <div>
    <b-form @submit.prevent="addAnswer" class="form answer-form" inline>
      <label class="sr-only" for="inline-form-input-name">Answer</label>
      <b-form-input
        id="inline-form-input-name"
        class="mb-2 mr-sm-2 mb-sm-0"
        v-model="text"
        required
        placeholder="Answer"
      ></b-form-input>
      <b-form-checkbox
        class="form-item-margin-right"
        v-model="correct"
        value="true"
        unchecked-value="false"
      >
        Is correct answer
      </b-form-checkbox>
      <b-button type="submit" variant="primary">Add</b-button>
    </b-form>
  </div>
</template>

<script>
export default {
  data () {
    return {
      correct: '',
      text: ''
    }
  },
  computed: {
    answers () {
      console.log(this.$store.getters['addedAnswers']())
      return this.$store.getters['addedAnswers']()
    }
  },
  methods: {
    addAnswer () {
      const answer = {id: this.answers.length, correct: this.correct === 'true', text: this.text}
      this.$emit('added')
      this.$store.dispatch('ADD_ANSWER', {answer})
    }
  },
};
</script>

<style>
  .answer-form {
    width: 100%;
    margin: 2rem;
  }
  .form-item-margin-right {
    margin-right: 1rem;
  }
</style>
