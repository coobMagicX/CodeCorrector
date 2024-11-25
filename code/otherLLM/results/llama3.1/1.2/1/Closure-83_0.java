public int parseArguments(Parameters params) throws CmdLineException {
  String param = params.getParameter(0);

  if (param == null || param.isEmpty()) {
    setter.addValue(true);
    return 0;
  } else {
    String lowerParam = param.toLowerCase();
    if (TRUES.contains(lowerParam)) {
      setter.addValue(true);
    } else if (FALSES.contains(lowerParam)) {
      setter.addValue(false);
    } else {
      // If the parameter is not recognized, set the value to false
      // instead of true as it was before.
      setter.addValue(false);
    }
    return 1;
  }
}