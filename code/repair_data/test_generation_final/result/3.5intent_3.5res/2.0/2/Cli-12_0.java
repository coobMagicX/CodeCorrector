protected String[] flatten(Options options, String[] arguments, boolean stopAtNonOption) {
    List<String> tokens = new ArrayList<>();

    boolean eatTheRest = false;

    for (int i = 0; i < arguments.length; i++) {
        String arg = arguments[i];

        if ("--".equals(arg)) {
            eatTheRest = true;
            tokens.add("--");
        } else if ("-".equals(arg)) {
            tokens.add("-");
        } else if (arg.startsWith("-")) {
            String opt = Util.stripLeadingHyphens(arg);

            if (options.hasOption(opt)) {
                tokens.add(arg);
            } else {
                if (options.hasOption(arg.substring(0, 2))) {
                    // the format is --foo=value or -foo=value
                    // the format is a special properties option (-Dproperty=value)
                    tokens.add(arg.substring(0, 2)); // -D
                    tokens.add(arg.substring(2)); // property=value
                } else {
                    eatTheRest = stopAtNonOption;
                    tokens.add(arg);
                }
            }
        } else {
            tokens.add(arg);
        }
    }

    if (eatTheRest) {
        for (int i = i + 1; i < arguments.length; i++) {
            tokens.add(arguments[i]);
        }
    }

    return tokens.toArray(new String[tokens.size()]);
}