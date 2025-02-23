public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        if (Character.isSurrogate(c)) {
            if (i + 1 < len && Character.isSurrogatePair(c, str.charAt(i + 1))) {
                
                int codePoint = Character.toCodePoint(c, str.charAt(i + 1));
                writer.write("&#");
                writer.write(Integer.toString(codePoint));
                writer.write(';');
                i++; 
            } else {
                