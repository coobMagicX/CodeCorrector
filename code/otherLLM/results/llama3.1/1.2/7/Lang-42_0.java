public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        String entityName = this.entityName(c);
        if (entityName == null) {
            int unicodeCodepoint = getUnicodeCodepoint(c);
            writer.write("&#");
            writer.write(Integer.toString(unicodeCodepoint, 10));
            writer.write(';');
        } else {
            writer.write('&');
            writer.write(entityName);
            writer.write(';');
        }
    }
}

private int getUnicodeCodepoint(char c) {
    return (int) c;
}