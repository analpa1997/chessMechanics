package es.chess.mechanics.backend.fichas.especificas;

import es.chess.mechanics.backend.entorno.Tablero;
import es.chess.mechanics.backend.fichas.generica.Pieza;

import java.util.HashSet;

public class Alfil extends Pieza {

    public Alfil(boolean color) {
        super(color);
        this.letra = this.toString();
        this.nombreImagen = "alfil" + (this.blanca ? "Blanco" : "Negro");
        this.tipoPieza = "alfil";
    }

    public Alfil(){
        this.tipoPieza = "alfil";
    }

    @Override
    public void actualizarCasillasDisponibles(Tablero tablero){
        this.casillasDisponibles = new HashSet<>();
        this.casillasDisponibles.addAll(movimientosDisponiblesAlfil(tablero));
    }

    @Override
    public void actualizarCasillasControladas(Tablero tablero){
        this.casillasControladas = new HashSet<>();
        this.casillasControladas.addAll(casillasControladasAlfil(tablero));
    }

    public String toString(){
        if (this.isBlanca()){
            return "A";
        }else{
            return "a";
        }
    }

    public String toStringNotacionAlgebraica(){
        return "A";
    }

    @Override
    public String informacionMovimientosPieza(Tablero tablero){
        String result = "";
        result += "Pieza: Alfil\n";
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
