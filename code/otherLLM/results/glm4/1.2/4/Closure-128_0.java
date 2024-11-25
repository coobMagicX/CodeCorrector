public class StringFormatter {

    // Existing method (correct)
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

    // Hypothetical method that formats a number as an object literal string
    static String formatAsObjectLiteral(int number) {
        if (!isSimpleNumber(Integer.toString(number))) {
            throw new IllegalArgumentException("Input is not a simple number.");
        }
        return "{number=" + number + "}";
    }

    // Hypothetical assertPrint method that asserts the formatted string matches an expected output
    static void assertPrint(String actual, String expected) {
        if (!actual.equals(expected)) {
            throw new AssertionError("Printed string does not match expected output: \nExpected:\t" + expected +
                                    "\nActual:\t\t" + actual);
        }
    }

    // Test method that uses the above methods
    public static void main(String[] args) {
        int numberToFormat = 123;
        String formattedString = formatAsObjectLiteral(numberToFormat);

        // The following line should pass without assertion errors
        assertPrint(formattedString, "{number=123}");
    }
}