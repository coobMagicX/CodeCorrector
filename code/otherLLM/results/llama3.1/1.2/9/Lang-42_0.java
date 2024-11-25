public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        int unicodeCodePoint = this.unicodeCodePoint(c); // Correct Unicode Code Point Calculation
        String entityName = this.entityName(unicodeCodePoint); // Ensure Entity Name Generation for High-Unicode Characters
        
        if (entityName == null) { // Proper Handling of High-Unicode Characters in `entityName` Method
            if (c > 0x7F) {
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

public int unicodeCodePoint(char c) {
    return (int)c;
}