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
                throw new IllegalArgumentException("Both letters and numbers cannot be false if chars array is not provided.");
            } else {
                end = 'z' + 1;
                start = ' ';
            }
        }
    }

    if (end <= start) {
        throw new IllegalArgumentException("Invalid range: end must be greater than start.");
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
        if ((letters && Character.isLetter(ch)) || (numbers && Character.isDigit(ch)) || (!letters && !numbers)) {
            buffer[i] = ch;
        } else {
            i--; // adjust the index to retry this position with a new character
        }
    }

    return new String(buffer);
}
