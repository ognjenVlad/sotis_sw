<template>
  <div>
    <h3> Subject name: {{domain.title}}</h3>
    <b-form @submit.prevent="addStudentToSubject" class="form answer-form" inline>
      <v-select class="form-select" label="displayValue" @input="setSelected" :options="formatedStudents"></v-select>
      <b-button type="submit" variant="primary">Add student to subject</b-button>
    </b-form>
    <students :students="studentsForSubject" @delete="deleteStudentFromSubject"></students>
  </div>
</template>
<script>
import Students from '@components/students/Students'

export default {
  components: {
    Students
  },
  data () {
    return {
      fields: [
        'index',
        {key: 'domain', label: 'Title'},
        'actions'
      ],
      subjectId: null,
      student: '',
      subject: null
    }
  },
  computed: {
    formatedStudents () {
      return this.students.map((item) => {
        return item.displayValue = `${item.firstName} ${item.lastName} ${item.index}`
      })
    },
    studentsForSubject () {
      if (this.activeSubject && this.activeSubject.students) {
        return this.students.filter(item => {
          return this.activeSubject.students.find(student => student.index === item.index)
        })
      }
      return null
    },
    activeSubject () {
      if (this.subjects) {
        return this.subjects.find(item => item.id === Number(this.subjectId))
      }
      return null
    },
    students () {
      return this.$store.getters['students']()
    },
    subjects () {
      return this.$store.getters['subjects']()
    },
    domain () {
      return this.$store.getters['domains']()
    }
  },
  methods: {
    setSelected (value) {
      this.student = this.students.find(item => item.index === value.split(' ')[2])
    },
    addStudentToSubject () {
      const addedStudents = this.studentsForSubject ? [...this.studentsForSubject] : []
      addedStudents.push(this.student)
      this.$store.dispatch('ADD_STUDENT_TO_SUBJECT', {axios: this.axios, subject: this.activeSubject, students: addedStudents})
    },
    deleteStudentFromSubject (student) {
      let addedStudents = this.studentsForSubject ? [...this.studentsForSubject] : []
      addedStudents = addedStudents.filter(item => item.index !== student.index)
      this.$store.dispatch('ADD_STUDENT_TO_SUBJECT', {axios: this.axios, subject: this.activeSubject, students: addedStudents})
    }
  },
  created() {
    if (!this.$route.query) return this.$router.push({path:'/'})
    this.subjectId = this.$route.query['subject-id']
    this.$store.dispatch('LOAD_STUDENTS', this.axios)
    if (!this.subjects || !this.subjects.length) {
      this.$store.dispatch('LOAD_SUBJECTS', this.axios)
    }
  }
};
</script>
<style>

  .form-select {
    width: 200px;
    margin-right: 1rem;
  }
</style>