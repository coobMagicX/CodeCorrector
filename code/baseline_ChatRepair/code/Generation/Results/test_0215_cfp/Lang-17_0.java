public final void translate(CharSequence input, Writer out) throws IOException {
    if (out == null) {
        throw new IllegalArgumentException("The Writer must not be null");
    }
    if (input == null) {
        return;
    }
    int pos = 0;
    
    int len = input.length();
    while (pos < len) {
        int consumed = translate(input, pos, out);
        if (consumed == 0) {
            
            int codePoint = Character.codePointAt(input, pos);
            char[] charArray = Character.toChars(codePoint);
            out.write(charArray);
            
            pos += Character.charCount(codePoint);
        } else {
            
            int count = 0;
            while (count < consumed) {
                int currentCodePoint = Character.codePointAt(input, pos);
                pos += Character.charCount(currentCodePoint);
                count++;
            }
            
            if (Character.isSurrogatePair(input.charAt(pos - 1), input.charAt(pos - 2))) {
                pos--;
            }
        }
    }
}
