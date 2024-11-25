public int translate(CharSequence input, int index, Writer out) throws IOException {
    int seqEnd = input.length();
    // Uses -2 to ensure there is something after the &#
    if (input.charAt(index) == '&' && index < seqEnd - 1 && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        char firstChar = input.charAt(start);
        if (firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;

            // Check there's more than just an x after the &#
            if (start >= seqEnd) {
                out.write('&');
                out.write('#');
                out.write('x');
                return 4; // Moved forward by 4 characters
            }
        }

        int end = start;
        // Note that this supports character codes without a ; on the end
        while (input.charAt(end) != ';') {
            if (++end >= seqEnd) {
                // If we reach the end of input and still no semicolon, consume up to the end
                out.write('&');
                out.write('#');
                if (isHex && start == index + 2) {
                    out.write('x'); // Output 'x' if in hex mode but not followed by a valid number
                }
                return seqEnd - index;
            }
        }

        int entityValue;
        try {
            if (isHex) {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 16);
            } else {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 10);
            }
        } catch (NumberFormatException nfe) {
            System.err.println("FAIL: " + input.subSequence(start, end) + "[" + start + "[" + end + "]");
            return 0;
        }

        if (entityValue > 0xFFFF) {
            char[] chrs = Character.toChars(entityValue);
            out.write(chrs[0]);
            out.write(chrs[1]);
        } else {
            out.write(entityValue);
        }

        // Include the semicolon in the count as well
        return 2 + (end - start) + (isHex ? 1 : 0) + 1;
    }
    return 0;
}