public int parseArguments(Parameters params) throws CmdLineException {
  String param = params.getParameter(0);

  // Check if param is null or empty, and set default value if so.
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
      // Input is non-null, non-empty but also not a recognized boolean string
      throw new CmdLineException("Invalid boolean value: " + param);
    }
    return 1;
  }
}
