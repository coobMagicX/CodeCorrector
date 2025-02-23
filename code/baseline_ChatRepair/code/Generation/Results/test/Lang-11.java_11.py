public static String random(int count, int start, int end, boolean letters, boolean numbers,
                            char[] chars, Random random) {
    if (count == 0) {
        return "";
    }
    if (count < 0) {
        throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
    }
    if (chars != null && chars.length == 0) {
        throw new IllegalArgumentException("The chars array must not be empty");
    }

    if (start == 0 && end == 0) {
        if (chars != null) {
            end = chars.length;
        } else {
            end = 'z' + 1;  // Setting 'z' as the default upper bound
            start = ' ';    // Setting ' ' as the default lower bound
        }
    }

    if (end <= start) {
        throw new IllegalArgumentException("End (" + end + ") must be greater than start (" + start + ")");
    }
    
    int gap = end - start;

    char[] buffer = new char[count];
    while (count-- != 0) {
        char ch;
        if (chars != null) {
            ch = chars[random.nextInt(chars.length)];
        } else {
            ch = (char) (random.nextInt(gap) + start);
        }

        if ((letters && Character.isLetter(ch)) || (numbers && Character.isDigit(ch)) || (!letters && !numbers)) {
            buffer[count] = ch;
        } else {
            count++; // Revert decrement when character did not meet criteria 
        }
    }

    return new String(buffer);
}
