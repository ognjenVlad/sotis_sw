<template>
  <div style="display: flex; flex-direction: column;">
    <div>
      <v-select style="margin: auto" class="form-select" label="id" @input="setSelected" :options="exams"></v-select>
      <b-button style="margin: 1rem" @click="generateGraph">Generate graph</b-button>
    </div>
     <svg style="width: 0px; height: 0px;">
        <defs>
          <marker id="x-end" markerWidth="10" markerHeight="10" refX="9" refY="3" orient="auto" markerUnits="strokeWidth" >
              <path d="M0,0 L0,6 L9,3 z"></path>
          </marker>
        </defs>
    </svg>
    <d3-network 
      v-if="formatGraph"
      :ref='`net-states`' 
      :net-nodes="formatGraph.nodes"
      :net-links="formatGraph.links"
      :link-cb="lcb"
      :options="options"
    />
    <ul v-if="graph && graph.nodes">
      <li v-for="(item, index) in graph.nodes" :key="index">
        {{ item.id }} : {{ item.question_text}}
      </li>
    </ul>
  </div>
</template> 

<script>
import D3Network from 'vue-d3-network';


export default {
  components: {
    D3Network
  },
  data() {
    return {
      options: {
        size: { w: 1200, h: 800},
        nodeSize: 25,
        nodeLabels: true,
        canvas: false,
        linkWidth: 5,
        force: 8000
      },
      statuses: {
        REMOVED_EDGE: '#ff3300',
        NEW_EDGE: '#009900',
        NONE: '#0066cc',
        SAME_EDGE: '#33ccff'
      }
    }
  },
  computed: {
    exams () {
      return this.$store.getters['exams']()
    },
    format() {
      let nodes = []
      let links = []
      return { nodes, links }
    },
    graph() {
      return this.$store.getters['examGraph']()
    },
    formatGraph() {
      let nodes = [];
      let links = [];
      if (!this.graph || !this.graph.nodes || !this.graph.edges) return null
      this.graph.nodes.forEach(node => {
        nodes.push({id: node.id, name: node.id})
      })
    
      this.graph.edges.forEach(edge => {
        const color = this.statuses[edge.status]
        links.push({sid: edge.source, tid: edge.destination, _color: color, _labelClass: 'label'})
      })
      return {
          nodes,
          links
      }
    },
  },
  methods: {
    setSelected (value) {
      this.selectedExam = value.id
    },
    lcb (link) {
      link._svgAttrs = { 'marker-end': 'url(#x-end)'}
      return link
    },
    generateGraph () {
      this.$store.dispatch('GENERATE_EXAM_GRAPH', {axios: this.axios, exam_id: this.selectedExam})
    }
  },
  beforeDestroy () {
    this.$store.dispatch('RESET_GRAPH')
  },
  created(){
    if (!this.exams || !this.exams.length) {
      this.$store.dispatch('LOAD_EXAMS', this.axios)
    }
  }
}
</script>

<style>
#m-end path, #m-start{
  fill: #00ff00
}


#x-end path, #x-start{
  fill: #00ff00
}


.link {
  width: 200px;
}

.node-label {
  font-size: 18px !important;
}
</style>