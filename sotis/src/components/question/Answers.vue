<template>
  <div class="answers-list">
    <b-list-group>
      <div v-for="(answer,index) in answers" :key="index">
        <b-list-group-item variant="primary">
          <p><span class="bold-text">Text</span> {{ answer.text }}</p>
          <p><span class="bold-text">Is correct:</span> {{ answer.correct }} </p>
          <b-button @click="deleteAnswer(answer)" variant="danger">Delete</b-button>
        </b-list-group-item>
      </div>
    </b-list-group>
  </div>
</template>

<script>
export default {
  computed: {
    answers () {
      console.log(this.$store.getters['addedAnswers']())
      return this.$store.getters['addedAnswers']()
    }
  },
  methods: {
    deleteAnswer (answer) {
      this.$store.dispatch('DELETE_ANSWER', {answer})
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
