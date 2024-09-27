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
                boolean shouldAddOption = true;
                if (value != null) {
                    value = value.toLowerCase();
                    shouldAddOption = value.equals("yes") || value.equals("true") || value.equals("1");
                }
                if (shouldAddOption) {
                    cmd.addOption(opt);
                }
            }
        }
    }
}