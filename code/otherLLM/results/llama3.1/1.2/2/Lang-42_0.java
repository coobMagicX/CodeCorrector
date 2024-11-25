public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        String entityName = this.entityName(c);
        if (entityName == null) {
            // Validate character encoding and generate proper entity name
            if (Character.isHighSurrogate(c)) {
                writer.write("&#x");
                writer.write(entityValue("\u" + Integer.toHexString(Character.toCodePoint(c, str.charAt(i + 1)))));
                i++;
                writer.write(';');
            } else if (c > 0x7F) {
                int codePoint = entityValue("#" + Integer.toString(c, 10));
                String entityName = "\u" + Integer.toHexString(codePoint);
                writer.write("&#");
                writer.write(entityValue(entityName));
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