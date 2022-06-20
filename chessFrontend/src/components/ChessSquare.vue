<template>
  <img draggable="true"
       class="pieza img-fluid"
       v-bind:class="{'conMovimientos':this.piezaEnCasilla.blanca === turnoBlancas && this.piezaEnCasilla.casillasDisponibles.length > 0}"
       v-bind:id="this.piezaEnCasilla.nombreImagen + '-' +  notacionAlgebraica"
       v-if="piezaEnCasilla"
       v-bind:src="'/static/img/' + this.piezaEnCasilla.nombreImagen + '.png'"
  />
</template>

<script>
export default {
  name: "ChessSquare",
  props: ['turnoBlancas', 'piezaEnCasilla','notacionAlgebraica', 'fila', 'columna', 'isBlanca', 'isActiva'],
  methods: {

  },
  mounted() {

  }
}
</script>

<style scoped>
  .blanca{
    color: #000000;
  }
  .negra{
    color: #ffffff;
  }
  .pieza{
    width: 35rem;
  }
  .conMovimientos{
    cursor:grab;
  }
  .conMovimientos:active{
    cursor:grabbing;
  }
</style>