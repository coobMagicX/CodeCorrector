protected String[] flatten(Options options, String[] arguments, boolean stopAtNonOption) {
    init();
    this.options = options;

    // an iterator for the command line tokens
    Iterator iter = Arrays.asList(arguments).iterator();

    // process each command line token
    while (iter.hasNext()) {
        // get the next command line token
        String token = (String) iter.next();

        if (stopAtNonOption && eatTheRest) {
            tokens.add(token);
            continue;
        }

        // handle long option --foo or --foo=bar
        if (token.startsWith("--")) {
            int equalsIndex = token.indexOf('=');
            if (equalsIndex != -1) {
                tokens.add(token.substring(0, equalsIndex));
                tokens.add(token.substring(equalsIndex + 1));
            } else {
                tokens.add(token);
            }
        }
        // single hyphen
        else if ("-".equals(token)) {
            tokens.add(token);
        }
        else if (token.startsWith("-")) {
            if (token.length() == 2) {
                processOptionToken(token, stopAtNonOption);
            } else if (options.hasOption(token)) {
                tokens.add(token);
            }
            // requires bursting
            else {
                burstToken(token, stopAtNonOption);
            }
        }
        else {
            if (stopAtNonOption) {
                tokens.add(token);
                eatTheRest = true;
            } else {
                tokens.add(token);
            }
        }
    }

    return (String[]) tokens.toArray(new String[tokens.size()]);
}

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
            tokens.add(token.substring(i));
            eatTheRest = true;
            break;
        } else {
            tokens.add(token);
            break;
        }
    }
}

private void processOptionToken(String token, boolean stopAtNonOption) {
    if (options.hasOption(token)) {
        currentOption = options.getOption(token);
    } else if (stopAtNonOption) {
        eatTheRest = true;
    }

    tokens.add(token);
}