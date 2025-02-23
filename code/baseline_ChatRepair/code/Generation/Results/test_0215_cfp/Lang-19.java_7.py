public int translate(CharSequence input, int index, Writer out) throws IOException {
    int seqEnd = input.length();
    
    if (index < seqEnd - 1 && input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        char firstChar = input.charAt(start);
        if (firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;
        }

        if (start >= seqEnd) {
            // Early exit if there's nothing after 'x' or 'X' or starting number
            return 0;
        }

        int end = start;
        while (end < seqEnd && input.charAt(end) != ';') {
            end++;
        }

        // Handle cases where `;` is not found.
        if (end == seqEnd || input.charAt(end) != ';') {
            return 0;
        }

        int entityValue = 0;
        try {
            String numberStr = input.subSequence(start, end).toString();
            entityValue = isHex ? Integer.parseInt(numberStr, 16) : Integer.parseInt(numberStr, 10);
        } catch (NumberFormatException nfe) {
            System.err.println("FAIL: " + input.subSequence(start, end) + "[" + start +"]["+ end +"]");
            return 0;
        }

        if (entityValue > 0xFFFF) {
            char[] chrs = Character.toChars(entityValue);
            out.write(chrs[0]);
            out.write(chrs[1]);
        } else {
            out.write(entityValue);
        }

        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}
