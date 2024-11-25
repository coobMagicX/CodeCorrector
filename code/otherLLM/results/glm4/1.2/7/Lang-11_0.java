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

    // Validate start and end parameters
    if (start < 0 || end < 0) {
        throw new IllegalArgumentException("Start and end values must be non-negative.");
    }
    if (start > end) {
        throw new IllegalArgumentException("Start value " + start + " is greater than end value " + end + ".");
    }

    // Set the end parameter based on available characters
    if (start == 0 && end == 0) {
        if (chars != null) {
            end = chars.length;
        } else {
            if (!letters && !numbers) {
                end = Integer.MAX_VALUE;
            } else {
                end = 'z' + 1;
                start = ' ';
            }
        }
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
        
        if ((letters && Character.isLetter(ch)) || (numbers && Character.isDigit(ch))) {
            buffer[count] = ch;
        } else if (!letters && !numbers) {
            buffer[count] = ch;
        } else {
            count++;
        }
    }
    return new String(buffer);
}