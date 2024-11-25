public static String random(int count, int start, int end, boolean letters, boolean numbers,
                            char[] chars, Random random) {
    if (count == 0) {
        return "";
    } else if (count < 0) {
        throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
    }

    if (start == 0 && end == 0) {
            if (!letters && !numbers) {
                end = Integer.MAX_VALUE;
            } else {
                end = 'z' + 1;
                start = ' ';                
            }
    }

    char[] buffer = new char[count];
    int gap = end - start;

    while (count-- != 0) {
        char ch;
        if (chars == null) {
            ch = (char) (random.nextInt(gap) + start);
        } else {
            ch = chars[random.nextInt(gap) + start];
        }
        
        // Validate input parameters more thoroughly
        if (!letters && !numbers && (start < 55296 || end > 57343)) {
            throw new IllegalArgumentException("Invalid range for random string generation: " +
                    "[" + start + ", " + end + ").");
        }

        if (letters && Character.isLetter(ch)
                || numbers && Character.isDigit(ch)
                || !letters && !numbers) {
            buffer[count] = ch;
        } else {
            count++; // increment count only when the generated character is not valid
        }
    }
    return new String(buffer);
}