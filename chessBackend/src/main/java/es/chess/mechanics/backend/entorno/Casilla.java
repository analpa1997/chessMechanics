package es.chess.mechanics.backend.entorno;

public class Casilla {

    private int fila;
    private String letraColumna;
    private int columna;
    private String notacionAlgebraica;
    /* 1 a
    2 b
    3 c
    4 d
    5 e
    6 f
    7 g
    8 h
    y sucesivas */
    private boolean blanca;
    private boolean activa;

    public Casilla(){

    }

    public boolean isActiva() {
        return activa;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public String getLetraColumna() {
        return "" + "abcdefghijklmnopqrstuvwxyz".charAt(columna - 1);
    }

    public void setLetraColumna(String letraColumna) {
        this.letraColumna = letraColumna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }

    public boolean isBlanca() {
        return blanca;
    }

    public void setBlanca(boolean blanca) {
        this.blanca = blanca;
    }

    public String getNotacionAlgebraica() {
        return notacionAlgebraica;
    }

    public void setNotacionAlgebraica(String notacionAlgebraica) {
        this.notacionAlgebraica = notacionAlgebraica;
    }

    public void setNotacionAlgebraica() {
        this.notacionAlgebraica = this.toStringNotacionAlgebraica();
    }

    public String toString(){
        String result = "";
        if (!this.isActiva()){
            return "X";
        }else{
            if (this.isBlanca()){
                result = "B";
            }else{
                result = "N";
            }
            return result;
        }

    }

    public String infoCasilla(Tablero tablero, boolean mostrarColoresCasilla){
        String result = "";
        if (!this.isActiva()){
            return "X";
        }else{
            if (mostrarColoresCasilla){
                if (this.isBlanca()){
                    result = "B";
                }else{
                    result = "N";
                }
            }else{

            }
            if (tablero.getPiezas().containsKey(this.toStringNotacionAlgebraica())){
                result += tablero.getPiezas().get(this.toStringNotacionAlgebraica());
            }
            return result;
        }

    }

    public boolean isOcupada(Tablero tablero) {
        return tablero.getPiezas().containsKey(this.toStringNotacionAlgebraica());
    }

    public boolean isOcupadaRival(Tablero tablero, boolean blanca) {
        return tablero.getPiezas().containsKey(this.toStringNotacionAlgebraica()) && (tablero.getPiezas().get(this.toStringNotacionAlgebraica()).isBlanca() != blanca);
    }

    public String toStringNotacionAlgebraica(){
        return ("abcdefghijklmnopqrstuvwxyz".charAt(this.columna - 1)) + "" +  this.fila;
    }
}
