public static Number createNumber(String val) throws NumberFormatException {
    if (val == null || val.isEmpty()) {
        throw new NumberFormatException("Input string is empty or null.");
    }
    // ... rest of the code remains the same ...
}