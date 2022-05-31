package es.chess.mechanics.backend.fichas.generica;

import es.chess.mechanics.backend.entorno.Casilla;
import es.chess.mechanics.backend.entorno.Tablero;
import es.chess.mechanics.backend.fichas.especificas.Rey;

import java.util.HashSet;
import java.util.Map;
import java.util.TreeMap;

public abstract class Pieza {

    protected boolean movida;
    protected boolean blanca;
    protected String casilla;
    protected HashSet<String> casillasDisponibles;
    protected HashSet<String> casillasControladas;

    public Pieza(boolean blanca){
        this.blanca = blanca;
        this.casilla = null;
        this.casillasDisponibles = new HashSet<>();
        this.casillasControladas = new HashSet<>();
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

    protected HashSet<String> movimientosDisponiblesPeon(Tablero tablero){
        if (this.isBlanca()){
            if (tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() == 2){
                comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna(), true, false);
            }
            comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna(), true, false);
            comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1, true, true);
            comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1, true, true);
        }else{
            if (tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() == tablero.getNumeroFilas() - 1){
                comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna(), true, false);
            }
            comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna(), true, false);
            comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1, true, true);
            comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1, true, true);
        }
        // AÑADIR CAPTURA AL PASO
        return casillasDisponibles;
    }

    protected HashSet<String> movimientosDisponiblesCaballo(Tablero tablero){
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+2, false, false);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-2, false, false);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+2, false, false);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-2, false, false);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1, false, false);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1, false, false);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1, false, false);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1, false, false);
        this.casillasControladas.addAll(this.casillasDisponibles);
        return this.casillasDisponibles;
    }

    protected HashSet<String> movimientosDisponiblesAlfil(Tablero tablero){
        int offsetFila = 1;
        int offsetCol = 1;
        boolean ocupada = false;
        Casilla casillaCandidata;
        while (!ocupada){
            ocupada = comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() - offsetFila,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna() - offsetCol, false, false);

            offsetFila++;
            offsetCol++;
        }
        offsetFila = 1;
        offsetCol = 1;
        ocupada = false;
        while (!ocupada){
            ocupada = comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() - offsetFila,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna() + offsetCol, false, false);
            offsetFila++;
            offsetCol++;
        }
        offsetFila = 1;
        offsetCol = 1;
        ocupada = false;
        while (!ocupada){
            ocupada = comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() + offsetFila,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna() - offsetCol, false, false);
            offsetFila++;
            offsetCol++;
        }
        offsetFila = 1;
        offsetCol = 1;
        ocupada = false;
        while (!ocupada){
            ocupada = comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() + offsetFila,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna() + offsetCol, false, false);
            offsetFila++;
            offsetCol++;
        }
        this.casillasControladas.addAll(this.casillasDisponibles);
        return casillasDisponibles;
    }

    protected HashSet<String> movimientosDisponiblesTorre(Tablero tablero){
        int offsetFila = 1;
        int offsetCol = 1;
        boolean ocupada = false;
        while (!ocupada){
            ocupada = comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() - offsetFila,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna(), false, false);
            offsetFila++;
        }
        ocupada = false;
        while (!ocupada){
            ocupada = comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila(),tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-offsetCol, false, false);
            offsetCol++;
        }
        offsetFila = 1;
        ocupada = false;
        while (!ocupada){
            ocupada = comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila() + offsetFila,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna(), false, false);
            offsetFila++;
        }
        offsetCol = 1;
        ocupada = false;
        while (!ocupada){
            ocupada = comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila(),tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna() + offsetCol, false, false);
            offsetCol++;
        }
        this.casillasControladas.addAll(this.casillasDisponibles);
        return casillasDisponibles;
    }

    protected HashSet<String> movimientosDisponiblesRey(Tablero tablero){
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1, false, false);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna(), false, false);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1, false, false);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila(),tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1, false, false);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila(),tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1, false, false);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1, false, false);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna(), false, false);
        comprobarCasillaMovimiento(tablero, tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1, false, false);
        this.casillasControladas.addAll(this.casillasDisponibles);
        // FALTA QUITAR CASILLAS CONTROLADAS POR EL RIVAL
        HashSet<String> casillasRivalesControladas = new HashSet<>();
        TreeMap<String, Pieza> piezasRivales;
        if (this.isBlanca()){
            piezasRivales = tablero.getPiezasNegras();
        }else{
            piezasRivales = tablero.getPiezasBlancas();
        }
        for (Map.Entry<String, Pieza> pieza : piezasRivales.entrySet()){
            casillasRivalesControladas.addAll(pieza.getValue().casillasControladas);
        }

        for (String casilla : casillasRivalesControladas){
            if (casillasDisponibles.contains(casilla)){
                casillasDisponibles.remove(casilla);
            }
        }
        //tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1).toStringNotacionAlgebraica();
        return casillasDisponibles;
    }

    protected HashSet<String> casillasControladasPeon(Tablero tablero){
        int filaDelantePeon = this.isBlanca() ? tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1 : tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1;
        if (tablero.obtenerCasilla(filaDelantePeon,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1)!=null){
            this.casillasControladas.add(tablero.obtenerCasilla(filaDelantePeon,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1).toStringNotacionAlgebraica());
        }
        if (tablero.obtenerCasilla(filaDelantePeon,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1)!=null){
            this.casillasControladas.add(tablero.obtenerCasilla(filaDelantePeon,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1).toStringNotacionAlgebraica());
        }
        return casillasControladas;
    }

    protected HashSet<String> casillasControladasCaballo(Tablero tablero){
        if (tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+2)!=null){
            this.casillasControladas.add(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+2).toStringNotacionAlgebraica());
        }
        if (tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+2)!=null){
            this.casillasControladas.add(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+2).toStringNotacionAlgebraica());
        }
        if (tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-2)!=null){
            this.casillasControladas.add(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-2).toStringNotacionAlgebraica());
        }
        if (tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-2)!=null){
            this.casillasControladas.add(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-1,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-2).toStringNotacionAlgebraica());
        }
        if (tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1)!=null){
            this.casillasControladas.add(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1).toStringNotacionAlgebraica());
        }
        if (tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1)!=null){
            this.casillasControladas.add(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()+1).toStringNotacionAlgebraica());
        }
        if (tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1)!=null){
            this.casillasControladas.add(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()+2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1).toStringNotacionAlgebraica());
        }
        if (tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1)!=null){
            this.casillasControladas.add(tablero.obtenerCasilla(tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getFila()-2,tablero.obtenerCasillaNotacionAlgebraica(this.getCasilla()).getColumna()-1).toStringNotacionAlgebraica());
        }
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
            }else{
                existe = false;
            }
            offsetFila++;
            offsetCol++;
        }
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

    private boolean comprobarCasillaJaque (Tablero tablero, String casillaDestino){
        return tablero.getPiezaCasilla(casillaDestino, !blanca) instanceof Rey;
    }

    private boolean comprobarCasillaMovimiento(Tablero tablero, int fila, int columna, boolean movimientoPeon, boolean capturaPeon){
        Casilla casillaCandidata = tablero.obtenerCasilla(fila, columna);
        if (casillaCandidata != null){
            if (movimientoPeon){
                if (capturaPeon){
                    if (casillaCandidata.isOcupadaRival(tablero, this.isBlanca())){
                        this.casillasDisponibles.add(casillaCandidata.toStringNotacionAlgebraica());
                    }
                }else{
                    if (!casillaCandidata.isOcupada(tablero)){
                        this.casillasDisponibles.add(casillaCandidata.toStringNotacionAlgebraica());
                    }
                }

            }else{
                if (!casillaCandidata.isOcupada(tablero) || casillaCandidata.isOcupadaRival(tablero, this.isBlanca())){
                    if (comprobarCasillaJaque(tablero, casillaCandidata.toStringNotacionAlgebraica())){
                        tablero.setJaque(true);
                    }else{
                        this.casillasDisponibles.add(casillaCandidata.toStringNotacionAlgebraica());
                    }
                }
            }
            return casillaCandidata.isOcupada(tablero);
        }else{
            return true;
        }
    }

    public void movimiento (String destino){
        this.setCasilla(destino);
        if (!this.movida){
            this.movida = true;
        }
    }
}
