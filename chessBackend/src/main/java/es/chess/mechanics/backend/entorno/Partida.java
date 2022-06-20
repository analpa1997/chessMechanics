package es.chess.mechanics.backend.entorno;

import es.chess.mechanics.backend.fichas.generica.Pieza;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

public class Partida {

    private Tablero tablero;
    private boolean turnoBlancas;
    private int resultado;
    private int nMovimientos;
    private boolean partidaFinalizada;
    private ArrayList<String> jugadasBlancas;
    private ArrayList<String> jugadasNegras;

    public Partida(){
        this.tablero = new Tablero();
        this.turnoBlancas = true;
        this.partidaFinalizada = false;
        this.nMovimientos = 1;
        this.jugadasBlancas = new ArrayList<>();
        this.jugadasNegras = new ArrayList<>();
    }

    public void pasarTurno(){
        this.turnoBlancas = !this.turnoBlancas;
        if (this.turnoBlancas){
            this.nMovimientos ++;
        }
    }

    private void movimiento(Pieza pieza, String destino){
        tablero.getPiezas().remove(pieza.getCasilla());
        pieza.movimiento(destino);
        tablero.getPiezas().put(pieza.getCasilla(), pieza);

    }

    public String realizarMovimiento(String casillaOrigen, String casillaDestino){
        String s = "";
        Pieza piezaAMover = null;
        if (!partidaFinalizada){
            piezaAMover = tablero.getPiezas().get(casillaOrigen);
            if (tablero.getPiezas().containsKey(casillaOrigen) && tablero.getPiezas().get(casillaOrigen).isBlanca() && turnoBlancas && piezaAMover.getCasillasDisponibles().contains(casillaDestino)){
                boolean captura = (tablero.getPiezas().containsKey(casillaDestino) && !tablero.getPiezas().get(casillaDestino).isBlanca());
                this.movimiento(piezaAMover, casillaDestino);
                tablero.setPiezasDandoJaque(new ArrayList<>());
                tablero.comprobarJaque();
                // PRIMERO MIRAMOS CASILLAS CONTROLADAS DEL COLOR, PARA QUITARLAS DE LAS DISPONIBLES DEL REY CONTRARIO
                tablero.setCasillasControladasBlancas(new TreeSet<>());
                tablero.setCasillasControladasNegras(new TreeSet<>());
                for (Map.Entry<String, Pieza> pieza : tablero.getPiezas().entrySet()){
                    pieza.getValue().actualizarCasillasControladas(tablero);
                    if (pieza.getValue().isBlanca()){
                        tablero.getCasillasControladasBlancas().addAll(pieza.getValue().getCasillasControladas());
                    }else{
                        tablero.getCasillasControladasNegras().addAll(pieza.getValue().getCasillasControladas());
                    }
                }
                for (Map.Entry<String, Pieza> pieza : tablero.getPiezas().entrySet()){
                    if (!pieza.getValue().isBlanca()){
                        pieza.getValue().actualizarCasillasDisponibles(tablero);
                    }
                }
                String jugadaNotacionAlgebraica = piezaAMover.toStringNotacionAlgebraica();
                if (captura){
                    if (piezaAMover.toStringNotacionAlgebraica().equals("")){
                        jugadaNotacionAlgebraica += casillaOrigen.substring(0,1);
                    }
                    jugadaNotacionAlgebraica += "x";
                }
                jugadaNotacionAlgebraica += casillaDestino;
                if (tablero.isJaque()){
                    jugadaNotacionAlgebraica += "+";
                }
                this.jugadasBlancas.add(jugadaNotacionAlgebraica);
                comprobarResultado();
                s = this.nMovimientos + "\t" + piezaAMover.toStringNotacionAlgebraica() + (captura ? "x" : "") + casillaDestino + "\t\t";
                pasarTurno();
            }else if (tablero.getPiezas().containsKey(casillaOrigen) && !tablero.getPiezas().get(casillaOrigen).isBlanca() && !turnoBlancas && piezaAMover.getCasillasDisponibles().contains(casillaDestino)){
                boolean captura = (tablero.getPiezas().containsKey(casillaDestino) && tablero.getPiezas().get(casillaDestino).isBlanca());
                this.movimiento(piezaAMover, casillaDestino);
                tablero.setPiezasDandoJaque(new ArrayList<>());
                tablero.comprobarJaque();
                // PRIMERO MIRAMOS CASILLAS CONTROLADAS DEL COLOR, PARA QUITARLAS DE LAS DISPONIBLES DEL REY CONTRARIO
                tablero.setCasillasControladasBlancas(new TreeSet<>());
                tablero.setCasillasControladasNegras(new TreeSet<>());
                for (Map.Entry<String, Pieza> pieza : tablero.getPiezas().entrySet()){
                    pieza.getValue().actualizarCasillasControladas(tablero);
                    if (pieza.getValue().isBlanca()){
                        tablero.getCasillasControladasBlancas().addAll(pieza.getValue().getCasillasControladas());
                    }else{
                        tablero.getCasillasControladasNegras().addAll(pieza.getValue().getCasillasControladas());
                    }
                }
                for (Map.Entry<String, Pieza> pieza : tablero.getPiezas().entrySet()){
                    if (pieza.getValue().isBlanca()){
                        pieza.getValue().actualizarCasillasDisponibles(tablero);
                    }
                }
                String jugadaNotacionAlgebraica = piezaAMover.toStringNotacionAlgebraica();
                if (captura){
                    if (piezaAMover.toStringNotacionAlgebraica().equals("")){
                        jugadaNotacionAlgebraica += casillaOrigen.substring(0,1);
                    }
                    jugadaNotacionAlgebraica += "x";
                }
                jugadaNotacionAlgebraica += casillaDestino;
                if (tablero.isJaque()){
                    jugadaNotacionAlgebraica += "+";
                }
                this.jugadasNegras.add(jugadaNotacionAlgebraica);
                comprobarResultado();
                s = this.nMovimientos + "\t" + piezaAMover.toStringNotacionAlgebraica() + (captura ? "x" : "") + casillaDestino + "\t\t";
                pasarTurno();
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

    public int getnMovimientos() {
        return nMovimientos;
    }

    public void setnMovimientos(int nMovimientos) {
        this.nMovimientos = nMovimientos;
    }

    public boolean isPartidaFinalizada() {
        return partidaFinalizada;
    }

    public void setPartidaFinalizada(boolean partidaFinalizada) {
        this.partidaFinalizada = partidaFinalizada;
    }

    public ArrayList<String> getJugadasBlancas() {
        return jugadasBlancas;
    }

    public void setJugadasBlancas(ArrayList<String> jugadasBlancas) {
        this.jugadasBlancas = jugadasBlancas;
    }

    public ArrayList<String> getJugadasNegras() {
        return jugadasNegras;
    }

    public void setJugadasNegras(ArrayList<String> jugadasNegras) {
        this.jugadasNegras = jugadasNegras;
    }

    public void comprobarResultado(){
        /*TreeMap<String, Pieza> piezasAComprobar;
        if (turnoBlancas){
            piezasAComprobar = tablero.getPiezasBlancas();
        }else{
            piezasAComprobar = tablero.getPiezasNegras();
        }*/
        boolean conMovimientos = false;
        for (Map.Entry<String, Pieza> pieza : tablero.getPiezas().entrySet()){
            if ((pieza.getValue().isBlanca() == turnoBlancas) && pieza.getValue().getCasillasDisponibles().size() > 0){
                conMovimientos = true;
                break;
            }
        }
        if (!conMovimientos){
            if (tablero.isJaque()){
                if (turnoBlancas){
                    resultado = 1;
                    String ultimaJugada = jugadasBlancas.get(nMovimientos-1);
                    jugadasBlancas.remove(nMovimientos-1);
                    ultimaJugada = ultimaJugada.replace("+", "#");
                    jugadasBlancas.add(ultimaJugada);
                }else{
                    resultado = -1;
                    String ultimaJugada = jugadasNegras.get(nMovimientos-1);
                    jugadasNegras.remove(nMovimientos-1);
                    ultimaJugada = ultimaJugada.replace("+", "#");
                    jugadasNegras.add(ultimaJugada);
                }
            }else{
                resultado = 0;
            }
            partidaFinalizada = true;
        }
    }
}
