private boolean isShortOption(String token) {
    // short options (-S, -SV, -S=V, -SV1=V2, -S1S2)
    if (!token.startsWith("-") || token.length() == 1) {
        return false;
    }

    // Check if token has concatenated short options or a value attached
    if (token.matches("-[a-zA-Z]+[0-9]*=?[0-9]*")) {
        for (int i = 1; i < token.length(); i++) {
            char ch = token.charAt(i);
            // Check if the character is a digit and the previous character is a letter (indicating a value start)
            if (Character.isDigit(ch) && i > 1 && Character.isLetter(token.charAt(i - 1))) {
                String optName = token.substring(1, i);
                String value = token.substring(i);
                // Check if the option part is valid
                if (options.hasShortOption(optName)) {
                    handleOption(options.getOption(optName));
                    if (currentOption != null) {
                        // Treat the rest as the value of the option
                        currentOption.addValueForProcessing(value);
                    }
                    return true;
                }
            } else if (!Character.isLetter(ch)) {
                // If it's not a letter, it can no longer be a valid concatenated short option
                break;
            }
        }
    }

    // Remove leading "-" and "=value"
    int pos = token.indexOf("=");
    String optName = pos == -1 ? token.substring(1) : token.substring(1, pos);

    // Check for several concatenated short options
    if (optName.length() > 1 && optName.matches("[a-zA-Z]+")) {
        handleConcatenatedOptions(token);
        return true;
    }

    return options.hasShortOption(optName);
}