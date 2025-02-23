public int parseArguments(Parameters params) throws CmdLineException {
    String param = params.getParameter(0);

    // Checks if the parameter is null or empty and assigns true as a default value if so, mirroring previous behavior
    if (param == null || param.isEmpty()) {
        setter.addValue(true);
        return 0;
    } 

    // Processing non-empty parameters
    String lowerParam = param.toLowerCase();
    if (TRUES.contains(lowerParam)) {
        setter.addValue(true);
    } else if (FALSES.contains(lowerParam)) {
        setter.addValue(false);
    } else {
        // If the parameter is not in the TRUES or FALSES set, handles it as true by default
        setter.addValue(true);
        return 0;
    }
    return 1;
}
