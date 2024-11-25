public int parseArguments(Parameters params) throws CmdLineException {
    String param = params.getParameter(0);

    if (param == null) {
        setter.addValue(true); // Assuming the default value is true for a missing parameter
        return 0;
    } else {
        String lowerParam = param.toLowerCase();
        if ("--version".equals(lowerParam)) {
            // Handle --version flag by setting appropriate behavior, e.g., printing version and returning non-zero to indicate a special case
            System.out.println("Version: " + someVersionString); // Hypothetical call to get version string
            return 1; // Return 1 to indicate that the command was successfully processed but requires further action
        } else if (TRUES.contains(lowerParam)) {
            setter.addValue(true);
        } else if (FALSES.contains(lowerParam)) {
            setter.addValue(false);
        } else {
            setter.addValue(true); // Default to true if not a recognized flag
            return 0; // Return 0 for non-recognized flags, indicating no action was taken
        }
    }
}