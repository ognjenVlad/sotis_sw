<template>
  <div>
    <b-button style="margin: 2rem"><router-link :to="{ path: 'new-exam' }">Create new exam</router-link></b-button>
    <b-table small :fields="fields" :items="exams" responsive="sm">
      <template #cell(ID)="data">
        {{ data.item.id }}
      </template>

      <template #cell(subjectTitle)="data">
        {{data.item.subjectTitle}}
      </template>

      <template #cell(actions)="data">
        <b-button style="margin-right: 10px"><router-link :to="{ path: 'exam', query: { 'exam-id': data.item.id  }}">Edit Exam</router-link></b-button>
        <b-button style="margin-right: 10px" v-if="false" variant="danger" @click="deleteExam(data.item.id)">Delete Exam</b-button>
        <b-button style="margin-right: 10px" variant="info" @click="takeExam(data.item.id)">Take Exam</b-button>
      </template>
    </b-table>
  </div>
</template>
<script>

export default {
  data () {
    return {
      fields: [
        'ID',
        {key: 'subjectTitle', label: 'Subject'},
        'actions'
      ],
    }
  },
  computed: {
    exams () {
      return this.$store.getters['exams']()
    }
  },
  methods: {
    async deleteExam (id) {
      try {
        await this.$store.dispatch('DELETE_EXAM', {axios:this.axios, id: id})
      } catch (e) {
        console.log(e)
      }
    },
    takeExam(id) {
      this.$router.push({path:'/test', query: { 'exam-id': id}})
    }
  },
  created() {
    this.$store.dispatch('LOAD_EXAMS', this.axios)
  }
};
</script>
<style>
</style>