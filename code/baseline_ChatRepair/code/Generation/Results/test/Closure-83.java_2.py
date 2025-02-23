public int parseArguments(Parameters params) throws CmdLineException {
  String param = params.getParameter(0);

  // Check if param is null, in that case, treat as a default true
  if (param == null) {
    setter.addValue(true);
    return 0;
  }
  
  // Deal explicitly with empty string by returning 1 but not setting any value
  if (param.isEmpty()) {
    return 1;
  }

  // Convert param to lowercase for case-insensitive comparison
  String lowerParam = param.toLowerCase();

  // Handle recognized boolean strings accordingly
  if (TRUES.contains(lowerParam)) {
    setter.addValue(true);
  } else if (FALSES.contains(lowerParam)) {
    setter.addValue(false);
  } else {
    // Param isn't recognized as an expected input, throw exception
    throw new CmdLineException("Invalid boolean value: " + param);
  }

  return 1;
}
