import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdScalarDeserializer;

import java.io.IOException;
import java.util.Locale;

public class LocaleDeserializer extends StdScalarDeserializer<Locale> {

    public LocaleDeserializer() {
        super(Locale.class);
    }

    @Override
    public Locale deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        String localeString = p.getValueAsString();
        if (localeString == null || localeString.isEmpty()) {
            return null;
        }

        // Deserialize locale from the string representation
        String[] parts = localeString.split("_");
        if (parts.length == 1) {
            return new Locale(parts[0]);
        } else if (parts.length == 2) {
            return new Locale(parts[0], parts[1]);
        } else {
            return new Locale(parts[0], parts[1], parts[2]);
        }
    }
}