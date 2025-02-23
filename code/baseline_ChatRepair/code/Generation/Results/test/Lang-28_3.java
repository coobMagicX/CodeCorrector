public int translate(CharSequence input, int index, Writer out) throws IOException {
    
    if (index <= input.length() - 2 && input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
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
            out.write('&');
            out.write('#');
            if (isHex) {
                out.write('x');
            }
            return 2 + (isHex ? 1 : 0);
        }

        
        try {
            String numberStr = input.subSequence(start, end).toString();
            int entityValue = Integer.parseInt(numberStr, isHex ? 16 : 10);
            
            if (