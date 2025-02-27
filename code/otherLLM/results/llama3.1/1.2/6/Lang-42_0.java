public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        String entityName = this.entityName(c);
        if (entityName == null) {
            if (c > 0x7F && c <= 0xFFFF) { // Corrected condition to include code points up to U+FFFF
                writer.write("&#");
                writer.write(Integer.toString(getCodePointValue(c), 10));
                writer.write(';');
            } else {
                writer.write("&#");
                writer.write(Integer.toString(c, 10));
                writer.write(';');
            }
        } else {
            writer.write('&');
            writer.write(entityName);
            writer.write(';');
        }
    }
}

private int getCodePointValue(char c) {
    return (c >> 8) & 0xFF | ((c & 0xFF) << 8);
}