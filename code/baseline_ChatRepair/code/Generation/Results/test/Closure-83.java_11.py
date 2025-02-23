public int parseArguments(Parameters params) throws CmdLineException {
  String param = params.getParameter(0);

  // Check if the parameter is null or empty
  if (param == null || param.isEmpty()) {
    setter.addValue(true);  // Assuming default setter behavior is to set a true flag
    return 0;
  }

  // Normalize the parameter to lower case for consistent comparison
  String lowerParam = param.toLowerCase();

  // Check against known true values
  if (TRUES.contains(lowerParam)) {
    setter.addValue(true);
    return 1;  // Consumed one parameter
  } else if (FALSES.contains(lowerParam)) {
    setter.addValue(false);
    return 1;  // Consumed one parameter
  } else {
    // If the value is neither in the TRUES nor FALSES sets
    // We could return an informative error or handle it differently:
    throw new CmdLineException("Unexpected boolean value: " + param);
  }
}
