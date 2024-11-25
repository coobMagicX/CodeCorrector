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
            int equalsIndex = opt.indexOf('=');
            String optionKey = (equalsIndex != -1) ? opt.substring(0, equalsIndex) : opt;
            String optionValue = (equalsIndex != -1) ? opt.substring(equalsIndex + 1) : null;

            if (options.hasOption(optionKey)) {
                if (optionValue != null) {
                    tokens.add("-" + optionKey);
                    tokens.add(optionValue);
                } else {
                    tokens.add(arg);
                }
            } else {
                if (arg.length() > 2 && options.hasOption(arg.substring(0, 2))) {
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

        if (eatTheRest) {
            for (i++; i < arguments.length; i++) {
                tokens.add(arguments[i]);
            }
        }
    }

    return tokens.toArray(new String[0]);
}