public int parseArguments(Parameters params) throws CmdLineException {
    String param = params.getParameter(0);

    // Check for the --version flag
    if ("--version".equals(param)) {
        System.out.println("Version information");
        return 0;
    }

    if (param == null) {
        setter.addValue(true);
        return 0;
    } else {
        String lowerParam = param.toLowerCase();
        if (TRUES.contains(lowerParam)) {
            setter.addValue(true);
        } else if (FALSES.contains(lowerParam)) {
            setter.addValue(false);
        } else {
            // This block is only reached for parameters that are not recognized
            throw new CmdLineException("Unrecognized parameter: " + param);
        }
        return 1;
    }
}