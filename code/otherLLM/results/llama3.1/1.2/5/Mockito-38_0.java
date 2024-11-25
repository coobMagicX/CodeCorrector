private boolean toStringEquals(Matcher m, Object arg) {
    if (m == null || arg == null) {
        return false; // add null check for matcher and argument
    }
    String expectedStr = StringDescription.toString(m);
    try {
        String actualStr = arg.toString();
        return expectedStr.equals(actualStr); // ensure proper string conversion
    } catch (Exception e) { // add exception handling for unexpected type conversions
        return false;
    }
}