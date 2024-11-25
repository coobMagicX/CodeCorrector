public int parseArguments(Parameters params) throws CmdLineException {
  String param = params.getParameter(0);

  if (param == null) {
    setter.addValue(true);
    return 0;
  } else {
    String lowerParam = param.toLowerCase();
    if (TRUES.contains(lowerParam)) {
      setter.addValue(true);
    } else if (FALSES.contains(lowerParam)) {
      setter.addValue(false);
    } else if ("--version".equals(lowerParam)) {
      // Handle "--version" flag specifically
      setter.addValue(true);
    } else {
      // Handle unknown parameters
      System.err.println("Unknown parameter: " + lowerParam);
      return 1;
    }
    return 1; // Return 1 even when parsing is successful
  }
}