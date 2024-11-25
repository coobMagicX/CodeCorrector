public static String random(int count, int start, int end, boolean letters, boolean numbers,
                            char[] chars, Random random) {
    if (count == 0) {
        return "";
    } else if (count < 0) {
        throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
    }

    if (chars == null || chars.length == 0) {
        throw new IllegalArgumentException("Character array cannot be null or empty.");
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
        if (chars == null) { // This condition should never be true due to the earlier check.
            ch = (char) (random.nextInt(gap) + start);
        } else {
            int index = random.nextInt(chars.length);
            ch = chars[index];
        }
        // ... rest of the code remains unchanged
    }
    return new String(buffer);
}