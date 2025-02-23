public int parseArguments(Parameters params) throws CmdLineException {
  String param = params.getParameter(0);

  if (param == null || param.isEmpty()) { // Check for null or empty input
    throw new CmdLineException("Parameter is missing or empty.");
  }

  String lowerParam = param.toLowerCase();
  if (TRUES.contains(lowerParam)) {
    setter.addValue(true);
  } else if (FALSES.contains(lowerParam)) {
    setter.addValue(false);
  } else {
    // Handle unexpected value here. Example: throw exception.
    throw new CmdLineException("Invalid boolean parameter: " + param);
  }

  return 1;
}
