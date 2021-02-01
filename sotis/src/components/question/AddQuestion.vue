<template>
  <div>
    <b-form @submit.prevent="addQuestion" class="form" inline>
      <label class="sr-only" for="inline-form-input-name">Question</label>
      <b-form-input
        id="inline-form-input-name"
        class="mb-2 mr-sm-2 mb-sm-0"
        v-model="text"
        required
        placeholder="Question"
      ></b-form-input>
      <b-button class="button-with-margins" variant="secondary" @click="addAnswer">Add new answer</b-button>
      <div style="width: 100%;">
        <answers></answers>
        <answer-form v-if="showAnswerForm" @added="hideAnswerForm"></answer-form>
      </div>
      <div style="width: 100%;">
        <b-button class="button-with-margins" type="submit" variant="primary">Add Question</b-button>
      </div>
    </b-form>
    <div class="alert alert-danger message" role="alert" v-if="showErrors">
      You have to add answers first
    </div>
    
  </div>
</template>

<script>
import AnswerForm from '@components/question/AnswerForm'
import Answers from '@components/question/Answers'
export default {
  components: {
    AnswerForm,
    Answers
  },
  data () {
    return {
      text: '',
      result: null,
      showErrors: false,
      showAnswerForm: false
    }
  },
  computed: {
    answers () {
      return this.$store.getters['addedAnswers']()
    },
    questions () {
      return this.$store.getters['addedQuestions']()
    }
  },
  methods: {
    addQuestion () {
      if (!this.answers || this.answers.length <= 0) {
        this.showErrors = true
        return
      }
      this.$emit('added')
      this.$store.dispatch('ADD_QUESTION', {id: this.questions.length, text: this.text})
    },
    addAnswer () {
      this.showAnswerForm = true
    },
    hideAnswerForm () {
      this.showAnswerForm = false
    }
  },
};
</script>


<style>

.button-with-margins {
  margin: 1rem;
}
</style>
