public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        String entityName = this.entityName(c);

        if (entityName == null) {
            // Check if the character is a non-BMP character
            if (c > 0xFFFF) { // Unicode BMP ends at U+FFFF, so we check for characters above this range
                int codePoint = Character.codePointAt(new char[]{(char)c}, 0);
                writer.write("&#");
                writer.write(Integer.toString(codePoint, 10));
                writer.write(';');
            } else if (c > 0x7F) {
                // Handle other non-ASCII characters which are not BMP
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