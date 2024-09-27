public static String random(int count, int start, int end, boolean letters, boolean numbers,
                            char[] chars, Random random) {
    if (count == 0) {
        return "";
    } else if (count < 0) {
        throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
    }
    if (chars != null && chars.length == 0) {
        throw new IllegalArgumentException("The chars array must not be empty");
    }

    if (start == 0 && end == 0) {
        if (chars != null) {
            end = chars.length;
        } else {
            if (!letters && !numbers) {
                end = Integer.MAX_VALUE;
            } else {
                end = 'z' + 1;
                start = ' ';
            }
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

        if ((letters && Character.isLetter(ch)) || (numbers && Character.isDigit(ch)) || (!letters && !numbers)) {
            if (Character.isHighSurrogate(ch) || Character.isLowSurrogate(ch)) {
                if (i + 1 < count) { // ensure there is room for a surrogate pair
                    char nextCh = (char) (random.nextInt(gap) + start);
                    if (Character.isLowSurrogate(nextCh)) {
                        buffer[i++] = ch;
                        buffer[i++] = nextCh;
                    } else {
                        // skip or try to generate again
                        continue;
                    }
                } else {
                    // not enough space for a surrogate pair, skip or decrement i
                    continue;
                }
            } else {
                buffer[i++] = ch;
            }
        }
    }
    return new String(buffer, 0, i);
}