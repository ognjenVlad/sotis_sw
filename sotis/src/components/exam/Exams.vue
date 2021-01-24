<template>
  <div>
    <b-button>Create new exam</b-button>
    <b-table small :fields="fields" :items="exams" responsive="sm">
      <template #cell(index)="data">
        {{ data.index + 1 }}
      </template>

      <template #cell(domain)="data">
        {{data.item.title}}
      </template>

      <template #cell(subject)="data">
        {{ data.item.subject_title }}
      </template>

      <template #cell(actions)>
        <b-button style="margin-right: 10px">Show</b-button>
        <b-button variant="outline-primary">Add question</b-button>
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
        {key: 'domain', label: 'Domain title'},
        {key: 'subject', label: 'Subject title'},
        'actions'
      ],
    }
  },
  computed: {
    exams () {
      return [{title: 'Naziv domena', subject_title: 'Naziv predmeta', actions: ''}]
      // return this.$store.getters['domains']()
    }
  },
  created() {
    // this.$store.dispatch('LOAD_EXAMS', this.axios)
  }
};
</script>
<style>
</style>