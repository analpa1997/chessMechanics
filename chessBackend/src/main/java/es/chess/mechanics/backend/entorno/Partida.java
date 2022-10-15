package es.chess.mechanics.backend.entorno;

import es.chess.mechanics.backend.fichas.especificas.Peon;
import es.chess.mechanics.backend.fichas.especificas.Rey;
import es.chess.mechanics.backend.fichas.generica.Pieza;

import java.util.*;

public class Partida {

    private Tablero tablero;
    private boolean turnoBlancas;
    private int resultado;
    private int nMovimientos;
    private int nTurnos;
    private boolean partidaFinalizada;
    private ArrayList<String> jugadasBlancas;
    private ArrayList<String> jugadasNegras;
    private PosicionFEN notacionFENActual;

    private String notacionFENActualString;

    private ArrayList<PosicionFEN> todasPosicionesFEN;

    private TreeMap<String, Integer> todasPosicionesFENReducido;

    public Partida(){
        this.tablero = new Tablero();
        this.turnoBlancas = true;
        this.partidaFinalizada = false;
        this.nMovimientos = 1;
        this.nTurnos = 0;
        this.jugadasBlancas = new ArrayList<>();
        this.jugadasNegras = new ArrayList<>();
        this.resultado = 99;
        this.notacionFENActual = new PosicionFEN();
        notacionFENActual.generarFEN(this);
        this.notacionFENActualString = this.notacionFENActual.toString();
        this.todasPosicionesFEN = new ArrayList<>();
        this.todasPosicionesFEN.add(notacionFENActual);
        this.todasPosicionesFENReducido = new TreeMap<>();
        this.todasPosicionesFENReducido.put(notacionFENActual.toStringReducido(), 1);

    }

    public void pasarTurno(){
        this.turnoBlancas = !this.turnoBlancas;
        if (this.turnoBlancas){
            this.nMovimientos ++;
        }
        if (partidaFinalizada){
            for (Map.Entry<String, Pieza> pieza : tablero.getPiezas().entrySet()){
                pieza.getValue().setCasillasDisponibles(new HashSet<>());
            }
        }
    }

    private void movimiento(Pieza pieza, String destino, boolean capturaPaso){
        Casilla casillaDestino = tablero.obtenerCasillaNotacionAlgebraica(destino);
        Casilla casillaOrigen = tablero.obtenerCasillaNotacionAlgebraica(pieza.getCasilla());
        tablero.getPiezas().remove(pieza.getCasilla());
        pieza.movimiento(destino);
        tablero.getPiezas().put(pieza.getCasilla(), pieza);
        if (pieza instanceof Rey && Math.abs(casillaDestino.getColumna() - casillaOrigen.getColumna())==2){
            // MOVIMIENTO DE ENROQUE, HAY QUE MOVER LA TORRE
            Pieza torreEnroque = null;
            Casilla casillaDestinoTorre = null;
            // Vemos si el enroque es largo o no, para mover la torre donde corresponda.
            if (casillaDestino.getColumna() > casillaOrigen.getColumna()){
                torreEnroque = tablero.getPiezaCasilla(tablero.obtenerCasilla(casillaOrigen.getFila(), tablero.getNumeroColumnas()).toStringNotacionAlgebraica());
                casillaDestinoTorre = tablero.obtenerCasilla(casillaOrigen.getFila(), casillaDestino.getColumna()-1);
            }else{
                torreEnroque = tablero.getPiezaCasilla(tablero.obtenerCasilla(casillaOrigen.getFila(), 1).toStringNotacionAlgebraica());
                casillaDestinoTorre = tablero.obtenerCasilla(casillaOrigen.getFila(), casillaDestino.getColumna()+1);
            }
            tablero.getPiezas().remove(torreEnroque.getCasilla());
            torreEnroque.movimiento(casillaDestinoTorre.toStringNotacionAlgebraica());
            tablero.getPiezas().put(torreEnroque.getCasilla(), torreEnroque);
        }
        if (capturaPaso){
            int desplazamientoCasillaDetras = pieza.isBlanca() ? -1 : 1;
            Casilla casillaPeonCapturado = tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(destino).getFila()+desplazamientoCasillaDetras, tablero.obtenerCasillaNotacionAlgebraica(destino).getColumna());
            tablero.getPiezas().remove(casillaPeonCapturado.toStringNotacionAlgebraica());
        }
    }

    private String jugadaNotacionAlgebraica(Pieza pieza, String casillaOrigen, String casillaDestino, boolean captura){
        boolean enroque = pieza instanceof Rey && (Math.abs(tablero.obtenerCasillaNotacionAlgebraica(casillaOrigen).getColumna() - tablero.obtenerCasillaNotacionAlgebraica(casillaDestino).getColumna())==2);
        String jugadaNotacionAlgebraica = "";
        if (enroque){
            if (tablero.obtenerCasillaNotacionAlgebraica(casillaOrigen).getColumna() > tablero.obtenerCasillaNotacionAlgebraica(casillaDestino).getColumna()){
                jugadaNotacionAlgebraica = "O-O-O";
            }else{
                jugadaNotacionAlgebraica = "O-O";
            }
        }else{
            jugadaNotacionAlgebraica = pieza.toStringNotacionAlgebraica();
            if (captura){
                if (pieza.toStringNotacionAlgebraica().equals("")){
                    jugadaNotacionAlgebraica += casillaOrigen.substring(0,1);
                }
                jugadaNotacionAlgebraica += "x";
            }
            jugadaNotacionAlgebraica += casillaDestino;
            if (tablero.isJaque()){
                jugadaNotacionAlgebraica += "+";
            }
        }
        return jugadaNotacionAlgebraica;
    }

    public String realizarMovimiento(String casillaOrigen, String casillaDestino){
        String s = "";
        Pieza piezaAMover = null;
        if (!partidaFinalizada){
            piezaAMover = tablero.getPiezas().get(casillaOrigen);
            if (tablero.getPiezas().containsKey(casillaOrigen) && tablero.getPiezas().get(casillaOrigen).isBlanca() == turnoBlancas && piezaAMover.getCasillasDisponibles().contains(casillaDestino)){
                int numDetrasDestinoPeon = piezaAMover.isBlanca() ? -1 : 1;
                Casilla detrasCasillaDestino = tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(casillaDestino).getFila() + numDetrasDestinoPeon, tablero.obtenerCasillaNotacionAlgebraica(casillaDestino).getColumna());
                boolean capturaPaso = tablero.isComerPaso() && piezaAMover instanceof Peon &&
                        tablero.obtenerCasillaNotacionAlgebraica(casillaOrigen).enLaMismaFila(detrasCasillaDestino) &&
                        (tablero.getPiezas().containsKey(detrasCasillaDestino.toStringNotacionAlgebraica()) && tablero.getPiezas().get(detrasCasillaDestino.toStringNotacionAlgebraica()).isBlanca() != piezaAMover.isBlanca())
                        ;
                boolean captura = capturaPaso || (tablero.getPiezas().containsKey(casillaDestino) && tablero.getPiezas().get(casillaDestino).isBlanca() != turnoBlancas);
                this.movimiento(piezaAMover, casillaDestino, capturaPaso);
                if ((piezaAMover instanceof Peon &&
                        (Math.abs(tablero.obtenerCasillaNotacionAlgebraica(casillaOrigen).getFila() - tablero.obtenerCasillaNotacionAlgebraica(casillaDestino).getFila())) == 2)){
                    tablero.setUltimoMovimientoPeonDosCasillas(casillaDestino);
                    tablero.setComerPaso(true);
                }else{
                    tablero.setUltimoMovimientoPeonDosCasillas("");
                    tablero.setComerPaso(false);
                }
                tablero.setPiezasDandoJaque(new ArrayList<>());
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
                tablero.comprobarJaque();
                this.comprobarEnroques();
                for (Map.Entry<String, Pieza> pieza : tablero.getPiezas().entrySet()){
                    if (pieza.getValue().isBlanca() != turnoBlancas) {
                        pieza.getValue().reiniciarCasillasDisponibles();
                        if ((tablero.getPiezasDandoJaque().size() <= 1 || (tablero.getPiezasDandoJaque().size() > 1 && pieza.getValue() instanceof Rey))) {
                            pieza.getValue().actualizarCasillasDisponibles(tablero);
                        }
                    }
                }
                ArrayList<String> jugadas = turnoBlancas ? this.jugadasBlancas : this.jugadasNegras ;
                boolean reiniciarContadorTurnos = (captura) || (piezaAMover instanceof Peon);
                this.nTurnos = reiniciarContadorTurnos ? 0 : this.nTurnos+1;
                String jugada = this.jugadaNotacionAlgebraica(piezaAMover, casillaOrigen, casillaDestino, captura);
                jugadas.add(jugada);
                comprobarResultado();
                s = this.nMovimientos + "\t" + jugada + "\t\t";
                pasarTurno();
            }else{
                return "Movimiento no válido";
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

    public PosicionFEN getNotacionFENActual() {
        return notacionFENActual;
    }

    public void setNotacionFENActual(PosicionFEN notacionFENActual) {
        this.notacionFENActual = notacionFENActual;
    }

    public int getnTurnos() {
        return nTurnos;
    }

    public void setnTurnos(int nTurnos) {
        this.nTurnos = nTurnos;
    }

    public ArrayList<PosicionFEN> getTodasPosicionesFEN() {
        return todasPosicionesFEN;
    }

    public void setTodasPosicionesFEN(ArrayList<PosicionFEN> todasPosicionesFEN) {
        this.todasPosicionesFEN = todasPosicionesFEN;
    }

    public TreeMap<String, Integer> getTodasPosicionesFENReducido() {
        return todasPosicionesFENReducido;
    }

    public void setTodasPosicionesFENReducido(TreeMap<String, Integer> todasPosicionesFENReducido) {
        this.todasPosicionesFENReducido = todasPosicionesFENReducido;
    }

    public String getNotacionFENActualString() {
        return notacionFENActualString;
    }

    public void setNotacionFENActualString(String notacionFENActualString) {
        this.notacionFENActualString = notacionFENActualString;
    }

    public void comprobarResultado(){
        if (!this.turnoBlancas){
            this.nMovimientos = this.nMovimientos + 1;
        }
        notacionFENActual.generarFEN(this);
        if (!this.turnoBlancas){
            this.nMovimientos = this.nMovimientos - 1;
        }
        this.notacionFENActualString = this.notacionFENActual.toString();
        notacionFENActual.setTurnoBlancas(!notacionFENActual.isTurnoBlancas());
        this.todasPosicionesFEN.add(notacionFENActual);
        System.out.println(notacionFENActual);
        if (this.todasPosicionesFENReducido.containsKey(notacionFENActual.toStringReducido())){
            this.todasPosicionesFENReducido.put(notacionFENActual.toStringReducido(), this.todasPosicionesFENReducido.get(notacionFENActual.toStringReducido())+1);
        }else{
            System.out.println(this.todasPosicionesFENReducido.put(notacionFENActual.toStringReducido(), 1));
        }
        boolean conMovimientos = false;
        for (Map.Entry<String, Pieza> pieza : tablero.getPiezas().entrySet()){
            if ((pieza.getValue().isBlanca() != turnoBlancas) && pieza.getValue().getCasillasDisponibles().size() > 0){
                conMovimientos = true;
                break;
            }
        }
        boolean tripleRepeticion = this.todasPosicionesFENReducido.get(this.notacionFENActual.toStringReducido()) > 3;
        if (tripleRepeticion || this.nTurnos == 100){
            // TRIPLE REPETICION Y 50 MOVIMIENTOS
            resultado = 0;
            partidaFinalizada = true;
        }else if (!conMovimientos){
            if (tablero.isJaque()){
                // JAQUE MATE
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
                // AHOGADO
                resultado = 0;
            }
            partidaFinalizada = true;
        }
    }

    public void comprobarEnroques (){
        boolean [] enroquesBlancas = new boolean[2];
        boolean [] enroquesNegras = new boolean[2];
        // ENROQUE CORTO BLANCAS
        enroquesBlancas[0] = tablero.checkEnroque(true, false);
        // ENROQUE LARGO BLANCAS
        enroquesBlancas[1] = tablero.checkEnroque(true, true);
        // ENROQUE CORTO NEGRAS
        enroquesNegras[0] = tablero.checkEnroque(false, false);
        // ENROQUE LARGO NEGRAS
        enroquesNegras[1] = tablero.checkEnroque(false, true);
        tablero.setEnroquesBlancas(enroquesBlancas);
        tablero.setEnroquesNegras(enroquesNegras);
    }
}
