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

    int i = 0; // buffer index
    while (i < count) {
        char ch;
        if (chars == null) {
            ch = (char) (random.nextInt(gap) + start);
        } else {
            ch = chars[random.nextInt(chars.length)];
        }
        if (letters && Character.isLetter(ch)
                || numbers && Character.isDigit(ch)
                || !letters && !numbers) {
            if (ch >= 0xD800 && ch <= 0xDBFF) { // High surrogate
                if (i == count - 1) {
                    // Not enough space for a surrogate pair
                    continue;
                } else {
                    buffer[i++] = ch; // High surrogate
                    buffer[i++] = (char) (0xDC00 + random.nextInt(128)); // Low surrogate
                }
            } else if (ch >= 0xDC00 && ch <= 0xDFFF) { // Low surrogate
                // Ignore lone low surrogates
                continue;
            } else {
                buffer[i++] = ch;
            }
        }
    }
    return new String(buffer);
}