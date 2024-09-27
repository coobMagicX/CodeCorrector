public void escape(Writer writer, String str) throws IOException {
    int i = 0;
    while (i < str.length()) {
        int codePoint = str.codePointAt(i);
        String entityName = this.entityName(codePoint);
        if (entityName == null) {
            if (codePoint > 0x7F) {
                writer.write("&#");
                writer.write(Integer.toString(codePoint));
                writer.write(';');
            } else {
                writer.write(Character.toChars(codePoint));
            }
        } else {
            writer.write('&');
            writer.write(entityName);
            writer.write(';');
        }
        i += Character.charCount(codePoint);
    }
}