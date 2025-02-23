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

    if (end < start) {
        throw new IllegalArgumentException("Character range is invalid with start (" + start + 
                                           ") greater than end (" + end + "). Start and end must define a positive range.");
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
        if (letters && Character.isLetter(ch) || numbers && Character.isDigit(ch) || !letters && !numbers) {
            if (Character.isHighSurrogate(ch) && count > 0) {
                buffer[count] = ch;
                count--;
                buffer[count] = Character.isLowSurrogate(ch) ? ch : (char) (random.nextInt(gap) + start);
            } else if (!Character.isSurrogate(ch)) {
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
