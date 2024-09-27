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
    } else if ("--version".equalsIgnoreCase(lowerParam)) {
      setter.addValue(true);
      return 1;
    } else if ("--version2".equalsIgnoreCase(lowerParam)) { // Adding support for a new version flag
      setter.addValue(true);
      return 1;
    } else {
      setter.addValue(true);
      return 0;
    }
    return 1;
  }
}