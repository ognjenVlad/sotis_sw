<template>
  <div>
    <b-button style="margin: 2rem"><router-link :to="{ path: 'add-subject' }">Create new subject</router-link></b-button>
    <b-table small :fields="fields" :items="subjects" responsive="sm">
      <template #cell(index)="data">
        {{ data.index + 1 }}
      </template>

      <template #cell(title)="data">
        {{data.item.title}}
      </template>

      <template #cell(actions)="data">
        <!-- <b-button variant="danger" style="margin-right: 10px" @click="deleteSubject(data.item)">Delete</b-button> -->
        <b-button variant="primary"><router-link :to="{ path: 'add-student', query: { 'subject-id': data.item.id  }}">Add Student</router-link></b-button>
        <b-button style="margin-right: 10px" variant="primary"><router-link :to="{ path: 'graphs', query: { 'subject-id': data.item.id  }}">Show Knowledge Space</router-link></b-button>
      </template>
    </b-table>
  </div>
</template>
<script>

export default {
  data () {
    return {
      fields: [
        'index',
        {key: 'title', label: 'Subject title'},
        'actions'
      ],
    }
  },
  computed: {
    subjects () {
      return this.$store.getters['subjects']()
    }
  },
  methods: {
    async deleteSubject (data) {
      try {
        await this.$store.dispatch('DELETE_SUBJECT', {axios:this.axios, id: data.id})
        this.$store.dispatch('LOAD_SUBJECTS', this.axios)
      } catch (e) {
        console.log(e)
      }
    }
  },
  created() {
    this.$store.dispatch('LOAD_SUBJECTS', this.axios)
  }
};
</script>
<style>
</style>