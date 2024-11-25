public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        String entityName = this.entityName(c);
        if (entityName == null) {
            if (c > 0x7F) {
                writer.write("&#");
                // Fix: Use Integer.toString(c, 16) to get the hexadecimal representation
                writer.write(Integer.toHexString(c));
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