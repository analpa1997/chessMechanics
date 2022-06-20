package es.chess.mechanics.backend.entorno;

import es.chess.mechanics.backend.fichas.especificas.*;
import es.chess.mechanics.backend.fichas.generica.Pieza;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;

public class Tablero {

    private int numeroFilas;
    private int numeroColumnas;
    private TreeMap<String, Casilla> casillas;
    private boolean jaque;
    private ArrayList<Pieza> piezasDandoJaque;
    private TreeMap<String, Pieza> piezas;
    private TreeSet<String> casillasControladasBlancas;
    private TreeSet<String> casillasControladasNegras;

    //private Casilla[][] tableroArray;

    public Tablero(){
        numeroFilas = 8;
        numeroColumnas = 8;
        piezas = new TreeMap<>();
        piezasDandoJaque = new ArrayList<>();
        casillasControladasNegras = new TreeSet<>();
        casillasControladasBlancas = new TreeSet<>();
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

    public Casilla obtenerCasilla (int fila, int columna){
        return casillas.get(fila + "_" + columna);
    }

    public Casilla obtenerCasillaNotacionAlgebraica (String columna, int fila){
        int nColumna = "abcdefghijklmnopqrstuvwxyz".indexOf(columna) + 1;
        return casillas.get(fila + "_" + nColumna);
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

    /*public void informacionPiezas(){
        for (int i = 1; i<=numeroFilas; i++){
            for (int j = 1; j<=numeroColumnas; j++){
                Casilla casilla = obtenerCasilla(i, j);
                if (casilla != null){
                    if (piezasBlancas.containsKey(casilla.toStringNotacionAlgebraica())){
                        Pieza pieza = piezasBlancas.get(casilla.toStringNotacionAlgebraica());
                        System.out.println(pieza.informacionMovimientosPieza(this));
                    }else if (piezasNegras.containsKey(casilla.toStringNotacionAlgebraica())){
                        Pieza pieza = piezasNegras.get(casilla.toStringNotacionAlgebraica());
                        System.out.println(pieza.informacionMovimientosPieza(this));
                    }
                }
            }
        }
    }*/

}
