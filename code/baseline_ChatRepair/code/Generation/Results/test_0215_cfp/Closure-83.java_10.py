public int parseArguments(Parameters params) throws CmdLineException {
  String param = params.getParameter(0);

  // Return the default true without advancing positional parameter index
  // when the first parameter is null or empty
  if (param == null || param.isEmpty()) {
    setter.addValue(true);
    return 0;
  }

  // Convert to lowercase to standardize for comparison
  String lowerParam = param.toLowerCase();

  // Handle recognized true or false values
  if (TRUES.contains(lowerParam)) {
    setter.addValue(true);
  } else if (FALSES.contains(lowerParam)) {
    setter.addValue(false);
  } else {
    // For any other input, return the