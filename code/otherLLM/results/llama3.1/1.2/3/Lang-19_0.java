public int translate(CharSequence input, int index, Writer out) throws IOException {
    int seqEnd = input.length();
    // Uses -2 to ensure there is something after the &#
    if(input.charAt(index) == '&' && index < seqEnd - 1 && input.charAt(index + 1) == '#') {
        int start = index + 2;
        boolean isHex = false;

        char firstChar = input.charAt(start);
        if(firstChar == 'x' || firstChar == 'X') {
            start++;
            isHex = true;

            // Check there's more than just an x after the &#
        }

        int end = start;
        // Note that this supports character codes without a ; on the end
        while(input.charAt(end) != ';') 
        {
            end++; // allow for incomplete entity references
            if (end >= seqEnd - 1) { // check if we've reached the end of the input
                System.err.println("Incomplete entity reference: &#" + input.subSequence(index, start));
                return 0; // or throw an exception, depending on your requirements
            }
        }

        int entityValue;
        try {
            if(isHex) {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 16);
            } else {
                entityValue = Integer.parseInt(input.subSequence(start, end).toString(), 10);
            }
        } catch(NumberFormatException nfe) {
            System.err.println("FAIL: " + input.subSequence(start, end) + "[" + start +"]["+ end +"]");
            return 0;
        }

        if(entityValue > 0xFFFF) {
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