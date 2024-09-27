public void validate(final WriteableCommandLine commandLine) throws OptionException {
    // number of options found
    int present = 0;

    // reference to first unexpected option
    Option unexpected = null;

    for (final Iterator i = options.iterator(); i.hasNext();) {
        final Option option = (Option) i.next();

        // Needs validation? Validate if required or it's a group that needs special validation
        boolean validate = option.isRequired() || option instanceof Group;
        if (validate) {
            option.validate(commandLine);
        }

        // Check if the option is present and validate it
        if (commandLine.hasOption(option)) {
            if (++present > maximum) {
                unexpected = option;
                break; // Break as soon as the maximum limit is exceeded
            }

            // Additional validation for file existence if the option requires a file
            if (option instanceof FileOption) {
                FileOption fileOption = (FileOption) option;
                if (!fileOption.getFile().exists()) {
                    throw new OptionException(option, ResourceConstants.FILE_NOT_FOUND, fileOption.getFile().getAbsolutePath());
                }
            }

            option.validate(commandLine);
        }
    }

    // Too many options
    if (unexpected != null) {
        throw new OptionException(this, ResourceConstants.UNEXPECTED_TOKEN, unexpected.getPreferredName());
    }

    // Too few options
    if (present < minimum) {
        throw new OptionException(this, ResourceConstants.MISSING_OPTION);
    }

    // Validate each anonymous argument
    for (final Iterator i = anonymous.iterator(); i.hasNext();) {
        final Option option = (Option) i.next();
        option.validate(commandLine);
    }
}