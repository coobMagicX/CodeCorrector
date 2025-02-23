public static String random(int count, int start, int end, boolean letters, boolean numbers,
                            char[] chars, Random random) {
    if (count == 0) {
        return "";
    } else if (count < 0) {
        throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
    }
    if (chars != null && chars.length == 0) {
        throw new IllegalArgumentException("The chars array must not be empty.");
    }

    if (start >= end) {
        throw new IllegalArgumentException("Start index (" + start + ") must be less than end index (" + end + ").");
    }

    if (start == 0 && end == 0) {
        if (chars != null) {
            end = chars.length;
        } else {
            if (!letters && !numbers) {
                throw new IllegalArgumentException("Character selection range is indeterminate (start = 0, end = 0) without specification of letters or numbers.");
            } else {
                end = 'z' + 1; // 'z' + 1 to include 'z'
                start = ' ';   // ' ' (space) to start from a common printable character
            }
        }
    }

    char[] buffer = new char[count];
    int gap = end - start;

    for (int i = 0; i < count; i++) {
        char ch;
        if (chars == null) {
            ch = (char) (random.nextInt(gap) + start);
        } else {
            ch = chars[random.nextInt(chars.length)];
        }
        if (letters && Character.isLetter(ch)
                || numbers && Character.isDigit(ch)
                || !letters && !numbers) {
            buffer[i] = ch;
        } else {
            i--; // decrement i to retry this position with a new character
        }
    }
    return new String(buffer);
}
