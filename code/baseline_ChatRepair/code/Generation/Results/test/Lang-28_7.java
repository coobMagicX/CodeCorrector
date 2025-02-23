public int translate(CharSequence input, int index, Writer out) throws IOException {
    
    if (index > input.length() - 2) {
        return 0;
    }

    if (input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
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

        
        if (end == input.length()) {
            return 0;
        }

        
        String numberStr = input.subSequence(start, end).toString();
        int entityValue;
        try {
            
            entityValue = Integer.parseInt(numberStr, isHex ? 16 : 10);
        } catch (NumberFormatException nfe) {
            
            return 0;
        }

        