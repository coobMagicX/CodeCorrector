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
    int i = 0;

    while (i < count) {
        char ch;
        if (chars == null) {
            ch = (char) (random.nextInt(gap) + start);
        } else {
            ch = chars[random.nextInt(chars.length)];
        }

        if ((letters && Character.isLetter(ch)) ||
            (numbers && Character.isDigit(ch)) ||
            (!letters && !numbers)) {
            if (ch >= 56320 && ch <= 57343) {
                if (i == count - 1) {
                    continue;
                } else {
                    // Low surrogate, insert high surrogate after putting it in
                    buffer[i++] = ch;
                    buffer[i] = (char) (55296 + random.nextInt(128));
                }
            } else if (ch >= 55296 && ch <= 56191) {
                if (i == count - 1) {
                    continue;
                } else {
                    // High surrogate, insert low surrogate before putting it in
                    buffer[i++] = (char) (56320 + random.nextInt(128));
                    buffer[i] = ch;
                }
            } else if (ch >= 56192 && ch <= 56319) {
                // Private high surrogate zone, skip it
                continue;
            } else {
                buffer[i] = ch;
            }
            i++;
        }
    }
    return new String(buffer);
}