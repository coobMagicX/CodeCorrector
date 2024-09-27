private boolean isShortOption(String token) {
    return token.startsWith("-") && token.length() >= 2 && 
           Character.isLetter(token.charAt(1)) && 
           options.hasShortOption(token.substring(1, 2)) &&
           (token.length() == 2 || token.charAt(2) == '=' || !Character.isLetter(token.charAt(2)));
}