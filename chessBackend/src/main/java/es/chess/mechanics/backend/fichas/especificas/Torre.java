package es.chess.mechanics.backend.fichas.especificas;

import es.chess.mechanics.backend.entorno.Tablero;
import es.chess.mechanics.backend.fichas.generica.Pieza;

import java.util.HashSet;

public class Torre extends Pieza {

    public Torre(boolean color) {
        super(color);
        this.letra = this.toString();
        this.nombreImagen = "torre" + (this.blanca ? "Blanca" : "Negra");
        this.tipoPieza = "torre";
    }

    public Torre(){
        this.tipoPieza = "torre";
    }

    @Override
    public void actualizarCasillasDisponibles(Tablero tablero){
        this.casillasDisponibles = new HashSet<>();
        this.casillasDisponibles.addAll(movimientosDisponiblesTorre(tablero));
    }

    @Override
    public void actualizarCasillasControladas(Tablero tablero){
        this.casillasControladas = new HashSet<>();
        this.casillasControladas.addAll(casillasControladasTorre(tablero));
    }

    public String toString(){
        if (this.isBlanca()){
            return "T";
        }else{
            return "t";
        }
    }

    @Override
    public String toStringIngles(){
        if (this.isBlanca()){
            return "R";
        }else{
            return "r";
        }
    }

    public String toStringNotacionAlgebraica(){
        return "T";
    }

    @Override
    public String informacionMovimientosPieza(Tablero tablero){
        String result = "";
        result += "Pieza: Torre\n";
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
