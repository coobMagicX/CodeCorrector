public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
    if (count == 0) {
        return "";
    } else if (count < 0) {
        throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
    }
    if (chars != null && chars.length == 0) {
        throw new IllegalArgumentException("The chars array must not be empty.");
    }

    if (start == 0 && end == 0) {
        if (chars != null) {
            end = chars.length;
        } else {
            if (!letters && !numbers) {
                throw new IllegalArgumentException("Need to specify 'letters' and/or 'numbers' or a non-empty 'chars' array.");
            } else {
                end = 'z' + 1;
                start = ' ';
            }
        }
    } else if (end <= start) {
        throw new IllegalArgumentException("Character range is invalid. End (" + end + ") must be greater than start (" + start + ").");
    }

    char[] buffer = new char[count];
    int gap = end - start;

    while (count-- != 0) {
        char ch;
        if (chars == null) {
            ch = (char) (random.nextInt(gap) + start);
        } else {
            ch = chars[random.nextInt(gap)];
        }
        if (letters && Character.isLetter(ch)
                || numbers && Character.isDigit(ch)
                || !letters && !numbers) {
            buffer[count] = ch;
        } else {
            count++;
        }
    }
    return new String(buffer);
}
