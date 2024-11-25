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

    if (chars == null || chars.length == 0) {
        throw new IllegalArgumentException("Chars array is empty.");
    }

    while (count-- != 0) {
        char ch;
        if (chars.length > 0) {
            ch = chars[random.nextInt(gap + 1)];
        } else {
            if (start < end) {
                ch = (char) (random.nextInt(gap) + start);
            } else {
                throw new IllegalArgumentException("Invalid range for random character generation.");
            }
        }

        // ... rest of the method remains the same ...
    }
    return new String(buffer);
}