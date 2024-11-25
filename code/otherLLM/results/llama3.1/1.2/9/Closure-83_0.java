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
    } else if (UNKNOWNS.contains(lowerParam)) { // Handle unknown parameters
      setter.addValue(true); // or throw an exception, depending on the desired behavior
    }
    return 1;
  }
}