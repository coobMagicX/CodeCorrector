public int parseArguments(Parameters params) throws CmdLineException {
  String param = params.getParameter(0);

  if (param == null || param.isEmpty()) {  // Check for both null and empty string
    setter.addValue(true);
    return 0;
  } else {
    String lowerParam = param.toLowerCase();
    if (TRUES.contains(lowerParam)) {
      setter.addValue(true);
    } else if (FALSES.contains(lowerParam)) {
      setter.addValue(false);
    } else {
      throw new CmdLineException("Invalid boolean value: " + param); // Handling unexpected values robustly
    }
    return 1;
  }
}
