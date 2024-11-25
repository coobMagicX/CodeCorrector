public static String random(int count, int start, int end, boolean letters, boolean numbers,
                            char[] chars, Random random) {
    if (count == 0) {
        return "";
    }
    if (count < 0) {
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
        throw new IllegalArgumentException("Character array is empty or null.");
    }

    while (count-- != 0) {
        char ch;
        if (chars == null) {
            ch = (char) (random.nextInt(gap) + start);
        } else {
            ch = chars[random.nextInt(chars.length)];
        }
        // ... rest of the code remains the same
}