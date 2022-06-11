<template>
  <div v-bind:id="notacionAlgebraica" v-bind:class="{ 'isActiva': isActiva, 'blanca': isBlanca, 'negra': !isBlanca }">

  </div>
</template>

<script>
export default {
  name: "ChessSquare",
  props: ['notacionAlgebraica', 'fila', 'columna', 'isBlanca', 'isActiva'],
  mounted() {
    console.log(this.notacionAlgebraica);
  }
}
</script>

<style scoped>
  .blanca{
    background: #ffffff;
  }
  .negra{
    background: #000000;
  }
</style>