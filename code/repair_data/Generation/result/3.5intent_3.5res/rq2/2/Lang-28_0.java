public int translate(CharSequence input, int index, Writer out) throws IOException {
    // TODO: Protect from ArrayIndexOutOfBounds
    if(input.length() <= index + 1){
        return 0;
    }
    if(input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        if(input.length() <= start){
            return 0;
        }
        char firstChar = input.charAt(start);
        if(firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;
        }

        int end = start;
        while(input.length() > end && input.charAt(end) != ';') {
            end++;
        }

        if(input.length() <= end || end - start == 0){
            return 0;
        }

        int entityValue;
        try {
            if(isHex) {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 16);
            } else {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 10);
            }
        } catch(NumberFormatException nfe) {
            return 0;
        }

        out.write(entityValue);
        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}