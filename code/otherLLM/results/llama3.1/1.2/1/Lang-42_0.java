public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        String entityName = this.entityName(c);
        if (entityName == null) {
            if (c > 0xFFFF) { // High Unicode codepoint
                int highSurrogate = 0xD800 + ((c - 0x10000) >> 10);
                int lowSurrogate = 0xDC00 + ((c - 0x10000) & 0x3FF);
                writer.write("&#x");
                writer.write(Integer.toHexString(highSurrogate));
                writer.write(";&#x");
                writer.write(Integer.toHexString(lowSurrogate));
                writer.write(';');
            } else if (c > 0x7F) { // Unicode codepoint between U+80 and U+FFFF
                writer.write("&#");
                writer.write(Integer.toString(c, 10));
                writer.write(';');
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