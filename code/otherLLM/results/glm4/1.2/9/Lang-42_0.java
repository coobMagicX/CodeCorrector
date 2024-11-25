public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        if (c > 0x7F) { // This is the critical part where we need to handle high Unicode characters
            String entityName = this.entityName(c);
            if (entityName == null) { // If there's no named entity, use numeric character reference
                writer.write("&#");
                writer.write(Integer.toString(c, 10));
                writer.write(';');
            } else {
                writer.write('&');
                writer.write(entityName);
                writer.write(';');
            }
        } else if (c == '&' || c == '<' || c == '>' || c == '"' || c == "'") { // Handle other special characters
            writer.write('&');
            writer.write(this.entityName(c));
            writer.write(';');
        } else {
            writer.write(c);
        }
    }
}

public String entityName(int value) {
    return map.name(value); // This method should return the correct named entity or null if none exists
}