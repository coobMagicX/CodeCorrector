public static Number createNumber(String val) throws NumberFormatException {
    if (val == null || !val.getClass().equals(String.class)) {
        throw new NumberFormatException("Input must be a string");
    }
    if (val.length() == 0) {
        throw new NumberFormatException("\"\" is not a valid number.");
    }
    // ... rest of the code remains the same ...
}