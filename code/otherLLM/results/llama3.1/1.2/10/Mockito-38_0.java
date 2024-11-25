private boolean toStringEquals(Matcher m, Object arg) {
    if (arg == null) { // Handle null arguments
        return safelyMatches(m, null); // Pass null to safelyMatches method
    } else {
        try {
            String expectedString = StringDescription.toString(m);
            Class<?> argClass = arg.getClass();
            Class<?> matcherClass = m.getClass(); // Check for mismatched types

            if (!argClass.equals(matcherClass)) { // Verify string comparison logic and type match
                return false;
            }

            return safelyMatches(m, arg); // Use safelyMatches to avoid potential exceptions
        } catch (Throwable t) {
            return false; // Return false on any exception
        }
    }
}