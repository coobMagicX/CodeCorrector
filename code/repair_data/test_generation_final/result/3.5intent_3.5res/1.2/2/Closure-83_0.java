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
    } else if ("--version".equals(lowerParam)) { // Check for version flag
      setter.addValue(true);
    } else if ("--version2".equals(lowerParam)) { // Check for version flag 2
      setter.addValue(true);
    } else {
      setter.addValue(true);
      return 0;
    }
    return 1;
  }
}