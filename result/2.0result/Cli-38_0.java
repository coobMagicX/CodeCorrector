private boolean isShortOption(String token) {
    if (!token.startsWith("-") || token.length() == 1) {
        return false;
    }

    // Handling multiple concatenated short options like '-abc'
    for (int i = 1; i < token.length(); i++) {
        String opt = String.valueOf(token.charAt(i));
        
        // Check if each character after '-' is a valid short option
        if (!options.hasShortOption(opt)) {
            // If any character fails to be a valid option, check if the substring from this point
            // should be treated as an argument rather than part of the option
            if (i > 1) {
                return true; // Previous characters were valid options, this might be an argument
            }
            return false; // No valid options, and current character isn't starting a valid option
        }
    }

    return true;
}