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

    // Ensure 'start' and 'end' are within valid range
    if (start < 0 || end <= start) {
        throw new IllegalArgumentException("Invalid 'start' and/or 'end' parameters: start=" + start + ", end=" + end);
    }

    // Assigning default values for 'start' and 'end'
    if (start == 0 && end == 0) {
        if (chars != null) {
            end = chars.length;
        } else {
            end = letters || numbers ? 'z' : Integer.MAX_VALUE;
            start = letters || numbers ? ' ' : 0;
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
        // Simplified condition to select characters
        if ((letters && Character.isLetter(ch)) || (numbers && Character.isDigit(ch))) {
            buffer[count] = ch;
        } else if (!letters && !numbers) {
            buffer[count] = ch;
        } else {
            count++; // Skip non-qualifying characters, and increment 'count' to not decrement it below 0
        }
    }
    return new String(buffer);
}