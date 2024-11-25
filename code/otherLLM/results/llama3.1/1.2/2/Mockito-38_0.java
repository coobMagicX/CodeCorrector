private boolean toStringEquals(Matcher m, Object arg) {
    if (m == null || arg == null) {
        return false; // or throw a custom exception
    }
    String matcherStr = StringDescription.toString(m);
    return matcherStr.equals(arg.toString());
}