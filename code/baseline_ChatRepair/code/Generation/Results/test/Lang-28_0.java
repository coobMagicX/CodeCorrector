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

        
        if (end == input.length()) {
            return 0;
        }

        
        String number = input.subSequence(start, end).toString();
        
        try {
            
            int entityValue = Integer.parseInt(number, isHex ? 16 : 10);
            
            
            if (entityValue > 0