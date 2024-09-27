public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; ) {
        int codePoint = str.codePointAt(i);
        i += Character.charCount(codePoint);

        String entityName = this.entityName(codePoint);
        if (entityName == null) {
            if (codePoint > 0x7F) {
                writer.write("&#");
                writer.write(Integer.toString(codePoint, 10));
                writer.write(';');
            } else {
                writer.write(Character.toChars(codePoint));
            }
        } else {
            writer.write('&');
            writer.write(entityName);
            writer.write(';');
        }
    }
}