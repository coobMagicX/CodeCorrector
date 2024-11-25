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
            ch = chars[random.nextInt(chars.length)];
        }
        
        // Check for letters and numbers to include in the string
        boolean validChar = (letters && Character.isLetter(ch)) ||
                             (numbers && Character.isDigit(ch)) ||
                             (!letters && !numbers);

        if (validChar) {
            if ((ch >= 0xD800 && ch <= 0xDBFF) || (ch >= 0xDC00 && ch <= 0xDFFF)) {
                // Handle surrogate pairs
                int countRemaining = count;
                
                if (countRemaining > 1) { // There is room to add both surrogates
                    buffer[count] = ch;
                    count--;
                    
                    // Generate the high or low surrogate based on current character position
                    boolean isHighSurrogate = (ch >= 0xD800 && ch <= 0xDBFF);
                    int baseSurrogateValue = isHighSurrogate ? 0xDC00 : 0xE000;
                    buffer[count] = (char) (baseSurrogateValue + random.nextInt(0x400 - baseSurrogateValue));
                } else {
                    count++;
                    // Throw an exception if we can't add a full surrogate pair
                    throw new IllegalArgumentException("Insufficient space to create a valid Unicode character.");
                }
            } else {
                buffer[count] = ch;
            }
        } else {
            count++;
            // Throw an exception for invalid characters
            throw new IllegalArgumentException("Invalid character range specified: start=" + start + ", end=" + end);
        }
    }

    return new String(buffer);
}