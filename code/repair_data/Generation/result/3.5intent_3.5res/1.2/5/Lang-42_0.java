public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        int codePoint = str.codePointAt(i);
        if (Character.charCount(codePoint) > 1) {
            writer.write(Character.toChars(codePoint));
            i += Character.charCount(codePoint) - 1; // Need to skip additional characters for high unicode
        } else {
            char c = (char) codePoint;
            String entityName = this.entityName(c);
            if (entityName == null) {
                if (c > 0x7F || Character.isISOControl(c)) { // Updated condition to include ISO control characters
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