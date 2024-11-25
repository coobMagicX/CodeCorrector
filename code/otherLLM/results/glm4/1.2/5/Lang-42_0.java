public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        if (c > 0x7F) { // Check for high Unicode character
            int codePoint = Character.codePointAt(str, i); // Get the full code point value
            writer.write("&#");
            writer.write(Integer.toString(codePoint, 10));
            writer.write(';');
            i += Character.charCount(codePoint) - 1; // Update the index if it was a surrogate pair
        } else if (c == '&') {
            writer.write("&amp;");
        } else if (c == '<') {
            writer.write("&lt;");
        } else if (c == '>') {
            writer.write("&gt;");
        } else {
            writer.write(c);
        }
    }
}