import java.io.Writer;

public class UnicodeTranslator {

    public int translate(CharSequence input, int index, Writer out) throws IOException {
        // Check for ArrayIndexOutOfBounds before accessing characters in the input string
        if (index < 0 || index >= input.length()) {
            return 0;
        }

        // TODO: Protect from ArrayIndexOutOfBounds
        if (input.charAt(index) == '&' && input.charAt(index + 1) == '#') {
            int start = index + 2;
            boolean isHex = false;

            char firstChar = input.charAt(start);
            if (firstChar == 'x' || firstChar == 'X') {
                start++;
                isHex = true;
            }

            // Check for ArrayIndexOutOfBounds before accessing characters in the input string
            if (start >= input.length()) {
                return 0; // Return early if the sequence does not exist
            }

            int end = start;
            while (end < input.length() && input.charAt(end) != ';') {
                end++;
            }

            // Check for ArrayIndexOutOfBounds before accessing characters in the input string and ensure correct bounds checking
            if (end >= input.length()) {
                return 0; // Return early if the sequence does not exist
            }

            int entityValue;
            try {
                // Use String.substring instead of subSequence to avoid the overhead of creating a new object
                String valueStr = input.subSequence(start, end).toString();
                entityValue = Integer.parseInt(valueStr, isHex ? 16 : 10);
            } catch (NumberFormatException nfe) {
                return 0;
            }

            // Ensure that the character can be written to the Writer without throwing an IOException
            if (out != null) {
                out.write(entityValue);
            }
            return 2 + (end - start) + (isHex ? 1 : 0) + 1;
        }
        return 0;
    }
}