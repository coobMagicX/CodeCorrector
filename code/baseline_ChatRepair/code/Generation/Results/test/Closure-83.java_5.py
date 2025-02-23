public int parseArguments(Parameters params) throws CmdLineException {
  if (params == null) {
    throw new NullPointerException("Parameters cannot be null");
  }
  
  String param = params.getParameter(0);
  if (param == null || param.isEmpty()) {
    // No action or value added if parameter is null or empty.
    return 0;
  }

  String lowerParam = param.toLowerCase();
  if (TRUES.contains(lowerParam)) {
    setter.addValue(true);
    return 1;
  }