
<template>
  <div style="margin-top: 2rem;">
    <h5> <span v-if="!isExamFinished"> Question: </span> {{question.text}}</h5>
    <b-form @submit.prevent="submitAnswer" class="form answer-form" inline>
      <b-form-radio
        class="form-item-margin-right"
        v-for="(answer, index) in question.choices"
        :key="index"
        :value="index"
        @change="addAnswer(answer)"
        v-model="selected"
      >
        {{answer.text}}
      </b-form-radio>
      <b-button v-if="!isExamFinished" type="submit">Submit answer</b-button>
    </b-form>
    <b-button v-if="isExamFinished"><router-link to="/exams">Show Exams</router-link></b-button>
  </div>
</template>

<script>

export default {
  props: {
    question: Object,
    examId: Number
  },
  data () {
    return {
      selected: null,
      answer: null
    }
  },
  computed: {
    isExamFinished () {
      console.log(this.question)
      return this.question && this.question.id === -1 && this.question.text.toLowerCase() === 'exam finished'
    }
  },
  methods: {
    addAnswer (answer) {
      console.log(answer)
      this.answer = answer
    },
    async submitAnswer () {
      console.log(this.question)
      console.log(this.examId)
      await this.$store.dispatch('SUBMIT_ANSWER', {axios: this.axios, examId: this.examId, questionId: this.question.id, choiceId: this.answer.choiceId})
      this.selected = null
    },
    beforeDestroy () {
      this.$store.dispatch('RESET_CURRENT_QUESTION')
    }
  }
};
</script>


<style lang="scss">
</style>
