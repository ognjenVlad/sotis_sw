
<template>
  <div style="margin-top: 2rem;">
    <h4> Exam questions </h4>
    <b-table small :fields="fields" :items="questions" striped responsive="sm">
      <template #cell(#)="data">
        {{ data.index + 1 }}
      </template>

      <template #cell(text)="data">
        {{data.item.text}}
      </template>

      <template #cell(choices)="data">
        <b-list-group>
          <b-list-group-item v-for="(answer,index) in data.item.choices" :key="index" variant="dark">
            <p> <span class="bold-text">Text: </span>{{ answer.text }} </p>
            <p> <span class="bold-text">Is correct: </span>{{ answer.correct }} </p>
          </b-list-group-item>
        </b-list-group>
      </template>

      <template #cell(actions)="data">
        <b-button variant="danger" style="margin-right: 10px" @click="deleteQuestion(data.item)">Delete</b-button>
      </template>
    </b-table>
  </div>
</template>

<script>

export default {
  props: {
    questions: Array
  },
  data () {
    return {
      fields: [
        '#',
        {key: 'text', label: 'Question'},
        {key: 'choices', label: 'Answers'},
        'actions'
      ]
    }
  },
  methods: {
    deleteQuestion (question) {
      this.$store.dispatch('DELETE_QUESTION', {question})
    }
  }
};
</script>


<style lang="scss">
  .answers-list {
    width: 30%;
    margin: auto;
    p {
      margin-bottom: 0px !important;
    }
  }

  .bold-text {
    font-weight: bold;
  }
</style>
