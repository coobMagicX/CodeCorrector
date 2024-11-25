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
        if (chars != null) {
            end = chars.length;
        } else {
            if (!letters && !numbers) {
                end = Integer.MAX_VALUE;
            } else {
                end = 'z' + 1; // Assuming ASCII range
                start = ' ';    // Assuming ASCII range for the smallest character
            }
        }
    }

    if (start > end || (chars != null && start >= chars.length)) {
        throw new IllegalArgumentException("Start value " + start + " is greater than or equal to end value " + end +
                                            ", or start is beyond the length of the char array");
    }

    char[] buffer = new char[count];
    int gap = end - start;

    while (count-- != 0) {
        char ch;
        if (chars == null) {
            ch = (char) (random.nextInt(gap) + start);
        } else {
            ch = chars[random.nextInt(chars.length)];
        }
        if ((letters && Character.isLetter(ch)) || 
            (numbers && Character.isDigit(ch)) ||
            (!letters && !numbers)) {
            buffer[count] = ch;
        } else {
            count++; // Increment count to avoid underflow in loop
        }
    }
    return new String(buffer);
}