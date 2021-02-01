<template>
  <div v-if="exam" style="margin: 2rem">
    <h3> Exam: {{exam.id}}</h3>
    <question v-if="question" :question="question" :examId="examId"></question>
  </div>
</template>
<script>
import Question from '@components/question/Question'

export default {
  components: {
    Question
  },
  data () {
    return {
    }
  },
  computed: {
    question () {
      return this.$store.getters['currentQuestion']()
      // if (this.exams && this.exams.length) {
      //   return this.exams.find(item => item.id === this.examId).questions[1]
      // }
      // return null
    },
    exam () {
      return this.exams.find(item => item.id === this.examId)
    },
    exams () {
      return this.$store.getters['exams']()
    }
  },
  methods: {
  },
  async created() {
    if (!this.$route.query) return this.$router.push({path:'/'})
    this.examId = Number(this.$route.query['exam-id'])
    // await this.$store.dispatch('LOAD_STUDENT_DATA', this.axios)
    if (!this.exams || !this.exams.length) {
      await this.$store.dispatch('LOAD_EXAMS', this.axios)
      await this.$store.dispatch('LOAD_QUESTION', {axios: this.axios, examId: this.examId})
    } else {
      await this.$store.dispatch('LOAD_QUESTION', {axios: this.axios, examId: this.examId})
    }
  }
};
</script>
<style>
</style>