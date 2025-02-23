public int translate(CharSequence input, int index, Writer out) throws IOException {
    
    if (index + 1 < input.length() && input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
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

        
        if (end == input.length() || end == start) {
            return 0; 
        }

        int entityValue;
        try {
            
            String numStr = input.subSequence(start, end).toString();
            entityValue = Integer.parseInt(numStr, isHex ? 16 : 10);
        } catch (NumberFormatException nfe) {
            
            return 0;
        }

        
       