protected void burstToken(String token, boolean stopAtNonOption) {
    for (int i = 1; i < token.length(); i++) {
        String ch = String.valueOf(token.charAt(i));

        if (options.hasOption(ch)) {
            tokens.add("-" + ch);
            currentOption = options.getOption(ch);

            if (currentOption.hasArg() && (token.length() != (i + 1))) {
                tokens.add(token.substring(i + 1));
                break;
            }
        } else if (stopAtNonOption) {
            tokens.add("-" + token.substring(i - 1)); // Include the dash and the entire substring starting from the first undefined option
            break; // Ensure to stop processing further characters once a non-option is found
        } else {
            tokens.add(token);
            break;
        }
    }
}