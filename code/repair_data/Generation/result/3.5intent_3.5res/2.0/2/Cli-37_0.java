protected void handleConcatenatedOptions(String token) throws ParseException {
    int i = 1;
    while (i < token.length()) {
        String ch = String.valueOf(token.charAt(i));

        if (options.hasOption(ch)) {
            handleOption(options.getOption(ch));

            if (currentOption != null && token.length() != i + 1 && token.charAt(i + 1) != '=') {
                // add the trail as an argument of the option
                currentOption.addValueForProcessing(token.substring(i + 1));
                break;
            }
        } else {
            handleUnknownToken(stopAtNonOption && i > 1 ? token.substring(i) : token);
            break;
        }

        i++;
    }
}