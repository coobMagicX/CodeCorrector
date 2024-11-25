public static String random(int count, int start, int end, boolean letters, boolean numbers,
                            char[] chars, Random random) {
    if (count == 0 || (start > end)) { // Check for invalid count or range
        throw new IllegalArgumentException("Invalid input parameters. 'count' must be positive and 'start' must be less than or equal to 'end'.");
    }
    
    if ((letters && !Character.isLetter(start)) ||
        (numbers && !Character.isDigit(start))) {
        throw new IllegalArgumentException("Invalid start value for the range specified.");
    }

    if (!letters && !numbers && chars == null) { // Check for valid non-empty array or valid parameters
        throw new IllegalArgumentException("No characters to choose from. 'chars' must not be null when both 'letters' and 'numbers' are false.");
    }

    char[] buffer = new char[count];
    int gap = end - start;

    while (count-- != 0) {
        char ch;
        if (chars == null) { // Use default character set if chars array is null
            ch = (char) (random.nextInt(gap) + start);
        } else {
            ch = chars[random.nextInt(chars.length)];
        }
        
        if ((letters && Character.isLetter(ch)) || 
            (numbers && Character.isDigit(ch))) { // Check for valid character based on flags
            buffer[count] = ch;
        } else if (!letters && !numbers) {
            throw new IllegalArgumentException("No valid characters in provided array when 'letters' and 'numbers' are both false.");
        }
    }
    return new String(buffer);
}