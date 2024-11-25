static void assertPrint(String expected, String actual) {
    if (!expected.equals(actual)) {
        throw new AssertionError("Expected: '" + expected + "' but was: '" + actual + "'");
    }
}