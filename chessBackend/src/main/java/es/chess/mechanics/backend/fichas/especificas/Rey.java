package es.chess.mechanics.backend.fichas.especificas;

import es.chess.mechanics.backend.entorno.Tablero;
import es.chess.mechanics.backend.fichas.generica.Pieza;

import java.util.HashSet;

public class Rey extends Pieza {

    public Rey(boolean color) {
        super(color);
        this.letra = this.toString();
        this.nombreImagen = "rey" + (this.blanca ? "Blanco" : "Negro");
        this.tipoPieza = "rey";
    }

    public Rey(){
        this.tipoPieza = "rey";
    }

    @Override
    public void actualizarCasillasDisponibles(Tablero tablero){
        this.casillasDisponibles = new HashSet<>();
        this.casillasDisponibles.addAll(movimientosDisponiblesRey(tablero));
    }

    @Override
    public void actualizarCasillasControladas(Tablero tablero){
        this.casillasControladas = new HashSet<>();
        this.casillasControladas.addAll(casillasControladasRey(tablero));
    }

    public String toString(){
        if (this.isBlanca()){
            return "R";
        }else{
            return "r";
        }
    }

    @Override
    public String toStringIngles(){
        if (this.isBlanca()){
            return "K";
        }else{
            return "k";
        }
    }

    public String toStringNotacionAlgebraica(){
        return "R";
    }

    @Override
    public String informacionMovimientosPieza(Tablero tablero){
        String result = "";
        result += "Pieza: Rey\n";
        result += this.isBlanca() ? "Color: Blanco" : "Color: Negro";
        result += "\nString: " + this.getCasilla();
        result += "\nCasillas disponibles:\n";
        for (String casilla: this.casillasDisponibles){
            result += tablero.obtenerCasillaNotacionAlgebraica(casilla).toStringNotacionAlgebraica() + "\n";
        }
        result += "\n";
        return result;
    }
}
