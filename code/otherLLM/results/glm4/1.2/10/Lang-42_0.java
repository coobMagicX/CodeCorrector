public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        String entityName = this.entityName(c);

        if (entityName == null) {
            if (c > 0x7F && c <= 0xFFFF) { // Handles characters from U+0000 to U+FFFF
                writer.write("&#");
                writer.write(Integer.toString(c, 10));
                writer.write(';');
            } else if (c > 0xFFFF) { // Handles characters above U+FFFF
                int codePoint = c;
                byte[] utf8Bytes = new byte[4];
                utf8Bytes[0] = (byte) ((codePoint >>> 12) & 0xFF);
                utf8Bytes[1] = (byte) ((codePoint >>> 6) & 0xFF);
                utf8Bytes[2] = (byte) (codePoint & 0x3F);
                writer.write("&#");
                writer.write(Integer.toString(codePoint, 10));
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