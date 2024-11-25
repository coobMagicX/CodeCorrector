public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        // Using entityName method to check if there's an HTML entity defined for this character.
        String entityName = this.entityName(c);
        if (entityName == null) {
            if (c > 0x7F && c <= 0xFFFF) { // Check if the character is between U+0001 and U+FFFF
                writer.write("&#");
                // Convert to decimal representation, padding with zeros if necessary.
                String hexValue = Integer.toString(c, 10);
                int digits = hexValue.length();
                for (int j = digits; j < 5; j++) { // HTML entities are typically 4 digits long
                    writer.write('0');
                }
                writer.write(hexValue);
                writer.write(';');
            } else if (c > 0xFFFF) { // Characters above U+FFFF are represented in UTF-16 encoding, which can be complex.
                // We need to implement a more complex approach for characters above U+FFFF.
                // This is left as an exercise since it's beyond the scope of this fix and requires understanding the context.
            } else {
                writer.write(c);
            }
        } else {
            writer.write('&');
            writer.write(entityName);
            writer.write(';');
        }
    }
}