private boolean toStringEquals(Matcher m, Object arg) {
    // Check if either argument is null
    if (m == null || arg == null) {
        return false;
    }
    
    // Perform the comparison
    return StringDescription.toString(m).equals(arg.toString());
}