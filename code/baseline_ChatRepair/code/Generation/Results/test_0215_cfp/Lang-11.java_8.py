import java.util.Random;

public static String random(int count, int start, int end, boolean letters, boolean numbers, char[] chars, Random random) {
    if (count == 0) {
        return "";
    }
    if (count < 0) {
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
                start = 32; // start of printable ASCII characters
                end = 127; // end of printable ASCII characters
            } else {
                end = 'z' + 1; // 1 past last letter in ASCII
                start = ' '; // first printable ASCII character
            }
        }
    }

    if (end <= start) {
        throw new IllegalArgumentException("Invalid range: end must be greater than start.");
    }

    char[] buffer = new char[count];
    int gap = end - start;
    if (random == null) {
        random = new Random(); // use a default Random if no Random is provided
    }

    for (int i = 0; i < count; i++) {
        char ch;
        if (chars == null) {
            ch = (char) (random.nextInt(gap) + start);
        } else {
            ch = chars[random.nextInt(chars.length)];
        }
        
        boolean validChar = (letters && Character.isLetter(ch)) || 
                            (numbers && Character.isDigit(ch)) || 
                            (!letters && !numbers);

        if (validChar) {
            buffer[i] = ch;
        } else {
            i--; // Decrement i to retry this position
        }
    }

    return new String(buffer);
}
