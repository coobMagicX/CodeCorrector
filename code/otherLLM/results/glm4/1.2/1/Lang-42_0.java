public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        String entityName = this.entityName(c);

        // Using the provided method 'entityName(int value)' that takes an integer as its parameter.
        if (entityName == null) {
            if (c > 0x7F && c <= 0xFFFF) { // Ensure we only write NCR for codepoints up to U+FFFF
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

public String entityName(int value) {
    return map.name(value);
}