public static Option create(String opt) throws IllegalArgumentException {
    // reset the OptionBuilder properties
    OptionBuilder.reset();

    // create the option
    Option option = new Option(opt, null);

    // set the option properties
    option.setLongOpt(longopt);
    option.setRequired(required);
    option.setOptionalArg(optionalArg);
    option.setArgs(numberOfArgs);
    option.setType(type);
    option.setValueSeparator(valuesep);
    option.setArgName(argName);

    // return the Option instance
    return option;
}