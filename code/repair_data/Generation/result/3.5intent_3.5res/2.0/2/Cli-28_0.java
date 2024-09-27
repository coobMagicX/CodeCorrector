protected void processProperties(Properties properties) {
    if (properties == null) {
        return;
    }

    for (Enumeration e = properties.propertyNames(); e.hasMoreElements();) {
        String option = e.nextElement().toString();

        if (!cmd.hasOption(option)) {
            Option opt = getOptions().getOption(option);

            // get the value from the properties instance
            String value = properties.getProperty(option);

            if (opt.hasArg()) {
                if (opt.getValues() == null || opt.getValues().length == 0) {
                    try {
                        opt.addValueForProcessing(value);
                    } catch (RuntimeException exp) {
                        // if we cannot add the value don't worry about it
                    }
                }
            } else {
                // if the value is not yes, true, no, false, 1 or 0 then don't add the option to the CommandLine
                if ("yes".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value) || "1".equalsIgnoreCase(value) ||
                    "no".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value) || "0".equalsIgnoreCase(value)) {
                    cmd.addOption(opt);
                }
            }
        }
    }
}