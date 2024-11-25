public int parseArguments(Parameters params) throws CmdLineException {
    String param = params.getParameter(0);

    if (param == null) {
        setter.addValue(true);
        return 0;
    } else {
        String lowerParam = param.toLowerCase();
        if (TRUES.contains(lowerParam)) {
            setter.addValue(true);
        } else if (FALSES.contains(lowerParam)) {
            setter.addValue(false);
        } else if ("--version".equals(lowerParam)) {
            throw new CmdLineException("Version flag provided. Version: " + getVersion());
        } else {
            // If the parameter is neither a true/false value nor the version flag,
            // it's an invalid argument that should be handled with an exception.
            throw new CmdLineException("Invalid command line argument: " + param);
        }
    }
}