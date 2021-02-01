
<template>
  <div>
    <b-tabs content-class="mt-3" pills card>
      <b-tab title="Domain graph" active><add-graph></add-graph></b-tab>
      <b-tab title="IITA estimation"><graph></graph></b-tab>
    </b-tabs>
  </div>
</template> 

<script>
import AddGraph from '@components/graphs/AddGraph'
import Graph from '@components/graphs/Graph'


export default {
  data() {
    return {
      showDomainGraph: false
    }
  },
  components: {
    AddGraph,
    Graph
  }
}
</script>

<style style="scss" lang="postcss">

.nav-tabs .nav-link {
  color: black !important;
}
</style>