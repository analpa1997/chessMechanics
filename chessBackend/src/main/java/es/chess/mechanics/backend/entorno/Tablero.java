package es.chess.mechanics.backend.entorno;

import es.chess.mechanics.backend.fichas.especificas.*;
import es.chess.mechanics.backend.fichas.generica.Pieza;

import java.util.*;

public class Tablero {

    private int numeroFilas;
    private int numeroColumnas;
    private TreeMap<String, Casilla> casillas;
    private boolean jaque;
    private ArrayList<Pieza> piezasDandoJaque;
    private TreeMap<String, Pieza> piezas;
    private TreeSet<String> casillasControladasBlancas;
    private TreeSet<String> casillasControladasNegras;

    private boolean[] enroquesBlancas;
    private boolean[] enroquesNegras;
    private boolean comerPaso;

    private String ultimoMovimientoPeonDosCasillas;

    //private Casilla[][] tableroArray;

    public Tablero(){
        numeroFilas = 8;
        numeroColumnas = 8;
        piezas = new TreeMap<>();
        piezasDandoJaque = new ArrayList<>();
        casillasControladasNegras = new TreeSet<>();
        casillasControladasBlancas = new TreeSet<>();
        this.enroquesBlancas = new boolean[]{true, true};
        this.enroquesNegras = new boolean[]{true, true};
        casillas = new TreeMap<String, Casilla>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int filaCasilla1 = Integer.parseInt(o1.split("_")[0]);
                int colCasilla1 = Integer.parseInt(o1.split("_")[1]);
                int filaCasilla2 = Integer.parseInt(o2.split("_")[0]);
                int colCasilla2 = Integer.parseInt(o2.split("_")[1]);
                if (filaCasilla1 != filaCasilla2){
                    return filaCasilla2 - filaCasilla1;
                }else if (colCasilla2 != colCasilla1){
                    return colCasilla1 - colCasilla2;
                }else{
                    return 0;
                }
            }
        });

        for (int i = 1; i<= numeroColumnas; i++){
            for (int j = 1; j<= numeroFilas; j++){
                Casilla casilla = new Casilla();
                casilla.setFila(i);
                casilla.setColumna(j);
                casilla.setBlanca(((i+j) % 2) != 0);
                casilla.setActiva(true);
                casilla.setNotacionAlgebraica();
                String notacionString = i + "_" + j;
                casillas.put(notacionString, casilla);
            }
        }
        posicionInicial();
    }

    // Obtener las casillas ordenadas de fila mayor a fila menor, y de columna mayor a columna menor.
    public TreeMap<String, Casilla> getCasillasNa1() {
        TreeMap<String, Casilla> casillas = new TreeMap<>(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                int filaCasilla1 = Integer.parseInt(o1.split("_")[0]);
                int colCasilla1 = Integer.parseInt(o1.split("_")[1]);
                int filaCasilla2 = Integer.parseInt(o2.split("_")[0]);
                int colCasilla2 = Integer.parseInt(o2.split("_")[1]);
                if (filaCasilla1 != filaCasilla2){
                    return filaCasilla2 - filaCasilla1;
                }else if (colCasilla2 != colCasilla1){
                    return colCasilla1 - colCasilla2;
                }else{
                    return 0;
                }
            }
        });
        for (Map.Entry<String, Casilla> casilla : this.getCasillas().entrySet()){
            casillas.put(casilla.getKey(), casilla.getValue());
        }
        return casillas;
    }


    public Casilla obtenerCasilla (int fila, int columna){
        return casillas.get(fila + "_" + columna);
    }

    /*
    * tipoAvance = 0 -> Avance por fila
    * tipoAvance = 1 -> Avance por columna
    * tipoAvance = 2 -> Avance por diagonal
    */
    public ArrayList<String> casillasEntreDosCasillas(String casillaOrigen, String casillaDestino, int tipoAvance){
        ArrayList<String> casillasIntermedias = new ArrayList<>();
        boolean finAvance = casillaOrigen.equals(casillaDestino);
        int filaInicio = this.obtenerCasillaNotacionAlgebraica(casillaOrigen).getFila();
        int columnaInicio = this.obtenerCasillaNotacionAlgebraica(casillaOrigen).getColumna();
        int filaFin = this.obtenerCasillaNotacionAlgebraica(casillaDestino).getFila();
        int columnaFin = this.obtenerCasillaNotacionAlgebraica(casillaDestino).getColumna();
        int filaIterador = filaInicio;
        int columnaIterador = columnaInicio;
        while(!finAvance){
            switch (tipoAvance) {
                case 0 -> filaIterador = (filaInicio > filaFin) ? filaIterador - 1 : filaIterador + 1;
                case 1 -> columnaIterador = (columnaInicio > columnaFin) ? columnaIterador - 1 : columnaIterador + 1;
                case 2 -> {
                    filaIterador = (filaInicio > filaFin) ? filaIterador - 1 : filaIterador + 1;
                    columnaIterador = (columnaInicio > columnaFin) ? columnaIterador - 1 : columnaIterador + 1;
                }
            }
            Casilla casillaIntermedia = this.obtenerCasilla(filaIterador, columnaIterador);
            if (casillaIntermedia == null || casillaIntermedia.getNotacionAlgebraica().equals(casillaDestino)){
                finAvance = true;
            }else{
                casillasIntermedias.add(casillaIntermedia.getNotacionAlgebraica());
            }
        }
        return casillasIntermedias;
    }

    public Pieza obtenerPrimeraPiezaHastaCasilla(String casillaOrigen, int desplazamientoVertical, int desplazamientoHorizontal){
        // desplazamientoVertical = -1: Abajo. 1: arriba. 0: Nada. desplazamientoHorizontal = -1: Izq. 1:Der. 0:Nada
        Pieza pieza = null;
        boolean finAvance = false;
        int filaInicio = this.obtenerCasillaNotacionAlgebraica(casillaOrigen).getFila();
        int columnaInicio = this.obtenerCasillaNotacionAlgebraica(casillaOrigen).getColumna();
        int filaIterador = desplazamientoVertical;
        int columnaIterador = desplazamientoHorizontal;
        while(!finAvance){
            filaInicio = filaInicio + filaIterador;
            columnaInicio = columnaInicio + columnaIterador;
            Casilla casillaIntermedia = this.obtenerCasilla(filaInicio, columnaInicio);
            if (casillaIntermedia == null){
                finAvance = true;
            }else{
                pieza = this.getPiezaCasilla(casillaIntermedia.getNotacionAlgebraica());
                if (pieza != null){
                    finAvance = true;
                }
            }
        }
        return pieza;
    }

    public Pieza obtenerRey(boolean blanca){
        for (Map.Entry<String, Pieza> pieza: piezas.entrySet()){
            Pieza ficha = pieza.getValue();
            if (ficha instanceof Rey && ficha.isBlanca() == blanca){
                return ficha;
            }
        }
        return null;
    }

    public Casilla obtenerCasillaNotacionAlgebraica (String string){
        int nColumna = "abcdefghijklmnopqrstuvwxyz".indexOf(string.substring(0,1)) + 1;
        return casillas.get(Integer.parseInt(string.substring(1)) + "_" + nColumna);
    }

    public TreeMap<String, Casilla> getCasillas() {
        return casillas;
    }

    public void setCasillas(TreeMap<String, Casilla> casillas) {
        this.casillas = casillas;
    }

    public String toString(){
        String result = "";
        String letrasAbecedario = "abcdefghijklmnopqrstuvwxyz";
        for (int i = numeroFilas; i>0; i--){
            result += "" + i + "\t|\t";
            for (int j = 1; j<= numeroColumnas; j++){
                result += obtenerCasilla(i,j).infoCasilla(this, false) + "\t|\t";
            }
            result += "\n";
            result += "\t-----------------------------------------------------------------\n";
        }
        result += "\t|\t";

        for (int j = 1; j<= numeroColumnas; j++){
            result += letrasAbecedario.charAt(j-1) + "\t|\t";
        }

        return result;
    }

    private void posicionInicial(){
        colocarPiezas(true);
        colocarPiezas(false);

        for (String piezaCasilla: piezas.keySet()){
            piezas.get(piezaCasilla).actualizarCasillasControladas(this);
            if (piezas.get(piezaCasilla).isBlanca()){
                casillasControladasBlancas.addAll(piezas.get(piezaCasilla).getCasillasControladas());
            }else{
                casillasControladasNegras.addAll(piezas.get(piezaCasilla).getCasillasControladas());
            }
        }
        for (String piezaCasilla: piezas.keySet()){
            if (piezas.get(piezaCasilla).isBlanca()){
                piezas.get(piezaCasilla).actualizarCasillasDisponibles(this);
            }
        }
    }

    public int getNumeroFilas() {
        return numeroFilas;
    }

    public void setNumeroFilas(int numeroFilas) {
        this.numeroFilas = numeroFilas;
    }

    public int getNumeroColumnas() {
        return numeroColumnas;
    }
    public void setNumeroColumnas(int numeroColumnas) {
        this.numeroColumnas = numeroColumnas;
    }

    public boolean isJaque() {
        return jaque;
    }

    public void setJaque(boolean jaque) {
        this.jaque = jaque;
    }

    public void comprobarJaque() {
        this.jaque = this.piezasDandoJaque.size() > 0;
    }

    public TreeMap<String, Pieza> getPiezas() {
        return piezas;
    }

    public void setPiezas(TreeMap<String, Pieza> piezas) {
        this.piezas = piezas;
    }

    public TreeSet<String> getCasillasControladasBlancas() {
        return casillasControladasBlancas;
    }

    public void setCasillasControladasBlancas(TreeSet<String> casillasControladasBlancas) {
        this.casillasControladasBlancas = casillasControladasBlancas;
    }

    public TreeSet<String> getCasillasControladasNegras() {
        return casillasControladasNegras;
    }

    public void setCasillasControladasNegras(TreeSet<String> casillasControladasNegras) {
        this.casillasControladasNegras = casillasControladasNegras;
    }

    public ArrayList<Pieza> getPiezasDandoJaque() {
        return piezasDandoJaque;
    }

    public void setPiezasDandoJaque(ArrayList<Pieza> piezasDandoJaque) {
        this.piezasDandoJaque = piezasDandoJaque;
    }

    public boolean isComerPaso() {
        return comerPaso;
    }

    public void setComerPaso(boolean comerPaso) {
        this.comerPaso = comerPaso;
    }

    public String getUltimoMovimientoPeonDosCasillas() {
        return ultimoMovimientoPeonDosCasillas;
    }

    public void setUltimoMovimientoPeonDosCasillas(String ultimoMovimientoPeonDosCasillas) {
        this.ultimoMovimientoPeonDosCasillas = ultimoMovimientoPeonDosCasillas;
    }

    public boolean[] getEnroquesBlancas() {
        return enroquesBlancas;
    }

    public void setEnroquesBlancas(boolean[] enroquesBlancas) {
        this.enroquesBlancas = enroquesBlancas;
    }

    public boolean[] getEnroquesNegras() {
        return enroquesNegras;
    }

    public void setEnroquesNegras(boolean[] enroquesNegras) {
        this.enroquesNegras = enroquesNegras;
    }

    public Pieza getPiezaCasilla(String casilla){
        return this.getPiezas().get(casilla);
    }

    private void colocarPiezas (boolean blancas){
        int primeraFila = blancas ? 1 : numeroFilas;
        for (int i = 1; i<=8; i++){
            Pieza peon = new Peon(blancas);
            Casilla casillaPeon = blancas ? obtenerCasilla(primeraFila + 1,i) : obtenerCasilla(primeraFila - 1,i);
            peon.setCasilla(casillaPeon.toStringNotacionAlgebraica());
            piezas.put(casillaPeon.toStringNotacionAlgebraica(), peon);
        }

        Pieza torreA = new Torre(blancas);
        Pieza torreH = new Torre(blancas);
        Pieza caballoB = new Caballo(blancas);
        Pieza caballoG = new Caballo(blancas);
        Pieza alfilC = new Alfil(blancas);
        Pieza alfilF = new Alfil(blancas);
        Pieza dama = new Dama(blancas);
        Pieza rey = new Rey(blancas);

        torreA.setCasilla("a" + primeraFila);
        caballoB.setCasilla("b" + primeraFila);
        alfilC.setCasilla("c" + primeraFila);
        dama.setCasilla("d" + primeraFila);
        rey.setCasilla("e" + primeraFila);
        alfilF.setCasilla("f" + primeraFila);
        caballoG.setCasilla("g" + primeraFila);
        torreH.setCasilla("h" + primeraFila);
        piezas.put(torreA.getCasilla(), torreA);
        piezas.put(torreH.getCasilla(), torreH);
        piezas.put(caballoB.getCasilla(), caballoB);
        piezas.put(caballoG.getCasilla(), caballoG);
        piezas.put(alfilC.getCasilla(), alfilC);
        piezas.put(alfilF.getCasilla(), alfilF);
        piezas.put(dama.getCasilla(), dama);
        piezas.put(rey.getCasilla(), rey);
        
    }

    public boolean peonesAdyacentesContrariosComerPaso (boolean turnoBlancas){
        if (this.isComerPaso()){
            Casilla casillaPeonPosibleCapturado = this.obtenerCasillaNotacionAlgebraica(this.getUltimoMovimientoPeonDosCasillas());
            int desplazamiento = turnoBlancas ? -1 : 1;
            String casillaTraseraPeonPosibleCapturado = this.obtenerCasilla(casillaPeonPosibleCapturado.getFila() + desplazamiento, casillaPeonPosibleCapturado.getColumna()).toStringNotacionAlgebraica();
            for (String piezaCasilla: piezas.keySet()){
                Pieza pieza = piezas.get(piezaCasilla);
                if ((pieza.isBlanca() != turnoBlancas) && (pieza.getCasillasDisponibles().contains(casillaTraseraPeonPosibleCapturado))){
                    return true;
                }
            }
            return false;
        }else{
            return false;
        }
    }


    public boolean checkEnroque(boolean isBlancas, boolean isLargo) {
        Pieza elRey = this.obtenerRey(isBlancas);
        if (elRey.isMovida()){
            return false;
        }
        int filaTorre = isBlancas ? 1 : this.numeroFilas;
        int columnaTorre = isLargo ? 1 : this.numeroColumnas;
        Pieza piezaEsquina = this.getPiezaCasilla(this.obtenerCasilla(filaTorre, columnaTorre).toStringNotacionAlgebraica());
        return piezaEsquina instanceof Torre && !piezaEsquina.isMovida();
    }
}
