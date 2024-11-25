public class StringFormatter {

    static boolean isSimpleNumber(String s) {
        int len = s.length();
        for (int index = 0; index < len; index++) {
            char c = s.charAt(index);
            if (c < '0' || c > '9') {
                return false;
            }
        }
        return len > 0 && s.charAt(0) != '0';
    }

    static void assertPrint(String input, boolean expectedOutput) {
        // Assuming the method is supposed to print something based on the expected output
        System.out.println("The string \"" + input + "\" is a simple number: " + (isSimpleNumber(input) ? "Yes" : "No"));
    }

    public static void main(String[] args) {
        // Example usage
        assertPrint("123", true); // Should print: The string "123" is a simple number: Yes
        assertPrint("0123", false); // Should print: The string "0123" is a simple number: No
        assertPrint("abc", false); // Should print: The string "abc" is a simple number: No
    }
}