public int parseArguments(Parameters params) throws CmdLineException {
    String param = params.getParameter(0);

    if (param == null) {
        setter.addValue(true);
        return 0;
    } else {
        String lowerParam = param.toLowerCase();
        if (TRUES.contains(lowerParam)) {
            setter.addValue(true);
            return 1;
        } else if (FALSES.contains(lowerParam)) {
            setter.addValue(false);
            return 1;
        } else {
            throw new CmdLineException("Invalid argument: " + param);
        }
    }
}