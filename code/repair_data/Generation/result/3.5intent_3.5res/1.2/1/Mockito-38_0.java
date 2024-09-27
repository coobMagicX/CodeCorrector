private boolean toStringEquals(Matcher m, Object arg) {
    if (arg == null) {
        return StringDescription.toString(m) == null;
    } else {
        return StringDescription.toString(m).equals(arg.toString());
    }
}