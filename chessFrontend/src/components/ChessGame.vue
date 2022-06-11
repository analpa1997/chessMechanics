<template>
  <button v-on:click="obtenerPartida()">Obtener partida</button>
  <b-table id="tablero">
    <tbody>
      <tr v-for="indexFila in numFilas" v-bind:key="indexFila">
        <td v-bind:class="square" v-for="indexCol in numColumnas" v-bind:key="indexCol">
          <ChessSquare fila="indexFila"
                       columna="indexCol"
                       v-bind:is-blanca="casillas[((numFilas - indexFila)+1) + '_' + indexCol].blanca"
                       v-bind:is-activa="casillas[((numFilas - indexFila)+1) + '_' + indexCol].activa"
                       v-bind:notacion-algebraica="casillas[((numFilas - indexFila)+1) + '_' + indexCol].notacionAlgebraica">
          </ChessSquare>
        </td>
      </tr>
    </tbody>
  </b-table>
</template>

<script>
import axios from "axios"
import ChessSquare from './ChessSquare.vue'

export default {
  name: "ChessGame",
  data() {
    return {
      resultado: null,
      jaque: null,
      numColumnas: null,
      numFilas: null,
      turnoBlancas: null,
      casillas: null,
      casillasControladasBlancas: null,
      casillasControladasNegras: null,
      piezasBlancas: null,
      piezasNegras: null
    }
  },
  components: {
    ChessSquare
  },
  /*mounted() {
    this.obtenerPartida();
  },*/
  methods: {
    obtenerPartida: function () {
      axios.get('/api/createGame').then((respuesta) => {
        console.log(respuesta);
        this.resultado=respuesta.data.resultado;
        this.turnoBlancas=respuesta.data.turnoBlancas;
        this.jaque=respuesta.data.tablero.jaque;
        this.numColumnas=respuesta.data.tablero.numeroColumnas;
        this.numFilas=respuesta.data.tablero.numeroFilas;
        this.casillas=respuesta.data.tablero.casillas;
        this.casillasControladasBlancas=respuesta.data.tablero.casillasControladasBlancas;
        this.casillasControladasNegras=respuesta.data.tablero.casillasControladasNegras;
        this.piezasBlancas=respuesta.data.tablero.piezasBlancas;
        this.piezasNegras=respuesta.data.tablero.piezasNegras;
        console.log(this.casillas);
      })
    }
  }
}
</script>

<style scoped>

</style>