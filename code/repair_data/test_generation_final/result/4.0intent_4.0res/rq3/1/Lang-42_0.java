public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        String entityName = this.entityName(c);
        if (entityName == null) {
            if (c > 0x7F) {
                writer.write("&#");
                writer.write(Integer.toString(c));
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

private String entityName(char c) {
    // This method should return the entity name if c is a special character;
    // otherwise, it should return null.
    // Example implementation (extend with actual mappings as needed):
    switch(c) {
        case '<': return "lt";
        case '>': return "gt";
        case '&': return "amp";
        case '"': return "quot";
        default: return null;
    }
}