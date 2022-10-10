package es.chess.mechanics.backend.entorno;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.KeyDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.BooleanNode;
import com.fasterxml.jackson.databind.node.IntNode;

import java.io.IOException;

public class PosicionFENDeserializer extends KeyDeserializer {

    @Override
    public PosicionFEN deserializeKey(String key, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        //Use the string key here to return a real map key object
        String[] campos = key.split(",");
        PosicionFEN posicionFEN = new PosicionFEN();
        posicionFEN.setPosicion(campos[0].split(":")[1].trim());
        posicionFEN.setEnroques(campos[2].split(":")[1].trim());
        posicionFEN.setComerPaso(campos[3].split(":")[1].trim());
        posicionFEN.setnMovimientos(Integer.parseInt(campos[5].split(":")[1].replaceAll("}", "").trim()));
        posicionFEN.setnTurnos(Integer.parseInt(campos[4].split(":")[1].trim()));
        posicionFEN.setTurnoBlancas(Boolean.parseBoolean(campos[1].split(":")[1]));
        return posicionFEN;
    }
}