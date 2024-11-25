public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        String entityName = this.entityName(c);
        if (entityName == null) {
            if (c > 0x7F) {
                writer.write("&#");
                // Use Unicode code point to match the expected escaped value
                writer.write(Integer.toString(c, 16)); // Change from base 10 to base 16
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