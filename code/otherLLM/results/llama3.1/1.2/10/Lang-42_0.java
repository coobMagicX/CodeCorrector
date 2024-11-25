public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        String entityName = this.entityName(c);
        if (entityName == null) {
            if (c > 0x7F) { // only convert Unicode characters to integer value if they are not single-byte ASCII
                int utf8BytesToIntValue = getUtf8BytesToIntValue(c); // call new method to correctly convert UTF-8 bytes to integer value
                writer.write("&#");
                writer.write(Integer.toString(utf8BytesToIntValue, 10));
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

// new method to correctly convert UTF-8 bytes to integer value
public int getUtf8BytesToIntValue(char c) {
    // implement logic here based on the analysis and repair strategy provided (UTF-8 byte representation)
    // For example, you might use a library like ICU4J or manually implement the conversion logic
}