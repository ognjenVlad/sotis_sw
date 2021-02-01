<template>
  <div style="margin-top: 2rem;">
      <h4> All subject sudents </h4>
      <b-table small :fields="fields" :items="students" striped responsive="sm">
        <template #cell(#)="data">
          {{ data.index + 1 }}
        </template>

        <template #cell(firstName)="data">
          {{data.item.firstName}}
        </template>

        <template #cell(lastName)="data">
          {{data.item.lastName}}
        </template>

        <template #cell(index)="data">
          {{data.item.index}}
        </template>

        <template #cell(actions)="data">
          <b-button variant="danger" @click="deleteStudentFromSubject(data.item)">Remove student from subject</b-button>
        </template>
      </b-table>
    </div>
</template>

<script>
export default {
  props: {
    students: Array
  },
  data () {
    return {
      fields: [
        '#',
        {key: 'firstName', label: 'First name'},
        {key: 'lastName', label: 'Last name'},
        {key: 'index', label: 'ID'},
        {key: 'actions', label: ''}
      ]
    }
  },
  methods: {
    deleteStudentFromSubject (student) {
      this.$emit('delete', student)
    }
  }
};
</script>


<style lang="scss">
</style>
