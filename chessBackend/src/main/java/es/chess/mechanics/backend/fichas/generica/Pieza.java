package es.chess.mechanics.backend.fichas.generica;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import es.chess.mechanics.backend.entorno.Casilla;
import es.chess.mechanics.backend.entorno.Tablero;
import es.chess.mechanics.backend.fichas.especificas.*;

import java.util.*;

@JsonTypeInfo( use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "tipoPieza")
@JsonSubTypes({
        @JsonSubTypes.Type(value = Peon.class, name = "peon"),
        @JsonSubTypes.Type(value = Dama.class, name = "dama"),
        @JsonSubTypes.Type(value = Caballo.class, name = "caballo"),
        @JsonSubTypes.Type(value = Alfil.class, name = "alfil"),
        @JsonSubTypes.Type(value = Torre.class, name = "torre"),
        @JsonSubTypes.Type(value = Rey.class, name = "rey")
})
public abstract class Pieza {

    protected String letra;
    protected boolean movida;
    protected boolean blanca;
    protected String casilla;
    protected HashSet<String> casillasDisponibles;
    protected HashSet<String> casillasControladas;
    protected String nombreImagen;
    protected String tipoPieza;

    protected boolean isClavadaAbsoluta;

    protected int tipoClavadaAbsoluta;

    protected String casillaPiezaQueClava;

    public Pieza(boolean blanca){
        this.blanca = blanca;
        this.casilla = null;
        this.casillasDisponibles = new HashSet<>();
        this.casillasControladas = new HashSet<>();
        this.tipoClavadaAbsoluta = -1;
        this.casillaPiezaQueClava = "";
    }

    public Pieza(){

    }

    public boolean isBlanca() {
        return blanca;
    }

    public void setBlanca(boolean blanca) {
        this.blanca = blanca;
    }

    public String getCasilla() {
        return casilla;
    }

    public void setCasilla(String string) {
        this.casilla = string;
    }

    public boolean isMovida() {
        return movida;
    }

    public void setMovida(boolean movida) {
        this.movida = movida;
    }

    public String getNombreImagen() {
        return nombreImagen;
    }

    public void setNombreImagen(String nombreImagen) {
        this.nombreImagen = nombreImagen;
    }

    public HashSet<String> getCasillasDisponibles() {
        return casillasDisponibles;
    }

    public void setCasillasDisponibles(HashSet<String> casillasDisponibles) {
        this.casillasDisponibles = casillasDisponibles;
    }

    public HashSet<String> getCasillasControladas() {
        return casillasControladas;
    }

    public void setCasillasControladas(HashSet<String> casillasControladas) {
        this.casillasControladas = casillasControladas;
    }

    public String getLetra() {
        return letra;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public String getTipoPieza() {
        return tipoPieza;
    }

    public void setTipoPieza(String tipoPieza) {
        this.tipoPieza = tipoPieza;
    }

    public boolean isClavadaAbsoluta() {
        return isClavadaAbsoluta;
    }

    public void setClavadaAbsoluta(boolean clavadaAbsoluta) {
        isClavadaAbsoluta = clavadaAbsoluta;
    }

    public int getTipoClavadaAbsoluta() {
        return tipoClavadaAbsoluta;
    }

    public void setTipoClavadaAbsoluta(int tipoClavadaAbsoluta) {
        this.tipoClavadaAbsoluta = tipoClavadaAbsoluta;
    }

    public String getCasillaPiezaQueClava() {
        return casillaPiezaQueClava;
    }

    public void setCasillaPiezaQueClava(String casillaPiezaQueClava) {
        this.casillaPiezaQueClava = casillaPiezaQueClava;
    }

    public abstract String toStringIngles();

    public void reiniciarCasillasDisponibles(){
        this.casillasDisponibles = new HashSet<>();
    }

    public void actualizarCasillasDisponibles(Tablero tablero){

    } // SE REDEFINE EN LOS HIJOS

    public void actualizarCasillasControladas(Tablero tablero){

    } // SE REDEFINE EN LOS HIJOS

    public String toStringNotacionAlgebraica(){
        return "";
    }

    public String informacionMovimientosPieza(Tablero tablero){
        return "";
    }

    private void checkCasilaCaballo(Tablero tablero, int fila, int columna){
        Casilla casillaControlada = tablero.obtenerCasilla(fila,columna);
        if (casillaControlada!=null){
            this.casillasControladas.add(casillaControlada.toStringNotacionAlgebraica());
            if (tablero.getPiezaCasilla(casillaControlada.toStringNotacionAlgebraica()) != null && tablero.getPiezaCasilla(casillaControlada.toStringNotacionAlgebraica()).isBlanca() != this.isBlanca() && tablero.getPiezaCasilla(casillaControlada.toStringNotacionAlgebraica()) instanceof Rey){
                tablero.getPiezasDandoJaque().add(this);
            }
        }
    }

    protected HashSet<String> movimientosDisponiblesPeon(Tablero tablero){
        this.isClavadaAbsoluta(tablero);
        if (this.isBlanca()){
            if (!this.movida){
                Casilla candidata = tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna());
                Casilla laCasillaAnterior = tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna());
                if (!candidata.isOcupada(tablero) && !laCasillaAnterior.isOcupada(tablero)){
                    comprobarCasillaMovimiento(tablero, candidata.getFila(), candidata.getColumna());
                }
            }
            comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna());
            comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1);
            comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1);
            // COMER AL PASO
            if (!"".equals(tablero.getUltimoMovimientoPeonDosCasillas()) && tablero.obtenerCasillaNotacionAlgebraica(this.casilla).getFila() == (tablero.getNumeroFilas() - 3)){
                Pieza peonPosibleCapturado = tablero.getPiezaCasilla(tablero.getUltimoMovimientoPeonDosCasillas());
                Casilla casillaPeonRival = tablero.obtenerCasillaNotacionAlgebraica(peonPosibleCapturado.casilla);
                System.out.println(this.getCasilla() + "-" + peonPosibleCapturado.getCasilla());
                if (peonPosibleCapturado.isBlanca() != this.isBlanca() &&
                (Math.abs(casillaPeonRival.getColumna() - tablero.obtenerCasillaNotacionAlgebraica(this.casilla).getColumna()) == 1) &&
                casillaPeonRival.enLaMismaFila(tablero.obtenerCasillaNotacionAlgebraica(this.casilla))){
                    comprobarCasillaMovimiento(tablero, casillaPeonRival.getFila()+1,casillaPeonRival.getColumna());
                }
            }
        }else{
            if (!this.movida){
                Casilla candidata = tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna());
                Casilla laCasillaAnterior = tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna());
                if (!candidata.isOcupada(tablero) && !laCasillaAnterior.isOcupada(tablero)){
                    comprobarCasillaMovimiento(tablero, candidata.getFila(), candidata.getColumna());
                }
            }
            comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna());
            comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1);
            comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1);
            // COMER AL PASO
            if (!"".equals(tablero.getUltimoMovimientoPeonDosCasillas()) && tablero.obtenerCasillaNotacionAlgebraica(this.casilla).getFila() == (4)){
                Pieza peonPosibleCapturado = tablero.getPiezaCasilla(tablero.getUltimoMovimientoPeonDosCasillas());
                Casilla casillaPeonRival = tablero.obtenerCasillaNotacionAlgebraica(peonPosibleCapturado.casilla);
                System.out.println(this.getCasilla() + "-" + peonPosibleCapturado.getCasilla());
                if (peonPosibleCapturado.isBlanca() != this.isBlanca() &&
                (Math.abs(casillaPeonRival.getColumna() - tablero.obtenerCasillaNotacionAlgebraica(this.casilla).getColumna()) == 1) &&
                casillaPeonRival.enLaMismaFila(tablero.obtenerCasillaNotacionAlgebraica(this.casilla))){
                    comprobarCasillaMovimiento(tablero, casillaPeonRival.getFila()-1,casillaPeonRival.getColumna());
                }
            }
        }
        return casillasDisponibles;
    }

    protected HashSet<String> movimientosDisponiblesCaballo(Tablero tablero){
        this.isClavadaAbsoluta(tablero);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+2);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-2);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+2);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-2);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1);
        return this.casillasDisponibles;
    }

    protected HashSet<String> movimientosDisponiblesAlfil(Tablero tablero){
        this.isClavadaAbsoluta(tablero);
        int offsetFila = 1;
        int offsetCol = 1;
        boolean ocupada = false;
        while (!ocupada){
            ocupada = comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() - offsetFila,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna() - offsetCol);

            offsetFila++;
            offsetCol++;
        }
        offsetFila = 1;
        offsetCol = 1;
        ocupada = false;
        while (!ocupada){
            ocupada = comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() - offsetFila,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna() + offsetCol);
            offsetFila++;
            offsetCol++;
        }
        offsetFila = 1;
        offsetCol = 1;
        ocupada = false;
        while (!ocupada){
            ocupada = comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() + offsetFila,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna() - offsetCol);
            offsetFila++;
            offsetCol++;
        }
        offsetFila = 1;
        offsetCol = 1;
        ocupada = false;
        while (!ocupada){
            ocupada = comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() + offsetFila,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna() + offsetCol);
            offsetFila++;
            offsetCol++;
        }
        return casillasDisponibles;
    }

    protected HashSet<String> movimientosDisponiblesTorre(Tablero tablero){
        this.isClavadaAbsoluta(tablero);
        int offsetFila = 1;
        int offsetCol = 1;
        boolean ocupada = false;
        while (!ocupada){
            ocupada = comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() - offsetFila,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna());
            offsetFila++;
        }
        ocupada = false;
        while (!ocupada){
            ocupada = comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila(),tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-offsetCol);
            offsetCol++;
        }
        offsetFila = 1;
        ocupada = false;
        while (!ocupada){
            ocupada = comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() + offsetFila,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna());
            offsetFila++;
        }
        offsetCol = 1;
        ocupada = false;
        while (!ocupada){
            ocupada = comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila(),tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna() + offsetCol);
            offsetCol++;
        }
        return casillasDisponibles;
    }

    protected HashSet<String> movimientosDisponiblesRey(Tablero tablero){
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna());
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila(),tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila(),tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna());
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1);
        // FALTA QUITAR CASILLAS CONTROLADAS POR EL RIVAL
        boolean[] enroques = this.isBlanca() ? tablero.getEnroquesBlancas() : tablero.getEnroquesNegras();
        if (enroques[0]){
            comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila(),tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+2);
        }
        if (enroques[1]){
            comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila(),tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-2);
        }
        HashSet<String> casillasRivalesControladas = new HashSet<>();
        for (Map.Entry<String, Pieza> pieza : tablero.getPiezas().entrySet()){
            if (this.isBlanca() != pieza.getValue().isBlanca()){
                casillasRivalesControladas.addAll(pieza.getValue().casillasControladas);
            }
        }
        for (String casilla : casillasRivalesControladas){
            if (casillasDisponibles.contains(casilla)){
                casillasDisponibles.remove(casilla);
            }
        }
        return casillasDisponibles;
    }

    protected HashSet<String> casillasControladasPeon(Tablero tablero){
        int filaDelantePeon = this.isBlanca() ? tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1 : tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1;
        if (tablero.obtenerCasilla(filaDelantePeon,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1)!=null){
            String casillaControlada = tablero.obtenerCasilla(filaDelantePeon,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1).toStringNotacionAlgebraica();
            this.casillasControladas.add(casillaControlada);
            if (tablero.getPiezaCasilla(casillaControlada) != null && tablero.getPiezaCasilla(casillaControlada).isBlanca() != this.isBlanca() && tablero.getPiezaCasilla(casillaControlada) instanceof Rey){
                tablero.getPiezasDandoJaque().add(this);
            }
        }
        if (tablero.obtenerCasilla(filaDelantePeon,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1)!=null){
            String casillaControlada = tablero.obtenerCasilla(filaDelantePeon,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1).toStringNotacionAlgebraica();
            this.casillasControladas.add(casillaControlada);
            if (tablero.getPiezaCasilla(casillaControlada) != null && tablero.getPiezaCasilla(casillaControlada).isBlanca() != this.isBlanca() && tablero.getPiezaCasilla(casillaControlada) instanceof Rey){
                tablero.getPiezasDandoJaque().add(this);
            }
        }
        tablero.comprobarJaque();
        return casillasControladas;
    }

    protected HashSet<String> casillasControladasCaballo(Tablero tablero){
        checkCasilaCaballo(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+2);
        checkCasilaCaballo(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-2);
        checkCasilaCaballo(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+2, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1);
        checkCasilaCaballo(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+2, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1);
        checkCasilaCaballo(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+2);
        checkCasilaCaballo(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-2);
        checkCasilaCaballo(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-2, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1);
        checkCasilaCaballo(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-2, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1);
        tablero.comprobarJaque();
        return this.casillasControladas;
    }

    protected HashSet<String> casillasControladasAlfil(Tablero tablero){
        int offsetFila = 1;
        int offsetCol = 1;
        boolean existe = true;
        Casilla casillaCandidata;
        while (existe){
            casillaCandidata = tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() - offsetFila,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna() - offsetCol);
            if (casillaCandidata != null){
                this.casillasControladas.add(casillaCandidata.toStringNotacionAlgebraica());
                if (casillaCandidata.isOcupada(tablero)){
                    existe = false;
                    if (tablero.getPiezaCasilla(casillaCandidata.toStringNotacionAlgebraica()).isBlanca() != this.isBlanca() && tablero.getPiezaCasilla(casillaCandidata.toStringNotacionAlgebraica()) instanceof Rey){
                        tablero.getPiezasDandoJaque().add(this);
                    }
                }
            }else{
                existe = false;
            }
            offsetFila++;
            offsetCol++;
        }
        offsetFila = 1;
        offsetCol = 1;
        existe = true;
        while (existe){
            casillaCandidata = tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() - offsetFila,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna() + offsetCol);
            if (casillaCandidata != null){
                this.casillasControladas.add(casillaCandidata.toStringNotacionAlgebraica());
                if (casillaCandidata.isOcupada(tablero)){
                    existe = false;
                    if (tablero.getPiezaCasilla(casillaCandidata.toStringNotacionAlgebraica()).isBlanca() != this.isBlanca() && tablero.getPiezaCasilla(casillaCandidata.toStringNotacionAlgebraica()) instanceof Rey){
                        tablero.getPiezasDandoJaque().add(this);
                    }
                }
            }else{
                existe = false;
            }
            offsetFila++;
            offsetCol++;
        }
        offsetFila = 1;
        offsetCol = 1;
        existe = true;
        while (existe){
            casillaCandidata = tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() + offsetFila,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna() - offsetCol);
            if (casillaCandidata != null){
                this.casillasControladas.add(casillaCandidata.toStringNotacionAlgebraica());
                if (casillaCandidata.isOcupada(tablero)){
                    existe = false;
                    if (tablero.getPiezaCasilla(casillaCandidata.toStringNotacionAlgebraica()).isBlanca() != this.isBlanca() && tablero.getPiezaCasilla(casillaCandidata.toStringNotacionAlgebraica()) instanceof Rey){
                        tablero.getPiezasDandoJaque().add(this);
                    }
                }
            }else{
                existe = false;
            }
            offsetFila++;
            offsetCol++;
        }
        offsetFila = 1;
        offsetCol = 1;
        existe = true;
        while (existe){
            casillaCandidata = tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() + offsetFila,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna() + offsetCol);
            if (casillaCandidata != null){
                this.casillasControladas.add(casillaCandidata.toStringNotacionAlgebraica());
                if (casillaCandidata.isOcupada(tablero)){
                    existe = false;
                    if (tablero.getPiezaCasilla(casillaCandidata.toStringNotacionAlgebraica()).isBlanca() != this.isBlanca() && tablero.getPiezaCasilla(casillaCandidata.toStringNotacionAlgebraica()) instanceof Rey){
                        tablero.getPiezasDandoJaque().add(this);
                    }
                }
            }else{
                existe = false;
            }
            offsetFila++;
            offsetCol++;
        }
        tablero.comprobarJaque();
        return casillasControladas;
    }

    protected HashSet<String> casillasControladasTorre(Tablero tablero){
        int offsetFila = 1;
        int offsetCol = 1;
        boolean existe = true;
        Casilla casillaCandidata;
        while (existe){
            casillaCandidata = tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() - offsetFila,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna());
            if (casillaCandidata != null){
                this.casillasControladas.add(casillaCandidata.toStringNotacionAlgebraica());
                if (casillaCandidata.isOcupada(tablero)){
                    existe = false;
                    if (tablero.getPiezaCasilla(casillaCandidata.toStringNotacionAlgebraica()).isBlanca() != this.isBlanca() && tablero.getPiezaCasilla(casillaCandidata.toStringNotacionAlgebraica()) instanceof Rey){
                        tablero.getPiezasDandoJaque().add(this);
                    }
                }
            }else{
                existe = false;
            }
            offsetFila++;
        }
        existe = true;
        while (existe){
            casillaCandidata = tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila(),tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna() - offsetCol);
            if (casillaCandidata != null){
                this.casillasControladas.add(casillaCandidata.toStringNotacionAlgebraica());
                if (casillaCandidata.isOcupada(tablero)){
                    existe = false;
                    if (tablero.getPiezaCasilla(casillaCandidata.toStringNotacionAlgebraica()).isBlanca() != this.isBlanca() && tablero.getPiezaCasilla(casillaCandidata.toStringNotacionAlgebraica()) instanceof Rey){
                        tablero.getPiezasDandoJaque().add(this);
                    }
                }
            }else{
                existe = false;
            }
            offsetCol++;
        }
        offsetFila = 1;
        existe = true;
        while (existe){
            casillaCandidata = tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() + offsetFila,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna());
            if (casillaCandidata != null){
                this.casillasControladas.add(casillaCandidata.toStringNotacionAlgebraica());
                if (casillaCandidata.isOcupada(tablero)){
                    existe = false;
                    if (tablero.getPiezaCasilla(casillaCandidata.toStringNotacionAlgebraica()).isBlanca() != this.isBlanca() && tablero.getPiezaCasilla(casillaCandidata.toStringNotacionAlgebraica()) instanceof Rey){
                        tablero.getPiezasDandoJaque().add(this);
                    }
                }
            }else{
                existe = false;
            }
            offsetFila++;
        }
        offsetCol = 1;
        existe = true;
        while (existe){
            casillaCandidata = tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila(),tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna() + offsetCol);
            if (casillaCandidata != null){
                this.casillasControladas.add(casillaCandidata.toStringNotacionAlgebraica());
                if (casillaCandidata.isOcupada(tablero)){
                    existe = false;
                    if (tablero.getPiezaCasilla(casillaCandidata.toStringNotacionAlgebraica()).isBlanca() != this.isBlanca() && tablero.getPiezaCasilla(casillaCandidata.toStringNotacionAlgebraica()) instanceof Rey){
                        tablero.getPiezasDandoJaque().add(this);
                    }
                }
            }else{
                existe = false;
            }
            offsetCol++;
        }
        return casillasControladas;
    }

    protected HashSet<String> casillasControladasRey(Tablero tablero){
        if(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1)!= null){
            this.casillasControladas.add(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1).toStringNotacionAlgebraica());
        }
        if(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna())!= null){
            this.casillasControladas.add(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()).toStringNotacionAlgebraica());
        }
        if(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1)!= null){
            this.casillasControladas.add(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1).toStringNotacionAlgebraica());
        }
        if(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila(),tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1)!= null){
            this.casillasControladas.add(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila(),tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1).toStringNotacionAlgebraica());
        }
        if(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila(),tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1)!= null){
            this.casillasControladas.add(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila(),tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1).toStringNotacionAlgebraica());
        }
        if(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1)!= null){
            this.casillasControladas.add(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1).toStringNotacionAlgebraica());
        }
        if(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna())!= null){
            this.casillasControladas.add(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()).toStringNotacionAlgebraica());
        }
        if(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1)!= null){
            this.casillasControladas.add(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1).toStringNotacionAlgebraica());
        }
        return casillasControladas;
    }

    private boolean comprobarCasillaMovimiento(Tablero tablero, int fila, int columna) {
        Casilla casillaCandidata = tablero.obtenerCasilla(fila, columna);
        Casilla casillaActual = tablero.obtenerCasillaNotacionAlgebraica(this.casilla);
        boolean jaqueDoble = tablero.isJaque() && tablero.getPiezasDandoJaque().size() > 1;

        if (casillaCandidata != null) {
            if (this instanceof Rey) { // Si es el rey añadimos la casilla, posteriormente se criban y eliminan las casillas controladas por el rival
                if (Math.abs(casillaCandidata.getColumna() - casillaActual.getColumna()) == 2){
                    // COMPROBADOR DE ENROQUE, YA COMPROBADO PREVIAMENTE QUE NI LA TORRE NI EL REY SE HAN MOVIDO
                    // TOCA COMPROBAR SI EL REY NO ESTÁ EN JAQUE, SI TODAS LAS CASILLAS ENTRE REY Y TORRE ESTÁN LIBRES Y NO ESTÁN CONTROLADAS POR RIVALES
                    if (!tablero.isJaque()){
                        int columnaEnroque;
                        if (columna > casillaActual.getColumna()){
                            columnaEnroque = tablero.getNumeroColumnas();
                        }else{
                            columnaEnroque = 1;
                        }
                        ArrayList<String> casillasIntermedias = tablero.casillasEntreDosCasillas(casillaActual.toStringNotacionAlgebraica(), tablero.obtenerCasilla(casillaActual.getFila(), columnaEnroque).toStringNotacionAlgebraica(), 1);
                        TreeSet<String> casillasControladasRival = this.isBlanca() ? tablero.getCasillasControladasNegras() : tablero.getCasillasControladasBlancas() ;
                        boolean enroqueDisponible = true;
                        for (int i = 0; i<casillasIntermedias.size() && enroqueDisponible; i++){
                            String casillaIntermedia = casillasIntermedias.get(i);
                            System.out.println(casillaIntermedia);
                            enroqueDisponible = (tablero.getPiezaCasilla(casillaIntermedia) == null) && (!casillasControladasRival.contains(casillaIntermedia));
                            System.out.println(enroqueDisponible);
                        }
                        if (enroqueDisponible){
                            this.casillasDisponibles.add(casillaCandidata.toStringNotacionAlgebraica());
                        }
                    }
                }else{
                    if (!casillaCandidata.isOcupada(tablero) || casillaCandidata.isOcupadaRival(tablero, this.blanca)) {
                        // Quitamos que en los jaques el rey se pueda mover por la misma diagonal / fila / columna que la pieza que le da jaque
                        boolean anadirCasilla = true;
                        for (Pieza pieza: tablero.getPiezasDandoJaque()){
                            anadirCasilla = anadirCasilla && !((pieza instanceof Alfil && casillaCandidata.enLaMismaDiagonal(tablero.obtenerCasillaNotacionAlgebraica(pieza.getCasilla()))) ||
                                    (pieza instanceof Torre && (casillaCandidata.enLaMismaFila(tablero.obtenerCasillaNotacionAlgebraica(pieza.getCasilla())) || casillaCandidata.enLaMismaColumna(tablero.obtenerCasillaNotacionAlgebraica(pieza.getCasilla())))) ||
                                    (pieza instanceof Dama && (casillaCandidata.enLaMismaFila(tablero.obtenerCasillaNotacionAlgebraica(pieza.getCasilla())) || casillaCandidata.enLaMismaColumna(tablero.obtenerCasillaNotacionAlgebraica(pieza.getCasilla())) || casillaCandidata.enLaMismaDiagonal(tablero.obtenerCasillaNotacionAlgebraica(pieza.getCasilla())))));
                        }
                        if (anadirCasilla){
                            this.casillasDisponibles.add(casillaCandidata.toStringNotacionAlgebraica());
                        }
                    }
                }
            } else {
                boolean puedeMoverClavada = true;
                if (this.isClavadaAbsoluta){
                    Pieza piezaQueClava = tablero.getPiezaCasilla(this.casillaPiezaQueClava);
                    puedeMoverClavada = ((piezaQueClava instanceof Alfil || piezaQueClava instanceof Dama) &&
                            casillaActual.enLaMismaDiagonal(casillaCandidata) &&
                            casillaCandidata.enLaMismaDiagonal(tablero.obtenerCasillaNotacionAlgebraica(this.casillaPiezaQueClava)) &&
                            casillaActual.enLaMismaDiagonal(tablero.obtenerCasillaNotacionAlgebraica(this.casillaPiezaQueClava))) ||
                            ((piezaQueClava instanceof Dama || piezaQueClava instanceof Torre) &&
                            (casillaCandidata.enLaMismaFila(casillaActual) &&
                             casillaCandidata.enLaMismaFila(tablero.obtenerCasillaNotacionAlgebraica(this.casillaPiezaQueClava))) ||
                            (casillaCandidata.enLaMismaColumna(casillaActual) &&
                             casillaCandidata.enLaMismaColumna(tablero.obtenerCasillaNotacionAlgebraica(this.casillaPiezaQueClava))));
                }
                    // En un jaque doble, únicamente se puede mover el rey, como ya lo hemos controlado arriba no es necesario mirar de nuevo
                if (tablero.isJaque() && !jaqueDoble) {
                /* En un jaque de una pieza, se pueden hacer tres cosas:
                    - Mover el rey
                    - Cubrir el jaque, siempre que lo de una pieza de largo alcance
                    - Capturar la pieza que da jaque
                 */
                    Casilla casillaReyJaque = tablero.obtenerCasillaNotacionAlgebraica(tablero.obtenerRey(this.blanca).casilla);
                    Pieza piezaQueDaJaque = tablero.getPiezasDandoJaque().get(0); // Únicamente hay una pieza dando jaque
                    Casilla casillaDesdeJaque = tablero.obtenerCasillaNotacionAlgebraica(piezaQueDaJaque.casilla);
                    if (puedeMoverClavada && casillaCandidata.equals(casillaDesdeJaque)) { // Se puede capturar la pieza que da jaque
                        this.casillasDisponibles.add(casillaCandidata.toStringNotacionAlgebraica());
                    } else { // Vemos como controlar que el rival se cubra del jaque, lo cual solo puede pasar con alfil, dama y torre
                        int tipoAvance = 0;
                        if (piezaQueDaJaque instanceof Dama) {
                            if (casillaReyJaque.enLaMismaFila(casillaDesdeJaque)) {
                                tipoAvance = 0;
                            } else if (casillaReyJaque.enLaMismaColumna(casillaDesdeJaque)) {
                                tipoAvance = 1;
                            } else {
                                tipoAvance = 2;
                            }
                        } else if (piezaQueDaJaque instanceof Torre) {
                            tipoAvance = casillaReyJaque.enLaMismaFila(casillaDesdeJaque) ? 0 : 1;
                        } else if (piezaQueDaJaque instanceof Alfil) {
                            tipoAvance = 2;
                        }
                        ArrayList<String> casillasIntermediasJaque = tablero.casillasEntreDosCasillas(casillaDesdeJaque.getNotacionAlgebraica(), casillaReyJaque.toStringNotacionAlgebraica(), tipoAvance);
                        if (casillasIntermediasJaque.contains(casillaCandidata.toStringNotacionAlgebraica())) {
                            if (puedeMoverClavada && (!(this instanceof Peon) || (casillaCandidata.enLaMismaColumna(tablero.obtenerCasillaNotacionAlgebraica(this.casilla))))) {
                                this.casillasDisponibles.add(casillaCandidata.toStringNotacionAlgebraica());
                            }
                        }
                    }
                } else if (!tablero.isJaque()) {
                    if(puedeMoverClavada) {
                        if (this instanceof Peon) {
             /* Si el peón se mueve a una columna distinta, está capturando, y solo puede hacerlo si la pieza de esa casilla es rival
                Si se mueve a su misma columna, únicamente hay que comprobar si esa casilla no está ocupada.
              */
                            if ((casillaActual.enLaMismaColumna(casillaCandidata))) {
                                if (!casillaCandidata.isOcupada(tablero)) {
                                    this.casillasDisponibles.add(casillaCandidata.toStringNotacionAlgebraica());
                                }
                            } else {
                                if (casillaCandidata.isOcupadaRival(tablero, this.isBlanca())) {
                                    this.casillasDisponibles.add(casillaCandidata.toStringNotacionAlgebraica());
                                } else if (tablero.isComerPaso()){
                                    Casilla casillaPeonComerPaso = tablero.obtenerCasillaNotacionAlgebraica(tablero.getUltimoMovimientoPeonDosCasillas());
                                    if(casillaPeonComerPaso.enLaMismaFila(tablero.obtenerCasillaNotacionAlgebraica(this.casilla)) &&
                                    casillaPeonComerPaso.enLaMismaColumna(casillaCandidata)){
                                        this.casillasDisponibles.add(casillaCandidata.toStringNotacionAlgebraica());
                                    }
                                }
                            }
                        } else {
                            if (!casillaCandidata.isOcupada(tablero) || casillaCandidata.isOcupadaRival(tablero, this.isBlanca())) {
                                this.casillasDisponibles.add(casillaCandidata.toStringNotacionAlgebraica());
                            }
                        }
                    }
                }
            }
            return casillaCandidata.isOcupada(tablero);
        }else{
            // No hay casilla, se devuelve true para parar los bucles
            return true;
        }
    }
    public void movimiento (String destino){
        this.setCasilla(destino);
        if (!this.movida){
            this.movida = true;
        }
    }

    public boolean isClavadaAbsoluta(Tablero tablero){
        /*
        REVISAR SI LA PIEZA ESTÄ CLAVADA
        Desde la casilla en la que se encuentra la pieza, y la casilla en la que se encuentra el rey del mismo bando, comprobar
        - Está directamente en la misma fila / columna / diagonal (no hay piezas entre ellas)
        - Si eso pasa, en la otra dirección de dicha fila / columna / diagonal debe haber directamente una pieza enemiga de largo alcance (torre, alfil, dama)
        - Con ambas, debe devolver true
         */
        if (this instanceof Rey){
            return false;
        }else{
            Casilla casillaPiezaActual = tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla());
            Casilla casillaReyPieza = tablero.obtenerCasillaNotacionAlgebraica(tablero.obtenerRey(this.isBlanca()).getCasilla());
            int tipoAvance = 0;
            int desplazamientoVertical = 0;
            int desplazamientoHorizontal = 0;
            // tipoAvance = 0 -> FILA. tipoAvance = 1 -> COLUMNA. tipoAvance = 2 -> DIAGONAL
            if (casillaPiezaActual.enLaMismaColumna(casillaReyPieza)){
                tipoAvance = 1;
                desplazamientoVertical = casillaPiezaActual.getFila() > casillaReyPieza.getFila() ? 1 : -1 ;
            }else if (casillaPiezaActual.enLaMismaFila(casillaReyPieza)){
                tipoAvance = 0;
                desplazamientoHorizontal = casillaPiezaActual.getColumna() > casillaReyPieza.getColumna() ? 1 : -1 ;
            }else if (casillaPiezaActual.enLaMismaDiagonal(casillaReyPieza)){
                tipoAvance = 2;
                desplazamientoVertical = casillaPiezaActual.getFila() > casillaReyPieza.getFila() ? 1 : -1 ;
                desplazamientoHorizontal = casillaPiezaActual.getColumna() > casillaReyPieza.getColumna() ? 1 : -1 ;
            }else{
                return false;
            }
            ArrayList<String> casillasIntermedias = tablero.casillasEntreDosCasillas(casillaReyPieza.getNotacionAlgebraica(), casillaPiezaActual.getNotacionAlgebraica(), tipoAvance);
            for (String square: casillasIntermedias) {
                if (tablero.getPiezaCasilla(square) != null){
                    return false;
                }
            }
            Pieza piezaQueQuizaClava = tablero.obtenerPrimeraPiezaHastaCasilla(casillaPiezaActual.getNotacionAlgebraica(), desplazamientoVertical, desplazamientoHorizontal);
            // Si las piezas están en la misma fila o la misma columna, sólo pueden ser clavadas por torre o dama
            // Si están en la misma diagonal, solo pueden ser clavadas por alfil o dama
            this.isClavadaAbsoluta = piezaQueQuizaClava != null && piezaQueQuizaClava.isBlanca() != this.isBlanca() &&
                    (((tipoAvance == 0 || tipoAvance == 1) && (piezaQueQuizaClava instanceof Torre || piezaQueQuizaClava instanceof Dama)) ||
                            ((tipoAvance == 2) && (piezaQueQuizaClava instanceof Alfil || piezaQueQuizaClava instanceof Dama)));
            if (this.isClavadaAbsoluta) {
                this.tipoClavadaAbsoluta = tipoAvance;
                this.casillaPiezaQueClava = piezaQueQuizaClava.getCasilla();
            }else{
                this.tipoClavadaAbsoluta = -1;
                this.casillaPiezaQueClava = "";
            }
            return this.isClavadaAbsoluta;
        }
    }
}
