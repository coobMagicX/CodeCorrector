public static String random(int count, int start, int end, boolean letters, boolean numbers,
                            char[] chars, Random random) {
    if (count < 0) {
        throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
    }

    if ((letters || numbers) && (chars == null || chars.length == 0)) {
        throw new IllegalArgumentException("Characters array cannot be empty or null when generating a string with letters or numbers");
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
        // ... rest of the method remains the same
}