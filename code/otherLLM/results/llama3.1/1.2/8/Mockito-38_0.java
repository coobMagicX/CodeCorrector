private boolean toStringEquals(Matcher m, Object arg) {
    if (arg == null) { // Handle null argument
        return safelyMatches(m, null); // Compare with null instead of calling .toString() on it
    }
    String description = StringDescription.toString(m);
    String argString = arg.toString();
    int[] indexes = getSuspiciouslyNotMatchingArgsIndexes(description, argString);
    
    if (indexes.length == 1) { // Verify return type
        return safelyMatches(m, arg); // Return a boolean value instead of an array
    } else {
        int matchesCount = 0;
        for (int index : indexes) {
            if (description.charAt(index) == argString.charAt(index)) {
                matchesCount++;
            }
        }
        
        return matchesCount == description.length();
    }
}