public int translate(CharSequence input, int index, Writer out) throws IOException {
    int seqEnd = input.length();
    if (input.charAt(index) == '&' && index < seqEnd - 1 && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        // Check if we're dealing with a hexadecimal entity
        char firstChar = input.charAt(start);
        if (firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;
        }

        int end = start;
        // Find the end of the numerical sequence or the whole input
        while (end < seqEnd && input.charAt(end) != ';') {
            end++;
        }

        // If the end of the input has been reached or no semi-colon is found
        if (end == seqEnd || input.charAt(end) != ';') {
            out.write("&");
            out.write(input.charAt(index + 1));
            if (isHex) {
                out.write('x');
            }
            out.write(input.subSequence(start, end).toString());
            return end - index;
        }

        int entityValue;
        try {
            String entity = input.subSequence(start, end).toString();
            if (isHex) {
                entityValue = Integer.parseInt(entity, 16);
            } else {
                entityValue = Integer.parseInt(entity, 10);
            }
        } catch (NumberFormatException nfe) {
            out.write(input.subSequence(index, end + 1).toString());
            return end - index + 1;
        }

        if (entityValue > 0xFFFF) {
            char[] chrs = Character.toChars(entityValue);
            out.write(chrs[0]);
            out.write(chrs[1]);
        } else {
            out.write(entityValue);
        }

        return end + 1 - index; // includes the ';' in the count
    }
    return 0;
}
