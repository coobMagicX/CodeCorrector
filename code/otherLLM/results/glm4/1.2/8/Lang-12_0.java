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
        if (chars == null || chars.length == 0) { // Check for null or empty character array
            ch = (char) (random.nextInt(gap) + start);
        } else {
            ch = chars[random.nextInt(chars.length)];
        }

        if (letters && Character.isLetter(ch)
                || numbers && Character.isDigit(ch)
                || !letters && !numbers) {
            if (ch >= 56320 && ch <= 57343) { // low surrogate
                if (count == 0) {
                    count++;
                } else {
                    buffer[count] = ch;
                    count--;
                    buffer[count] = (char) (55296 + random.nextInt(128));
                }
            } else if (ch >= 55296 && ch <= 56191) { // high surrogate
                if (count == 0) {
                    count++;
                } else {
                    buffer[count] = (char) (56320 + random.nextInt(128));
                    count--;
                    buffer[count] = ch;
                }
            } else if (ch >= 56192 && ch <= 56319) { // private high surrogate
                continue; // Skip it as mentioned, do not increment or decrement count
            } else {
                buffer[count] = ch;
            }
        } else {
            count++; // This line is no longer needed and was incorrect logic
        }
    }

    return new String(buffer);
}