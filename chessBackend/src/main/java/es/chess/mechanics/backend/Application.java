package es.chess.mechanics.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    /**
     * Application's main method
     *
     * @param args the console arguments
     */
    public static void main(String[] args) {
        // Run the app!
        /*Partida partida = new Partida();
        String notacionPartida = "#\tBlancas\tNegras\n";
        notacionPartida += partida.realizarMovimiento("e2", "e4");
        notacionPartida += partida.realizarMovimiento("e7", "e5");
        notacionPartida += partida.realizarMovimiento("g1", "f3");
        notacionPartida += partida.realizarMovimiento("b8", "c6");
        notacionPartida += partida.realizarMovimiento("f1", "c4");
        notacionPartida += partida.realizarMovimiento("f8", "c5");
        notacionPartida += partida.realizarMovimiento("b1", "c3");
        notacionPartida += partida.realizarMovimiento("g8", "f6");
        notacionPartida += partida.realizarMovimiento("f3", "e5");
        System.out.println(notacionPartida);
        System.out.println();
        System.out.println("TABLERO ACTUAL");
        System.out.println();
        System.out.println(partida.getTablero());*/
        //partida.getTablero().informacionPiezas();
        SpringApplication.run(Application.class, args);
    }
}