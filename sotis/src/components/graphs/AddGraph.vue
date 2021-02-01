<template>
    <div>
      <div>
        <b-button variant="success" style="margin-right: 10px; margin-bottom: 3rem; width: 50%;" @click="createGraph">Update graph</b-button>
        <div class="alert alert-danger message" role="alert" v-if="showErrors">
          {{ errorMessage }}
        </div>
      </div>
      <div style="display: flex">
        <svg style="width: 0px; height: 0px;">
            <defs>
                <marker id="m-end" markerWidth="10" markerHeight="10" refX="9" refY="3" orient="auto" markerUnits="strokeWidth" >
                    <path d="M0,0 L0,6 L9,3 z"></path>
                </marker>
            </defs>
        </svg>
        <d3-network :ref='`net-states`' 
          v-if="formatGraph"
          :net-nodes="formatGraph.nodes"
          :net-links="formatGraph.links"
          :selection="selection"
          :link-cb="lcb"
          :options="options"
          @node-click="onNodeSelect"
          @link-click="onLinkClick"/>
        <div>
          <ul v-if="graphNodes" style="margin-right: 1rem;">
            <li v-for="(item, index) in graphNodes" :key="index">
              {{ item.id }} : {{ item.question_text}}
            </li>
          </ul>
          <b-button v-if="selectedLink" variant="danger" style="margin-right: 10px; max-height: 80px;" @click="deleteLink">Delete selected link</b-button>
        </div>
      </div>
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
        force: 8000,
      },
      source: null,
      destination: null,
      showErrors: false,
      errorMessage: '',
      selectedLink: null,
      subjectId: null,
      selection: {
        nodes: {},
        links: {}
      }
    }
  },
  computed: {
    format() {
      let nodes = []
      let links = []
      return { nodes, links }
    },
    graphNodes() {
      return this.$store.getters['graphNodes']()
    },
    addedLinks () {
      return this.$store.getters['addedLinks']()
    },
    formatGraph() {
      let nodes = [];
      let links = this.addedLinks ? [...this.addedLinks] : []
      let allLinks = []
      if (!this.graphNodes) return 
      
      this.graphNodes.forEach(node => {
        nodes.push({id: node.id, name: node.id})
      })

      links.forEach(edge => {
        allLinks.push({sid: edge.source, tid: edge.destination, _color: '#0066cc', _labelClass: 'label'})
      })
    
      return {
          nodes,
          links: allLinks
      }
    },
    graphToSubmit () {
      let nodes = [];
      let links = this.addedLinks ? [...this.addedLinks] : []
      let allLinks = []
      if (!this.graphNodes) return 
      
      this.graphNodes.forEach(node => {
        nodes.push({id: node.id, name: node.id})
      })

      links.forEach(edge => {
        allLinks.push({sid: edge.source, tid: edge.destination, _color: '#0066cc', _labelClass: 'label'})
      })
    
      return {
          nodes,
          links: allLinks
      }
    }
  },
  methods: {
    lcb (link) {
      link._svgAttrs = { 'marker-end': 'url(#m-end)' }
      return link
    },
    onNodeSelect(event, node) {
      this.selection.nodes[node.id] = node
      this.selection.links = {}
      if (!this.source) {
        this.source = node.id
        return
      } else if (this.source === node.id) {
        return 
      }
      this.destination = node.id;
      this.$store.dispatch('ADD_LINK', {axios: this.axios, source: this.source, destination: this.destination})
      this.destination = null
      this.source = null
      this.selection.nodes = {}
    },
    async createGraph () {
      try {
        const graphToAdd = this.formatGraph
        const rootNodeIndex = graphToAdd.nodes.findIndex(item => {
          const foundLink = graphToAdd.links.find(link => link.tid === item.id)
          return foundLink ? false : true
        })
        var element = graphToAdd.nodes[rootNodeIndex]
        graphToAdd.nodes.splice(rootNodeIndex, 1)
        graphToAdd.nodes.splice(0, 0, element)
        await this.$store.dispatch('ADD_GRAPH', {axios: this.axios, graph: graphToAdd, subject_id: this.subjectId})
      } catch (e) {
        this.showErrors = true
        this.errorMessage = e.response.data.subject_title
      }
      this.$store.dispatch('LOAD_SUBJECT_GRAPH_DATA', {axios: this.axios, subjectId: this.subjectId})
    },
    onLinkClick (event, link) {
      this.selectedLink = link
      const id = link.id
      this.selection.links = {}
      this.selection.links[id] = link
    },
    deleteLink () {
      if (!this.selectedLink) {
        this.selection.links = {}
        return
      }
      this.$store.dispatch('DELETE_LINK', {source: this.selectedLink.sid, destination: this.selectedLink.tid })
      this.selection.links = {}
      this.selectedLink = null
    }
  },
  created(){
    if (!this.$route.query) return this.$router.push({path:'/'})
    this.subjectId = Number(this.$route.query['subject-id'])
    this.$store.dispatch('LOAD_SUBJECT_GRAPH_DATA', {axios: this.axios, subjectId: this.subjectId})
  },
  beforeDestroy () {
    this.$store.dispatch('RESET_ADDED_GRAPH_DATA')
  }
}
</script>

<style>
#m-end path, #m-start{
  fill: rgba(18, 120, 98, 0.8);
}

.selected {
  color: #ff0000;
  fill: #ff0000;
  stroke: #ff9933 !important;
}

.link .selected {
  color: yellow;
  fill: yellow;
  stroke: yellow;
}
</style>