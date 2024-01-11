package gestion.cosit.gestionDepense.model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

public class MontantSerializer extends JsonSerializer<Integer> {

    @Override
    public void serialize(Integer montant, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        // Masquer le montant, par exemple, avec des étoiles
        String montantMasque = "****";
        jsonGenerator.writeString(montantMasque);
    }
}
