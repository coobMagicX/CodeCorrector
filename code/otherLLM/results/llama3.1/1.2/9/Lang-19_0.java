public int translate(CharSequence input, int index, Writer out) throws IOException {
    int seqEnd = input.length();
    if(input.charAt(index) == '&' && index < seqEnd - 1 && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        char firstChar = input.charAt(start);
        if(firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;
        }

        int end = start;
        while(input.charAt(end) != ';') {
            end++;
        }

        String entityValueStr = input.subSequence(start, end).toString();
        try {
            int entityValue = Integer.parseInt(entityValueStr, 16);
            if(entityValue > 0xFFFF) {
                char[] chrs = Character.toChars(entityValue);
                out.write(chrs[0]);
                out.write(chrs[1]);
            } else {
                out.write(entityValue);
            }
        } catch(NumberFormatException nfe) {
            System.err.println("FAIL: invalid entity value " + entityValueStr);
            return 0;
        }

        int length = end - start + (isHex ? 2 : 1);
        try {
            String str = input.subSequence(start, end).toString();
            if(entityValue > 0xFFFF) {
                out.write(str.toCharArray()[0]);
                out.write(str.toCharArray()[1]);
            } else {
                out.write(str);
            }
        } catch (IOException e) {
            // Handle IOException
        }

        return length + (isHex ? 2 : 1);
    }
    return 0;
}