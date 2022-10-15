package es.chess.mechanics.backend.fichas.especificas;

import es.chess.mechanics.backend.entorno.Tablero;
import es.chess.mechanics.backend.fichas.generica.Pieza;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContext;
import org.springframework.context.i18n.LocaleContextHolder;

import java.util.HashSet;
import java.util.Locale;
import java.util.ResourceBundle;

public class Alfil extends Pieza {

    //ResourceBundle rb = ResourceBundle.getBundle("Language", Locale.forLanguageTag("en"));

    //private static ResourceBundle rb = ResourceBundle.getBundle("Language", Locale.forLanguageTag("en"));


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
            //return ((String) rb.getObject("alfil")).toUpperCase();
            //return rb.getString("alfil").toUpperCase();
        }else{
            return "a";
            //return ((String) rb.getObject("alfil")).toLowerCase();
            //return rb.getString("alfil").toLowerCase();
        }
    }
    @Override
    public String toStringIngles(){
        if (this.isBlanca()){
            return "B";
        }else{
            return "b";
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
