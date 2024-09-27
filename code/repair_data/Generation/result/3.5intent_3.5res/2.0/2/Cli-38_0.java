private boolean isShortOption(String token) {
    // short options (-S, -SV, -S=V, -SV1=V2, -S1S2)
    if (!token.startsWith("-") || token.length() == 1) {
        return false;
    }

    // remove leading "-" and "=value"
    int pos = token.indexOf("=");
    String optName = pos == -1 ? token.substring(1) : token.substring(1, pos);

    if (!options.hasShortOption(optName)) {
        return false;
    }

    // check for several concatenated short options
    handleConcatenatedOptions(token);

    return true;
}

protected void handleConcatenatedOptions(String token) throws ParseException {
    for (int i = 1; i < token.length(); i++) {
        String ch = String.valueOf(token.charAt(i));

        if (options.hasOption(ch)) {
            handleOption(options.getOption(ch));

            if (currentOption != null && token.length() != i + 1) {
                // add the trail as an argument of the option
                currentOption.addValueForProcessing(token.substring(i + 1));
                break;
            }
        } else {
            handleUnknownToken(stopAtNonOption && i > 1 ? token.substring(i) : token);
            break;
        }
    }
}