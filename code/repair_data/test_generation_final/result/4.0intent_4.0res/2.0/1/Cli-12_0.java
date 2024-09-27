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
            if (arg.contains("=")) {
                String[] parts = arg.split("=", 2);
                String opt = Util.stripLeadingHyphens(parts[0]);
                if (options.hasOption(opt)) {
                    tokens.add(parts[0]);
                    tokens.add(parts[1]);
                } else {
                    tokens.add(arg);
                }
            } else {
                String opt = Util.stripLeadingHyphens(arg);
                if (options.hasOption(opt)) {
                    tokens.add(arg);
                } else {
                    if (arg.length() > 2 && options.hasOption(arg.substring(0, 2))) {
                        tokens.add(arg.substring(0, 2));
                        tokens.add(arg.substring(2));
                    } else {
                        tokens.add(arg);
                    }
                }
            }
        } else {
            tokens.add(arg);
        }

        if (eatTheRest) {
            for (i++; i < arguments.length; i++) {
                tokens.add(arguments[i]);
            }
        }
    }

    return tokens.toArray(new String[0]);
}