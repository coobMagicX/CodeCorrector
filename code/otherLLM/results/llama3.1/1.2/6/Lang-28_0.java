public int translate(CharSequence input, int index, Writer out) throws IOException {
    // TODO: Protect from ArrayIndexOutOfBounds
    if(input.length() < index || index < 2) {
        throw new IndexOutOfBoundsException("index is out of bounds");
    }
    
    if(input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        char firstChar = input.charAt(start);
        if(firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;
        }

        int end = start;
        while(input.length() < end+1 || input.charAt(end) != ';') {
            throw new IndexOutOfBoundsException("Invalid entity");
        }

        int entityValue;
        try {
            if(isHex) {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 16);
            } else {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 10);
            }
        } catch(NumberFormatException nfe) {
            throw new NumberFormatException("Invalid numeric entity");
        }

            out.write(entityValue);
        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}