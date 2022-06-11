package es.chess.mechanics.backend.controller;

import es.chess.mechanics.backend.entorno.Partida;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class GameController {

    @GetMapping("/createGame")
    public Partida obtenerPartida() {
        return new Partida();
    }

}
