package es.chess.mechanics.backend.fichas.especificas;

import es.chess.mechanics.backend.entorno.Tablero;
import es.chess.mechanics.backend.fichas.generica.Pieza;

import java.util.HashSet;

public class Dama extends Pieza {

    public Dama(boolean color) {
        super(color);
    }

    @Override
    public void actualizarCasillasDisponibles(Tablero tablero){
        this.casillasDisponibles = new HashSet<>();
        this.casillasDisponibles.addAll(movimientosDisponiblesAlfil(tablero));
        this.casillasDisponibles.addAll(movimientosDisponiblesTorre(tablero));
    }

    @Override
    public void actualizarCasillasControladas(Tablero tablero){
        this.casillasControladas = new HashSet<>();
        this.casillasControladas.addAll(casillasControladasAlfil(tablero));
        this.casillasControladas.addAll(casillasControladasTorre(tablero));
    }

    public String toString(){
        if (this.isBlanca()){
            return "D";
        }else{
            return "d";
        }
    }

    public String toStringNotacionAlgebraica(){
        return "D";
    }

    @Override
    public String informacionMovimientosPieza(Tablero tablero){
        String result = "";
        result += "Pieza: Dama\n";
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
