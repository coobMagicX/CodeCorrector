public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        if (c > 0x7F && c <= 0xFFFF) { // Check for non-BMP character
            writer.write("&#");
            String codePoint;
            if (c <= 0xFF) {
                codePoint = Integer.toString(c, 10);
            } else if (c <= 0x7FF) {
                codePoint = Integer.toHexString(c & 0x7FF) + "000";
            } else {
                codePoint = Integer.toHexString(c & 0xFFFF) + "0000";
            }
            writer.write(codePoint.substring(1, codePoint.length() - 4)); // Remove leading zeros
            writer.write(';');
        } else if (c > 0x7F && c < 0x80) {
            // Characters between U+0080 and U+07FF are represented as two bytes in UTF-8
            String byteRepresentation = Integer.toString(c & 0xFF, 16);
            writer.write("&#");
            if (byteRepresentation.length() == 1) writer.write('0');
            writer.write(byteRepresentation);
            writer.write(';');
        } else {
            // The character is either a BMP character or needs to be encoded as entity reference
            String entityName = this.entityName(c);
            if (entityName == null) {
                writer.write(c);
            } else {
                writer.write('&');
                writer.write(entityName);
                writer.write(';');
            }
        }
    }
}