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

    while (count-- != 0) {
        char ch;
        if (chars == null) {
            ch = (char) (random.nextInt(gap) + start);
        } else {
            ch = chars[random.nextInt(gap) + start];
        }
        if (letters && Character.isLetter(ch)
                || numbers && Character.isDigit(ch)
                || !letters && !numbers) {
            if (Character.isHighSurrogate(ch)) {
                if(count == 0) {
                    count++;
                } else {
                    // low surrogate, insert high surrogate after putting it in
                    buffer[count] = ch;
                    count--;
                    buffer[count] = Character.toChars(0xD800 + random.nextInt(0x800))[0];
                }
            } else if (Character.isLowSurrogate(ch)) {
                if(count == 0) {
                    count++;
                } else {
                    // high surrogate, insert low surrogate before putting it in
                    buffer[count] = Character.toChars(0xDC00 + random.nextInt(0x800))[0];
                    count--;
                    buffer[count] = ch;
                }
            } else if (Character.isSurrogate(ch)) {
                // Skip surrogate characters
                count++;
            } else {
                buffer[count] = ch;
            }
        } else {
            count++;
        }
    }
    return new String(buffer);
}