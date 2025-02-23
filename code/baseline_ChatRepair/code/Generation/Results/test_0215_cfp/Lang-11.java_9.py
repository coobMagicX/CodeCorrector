public static String random(int count, int start, int end, boolean letters, boolean numbers,
                            char[] chars, Random random) {
    if (count == 0) {
        return "";
    } else if (count < 0) {
        throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
    }
    if (chars != null && chars.length == 0) {
        throw new IllegalArgumentException("The chars array must not be empty");
    }

    if (start == 0 && end == 0) {
        // Adjust depending on whether or not letters or numbers are needed
        if (chars != null) {
            end = chars.length;
        } else {
            if (!letters && !numbers) {
                throw new IllegalArgumentException("When neither letters nor numbers are specified, start and end must define a valid range of characters");
            }   
            start = ' ';
            end = 'z' + 1;
        }
    } else if (end <= start) {
        throw new IllegalArgumentException("End (" + end + ") must be greater than start (" + start + ")");
    }

    char[] buffer = new char[count];
    int gap = end - start;
    
    while (count-- != 0) {
        char ch;
        if (chars == null) {
            ch = (char) (random.nextInt(gap) + start);
        } else {
            ch = chars[random.nextInt(chars.length) + start];
        }
        if ((letters && Character.isLetter(ch)) || (numbers && Character.isDigit(ch)) || (!letters && !numbers)) {
            buffer[count] = ch;
        } else {
            count++; // Revert decrement to counter as character was not added
        }
    }
    return new String(buffer);
}
