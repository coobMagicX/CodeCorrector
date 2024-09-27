public int parseArguments(Parameters params) throws CmdLineException {
    String param = params.getParameter(0);

    if (param == null || param.isEmpty()) {
        setter.addValue(true);
        return 0;
    } else {
        String lowerParam = param.toLowerCase();
        if (TRUES.contains(lowerParam) || lowerParam.equals("--version")) {
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