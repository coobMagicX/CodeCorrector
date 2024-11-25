public static String random(int count, int start, int end, boolean letters, boolean numbers,
                            char[] chars, Random random) {
    if (count < 0) {
        throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
    }

    if (count == 0) {
        return "";
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
            ch = chars[random.nextInt(chars.length)];
        }

        if (letters && Character.isLetter(ch)
                || numbers && Character.isDigit(ch)
                || (!letters && !numbers)) {

            if ((ch >= 0xD800 && ch <= 0xDBFF) || (ch >= 0xDC00 && ch <= 0xDFFF)) {
                // Handle surrogates
                int codePoint = ch;
                if (count == 1) {
                    buffer[count] = ch;
                } else {
                    count--;
                    codePoint -= ((ch >= 0xD800 && ch < 0xDC00) ? 0x0300 : 0);
                    buffer[count] = (char) codePoint;
                    ch = (char) ((codePoint & 0x03FF) + 0x010000);
                }
            }

            if (ch >= start && ch <= end) {
                buffer[count] = ch;
            } else {
                count++;
            }
        } else {
            count++;
        }
    }

    return new String(buffer);
}