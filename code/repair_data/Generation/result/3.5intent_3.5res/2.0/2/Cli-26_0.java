public static Option create(String opt) throws IllegalArgumentException {
    // create the option
    Option option = new Option(opt, description);

    // set the option properties
    option.setLongOpt(longopt);
    option.setRequired(required);
    option.setOptionalArg(optionalArg);
    option.setArgs(numberOfArgs);
    option.setType(type);
    option.setValueSeparator(valuesep);
    option.setArgName(argName);
    
    // reset the OptionBuilder properties
    reset();

    // return the Option instance
    return option;
}

private static void reset() {
    description = null;
    argName = "arg";
    longopt = null;
    type = null;
    required = false;
    numberOfArgs = Option.UNINITIALIZED;
    optionalArg = false;
    valuesep = (char) 0;
}