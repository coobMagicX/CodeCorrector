public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        String entityName = this.entityName(c);

        if (entityName == null) {
            // Check for high Unicode characters and output HTML entities accordingly.
            if (c > 0x7F && c <= 0xFFFF) { // We need to handle both BMP (0-FFFF) and supplementary planes.
                int codePoint = c;
                if (codePoint > 0xFF) {
                    writer.write("&#");
                    writer.write(Integer.toString(codePoint, 10));
                    writer.write(';');
                } else {
                    // Handle single byte characters in the range of 0x80 to 0xFF directly as entities.
                    String entity = getEntityForCodePoint(c);
                    if (entity != null) {
                        writer.write('&');
                        writer.write(entity);
                        writer.write(';');
                    } else {
                        writer.write(c); // Fallback for unsupported or control characters
                    }
                }
            } else if (c > 0xFFFF && c <= 0x10FFFF) { // Handle supplementary planes.
                int codePoint = Character.codePointAt(str, i);
                i++; // Increment the index to account for the second character of a surrogate pair.
                
                String entity = getEntityForCodePoint(codePoint);
                if (entity != null) {
                    writer.write("&#");
                    writer.write(Integer.toString(codePoint, 10));
                    writer.write(';');
                } else {
                    writer.write(c); // Fallback for unsupported or control characters
                }
            } else {
                // ASCII range (0-7F)
                writer.write(c);
            }
        } else {
            // Output the named entity.
            writer.write('&');
            writer.write(entityName);
            writer.write(';');
        }
    }
}

// Helper method to get the HTML entity for a given code point.
private String getEntityForCodePoint(int codePoint) {
    switch (codePoint) {
        case 0x26: return "amp";
        case 0x3C: return "lt";
        case 0x3E: return "gt";
        case 0x22: return "quot";
        case 0x27: return "apos";
        default:
            // Additional named entities can be added here.
            return null;
    }
}