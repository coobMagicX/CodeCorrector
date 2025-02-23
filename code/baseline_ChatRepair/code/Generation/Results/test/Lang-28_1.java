public int translate(CharSequence input, int index, Writer out) throws IOException {
    
    if (index <= input.length() - 2 && input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        
        char firstChar = input.charAt(start);
        if (firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;
        }

        int end = start;
        
        while (end < input.length() && input.charAt(end) != ';') {
            end++;
        }

        
        if (end == input.length() || end == start) {
            out.write(input.subSequence(index, end).toString());
            return end - index;
        }

        
        String entity = input.subSequence(start, end).toString();
        int entityValue;
        try {
            
            entityValue = Integer.parseInt(entity, isHex ? 16 : 10);
            
            if (Character.isValidCodePoint(entityValue)) {
                if (Character.isSupplementaryCodePoint(entityValue)) {
                    
                    out.write(Character.toChars(entity