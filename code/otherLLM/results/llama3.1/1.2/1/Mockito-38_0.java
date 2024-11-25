private boolean toStringEquals(Matcher m, Object arg) {
    if (m == null || arg == null) {
        return false; // Return false for null inputs
    }
    return StringDescription.toString(m).equals(arg.toString());
}