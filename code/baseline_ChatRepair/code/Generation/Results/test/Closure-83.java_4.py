public int parseArguments(Parameters params) throws CmdLineException {
  String param = params.getParameter(0);

  if (param == null || param.isEmpty()) {
    // Do not add any value if there's no parameter or if it's empty
    return 0;
  } else {
    String lowerParam = param.toLowerCase();
    // Check against known true values
    if (TRUES.contains(lowerParam)) {
      setter.addValue(true);
      return 1;
    // Check against known false values
    } else if (FALSES.contains(lowerParam)) {
      setter.addValue(false);
      return 1;
    } else {
      // If parameter is neither true nor false, do not add any value
      return 0;
    }
  }
}
