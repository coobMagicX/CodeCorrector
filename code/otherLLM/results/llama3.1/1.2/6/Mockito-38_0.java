private boolean toStringEquals(Matcher m, Object arg) {
    if (arg == null) { // Handle null argument
        return StringDescription.toString(m).equals("null");
    }
    return StringDescription.toString(m).equals(arg.toString());
}