private boolean toStringEquals(Matcher m, Object arg) {
    if (arg == null || !(arg instanceof String)) {
        return false;
    }
    return StringDescription.toString(m).equals(arg.toString());
}