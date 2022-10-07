<template>
  <button class="btn btn-primary" v-on:click="obtenerPartida()">Obtener partida</button>
  <div class="col container-fluid">
    <div v-if="this.textoFinPartida" class="row">
      <h1> {{this.textoFinPartida}}</h1>
    </div>

    <div class="row">
      <table id="tablero" class="col-4">
        <tbody>
        <tr v-for="indexFila in numFilas" v-bind:key="indexFila">
          <td class="col" @drop="drop" @dragover="allowDrop"
              v-for="indexCol in numColumnas" v-bind:key="indexCol"
              v-bind:id="casillas[((numFilas - indexFila)+1) + '_' + indexCol].notacionAlgebraica"
              v-bind:class="{
            'square': !this.piezaEnCasilla(casillas[((numFilas - indexFila)+1) + '_' + indexCol]),
            'isActiva': casillas[((numFilas - indexFila)+1) + '_' + indexCol].activa,
            'blanca': casillas[((numFilas - indexFila)+1) + '_' + indexCol].blanca,
            'negra': !casillas[((numFilas - indexFila)+1) + '_' + indexCol].blanca }">
            <ChessSquare fila="indexFila"
                         columna="indexCol"
                         @dragstart="drag"
                         v-bind:turno-blancas="this.turnoBlancas"
                         v-bind:draggable="this.piezaConMovimiento(casillas[((numFilas - indexFila)+1) + '_' + indexCol]) "
                         v-bind:pieza-en-casilla="this.piezaEnCasilla(casillas[((numFilas - indexFila)+1) + '_' + indexCol])"
                         v-bind:is-blanca="casillas[((numFilas - indexFila)+1) + '_' + indexCol].blanca"
                         v-bind:is-activa="casillas[((numFilas - indexFila)+1) + '_' + indexCol].activa"
                         v-bind:notacion-algebraica="casillas[((numFilas - indexFila)+1) + '_' + indexCol].notacionAlgebraica">
            </ChessSquare>
          </td>
        </tr>
        </tbody>
      </table>
      <div class="col-4"></div>
      <div class="col-4">
        <div class="row">
          <h3>NOTACIÓN ALGEBRAICA</h3>
        </div>
        <div class="row">
          <table class="col-12">
            <thead>
            <th class="col-4">Blancas</th>
            <th class="col-4">#</th>
            <th class="col-4">Negras</th>
            </thead>
            <tbody>
            <tr v-for="movimiento in this.nMovimientos" v-bind:key="movimiento">
              <td class="col-4">{{ this.jugadasBlancas[movimiento-1] }}</td>
              <td class="col-4" v-if="this.jugadasBlancas[movimiento-1]">{{ movimiento }}</td>
              <td class="col-4">{{ this.jugadasNegras[movimiento-1] }}</td>
            </tr>
            </tbody>
          </table>
        </div>
      </div>
    </div>
  </div>

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
      piezas: null,
      nMovimientos: null,
      partidaFinalizada: null,
      tablero: null,
      jugadasBlancas: null,
      jugadasNegras: null,
      textoFinPartida: null
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
        this.tablero = respuesta.data.tablero;
        this.resultado=respuesta.data.resultado;
        this.turnoBlancas=respuesta.data.turnoBlancas;
        this.jaque=respuesta.data.tablero.jaque;
        this.numColumnas=respuesta.data.tablero.numeroColumnas;
        this.numFilas=respuesta.data.tablero.numeroFilas;
        this.casillas=respuesta.data.tablero.casillas;
        this.casillasControladasBlancas=respuesta.data.tablero.casillasControladasBlancas;
        this.casillasControladasNegras=respuesta.data.tablero.casillasControladasNegras;
        this.nMovimientos = respuesta.data.nMovimientos;
        this.partidaFinalizada = respuesta.data.partidaFinalizada;
        this.jugadasBlancas = respuesta.data.jugadasBlancas;
        this.jugadasNegras = respuesta.data.jugadasNegras
        this.piezas=respuesta.data.tablero.piezas;
        this.textoFinPartida = null;
      })
    },
    piezaEnCasilla(casilla){
      return this.piezas[casilla.notacionAlgebraica];
    },



    piezaConMovimiento(casilla){
      return (this.piezas[casilla.notacionAlgebraica] && this.piezas[casilla.notacionAlgebraica].blanca === this.turnoBlancas && this.piezas[casilla.notacionAlgebraica].casillasDisponibles.length > 0);
    },

    allowDrop(ev) {
      ev.preventDefault();
    },

    mostrarResultado(){
      if (this.resultado < 99){
        if (this.resultado === 1){
          this.textoFinPartida = "GANAN LAS BLANCAS";
        }else if (this.resultado === 0){
          this.textoFinPartida = "TABLAS";
        }else{
          this.textoFinPartida = "GANAN LAS NEGRAS";
        }
      }else{
        this.textoFinPartida = null;
      }
    },

    drag(ev) {
      console.log(ev);
      //document.getElementById(ev.target.id).style.opacity = 0;
      ev.dataTransfer.setData("text", ev.target.id);
      var casillaPieza = ev.target.id.split("-")[1];
      var pieza = this.piezas[casillaPieza];
      pieza.casillasDisponibles.forEach(function(value){
        document.getElementById(value).classList.add("movimientoDisponible");
      });
    },
    drop(ev) {
      ev.preventDefault();
      //document.getElementById(ev.dataTransfer.getData("text")).style.opacity = 1;
      var casillaOrigen = ev.dataTransfer.getData("text").split("-")[1];
      var casillaDestino = ev.target.id.includes("-") ? ev.target.id.split("-")[1] : ev.target.id;
      var pieza = this.piezas[casillaOrigen];
      pieza.casillasDisponibles.forEach(function(value){
        document.getElementById(value).classList.remove("movimientoDisponible");
      });
      if (casillaOrigen !== casillaDestino && pieza.casillasDisponibles.includes(casillaDestino)){
        axios.post('/api/moveGame', {
          partida: {
            resultado: this.resultado,
            tablero: this.tablero,
            nMovimientos: this.nMovimientos,
            partidaFinalizada: this.partidaFinalizada,
            turnoBlancas: this.turnoBlancas,
            jugadasBlancas: this.jugadasBlancas,
            jugadasNegras: this.jugadasNegras
          },
          movimientoOrigen: casillaOrigen,
          movimientoDestino: casillaDestino,
        }).then((respuesta) => {
          console.log(respuesta);
          this.tablero = respuesta.data.tablero;
          this.resultado=respuesta.data.resultado;
          this.turnoBlancas=respuesta.data.turnoBlancas;
          this.jaque=respuesta.data.tablero.jaque;
          this.numColumnas=respuesta.data.tablero.numeroColumnas;
          this.numFilas=respuesta.data.tablero.numeroFilas;
          this.casillas=respuesta.data.tablero.casillas;
          this.casillasControladasBlancas=respuesta.data.tablero.casillasControladasBlancas;
          this.casillasControladasNegras=respuesta.data.tablero.casillasControladasNegras;
          this.nMovimientos = respuesta.data.nMovimientos;
          this.partidaFinalizada = respuesta.data.partidaFinalizada;
          this.jugadasBlancas = respuesta.data.jugadasBlancas;
          this.jugadasNegras = respuesta.data.jugadasNegras
          this.piezas=respuesta.data.tablero.piezas;
          this.mostrarResultado();
        })
      }else{
        console.log("Movimiento no válido");
      }
    }


  }
}
</script>

<style scoped>
  .square:before{
    content: "";
    display: block;
    padding-top: 100%; 	/* initial ratio of 1:1*/
  }
  .blanca{
    background: #fecea0;
  }
  .negra{
    background: #d18b46;
  }
  .movimientoDisponible{
    background: #7ec699!important;
  }
  .conMovimientos:active{
    cursor: grabbing;

  }
</style>