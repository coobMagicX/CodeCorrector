public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        String entityName = this.entityName(c);
        if (entityName == null) {
            if (c > 0x7F) {
                writer.write("&#");
                writer.write(Integer.toString(c, 10));
                writer.write(';');
            } else {
                writer.write(Character.toString(c)); // Fix: Write the character as a string
            }
        } else {
            writer.write('&');
            writer.write(entityName);
            writer.write(';');
        }
    }
}

Methods that you can utilize:
static void fillWithHtml40Entities(Entities entities) {
    entities.addEntities(BASIC_ARRAY);
    entities.addEntities(ISO8859_1_ARRAY);
    entities.addEntities(HTML40_ARRAY);
}