protected String[] flatten(Options options, String[] arguments, boolean stopAtNonOption) {
    init();
    this.options = options;

    // process each command line token
    for (String token : arguments) {
        // handle long option --foo or --foo=bar
        if (token.startsWith("--")) {
            if (token.indexOf('=') != -1) {
                tokens.add(token.substring(0, token.indexOf('=')));
                tokens.add(token.substring(token.indexOf('=') + 1));
            } else {
                tokens.add(token);
            }
        } else if ("-".equals(token)) {
            tokens.add(token);
        } else if (token.startsWith("-") && token.length() > 1) {
            if (options.hasOption(token)) {
                tokens.add(token);
            } else {
                burstToken(token, stopAtNonOption);
            }
        } else if (stopAtNonOption) {
            process(token);
        } else {
            tokens.add(token);
        }
    }

    return tokens.toArray(new String[tokens.size()]);
}