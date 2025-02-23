public int parseArguments(Parameters params) throws CmdLineException {
    // Attempt to retrieve the parameter; if none, default to true
    String param = params.getParameter(0);
    if (param == null) {
        setter.addValue(true);
        return 0;
    }

    // Normalize the string to handle case insensitivity
    String lowerParam = param.toLowerCase();

    // Check for values that clearly indicate true or false
    if (TRUES.contains(lowerParam)) {
        setter.addValue(true);
    } else if (FALSES.contains(lowerParam)) {
        setter.addValue(false);
    } else {
        // For any other values including empty string, default to true
        setter.addValue(true);
        return 0;
    }

    return 1;
}
