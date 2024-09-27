protected String[] flatten(Options options, String[] arguments, boolean stopAtNonOption) {
    init();
    this.options = options;

    // an iterator for the command line tokens
    Iterator<String> iter = Arrays.asList(arguments).iterator();

    // process each command line token
    while (iter.hasNext()) {
        // get the next command line token
        String token = iter.next();

        // handle long option --foo or --foo=bar
        if (token.startsWith("--")) {
            if (token.indexOf('=') != -1) {
                tokens.add(token.substring(0, token.indexOf('=')));
                tokens.add(token.substring(token.indexOf('=') + 1, token.length()));
            } else {
                tokens.add(token);
            }
        }
        // single hyphen
        else if ("-".equals(token)) {
            tokens.add(token);
        }
        // handle short option -f or -fvalue
        else if (token.startsWith("-")) {
            if (token.length() == 2) {
                processOptionToken(token, stopAtNonOption);
            } else if (options.hasOption(token.substring(0, 2))) {
                tokens.add(token.substring(0, 2));
                tokens.add(token.substring(2));
            }
            // requires bursting
            else {
                burstToken(token, stopAtNonOption);
            }
        }
        // handle non-option argument
        else if (stopAtNonOption) {
            process(token);
        } else {
            tokens.add(token);
        }

        gobble(iter);
    }

    return tokens.toArray(new String[tokens.size()]);
}