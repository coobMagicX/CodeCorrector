public int translate(CharSequence input, int index, Writer out) throws IOException {
    
    if (index + 2 >= input.length() || input.charAt(index) != '&' || input.charAt(index + 1) != '#') {
        return 0;
    }
    
    int start = index + 2;
    boolean isHex = false;

    
    if (start < input.length() && (input.charAt(start) == 'x' || input.charAt(start) == 'X')) {
        start++;
        isHex = true;
    }

    
    int end = start;
    while (end < input.length() && input.charAt(end) != ';') {
        end++;
    }

    if (end >= input.length()) { 
        out.write(input.subSequence(index, input.length()).toString());
        return input.length() - index;
    }

    try {
        int entityValue = Integer.parseInt(input.subSequence(start, end).toString(), isHex ? 16 : 10);

        
        if (Character.isValidCodePoint(entityValue)) { 
            if (entityValue > 0xFFFF) { 
                out.write(Character.toChars(entityValue)); 
            } else {
                out.write(entityValue);
            }
        } else {
            out.write(input.subSequence(index, end + 1).