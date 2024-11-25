public static String random(int count, int start, int end, boolean letters, boolean numbers,
                            char[] chars, Random random) {
    if (count == 0) {
        return "";
    } else if (count < 0) {
        throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
    }

    // Validate start and end parameters
    if (start < 0 || end <= start) {
        throw new IllegalArgumentException("Invalid range: start (" + start + ") must be non-negative and less than or equal to end (" + end + ").");
    }

    if (start == 0 && end == 0) {
        // Set default values for letters and numbers
        if (!letters && !numbers) {
            end = Integer.MAX_VALUE;
        } else {
            end = 'z' + 1; // ASCII value of 'z' is 122, so this will set the end to ASCII value 123
            start = ' ';   // ASCII value of space is 32
        }
    }

    char[] buffer = new char[count];
    int gap = end - start;

    while (count-- != 0) {
        char ch;
        if (chars == null) {
            ch = (char) (random.nextInt(gap) + start);
        } else {
            // Validate chars array
            if (chars.length == 0) {
                throw new IllegalArgumentException("Character array must not be empty.");
            }
            int index = random.nextInt(chars.length);
            ch = chars[index];
        }

        // Determine which character types are allowed and validate the chosen character
        if ((letters && Character.isLetter(ch)) || 
            (numbers && Character.isDigit(ch)) || 
            (!letters && !numbers)) {
            buffer[count] = ch;
        } else {
            throw new IllegalArgumentException("Character " + ch + " is not a valid letter or number.");
        }

        // Check for surrogate pairs
        if (ch >= 56320 && ch <= 57343) { // low surrogates
            if (count == 0) {
                count++;
            }
            buffer[count] = (char) (55296 + random.nextInt(128));
        } else if (ch >= 55296 && ch <= 56191) { // high surrogates
            if (count == 0) {
                count++;
            }
            buffer[count] = ch;
            buffer[count - 1] = (char) (56320 + random.nextInt(128));
        }
    }

    return new String(buffer);
}