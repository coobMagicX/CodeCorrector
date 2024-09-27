public int parseArguments(Parameters params) throws CmdLineException {
    String param = params.getParameter(0);

    if (param == null) {
        setter.addValue(true);
        return 0;
    } else {
        String lowerParam = param.toLowerCase();
        if (lowerParam.equals("--version")) {
            System.out.println("Version 1.0"); // Assuming version 1.0, modify as necessary
            System.exit(0);
        } else if (TRUES.contains(lowerParam)) {
            setter.addValue(true);
        } else if (FALSES.contains(lowerParam)) {
            setter.addValue(false);
        } else {
            throw new CmdLineException("Invalid argument: " + param);
        }
        return 1;
    }
}