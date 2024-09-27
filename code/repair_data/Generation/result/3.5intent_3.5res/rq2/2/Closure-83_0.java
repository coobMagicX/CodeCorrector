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
    } else {
      setter.addValue(true);
      return 0;
    }
    return 1;
  }
}

Methods that you can utilize:
public static void main(String[] args) {
  CommandLineRunner runner = new CommandLineRunner(args);
  if (runner.shouldRunCompiler()) {
    runner.run();
  } else {
    System.exit(-1);
  }
}