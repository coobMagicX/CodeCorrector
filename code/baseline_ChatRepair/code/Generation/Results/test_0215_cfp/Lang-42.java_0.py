public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        if (Character.isHighSurrogate(c) && i + 1 < len && Character.isLowSurrogate(str.charAt(i + 1))) {
            // Get the code point for the surrogate pair.
            int codePoint = Character.toCodePoint(c, str.charAt(i + 1));
            writer.write("&#");
            writer.write(Integer.toString(codePoint, 10));
            writer.write(';');
            i++; // Skip the next character as it is part of the surrogate pair
        } else {
            String entityName = this.entityName(c);
            if (entityName == null) {
                if (c > 0x7F) {
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
}
