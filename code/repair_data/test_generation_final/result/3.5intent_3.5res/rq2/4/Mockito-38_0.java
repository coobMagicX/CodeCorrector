private boolean toStringEquals(Matcher m, Object arg) {
    return StringDescription.asString(m).equals(String.valueOf(arg));
}