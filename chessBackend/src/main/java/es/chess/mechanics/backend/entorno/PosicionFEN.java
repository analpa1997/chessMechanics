package es.chess.mechanics.backend.entorno;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import es.chess.mechanics.backend.fichas.generica.Pieza;

import java.util.Map;
import java.util.Objects;

@JsonSerialize(keyUsing = PosicionFENSerializer.class)
@JsonDeserialize(keyUsing = PosicionFENDeserializer.class)
public class PosicionFEN implements Comparable<PosicionFEN>{

    private String posicion;
    private boolean turnoBlancas;
    private String enroques;
    private String comerPaso;
    private int nTurnos;
    private int nMovimientos;

    public PosicionFEN() {
        this.posicion = "";
        this.turnoBlancas = true;
        this.enroques = "";
        this.comerPaso = "";
        this.nTurnos = 0;
        this.nMovimientos = 0;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getEnroques() {
        return enroques;
    }

    public void setEnroques(String enroques) {
        this.enroques = enroques;
    }

    public String getComerPaso() {
        return comerPaso;
    }

    public void setComerPaso(String comerPaso) {
        this.comerPaso = comerPaso;
    }

    public int getnTurnos() {
        return nTurnos;
    }

    public void setnTurnos(int nTurnos) {
        this.nTurnos = nTurnos;
    }

    public int getnMovimientos() {
        return nMovimientos;
    }

    public void setnMovimientos(int nMovimientos) {
        this.nMovimientos = nMovimientos;
    }

    public boolean isTurnoBlancas() {
        return turnoBlancas;
    }

    public void setTurnoBlancas(boolean turnoBlancas) {
        this.turnoBlancas = turnoBlancas;
    }

    public String toString() {
        String turno = turnoBlancas ? "b" : "n" ;
        return posicion + " " + turno + " " + enroques + " " + comerPaso + " " + nTurnos + " " + nMovimientos;
    }

    public String toStringReducido() {
        String turno = turnoBlancas ? "b" : "n" ;
        return posicion + " " + turno + " " + enroques + " " + comerPaso;
    }


    @Override
    public boolean equals(Object obj) {
        PosicionFEN posicionFEN = (PosicionFEN) obj;
        return this.posicion.equals(posicionFEN.getPosicion()) && this.turnoBlancas == posicionFEN.turnoBlancas;
    }

    public void generarFEN (Partida partida){
        String posicion = "";
        Tablero tablero = partida.getTablero();
        int filaInicio = tablero.getNumeroFilas();
        int nCasillasVacias = 0;
        for (Map.Entry<String, Casilla> square : tablero.getCasillas().entrySet()) {
            Casilla casilla = square.getValue();
            if (filaInicio != casilla.getFila()){
                if (nCasillasVacias > 0){
                    posicion += nCasillasVacias;
                }
                posicion += "/";
                nCasillasVacias = 0;
                filaInicio--;
            }
            Pieza pieza = tablero.getPiezaCasilla(casilla.getNotacionAlgebraica());
            if (pieza != null){
                if (nCasillasVacias > 0){
                    posicion += nCasillasVacias;
                }
                posicion += pieza;
                nCasillasVacias = 0;
            }else{
                nCasillasVacias++;
            }
        }
        this.posicion = posicion;
        this.enroques = "-";
        this.comerPaso = "-";
        this.turnoBlancas = partida.isTurnoBlancas();
        this.nTurnos = partida.getnTurnos();
        this.nMovimientos = partida.getnMovimientos();
    }

    @Override
    public int compareTo(PosicionFEN o) {
        if (this.nMovimientos != o.nMovimientos){
            return this.nMovimientos - o.nMovimientos;
        }else{
            if (this.turnoBlancas == o.turnoBlancas){
                return 0;
            }else{
                if (this.turnoBlancas){
                    return -1;
                }else{
                    return 1;
                }
            }
        }
    }

    @Override
    public int hashCode() {
        String turno = turnoBlancas ? "b" : "n" ;
        String idPosicionFEN = posicion + " " + turno;
        return Objects.hash(idPosicionFEN);
    }
}
