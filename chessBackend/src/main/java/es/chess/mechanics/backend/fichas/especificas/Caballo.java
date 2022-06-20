package es.chess.mechanics.backend.fichas.especificas;

import es.chess.mechanics.backend.entorno.Tablero;
import es.chess.mechanics.backend.fichas.generica.Pieza;

import java.util.HashSet;

public class Caballo extends Pieza {

    public Caballo(boolean color) {
        super(color);
        this.letra = this.toString();
        this.nombreImagen = "caballo" + (this.blanca ? "Blanco" : "Negro");
        this.tipoPieza = "caballo";
    }

    public Caballo(){
        this.tipoPieza = "caballo";
    }

    @Override
    public void actualizarCasillasDisponibles(Tablero tablero){
        this.casillasDisponibles = new HashSet<>();
        this.casillasDisponibles.addAll(movimientosDisponiblesCaballo(tablero));
    }

    @Override
    public void actualizarCasillasControladas(Tablero tablero){
        this.casillasControladas = new HashSet<>();
        this.casillasControladas.addAll(casillasControladasCaballo(tablero));
    }

    public String toString(){
        if (this.isBlanca()){
            return "C";
        }else{
            return "c";
        }
    }

    public String toStringNotacionAlgebraica(){
        return "C";
    }

    @Override
    public String informacionMovimientosPieza(Tablero tablero){
        String result = "";
        result += "Pieza: Caballo\n";
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
