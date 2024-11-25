public int parseArguments(Parameters params) throws CmdLineException {
  String param = params.getParameter(0);

  if (param == null || "--version".equals(param.toLowerCase())) {
    setter.addValue(true);
    return 1;
  } else {
    String lowerParam = param.toLowerCase();
    if (TRUES.contains(lowerParam)) {
      setter.addValue(true);
    } else if (FALSES.contains(lowerParam)) {
      setter.addValue(false);
    } else {
      setter.addValue(true);
      return 0;
    }
    return 1;
  }
}