public int parseArguments(Parameters params) throws CmdLineException {
  String param = params.getParameter(0);

  if (param == null) {
    setter.addValue(true);
    return 0; // Return 0 when first parameter is null, as per original intent
  } else {
    String lowerParam = param.toLowerCase();
    if (TRUES.contains(lowerParam)) {
      setter.addValue(true); // Update flag for true values
    } else if (FALSES.contains(lowerParam)) {
      setter.addValue(false); // Update flag for false values
    } else { // Added an else block to return 0 when "--version" is not encountered
      return 0;
    }
    return 1; // Return non-zero value only when the "--version" flag is not encountered and other conditions are met
  }
}