package es.chess.mechanics.backend.entorno;

import es.chess.mechanics.backend.fichas.generica.Pieza;

import java.util.Map;
import java.util.TreeMap;

public class Partida {

    private Tablero tablero;
    private boolean turnoBlancas;
    private int resultado;
    private int nMovimientos;
    private boolean partidaFinalizada;

    public Partida(){
        this.tablero = new Tablero();
        this.turnoBlancas = true;
        this.partidaFinalizada = false;
        this.nMovimientos = 1;
    }

    public void pasarTurno(){
        this.turnoBlancas = !this.turnoBlancas;
        if (this.turnoBlancas){
            this.nMovimientos ++;
        }
    }

    private void movimiento(Pieza pieza, String destino){
        if(pieza.isBlanca()){
            tablero.getPiezasBlancas().remove(pieza.getCasilla());
        }else{
            tablero.getPiezasNegras().remove(pieza.getCasilla());
        }
        pieza.movimiento(destino);
        if(pieza.isBlanca()){
            tablero.getPiezasBlancas().put(pieza.getCasilla(), pieza);
        }else{
            tablero.getPiezasNegras().put(pieza.getCasilla(), pieza);
        }
    }

    public String realizarMovimiento(String casillaOrigen, String casillaDestino){
        String s = "";
        Pieza piezaAMover = null;
        if (!partidaFinalizada){
            if (tablero.getPiezasBlancas().containsKey(casillaOrigen) && turnoBlancas){
                piezaAMover = tablero.getPiezasBlancas().get(casillaOrigen);
                if (piezaAMover.getCasillasDisponibles().contains(casillaDestino)){
                    boolean captura = tablero.getPiezasNegras().containsKey(casillaDestino);
                    this.movimiento(piezaAMover, casillaDestino);
                    // PRIMERO MIRAMOS CASILLAS CONTROLADAS DEL COLOR, PARA QUITARLAS DE LAS DISPONIBLES DEL REY CONTRARIO
                    for (Map.Entry<String, Pieza> pieza : tablero.getPiezasBlancas().entrySet()){
                        pieza.getValue().actualizarCasillasControladas(tablero);
                        //pieza.getValue().actualizarCasillasDisponibles(tablero);
                    }
                    for (Map.Entry<String, Pieza> pieza : tablero.getPiezasNegras().entrySet()){
                        pieza.getValue().actualizarCasillasControladas(tablero);
                        pieza.getValue().actualizarCasillasDisponibles(tablero);
                    }
                    comprobarResultado();
                    s = this.nMovimientos + "\t" + piezaAMover.toStringNotacionAlgebraica() + (captura ? "x" : "") + casillaDestino + "\t\t";
                    pasarTurno();
                }
            }else if (tablero.getPiezasNegras().containsKey(casillaOrigen) && !turnoBlancas){
                piezaAMover = tablero.getPiezasNegras().get(casillaOrigen);
                if (piezaAMover.getCasillasDisponibles().contains(casillaDestino)){
                    boolean captura = tablero.getPiezasBlancas().containsKey(casillaDestino);
                    this.movimiento(piezaAMover, casillaDestino);
                    // PRIMERO MIRAMOS CASILLAS CONTROLADAS DEL COLOR, PARA QUITARLAS DE LAS DISPONIBLES DEL REY CONTRARIO
                    for (Map.Entry<String, Pieza> pieza : tablero.getPiezasNegras().entrySet()){
                        pieza.getValue().actualizarCasillasControladas(tablero);
                        //pieza.getValue().actualizarCasillasDisponibles(tablero);
                    }
                    for (Map.Entry<String, Pieza> pieza : tablero.getPiezasBlancas().entrySet()){
                        pieza.getValue().actualizarCasillasControladas(tablero);
                        pieza.getValue().actualizarCasillasDisponibles(tablero);
                    }
                    s = piezaAMover.toStringNotacionAlgebraica() + (captura ? "x" : "") + casillaDestino + "\n";
                    pasarTurno();
                }
            }else{
                return "Movimiento no válido";
            }
        }else{
            switch (resultado){
                // COMPROBAR SI ES MATE O ES AHOGADO
                // FUTURO, COMPROBAR TRIPLE REPETICIÓN / 50 MOVIMIENTOS
                case (-1):
                    return "Partida finalizada, ganan las negras";
                case (0):
                    return "Partida finalizada, son tablas";
                case (1):
                    return "Partida finalizada, ganan las blancas";
            }
        }
        return s;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public void setTablero(Tablero tablero) {
        this.tablero = tablero;
    }

    public boolean isTurnoBlancas() {
        return turnoBlancas;
    }

    public void setTurnoBlancas(boolean turnoBlancas) {
        this.turnoBlancas = turnoBlancas;
    }

    public int getResultado() {
        return resultado;
    }

    public void setResultado(int resultado) {
        this.resultado = resultado;
    }

    public void comprobarResultado(){
        TreeMap<String, Pieza> piezasAComprobar;
        if (turnoBlancas){
            piezasAComprobar = tablero.getPiezasBlancas();
        }else{
            piezasAComprobar = tablero.getPiezasNegras();
        }
        boolean conMovimientos = false;
        for (Map.Entry<String, Pieza> pieza : piezasAComprobar.entrySet()){
            if (pieza.getValue().getCasillasDisponibles().size() > 0){
                conMovimientos = true;
                break;
            }
        }
        if (!conMovimientos){
            if (tablero.isJaque()){
                resultado = turnoBlancas ? -1 : 1;
            }else{
                resultado = 0;
            }
            partidaFinalizada = true;
        }
    }
}
