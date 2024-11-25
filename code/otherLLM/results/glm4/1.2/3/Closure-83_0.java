public int parseArguments(Parameters params) throws CmdLineException {
    String param = params.getParameter(0);

    if (param == null || !param.equals("--version")) { // Check if the first parameter is not null and not "--version"
        setter.addValue(true); // This line should be adjusted based on the actual logic needed when a flag is present
        return 0;
    } else {
        // If the flag is "--version", do not add a value to the setter and don't return an error code.
        // Instead, you might want to handle the version display or perform other actions specific to the version flag.
        // For example:
        System.out.println("Version information: ..."); // Replace with actual version information logic
        return 1; // This indicates that a valid command was processed without adding a value to the setter
    }
}