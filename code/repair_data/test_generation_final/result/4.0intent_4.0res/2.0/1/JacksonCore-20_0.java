import java.io.IOException;
import java.util.Base64;

public void writeEmbeddedObject(Object object) throws IOException {
    if (object instanceof byte[]) {
        String encoded = Base64.getEncoder().encodeToString((byte[]) object);
        writeString(encoded);
    } else {
        // Handle other types as needed
        throw new JsonGenerationException("Unsupported type for embedded objects", this);
    }
}