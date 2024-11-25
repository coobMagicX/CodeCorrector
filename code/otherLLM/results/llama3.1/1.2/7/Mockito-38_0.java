private boolean toStringEquals(Matcher m, Object arg) {
    if (arg == null) { // Input Validation: Null check for the 'arg' object
        return false; // Handle null argument by returning false
    }
    return StringDescription.toString(m).equals(arg.toString());
}