<template>
  <div v-if="activeExam">
    <h3> Exam id: {{activeExam.id}}</h3>
    <h3> Subject name: {{activeExam.subjectTitle}}</h3>
    <questions :questions="activeExam.questions"></questions>
    <b-button v-if="!showAddQuestion" style="margin-right: 10px; margin-bottom: 3rem;" @click="displayAddQuestion">Add question</b-button>
    <add-question v-if="showAddQuestion" @added="hideAddQuestion"></add-question>
  </div>
</template>
<script>
import AddQuestion from '@components/question/AddQuestion'
import Questions from '@components/question/Questions'

export default {
  components: {
    AddQuestion,
    Questions
  },
  data () {
    return {
      fields: [
        'index',
        {key: 'domain', label: 'Title'},
        'actions'
      ],
      examId: null,
      showAddQuestion: false
    }
  },
  computed: {
    exams () {
      return this.$store.getters['exams']()
    },
    activeExam () {
      console.log(this.exams)
      return this.exams.find(item => item.id === this.examId)
    }
  },
  methods: {
    displayAddQuestion () {
      this.showAddQuestion = true
    },
    hideAddQuestion () {
      this.showAddQuestion = false
    }
  },
  created() {
    if (!this.$route.query) return this.$router.push({path:'/'})
    this.examId = Number(this.$route.query['exam-id'])
    console.log(this.examId)
    if (!this.exams || !this.exams.length) {
      this.$store.dispatch('LOAD_EXAMS', this.axios)
    }
  }
};
</script>
<style>
</style>