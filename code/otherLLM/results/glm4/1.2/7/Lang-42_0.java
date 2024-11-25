import java.io.Writer;
import java.nio.charset.StandardCharsets;

public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; ) {
        int codePoint = str.codePointAt(i);
        if (Character.isSurrogatePair(str.charAt(i), str.charAt(i + 1))) {
            // We found a surrogate pair, write it as two characters
            writer.write(new String(Character.toChars(codePoint)));
            i += 2;
        } else {
            String entityName = this.entityName(codePoint);
            if (entityName == null) {
                // Write code point as hexadecimal if not an HTML entity
                byte[] bytes = new byte[4];
                bytes[0] = (byte) ('&');
                bytes[1] = (byte) ('#');
                bytes[2] = (byte) ((codePoint >> 12) & 0x0F);
                bytes[3] = (byte) ((codePoint >> 8) & 0x0F);
                writer.write(new String(bytes, StandardCharsets.UTF_8));
                writer.write(Integer.toString((char) codePoint));
                writer.write(';');
            } else {
                // Write the entity name
                writer.write('&');
                writer.write(entityName);
                writer.write(';');
            }
            i += Character.charCount(codePoint); // Move to the end of this character
        }
    }
}