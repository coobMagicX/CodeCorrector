public int translate(CharSequence input, int index, Writer out) throws IOException {
    
    if(index + 2 <= input.length() && input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        
        if(input.charAt(start) == 'x' || input.charAt(start) == 'X') {
            start++;
            isHex = true;
        }

        int end = start;

        
        while(end < input.length() && input.charAt(end) != ';') {
            end++;
        }

        
        if(end == input.length()) {
            out.write(input.subSequence(index, end).toString());
            return end - index;
        }

        int entityValue = 0;
        String entity = input.subSequence(start, end).toString();

        
        try {
            if (entity.isEmpty()) {
                out.write(input.subSequence(index, end + 1).toString());
                return end + 1 - index;
            }
            entityValue = Integer.parseInt(entity, isHex ? 16 : 10);

            
            if(entityValue > 0xFFFF) {
                
                out.write(Character.toChars(entityValue));
            } else {
                out