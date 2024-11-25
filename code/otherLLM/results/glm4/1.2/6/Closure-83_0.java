public int parseArguments(Parameters params) throws CmdLineException {
    String param = params.getParameter(0);

    if (param == null) {
        setter.addValue(true);
        return 0;
    } else {
        String lowerParam = param.toLowerCase();
        
        // Check for the --version flag and throw an exception if present
        if ("--version".equals(lowerParam)) {
            throw new CmdLineException("Version information is not available via command-line arguments.");
        }

        if (TRUES.contains(lowerParam)) {
            setter.addValue(true);
        } else if (FALSES.contains(lowerParam)) {
            setter.addValue(false);
        } else {
            // This should handle any other flags that are not recognized
            throw new CmdLineException("Unrecognized argument: " + param);
        }
    }

    return 1;
}