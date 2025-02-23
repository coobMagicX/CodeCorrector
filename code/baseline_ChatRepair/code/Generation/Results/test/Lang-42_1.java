public void escape(Writer writer, String str) throws IOException {
    int len = str.length();
    for (int i = 0; i < len; i++) {
        char c = str.charAt(i);
        if (Character.isSurrogate(c)) {
            int codePoint;
            if (Character.isHighSurrogate(c) && i + 1 < len && Character.isLowSurrogate(str.charAt(i + 1))) {
                
                codePoint = Character.toCodePoint(c, str.charAt(i + 1));
                i++; 
            } else {
                
                codePoint = c; 
            }
            writeCharacterEntity(writer, codePoint);
