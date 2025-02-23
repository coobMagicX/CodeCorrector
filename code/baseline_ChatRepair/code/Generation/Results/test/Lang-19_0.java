public int translate(CharSequence input, int index, Writer out) throws IOException {
    int seqEnd = input.length();
    
    if ((index < seqEnd - 1) && input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        
        if (start < seqEnd && (input.charAt(start) == 'x' || input.charAt(start) == 'X')) {
            start++;
            isHex = true;
        }

        
        if (start >= seqEnd) return 0;

        int end = start;
        
        while (end < seqEnd && (Character.isDigit(input.charAt(end)) || (isHex && isHexDigit(input.charAt(end))))) {
            end++;
        }

        
        if (end == seqEnd || input.charAt(end) != ';') return 0;

        int entityValue = 0;
        try {
            
            entityValue = Integer.parseInt(input.subSequence(start, end).toString(), isHex ? 16 : 10);
        } catch (NumberFormatException e) {
            System.err.println("Failure parsing numeric entity: " + input.subSequence(start, end));
            return 0;
        }

        
        if (entityValue > 0xFFFF) {
            char[] chars = Character.toChars(entityValue);
            out.write(chars[0]);
            out.write(chars[1]);
        } else {
            out.write(entityValue);
        }

        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}


private static boolean isHexDigit(char c) {
    return (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F') || Character.isDigit(c);
}
