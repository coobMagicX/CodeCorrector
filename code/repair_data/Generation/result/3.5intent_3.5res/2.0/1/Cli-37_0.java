private boolean isShortOption(String token) {
    // short options (-S, -SV, -S=V, -SV1=V2, -S1S2)
    if (token.startsWith("-") && token.length() >= 2 && options.hasShortOption(token.substring(1, 2))) {
        // Check if the token has a value
        if (token.length() > 2 && token.charAt(2) == '=') {
            return true;
        } else {
            // Handle case where token does not have a value
            String shortOption = token.substring(1);
            if (shortOption.length() > 1) {
                return false;
            }
            return !options.hasShortOption(shortOption);
        }
    }
    return false;
}