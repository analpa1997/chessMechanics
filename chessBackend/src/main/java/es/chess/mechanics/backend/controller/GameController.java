package es.chess.mechanics.backend.controller;

import es.chess.mechanics.backend.entorno.Partida;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class GameController {

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("partida", new Partida());
        return "index";
    }

}
