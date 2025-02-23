public static String random(int count, int start, int end, boolean letters, boolean numbers,
                            char[] chars, Random random) {
    if (count == 0) {
        return "";
    }
    if (count < 0) {
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
                throw new IllegalArgumentException("Both letters and numbers cannot be false when start and end are both zero.");
            }
            start = ' ';
            end = 'z' + 1;
        }
    }

    if (end <= start) {
        throw new IllegalArgumentException("The value of end (" + end + ") must be greater than the value of start (" + start + ")");
    }
    
    int gap = end - start;
    if (gap <= 0) {
        throw new IllegalArgumentException("Difference between end and start must be positive.");
    }

    char[] buffer = new char[count];

    while (count-- != 0) {
        char ch;
        if (chars == null) {
            ch = (char) (random.nextInt(gap) + start);
        } else {
            ch = chars[random.nextInt(chars.length)];
        }
        
        if ((letters && Character.isLetter(ch)) || (numbers && Character.isDigit(ch)) || (!letters && !numbers)) {
            buffer[count] = ch;
        } else {
            count++; // Revert the decrement to make sure right number of chars is generated
        }
    }

    return new String(buffer);
}
