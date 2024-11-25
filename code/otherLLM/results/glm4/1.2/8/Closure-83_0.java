public int parseArguments(Parameters params) throws CmdLineException {
    String param = params.getParameter(0);

    if (param == null || "--version".equalsIgnoreCase(param)) {
        setter.addValue(true);
        return 2; // Changed the return value to indicate version flag detection
    } else {
        String lowerParam = param.toLowerCase();
        if (TRUES.contains(lowerParam)) {
            setter.addValue(true);
        } else if (FALSES.contains(lowerParam)) {
            setter.addValue(false);
        } else {
            setter.addValue(true); // Assuming that by default the value should be set to true
            return 1; // Changed the return value to indicate a parameter was processed
        }
    }

    // If no command is recognized, including version flag, throw an exception
    if (lowerParam.equals("--version")) {
        System.out.println("Version Information: " + getVersion());
    } else {
        throw new CmdLineException("Invalid argument: " + param);
    }
}

// Assuming there's a method that gets the application version
private static String getVersion() {
    // This should return the actual version string of your application
    return "1.0.0";
}