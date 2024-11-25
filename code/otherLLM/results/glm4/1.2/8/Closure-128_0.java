public static boolean assertPrint(String expected, Appendable actual) throws IOException {
    if (!expected.equals(actual.toString())) {
        System.err.println("Expected: " + expected);
        System.err.println("Actual:   " + actual.toString());
        return false;
    }
    return true;
}

// Example usage of the fixed assertPrint method:
// boolean success = assertPrint("\\{\\}", new StringBuilder("{}}"));
// assert success : "String formatting failed.";