import java.util.Random;

public class RandomStringGenerator {

    public static String random(int count, int start, int end, boolean letters, boolean numbers,
                                char[] chars, Random random) {
        if (count == 0) {
            return "";
        } else if (count < 0) {
            throw new IllegalArgumentException("Requested random string length " + count + " is less than 0.");
        }

        // Check for empty array and throw IllegalArgumentException if not null but empty
        if (chars != null && chars.length == 0) {
            throw new IllegalArgumentException("The char array provided cannot be empty.");
        }

        // Set start and end values based on the flags
        if (start == 0 && end == 0) {
            if (!letters && !numbers) {
                end = Integer.MAX_VALUE;
            } else {
                end = 'z' + 1; // Assuming ASCII for letters, adjust as needed
                start = ' ';   // Assuming ASCII for letters, adjust as needed
            }
        }

        char[] buffer = new char[count];
        int gap = Math.max(0, end - start); // Ensure the gap is not negative

        while (count-- != 0) {
            char ch;
            if (chars == null) {
                ch = (char) (random.nextInt(gap) + start);
            } else {
                ch = chars[random.nextInt(chars.length)];
            }
            
            // Check for valid characters based on flags
            if ((letters && Character.isLetter(ch)) || 
                (numbers && Character.isDigit(ch)) || 
                (!letters && !numbers)) {
                buffer[count] = ch;
            } else if ((ch >= 56320 && ch <= 57343) || // low surrogate check
                       (ch >= 55296 && ch <= 56191)) {   // high surrogate check
                throw new IllegalArgumentException("Input characters cannot be surrogates: " + ch);
            } else {
                count++; // If the character is invalid, increment count and replace the value later
            }
        }

        // Correct any skipped values due to invalid characters
        for (int i = 0; i < count; i++) {
            if (buffer[i] == 0) {
                // Fill with a default character or throw an exception if no valid character is found
                buffer[i] = start; // Assuming 'start' is a valid character, adjust as needed
            }
        }

        return new String(buffer);
    }

    public static void main(String[] args) {
        Random random = new Random();
        try {
            String result = random(10, 0, 0, true, false, null, random);
            System.out.println(result);
        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}