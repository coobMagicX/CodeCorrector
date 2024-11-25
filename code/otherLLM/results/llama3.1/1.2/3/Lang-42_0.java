public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        String entityName = this.entityName(c);
        if (entityName == null) {
            if (c > 0xFFFF) { // Update condition to check for Unicode code points outside BMP
                writer.write("&#x");
                writer.write(Integer.toHexString(c)); // Escape as hex value
                writer.write(';');
            } else if (c > 0x7F) { // Handle non-ASCII characters correctly
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