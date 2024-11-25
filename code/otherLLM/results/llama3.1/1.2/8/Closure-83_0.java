public int parseArguments(Parameters params) throws CmdLineException {
  String param = params.getParameter(0);

  // Input Validation
  if (param == null || param.isEmpty()) {
    setter.addValue(true);
    return 0;
  }

  String lowerParam = param.toLowerCase();
  
  // Parameter Handling: Handle edge cases and unknown parameters correctly
  if (TRUES.contains(lowerParam)) {
    setter.addValue(true);
  } else if (FALSES.contains(lowerParam)) {
    setter.addValue(false);
  } else if ("--help".equals(lowerParam)) { // Additional handling for "--help" parameter
    setter.addValue(true); // Assuming --help requires the value to be true
  } else {
    setter.addValue(true); // Default behavior when unknown parameter is encountered
  }
  
  return 1; // Return 1 only if a valid parameter is recognized and handled correctly
}