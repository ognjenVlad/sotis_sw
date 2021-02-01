<template>
  <div>
    <b-form @submit.prevent="addExam" style="margin-top: 2rem; display: flex; flex-direction: column !important;" class="form" inline>
      <b-form-input
        id="inline-form-input-name"
        class="mb-2 mr-sm-2 mb-sm-0"
        v-model="title"
        required
        placeholder="Exam title"
      ></b-form-input>
      <div class="alert alert-danger message" role="alert" v-if="showErrors">
        You have to add exam title!
      </div>
      <b-form-select style="margin-top: 1rem" v-model="subject">
        <option v-for="(domain, index) in subjects" :key="index">{{ domain.title }}</option>
      </b-form-select>
    </b-form>
  
    <questions :questions="questions"></questions>
    <b-button v-if="!showAddQuestion" style="margin-right: 10px; margin-bottom: 3rem;" @click="displayAddQuestion">Add question</b-button>
    <add-question v-if="showAddQuestion" @added="hideAddQuestion"></add-question>
    <div style="width: 100%">
      <b-button type="submit" variant="success" style="margin-right: 10px; margin-bottom: 3rem; width: 50%;" @click="createExam">Create exam</b-button>
    </div>
    <div class="alert alert-danger message" role="alert" v-if="showQuestionsErrors">
      You have to add questions!
    </div>
    <div class="alert alert-danger message" role="alert" v-if="showApiErrors">
      Something went wrong!
    </div>
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
      title: '',
      subject: '',
      showQuestionsErrors: false,
      showErrors: false,
      examId: null,
      showAddQuestion: false,
      showApiErrors: false
    }
  },
  computed: {
    questions () {
      return this.$store.getters['addedQuestions']()
    },
    subjects () {
      console.log(this.$store.getters['subjects']())
      return this.$store.getters['subjects']()
    }
  },
  methods: {
    displayAddQuestion () {
      this.showAddQuestion = true
    },
    hideAddQuestion () {
      this.showAddQuestion = false
    },
    async createExam () {
      if (!this.title) {
        this.showErrors = true
        this.showQuestionsErrors = false
        return
      } else if (!this.questions || this.questions.length <=0) {
        this.showErrors = false
        this.showQuestionsErrors = true
        return
      }

      try {
        await this.$store.dispatch('CREATE_EXAM', {axios: this.axios, title: this.title, subjectTitle: this.subject})
        this.$router.push({path: '/exams'})
      } catch (e) {
        console.log(e)
        this.showApiErrors = true
        return
      }
    }
  },
  created () {
    this.$store.dispatch('LOAD_SUBJECTS', this.axios)
  }
};
</script>
<style>
</style>