private boolean toStringEquals(Matcher m, Object arg) {
    if (m == null || arg == null) { // Handle null arguments
        return false;
    }
    
    try {
        String stringArg = arg.toString(); // Verify toString method behavior
        if (stringArg == null) { // Check for empty or null strings
            return false;
        }
        
        return StringDescription.toString(m).equals(stringArg);
    } catch (Exception e) { // Catch any exceptions that occur during the comparison
        return false;
    }
}