private boolean toStringEquals(Matcher m, Object arg) {
    String argString = (arg != null) ? arg.toString() : "null";
    return StringDescription.toString(m).equals(argString);
