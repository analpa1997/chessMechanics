package es.chess.mechanics.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import es.chess.mechanics.backend.entorno.Partida;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class GameController {

    @GetMapping("/createGame")
    public Partida obtenerPartida() {
        return new Partida();
    }

    @PostMapping("/moveGame")
    public Partida realizarMovimiento (@RequestBody Map<String, Object> request){
        String movimientoOrigen = (String) request.get("movimientoOrigen");
        String movimientoDestino = (String) request.get("movimientoDestino");
        ObjectMapper mapper = new ObjectMapper();
        Partida partida = mapper.convertValue(request.get("partida"), Partida.class);

        partida.realizarMovimiento(movimientoOrigen, movimientoDestino);
        return partida;
    }

}
